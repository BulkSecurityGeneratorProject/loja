package br.com.rogrs.loja.web.rest;

import br.com.rogrs.loja.LojaApp;

import br.com.rogrs.loja.domain.Localidades;
import br.com.rogrs.loja.repository.LocalidadesRepository;
import br.com.rogrs.loja.repository.search.LocalidadesSearchRepository;
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

import br.com.rogrs.loja.domain.enumeration.Estados;
/**
 * Test class for the LocalidadesResource REST controller.
 *
 * @see LocalidadesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LojaApp.class)
public class LocalidadesResourceIntTest {

    private static final String DEFAULT_CEP = "AAAAAAAAA";
    private static final String UPDATED_CEP = "BBBBBBBBB";

    private static final String DEFAULT_ENDERECO = "AAAAAAAAAA";
    private static final String UPDATED_ENDERECO = "BBBBBBBBBB";

    private static final String DEFAULT_BAIRRO = "AAAAAAAAAA";
    private static final String UPDATED_BAIRRO = "BBBBBBBBBB";

    private static final String DEFAULT_CIDADE = "AAAAAAAAAA";
    private static final String UPDATED_CIDADE = "BBBBBBBBBB";

    private static final Estados DEFAULT_U_F = Estados.AC;
    private static final Estados UPDATED_U_F = Estados.AL;

    @Autowired
    private LocalidadesRepository localidadesRepository;

    @Autowired
    private LocalidadesSearchRepository localidadesSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restLocalidadesMockMvc;

    private Localidades localidades;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LocalidadesResource localidadesResource = new LocalidadesResource(localidadesRepository, localidadesSearchRepository);
        this.restLocalidadesMockMvc = MockMvcBuilders.standaloneSetup(localidadesResource)
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
    public static Localidades createEntity(EntityManager em) {
        Localidades localidades = new Localidades()
            .cep(DEFAULT_CEP)
            .endereco(DEFAULT_ENDERECO)
            .bairro(DEFAULT_BAIRRO)
            .cidade(DEFAULT_CIDADE)
            .uF(DEFAULT_U_F);
        return localidades;
    }

    @Before
    public void initTest() {
        localidadesSearchRepository.deleteAll();
        localidades = createEntity(em);
    }

    @Test
    @Transactional
    public void createLocalidades() throws Exception {
        int databaseSizeBeforeCreate = localidadesRepository.findAll().size();

        // Create the Localidades
        restLocalidadesMockMvc.perform(post("/api/localidades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(localidades)))
            .andExpect(status().isCreated());

        // Validate the Localidades in the database
        List<Localidades> localidadesList = localidadesRepository.findAll();
        assertThat(localidadesList).hasSize(databaseSizeBeforeCreate + 1);
        Localidades testLocalidades = localidadesList.get(localidadesList.size() - 1);
        assertThat(testLocalidades.getCep()).isEqualTo(DEFAULT_CEP);
        assertThat(testLocalidades.getEndereco()).isEqualTo(DEFAULT_ENDERECO);
        assertThat(testLocalidades.getBairro()).isEqualTo(DEFAULT_BAIRRO);
        assertThat(testLocalidades.getCidade()).isEqualTo(DEFAULT_CIDADE);
        assertThat(testLocalidades.getuF()).isEqualTo(DEFAULT_U_F);

        // Validate the Localidades in Elasticsearch
        Localidades localidadesEs = localidadesSearchRepository.findOne(testLocalidades.getId());
        assertThat(localidadesEs).isEqualToIgnoringGivenFields(testLocalidades);
    }

    @Test
    @Transactional
    public void createLocalidadesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = localidadesRepository.findAll().size();

        // Create the Localidades with an existing ID
        localidades.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLocalidadesMockMvc.perform(post("/api/localidades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(localidades)))
            .andExpect(status().isBadRequest());

        // Validate the Localidades in the database
        List<Localidades> localidadesList = localidadesRepository.findAll();
        assertThat(localidadesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCepIsRequired() throws Exception {
        int databaseSizeBeforeTest = localidadesRepository.findAll().size();
        // set the field null
        localidades.setCep(null);

        // Create the Localidades, which fails.

        restLocalidadesMockMvc.perform(post("/api/localidades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(localidades)))
            .andExpect(status().isBadRequest());

        List<Localidades> localidadesList = localidadesRepository.findAll();
        assertThat(localidadesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEnderecoIsRequired() throws Exception {
        int databaseSizeBeforeTest = localidadesRepository.findAll().size();
        // set the field null
        localidades.setEndereco(null);

        // Create the Localidades, which fails.

        restLocalidadesMockMvc.perform(post("/api/localidades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(localidades)))
            .andExpect(status().isBadRequest());

        List<Localidades> localidadesList = localidadesRepository.findAll();
        assertThat(localidadesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBairroIsRequired() throws Exception {
        int databaseSizeBeforeTest = localidadesRepository.findAll().size();
        // set the field null
        localidades.setBairro(null);

        // Create the Localidades, which fails.

        restLocalidadesMockMvc.perform(post("/api/localidades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(localidades)))
            .andExpect(status().isBadRequest());

        List<Localidades> localidadesList = localidadesRepository.findAll();
        assertThat(localidadesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCidadeIsRequired() throws Exception {
        int databaseSizeBeforeTest = localidadesRepository.findAll().size();
        // set the field null
        localidades.setCidade(null);

        // Create the Localidades, which fails.

        restLocalidadesMockMvc.perform(post("/api/localidades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(localidades)))
            .andExpect(status().isBadRequest());

        List<Localidades> localidadesList = localidadesRepository.findAll();
        assertThat(localidadesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkuFIsRequired() throws Exception {
        int databaseSizeBeforeTest = localidadesRepository.findAll().size();
        // set the field null
        localidades.setuF(null);

        // Create the Localidades, which fails.

        restLocalidadesMockMvc.perform(post("/api/localidades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(localidades)))
            .andExpect(status().isBadRequest());

        List<Localidades> localidadesList = localidadesRepository.findAll();
        assertThat(localidadesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLocalidades() throws Exception {
        // Initialize the database
        localidadesRepository.saveAndFlush(localidades);

        // Get all the localidadesList
        restLocalidadesMockMvc.perform(get("/api/localidades?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(localidades.getId().intValue())))
            .andExpect(jsonPath("$.[*].cep").value(hasItem(DEFAULT_CEP.toString())))
            .andExpect(jsonPath("$.[*].endereco").value(hasItem(DEFAULT_ENDERECO.toString())))
            .andExpect(jsonPath("$.[*].bairro").value(hasItem(DEFAULT_BAIRRO.toString())))
            .andExpect(jsonPath("$.[*].cidade").value(hasItem(DEFAULT_CIDADE.toString())))
            .andExpect(jsonPath("$.[*].uF").value(hasItem(DEFAULT_U_F.toString())));
    }

    @Test
    @Transactional
    public void getLocalidades() throws Exception {
        // Initialize the database
        localidadesRepository.saveAndFlush(localidades);

        // Get the localidades
        restLocalidadesMockMvc.perform(get("/api/localidades/{id}", localidades.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(localidades.getId().intValue()))
            .andExpect(jsonPath("$.cep").value(DEFAULT_CEP.toString()))
            .andExpect(jsonPath("$.endereco").value(DEFAULT_ENDERECO.toString()))
            .andExpect(jsonPath("$.bairro").value(DEFAULT_BAIRRO.toString()))
            .andExpect(jsonPath("$.cidade").value(DEFAULT_CIDADE.toString()))
            .andExpect(jsonPath("$.uF").value(DEFAULT_U_F.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLocalidades() throws Exception {
        // Get the localidades
        restLocalidadesMockMvc.perform(get("/api/localidades/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLocalidades() throws Exception {
        // Initialize the database
        localidadesRepository.saveAndFlush(localidades);
        localidadesSearchRepository.save(localidades);
        int databaseSizeBeforeUpdate = localidadesRepository.findAll().size();

        // Update the localidades
        Localidades updatedLocalidades = localidadesRepository.findOne(localidades.getId());
        // Disconnect from session so that the updates on updatedLocalidades are not directly saved in db
        em.detach(updatedLocalidades);
        updatedLocalidades
            .cep(UPDATED_CEP)
            .endereco(UPDATED_ENDERECO)
            .bairro(UPDATED_BAIRRO)
            .cidade(UPDATED_CIDADE)
            .uF(UPDATED_U_F);

        restLocalidadesMockMvc.perform(put("/api/localidades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLocalidades)))
            .andExpect(status().isOk());

        // Validate the Localidades in the database
        List<Localidades> localidadesList = localidadesRepository.findAll();
        assertThat(localidadesList).hasSize(databaseSizeBeforeUpdate);
        Localidades testLocalidades = localidadesList.get(localidadesList.size() - 1);
        assertThat(testLocalidades.getCep()).isEqualTo(UPDATED_CEP);
        assertThat(testLocalidades.getEndereco()).isEqualTo(UPDATED_ENDERECO);
        assertThat(testLocalidades.getBairro()).isEqualTo(UPDATED_BAIRRO);
        assertThat(testLocalidades.getCidade()).isEqualTo(UPDATED_CIDADE);
        assertThat(testLocalidades.getuF()).isEqualTo(UPDATED_U_F);

        // Validate the Localidades in Elasticsearch
        Localidades localidadesEs = localidadesSearchRepository.findOne(testLocalidades.getId());
        assertThat(localidadesEs).isEqualToIgnoringGivenFields(testLocalidades);
    }

    @Test
    @Transactional
    public void updateNonExistingLocalidades() throws Exception {
        int databaseSizeBeforeUpdate = localidadesRepository.findAll().size();

        // Create the Localidades

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLocalidadesMockMvc.perform(put("/api/localidades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(localidades)))
            .andExpect(status().isCreated());

        // Validate the Localidades in the database
        List<Localidades> localidadesList = localidadesRepository.findAll();
        assertThat(localidadesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteLocalidades() throws Exception {
        // Initialize the database
        localidadesRepository.saveAndFlush(localidades);
        localidadesSearchRepository.save(localidades);
        int databaseSizeBeforeDelete = localidadesRepository.findAll().size();

        // Get the localidades
        restLocalidadesMockMvc.perform(delete("/api/localidades/{id}", localidades.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean localidadesExistsInEs = localidadesSearchRepository.exists(localidades.getId());
        assertThat(localidadesExistsInEs).isFalse();

        // Validate the database is empty
        List<Localidades> localidadesList = localidadesRepository.findAll();
        assertThat(localidadesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchLocalidades() throws Exception {
        // Initialize the database
        localidadesRepository.saveAndFlush(localidades);
        localidadesSearchRepository.save(localidades);

        // Search the localidades
        restLocalidadesMockMvc.perform(get("/api/_search/localidades?query=id:" + localidades.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(localidades.getId().intValue())))
            .andExpect(jsonPath("$.[*].cep").value(hasItem(DEFAULT_CEP.toString())))
            .andExpect(jsonPath("$.[*].endereco").value(hasItem(DEFAULT_ENDERECO.toString())))
            .andExpect(jsonPath("$.[*].bairro").value(hasItem(DEFAULT_BAIRRO.toString())))
            .andExpect(jsonPath("$.[*].cidade").value(hasItem(DEFAULT_CIDADE.toString())))
            .andExpect(jsonPath("$.[*].uF").value(hasItem(DEFAULT_U_F.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Localidades.class);
        Localidades localidades1 = new Localidades();
        localidades1.setId(1L);
        Localidades localidades2 = new Localidades();
        localidades2.setId(localidades1.getId());
        assertThat(localidades1).isEqualTo(localidades2);
        localidades2.setId(2L);
        assertThat(localidades1).isNotEqualTo(localidades2);
        localidades1.setId(null);
        assertThat(localidades1).isNotEqualTo(localidades2);
    }
}
