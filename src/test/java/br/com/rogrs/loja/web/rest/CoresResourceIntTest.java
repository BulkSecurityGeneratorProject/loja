package br.com.rogrs.loja.web.rest;

import br.com.rogrs.loja.LojaApp;
import br.com.rogrs.loja.domain.Cores;
import br.com.rogrs.loja.repository.CoresRepository;
import br.com.rogrs.loja.repository.search.CoresSearchRepository;

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
 * Test class for the CoresResource REST controller.
 *
 * @see CoresResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = LojaApp.class)
@WebAppConfiguration
@IntegrationTest
public class CoresResourceIntTest {

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private CoresRepository coresRepository;

    @Inject
    private CoresSearchRepository coresSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCoresMockMvc;

    private Cores cores;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CoresResource coresResource = new CoresResource();
        ReflectionTestUtils.setField(coresResource, "coresSearchRepository", coresSearchRepository);
        ReflectionTestUtils.setField(coresResource, "coresRepository", coresRepository);
        this.restCoresMockMvc = MockMvcBuilders.standaloneSetup(coresResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        coresSearchRepository.deleteAll();
        cores = new Cores();
        cores.setDescricao(DEFAULT_DESCRICAO);
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
        List<Cores> cores = coresRepository.findAll();
        assertThat(cores).hasSize(databaseSizeBeforeCreate + 1);
        Cores testCores = cores.get(cores.size() - 1);
        assertThat(testCores.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);

        // Validate the Cores in ElasticSearch
        Cores coresEs = coresSearchRepository.findOne(testCores.getId());
        assertThat(coresEs).isEqualToComparingFieldByField(testCores);
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

        List<Cores> cores = coresRepository.findAll();
        assertThat(cores).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCores() throws Exception {
        // Initialize the database
        coresRepository.saveAndFlush(cores);

        // Get all the cores
        restCoresMockMvc.perform(get("/api/cores?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
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
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
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
        Cores updatedCores = new Cores();
        updatedCores.setId(cores.getId());
        updatedCores.setDescricao(UPDATED_DESCRICAO);

        restCoresMockMvc.perform(put("/api/cores")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCores)))
                .andExpect(status().isOk());

        // Validate the Cores in the database
        List<Cores> cores = coresRepository.findAll();
        assertThat(cores).hasSize(databaseSizeBeforeUpdate);
        Cores testCores = cores.get(cores.size() - 1);
        assertThat(testCores.getDescricao()).isEqualTo(UPDATED_DESCRICAO);

        // Validate the Cores in ElasticSearch
        Cores coresEs = coresSearchRepository.findOne(testCores.getId());
        assertThat(coresEs).isEqualToComparingFieldByField(testCores);
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

        // Validate ElasticSearch is empty
        boolean coresExistsInEs = coresSearchRepository.exists(cores.getId());
        assertThat(coresExistsInEs).isFalse();

        // Validate the database is empty
        List<Cores> cores = coresRepository.findAll();
        assertThat(cores).hasSize(databaseSizeBeforeDelete - 1);
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
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cores.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())));
    }
}
