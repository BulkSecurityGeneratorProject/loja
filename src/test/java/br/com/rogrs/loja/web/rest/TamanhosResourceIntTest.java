package br.com.rogrs.loja.web.rest;

import br.com.rogrs.loja.LojaApp;
import br.com.rogrs.loja.domain.Tamanhos;
import br.com.rogrs.loja.repository.TamanhosRepository;
import br.com.rogrs.loja.repository.search.TamanhosSearchRepository;

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
 * Test class for the TamanhosResource REST controller.
 *
 * @see TamanhosResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = LojaApp.class)
@WebAppConfiguration
@IntegrationTest
public class TamanhosResourceIntTest {

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private TamanhosRepository tamanhosRepository;

    @Inject
    private TamanhosSearchRepository tamanhosSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTamanhosMockMvc;

    private Tamanhos tamanhos;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TamanhosResource tamanhosResource = new TamanhosResource();
        ReflectionTestUtils.setField(tamanhosResource, "tamanhosSearchRepository", tamanhosSearchRepository);
        ReflectionTestUtils.setField(tamanhosResource, "tamanhosRepository", tamanhosRepository);
        this.restTamanhosMockMvc = MockMvcBuilders.standaloneSetup(tamanhosResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        tamanhosSearchRepository.deleteAll();
        tamanhos = new Tamanhos();
        tamanhos.setDescricao(DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    public void createTamanhos() throws Exception {
        int databaseSizeBeforeCreate = tamanhosRepository.findAll().size();

        // Create the Tamanhos

        restTamanhosMockMvc.perform(post("/api/tamanhos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tamanhos)))
                .andExpect(status().isCreated());

        // Validate the Tamanhos in the database
        List<Tamanhos> tamanhos = tamanhosRepository.findAll();
        assertThat(tamanhos).hasSize(databaseSizeBeforeCreate + 1);
        Tamanhos testTamanhos = tamanhos.get(tamanhos.size() - 1);
        assertThat(testTamanhos.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);

        // Validate the Tamanhos in ElasticSearch
        Tamanhos tamanhosEs = tamanhosSearchRepository.findOne(testTamanhos.getId());
        assertThat(tamanhosEs).isEqualToComparingFieldByField(testTamanhos);
    }

    @Test
    @Transactional
    public void checkDescricaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = tamanhosRepository.findAll().size();
        // set the field null
        tamanhos.setDescricao(null);

        // Create the Tamanhos, which fails.

        restTamanhosMockMvc.perform(post("/api/tamanhos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tamanhos)))
                .andExpect(status().isBadRequest());

        List<Tamanhos> tamanhos = tamanhosRepository.findAll();
        assertThat(tamanhos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTamanhos() throws Exception {
        // Initialize the database
        tamanhosRepository.saveAndFlush(tamanhos);

        // Get all the tamanhos
        restTamanhosMockMvc.perform(get("/api/tamanhos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(tamanhos.getId().intValue())))
                .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())));
    }

    @Test
    @Transactional
    public void getTamanhos() throws Exception {
        // Initialize the database
        tamanhosRepository.saveAndFlush(tamanhos);

        // Get the tamanhos
        restTamanhosMockMvc.perform(get("/api/tamanhos/{id}", tamanhos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(tamanhos.getId().intValue()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTamanhos() throws Exception {
        // Get the tamanhos
        restTamanhosMockMvc.perform(get("/api/tamanhos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTamanhos() throws Exception {
        // Initialize the database
        tamanhosRepository.saveAndFlush(tamanhos);
        tamanhosSearchRepository.save(tamanhos);
        int databaseSizeBeforeUpdate = tamanhosRepository.findAll().size();

        // Update the tamanhos
        Tamanhos updatedTamanhos = new Tamanhos();
        updatedTamanhos.setId(tamanhos.getId());
        updatedTamanhos.setDescricao(UPDATED_DESCRICAO);

        restTamanhosMockMvc.perform(put("/api/tamanhos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTamanhos)))
                .andExpect(status().isOk());

        // Validate the Tamanhos in the database
        List<Tamanhos> tamanhos = tamanhosRepository.findAll();
        assertThat(tamanhos).hasSize(databaseSizeBeforeUpdate);
        Tamanhos testTamanhos = tamanhos.get(tamanhos.size() - 1);
        assertThat(testTamanhos.getDescricao()).isEqualTo(UPDATED_DESCRICAO);

        // Validate the Tamanhos in ElasticSearch
        Tamanhos tamanhosEs = tamanhosSearchRepository.findOne(testTamanhos.getId());
        assertThat(tamanhosEs).isEqualToComparingFieldByField(testTamanhos);
    }

    @Test
    @Transactional
    public void deleteTamanhos() throws Exception {
        // Initialize the database
        tamanhosRepository.saveAndFlush(tamanhos);
        tamanhosSearchRepository.save(tamanhos);
        int databaseSizeBeforeDelete = tamanhosRepository.findAll().size();

        // Get the tamanhos
        restTamanhosMockMvc.perform(delete("/api/tamanhos/{id}", tamanhos.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean tamanhosExistsInEs = tamanhosSearchRepository.exists(tamanhos.getId());
        assertThat(tamanhosExistsInEs).isFalse();

        // Validate the database is empty
        List<Tamanhos> tamanhos = tamanhosRepository.findAll();
        assertThat(tamanhos).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTamanhos() throws Exception {
        // Initialize the database
        tamanhosRepository.saveAndFlush(tamanhos);
        tamanhosSearchRepository.save(tamanhos);

        // Search the tamanhos
        restTamanhosMockMvc.perform(get("/api/_search/tamanhos?query=id:" + tamanhos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tamanhos.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())));
    }
}
