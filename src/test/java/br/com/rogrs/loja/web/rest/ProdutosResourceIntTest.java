package br.com.rogrs.loja.web.rest;

import br.com.rogrs.loja.LojaApp;

import br.com.rogrs.loja.domain.Produtos;
import br.com.rogrs.loja.repository.ProdutosRepository;
import br.com.rogrs.loja.repository.search.ProdutosSearchRepository;
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
 * Test class for the ProdutosResource REST controller.
 *
 * @see ProdutosResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LojaApp.class)
public class ProdutosResourceIntTest {

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final String DEFAULT_CODIGO_EAN = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO_EAN = "BBBBBBBBBB";

    private static final Float DEFAULT_QTDE_ATUAL = 1F;
    private static final Float UPDATED_QTDE_ATUAL = 2F;

    private static final String DEFAULT_OBSERVACOES = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACOES = "BBBBBBBBBB";

    @Autowired
    private ProdutosRepository produtosRepository;

    @Autowired
    private ProdutosSearchRepository produtosSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProdutosMockMvc;

    private Produtos produtos;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProdutosResource produtosResource = new ProdutosResource(produtosRepository, produtosSearchRepository);
        this.restProdutosMockMvc = MockMvcBuilders.standaloneSetup(produtosResource)
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
    public static Produtos createEntity(EntityManager em) {
        Produtos produtos = new Produtos()
            .descricao(DEFAULT_DESCRICAO)
            .codigoEAN(DEFAULT_CODIGO_EAN)
            .qtdeAtual(DEFAULT_QTDE_ATUAL)
            .observacoes(DEFAULT_OBSERVACOES);
        return produtos;
    }

    @Before
    public void initTest() {
        produtosSearchRepository.deleteAll();
        produtos = createEntity(em);
    }

