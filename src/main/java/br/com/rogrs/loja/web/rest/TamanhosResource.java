package br.com.rogrs.loja.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.rogrs.loja.domain.Tamanhos;

import br.com.rogrs.loja.repository.TamanhosRepository;
import br.com.rogrs.loja.repository.search.TamanhosSearchRepository;
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
 * REST controller for managing Tamanhos.
 */
@RestController
@RequestMapping("/api")
public class TamanhosResource {

    private final Logger log = LoggerFactory.getLogger(TamanhosResource.class);

    private static final String ENTITY_NAME = "tamanhos";

    private final TamanhosRepository tamanhosRepository;

    private final TamanhosSearchRepository tamanhosSearchRepository;

    public TamanhosResource(TamanhosRepository tamanhosRepository, TamanhosSearchRepository tamanhosSearchRepository) {
        this.tamanhosRepository = tamanhosRepository;
        this.tamanhosSearchRepository = tamanhosSearchRepository;
    }

    /**
     * POST  /tamanhos : Create a new tamanhos.
     *
     * @param tamanhos the tamanhos to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tamanhos, or with status 400 (Bad Request) if the tamanhos has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tamanhos")
    @Timed
    public ResponseEntity<Tamanhos> createTamanhos(@Valid @RequestBody Tamanhos tamanhos) throws URISyntaxException {
        log.debug("REST request to save Tamanhos : {}", tamanhos);
        if (tamanhos.getId() != null) {
            throw new BadRequestAlertException("A new tamanhos cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Tamanhos result = tamanhosRepository.save(tamanhos);
        tamanhosSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/tamanhos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tamanhos : Updates an existing tamanhos.
     *
     * @param tamanhos the tamanhos to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tamanhos,
     * or with status 400 (Bad Request) if the tamanhos is not valid,
     * or with status 500 (Internal Server Error) if the tamanhos couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tamanhos")
    @Timed
    public ResponseEntity<Tamanhos> updateTamanhos(@Valid @RequestBody Tamanhos tamanhos) throws URISyntaxException {
        log.debug("REST request to update Tamanhos : {}", tamanhos);
        if (tamanhos.getId() == null) {
            return createTamanhos(tamanhos);
        }
        Tamanhos result = tamanhosRepository.save(tamanhos);
        tamanhosSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tamanhos.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tamanhos : get all the tamanhos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tamanhos in body
     */
    @GetMapping("/tamanhos")
    @Timed
    public ResponseEntity<List<Tamanhos>> getAllTamanhos(Pageable pageable) {
        log.debug("REST request to get a page of Tamanhos");
        Page<Tamanhos> page = tamanhosRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tamanhos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /tamanhos/:id : get the "id" tamanhos.
     *
     * @param id the id of the tamanhos to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tamanhos, or with status 404 (Not Found)
     */
    @GetMapping("/tamanhos/{id}")
    @Timed
    public ResponseEntity<Tamanhos> getTamanhos(@PathVariable Long id) {
        log.debug("REST request to get Tamanhos : {}", id);
        Tamanhos tamanhos = tamanhosRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(tamanhos));
    }

    /**
     * DELETE  /tamanhos/:id : delete the "id" tamanhos.
     *
     * @param id the id of the tamanhos to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tamanhos/{id}")
    @Timed
    public ResponseEntity<Void> deleteTamanhos(@PathVariable Long id) {
        log.debug("REST request to delete Tamanhos : {}", id);
        tamanhosRepository.delete(id);
        tamanhosSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/tamanhos?query=:query : search for the tamanhos corresponding
     * to the query.
     *
     * @param query the query of the tamanhos search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/tamanhos")
    @Timed
    public ResponseEntity<List<Tamanhos>> searchTamanhos(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Tamanhos for query {}", query);
        Page<Tamanhos> page = tamanhosSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/tamanhos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
