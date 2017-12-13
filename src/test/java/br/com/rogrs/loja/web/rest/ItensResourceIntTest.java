package br.com.rogrs.loja.web.rest;

import br.com.rogrs.loja.LojaApp;

import br.com.rogrs.loja.domain.Itens;
import br.com.rogrs.loja.repository.ItensRepository;
import br.com.rogrs.loja.repository.search.ItensSearchRepository;
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
import java.math.BigDecimal;
import java.util.List;

import static br.com.rogrs.loja.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ItensResource REST controller.
 *
 * @see ItensResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LojaApp.class)
public class ItensResourceIntTest {

    private static final Float DEFAULT_QTDE = 1F;
    private static final Float UPDATED_QTDE = 2F;

    private static final BigDecimal DEFAULT_VALOR = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR = new BigDecimal(2);

    private static final BigDecimal DEFAULT_VALOR_DESCONTO = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_DESCONTO = new BigDecimal(2);

    @Autowired
    private ItensRepository itensRepository;

    @Autowired
    private ItensSearchRepository itensSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restItensMockMvc;

    private Itens itens;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ItensResource itensResource = new ItensResource(itensRepository, itensSearchRepository);
        this.restItensMockMvc = MockMvcBuilders.standaloneSetup(itensResource)
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
    public static Itens createEntity(EntityManager em) {
        Itens itens = new Itens()
            .qtde(DEFAULT_QTDE)
            .valor(DEFAULT_VALOR)
            .valorDesconto(DEFAULT_VALOR_DESCONTO);
        return itens;
    }

    @Before
    public void initTest() {
        itensSearchRepository.deleteAll();
        itens = createEntity(em);
    }

