package br.com.rogrs.loja.web.rest;

import br.com.rogrs.loja.LojaApp;

import br.com.rogrs.loja.domain.CadastrosLocalidades;
import br.com.rogrs.loja.repository.CadastrosLocalidadesRepository;
import br.com.rogrs.loja.repository.search.CadastrosLocalidadesSearchRepository;
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
 * Test class for the CadastrosLocalidadesResource REST controller.
 *
 * @see CadastrosLocalidadesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LojaApp.class)
public class CadastrosLocalidadesResourceIntTest {

    private static final String DEFAULT_NUMERO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO = "BBBBBBBBBB";

    private static final String DEFAULT_COMPLEMENTO = "AAAAAAAAAA";
    private static final String UPDATED_COMPLEMENTO = "BBBBBBBBBB";

    private static final String DEFAULT_REFERENCIAS = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCIAS = "BBBBBBBBBB";

    @Autowired
    private CadastrosLocalidadesRepository cadastrosLocalidadesRepository;

    @Autowired
    private CadastrosLocalidadesSearchRepository cadastrosLocalidadesSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCadastrosLocalidadesMockMvc;

    private CadastrosLocalidades cadastrosLocalidades;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CadastrosLocalidadesResource cadastrosLocalidadesResource = new CadastrosLocalidadesResource(cadastrosLocalidadesRepository, cadastrosLocalidadesSearchRepository);
        this.restCadastrosLocalidadesMockMvc = MockMvcBuilders.standaloneSetup(cadastrosLocalidadesResource)
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
    public static CadastrosLocalidades createEntity(EntityManager em) {
        CadastrosLocalidades cadastrosLocalidades = new CadastrosLocalidades()
            .numero(DEFAULT_NUMERO)
            .complemento(DEFAULT_COMPLEMENTO)
            .referencias(DEFAULT_REFERENCIAS);
        return cadastrosLocalidades;
    }

    @Before
    public void initTest() {
        cadastrosLocalidadesSearchRepository.deleteAll();
        cadastrosLocalidades = createEntity(em);
    }

