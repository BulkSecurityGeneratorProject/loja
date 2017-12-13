package br.com.rogrs.loja.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.rogrs.loja.domain.CadastrosLocalidades;

import br.com.rogrs.loja.repository.CadastrosLocalidadesRepository;
import br.com.rogrs.loja.repository.search.CadastrosLocalidadesSearchRepository;
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
 * REST controller for managing CadastrosLocalidades.
 */
@RestController
@RequestMapping("/api")
public class CadastrosLocalidadesResource {

    private final Logger log = LoggerFactory.getLogger(CadastrosLocalidadesResource.class);

    private static final String ENTITY_NAME = "cadastrosLocalidades";

    private final CadastrosLocalidadesRepository cadastrosLocalidadesRepository;

    private final CadastrosLocalidadesSearchRepository cadastrosLocalidadesSearchRepository;

    public CadastrosLocalidadesResource(CadastrosLocalidadesRepository cadastrosLocalidadesRepository, CadastrosLocalidadesSearchRepository cadastrosLocalidadesSearchRepository) {
        this.cadastrosLocalidadesRepository = cadastrosLocalidadesRepository;
        this.cadastrosLocalidadesSearchRepository = cadastrosLocalidadesSearchRepository;
    }

    /**
     * POST  /cadastros-localidades : Create a new cadastrosLocalidades.
     *
     * @param cadastrosLocalidades the cadastrosLocalidades to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cadastrosLocalidades, or with status 400 (Bad Request) if the cadastrosLocalidades has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cadastros-localidades")
    @Timed
    public ResponseEntity<CadastrosLocalidades> createCadastrosLocalidades(@Valid @RequestBody CadastrosLocalidades cadastrosLocalidades) throws URISyntaxException {
        log.debug("REST request to save CadastrosLocalidades : {}", cadastrosLocalidades);
        if (cadastrosLocalidades.getId() != null) {
            throw new BadRequestAlertException("A new cadastrosLocalidades cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CadastrosLocalidades result = cadastrosLocalidadesRepository.save(cadastrosLocalidades);
        cadastrosLocalidadesSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/cadastros-localidades/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cadastros-localidades : Updates an existing cadastrosLocalidades.
     *
     * @param cadastrosLocalidades the cadastrosLocalidades to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cadastrosLocalidades,
     * or with status 400 (Bad Request) if the cadastrosLocalidades is not valid,
     * or with status 500 (Internal Server Error) if the cadastrosLocalidades couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cadastros-localidades")
    @Timed
    public ResponseEntity<CadastrosLocalidades> updateCadastrosLocalidades(@Valid @RequestBody CadastrosLocalidades cadastrosLocalidades) throws URISyntaxException {
        log.debug("REST request to update CadastrosLocalidades : {}", cadastrosLocalidades);
        if (cadastrosLocalidades.getId() == null) {
            return createCadastrosLocalidades(cadastrosLocalidades);
        }
        CadastrosLocalidades result = cadastrosLocalidadesRepository.save(cadastrosLocalidades);
        cadastrosLocalidadesSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cadastrosLocalidades.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cadastros-localidades : get all the cadastrosLocalidades.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cadastrosLocalidades in body
     */
    @GetMapping("/cadastros-localidades")
    @Timed
    public ResponseEntity<List<CadastrosLocalidades>> getAllCadastrosLocalidades(Pageable pageable) {
        log.debug("REST request to get a page of CadastrosLocalidades");
        Page<CadastrosLocalidades> page = cadastrosLocalidadesRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cadastros-localidades");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /cadastros-localidades/:id : get the "id" cadastrosLocalidades.
     *
     * @param id the id of the cadastrosLocalidades to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cadastrosLocalidades, or with status 404 (Not Found)
     */
    @GetMapping("/cadastros-localidades/{id}")
    @Timed
    public ResponseEntity<CadastrosLocalidades> getCadastrosLocalidades(@PathVariable Long id) {
        log.debug("REST request to get CadastrosLocalidades : {}", id);
        CadastrosLocalidades cadastrosLocalidades = cadastrosLocalidadesRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cadastrosLocalidades));
    }

    /**
     * DELETE  /cadastros-localidades/:id : delete the "id" cadastrosLocalidades.
     *
     * @param id the id of the cadastrosLocalidades to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cadastros-localidades/{id}")
    @Timed
    public ResponseEntity<Void> deleteCadastrosLocalidades(@PathVariable Long id) {
        log.debug("REST request to delete CadastrosLocalidades : {}", id);
        cadastrosLocalidadesRepository.delete(id);
        cadastrosLocalidadesSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/cadastros-localidades?query=:query : search for the cadastrosLocalidades corresponding
     * to the query.
     *
     * @param query the query of the cadastrosLocalidades search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/cadastros-localidades")
    @Timed
    public ResponseEntity<List<CadastrosLocalidades>> searchCadastrosLocalidades(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CadastrosLocalidades for query {}", query);
        Page<CadastrosLocalidades> page = cadastrosLocalidadesSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/cadastros-localidades");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
