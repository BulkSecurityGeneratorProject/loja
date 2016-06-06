package br.com.rogrs.loja.web.rest;

import br.com.rogrs.loja.LojaApp;
import br.com.rogrs.loja.domain.Categorias;
import br.com.rogrs.loja.repository.CategoriasRepository;
import br.com.rogrs.loja.repository.search.CategoriasSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the CategoriasResource REST controller.
 *
 * @see CategoriasResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = LojaApp.class)
@WebAppConfiguration
@IntegrationTest
public class CategoriasResourceIntTest {

    private static final String DEFAULT_DESCRICAO = "AAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBB";

    @Inject
    private CategoriasRepository categoriasRepository;

    @Inject
    private CategoriasSearchRepository categoriasSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCategoriasMockMvc;

    private Categorias categorias;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CategoriasResource categoriasResource = new CategoriasResource();
        ReflectionTestUtils.setField(categoriasResource, "categoriasSearchRepository", categoriasSearchRepository);
        ReflectionTestUtils.setField(categoriasResource, "categoriasRepository", categoriasRepository);
        this.restCategoriasMockMvc = MockMvcBuilders.standaloneSetup(categoriasResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        categoriasSearchRepository.deleteAll();
        categorias = new Categorias();
        categorias.setDescricao(DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    public void createCategorias() throws Exception {
        int databaseSizeBeforeCreate = categoriasRepository.findAll().size();

        // Create the Categorias

        restCategoriasMockMvc.perform(post("/api/categorias")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(categorias)))
                .andExpect(status().isCreated());

        // Validate the Categorias in the database
        List<Categorias> categorias = categoriasRepository.findAll();
        assertThat(categorias).hasSize(databaseSizeBeforeCreate + 1);
        Categorias testCategorias = categorias.get(categorias.size() - 1);
        assertThat(testCategorias.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);

        // Validate the Categorias in ElasticSearch
        Categorias categoriasEs = categoriasSearchRepository.findOne(testCategorias.getId());
        assertThat(categoriasEs).isEqualToComparingFieldByField(testCategorias);
    }

    @Test
    @Transactional
    public void checkDescricaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = categoriasRepository.findAll().size();
        // set the field null
        categorias.setDescricao(null);

        // Create the Categorias, which fails.

        restCategoriasMockMvc.perform(post("/api/categorias")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(categorias)))
                .andExpect(status().isBadRequest());

        List<Categorias> categorias = categoriasRepository.findAll();
        assertThat(categorias).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCategorias() throws Exception {
        // Initialize the database
        categoriasRepository.saveAndFlush(categorias);

        // Get all the categorias
        restCategoriasMockMvc.perform(get("/api/categorias?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(categorias.getId().intValue())))
                .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())));
    }

    @Test
    @Transactional
    public void getCategorias() throws Exception {
        // Initialize the database
        categoriasRepository.saveAndFlush(categorias);

        // Get the categorias
        restCategoriasMockMvc.perform(get("/api/categorias/{id}", categorias.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(categorias.getId().intValue()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCategorias() throws Exception {
        // Get the categorias
        restCategoriasMockMvc.perform(get("/api/categorias/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCategorias() throws Exception {
        // Initialize the database
        categoriasRepository.saveAndFlush(categorias);
        categoriasSearchRepository.save(categorias);
        int databaseSizeBeforeUpdate = categoriasRepository.findAll().size();

        // Update the categorias
        Categorias updatedCategorias = new Categorias();
        updatedCategorias.setId(categorias.getId());
        updatedCategorias.setDescricao(UPDATED_DESCRICAO);

        restCategoriasMockMvc.perform(put("/api/categorias")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCategorias)))
                .andExpect(status().isOk());

        // Validate the Categorias in the database
        List<Categorias> categorias = categoriasRepository.findAll();
        assertThat(categorias).hasSize(databaseSizeBeforeUpdate);
        Categorias testCategorias = categorias.get(categorias.size() - 1);
        assertThat(testCategorias.getDescricao()).isEqualTo(UPDATED_DESCRICAO);

        // Validate the Categorias in ElasticSearch
        Categorias categoriasEs = categoriasSearchRepository.findOne(testCategorias.getId());
        assertThat(categoriasEs).isEqualToComparingFieldByField(testCategorias);
    }

    @Test
    @Transactional
    public void deleteCategorias() throws Exception {
        // Initialize the database
        categoriasRepository.saveAndFlush(categorias);
        categoriasSearchRepository.save(categorias);
        int databaseSizeBeforeDelete = categoriasRepository.findAll().size();

        // Get the categorias
        restCategoriasMockMvc.perform(delete("/api/categorias/{id}", categorias.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean categoriasExistsInEs = categoriasSearchRepository.exists(categorias.getId());
        assertThat(categoriasExistsInEs).isFalse();

        // Validate the database is empty
        List<Categorias> categorias = categoriasRepository.findAll();
        assertThat(categorias).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCategorias() throws Exception {
        // Initialize the database
        categoriasRepository.saveAndFlush(categorias);
        categoriasSearchRepository.save(categorias);

        // Search the categorias
        restCategoriasMockMvc.perform(get("/api/_search/categorias?query=id:" + categorias.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categorias.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())));
    }
}
