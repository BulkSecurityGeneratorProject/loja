package br.com.rogrs.loja.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.rogrs.loja.domain.Produtos;
import br.com.rogrs.loja.repository.ProdutosRepository;
import br.com.rogrs.loja.repository.search.ProdutosSearchRepository;
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
 * REST controller for managing Produtos.
 */
@RestController
@RequestMapping("/api")
public class ProdutosResource {

    private final Logger log = LoggerFactory.getLogger(ProdutosResource.class);
        
    @Inject
    private ProdutosRepository produtosRepository;
    
    @Inject
    private ProdutosSearchRepository produtosSearchRepository;
    
    /**
     * POST  /produtos : Create a new produtos.
     *
     * @param produtos the produtos to create
     * @return the ResponseEntity with status 201 (Created) and with body the new produtos, or with status 400 (Bad Request) if the produtos has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/produtos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Produtos> createProdutos(@Valid @RequestBody Produtos produtos) throws URISyntaxException {
        log.debug("REST request to save Produtos : {}", produtos);
        if (produtos.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("produtos", "idexists", "A new produtos cannot already have an ID")).body(null);
        }
        Produtos result = produtosRepository.save(produtos);
        produtosSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/produtos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("produtos", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /produtos : Updates an existing produtos.
     *
     * @param produtos the produtos to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated produtos,
     * or with status 400 (Bad Request) if the produtos is not valid,
     * or with status 500 (Internal Server Error) if the produtos couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/produtos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Produtos> updateProdutos(@Valid @RequestBody Produtos produtos) throws URISyntaxException {
        log.debug("REST request to update Produtos : {}", produtos);
        if (produtos.getId() == null) {
            return createProdutos(produtos);
        }
        Produtos result = produtosRepository.save(produtos);
        produtosSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("produtos", produtos.getId().toString()))
            .body(result);
    }

    /**
     * GET  /produtos : get all the produtos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of produtos in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/produtos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Produtos>> getAllProdutos(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Produtos");
        Page<Produtos> page = produtosRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/produtos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /produtos/:id : get the "id" produtos.
     *
     * @param id the id of the produtos to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the produtos, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/produtos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Produtos> getProdutos(@PathVariable Long id) {
        log.debug("REST request to get Produtos : {}", id);
        Produtos produtos = produtosRepository.findOne(id);
        return Optional.ofNullable(produtos)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /produtos/:id : delete the "id" produtos.
     *
     * @param id the id of the produtos to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/produtos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteProdutos(@PathVariable Long id) {
        log.debug("REST request to delete Produtos : {}", id);
        produtosRepository.delete(id);
        produtosSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("produtos", id.toString())).build();
    }

    /**
     * SEARCH  /_search/produtos?query=:query : search for the produtos corresponding
     * to the query.
     *
     * @param query the query of the produtos search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/produtos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Produtos>> searchProdutos(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Produtos for query {}", query);
        Page<Produtos> page = produtosSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/produtos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
