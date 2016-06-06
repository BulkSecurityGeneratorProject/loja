package br.com.rogrs.loja.web.rest;

import br.com.rogrs.loja.LojaApp;
import br.com.rogrs.loja.domain.Marcas;
import br.com.rogrs.loja.repository.MarcasRepository;
import br.com.rogrs.loja.repository.search.MarcasSearchRepository;

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
 * Test class for the MarcasResource REST controller.
 *
 * @see MarcasResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = LojaApp.class)
@WebAppConfiguration
@IntegrationTest
public class MarcasResourceIntTest {

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private MarcasRepository marcasRepository;

    @Inject
    private MarcasSearchRepository marcasSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMarcasMockMvc;

    private Marcas marcas;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MarcasResource marcasResource = new MarcasResource();
        ReflectionTestUtils.setField(marcasResource, "marcasSearchRepository", marcasSearchRepository);
        ReflectionTestUtils.setField(marcasResource, "marcasRepository", marcasRepository);
        this.restMarcasMockMvc = MockMvcBuilders.standaloneSetup(marcasResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        marcasSearchRepository.deleteAll();
        marcas = new Marcas();
        marcas.setDescricao(DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    public void createMarcas() throws Exception {
        int databaseSizeBeforeCreate = marcasRepository.findAll().size();

        // Create the Marcas

        restMarcasMockMvc.perform(post("/api/marcas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(marcas)))
                .andExpect(status().isCreated());

        // Validate the Marcas in the database
        List<Marcas> marcas = marcasRepository.findAll();
        assertThat(marcas).hasSize(databaseSizeBeforeCreate + 1);
        Marcas testMarcas = marcas.get(marcas.size() - 1);
        assertThat(testMarcas.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);

        // Validate the Marcas in ElasticSearch
        Marcas marcasEs = marcasSearchRepository.findOne(testMarcas.getId());
        assertThat(marcasEs).isEqualToComparingFieldByField(testMarcas);
    }

    @Test
    @Transactional
    public void checkDescricaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = marcasRepository.findAll().size();
        // set the field null
        marcas.setDescricao(null);

        // Create the Marcas, which fails.

        restMarcasMockMvc.perform(post("/api/marcas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(marcas)))
                .andExpect(status().isBadRequest());

        List<Marcas> marcas = marcasRepository.findAll();
        assertThat(marcas).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMarcas() throws Exception {
        // Initialize the database
        marcasRepository.saveAndFlush(marcas);

        // Get all the marcas
        restMarcasMockMvc.perform(get("/api/marcas?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(marcas.getId().intValue())))
                .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())));
    }

    @Test
    @Transactional
    public void getMarcas() throws Exception {
        // Initialize the database
        marcasRepository.saveAndFlush(marcas);

        // Get the marcas
        restMarcasMockMvc.perform(get("/api/marcas/{id}", marcas.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(marcas.getId().intValue()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMarcas() throws Exception {
        // Get the marcas
        restMarcasMockMvc.perform(get("/api/marcas/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMarcas() throws Exception {
        // Initialize the database
        marcasRepository.saveAndFlush(marcas);
        marcasSearchRepository.save(marcas);
        int databaseSizeBeforeUpdate = marcasRepository.findAll().size();

        // Update the marcas
        Marcas updatedMarcas = new Marcas();
        updatedMarcas.setId(marcas.getId());
        updatedMarcas.setDescricao(UPDATED_DESCRICAO);

        restMarcasMockMvc.perform(put("/api/marcas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedMarcas)))
                .andExpect(status().isOk());

        // Validate the Marcas in the database
        List<Marcas> marcas = marcasRepository.findAll();
        assertThat(marcas).hasSize(databaseSizeBeforeUpdate);
        Marcas testMarcas = marcas.get(marcas.size() - 1);
        assertThat(testMarcas.getDescricao()).isEqualTo(UPDATED_DESCRICAO);

        // Validate the Marcas in ElasticSearch
        Marcas marcasEs = marcasSearchRepository.findOne(testMarcas.getId());
        assertThat(marcasEs).isEqualToComparingFieldByField(testMarcas);
    }

    @Test
    @Transactional
    public void deleteMarcas() throws Exception {
        // Initialize the database
        marcasRepository.saveAndFlush(marcas);
        marcasSearchRepository.save(marcas);
        int databaseSizeBeforeDelete = marcasRepository.findAll().size();

        // Get the marcas
        restMarcasMockMvc.perform(delete("/api/marcas/{id}", marcas.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean marcasExistsInEs = marcasSearchRepository.exists(marcas.getId());
        assertThat(marcasExistsInEs).isFalse();

        // Validate the database is empty
        List<Marcas> marcas = marcasRepository.findAll();
        assertThat(marcas).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMarcas() throws Exception {
        // Initialize the database
        marcasRepository.saveAndFlush(marcas);
        marcasSearchRepository.save(marcas);

        // Search the marcas
        restMarcasMockMvc.perform(get("/api/_search/marcas?query=id:" + marcas.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(marcas.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())));
    }
}
