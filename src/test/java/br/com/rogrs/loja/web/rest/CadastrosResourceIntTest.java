package br.com.rogrs.loja.web.rest;

import br.com.rogrs.loja.LojaApp;
import br.com.rogrs.loja.domain.Cadastros;
import br.com.rogrs.loja.repository.CadastrosRepository;
import br.com.rogrs.loja.repository.search.CadastrosSearchRepository;

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

import br.com.rogrs.loja.domain.enumeration.TipoPessoa;
import br.com.rogrs.loja.domain.enumeration.TipoCadastro;

/**
 * Test class for the CadastrosResource REST controller.
 *
 * @see CadastrosResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = LojaApp.class)
@WebAppConfiguration
@IntegrationTest
public class CadastrosResourceIntTest {

    private static final String DEFAULT_NOME = "AAAAA";
    private static final String UPDATED_NOME = "BBBBB";

    private static final TipoPessoa DEFAULT_TIPO_PESSOA = TipoPessoa.FISICA;
    private static final TipoPessoa UPDATED_TIPO_PESSOA = TipoPessoa.JURIDICA;

    private static final TipoCadastro DEFAULT_TIPO_CADASTRO = TipoCadastro.CLIENTE;
    private static final TipoCadastro UPDATED_TIPO_CADASTRO = TipoCadastro.FORNECEDOR;
    private static final String DEFAULT_EMAIL = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_OBSERVACOES = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_OBSERVACOES = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private CadastrosRepository cadastrosRepository;

    @Inject
    private CadastrosSearchRepository cadastrosSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCadastrosMockMvc;

    private Cadastros cadastros;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CadastrosResource cadastrosResource = new CadastrosResource();
        ReflectionTestUtils.setField(cadastrosResource, "cadastrosSearchRepository", cadastrosSearchRepository);
        ReflectionTestUtils.setField(cadastrosResource, "cadastrosRepository", cadastrosRepository);
        this.restCadastrosMockMvc = MockMvcBuilders.standaloneSetup(cadastrosResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        cadastrosSearchRepository.deleteAll();
        cadastros = new Cadastros();
        cadastros.setNome(DEFAULT_NOME);
        cadastros.setTipoPessoa(DEFAULT_TIPO_PESSOA);
        cadastros.setTipoCadastro(DEFAULT_TIPO_CADASTRO);
        cadastros.setEmail(DEFAULT_EMAIL);
        cadastros.setObservacoes(DEFAULT_OBSERVACOES);
    }

    @Test
    @Transactional
    public void createCadastros() throws Exception {
        int databaseSizeBeforeCreate = cadastrosRepository.findAll().size();

        // Create the Cadastros

        restCadastrosMockMvc.perform(post("/api/cadastros")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cadastros)))
                .andExpect(status().isCreated());

        // Validate the Cadastros in the database
        List<Cadastros> cadastros = cadastrosRepository.findAll();
        assertThat(cadastros).hasSize(databaseSizeBeforeCreate + 1);
        Cadastros testCadastros = cadastros.get(cadastros.size() - 1);
        assertThat(testCadastros.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testCadastros.getTipoPessoa()).isEqualTo(DEFAULT_TIPO_PESSOA);
        assertThat(testCadastros.getTipoCadastro()).isEqualTo(DEFAULT_TIPO_CADASTRO);
        assertThat(testCadastros.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCadastros.getObservacoes()).isEqualTo(DEFAULT_OBSERVACOES);

        // Validate the Cadastros in ElasticSearch
        Cadastros cadastrosEs = cadastrosSearchRepository.findOne(testCadastros.getId());
        assertThat(cadastrosEs).isEqualToComparingFieldByField(testCadastros);
    }

    @Test
    @Transactional
    public void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = cadastrosRepository.findAll().size();
        // set the field null
        cadastros.setNome(null);

        // Create the Cadastros, which fails.

        restCadastrosMockMvc.perform(post("/api/cadastros")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cadastros)))
                .andExpect(status().isBadRequest());

        List<Cadastros> cadastros = cadastrosRepository.findAll();
        assertThat(cadastros).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTipoPessoaIsRequired() throws Exception {
        int databaseSizeBeforeTest = cadastrosRepository.findAll().size();
        // set the field null
        cadastros.setTipoPessoa(null);

        // Create the Cadastros, which fails.

        restCadastrosMockMvc.perform(post("/api/cadastros")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cadastros)))
                .andExpect(status().isBadRequest());

        List<Cadastros> cadastros = cadastrosRepository.findAll();
        assertThat(cadastros).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTipoCadastroIsRequired() throws Exception {
        int databaseSizeBeforeTest = cadastrosRepository.findAll().size();
        // set the field null
        cadastros.setTipoCadastro(null);

        // Create the Cadastros, which fails.

        restCadastrosMockMvc.perform(post("/api/cadastros")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cadastros)))
                .andExpect(status().isBadRequest());

        List<Cadastros> cadastros = cadastrosRepository.findAll();
        assertThat(cadastros).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCadastros() throws Exception {
        // Initialize the database
        cadastrosRepository.saveAndFlush(cadastros);

        // Get all the cadastros
        restCadastrosMockMvc.perform(get("/api/cadastros?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(cadastros.getId().intValue())))
                .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
                .andExpect(jsonPath("$.[*].tipoPessoa").value(hasItem(DEFAULT_TIPO_PESSOA.toString())))
                .andExpect(jsonPath("$.[*].tipoCadastro").value(hasItem(DEFAULT_TIPO_CADASTRO.toString())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].observacoes").value(hasItem(DEFAULT_OBSERVACOES.toString())));
    }

    @Test
    @Transactional
    public void getCadastros() throws Exception {
        // Initialize the database
        cadastrosRepository.saveAndFlush(cadastros);

        // Get the cadastros
        restCadastrosMockMvc.perform(get("/api/cadastros/{id}", cadastros.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(cadastros.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.tipoPessoa").value(DEFAULT_TIPO_PESSOA.toString()))
            .andExpect(jsonPath("$.tipoCadastro").value(DEFAULT_TIPO_CADASTRO.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.observacoes").value(DEFAULT_OBSERVACOES.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCadastros() throws Exception {
        // Get the cadastros
        restCadastrosMockMvc.perform(get("/api/cadastros/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCadastros() throws Exception {
        // Initialize the database
        cadastrosRepository.saveAndFlush(cadastros);
        cadastrosSearchRepository.save(cadastros);
        int databaseSizeBeforeUpdate = cadastrosRepository.findAll().size();

        // Update the cadastros
        Cadastros updatedCadastros = new Cadastros();
        updatedCadastros.setId(cadastros.getId());
        updatedCadastros.setNome(UPDATED_NOME);
        updatedCadastros.setTipoPessoa(UPDATED_TIPO_PESSOA);
        updatedCadastros.setTipoCadastro(UPDATED_TIPO_CADASTRO);
        updatedCadastros.setEmail(UPDATED_EMAIL);
        updatedCadastros.setObservacoes(UPDATED_OBSERVACOES);

        restCadastrosMockMvc.perform(put("/api/cadastros")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCadastros)))
                .andExpect(status().isOk());

        // Validate the Cadastros in the database
        List<Cadastros> cadastros = cadastrosRepository.findAll();
        assertThat(cadastros).hasSize(databaseSizeBeforeUpdate);
        Cadastros testCadastros = cadastros.get(cadastros.size() - 1);
        assertThat(testCadastros.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testCadastros.getTipoPessoa()).isEqualTo(UPDATED_TIPO_PESSOA);
        assertThat(testCadastros.getTipoCadastro()).isEqualTo(UPDATED_TIPO_CADASTRO);
        assertThat(testCadastros.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCadastros.getObservacoes()).isEqualTo(UPDATED_OBSERVACOES);

        // Validate the Cadastros in ElasticSearch
        Cadastros cadastrosEs = cadastrosSearchRepository.findOne(testCadastros.getId());
        assertThat(cadastrosEs).isEqualToComparingFieldByField(testCadastros);
    }

    @Test
    @Transactional
    public void deleteCadastros() throws Exception {
        // Initialize the database
        cadastrosRepository.saveAndFlush(cadastros);
        cadastrosSearchRepository.save(cadastros);
        int databaseSizeBeforeDelete = cadastrosRepository.findAll().size();

        // Get the cadastros
        restCadastrosMockMvc.perform(delete("/api/cadastros/{id}", cadastros.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean cadastrosExistsInEs = cadastrosSearchRepository.exists(cadastros.getId());
        assertThat(cadastrosExistsInEs).isFalse();

        // Validate the database is empty
        List<Cadastros> cadastros = cadastrosRepository.findAll();
        assertThat(cadastros).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCadastros() throws Exception {
        // Initialize the database
        cadastrosRepository.saveAndFlush(cadastros);
        cadastrosSearchRepository.save(cadastros);

        // Search the cadastros
        restCadastrosMockMvc.perform(get("/api/_search/cadastros?query=id:" + cadastros.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cadastros.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].tipoPessoa").value(hasItem(DEFAULT_TIPO_PESSOA.toString())))
            .andExpect(jsonPath("$.[*].tipoCadastro").value(hasItem(DEFAULT_TIPO_CADASTRO.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].observacoes").value(hasItem(DEFAULT_OBSERVACOES.toString())));
    }
}
