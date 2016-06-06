package br.com.rogrs.loja.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.rogrs.loja.domain.Itens;
import br.com.rogrs.loja.repository.ItensRepository;
import br.com.rogrs.loja.repository.search.ItensSearchRepository;
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
 * REST controller for managing Itens.
 */
@RestController
@RequestMapping("/api")
public class ItensResource {

    private final Logger log = LoggerFactory.getLogger(ItensResource.class);
        
    @Inject
    private ItensRepository itensRepository;
    
    @Inject
    private ItensSearchRepository itensSearchRepository;
    
    /**
     * POST  /itens : Create a new itens.
     *
     * @param itens the itens to create
     * @return the ResponseEntity with status 201 (Created) and with body the new itens, or with status 400 (Bad Request) if the itens has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/itens",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Itens> createItens(@Valid @RequestBody Itens itens) throws URISyntaxException {
        log.debug("REST request to save Itens : {}", itens);
        if (itens.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("itens", "idexists", "A new itens cannot already have an ID")).body(null);
        }
        Itens result = itensRepository.save(itens);
        itensSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/itens/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("itens", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /itens : Updates an existing itens.
     *
     * @param itens the itens to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated itens,
     * or with status 400 (Bad Request) if the itens is not valid,
     * or with status 500 (Internal Server Error) if the itens couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/itens",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Itens> updateItens(@Valid @RequestBody Itens itens) throws URISyntaxException {
        log.debug("REST request to update Itens : {}", itens);
        if (itens.getId() == null) {
            return createItens(itens);
        }
        Itens result = itensRepository.save(itens);
        itensSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("itens", itens.getId().toString()))
            .body(result);
    }

    /**
     * GET  /itens : get all the itens.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of itens in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/itens",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Itens>> getAllItens(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Itens");
        Page<Itens> page = itensRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/itens");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /itens/:id : get the "id" itens.
     *
     * @param id the id of the itens to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the itens, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/itens/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Itens> getItens(@PathVariable Long id) {
        log.debug("REST request to get Itens : {}", id);
        Itens itens = itensRepository.findOne(id);
        return Optional.ofNullable(itens)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /itens/:id : delete the "id" itens.
     *
     * @param id the id of the itens to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/itens/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteItens(@PathVariable Long id) {
        log.debug("REST request to delete Itens : {}", id);
        itensRepository.delete(id);
        itensSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("itens", id.toString())).build();
    }

    /**
     * SEARCH  /_search/itens?query=:query : search for the itens corresponding
     * to the query.
     *
     * @param query the query of the itens search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/itens",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Itens>> searchItens(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Itens for query {}", query);
        Page<Itens> page = itensSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/itens");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
