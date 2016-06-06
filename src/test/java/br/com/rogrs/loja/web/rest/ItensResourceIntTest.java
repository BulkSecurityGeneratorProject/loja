package br.com.rogrs.loja.web.rest;

import br.com.rogrs.loja.LojaApp;
import br.com.rogrs.loja.domain.Itens;
import br.com.rogrs.loja.repository.ItensRepository;
import br.com.rogrs.loja.repository.search.ItensSearchRepository;

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
import java.math.BigDecimal;;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the ItensResource REST controller.
 *
 * @see ItensResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = LojaApp.class)
@WebAppConfiguration
@IntegrationTest
public class ItensResourceIntTest {


    private static final Float DEFAULT_QTDE = 1F;
    private static final Float UPDATED_QTDE = 2F;

    private static final BigDecimal DEFAULT_VALOR = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR = new BigDecimal(2);

    private static final BigDecimal DEFAULT_VALOR_DESCONTO = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_DESCONTO = new BigDecimal(2);

    @Inject
    private ItensRepository itensRepository;

    @Inject
    private ItensSearchRepository itensSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restItensMockMvc;

    private Itens itens;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ItensResource itensResource = new ItensResource();
        ReflectionTestUtils.setField(itensResource, "itensSearchRepository", itensSearchRepository);
        ReflectionTestUtils.setField(itensResource, "itensRepository", itensRepository);
        this.restItensMockMvc = MockMvcBuilders.standaloneSetup(itensResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        itensSearchRepository.deleteAll();
        itens = new Itens();
        itens.setQtde(DEFAULT_QTDE);
        itens.setValor(DEFAULT_VALOR);
        itens.setValorDesconto(DEFAULT_VALOR_DESCONTO);
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
        List<Itens> itens = itensRepository.findAll();
        assertThat(itens).hasSize(databaseSizeBeforeCreate + 1);
        Itens testItens = itens.get(itens.size() - 1);
        assertThat(testItens.getQtde()).isEqualTo(DEFAULT_QTDE);
        assertThat(testItens.getValor()).isEqualTo(DEFAULT_VALOR);
        assertThat(testItens.getValorDesconto()).isEqualTo(DEFAULT_VALOR_DESCONTO);

        // Validate the Itens in ElasticSearch
        Itens itensEs = itensSearchRepository.findOne(testItens.getId());
        assertThat(itensEs).isEqualToComparingFieldByField(testItens);
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

        List<Itens> itens = itensRepository.findAll();
        assertThat(itens).hasSize(databaseSizeBeforeTest);
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

        List<Itens> itens = itensRepository.findAll();
        assertThat(itens).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllItens() throws Exception {
        // Initialize the database
        itensRepository.saveAndFlush(itens);

        // Get all the itens
        restItensMockMvc.perform(get("/api/itens?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
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
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
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
        Itens updatedItens = new Itens();
        updatedItens.setId(itens.getId());
        updatedItens.setQtde(UPDATED_QTDE);
        updatedItens.setValor(UPDATED_VALOR);
        updatedItens.setValorDesconto(UPDATED_VALOR_DESCONTO);

        restItensMockMvc.perform(put("/api/itens")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedItens)))
                .andExpect(status().isOk());

        // Validate the Itens in the database
        List<Itens> itens = itensRepository.findAll();
        assertThat(itens).hasSize(databaseSizeBeforeUpdate);
        Itens testItens = itens.get(itens.size() - 1);
        assertThat(testItens.getQtde()).isEqualTo(UPDATED_QTDE);
        assertThat(testItens.getValor()).isEqualTo(UPDATED_VALOR);
        assertThat(testItens.getValorDesconto()).isEqualTo(UPDATED_VALOR_DESCONTO);

        // Validate the Itens in ElasticSearch
        Itens itensEs = itensSearchRepository.findOne(testItens.getId());
        assertThat(itensEs).isEqualToComparingFieldByField(testItens);
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

        // Validate ElasticSearch is empty
        boolean itensExistsInEs = itensSearchRepository.exists(itens.getId());
        assertThat(itensExistsInEs).isFalse();

        // Validate the database is empty
        List<Itens> itens = itensRepository.findAll();
        assertThat(itens).hasSize(databaseSizeBeforeDelete - 1);
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
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itens.getId().intValue())))
            .andExpect(jsonPath("$.[*].qtde").value(hasItem(DEFAULT_QTDE.doubleValue())))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR.intValue())))
            .andExpect(jsonPath("$.[*].valorDesconto").value(hasItem(DEFAULT_VALOR_DESCONTO.intValue())));
    }
}
