package br.com.rogrs.loja.web.rest;

import br.com.rogrs.loja.LojaApp;

import br.com.rogrs.loja.domain.Marcas;
import br.com.rogrs.loja.repository.MarcasRepository;
import br.com.rogrs.loja.repository.search.MarcasSearchRepository;
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
 * Test class for the MarcasResource REST controller.
 *
 * @see MarcasResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LojaApp.class)
public class MarcasResourceIntTest {

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    @Autowired
    private MarcasRepository marcasRepository;

    @Autowired
    private MarcasSearchRepository marcasSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMarcasMockMvc;

    private Marcas marcas;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MarcasResource marcasResource = new MarcasResource(marcasRepository, marcasSearchRepository);
        this.restMarcasMockMvc = MockMvcBuilders.standaloneSetup(marcasResource)
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
    public static Marcas createEntity(EntityManager em) {
        Marcas marcas = new Marcas()
            .descricao(DEFAULT_DESCRICAO);
        return marcas;
    }

    @Before
    public void initTest() {
        marcasSearchRepository.deleteAll();
        marcas = createEntity(em);
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
        List<Marcas> marcasList = marcasRepository.findAll();
        assertThat(marcasList).hasSize(databaseSizeBeforeCreate + 1);
        Marcas testMarcas = marcasList.get(marcasList.size() - 1);
        assertThat(testMarcas.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);

        // Validate the Marcas in Elasticsearch
        Marcas marcasEs = marcasSearchRepository.findOne(testMarcas.getId());
        assertThat(marcasEs).isEqualToComparingFieldByField(testMarcas);
    }

    @Test
    @Transactional
    public void createMarcasWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = marcasRepository.findAll().size();

        // Create the Marcas with an existing ID
        marcas.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMarcasMockMvc.perform(post("/api/marcas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(marcas)))
            .andExpect(status().isBadRequest());

        // Validate the Marcas in the database
        List<Marcas> marcasList = marcasRepository.findAll();
        assertThat(marcasList).hasSize(databaseSizeBeforeCreate);
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

        List<Marcas> marcasList = marcasRepository.findAll();
        assertThat(marcasList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMarcas() throws Exception {
        // Initialize the database
        marcasRepository.saveAndFlush(marcas);

        // Get all the marcasList
        restMarcasMockMvc.perform(get("/api/marcas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
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
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
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
        Marcas updatedMarcas = marcasRepository.findOne(marcas.getId());
        updatedMarcas
            .descricao(UPDATED_DESCRICAO);

        restMarcasMockMvc.perform(put("/api/marcas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMarcas)))
            .andExpect(status().isOk());

        // Validate the Marcas in the database
        List<Marcas> marcasList = marcasRepository.findAll();
        assertThat(marcasList).hasSize(databaseSizeBeforeUpdate);
        Marcas testMarcas = marcasList.get(marcasList.size() - 1);
        assertThat(testMarcas.getDescricao()).isEqualTo(UPDATED_DESCRICAO);

        // Validate the Marcas in Elasticsearch
        Marcas marcasEs = marcasSearchRepository.findOne(testMarcas.getId());
        assertThat(marcasEs).isEqualToComparingFieldByField(testMarcas);
    }

    @Test
    @Transactional
    public void updateNonExistingMarcas() throws Exception {
        int databaseSizeBeforeUpdate = marcasRepository.findAll().size();

        // Create the Marcas

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMarcasMockMvc.perform(put("/api/marcas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(marcas)))
            .andExpect(status().isCreated());

        // Validate the Marcas in the database
        List<Marcas> marcasList = marcasRepository.findAll();
        assertThat(marcasList).hasSize(databaseSizeBeforeUpdate + 1);
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

        // Validate Elasticsearch is empty
        boolean marcasExistsInEs = marcasSearchRepository.exists(marcas.getId());
        assertThat(marcasExistsInEs).isFalse();

        // Validate the database is empty
        List<Marcas> marcasList = marcasRepository.findAll();
        assertThat(marcasList).hasSize(databaseSizeBeforeDelete - 1);
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
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(marcas.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Marcas.class);
        Marcas marcas1 = new Marcas();
        marcas1.setId(1L);
        Marcas marcas2 = new Marcas();
        marcas2.setId(marcas1.getId());
        assertThat(marcas1).isEqualTo(marcas2);
        marcas2.setId(2L);
        assertThat(marcas1).isNotEqualTo(marcas2);
        marcas1.setId(null);
        assertThat(marcas1).isNotEqualTo(marcas2);
    }
}
