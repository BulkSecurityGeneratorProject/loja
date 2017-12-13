package br.com.rogrs.loja.web.rest;

import br.com.rogrs.loja.LojaApp;

import br.com.rogrs.loja.domain.Cores;
import br.com.rogrs.loja.repository.CoresRepository;
import br.com.rogrs.loja.repository.search.CoresSearchRepository;
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

import static br.com.rogrs.loja.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CoresResource REST controller.
 *
 * @see CoresResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LojaApp.class)
public class CoresResourceIntTest {

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    @Autowired
    private CoresRepository coresRepository;

    @Autowired
    private CoresSearchRepository coresSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCoresMockMvc;

    private Cores cores;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CoresResource coresResource = new CoresResource(coresRepository, coresSearchRepository);
        this.restCoresMockMvc = MockMvcBuilders.standaloneSetup(coresResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cores createEntity(EntityManager em) {
        Cores cores = new Cores()
            .descricao(DEFAULT_DESCRICAO);
        return cores;
    }

    @Before
    public void initTest() {
        coresSearchRepository.deleteAll();
        cores = createEntity(em);
    }

    @Test
    @Transactional
    public void createCores() throws Exception {
        int databaseSizeBeforeCreate = coresRepository.findAll().size();

        // Create the Cores
        restCoresMockMvc.perform(post("/api/cores")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cores)))
            .andExpect(status().isCreated());

        // Validate the Cores in the database
        List<Cores> coresList = coresRepository.findAll();
        assertThat(coresList).hasSize(databaseSizeBeforeCreate + 1);
        Cores testCores = coresList.get(coresList.size() - 1);
        assertThat(testCores.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);

        // Validate the Cores in Elasticsearch
        Cores coresEs = coresSearchRepository.findOne(testCores.getId());
        assertThat(coresEs).isEqualToIgnoringGivenFields(testCores);
    }

    @Test
    @Transactional
    public void createCoresWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = coresRepository.findAll().size();

        // Create the Cores with an existing ID
        cores.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCoresMockMvc.perform(post("/api/cores")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cores)))
            .andExpect(status().isBadRequest());

        // Validate the Cores in the database
        List<Cores> coresList = coresRepository.findAll();
        assertThat(coresList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDescricaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = coresRepository.findAll().size();
        // set the field null
        cores.setDescricao(null);

        // Create the Cores, which fails.

        restCoresMockMvc.perform(post("/api/cores")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cores)))
            .andExpect(status().isBadRequest());

        List<Cores> coresList = coresRepository.findAll();
        assertThat(coresList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCores() throws Exception {
        // Initialize the database
        coresRepository.saveAndFlush(cores);

        // Get all the coresList
        restCoresMockMvc.perform(get("/api/cores?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cores.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())));
    }

    @Test
    @Transactional
    public void getCores() throws Exception {
        // Initialize the database
        coresRepository.saveAndFlush(cores);

        // Get the cores
        restCoresMockMvc.perform(get("/api/cores/{id}", cores.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cores.getId().intValue()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCores() throws Exception {
        // Get the cores
        restCoresMockMvc.perform(get("/api/cores/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCores() throws Exception {
        // Initialize the database
        coresRepository.saveAndFlush(cores);
        coresSearchRepository.save(cores);
        int databaseSizeBeforeUpdate = coresRepository.findAll().size();

        // Update the cores
        Cores updatedCores = coresRepository.findOne(cores.getId());
        // Disconnect from session so that the updates on updatedCores are not directly saved in db
        em.detach(updatedCores);
        updatedCores
            .descricao(UPDATED_DESCRICAO);

        restCoresMockMvc.perform(put("/api/cores")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCores)))
            .andExpect(status().isOk());

        // Validate the Cores in the database
        List<Cores> coresList = coresRepository.findAll();
        assertThat(coresList).hasSize(databaseSizeBeforeUpdate);
        Cores testCores = coresList.get(coresList.size() - 1);
        assertThat(testCores.getDescricao()).isEqualTo(UPDATED_DESCRICAO);

        // Validate the Cores in Elasticsearch
        Cores coresEs = coresSearchRepository.findOne(testCores.getId());
        assertThat(coresEs).isEqualToIgnoringGivenFields(testCores);
    }

    @Test
    @Transactional
    public void updateNonExistingCores() throws Exception {
        int databaseSizeBeforeUpdate = coresRepository.findAll().size();

        // Create the Cores

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCoresMockMvc.perform(put("/api/cores")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cores)))
            .andExpect(status().isCreated());

        // Validate the Cores in the database
        List<Cores> coresList = coresRepository.findAll();
        assertThat(coresList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCores() throws Exception {
        // Initialize the database
        coresRepository.saveAndFlush(cores);
        coresSearchRepository.save(cores);
        int databaseSizeBeforeDelete = coresRepository.findAll().size();

        // Get the cores
        restCoresMockMvc.perform(delete("/api/cores/{id}", cores.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean coresExistsInEs = coresSearchRepository.exists(cores.getId());
        assertThat(coresExistsInEs).isFalse();

        // Validate the database is empty
        List<Cores> coresList = coresRepository.findAll();
        assertThat(coresList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCores() throws Exception {
        // Initialize the database
        coresRepository.saveAndFlush(cores);
        coresSearchRepository.save(cores);

        // Search the cores
        restCoresMockMvc.perform(get("/api/_search/cores?query=id:" + cores.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cores.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cores.class);
        Cores cores1 = new Cores();
        cores1.setId(1L);
        Cores cores2 = new Cores();
        cores2.setId(cores1.getId());
        assertThat(cores1).isEqualTo(cores2);
        cores2.setId(2L);
        assertThat(cores1).isNotEqualTo(cores2);
        cores1.setId(null);
        assertThat(cores1).isNotEqualTo(cores2);
    }
}
