package br.com.rogrs.loja.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.rogrs.loja.domain.Cores;

import br.com.rogrs.loja.repository.CoresRepository;
import br.com.rogrs.loja.repository.search.CoresSearchRepository;
import br.com.rogrs.loja.web.rest.errors.BadRequestAlertException;
import br.com.rogrs.loja.web.rest.util.HeaderUtil;
import br.com.rogrs.loja.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    private static final String ENTITY_NAME = "cores";

    private final CoresRepository coresRepository;

    private final CoresSearchRepository coresSearchRepository;

    public CoresResource(CoresRepository coresRepository, CoresSearchRepository coresSearchRepository) {
        this.coresRepository = coresRepository;
        this.coresSearchRepository = coresSearchRepository;
    }

    /**
     * POST  /cores : Create a new cores.
     *
     * @param cores the cores to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cores, or with status 400 (Bad Request) if the cores has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cores")
    @Timed
    public ResponseEntity<Cores> createCores(@Valid @RequestBody Cores cores) throws URISyntaxException {
        log.debug("REST request to save Cores : {}", cores);
        if (cores.getId() != null) {
            throw new BadRequestAlertException("A new cores cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Cores result = coresRepository.save(cores);
        coresSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/cores/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cores : Updates an existing cores.
     *
     * @param cores the cores to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cores,
     * or with status 400 (Bad Request) if the cores is not valid,
     * or with status 500 (Internal Server Error) if the cores couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cores")
    @Timed
    public ResponseEntity<Cores> updateCores(@Valid @RequestBody Cores cores) throws URISyntaxException {
        log.debug("REST request to update Cores : {}", cores);
        if (cores.getId() == null) {
            return createCores(cores);
        }
        Cores result = coresRepository.save(cores);
        coresSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cores.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cores : get all the cores.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cores in body
     */
    @GetMapping("/cores")
    @Timed
    public ResponseEntity<List<Cores>> getAllCores(Pageable pageable) {
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
    @GetMapping("/cores/{id}")
    @Timed
    public ResponseEntity<Cores> getCores(@PathVariable Long id) {
        log.debug("REST request to get Cores : {}", id);
        Cores cores = coresRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cores));
    }

    /**
     * DELETE  /cores/:id : delete the "id" cores.
     *
     * @param id the id of the cores to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cores/{id}")
    @Timed
    public ResponseEntity<Void> deleteCores(@PathVariable Long id) {
        log.debug("REST request to delete Cores : {}", id);
        coresRepository.delete(id);
        coresSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/cores?query=:query : search for the cores corresponding
     * to the query.
     *
     * @param query the query of the cores search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/cores")
    @Timed
    public ResponseEntity<List<Cores>> searchCores(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Cores for query {}", query);
        Page<Cores> page = coresSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/cores");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
