package br.com.rogrs.loja.web.rest;

import br.com.rogrs.loja.LojaApp;
import br.com.rogrs.loja.domain.Produtos;
import br.com.rogrs.loja.repository.ProdutosRepository;
import br.com.rogrs.loja.repository.search.ProdutosSearchRepository;

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
 * Test class for the ProdutosResource REST controller.
 *
 * @see ProdutosResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = LojaApp.class)
@WebAppConfiguration
@IntegrationTest
public class ProdutosResourceIntTest {

    private static final String DEFAULT_DESCRICAO = "AAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBB";
    private static final String DEFAULT_CODIGO_EAN = "AAAAAAAAAAAAA";
    private static final String UPDATED_CODIGO_EAN = "BBBBBBBBBBBBB";

    private static final Float DEFAULT_QTDE_ATUAL = 1F;
    private static final Float UPDATED_QTDE_ATUAL = 2F;
    private static final String DEFAULT_OBSERVACOES = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_OBSERVACOES = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private ProdutosRepository produtosRepository;

    @Inject
    private ProdutosSearchRepository produtosSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restProdutosMockMvc;

    private Produtos produtos;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProdutosResource produtosResource = new ProdutosResource();
        ReflectionTestUtils.setField(produtosResource, "produtosSearchRepository", produtosSearchRepository);
        ReflectionTestUtils.setField(produtosResource, "produtosRepository", produtosRepository);
        this.restProdutosMockMvc = MockMvcBuilders.standaloneSetup(produtosResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        produtosSearchRepository.deleteAll();
        produtos = new Produtos();
        produtos.setDescricao(DEFAULT_DESCRICAO);
        produtos.setCodigoEAN(DEFAULT_CODIGO_EAN);
        produtos.setQtdeAtual(DEFAULT_QTDE_ATUAL);
        produtos.setObservacoes(DEFAULT_OBSERVACOES);
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
        List<Produtos> produtos = produtosRepository.findAll();
        assertThat(produtos).hasSize(databaseSizeBeforeCreate + 1);
        Produtos testProdutos = produtos.get(produtos.size() - 1);
        assertThat(testProdutos.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testProdutos.getCodigoEAN()).isEqualTo(DEFAULT_CODIGO_EAN);
        assertThat(testProdutos.getQtdeAtual()).isEqualTo(DEFAULT_QTDE_ATUAL);
        assertThat(testProdutos.getObservacoes()).isEqualTo(DEFAULT_OBSERVACOES);

        // Validate the Produtos in ElasticSearch
        Produtos produtosEs = produtosSearchRepository.findOne(testProdutos.getId());
        assertThat(produtosEs).isEqualToComparingFieldByField(testProdutos);
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

        List<Produtos> produtos = produtosRepository.findAll();
        assertThat(produtos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProdutos() throws Exception {
        // Initialize the database
        produtosRepository.saveAndFlush(produtos);

        // Get all the produtos
        restProdutosMockMvc.perform(get("/api/produtos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
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
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
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
        Produtos updatedProdutos = new Produtos();
        updatedProdutos.setId(produtos.getId());
        updatedProdutos.setDescricao(UPDATED_DESCRICAO);
        updatedProdutos.setCodigoEAN(UPDATED_CODIGO_EAN);
        updatedProdutos.setQtdeAtual(UPDATED_QTDE_ATUAL);
        updatedProdutos.setObservacoes(UPDATED_OBSERVACOES);

        restProdutosMockMvc.perform(put("/api/produtos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedProdutos)))
                .andExpect(status().isOk());

        // Validate the Produtos in the database
        List<Produtos> produtos = produtosRepository.findAll();
        assertThat(produtos).hasSize(databaseSizeBeforeUpdate);
        Produtos testProdutos = produtos.get(produtos.size() - 1);
        assertThat(testProdutos.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testProdutos.getCodigoEAN()).isEqualTo(UPDATED_CODIGO_EAN);
        assertThat(testProdutos.getQtdeAtual()).isEqualTo(UPDATED_QTDE_ATUAL);
        assertThat(testProdutos.getObservacoes()).isEqualTo(UPDATED_OBSERVACOES);

        // Validate the Produtos in ElasticSearch
        Produtos produtosEs = produtosSearchRepository.findOne(testProdutos.getId());
        assertThat(produtosEs).isEqualToComparingFieldByField(testProdutos);
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

        // Validate ElasticSearch is empty
        boolean produtosExistsInEs = produtosSearchRepository.exists(produtos.getId());
        assertThat(produtosExistsInEs).isFalse();

        // Validate the database is empty
        List<Produtos> produtos = produtosRepository.findAll();
        assertThat(produtos).hasSize(databaseSizeBeforeDelete - 1);
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
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(produtos.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())))
            .andExpect(jsonPath("$.[*].codigoEAN").value(hasItem(DEFAULT_CODIGO_EAN.toString())))
            .andExpect(jsonPath("$.[*].qtdeAtual").value(hasItem(DEFAULT_QTDE_ATUAL.doubleValue())))
            .andExpect(jsonPath("$.[*].observacoes").value(hasItem(DEFAULT_OBSERVACOES.toString())));
    }
}
