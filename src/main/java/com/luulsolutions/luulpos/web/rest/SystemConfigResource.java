package com.luulsolutions.luulpos.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.luulsolutions.luulpos.service.SystemConfigService;
import com.luulsolutions.luulpos.web.rest.errors.BadRequestAlertException;
import com.luulsolutions.luulpos.web.rest.util.HeaderUtil;
import com.luulsolutions.luulpos.web.rest.util.PaginationUtil;
import com.luulsolutions.luulpos.service.dto.SystemConfigDTO;
import com.luulsolutions.luulpos.service.dto.SystemConfigCriteria;
import com.luulsolutions.luulpos.service.SystemConfigQueryService;
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
 * REST controller for managing SystemConfig.
 */
@RestController
@RequestMapping("/api")
public class SystemConfigResource {

    private final Logger log = LoggerFactory.getLogger(SystemConfigResource.class);

    private static final String ENTITY_NAME = "systemConfig";

    private final SystemConfigService systemConfigService;

    private final SystemConfigQueryService systemConfigQueryService;

    public SystemConfigResource(SystemConfigService systemConfigService, SystemConfigQueryService systemConfigQueryService) {
        this.systemConfigService = systemConfigService;
        this.systemConfigQueryService = systemConfigQueryService;
    }

    /**
     * POST  /system-configs : Create a new systemConfig.
     *
     * @param systemConfigDTO the systemConfigDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new systemConfigDTO, or with status 400 (Bad Request) if the systemConfig has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/system-configs")
    @Timed
    public ResponseEntity<SystemConfigDTO> createSystemConfig(@RequestBody SystemConfigDTO systemConfigDTO) throws URISyntaxException {
        log.debug("REST request to save SystemConfig : {}", systemConfigDTO);
        if (systemConfigDTO.getId() != null) {
            throw new BadRequestAlertException("A new systemConfig cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SystemConfigDTO result = systemConfigService.save(systemConfigDTO);
        return ResponseEntity.created(new URI("/api/system-configs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /system-configs : Updates an existing systemConfig.
     *
     * @param systemConfigDTO the systemConfigDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated systemConfigDTO,
     * or with status 400 (Bad Request) if the systemConfigDTO is not valid,
     * or with status 500 (Internal Server Error) if the systemConfigDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/system-configs")
    @Timed
    public ResponseEntity<SystemConfigDTO> updateSystemConfig(@RequestBody SystemConfigDTO systemConfigDTO) throws URISyntaxException {
        log.debug("REST request to update SystemConfig : {}", systemConfigDTO);
        if (systemConfigDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SystemConfigDTO result = systemConfigService.save(systemConfigDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, systemConfigDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /system-configs : get all the systemConfigs.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of systemConfigs in body
     */
    @GetMapping("/system-configs")
    @Timed
    public ResponseEntity<List<SystemConfigDTO>> getAllSystemConfigs(SystemConfigCriteria criteria, Pageable pageable) {
        log.debug("REST request to get SystemConfigs by criteria: {}", criteria);
        Page<SystemConfigDTO> page = systemConfigQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/system-configs");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /system-configs/count : count all the systemConfigs.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/system-configs/count")
    @Timed
    public ResponseEntity<Long> countSystemConfigs(SystemConfigCriteria criteria) {
        log.debug("REST request to count SystemConfigs by criteria: {}", criteria);
        return ResponseEntity.ok().body(systemConfigQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /system-configs/:id : get the "id" systemConfig.
     *
     * @param id the id of the systemConfigDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the systemConfigDTO, or with status 404 (Not Found)
     */
    @GetMapping("/system-configs/{id}")
    @Timed
    public ResponseEntity<SystemConfigDTO> getSystemConfig(@PathVariable Long id) {
        log.debug("REST request to get SystemConfig : {}", id);
        Optional<SystemConfigDTO> systemConfigDTO = systemConfigService.findOne(id);
        return ResponseUtil.wrapOrNotFound(systemConfigDTO);
    }

    /**
     * DELETE  /system-configs/:id : delete the "id" systemConfig.
     *
     * @param id the id of the systemConfigDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/system-configs/{id}")
    @Timed
    public ResponseEntity<Void> deleteSystemConfig(@PathVariable Long id) {
        log.debug("REST request to delete SystemConfig : {}", id);
        systemConfigService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/system-configs?query=:query : search for the systemConfig corresponding
     * to the query.
     *
     * @param query the query of the systemConfig search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/system-configs")
    @Timed
    public ResponseEntity<List<SystemConfigDTO>> searchSystemConfigs(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of SystemConfigs for query {}", query);
        Page<SystemConfigDTO> page = systemConfigService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/system-configs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
