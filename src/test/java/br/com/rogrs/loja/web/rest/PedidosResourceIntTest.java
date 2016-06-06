package br.com.rogrs.loja.web.rest;

import br.com.rogrs.loja.LojaApp;
import br.com.rogrs.loja.domain.Pedidos;
import br.com.rogrs.loja.repository.PedidosRepository;
import br.com.rogrs.loja.repository.search.PedidosSearchRepository;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.rogrs.loja.domain.enumeration.TipoPedido;

/**
 * Test class for the PedidosResource REST controller.
 *
 * @see PedidosResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = LojaApp.class)
@WebAppConfiguration
@IntegrationTest
public class PedidosResourceIntTest {


    private static final LocalDate DEFAULT_DATA_PEDIDO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_PEDIDO = LocalDate.now(ZoneId.systemDefault());

    private static final TipoPedido DEFAULT_TIPO_PEDIDO = TipoPedido.VENDA;
    private static final TipoPedido UPDATED_TIPO_PEDIDO = TipoPedido.COMPRA;
    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private PedidosRepository pedidosRepository;

    @Inject
    private PedidosSearchRepository pedidosSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPedidosMockMvc;

    private Pedidos pedidos;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PedidosResource pedidosResource = new PedidosResource();
        ReflectionTestUtils.setField(pedidosResource, "pedidosSearchRepository", pedidosSearchRepository);
        ReflectionTestUtils.setField(pedidosResource, "pedidosRepository", pedidosRepository);
        this.restPedidosMockMvc = MockMvcBuilders.standaloneSetup(pedidosResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        pedidosSearchRepository.deleteAll();
        pedidos = new Pedidos();
        pedidos.setDataPedido(DEFAULT_DATA_PEDIDO);
        pedidos.setTipoPedido(DEFAULT_TIPO_PEDIDO);
        pedidos.setDescricao(DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    public void createPedidos() throws Exception {
        int databaseSizeBeforeCreate = pedidosRepository.findAll().size();

        // Create the Pedidos

        restPedidosMockMvc.perform(post("/api/pedidos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pedidos)))
                .andExpect(status().isCreated());

        // Validate the Pedidos in the database
        List<Pedidos> pedidos = pedidosRepository.findAll();
        assertThat(pedidos).hasSize(databaseSizeBeforeCreate + 1);
        Pedidos testPedidos = pedidos.get(pedidos.size() - 1);
        assertThat(testPedidos.getDataPedido()).isEqualTo(DEFAULT_DATA_PEDIDO);
        assertThat(testPedidos.getTipoPedido()).isEqualTo(DEFAULT_TIPO_PEDIDO);
        assertThat(testPedidos.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);

        // Validate the Pedidos in ElasticSearch
        Pedidos pedidosEs = pedidosSearchRepository.findOne(testPedidos.getId());
        assertThat(pedidosEs).isEqualToComparingFieldByField(testPedidos);
    }

    @Test
    @Transactional
    public void checkDataPedidoIsRequired() throws Exception {
        int databaseSizeBeforeTest = pedidosRepository.findAll().size();
        // set the field null
        pedidos.setDataPedido(null);

        // Create the Pedidos, which fails.

        restPedidosMockMvc.perform(post("/api/pedidos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pedidos)))
                .andExpect(status().isBadRequest());

        List<Pedidos> pedidos = pedidosRepository.findAll();
        assertThat(pedidos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTipoPedidoIsRequired() throws Exception {
        int databaseSizeBeforeTest = pedidosRepository.findAll().size();
        // set the field null
        pedidos.setTipoPedido(null);

        // Create the Pedidos, which fails.

        restPedidosMockMvc.perform(post("/api/pedidos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pedidos)))
                .andExpect(status().isBadRequest());

        List<Pedidos> pedidos = pedidosRepository.findAll();
        assertThat(pedidos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescricaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = pedidosRepository.findAll().size();
        // set the field null
        pedidos.setDescricao(null);

        // Create the Pedidos, which fails.

        restPedidosMockMvc.perform(post("/api/pedidos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pedidos)))
                .andExpect(status().isBadRequest());

        List<Pedidos> pedidos = pedidosRepository.findAll();
        assertThat(pedidos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPedidos() throws Exception {
        // Initialize the database
        pedidosRepository.saveAndFlush(pedidos);

        // Get all the pedidos
        restPedidosMockMvc.perform(get("/api/pedidos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(pedidos.getId().intValue())))
                .andExpect(jsonPath("$.[*].dataPedido").value(hasItem(DEFAULT_DATA_PEDIDO.toString())))
                .andExpect(jsonPath("$.[*].tipoPedido").value(hasItem(DEFAULT_TIPO_PEDIDO.toString())))
                .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())));
    }

    @Test
    @Transactional
    public void getPedidos() throws Exception {
        // Initialize the database
        pedidosRepository.saveAndFlush(pedidos);

        // Get the pedidos
        restPedidosMockMvc.perform(get("/api/pedidos/{id}", pedidos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(pedidos.getId().intValue()))
            .andExpect(jsonPath("$.dataPedido").value(DEFAULT_DATA_PEDIDO.toString()))
            .andExpect(jsonPath("$.tipoPedido").value(DEFAULT_TIPO_PEDIDO.toString()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPedidos() throws Exception {
        // Get the pedidos
        restPedidosMockMvc.perform(get("/api/pedidos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePedidos() throws Exception {
        // Initialize the database
        pedidosRepository.saveAndFlush(pedidos);
        pedidosSearchRepository.save(pedidos);
        int databaseSizeBeforeUpdate = pedidosRepository.findAll().size();

        // Update the pedidos
        Pedidos updatedPedidos = new Pedidos();
        updatedPedidos.setId(pedidos.getId());
        updatedPedidos.setDataPedido(UPDATED_DATA_PEDIDO);
        updatedPedidos.setTipoPedido(UPDATED_TIPO_PEDIDO);
        updatedPedidos.setDescricao(UPDATED_DESCRICAO);

        restPedidosMockMvc.perform(put("/api/pedidos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPedidos)))
                .andExpect(status().isOk());

        // Validate the Pedidos in the database
        List<Pedidos> pedidos = pedidosRepository.findAll();
        assertThat(pedidos).hasSize(databaseSizeBeforeUpdate);
        Pedidos testPedidos = pedidos.get(pedidos.size() - 1);
        assertThat(testPedidos.getDataPedido()).isEqualTo(UPDATED_DATA_PEDIDO);
        assertThat(testPedidos.getTipoPedido()).isEqualTo(UPDATED_TIPO_PEDIDO);
        assertThat(testPedidos.getDescricao()).isEqualTo(UPDATED_DESCRICAO);

        // Validate the Pedidos in ElasticSearch
        Pedidos pedidosEs = pedidosSearchRepository.findOne(testPedidos.getId());
        assertThat(pedidosEs).isEqualToComparingFieldByField(testPedidos);
    }

    @Test
    @Transactional
    public void deletePedidos() throws Exception {
        // Initialize the database
        pedidosRepository.saveAndFlush(pedidos);
        pedidosSearchRepository.save(pedidos);
        int databaseSizeBeforeDelete = pedidosRepository.findAll().size();

        // Get the pedidos
        restPedidosMockMvc.perform(delete("/api/pedidos/{id}", pedidos.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean pedidosExistsInEs = pedidosSearchRepository.exists(pedidos.getId());
        assertThat(pedidosExistsInEs).isFalse();

        // Validate the database is empty
        List<Pedidos> pedidos = pedidosRepository.findAll();
        assertThat(pedidos).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPedidos() throws Exception {
        // Initialize the database
        pedidosRepository.saveAndFlush(pedidos);
        pedidosSearchRepository.save(pedidos);

        // Search the pedidos
        restPedidosMockMvc.perform(get("/api/_search/pedidos?query=id:" + pedidos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pedidos.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataPedido").value(hasItem(DEFAULT_DATA_PEDIDO.toString())))
            .andExpect(jsonPath("$.[*].tipoPedido").value(hasItem(DEFAULT_TIPO_PEDIDO.toString())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())));
    }
}