    @Test
    @Transactional
    public void createCadastrosLocalidades() throws Exception {
        int databaseSizeBeforeCreate = cadastrosLocalidadesRepository.findAll().size();

        // Create the CadastrosLocalidades
        restCadastrosLocalidadesMockMvc.perform(post("/api/cadastros-localidades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cadastrosLocalidades)))
            .andExpect(status().isCreated());

        // Validate the CadastrosLocalidades in the database
        List<CadastrosLocalidades> cadastrosLocalidadesList = cadastrosLocalidadesRepository.findAll();
        assertThat(cadastrosLocalidadesList).hasSize(databaseSizeBeforeCreate + 1);
        CadastrosLocalidades testCadastrosLocalidades = cadastrosLocalidadesList.get(cadastrosLocalidadesList.size() - 1);
        assertThat(testCadastrosLocalidades.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testCadastrosLocalidades.getComplemento()).isEqualTo(DEFAULT_COMPLEMENTO);
        assertThat(testCadastrosLocalidades.getReferencias()).isEqualTo(DEFAULT_REFERENCIAS);

        // Validate the CadastrosLocalidades in Elasticsearch
        CadastrosLocalidades cadastrosLocalidadesEs = cadastrosLocalidadesSearchRepository.findOne(testCadastrosLocalidades.getId());
        assertThat(cadastrosLocalidadesEs).isEqualToIgnoringGivenFields(testCadastrosLocalidades);
    }

    @Test
    @Transactional
    public void createCadastrosLocalidadesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cadastrosLocalidadesRepository.findAll().size();

        // Create the CadastrosLocalidades with an existing ID
        cadastrosLocalidades.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCadastrosLocalidadesMockMvc.perform(post("/api/cadastros-localidades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cadastrosLocalidades)))
            .andExpect(status().isBadRequest());

        // Validate the CadastrosLocalidades in the database
        List<CadastrosLocalidades> cadastrosLocalidadesList = cadastrosLocalidadesRepository.findAll();
        assertThat(cadastrosLocalidadesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNumeroIsRequired() throws Exception {
        int databaseSizeBeforeTest = cadastrosLocalidadesRepository.findAll().size();
        // set the field null
        cadastrosLocalidades.setNumero(null);

        // Create the CadastrosLocalidades, which fails.

        restCadastrosLocalidadesMockMvc.perform(post("/api/cadastros-localidades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cadastrosLocalidades)))
            .andExpect(status().isBadRequest());

        List<CadastrosLocalidades> cadastrosLocalidadesList = cadastrosLocalidadesRepository.findAll();
        assertThat(cadastrosLocalidadesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkComplementoIsRequired() throws Exception {
        int databaseSizeBeforeTest = cadastrosLocalidadesRepository.findAll().size();
        // set the field null
        cadastrosLocalidades.setComplemento(null);

        // Create the CadastrosLocalidades, which fails.

        restCadastrosLocalidadesMockMvc.perform(post("/api/cadastros-localidades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cadastrosLocalidades)))
            .andExpect(status().isBadRequest());

        List<CadastrosLocalidades> cadastrosLocalidadesList = cadastrosLocalidadesRepository.findAll();
        assertThat(cadastrosLocalidadesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCadastrosLocalidades() throws Exception {
        // Initialize the database
        cadastrosLocalidadesRepository.saveAndFlush(cadastrosLocalidades);

        // Get all the cadastrosLocalidadesList
        restCadastrosLocalidadesMockMvc.perform(get("/api/cadastros-localidades?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cadastrosLocalidades.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO.toString())))
            .andExpect(jsonPath("$.[*].complemento").value(hasItem(DEFAULT_COMPLEMENTO.toString())))
            .andExpect(jsonPath("$.[*].referencias").value(hasItem(DEFAULT_REFERENCIAS.toString())));
    }

    @Test
    @Transactional
    public void getCadastrosLocalidades() throws Exception {
        // Initialize the database
        cadastrosLocalidadesRepository.saveAndFlush(cadastrosLocalidades);

        // Get the cadastrosLocalidades
        restCadastrosLocalidadesMockMvc.perform(get("/api/cadastros-localidades/{id}", cadastrosLocalidades.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cadastrosLocalidades.getId().intValue()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO.toString()))
            .andExpect(jsonPath("$.complemento").value(DEFAULT_COMPLEMENTO.toString()))
            .andExpect(jsonPath("$.referencias").value(DEFAULT_REFERENCIAS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCadastrosLocalidades() throws Exception {
        // Get the cadastrosLocalidades
        restCadastrosLocalidadesMockMvc.perform(get("/api/cadastros-localidades/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCadastrosLocalidades() throws Exception {
        // Initialize the database
        cadastrosLocalidadesRepository.saveAndFlush(cadastrosLocalidades);
        cadastrosLocalidadesSearchRepository.save(cadastrosLocalidades);
        int databaseSizeBeforeUpdate = cadastrosLocalidadesRepository.findAll().size();

        // Update the cadastrosLocalidades
        CadastrosLocalidades updatedCadastrosLocalidades = cadastrosLocalidadesRepository.findOne(cadastrosLocalidades.getId());
        // Disconnect from session so that the updates on updatedCadastrosLocalidades are not directly saved in db
        em.detach(updatedCadastrosLocalidades);
        updatedCadastrosLocalidades
            .numero(UPDATED_NUMERO)
            .complemento(UPDATED_COMPLEMENTO)
            .referencias(UPDATED_REFERENCIAS);

        restCadastrosLocalidadesMockMvc.perform(put("/api/cadastros-localidades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCadastrosLocalidades)))
            .andExpect(status().isOk());

        // Validate the CadastrosLocalidades in the database
        List<CadastrosLocalidades> cadastrosLocalidadesList = cadastrosLocalidadesRepository.findAll();
        assertThat(cadastrosLocalidadesList).hasSize(databaseSizeBeforeUpdate);
        CadastrosLocalidades testCadastrosLocalidades = cadastrosLocalidadesList.get(cadastrosLocalidadesList.size() - 1);
        assertThat(testCadastrosLocalidades.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testCadastrosLocalidades.getComplemento()).isEqualTo(UPDATED_COMPLEMENTO);
        assertThat(testCadastrosLocalidades.getReferencias()).isEqualTo(UPDATED_REFERENCIAS);

        // Validate the CadastrosLocalidades in Elasticsearch
        CadastrosLocalidades cadastrosLocalidadesEs = cadastrosLocalidadesSearchRepository.findOne(testCadastrosLocalidades.getId());
        assertThat(cadastrosLocalidadesEs).isEqualToIgnoringGivenFields(testCadastrosLocalidades);
    }

    @Test
    @Transactional
    public void updateNonExistingCadastrosLocalidades() throws Exception {
        int databaseSizeBeforeUpdate = cadastrosLocalidadesRepository.findAll().size();

        // Create the CadastrosLocalidades

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCadastrosLocalidadesMockMvc.perform(put("/api/cadastros-localidades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cadastrosLocalidades)))
            .andExpect(status().isCreated());

        // Validate the CadastrosLocalidades in the database
        List<CadastrosLocalidades> cadastrosLocalidadesList = cadastrosLocalidadesRepository.findAll();
        assertThat(cadastrosLocalidadesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCadastrosLocalidades() throws Exception {
        // Initialize the database
        cadastrosLocalidadesRepository.saveAndFlush(cadastrosLocalidades);
        cadastrosLocalidadesSearchRepository.save(cadastrosLocalidades);
        int databaseSizeBeforeDelete = cadastrosLocalidadesRepository.findAll().size();

        // Get the cadastrosLocalidades
        restCadastrosLocalidadesMockMvc.perform(delete("/api/cadastros-localidades/{id}", cadastrosLocalidades.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean cadastrosLocalidadesExistsInEs = cadastrosLocalidadesSearchRepository.exists(cadastrosLocalidades.getId());
        assertThat(cadastrosLocalidadesExistsInEs).isFalse();

        // Validate the database is empty
        List<CadastrosLocalidades> cadastrosLocalidadesList = cadastrosLocalidadesRepository.findAll();
        assertThat(cadastrosLocalidadesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCadastrosLocalidades() throws Exception {
        // Initialize the database
        cadastrosLocalidadesRepository.saveAndFlush(cadastrosLocalidades);
        cadastrosLocalidadesSearchRepository.save(cadastrosLocalidades);

        // Search the cadastrosLocalidades
        restCadastrosLocalidadesMockMvc.perform(get("/api/_search/cadastros-localidades?query=id:" + cadastrosLocalidades.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cadastrosLocalidades.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO.toString())))
            .andExpect(jsonPath("$.[*].complemento").value(hasItem(DEFAULT_COMPLEMENTO.toString())))
            .andExpect(jsonPath("$.[*].referencias").value(hasItem(DEFAULT_REFERENCIAS.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CadastrosLocalidades.class);
        CadastrosLocalidades cadastrosLocalidades1 = new CadastrosLocalidades();
        cadastrosLocalidades1.setId(1L);
        CadastrosLocalidades cadastrosLocalidades2 = new CadastrosLocalidades();
        cadastrosLocalidades2.setId(cadastrosLocalidades1.getId());
        assertThat(cadastrosLocalidades1).isEqualTo(cadastrosLocalidades2);
        cadastrosLocalidades2.setId(2L);
        assertThat(cadastrosLocalidades1).isNotEqualTo(cadastrosLocalidades2);
        cadastrosLocalidades1.setId(null);
        assertThat(cadastrosLocalidades1).isNotEqualTo(cadastrosLocalidades2);
    }
}
