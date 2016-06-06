package br.com.rogrs.loja.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.rogrs.loja.domain.Cores;
import br.com.rogrs.loja.repository.CoresRepository;
import br.com.rogrs.loja.repository.search.CoresSearchRepository;
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
 * REST controller for managing Cores.
 */
@RestController
@RequestMapping("/api")
public class CoresResource {

    private final Logger log = LoggerFactory.getLogger(CoresResource.class);
        
    @Inject
    private CoresRepository coresRepository;
    
    @Inject
    private CoresSearchRepository coresSearchRepository;
    
    /**
     * POST  /cores : Create a new cores.
     *
     * @param cores the cores to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cores, or with status 400 (Bad Request) if the cores has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/cores",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Cores> createCores(@Valid @RequestBody Cores cores) throws URISyntaxException {
        log.debug("REST request to save Cores : {}", cores);
        if (cores.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("cores", "idexists", "A new cores cannot already have an ID")).body(null);
        }
        Cores result = coresRepository.save(cores);
        coresSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/cores/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("cores", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cores : Updates an existing cores.
     *
     * @param cores the cores to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cores,
     * or with status 400 (Bad Request) if the cores is not valid,
     * or with status 500 (Internal Server Error) if the cores couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/cores",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Cores> updateCores(@Valid @RequestBody Cores cores) throws URISyntaxException {
        log.debug("REST request to update Cores : {}", cores);
        if (cores.getId() == null) {
            return createCores(cores);
        }
        Cores result = coresRepository.save(cores);
        coresSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("cores", cores.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cores : get all the cores.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cores in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/cores",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Cores>> getAllCores(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Cores");
        Page<Cores> page = coresRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cores");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /cores/:id : get the "id" cores.
     *
     * @param id the id of the cores to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cores, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/cores/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Cores> getCores(@PathVariable Long id) {
        log.debug("REST request to get Cores : {}", id);
        Cores cores = coresRepository.findOne(id);
        return Optional.ofNullable(cores)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /cores/:id : delete the "id" cores.
     *
     * @param id the id of the cores to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/cores/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCores(@PathVariable Long id) {
        log.debug("REST request to delete Cores : {}", id);
        coresRepository.delete(id);
        coresSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("cores", id.toString())).build();
    }

    /**
     * SEARCH  /_search/cores?query=:query : search for the cores corresponding
     * to the query.
     *
     * @param query the query of the cores search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/cores",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Cores>> searchCores(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Cores for query {}", query);
        Page<Cores> page = coresSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/cores");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
