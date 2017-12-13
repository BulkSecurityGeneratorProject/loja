package br.com.rogrs.loja.web.rest;

import br.com.rogrs.loja.LojaApp;

import br.com.rogrs.loja.domain.Tamanhos;
import br.com.rogrs.loja.repository.TamanhosRepository;
import br.com.rogrs.loja.repository.search.TamanhosSearchRepository;
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
 * Test class for the TamanhosResource REST controller.
 *
 * @see TamanhosResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LojaApp.class)
public class TamanhosResourceIntTest {

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    @Autowired
    private TamanhosRepository tamanhosRepository;

    @Autowired
    private TamanhosSearchRepository tamanhosSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTamanhosMockMvc;

    private Tamanhos tamanhos;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TamanhosResource tamanhosResource = new TamanhosResource(tamanhosRepository, tamanhosSearchRepository);
        this.restTamanhosMockMvc = MockMvcBuilders.standaloneSetup(tamanhosResource)
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
    public static Tamanhos createEntity(EntityManager em) {
        Tamanhos tamanhos = new Tamanhos()
            .descricao(DEFAULT_DESCRICAO);
        return tamanhos;
    }

    @Before
    public void initTest() {
        tamanhosSearchRepository.deleteAll();
        tamanhos = createEntity(em);
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
        List<Tamanhos> tamanhosList = tamanhosRepository.findAll();
        assertThat(tamanhosList).hasSize(databaseSizeBeforeCreate + 1);
        Tamanhos testTamanhos = tamanhosList.get(tamanhosList.size() - 1);
        assertThat(testTamanhos.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);

        // Validate the Tamanhos in Elasticsearch
        Tamanhos tamanhosEs = tamanhosSearchRepository.findOne(testTamanhos.getId());
        assertThat(tamanhosEs).isEqualToIgnoringGivenFields(testTamanhos);
    }

    @Test
    @Transactional
    public void createTamanhosWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tamanhosRepository.findAll().size();

        // Create the Tamanhos with an existing ID
        tamanhos.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTamanhosMockMvc.perform(post("/api/tamanhos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tamanhos)))
            .andExpect(status().isBadRequest());

        // Validate the Tamanhos in the database
        List<Tamanhos> tamanhosList = tamanhosRepository.findAll();
        assertThat(tamanhosList).hasSize(databaseSizeBeforeCreate);
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

        List<Tamanhos> tamanhosList = tamanhosRepository.findAll();
        assertThat(tamanhosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTamanhos() throws Exception {
        // Initialize the database
        tamanhosRepository.saveAndFlush(tamanhos);

        // Get all the tamanhosList
        restTamanhosMockMvc.perform(get("/api/tamanhos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
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
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
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
        Tamanhos updatedTamanhos = tamanhosRepository.findOne(tamanhos.getId());
        // Disconnect from session so that the updates on updatedTamanhos are not directly saved in db
        em.detach(updatedTamanhos);
        updatedTamanhos
            .descricao(UPDATED_DESCRICAO);

        restTamanhosMockMvc.perform(put("/api/tamanhos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTamanhos)))
            .andExpect(status().isOk());

        // Validate the Tamanhos in the database
        List<Tamanhos> tamanhosList = tamanhosRepository.findAll();
        assertThat(tamanhosList).hasSize(databaseSizeBeforeUpdate);
        Tamanhos testTamanhos = tamanhosList.get(tamanhosList.size() - 1);
        assertThat(testTamanhos.getDescricao()).isEqualTo(UPDATED_DESCRICAO);

        // Validate the Tamanhos in Elasticsearch
        Tamanhos tamanhosEs = tamanhosSearchRepository.findOne(testTamanhos.getId());
        assertThat(tamanhosEs).isEqualToIgnoringGivenFields(testTamanhos);
    }

    @Test
    @Transactional
    public void updateNonExistingTamanhos() throws Exception {
        int databaseSizeBeforeUpdate = tamanhosRepository.findAll().size();

        // Create the Tamanhos

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTamanhosMockMvc.perform(put("/api/tamanhos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tamanhos)))
            .andExpect(status().isCreated());

        // Validate the Tamanhos in the database
        List<Tamanhos> tamanhosList = tamanhosRepository.findAll();
        assertThat(tamanhosList).hasSize(databaseSizeBeforeUpdate + 1);
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

        // Validate Elasticsearch is empty
        boolean tamanhosExistsInEs = tamanhosSearchRepository.exists(tamanhos.getId());
        assertThat(tamanhosExistsInEs).isFalse();

        // Validate the database is empty
        List<Tamanhos> tamanhosList = tamanhosRepository.findAll();
        assertThat(tamanhosList).hasSize(databaseSizeBeforeDelete - 1);
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
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tamanhos.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tamanhos.class);
        Tamanhos tamanhos1 = new Tamanhos();
        tamanhos1.setId(1L);
        Tamanhos tamanhos2 = new Tamanhos();
        tamanhos2.setId(tamanhos1.getId());
        assertThat(tamanhos1).isEqualTo(tamanhos2);
        tamanhos2.setId(2L);
        assertThat(tamanhos1).isNotEqualTo(tamanhos2);
        tamanhos1.setId(null);
        assertThat(tamanhos1).isNotEqualTo(tamanhos2);
    }
}
