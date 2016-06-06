package br.com.rogrs.loja.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.rogrs.loja.domain.Categorias;
import br.com.rogrs.loja.repository.CategoriasRepository;
import br.com.rogrs.loja.repository.search.CategoriasSearchRepository;
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
 * REST controller for managing Categorias.
 */
@RestController
@RequestMapping("/api")
public class CategoriasResource {

    private final Logger log = LoggerFactory.getLogger(CategoriasResource.class);
        
    @Inject
    private CategoriasRepository categoriasRepository;
    
    @Inject
    private CategoriasSearchRepository categoriasSearchRepository;
    
    /**
     * POST  /categorias : Create a new categorias.
     *
     * @param categorias the categorias to create
     * @return the ResponseEntity with status 201 (Created) and with body the new categorias, or with status 400 (Bad Request) if the categorias has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/categorias",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Categorias> createCategorias(@Valid @RequestBody Categorias categorias) throws URISyntaxException {
        log.debug("REST request to save Categorias : {}", categorias);
        if (categorias.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("categorias", "idexists", "A new categorias cannot already have an ID")).body(null);
        }
        Categorias result = categoriasRepository.save(categorias);
        categoriasSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/categorias/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("categorias", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /categorias : Updates an existing categorias.
     *
     * @param categorias the categorias to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated categorias,
     * or with status 400 (Bad Request) if the categorias is not valid,
     * or with status 500 (Internal Server Error) if the categorias couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/categorias",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Categorias> updateCategorias(@Valid @RequestBody Categorias categorias) throws URISyntaxException {
        log.debug("REST request to update Categorias : {}", categorias);
        if (categorias.getId() == null) {
            return createCategorias(categorias);
        }
        Categorias result = categoriasRepository.save(categorias);
        categoriasSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("categorias", categorias.getId().toString()))
            .body(result);
    }

    /**
     * GET  /categorias : get all the categorias.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of categorias in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/categorias",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Categorias>> getAllCategorias(Pageable pageable)
        throws URISyntaxException {
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
    @RequestMapping(value = "/categorias/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Categorias> getCategorias(@PathVariable Long id) {
        log.debug("REST request to get Categorias : {}", id);
        Categorias categorias = categoriasRepository.findOne(id);
        return Optional.ofNullable(categorias)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /categorias/:id : delete the "id" categorias.
     *
     * @param id the id of the categorias to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/categorias/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCategorias(@PathVariable Long id) {
        log.debug("REST request to delete Categorias : {}", id);
        categoriasRepository.delete(id);
        categoriasSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("categorias", id.toString())).build();
    }

    /**
     * SEARCH  /_search/categorias?query=:query : search for the categorias corresponding
     * to the query.
     *
     * @param query the query of the categorias search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/categorias",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Categorias>> searchCategorias(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Categorias for query {}", query);
        Page<Categorias> page = categoriasSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/categorias");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