    @Test
    @Transactional
    public void createProdutos() throws Exception {
        int databaseSizeBeforeCreate = produtosRepository.findAll().size();

        // Create the Produtos
        restProdutosMockMvc.perform(post("/api/produtos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(produtos)))
            .andExpect(status().isCreated());

        // Validate the Produtos in the database
        List<Produtos> produtosList = produtosRepository.findAll();
        assertThat(produtosList).hasSize(databaseSizeBeforeCreate + 1);
        Produtos testProdutos = produtosList.get(produtosList.size() - 1);
        assertThat(testProdutos.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testProdutos.getCodigoEAN()).isEqualTo(DEFAULT_CODIGO_EAN);
        assertThat(testProdutos.getQtdeAtual()).isEqualTo(DEFAULT_QTDE_ATUAL);
        assertThat(testProdutos.getObservacoes()).isEqualTo(DEFAULT_OBSERVACOES);

        // Validate the Produtos in Elasticsearch
        Produtos produtosEs = produtosSearchRepository.findOne(testProdutos.getId());
        assertThat(produtosEs).isEqualToComparingFieldByField(testProdutos);
    }

    @Test
    @Transactional
    public void createProdutosWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = produtosRepository.findAll().size();

        // Create the Produtos with an existing ID
        produtos.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProdutosMockMvc.perform(post("/api/produtos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(produtos)))
            .andExpect(status().isBadRequest());

        // Validate the Produtos in the database
        List<Produtos> produtosList = produtosRepository.findAll();
        assertThat(produtosList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDescricaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = produtosRepository.findAll().size();
        // set the field null
        produtos.setDescricao(null);

        // Create the Produtos, which fails.

        restProdutosMockMvc.perform(post("/api/produtos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(produtos)))
            .andExpect(status().isBadRequest());

        List<Produtos> produtosList = produtosRepository.findAll();
        assertThat(produtosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProdutos() throws Exception {
        // Initialize the database
        produtosRepository.saveAndFlush(produtos);

        // Get all the produtosList
        restProdutosMockMvc.perform(get("/api/produtos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(produtos.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())))
            .andExpect(jsonPath("$.[*].codigoEAN").value(hasItem(DEFAULT_CODIGO_EAN.toString())))
            .andExpect(jsonPath("$.[*].qtdeAtual").value(hasItem(DEFAULT_QTDE_ATUAL.doubleValue())))
            .andExpect(jsonPath("$.[*].observacoes").value(hasItem(DEFAULT_OBSERVACOES.toString())));
    }

    @Test
    @Transactional
    public void getProdutos() throws Exception {
        // Initialize the database
        produtosRepository.saveAndFlush(produtos);

        // Get the produtos
        restProdutosMockMvc.perform(get("/api/produtos/{id}", produtos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(produtos.getId().intValue()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO.toString()))
            .andExpect(jsonPath("$.codigoEAN").value(DEFAULT_CODIGO_EAN.toString()))
            .andExpect(jsonPath("$.qtdeAtual").value(DEFAULT_QTDE_ATUAL.doubleValue()))
            .andExpect(jsonPath("$.observacoes").value(DEFAULT_OBSERVACOES.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProdutos() throws Exception {
        // Get the produtos
        restProdutosMockMvc.perform(get("/api/produtos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProdutos() throws Exception {
        // Initialize the database
        produtosRepository.saveAndFlush(produtos);
        produtosSearchRepository.save(produtos);
        int databaseSizeBeforeUpdate = produtosRepository.findAll().size();

        // Update the produtos
        Produtos updatedProdutos = produtosRepository.findOne(produtos.getId());
        updatedProdutos
            .descricao(UPDATED_DESCRICAO)
            .codigoEAN(UPDATED_CODIGO_EAN)
            .qtdeAtual(UPDATED_QTDE_ATUAL)
            .observacoes(UPDATED_OBSERVACOES);

        restProdutosMockMvc.perform(put("/api/produtos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProdutos)))
            .andExpect(status().isOk());

        // Validate the Produtos in the database
        List<Produtos> produtosList = produtosRepository.findAll();
        assertThat(produtosList).hasSize(databaseSizeBeforeUpdate);
        Produtos testProdutos = produtosList.get(produtosList.size() - 1);
        assertThat(testProdutos.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testProdutos.getCodigoEAN()).isEqualTo(UPDATED_CODIGO_EAN);
        assertThat(testProdutos.getQtdeAtual()).isEqualTo(UPDATED_QTDE_ATUAL);
        assertThat(testProdutos.getObservacoes()).isEqualTo(UPDATED_OBSERVACOES);

        // Validate the Produtos in Elasticsearch
        Produtos produtosEs = produtosSearchRepository.findOne(testProdutos.getId());
        assertThat(produtosEs).isEqualToComparingFieldByField(testProdutos);
    }

    @Test
    @Transactional
    public void updateNonExistingProdutos() throws Exception {
        int databaseSizeBeforeUpdate = produtosRepository.findAll().size();

        // Create the Produtos

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProdutosMockMvc.perform(put("/api/produtos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(produtos)))
            .andExpect(status().isCreated());

        // Validate the Produtos in the database
        List<Produtos> produtosList = produtosRepository.findAll();
        assertThat(produtosList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProdutos() throws Exception {
        // Initialize the database
        produtosRepository.saveAndFlush(produtos);
        produtosSearchRepository.save(produtos);
        int databaseSizeBeforeDelete = produtosRepository.findAll().size();

        // Get the produtos
        restProdutosMockMvc.perform(delete("/api/produtos/{id}", produtos.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean produtosExistsInEs = produtosSearchRepository.exists(produtos.getId());
        assertThat(produtosExistsInEs).isFalse();

        // Validate the database is empty
        List<Produtos> produtosList = produtosRepository.findAll();
        assertThat(produtosList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchProdutos() throws Exception {
        // Initialize the database
        produtosRepository.saveAndFlush(produtos);
        produtosSearchRepository.save(produtos);

        // Search the produtos
        restProdutosMockMvc.perform(get("/api/_search/produtos?query=id:" + produtos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(produtos.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())))
            .andExpect(jsonPath("$.[*].codigoEAN").value(hasItem(DEFAULT_CODIGO_EAN.toString())))
            .andExpect(jsonPath("$.[*].qtdeAtual").value(hasItem(DEFAULT_QTDE_ATUAL.doubleValue())))
            .andExpect(jsonPath("$.[*].observacoes").value(hasItem(DEFAULT_OBSERVACOES.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Produtos.class);
        Produtos produtos1 = new Produtos();
        produtos1.setId(1L);
        Produtos produtos2 = new Produtos();
        produtos2.setId(produtos1.getId());
        assertThat(produtos1).isEqualTo(produtos2);
        produtos2.setId(2L);
        assertThat(produtos1).isNotEqualTo(produtos2);
        produtos1.setId(null);
        assertThat(produtos1).isNotEqualTo(produtos2);
    }
}
