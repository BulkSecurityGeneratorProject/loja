package br.com.rogrs.loja.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.rogrs.loja.domain.GradeProdutos;
import br.com.rogrs.loja.repository.GradeProdutosRepository;
import br.com.rogrs.loja.repository.search.GradeProdutosSearchRepository;
import br.com.rogrs.loja.web.rest.util.HeaderUtil;
import br.com.rogrs.loja.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing GradeProdutos.
 */
@RestController
@RequestMapping("/api")
public class GradeProdutosResource {

    private final Logger log = LoggerFactory.getLogger(GradeProdutosResource.class);
        
    @Inject
    private GradeProdutosRepository gradeProdutosRepository;
    
    @Inject
    private GradeProdutosSearchRepository gradeProdutosSearchRepository;
    
    /**
     * POST  /grade-produtos : Create a new gradeProdutos.
     *
     * @param gradeProdutos the gradeProdutos to create
     * @return the ResponseEntity with status 201 (Created) and with body the new gradeProdutos, or with status 400 (Bad Request) if the gradeProdutos has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/grade-produtos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<GradeProdutos> createGradeProdutos(@RequestBody GradeProdutos gradeProdutos) throws URISyntaxException {
        log.debug("REST request to save GradeProdutos : {}", gradeProdutos);
        if (gradeProdutos.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("gradeProdutos", "idexists", "A new gradeProdutos cannot already have an ID")).body(null);
        }
        GradeProdutos result = gradeProdutosRepository.save(gradeProdutos);
        gradeProdutosSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/grade-produtos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("gradeProdutos", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /grade-produtos : Updates an existing gradeProdutos.
     *
     * @param gradeProdutos the gradeProdutos to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated gradeProdutos,
     * or with status 400 (Bad Request) if the gradeProdutos is not valid,
     * or with status 500 (Internal Server Error) if the gradeProdutos couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/grade-produtos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<GradeProdutos> updateGradeProdutos(@RequestBody GradeProdutos gradeProdutos) throws URISyntaxException {
        log.debug("REST request to update GradeProdutos : {}", gradeProdutos);
        if (gradeProdutos.getId() == null) {
            return createGradeProdutos(gradeProdutos);
        }
        GradeProdutos result = gradeProdutosRepository.save(gradeProdutos);
        gradeProdutosSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("gradeProdutos", gradeProdutos.getId().toString()))
            .body(result);
    }

    /**
     * GET  /grade-produtos : get all the gradeProdutos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of gradeProdutos in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/grade-produtos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<GradeProdutos>> getAllGradeProdutos(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of GradeProdutos");
        Page<GradeProdutos> page = gradeProdutosRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/grade-produtos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /grade-produtos/:id : get the "id" gradeProdutos.
     *
     * @param id the id of the gradeProdutos to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the gradeProdutos, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/grade-produtos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<GradeProdutos> getGradeProdutos(@PathVariable Long id) {
        log.debug("REST request to get GradeProdutos : {}", id);
        GradeProdutos gradeProdutos = gradeProdutosRepository.findOne(id);
        return Optional.ofNullable(gradeProdutos)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /grade-produtos/:id : delete the "id" gradeProdutos.
     *
     * @param id the id of the gradeProdutos to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/grade-produtos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteGradeProdutos(@PathVariable Long id) {
        log.debug("REST request to delete GradeProdutos : {}", id);
        gradeProdutosRepository.delete(id);
        gradeProdutosSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("gradeProdutos", id.toString())).build();
    }

    /**
     * SEARCH  /_search/grade-produtos?query=:query : search for the gradeProdutos corresponding
     * to the query.
     *
     * @param query the query of the gradeProdutos search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/grade-produtos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<GradeProdutos>> searchGradeProdutos(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of GradeProdutos for query {}", query);
        Page<GradeProdutos> page = gradeProdutosSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/grade-produtos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
