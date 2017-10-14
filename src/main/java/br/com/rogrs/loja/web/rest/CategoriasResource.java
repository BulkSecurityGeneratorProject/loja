package br.com.rogrs.loja.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.rogrs.loja.domain.Categorias;

import br.com.rogrs.loja.repository.CategoriasRepository;
import br.com.rogrs.loja.repository.search.CategoriasSearchRepository;
import br.com.rogrs.loja.web.rest.util.HeaderUtil;
import br.com.rogrs.loja.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
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
 * REST controller for managing Categorias.
 */
@RestController
@RequestMapping("/api")
public class CategoriasResource {

    private final Logger log = LoggerFactory.getLogger(CategoriasResource.class);

    private static final String ENTITY_NAME = "categorias";

    private final CategoriasRepository categoriasRepository;

    private final CategoriasSearchRepository categoriasSearchRepository;

    public CategoriasResource(CategoriasRepository categoriasRepository, CategoriasSearchRepository categoriasSearchRepository) {
        this.categoriasRepository = categoriasRepository;
        this.categoriasSearchRepository = categoriasSearchRepository;
    }

    /**
     * POST  /categorias : Create a new categorias.
     *
     * @param categorias the categorias to create
     * @return the ResponseEntity with status 201 (Created) and with body the new categorias, or with status 400 (Bad Request) if the categorias has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/categorias")
    @Timed
    public ResponseEntity<Categorias> createCategorias(@Valid @RequestBody Categorias categorias) throws URISyntaxException {
        log.debug("REST request to save Categorias : {}", categorias);
        if (categorias.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new categorias cannot already have an ID")).body(null);
        }
        Categorias result = categoriasRepository.save(categorias);
        categoriasSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/categorias/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /categorias : Updates an existing categorias.
     *
     * @param categorias the categorias to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated categorias,
     * or with status 400 (Bad Request) if the categorias is not valid,
     * or with status 500 (Internal Server Error) if the categorias couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/categorias")
    @Timed
    public ResponseEntity<Categorias> updateCategorias(@Valid @RequestBody Categorias categorias) throws URISyntaxException {
        log.debug("REST request to update Categorias : {}", categorias);
        if (categorias.getId() == null) {
            return createCategorias(categorias);
        }
        Categorias result = categoriasRepository.save(categorias);
        categoriasSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, categorias.getId().toString()))
            .body(result);
    }

    /**
     * GET  /categorias : get all the categorias.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of categorias in body
     */
    @GetMapping("/categorias")
    @Timed
    public ResponseEntity<List<Categorias>> getAllCategorias(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Categorias");
        Page<Categorias> page = categoriasRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/categorias");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /categorias/:id : get the "id" categorias.
     *
     * @param id the id of the categorias to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the categorias, or with status 404 (Not Found)
     */
    @GetMapping("/categorias/{id}")
    @Timed
    public ResponseEntity<Categorias> getCategorias(@PathVariable Long id) {
        log.debug("REST request to get Categorias : {}", id);
        Categorias categorias = categoriasRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(categorias));
    }

    /**
     * DELETE  /categorias/:id : delete the "id" categorias.
     *
     * @param id the id of the categorias to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/categorias/{id}")
    @Timed
    public ResponseEntity<Void> deleteCategorias(@PathVariable Long id) {
        log.debug("REST request to delete Categorias : {}", id);
        categoriasRepository.delete(id);
        categoriasSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/categorias?query=:query : search for the categorias corresponding
     * to the query.
     *
     * @param query the query of the categorias search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/categorias")
    @Timed
    public ResponseEntity<List<Categorias>> searchCategorias(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Categorias for query {}", query);
        Page<Categorias> page = categoriasSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/categorias");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
