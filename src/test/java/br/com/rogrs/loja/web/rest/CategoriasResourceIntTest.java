package br.com.rogrs.loja.web.rest;

import br.com.rogrs.loja.LojaApp;

import br.com.rogrs.loja.domain.Categorias;
import br.com.rogrs.loja.repository.CategoriasRepository;
import br.com.rogrs.loja.repository.search.CategoriasSearchRepository;
import br.com.rogrs.loja.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CategoriasResource REST controller.
 *
 * @see CategoriasResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LojaApp.class)
public class CategoriasResourceIntTest {

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    @Autowired
    private CategoriasRepository categoriasRepository;

    @Autowired
    private CategoriasSearchRepository categoriasSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCategoriasMockMvc;

    private Categorias categorias;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CategoriasResource categoriasResource = new CategoriasResource(categoriasRepository, categoriasSearchRepository);
        this.restCategoriasMockMvc = MockMvcBuilders.standaloneSetup(categoriasResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Categorias createEntity(EntityManager em) {
        Categorias categorias = new Categorias()
            .descricao(DEFAULT_DESCRICAO);
        return categorias;
    }

    @Before
    public void initTest() {
        categoriasSearchRepository.deleteAll();
        categorias = createEntity(em);
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
        List<Categorias> categoriasList = categoriasRepository.findAll();
        assertThat(categoriasList).hasSize(databaseSizeBeforeCreate + 1);
        Categorias testCategorias = categoriasList.get(categoriasList.size() - 1);
        assertThat(testCategorias.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);

        // Validate the Categorias in Elasticsearch
        Categorias categoriasEs = categoriasSearchRepository.findOne(testCategorias.getId());
        assertThat(categoriasEs).isEqualToComparingFieldByField(testCategorias);
    }

    @Test
    @Transactional
    public void createCategoriasWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = categoriasRepository.findAll().size();

        // Create the Categorias with an existing ID
        categorias.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCategoriasMockMvc.perform(post("/api/categorias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(categorias)))
            .andExpect(status().isBadRequest());

        // Validate the Categorias in the database
        List<Categorias> categoriasList = categoriasRepository.findAll();
        assertThat(categoriasList).hasSize(databaseSizeBeforeCreate);
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

        List<Categorias> categoriasList = categoriasRepository.findAll();
        assertThat(categoriasList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCategorias() throws Exception {
        // Initialize the database
        categoriasRepository.saveAndFlush(categorias);

        // Get all the categoriasList
        restCategoriasMockMvc.perform(get("/api/categorias?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
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
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
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
        Categorias updatedCategorias = categoriasRepository.findOne(categorias.getId());
        updatedCategorias
            .descricao(UPDATED_DESCRICAO);

        restCategoriasMockMvc.perform(put("/api/categorias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCategorias)))
            .andExpect(status().isOk());

        // Validate the Categorias in the database
        List<Categorias> categoriasList = categoriasRepository.findAll();
        assertThat(categoriasList).hasSize(databaseSizeBeforeUpdate);
        Categorias testCategorias = categoriasList.get(categoriasList.size() - 1);
        assertThat(testCategorias.getDescricao()).isEqualTo(UPDATED_DESCRICAO);

        // Validate the Categorias in Elasticsearch
        Categorias categoriasEs = categoriasSearchRepository.findOne(testCategorias.getId());
        assertThat(categoriasEs).isEqualToComparingFieldByField(testCategorias);
    }

    @Test
    @Transactional
    public void updateNonExistingCategorias() throws Exception {
        int databaseSizeBeforeUpdate = categoriasRepository.findAll().size();

        // Create the Categorias

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCategoriasMockMvc.perform(put("/api/categorias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(categorias)))
            .andExpect(status().isCreated());

        // Validate the Categorias in the database
        List<Categorias> categoriasList = categoriasRepository.findAll();
        assertThat(categoriasList).hasSize(databaseSizeBeforeUpdate + 1);
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

        // Validate Elasticsearch is empty
        boolean categoriasExistsInEs = categoriasSearchRepository.exists(categorias.getId());
        assertThat(categoriasExistsInEs).isFalse();

        // Validate the database is empty
        List<Categorias> categoriasList = categoriasRepository.findAll();
        assertThat(categoriasList).hasSize(databaseSizeBeforeDelete - 1);
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
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categorias.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Categorias.class);
        Categorias categorias1 = new Categorias();
        categorias1.setId(1L);
        Categorias categorias2 = new Categorias();
        categorias2.setId(categorias1.getId());
        assertThat(categorias1).isEqualTo(categorias2);
        categorias2.setId(2L);
        assertThat(categorias1).isNotEqualTo(categorias2);
        categorias1.setId(null);
        assertThat(categorias1).isNotEqualTo(categorias2);
    }
}