    @Test
    @Transactional
    public void createItens() throws Exception {
        int databaseSizeBeforeCreate = itensRepository.findAll().size();

        // Create the Itens
        restItensMockMvc.perform(post("/api/itens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itens)))
            .andExpect(status().isCreated());

        // Validate the Itens in the database
        List<Itens> itensList = itensRepository.findAll();
        assertThat(itensList).hasSize(databaseSizeBeforeCreate + 1);
        Itens testItens = itensList.get(itensList.size() - 1);
        assertThat(testItens.getQtde()).isEqualTo(DEFAULT_QTDE);
        assertThat(testItens.getValor()).isEqualTo(DEFAULT_VALOR);
        assertThat(testItens.getValorDesconto()).isEqualTo(DEFAULT_VALOR_DESCONTO);

        // Validate the Itens in Elasticsearch
        Itens itensEs = itensSearchRepository.findOne(testItens.getId());
        assertThat(itensEs).isEqualToIgnoringGivenFields(testItens);
    }

    @Test
    @Transactional
    public void createItensWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = itensRepository.findAll().size();

        // Create the Itens with an existing ID
        itens.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restItensMockMvc.perform(post("/api/itens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itens)))
            .andExpect(status().isBadRequest());

        // Validate the Itens in the database
        List<Itens> itensList = itensRepository.findAll();
        assertThat(itensList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkQtdeIsRequired() throws Exception {
        int databaseSizeBeforeTest = itensRepository.findAll().size();
        // set the field null
        itens.setQtde(null);

        // Create the Itens, which fails.

        restItensMockMvc.perform(post("/api/itens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itens)))
            .andExpect(status().isBadRequest());

        List<Itens> itensList = itensRepository.findAll();
        assertThat(itensList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValorIsRequired() throws Exception {
        int databaseSizeBeforeTest = itensRepository.findAll().size();
        // set the field null
        itens.setValor(null);

        // Create the Itens, which fails.

        restItensMockMvc.perform(post("/api/itens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itens)))
            .andExpect(status().isBadRequest());

        List<Itens> itensList = itensRepository.findAll();
        assertThat(itensList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllItens() throws Exception {
        // Initialize the database
        itensRepository.saveAndFlush(itens);

        // Get all the itensList
        restItensMockMvc.perform(get("/api/itens?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itens.getId().intValue())))
            .andExpect(jsonPath("$.[*].qtde").value(hasItem(DEFAULT_QTDE.doubleValue())))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR.intValue())))
            .andExpect(jsonPath("$.[*].valorDesconto").value(hasItem(DEFAULT_VALOR_DESCONTO.intValue())));
    }

    @Test
    @Transactional
    public void getItens() throws Exception {
        // Initialize the database
        itensRepository.saveAndFlush(itens);

        // Get the itens
        restItensMockMvc.perform(get("/api/itens/{id}", itens.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(itens.getId().intValue()))
            .andExpect(jsonPath("$.qtde").value(DEFAULT_QTDE.doubleValue()))
            .andExpect(jsonPath("$.valor").value(DEFAULT_VALOR.intValue()))
            .andExpect(jsonPath("$.valorDesconto").value(DEFAULT_VALOR_DESCONTO.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingItens() throws Exception {
        // Get the itens
        restItensMockMvc.perform(get("/api/itens/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateItens() throws Exception {
        // Initialize the database
        itensRepository.saveAndFlush(itens);
        itensSearchRepository.save(itens);
        int databaseSizeBeforeUpdate = itensRepository.findAll().size();

        // Update the itens
        Itens updatedItens = itensRepository.findOne(itens.getId());
        // Disconnect from session so that the updates on updatedItens are not directly saved in db
        em.detach(updatedItens);
        updatedItens
            .qtde(UPDATED_QTDE)
            .valor(UPDATED_VALOR)
            .valorDesconto(UPDATED_VALOR_DESCONTO);

        restItensMockMvc.perform(put("/api/itens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedItens)))
            .andExpect(status().isOk());

        // Validate the Itens in the database
        List<Itens> itensList = itensRepository.findAll();
        assertThat(itensList).hasSize(databaseSizeBeforeUpdate);
        Itens testItens = itensList.get(itensList.size() - 1);
        assertThat(testItens.getQtde()).isEqualTo(UPDATED_QTDE);
        assertThat(testItens.getValor()).isEqualTo(UPDATED_VALOR);
        assertThat(testItens.getValorDesconto()).isEqualTo(UPDATED_VALOR_DESCONTO);

        // Validate the Itens in Elasticsearch
        Itens itensEs = itensSearchRepository.findOne(testItens.getId());
        assertThat(itensEs).isEqualToIgnoringGivenFields(testItens);
    }

    @Test
    @Transactional
    public void updateNonExistingItens() throws Exception {
        int databaseSizeBeforeUpdate = itensRepository.findAll().size();

        // Create the Itens

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restItensMockMvc.perform(put("/api/itens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itens)))
            .andExpect(status().isCreated());

        // Validate the Itens in the database
        List<Itens> itensList = itensRepository.findAll();
        assertThat(itensList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteItens() throws Exception {
        // Initialize the database
        itensRepository.saveAndFlush(itens);
        itensSearchRepository.save(itens);
        int databaseSizeBeforeDelete = itensRepository.findAll().size();

        // Get the itens
        restItensMockMvc.perform(delete("/api/itens/{id}", itens.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean itensExistsInEs = itensSearchRepository.exists(itens.getId());
        assertThat(itensExistsInEs).isFalse();

        // Validate the database is empty
        List<Itens> itensList = itensRepository.findAll();
        assertThat(itensList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchItens() throws Exception {
        // Initialize the database
        itensRepository.saveAndFlush(itens);
        itensSearchRepository.save(itens);

        // Search the itens
        restItensMockMvc.perform(get("/api/_search/itens?query=id:" + itens.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itens.getId().intValue())))
            .andExpect(jsonPath("$.[*].qtde").value(hasItem(DEFAULT_QTDE.doubleValue())))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR.intValue())))
            .andExpect(jsonPath("$.[*].valorDesconto").value(hasItem(DEFAULT_VALOR_DESCONTO.intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Itens.class);
        Itens itens1 = new Itens();
        itens1.setId(1L);
        Itens itens2 = new Itens();
        itens2.setId(itens1.getId());
        assertThat(itens1).isEqualTo(itens2);
        itens2.setId(2L);
        assertThat(itens1).isNotEqualTo(itens2);
        itens1.setId(null);
        assertThat(itens1).isNotEqualTo(itens2);
    }
}
