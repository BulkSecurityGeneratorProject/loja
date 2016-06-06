package br.com.rogrs.loja.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.rogrs.loja.domain.Pedidos;
import br.com.rogrs.loja.repository.PedidosRepository;
import br.com.rogrs.loja.repository.search.PedidosSearchRepository;
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
 * REST controller for managing Pedidos.
 */
@RestController
@RequestMapping("/api")
public class PedidosResource {

    private final Logger log = LoggerFactory.getLogger(PedidosResource.class);
        
    @Inject
    private PedidosRepository pedidosRepository;
    
    @Inject
    private PedidosSearchRepository pedidosSearchRepository;
    
    /**
     * POST  /pedidos : Create a new pedidos.
     *
     * @param pedidos the pedidos to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pedidos, or with status 400 (Bad Request) if the pedidos has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/pedidos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Pedidos> createPedidos(@Valid @RequestBody Pedidos pedidos) throws URISyntaxException {
        log.debug("REST request to save Pedidos : {}", pedidos);
        if (pedidos.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("pedidos", "idexists", "A new pedidos cannot already have an ID")).body(null);
        }
        Pedidos result = pedidosRepository.save(pedidos);
        pedidosSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/pedidos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("pedidos", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pedidos : Updates an existing pedidos.
     *
     * @param pedidos the pedidos to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pedidos,
     * or with status 400 (Bad Request) if the pedidos is not valid,
     * or with status 500 (Internal Server Error) if the pedidos couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/pedidos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Pedidos> updatePedidos(@Valid @RequestBody Pedidos pedidos) throws URISyntaxException {
        log.debug("REST request to update Pedidos : {}", pedidos);
        if (pedidos.getId() == null) {
            return createPedidos(pedidos);
        }
        Pedidos result = pedidosRepository.save(pedidos);
        pedidosSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("pedidos", pedidos.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pedidos : get all the pedidos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of pedidos in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/pedidos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Pedidos>> getAllPedidos(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Pedidos");
        Page<Pedidos> page = pedidosRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pedidos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /pedidos/:id : get the "id" pedidos.
     *
     * @param id the id of the pedidos to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pedidos, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/pedidos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Pedidos> getPedidos(@PathVariable Long id) {
        log.debug("REST request to get Pedidos : {}", id);
        Pedidos pedidos = pedidosRepository.findOne(id);
        return Optional.ofNullable(pedidos)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /pedidos/:id : delete the "id" pedidos.
     *
     * @param id the id of the pedidos to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/pedidos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePedidos(@PathVariable Long id) {
        log.debug("REST request to delete Pedidos : {}", id);
        pedidosRepository.delete(id);
        pedidosSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("pedidos", id.toString())).build();
    }

    /**
     * SEARCH  /_search/pedidos?query=:query : search for the pedidos corresponding
     * to the query.
     *
     * @param query the query of the pedidos search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/pedidos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Pedidos>> searchPedidos(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Pedidos for query {}", query);
        Page<Pedidos> page = pedidosSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/pedidos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
