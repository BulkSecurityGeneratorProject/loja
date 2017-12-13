package br.com.rogrs.loja.web.rest;

import br.com.rogrs.loja.LojaApp;

import br.com.rogrs.loja.domain.Pedidos;
import br.com.rogrs.loja.repository.PedidosRepository;
import br.com.rogrs.loja.repository.search.PedidosSearchRepository;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static br.com.rogrs.loja.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.rogrs.loja.domain.enumeration.TipoPedido;
/**
 * Test class for the PedidosResource REST controller.
 *
 * @see PedidosResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LojaApp.class)
public class PedidosResourceIntTest {

    private static final LocalDate DEFAULT_DATA_PEDIDO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_PEDIDO = LocalDate.now(ZoneId.systemDefault());

    private static final TipoPedido DEFAULT_TIPO_PEDIDO = TipoPedido.VENDA;
    private static final TipoPedido UPDATED_TIPO_PEDIDO = TipoPedido.COMPRA;

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    @Autowired
    private PedidosRepository pedidosRepository;

    @Autowired
    private PedidosSearchRepository pedidosSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPedidosMockMvc;

    private Pedidos pedidos;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PedidosResource pedidosResource = new PedidosResource(pedidosRepository, pedidosSearchRepository);
        this.restPedidosMockMvc = MockMvcBuilders.standaloneSetup(pedidosResource)
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
    public static Pedidos createEntity(EntityManager em) {
        Pedidos pedidos = new Pedidos()
            .dataPedido(DEFAULT_DATA_PEDIDO)
            .tipoPedido(DEFAULT_TIPO_PEDIDO)
            .descricao(DEFAULT_DESCRICAO);
        return pedidos;
    }

    @Before
    public void initTest() {
        pedidosSearchRepository.deleteAll();
        pedidos = createEntity(em);
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
        List<Pedidos> pedidosList = pedidosRepository.findAll();
        assertThat(pedidosList).hasSize(databaseSizeBeforeCreate + 1);
        Pedidos testPedidos = pedidosList.get(pedidosList.size() - 1);
        assertThat(testPedidos.getDataPedido()).isEqualTo(DEFAULT_DATA_PEDIDO);
        assertThat(testPedidos.getTipoPedido()).isEqualTo(DEFAULT_TIPO_PEDIDO);
        assertThat(testPedidos.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);

        // Validate the Pedidos in Elasticsearch
        Pedidos pedidosEs = pedidosSearchRepository.findOne(testPedidos.getId());
        assertThat(pedidosEs).isEqualToIgnoringGivenFields(testPedidos);
    }

    @Test
    @Transactional
    public void createPedidosWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pedidosRepository.findAll().size();

        // Create the Pedidos with an existing ID
        pedidos.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPedidosMockMvc.perform(post("/api/pedidos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pedidos)))
            .andExpect(status().isBadRequest());

        // Validate the Pedidos in the database
        List<Pedidos> pedidosList = pedidosRepository.findAll();
        assertThat(pedidosList).hasSize(databaseSizeBeforeCreate);
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

        List<Pedidos> pedidosList = pedidosRepository.findAll();
        assertThat(pedidosList).hasSize(databaseSizeBeforeTest);
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

        List<Pedidos> pedidosList = pedidosRepository.findAll();
        assertThat(pedidosList).hasSize(databaseSizeBeforeTest);
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

        List<Pedidos> pedidosList = pedidosRepository.findAll();
        assertThat(pedidosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPedidos() throws Exception {
        // Initialize the database
        pedidosRepository.saveAndFlush(pedidos);

        // Get all the pedidosList
        restPedidosMockMvc.perform(get("/api/pedidos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
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
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
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
        Pedidos updatedPedidos = pedidosRepository.findOne(pedidos.getId());
        // Disconnect from session so that the updates on updatedPedidos are not directly saved in db
        em.detach(updatedPedidos);
        updatedPedidos
            .dataPedido(UPDATED_DATA_PEDIDO)
            .tipoPedido(UPDATED_TIPO_PEDIDO)
            .descricao(UPDATED_DESCRICAO);

        restPedidosMockMvc.perform(put("/api/pedidos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPedidos)))
            .andExpect(status().isOk());

        // Validate the Pedidos in the database
        List<Pedidos> pedidosList = pedidosRepository.findAll();
        assertThat(pedidosList).hasSize(databaseSizeBeforeUpdate);
        Pedidos testPedidos = pedidosList.get(pedidosList.size() - 1);
        assertThat(testPedidos.getDataPedido()).isEqualTo(UPDATED_DATA_PEDIDO);
        assertThat(testPedidos.getTipoPedido()).isEqualTo(UPDATED_TIPO_PEDIDO);
        assertThat(testPedidos.getDescricao()).isEqualTo(UPDATED_DESCRICAO);

        // Validate the Pedidos in Elasticsearch
        Pedidos pedidosEs = pedidosSearchRepository.findOne(testPedidos.getId());
        assertThat(pedidosEs).isEqualToIgnoringGivenFields(testPedidos);
    }

    @Test
    @Transactional
    public void updateNonExistingPedidos() throws Exception {
        int databaseSizeBeforeUpdate = pedidosRepository.findAll().size();

        // Create the Pedidos

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPedidosMockMvc.perform(put("/api/pedidos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pedidos)))
            .andExpect(status().isCreated());

        // Validate the Pedidos in the database
        List<Pedidos> pedidosList = pedidosRepository.findAll();
        assertThat(pedidosList).hasSize(databaseSizeBeforeUpdate + 1);
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

        // Validate Elasticsearch is empty
        boolean pedidosExistsInEs = pedidosSearchRepository.exists(pedidos.getId());
        assertThat(pedidosExistsInEs).isFalse();

        // Validate the database is empty
        List<Pedidos> pedidosList = pedidosRepository.findAll();
        assertThat(pedidosList).hasSize(databaseSizeBeforeDelete - 1);
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
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pedidos.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataPedido").value(hasItem(DEFAULT_DATA_PEDIDO.toString())))
            .andExpect(jsonPath("$.[*].tipoPedido").value(hasItem(DEFAULT_TIPO_PEDIDO.toString())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pedidos.class);
        Pedidos pedidos1 = new Pedidos();
        pedidos1.setId(1L);
        Pedidos pedidos2 = new Pedidos();
        pedidos2.setId(pedidos1.getId());
        assertThat(pedidos1).isEqualTo(pedidos2);
        pedidos2.setId(2L);
        assertThat(pedidos1).isNotEqualTo(pedidos2);
        pedidos1.setId(null);
        assertThat(pedidos1).isNotEqualTo(pedidos2);
    }
}
