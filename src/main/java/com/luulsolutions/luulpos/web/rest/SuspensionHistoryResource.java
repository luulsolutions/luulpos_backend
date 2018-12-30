package com.luulsolutions.luulpos.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.luulsolutions.luulpos.service.SuspensionHistoryService;
import com.luulsolutions.luulpos.web.rest.errors.BadRequestAlertException;
import com.luulsolutions.luulpos.web.rest.util.HeaderUtil;
import com.luulsolutions.luulpos.web.rest.util.PaginationUtil;
import com.luulsolutions.luulpos.service.dto.SuspensionHistoryDTO;
import com.luulsolutions.luulpos.service.dto.SuspensionHistoryCriteria;
import com.luulsolutions.luulpos.service.SuspensionHistoryQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing SuspensionHistory.
 */
@RestController
@RequestMapping("/api")
public class SuspensionHistoryResource {

    private final Logger log = LoggerFactory.getLogger(SuspensionHistoryResource.class);

    private static final String ENTITY_NAME = "suspensionHistory";

    private final SuspensionHistoryService suspensionHistoryService;

    private final SuspensionHistoryQueryService suspensionHistoryQueryService;

    public SuspensionHistoryResource(SuspensionHistoryService suspensionHistoryService, SuspensionHistoryQueryService suspensionHistoryQueryService) {
        this.suspensionHistoryService = suspensionHistoryService;
        this.suspensionHistoryQueryService = suspensionHistoryQueryService;
    }

    /**
     * POST  /suspension-histories : Create a new suspensionHistory.
     *
     * @param suspensionHistoryDTO the suspensionHistoryDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new suspensionHistoryDTO, or with status 400 (Bad Request) if the suspensionHistory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/suspension-histories")
    @Timed
    public ResponseEntity<SuspensionHistoryDTO> createSuspensionHistory(@RequestBody SuspensionHistoryDTO suspensionHistoryDTO) throws URISyntaxException {
        log.debug("REST request to save SuspensionHistory : {}", suspensionHistoryDTO);
        if (suspensionHistoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new suspensionHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SuspensionHistoryDTO result = suspensionHistoryService.save(suspensionHistoryDTO);
        return ResponseEntity.created(new URI("/api/suspension-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /suspension-histories : Updates an existing suspensionHistory.
     *
     * @param suspensionHistoryDTO the suspensionHistoryDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated suspensionHistoryDTO,
     * or with status 400 (Bad Request) if the suspensionHistoryDTO is not valid,
     * or with status 500 (Internal Server Error) if the suspensionHistoryDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/suspension-histories")
    @Timed
    public ResponseEntity<SuspensionHistoryDTO> updateSuspensionHistory(@RequestBody SuspensionHistoryDTO suspensionHistoryDTO) throws URISyntaxException {
        log.debug("REST request to update SuspensionHistory : {}", suspensionHistoryDTO);
        if (suspensionHistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SuspensionHistoryDTO result = suspensionHistoryService.save(suspensionHistoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, suspensionHistoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /suspension-histories : get all the suspensionHistories.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of suspensionHistories in body
     */
    @GetMapping("/suspension-histories")
    @Timed
    public ResponseEntity<List<SuspensionHistoryDTO>> getAllSuspensionHistories(SuspensionHistoryCriteria criteria, Pageable pageable) {
        log.debug("REST request to get SuspensionHistories by criteria: {}", criteria);
        Page<SuspensionHistoryDTO> page = suspensionHistoryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/suspension-histories");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /suspension-histories/count : count all the suspensionHistories.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/suspension-histories/count")
    @Timed
    public ResponseEntity<Long> countSuspensionHistories(SuspensionHistoryCriteria criteria) {
        log.debug("REST request to count SuspensionHistories by criteria: {}", criteria);
        return ResponseEntity.ok().body(suspensionHistoryQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /suspension-histories/:id : get the "id" suspensionHistory.
     *
     * @param id the id of the suspensionHistoryDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the suspensionHistoryDTO, or with status 404 (Not Found)
     */
    @GetMapping("/suspension-histories/{id}")
    @Timed
    public ResponseEntity<SuspensionHistoryDTO> getSuspensionHistory(@PathVariable Long id) {
        log.debug("REST request to get SuspensionHistory : {}", id);
        Optional<SuspensionHistoryDTO> suspensionHistoryDTO = suspensionHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(suspensionHistoryDTO);
    }

    /**
     * DELETE  /suspension-histories/:id : delete the "id" suspensionHistory.
     *
     * @param id the id of the suspensionHistoryDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/suspension-histories/{id}")
    @Timed
    public ResponseEntity<Void> deleteSuspensionHistory(@PathVariable Long id) {
        log.debug("REST request to delete SuspensionHistory : {}", id);
        suspensionHistoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/suspension-histories?query=:query : search for the suspensionHistory corresponding
     * to the query.
     *
     * @param query the query of the suspensionHistory search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/suspension-histories")
    @Timed
    public ResponseEntity<List<SuspensionHistoryDTO>> searchSuspensionHistories(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of SuspensionHistories for query {}", query);
        Page<SuspensionHistoryDTO> page = suspensionHistoryService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/suspension-histories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
