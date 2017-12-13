package br.com.rogrs.loja.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.rogrs.loja.domain.Cadastros;

import br.com.rogrs.loja.repository.CadastrosRepository;
import br.com.rogrs.loja.repository.search.CadastrosSearchRepository;
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
 * REST controller for managing Cadastros.
 */
@RestController
@RequestMapping("/api")
public class CadastrosResource {

    private final Logger log = LoggerFactory.getLogger(CadastrosResource.class);

    private static final String ENTITY_NAME = "cadastros";

    private final CadastrosRepository cadastrosRepository;

    private final CadastrosSearchRepository cadastrosSearchRepository;

    public CadastrosResource(CadastrosRepository cadastrosRepository, CadastrosSearchRepository cadastrosSearchRepository) {
        this.cadastrosRepository = cadastrosRepository;
        this.cadastrosSearchRepository = cadastrosSearchRepository;
    }

    /**
     * POST  /cadastros : Create a new cadastros.
     *
     * @param cadastros the cadastros to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cadastros, or with status 400 (Bad Request) if the cadastros has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cadastros")
    @Timed
    public ResponseEntity<Cadastros> createCadastros(@Valid @RequestBody Cadastros cadastros) throws URISyntaxException {
        log.debug("REST request to save Cadastros : {}", cadastros);
        if (cadastros.getId() != null) {
            throw new BadRequestAlertException("A new cadastros cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Cadastros result = cadastrosRepository.save(cadastros);
        cadastrosSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/cadastros/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cadastros : Updates an existing cadastros.
     *
     * @param cadastros the cadastros to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cadastros,
     * or with status 400 (Bad Request) if the cadastros is not valid,
     * or with status 500 (Internal Server Error) if the cadastros couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cadastros")
    @Timed
    public ResponseEntity<Cadastros> updateCadastros(@Valid @RequestBody Cadastros cadastros) throws URISyntaxException {
        log.debug("REST request to update Cadastros : {}", cadastros);
        if (cadastros.getId() == null) {
            return createCadastros(cadastros);
        }
        Cadastros result = cadastrosRepository.save(cadastros);
        cadastrosSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cadastros.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cadastros : get all the cadastros.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cadastros in body
     */
    @GetMapping("/cadastros")
    @Timed
    public ResponseEntity<List<Cadastros>> getAllCadastros(Pageable pageable) {
        log.debug("REST request to get a page of Cadastros");
        Page<Cadastros> page = cadastrosRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cadastros");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /cadastros/:id : get the "id" cadastros.
     *
     * @param id the id of the cadastros to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cadastros, or with status 404 (Not Found)
     */
    @GetMapping("/cadastros/{id}")
    @Timed
    public ResponseEntity<Cadastros> getCadastros(@PathVariable Long id) {
        log.debug("REST request to get Cadastros : {}", id);
        Cadastros cadastros = cadastrosRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cadastros));
    }

    /**
     * DELETE  /cadastros/:id : delete the "id" cadastros.
     *
     * @param id the id of the cadastros to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cadastros/{id}")
    @Timed
    public ResponseEntity<Void> deleteCadastros(@PathVariable Long id) {
        log.debug("REST request to delete Cadastros : {}", id);
        cadastrosRepository.delete(id);
        cadastrosSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/cadastros?query=:query : search for the cadastros corresponding
     * to the query.
     *
     * @param query the query of the cadastros search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/cadastros")
    @Timed
    public ResponseEntity<List<Cadastros>> searchCadastros(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Cadastros for query {}", query);
        Page<Cadastros> page = cadastrosSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/cadastros");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
