package br.com.rogrs.loja.web.rest;

import br.com.rogrs.loja.LojaApp;

import br.com.rogrs.loja.domain.Cadastros;
import br.com.rogrs.loja.repository.CadastrosRepository;
import br.com.rogrs.loja.repository.search.CadastrosSearchRepository;
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

import br.com.rogrs.loja.domain.enumeration.TipoPessoa;
import br.com.rogrs.loja.domain.enumeration.TipoCadastro;
/**
 * Test class for the CadastrosResource REST controller.
 *
 * @see CadastrosResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LojaApp.class)
public class CadastrosResourceIntTest {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final TipoPessoa DEFAULT_TIPO_PESSOA = TipoPessoa.FISICA;
    private static final TipoPessoa UPDATED_TIPO_PESSOA = TipoPessoa.JURIDICA;

    private static final TipoCadastro DEFAULT_TIPO_CADASTRO = TipoCadastro.CLIENTE;
    private static final TipoCadastro UPDATED_TIPO_CADASTRO = TipoCadastro.FORNECEDOR;

    private static final String DEFAULT_CPFCNPJ = "AAAAAAAAAA";
    private static final String UPDATED_CPFCNPJ = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_OBSERVACOES = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACOES = "BBBBBBBBBB";

    @Autowired
    private CadastrosRepository cadastrosRepository;

    @Autowired
    private CadastrosSearchRepository cadastrosSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCadastrosMockMvc;

    private Cadastros cadastros;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CadastrosResource cadastrosResource = new CadastrosResource(cadastrosRepository, cadastrosSearchRepository);
        this.restCadastrosMockMvc = MockMvcBuilders.standaloneSetup(cadastrosResource)
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
    public static Cadastros createEntity(EntityManager em) {
        Cadastros cadastros = new Cadastros()
            .nome(DEFAULT_NOME)
            .tipoPessoa(DEFAULT_TIPO_PESSOA)
            .tipoCadastro(DEFAULT_TIPO_CADASTRO)
            .cpfcnpj(DEFAULT_CPFCNPJ)
            .email(DEFAULT_EMAIL)
            .observacoes(DEFAULT_OBSERVACOES);
        return cadastros;
    }

    @Before
    public void initTest() {
        cadastrosSearchRepository.deleteAll();
        cadastros = createEntity(em);
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
        List<Cadastros> cadastrosList = cadastrosRepository.findAll();
        assertThat(cadastrosList).hasSize(databaseSizeBeforeCreate + 1);
        Cadastros testCadastros = cadastrosList.get(cadastrosList.size() - 1);
        assertThat(testCadastros.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testCadastros.getTipoPessoa()).isEqualTo(DEFAULT_TIPO_PESSOA);
        assertThat(testCadastros.getTipoCadastro()).isEqualTo(DEFAULT_TIPO_CADASTRO);
        assertThat(testCadastros.getCpfcnpj()).isEqualTo(DEFAULT_CPFCNPJ);
        assertThat(testCadastros.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCadastros.getObservacoes()).isEqualTo(DEFAULT_OBSERVACOES);

        // Validate the Cadastros in Elasticsearch
        Cadastros cadastrosEs = cadastrosSearchRepository.findOne(testCadastros.getId());
        assertThat(cadastrosEs).isEqualToIgnoringGivenFields(testCadastros);
    }

    @Test
    @Transactional
    public void createCadastrosWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cadastrosRepository.findAll().size();

        // Create the Cadastros with an existing ID
        cadastros.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCadastrosMockMvc.perform(post("/api/cadastros")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cadastros)))
            .andExpect(status().isBadRequest());

        // Validate the Cadastros in the database
        List<Cadastros> cadastrosList = cadastrosRepository.findAll();
        assertThat(cadastrosList).hasSize(databaseSizeBeforeCreate);
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

        List<Cadastros> cadastrosList = cadastrosRepository.findAll();
        assertThat(cadastrosList).hasSize(databaseSizeBeforeTest);
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

        List<Cadastros> cadastrosList = cadastrosRepository.findAll();
        assertThat(cadastrosList).hasSize(databaseSizeBeforeTest);
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

        List<Cadastros> cadastrosList = cadastrosRepository.findAll();
        assertThat(cadastrosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCadastros() throws Exception {
        // Initialize the database
        cadastrosRepository.saveAndFlush(cadastros);

        // Get all the cadastrosList
        restCadastrosMockMvc.perform(get("/api/cadastros?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cadastros.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].tipoPessoa").value(hasItem(DEFAULT_TIPO_PESSOA.toString())))
            .andExpect(jsonPath("$.[*].tipoCadastro").value(hasItem(DEFAULT_TIPO_CADASTRO.toString())))
            .andExpect(jsonPath("$.[*].cpfcnpj").value(hasItem(DEFAULT_CPFCNPJ.toString())))
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
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cadastros.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.tipoPessoa").value(DEFAULT_TIPO_PESSOA.toString()))
            .andExpect(jsonPath("$.tipoCadastro").value(DEFAULT_TIPO_CADASTRO.toString()))
            .andExpect(jsonPath("$.cpfcnpj").value(DEFAULT_CPFCNPJ.toString()))
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
        Cadastros updatedCadastros = cadastrosRepository.findOne(cadastros.getId());
        // Disconnect from session so that the updates on updatedCadastros are not directly saved in db
        em.detach(updatedCadastros);
        updatedCadastros
            .nome(UPDATED_NOME)
            .tipoPessoa(UPDATED_TIPO_PESSOA)
            .tipoCadastro(UPDATED_TIPO_CADASTRO)
            .cpfcnpj(UPDATED_CPFCNPJ)
            .email(UPDATED_EMAIL)
            .observacoes(UPDATED_OBSERVACOES);

        restCadastrosMockMvc.perform(put("/api/cadastros")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCadastros)))
            .andExpect(status().isOk());

        // Validate the Cadastros in the database
        List<Cadastros> cadastrosList = cadastrosRepository.findAll();
        assertThat(cadastrosList).hasSize(databaseSizeBeforeUpdate);
        Cadastros testCadastros = cadastrosList.get(cadastrosList.size() - 1);
        assertThat(testCadastros.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testCadastros.getTipoPessoa()).isEqualTo(UPDATED_TIPO_PESSOA);
        assertThat(testCadastros.getTipoCadastro()).isEqualTo(UPDATED_TIPO_CADASTRO);
        assertThat(testCadastros.getCpfcnpj()).isEqualTo(UPDATED_CPFCNPJ);
        assertThat(testCadastros.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCadastros.getObservacoes()).isEqualTo(UPDATED_OBSERVACOES);

        // Validate the Cadastros in Elasticsearch
        Cadastros cadastrosEs = cadastrosSearchRepository.findOne(testCadastros.getId());
        assertThat(cadastrosEs).isEqualToIgnoringGivenFields(testCadastros);
    }

    @Test
    @Transactional
    public void updateNonExistingCadastros() throws Exception {
        int databaseSizeBeforeUpdate = cadastrosRepository.findAll().size();

        // Create the Cadastros

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCadastrosMockMvc.perform(put("/api/cadastros")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cadastros)))
            .andExpect(status().isCreated());

        // Validate the Cadastros in the database
        List<Cadastros> cadastrosList = cadastrosRepository.findAll();
        assertThat(cadastrosList).hasSize(databaseSizeBeforeUpdate + 1);
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

        // Validate Elasticsearch is empty
        boolean cadastrosExistsInEs = cadastrosSearchRepository.exists(cadastros.getId());
        assertThat(cadastrosExistsInEs).isFalse();

        // Validate the database is empty
        List<Cadastros> cadastrosList = cadastrosRepository.findAll();
        assertThat(cadastrosList).hasSize(databaseSizeBeforeDelete - 1);
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
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cadastros.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].tipoPessoa").value(hasItem(DEFAULT_TIPO_PESSOA.toString())))
            .andExpect(jsonPath("$.[*].tipoCadastro").value(hasItem(DEFAULT_TIPO_CADASTRO.toString())))
            .andExpect(jsonPath("$.[*].cpfcnpj").value(hasItem(DEFAULT_CPFCNPJ.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].observacoes").value(hasItem(DEFAULT_OBSERVACOES.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cadastros.class);
        Cadastros cadastros1 = new Cadastros();
        cadastros1.setId(1L);
        Cadastros cadastros2 = new Cadastros();
        cadastros2.setId(cadastros1.getId());
        assertThat(cadastros1).isEqualTo(cadastros2);
        cadastros2.setId(2L);
        assertThat(cadastros1).isNotEqualTo(cadastros2);
        cadastros1.setId(null);
        assertThat(cadastros1).isNotEqualTo(cadastros2);
    }
}
