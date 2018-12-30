package com.luulsolutions.luulpos.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.luulsolutions.luulpos.service.EmployeeTimesheetService;
import com.luulsolutions.luulpos.web.rest.errors.BadRequestAlertException;
import com.luulsolutions.luulpos.web.rest.util.HeaderUtil;
import com.luulsolutions.luulpos.web.rest.util.PaginationUtil;
import com.luulsolutions.luulpos.service.dto.EmployeeTimesheetDTO;
import com.luulsolutions.luulpos.service.dto.EmployeeTimesheetCriteria;
import com.luulsolutions.luulpos.service.EmployeeTimesheetQueryService;
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
 * REST controller for managing EmployeeTimesheet.
 */
@RestController
@RequestMapping("/api")
public class EmployeeTimesheetResource {

    private final Logger log = LoggerFactory.getLogger(EmployeeTimesheetResource.class);

    private static final String ENTITY_NAME = "employeeTimesheet";

    private final EmployeeTimesheetService employeeTimesheetService;

    private final EmployeeTimesheetQueryService employeeTimesheetQueryService;

    public EmployeeTimesheetResource(EmployeeTimesheetService employeeTimesheetService, EmployeeTimesheetQueryService employeeTimesheetQueryService) {
        this.employeeTimesheetService = employeeTimesheetService;
        this.employeeTimesheetQueryService = employeeTimesheetQueryService;
    }

    /**
     * POST  /employee-timesheets : Create a new employeeTimesheet.
     *
     * @param employeeTimesheetDTO the employeeTimesheetDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new employeeTimesheetDTO, or with status 400 (Bad Request) if the employeeTimesheet has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/employee-timesheets")
    @Timed
    public ResponseEntity<EmployeeTimesheetDTO> createEmployeeTimesheet(@RequestBody EmployeeTimesheetDTO employeeTimesheetDTO) throws URISyntaxException {
        log.debug("REST request to save EmployeeTimesheet : {}", employeeTimesheetDTO);
        if (employeeTimesheetDTO.getId() != null) {
            throw new BadRequestAlertException("A new employeeTimesheet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EmployeeTimesheetDTO result = employeeTimesheetService.save(employeeTimesheetDTO);
        return ResponseEntity.created(new URI("/api/employee-timesheets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /employee-timesheets : Updates an existing employeeTimesheet.
     *
     * @param employeeTimesheetDTO the employeeTimesheetDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated employeeTimesheetDTO,
     * or with status 400 (Bad Request) if the employeeTimesheetDTO is not valid,
     * or with status 500 (Internal Server Error) if the employeeTimesheetDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/employee-timesheets")
    @Timed
    public ResponseEntity<EmployeeTimesheetDTO> updateEmployeeTimesheet(@RequestBody EmployeeTimesheetDTO employeeTimesheetDTO) throws URISyntaxException {
        log.debug("REST request to update EmployeeTimesheet : {}", employeeTimesheetDTO);
        if (employeeTimesheetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EmployeeTimesheetDTO result = employeeTimesheetService.save(employeeTimesheetDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, employeeTimesheetDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /employee-timesheets : get all the employeeTimesheets.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of employeeTimesheets in body
     */
    @GetMapping("/employee-timesheets")
    @Timed
    public ResponseEntity<List<EmployeeTimesheetDTO>> getAllEmployeeTimesheets(EmployeeTimesheetCriteria criteria, Pageable pageable) {
        log.debug("REST request to get EmployeeTimesheets by criteria: {}", criteria);
        Page<EmployeeTimesheetDTO> page = employeeTimesheetQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/employee-timesheets");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /employee-timesheets/count : count all the employeeTimesheets.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/employee-timesheets/count")
    @Timed
    public ResponseEntity<Long> countEmployeeTimesheets(EmployeeTimesheetCriteria criteria) {
        log.debug("REST request to count EmployeeTimesheets by criteria: {}", criteria);
        return ResponseEntity.ok().body(employeeTimesheetQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /employee-timesheets/:id : get the "id" employeeTimesheet.
     *
     * @param id the id of the employeeTimesheetDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the employeeTimesheetDTO, or with status 404 (Not Found)
     */
    @GetMapping("/employee-timesheets/{id}")
    @Timed
    public ResponseEntity<EmployeeTimesheetDTO> getEmployeeTimesheet(@PathVariable Long id) {
        log.debug("REST request to get EmployeeTimesheet : {}", id);
        Optional<EmployeeTimesheetDTO> employeeTimesheetDTO = employeeTimesheetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(employeeTimesheetDTO);
    }

    /**
     * DELETE  /employee-timesheets/:id : delete the "id" employeeTimesheet.
     *
     * @param id the id of the employeeTimesheetDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/employee-timesheets/{id}")
    @Timed
    public ResponseEntity<Void> deleteEmployeeTimesheet(@PathVariable Long id) {
        log.debug("REST request to delete EmployeeTimesheet : {}", id);
        employeeTimesheetService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/employee-timesheets?query=:query : search for the employeeTimesheet corresponding
     * to the query.
     *
     * @param query the query of the employeeTimesheet search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/employee-timesheets")
    @Timed
    public ResponseEntity<List<EmployeeTimesheetDTO>> searchEmployeeTimesheets(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of EmployeeTimesheets for query {}", query);
        Page<EmployeeTimesheetDTO> page = employeeTimesheetService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/employee-timesheets");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
