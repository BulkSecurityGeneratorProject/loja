package br.com.rogrs.loja.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.rogrs.loja.domain.Fornecedor;
import br.com.rogrs.loja.repository.FornecedorRepository;
import br.com.rogrs.loja.repository.search.FornecedorSearchRepository;
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
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Fornecedor.
 */
@RestController
@RequestMapping("/api")
public class FornecedorResource {

    private final Logger log = LoggerFactory.getLogger(FornecedorResource.class);
        
    @Inject
    private FornecedorRepository fornecedorRepository;
    
    @Inject
    private FornecedorSearchRepository fornecedorSearchRepository;
    
    /**
     * POST  /fornecedors : Create a new fornecedor.
     *
     * @param fornecedor the fornecedor to create
     * @return the ResponseEntity with status 201 (Created) and with body the new fornecedor, or with status 400 (Bad Request) if the fornecedor has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/fornecedors",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Fornecedor> createFornecedor(@Valid @RequestBody Fornecedor fornecedor) throws URISyntaxException {
        log.debug("REST request to save Fornecedor : {}", fornecedor);
        if (fornecedor.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("fornecedor", "idexists", "A new fornecedor cannot already have an ID")).body(null);
        }
        Fornecedor result = fornecedorRepository.save(fornecedor);
        fornecedorSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/fornecedors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("fornecedor", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /fornecedors : Updates an existing fornecedor.
     *
     * @param fornecedor the fornecedor to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated fornecedor,
     * or with status 400 (Bad Request) if the fornecedor is not valid,
     * or with status 500 (Internal Server Error) if the fornecedor couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/fornecedors",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Fornecedor> updateFornecedor(@Valid @RequestBody Fornecedor fornecedor) throws URISyntaxException {
        log.debug("REST request to update Fornecedor : {}", fornecedor);
        if (fornecedor.getId() == null) {
            return createFornecedor(fornecedor);
        }
        Fornecedor result = fornecedorRepository.save(fornecedor);
        fornecedorSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("fornecedor", fornecedor.getId().toString()))
            .body(result);
    }

    /**
     * GET  /fornecedors : get all the fornecedors.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of fornecedors in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/fornecedors",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Fornecedor>> getAllFornecedors(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Fornecedors");
        Page<Fornecedor> page = fornecedorRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/fornecedors");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /fornecedors/:id : get the "id" fornecedor.
     *
     * @param id the id of the fornecedor to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the fornecedor, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/fornecedors/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Fornecedor> getFornecedor(@PathVariable Long id) {
        log.debug("REST request to get Fornecedor : {}", id);
        Fornecedor fornecedor = fornecedorRepository.findOne(id);
        return Optional.ofNullable(fornecedor)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /fornecedors/:id : delete the "id" fornecedor.
     *
     * @param id the id of the fornecedor to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/fornecedors/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteFornecedor(@PathVariable Long id) {
        log.debug("REST request to delete Fornecedor : {}", id);
        fornecedorRepository.delete(id);
        fornecedorSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("fornecedor", id.toString())).build();
    }

    /**
     * SEARCH  /_search/fornecedors?query=:query : search for the fornecedor corresponding
     * to the query.
     *
     * @param query the query of the fornecedor search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/fornecedors",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Fornecedor>> searchFornecedors(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Fornecedors for query {}", query);
        Page<Fornecedor> page = fornecedorSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/fornecedors");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
