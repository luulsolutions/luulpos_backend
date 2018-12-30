package com.luulsolutions.luulpos.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.luulsolutions.luulpos.service.SystemEventsHistoryService;
import com.luulsolutions.luulpos.web.rest.errors.BadRequestAlertException;
import com.luulsolutions.luulpos.web.rest.util.HeaderUtil;
import com.luulsolutions.luulpos.web.rest.util.PaginationUtil;
import com.luulsolutions.luulpos.service.dto.SystemEventsHistoryDTO;
import com.luulsolutions.luulpos.service.dto.SystemEventsHistoryCriteria;
import com.luulsolutions.luulpos.service.SystemEventsHistoryQueryService;
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
 * REST controller for managing SystemEventsHistory.
 */
@RestController
@RequestMapping("/api")
public class SystemEventsHistoryResource {

    private final Logger log = LoggerFactory.getLogger(SystemEventsHistoryResource.class);

    private static final String ENTITY_NAME = "systemEventsHistory";

    private final SystemEventsHistoryService systemEventsHistoryService;

    private final SystemEventsHistoryQueryService systemEventsHistoryQueryService;

    public SystemEventsHistoryResource(SystemEventsHistoryService systemEventsHistoryService, SystemEventsHistoryQueryService systemEventsHistoryQueryService) {
        this.systemEventsHistoryService = systemEventsHistoryService;
        this.systemEventsHistoryQueryService = systemEventsHistoryQueryService;
    }

    /**
     * POST  /system-events-histories : Create a new systemEventsHistory.
     *
     * @param systemEventsHistoryDTO the systemEventsHistoryDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new systemEventsHistoryDTO, or with status 400 (Bad Request) if the systemEventsHistory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/system-events-histories")
    @Timed
    public ResponseEntity<SystemEventsHistoryDTO> createSystemEventsHistory(@Valid @RequestBody SystemEventsHistoryDTO systemEventsHistoryDTO) throws URISyntaxException {
        log.debug("REST request to save SystemEventsHistory : {}", systemEventsHistoryDTO);
        if (systemEventsHistoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new systemEventsHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SystemEventsHistoryDTO result = systemEventsHistoryService.save(systemEventsHistoryDTO);
        return ResponseEntity.created(new URI("/api/system-events-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /system-events-histories : Updates an existing systemEventsHistory.
     *
     * @param systemEventsHistoryDTO the systemEventsHistoryDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated systemEventsHistoryDTO,
     * or with status 400 (Bad Request) if the systemEventsHistoryDTO is not valid,
     * or with status 500 (Internal Server Error) if the systemEventsHistoryDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/system-events-histories")
    @Timed
    public ResponseEntity<SystemEventsHistoryDTO> updateSystemEventsHistory(@Valid @RequestBody SystemEventsHistoryDTO systemEventsHistoryDTO) throws URISyntaxException {
        log.debug("REST request to update SystemEventsHistory : {}", systemEventsHistoryDTO);
        if (systemEventsHistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SystemEventsHistoryDTO result = systemEventsHistoryService.save(systemEventsHistoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, systemEventsHistoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /system-events-histories : get all the systemEventsHistories.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of systemEventsHistories in body
     */
    @GetMapping("/system-events-histories")
    @Timed
    public ResponseEntity<List<SystemEventsHistoryDTO>> getAllSystemEventsHistories(SystemEventsHistoryCriteria criteria, Pageable pageable) {
        log.debug("REST request to get SystemEventsHistories by criteria: {}", criteria);
        Page<SystemEventsHistoryDTO> page = systemEventsHistoryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/system-events-histories");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /system-events-histories/count : count all the systemEventsHistories.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/system-events-histories/count")
    @Timed
    public ResponseEntity<Long> countSystemEventsHistories(SystemEventsHistoryCriteria criteria) {
        log.debug("REST request to count SystemEventsHistories by criteria: {}", criteria);
        return ResponseEntity.ok().body(systemEventsHistoryQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /system-events-histories/:id : get the "id" systemEventsHistory.
     *
     * @param id the id of the systemEventsHistoryDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the systemEventsHistoryDTO, or with status 404 (Not Found)
     */
    @GetMapping("/system-events-histories/{id}")
    @Timed
    public ResponseEntity<SystemEventsHistoryDTO> getSystemEventsHistory(@PathVariable Long id) {
        log.debug("REST request to get SystemEventsHistory : {}", id);
        Optional<SystemEventsHistoryDTO> systemEventsHistoryDTO = systemEventsHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(systemEventsHistoryDTO);
    }

    /**
     * DELETE  /system-events-histories/:id : delete the "id" systemEventsHistory.
     *
     * @param id the id of the systemEventsHistoryDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/system-events-histories/{id}")
    @Timed
    public ResponseEntity<Void> deleteSystemEventsHistory(@PathVariable Long id) {
        log.debug("REST request to delete SystemEventsHistory : {}", id);
        systemEventsHistoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/system-events-histories?query=:query : search for the systemEventsHistory corresponding
     * to the query.
     *
     * @param query the query of the systemEventsHistory search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/system-events-histories")
    @Timed
    public ResponseEntity<List<SystemEventsHistoryDTO>> searchSystemEventsHistories(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of SystemEventsHistories for query {}", query);
        Page<SystemEventsHistoryDTO> page = systemEventsHistoryService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/system-events-histories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
