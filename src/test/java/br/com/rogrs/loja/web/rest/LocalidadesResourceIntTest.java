package br.com.rogrs.loja.web.rest;

import br.com.rogrs.loja.LojaApp;
import br.com.rogrs.loja.domain.Localidades;
import br.com.rogrs.loja.repository.LocalidadesRepository;
import br.com.rogrs.loja.repository.search.LocalidadesSearchRepository;

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
 * Test class for the LocalidadesResource REST controller.
 *
 * @see LocalidadesResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = LojaApp.class)
@WebAppConfiguration
@IntegrationTest
public class LocalidadesResourceIntTest {

    private static final String DEFAULT_ENDERECO = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_ENDERECO = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_CEP = "AAAAAAAAA";
    private static final String UPDATED_CEP = "BBBBBBBBB";
    private static final String DEFAULT_BAIRRO = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_BAIRRO = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_CIDADE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_CIDADE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_U_F = "AA";
    private static final String UPDATED_U_F = "BB";

    @Inject
    private LocalidadesRepository localidadesRepository;

    @Inject
    private LocalidadesSearchRepository localidadesSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restLocalidadesMockMvc;

    private Localidades localidades;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LocalidadesResource localidadesResource = new LocalidadesResource();
        ReflectionTestUtils.setField(localidadesResource, "localidadesSearchRepository", localidadesSearchRepository);
        ReflectionTestUtils.setField(localidadesResource, "localidadesRepository", localidadesRepository);
        this.restLocalidadesMockMvc = MockMvcBuilders.standaloneSetup(localidadesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        localidadesSearchRepository.deleteAll();
        localidades = new Localidades();
        localidades.setEndereco(DEFAULT_ENDERECO);
        localidades.setCep(DEFAULT_CEP);
        localidades.setBairro(DEFAULT_BAIRRO);
        localidades.setCidade(DEFAULT_CIDADE);
        localidades.setuF(DEFAULT_U_F);
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
        List<Localidades> localidades = localidadesRepository.findAll();
        assertThat(localidades).hasSize(databaseSizeBeforeCreate + 1);
        Localidades testLocalidades = localidades.get(localidades.size() - 1);
        assertThat(testLocalidades.getEndereco()).isEqualTo(DEFAULT_ENDERECO);
        assertThat(testLocalidades.getCep()).isEqualTo(DEFAULT_CEP);
        assertThat(testLocalidades.getBairro()).isEqualTo(DEFAULT_BAIRRO);
        assertThat(testLocalidades.getCidade()).isEqualTo(DEFAULT_CIDADE);
        assertThat(testLocalidades.getuF()).isEqualTo(DEFAULT_U_F);

        // Validate the Localidades in ElasticSearch
        Localidades localidadesEs = localidadesSearchRepository.findOne(testLocalidades.getId());
        assertThat(localidadesEs).isEqualToComparingFieldByField(testLocalidades);
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

        List<Localidades> localidades = localidadesRepository.findAll();
        assertThat(localidades).hasSize(databaseSizeBeforeTest);
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

        List<Localidades> localidades = localidadesRepository.findAll();
        assertThat(localidades).hasSize(databaseSizeBeforeTest);
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

        List<Localidades> localidades = localidadesRepository.findAll();
        assertThat(localidades).hasSize(databaseSizeBeforeTest);
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

        List<Localidades> localidades = localidadesRepository.findAll();
        assertThat(localidades).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLocalidades() throws Exception {
        // Initialize the database
        localidadesRepository.saveAndFlush(localidades);

        // Get all the localidades
        restLocalidadesMockMvc.perform(get("/api/localidades?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(localidades.getId().intValue())))
                .andExpect(jsonPath("$.[*].endereco").value(hasItem(DEFAULT_ENDERECO.toString())))
                .andExpect(jsonPath("$.[*].cep").value(hasItem(DEFAULT_CEP.toString())))
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
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(localidades.getId().intValue()))
            .andExpect(jsonPath("$.endereco").value(DEFAULT_ENDERECO.toString()))
            .andExpect(jsonPath("$.cep").value(DEFAULT_CEP.toString()))
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
        Localidades updatedLocalidades = new Localidades();
        updatedLocalidades.setId(localidades.getId());
        updatedLocalidades.setEndereco(UPDATED_ENDERECO);
        updatedLocalidades.setCep(UPDATED_CEP);
        updatedLocalidades.setBairro(UPDATED_BAIRRO);
        updatedLocalidades.setCidade(UPDATED_CIDADE);
        updatedLocalidades.setuF(UPDATED_U_F);

        restLocalidadesMockMvc.perform(put("/api/localidades")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedLocalidades)))
                .andExpect(status().isOk());

        // Validate the Localidades in the database
        List<Localidades> localidades = localidadesRepository.findAll();
        assertThat(localidades).hasSize(databaseSizeBeforeUpdate);
        Localidades testLocalidades = localidades.get(localidades.size() - 1);
        assertThat(testLocalidades.getEndereco()).isEqualTo(UPDATED_ENDERECO);
        assertThat(testLocalidades.getCep()).isEqualTo(UPDATED_CEP);
        assertThat(testLocalidades.getBairro()).isEqualTo(UPDATED_BAIRRO);
        assertThat(testLocalidades.getCidade()).isEqualTo(UPDATED_CIDADE);
        assertThat(testLocalidades.getuF()).isEqualTo(UPDATED_U_F);

        // Validate the Localidades in ElasticSearch
        Localidades localidadesEs = localidadesSearchRepository.findOne(testLocalidades.getId());
        assertThat(localidadesEs).isEqualToComparingFieldByField(testLocalidades);
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

        // Validate ElasticSearch is empty
        boolean localidadesExistsInEs = localidadesSearchRepository.exists(localidades.getId());
        assertThat(localidadesExistsInEs).isFalse();

        // Validate the database is empty
        List<Localidades> localidades = localidadesRepository.findAll();
        assertThat(localidades).hasSize(databaseSizeBeforeDelete - 1);
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
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(localidades.getId().intValue())))
            .andExpect(jsonPath("$.[*].endereco").value(hasItem(DEFAULT_ENDERECO.toString())))
            .andExpect(jsonPath("$.[*].cep").value(hasItem(DEFAULT_CEP.toString())))
            .andExpect(jsonPath("$.[*].bairro").value(hasItem(DEFAULT_BAIRRO.toString())))
            .andExpect(jsonPath("$.[*].cidade").value(hasItem(DEFAULT_CIDADE.toString())))
            .andExpect(jsonPath("$.[*].uF").value(hasItem(DEFAULT_U_F.toString())));
    }
}
