package br.com.rogrs.loja.web.rest;

import br.com.rogrs.loja.LojaApp;
import br.com.rogrs.loja.domain.GradeProdutos;
import br.com.rogrs.loja.repository.GradeProdutosRepository;
import br.com.rogrs.loja.repository.search.GradeProdutosSearchRepository;

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
 * Test class for the GradeProdutosResource REST controller.
 *
 * @see GradeProdutosResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = LojaApp.class)
@WebAppConfiguration
@IntegrationTest
public class GradeProdutosResourceIntTest {


    @Inject
    private GradeProdutosRepository gradeProdutosRepository;

    @Inject
    private GradeProdutosSearchRepository gradeProdutosSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restGradeProdutosMockMvc;

    private GradeProdutos gradeProdutos;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        GradeProdutosResource gradeProdutosResource = new GradeProdutosResource();
        ReflectionTestUtils.setField(gradeProdutosResource, "gradeProdutosSearchRepository", gradeProdutosSearchRepository);
        ReflectionTestUtils.setField(gradeProdutosResource, "gradeProdutosRepository", gradeProdutosRepository);
        this.restGradeProdutosMockMvc = MockMvcBuilders.standaloneSetup(gradeProdutosResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        gradeProdutosSearchRepository.deleteAll();
        gradeProdutos = new GradeProdutos();
    }

    @Test
    @Transactional
    public void createGradeProdutos() throws Exception {
        int databaseSizeBeforeCreate = gradeProdutosRepository.findAll().size();

        // Create the GradeProdutos

        restGradeProdutosMockMvc.perform(post("/api/grade-produtos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(gradeProdutos)))
                .andExpect(status().isCreated());

        // Validate the GradeProdutos in the database
        List<GradeProdutos> gradeProdutos = gradeProdutosRepository.findAll();
        assertThat(gradeProdutos).hasSize(databaseSizeBeforeCreate + 1);
        GradeProdutos testGradeProdutos = gradeProdutos.get(gradeProdutos.size() - 1);

        // Validate the GradeProdutos in ElasticSearch
        GradeProdutos gradeProdutosEs = gradeProdutosSearchRepository.findOne(testGradeProdutos.getId());
        assertThat(gradeProdutosEs).isEqualToComparingFieldByField(testGradeProdutos);
    }

    @Test
    @Transactional
    public void getAllGradeProdutos() throws Exception {
        // Initialize the database
        gradeProdutosRepository.saveAndFlush(gradeProdutos);

        // Get all the gradeProdutos
        restGradeProdutosMockMvc.perform(get("/api/grade-produtos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(gradeProdutos.getId().intValue())));
    }

    @Test
    @Transactional
    public void getGradeProdutos() throws Exception {
        // Initialize the database
        gradeProdutosRepository.saveAndFlush(gradeProdutos);

        // Get the gradeProdutos
        restGradeProdutosMockMvc.perform(get("/api/grade-produtos/{id}", gradeProdutos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(gradeProdutos.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingGradeProdutos() throws Exception {
        // Get the gradeProdutos
        restGradeProdutosMockMvc.perform(get("/api/grade-produtos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGradeProdutos() throws Exception {
        // Initialize the database
        gradeProdutosRepository.saveAndFlush(gradeProdutos);
        gradeProdutosSearchRepository.save(gradeProdutos);
        int databaseSizeBeforeUpdate = gradeProdutosRepository.findAll().size();

        // Update the gradeProdutos
        GradeProdutos updatedGradeProdutos = new GradeProdutos();
        updatedGradeProdutos.setId(gradeProdutos.getId());

        restGradeProdutosMockMvc.perform(put("/api/grade-produtos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedGradeProdutos)))
                .andExpect(status().isOk());

        // Validate the GradeProdutos in the database
        List<GradeProdutos> gradeProdutos = gradeProdutosRepository.findAll();
        assertThat(gradeProdutos).hasSize(databaseSizeBeforeUpdate);
        GradeProdutos testGradeProdutos = gradeProdutos.get(gradeProdutos.size() - 1);

        // Validate the GradeProdutos in ElasticSearch
        GradeProdutos gradeProdutosEs = gradeProdutosSearchRepository.findOne(testGradeProdutos.getId());
        assertThat(gradeProdutosEs).isEqualToComparingFieldByField(testGradeProdutos);
    }

    @Test
    @Transactional
    public void deleteGradeProdutos() throws Exception {
        // Initialize the database
        gradeProdutosRepository.saveAndFlush(gradeProdutos);
        gradeProdutosSearchRepository.save(gradeProdutos);
        int databaseSizeBeforeDelete = gradeProdutosRepository.findAll().size();

        // Get the gradeProdutos
        restGradeProdutosMockMvc.perform(delete("/api/grade-produtos/{id}", gradeProdutos.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean gradeProdutosExistsInEs = gradeProdutosSearchRepository.exists(gradeProdutos.getId());
        assertThat(gradeProdutosExistsInEs).isFalse();

        // Validate the database is empty
        List<GradeProdutos> gradeProdutos = gradeProdutosRepository.findAll();
        assertThat(gradeProdutos).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchGradeProdutos() throws Exception {
        // Initialize the database
        gradeProdutosRepository.saveAndFlush(gradeProdutos);
        gradeProdutosSearchRepository.save(gradeProdutos);

        // Search the gradeProdutos
        restGradeProdutosMockMvc.perform(get("/api/_search/grade-produtos?query=id:" + gradeProdutos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gradeProdutos.getId().intValue())));
    }
}
