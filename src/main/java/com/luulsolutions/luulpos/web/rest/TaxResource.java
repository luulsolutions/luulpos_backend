package com.luulsolutions.luulpos.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.luulsolutions.luulpos.service.TaxService;
import com.luulsolutions.luulpos.web.rest.errors.BadRequestAlertException;
import com.luulsolutions.luulpos.web.rest.util.HeaderUtil;
import com.luulsolutions.luulpos.web.rest.util.PaginationUtil;
import com.luulsolutions.luulpos.service.dto.TaxDTO;
import com.luulsolutions.luulpos.service.dto.TaxCriteria;
import com.luulsolutions.luulpos.service.TaxQueryService;
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
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Tax.
 */
@RestController
@RequestMapping("/api")
public class TaxResource {

    private final Logger log = LoggerFactory.getLogger(TaxResource.class);

    private static final String ENTITY_NAME = "tax";

    private final TaxService taxService;

    private final TaxQueryService taxQueryService;

    public TaxResource(TaxService taxService, TaxQueryService taxQueryService) {
        this.taxService = taxService;
        this.taxQueryService = taxQueryService;
    }

    /**
     * POST  /taxes : Create a new tax.
     *
     * @param taxDTO the taxDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new taxDTO, or with status 400 (Bad Request) if the tax has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/taxes")
    @Timed
    public ResponseEntity<TaxDTO> createTax(@Valid @RequestBody TaxDTO taxDTO) throws URISyntaxException {
        log.debug("REST request to save Tax : {}", taxDTO);
        if (taxDTO.getId() != null) {
            throw new BadRequestAlertException("A new tax cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TaxDTO result = taxService.save(taxDTO);
        return ResponseEntity.created(new URI("/api/taxes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /taxes : Updates an existing tax.
     *
     * @param taxDTO the taxDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated taxDTO,
     * or with status 400 (Bad Request) if the taxDTO is not valid,
     * or with status 500 (Internal Server Error) if the taxDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/taxes")
    @Timed
    public ResponseEntity<TaxDTO> updateTax(@Valid @RequestBody TaxDTO taxDTO) throws URISyntaxException {
        log.debug("REST request to update Tax : {}", taxDTO);
        if (taxDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TaxDTO result = taxService.save(taxDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, taxDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /taxes : get all the taxes.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of taxes in body
     */
    @GetMapping("/taxes")
    @Timed
    public ResponseEntity<List<TaxDTO>> getAllTaxes(TaxCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Taxes by criteria: {}", criteria);
        Page<TaxDTO> page = taxQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/taxes");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /taxes/count : count all the taxes.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/taxes/count")
    @Timed
    public ResponseEntity<Long> countTaxes(TaxCriteria criteria) {
        log.debug("REST request to count Taxes by criteria: {}", criteria);
        return ResponseEntity.ok().body(taxQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /taxes/:id : get the "id" tax.
     *
     * @param id the id of the taxDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the taxDTO, or with status 404 (Not Found)
     */
    @GetMapping("/taxes/{id}")
    @Timed
    public ResponseEntity<TaxDTO> getTax(@PathVariable Long id) {
        log.debug("REST request to get Tax : {}", id);
        Optional<TaxDTO> taxDTO = taxService.findOne(id);
        return ResponseUtil.wrapOrNotFound(taxDTO);
    }

    /**
     * DELETE  /taxes/:id : delete the "id" tax.
     *
     * @param id the id of the taxDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/taxes/{id}")
    @Timed
    public ResponseEntity<Void> deleteTax(@PathVariable Long id) {
        log.debug("REST request to delete Tax : {}", id);
        taxService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/taxes?query=:query : search for the tax corresponding
     * to the query.
     *
     * @param query the query of the tax search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/taxes")
    @Timed
    public ResponseEntity<List<TaxDTO>> searchTaxes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Taxes for query {}", query);
        Page<TaxDTO> page = taxService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/taxes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
