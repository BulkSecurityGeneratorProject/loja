package br.com.rogrs.loja.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.rogrs.loja.domain.Localidades;

import br.com.rogrs.loja.repository.LocalidadesRepository;
import br.com.rogrs.loja.repository.search.LocalidadesSearchRepository;
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
 * REST controller for managing Localidades.
 */
@RestController
@RequestMapping("/api")
public class LocalidadesResource {

    private final Logger log = LoggerFactory.getLogger(LocalidadesResource.class);

    private static final String ENTITY_NAME = "localidades";

    private final LocalidadesRepository localidadesRepository;

    private final LocalidadesSearchRepository localidadesSearchRepository;

    public LocalidadesResource(LocalidadesRepository localidadesRepository, LocalidadesSearchRepository localidadesSearchRepository) {
        this.localidadesRepository = localidadesRepository;
        this.localidadesSearchRepository = localidadesSearchRepository;
    }

    /**
     * POST  /localidades : Create a new localidades.
     *
     * @param localidades the localidades to create
     * @return the ResponseEntity with status 201 (Created) and with body the new localidades, or with status 400 (Bad Request) if the localidades has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/localidades")
    @Timed
    public ResponseEntity<Localidades> createLocalidades(@Valid @RequestBody Localidades localidades) throws URISyntaxException {
        log.debug("REST request to save Localidades : {}", localidades);
        if (localidades.getId() != null) {
            throw new BadRequestAlertException("A new localidades cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Localidades result = localidadesRepository.save(localidades);
        localidadesSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/localidades/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /localidades : Updates an existing localidades.
     *
     * @param localidades the localidades to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated localidades,
     * or with status 400 (Bad Request) if the localidades is not valid,
     * or with status 500 (Internal Server Error) if the localidades couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/localidades")
    @Timed
    public ResponseEntity<Localidades> updateLocalidades(@Valid @RequestBody Localidades localidades) throws URISyntaxException {
        log.debug("REST request to update Localidades : {}", localidades);
        if (localidades.getId() == null) {
            return createLocalidades(localidades);
        }
        Localidades result = localidadesRepository.save(localidades);
        localidadesSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, localidades.getId().toString()))
            .body(result);
    }

    /**
     * GET  /localidades : get all the localidades.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of localidades in body
     */
    @GetMapping("/localidades")
    @Timed
    public ResponseEntity<List<Localidades>> getAllLocalidades(Pageable pageable) {
        log.debug("REST request to get a page of Localidades");
        Page<Localidades> page = localidadesRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/localidades");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /localidades/:id : get the "id" localidades.
     *
     * @param id the id of the localidades to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the localidades, or with status 404 (Not Found)
     */
    @GetMapping("/localidades/{id}")
    @Timed
    public ResponseEntity<Localidades> getLocalidades(@PathVariable Long id) {
        log.debug("REST request to get Localidades : {}", id);
        Localidades localidades = localidadesRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(localidades));
    }

    /**
     * DELETE  /localidades/:id : delete the "id" localidades.
     *
     * @param id the id of the localidades to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/localidades/{id}")
    @Timed
    public ResponseEntity<Void> deleteLocalidades(@PathVariable Long id) {
        log.debug("REST request to delete Localidades : {}", id);
        localidadesRepository.delete(id);
        localidadesSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/localidades?query=:query : search for the localidades corresponding
     * to the query.
     *
     * @param query the query of the localidades search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/localidades")
    @Timed
    public ResponseEntity<List<Localidades>> searchLocalidades(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Localidades for query {}", query);
        Page<Localidades> page = localidadesSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/localidades");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
