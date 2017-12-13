package br.com.rogrs.loja.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.rogrs.loja.domain.Marcas;

import br.com.rogrs.loja.repository.MarcasRepository;
import br.com.rogrs.loja.repository.search.MarcasSearchRepository;
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
 * REST controller for managing Marcas.
 */
@RestController
@RequestMapping("/api")
public class MarcasResource {

    private final Logger log = LoggerFactory.getLogger(MarcasResource.class);

    private static final String ENTITY_NAME = "marcas";

    private final MarcasRepository marcasRepository;

    private final MarcasSearchRepository marcasSearchRepository;

    public MarcasResource(MarcasRepository marcasRepository, MarcasSearchRepository marcasSearchRepository) {
        this.marcasRepository = marcasRepository;
        this.marcasSearchRepository = marcasSearchRepository;
    }

    /**
     * POST  /marcas : Create a new marcas.
     *
     * @param marcas the marcas to create
     * @return the ResponseEntity with status 201 (Created) and with body the new marcas, or with status 400 (Bad Request) if the marcas has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/marcas")
    @Timed
    public ResponseEntity<Marcas> createMarcas(@Valid @RequestBody Marcas marcas) throws URISyntaxException {
        log.debug("REST request to save Marcas : {}", marcas);
        if (marcas.getId() != null) {
            throw new BadRequestAlertException("A new marcas cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Marcas result = marcasRepository.save(marcas);
        marcasSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/marcas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /marcas : Updates an existing marcas.
     *
     * @param marcas the marcas to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated marcas,
     * or with status 400 (Bad Request) if the marcas is not valid,
     * or with status 500 (Internal Server Error) if the marcas couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/marcas")
    @Timed
    public ResponseEntity<Marcas> updateMarcas(@Valid @RequestBody Marcas marcas) throws URISyntaxException {
        log.debug("REST request to update Marcas : {}", marcas);
        if (marcas.getId() == null) {
            return createMarcas(marcas);
        }
        Marcas result = marcasRepository.save(marcas);
        marcasSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, marcas.getId().toString()))
            .body(result);
    }

    /**
     * GET  /marcas : get all the marcas.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of marcas in body
     */
    @GetMapping("/marcas")
    @Timed
    public ResponseEntity<List<Marcas>> getAllMarcas(Pageable pageable) {
        log.debug("REST request to get a page of Marcas");
        Page<Marcas> page = marcasRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/marcas");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /marcas/:id : get the "id" marcas.
     *
     * @param id the id of the marcas to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the marcas, or with status 404 (Not Found)
     */
    @GetMapping("/marcas/{id}")
    @Timed
    public ResponseEntity<Marcas> getMarcas(@PathVariable Long id) {
        log.debug("REST request to get Marcas : {}", id);
        Marcas marcas = marcasRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(marcas));
    }

    /**
     * DELETE  /marcas/:id : delete the "id" marcas.
     *
     * @param id the id of the marcas to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/marcas/{id}")
    @Timed
    public ResponseEntity<Void> deleteMarcas(@PathVariable Long id) {
        log.debug("REST request to delete Marcas : {}", id);
        marcasRepository.delete(id);
        marcasSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/marcas?query=:query : search for the marcas corresponding
     * to the query.
     *
     * @param query the query of the marcas search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/marcas")
    @Timed
    public ResponseEntity<List<Marcas>> searchMarcas(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Marcas for query {}", query);
        Page<Marcas> page = marcasSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/marcas");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
