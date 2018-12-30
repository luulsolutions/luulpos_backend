package com.luulsolutions.luulpos.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.luulsolutions.luulpos.service.EmailBalancerService;
import com.luulsolutions.luulpos.web.rest.errors.BadRequestAlertException;
import com.luulsolutions.luulpos.web.rest.util.HeaderUtil;
import com.luulsolutions.luulpos.web.rest.util.PaginationUtil;
import com.luulsolutions.luulpos.service.dto.EmailBalancerDTO;
import com.luulsolutions.luulpos.service.dto.EmailBalancerCriteria;
import com.luulsolutions.luulpos.service.EmailBalancerQueryService;
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
 * REST controller for managing EmailBalancer.
 */
@RestController
@RequestMapping("/api")
public class EmailBalancerResource {

    private final Logger log = LoggerFactory.getLogger(EmailBalancerResource.class);

    private static final String ENTITY_NAME = "emailBalancer";

    private final EmailBalancerService emailBalancerService;

    private final EmailBalancerQueryService emailBalancerQueryService;

    public EmailBalancerResource(EmailBalancerService emailBalancerService, EmailBalancerQueryService emailBalancerQueryService) {
        this.emailBalancerService = emailBalancerService;
        this.emailBalancerQueryService = emailBalancerQueryService;
    }

    /**
     * POST  /email-balancers : Create a new emailBalancer.
     *
     * @param emailBalancerDTO the emailBalancerDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new emailBalancerDTO, or with status 400 (Bad Request) if the emailBalancer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/email-balancers")
    @Timed
    public ResponseEntity<EmailBalancerDTO> createEmailBalancer(@RequestBody EmailBalancerDTO emailBalancerDTO) throws URISyntaxException {
        log.debug("REST request to save EmailBalancer : {}", emailBalancerDTO);
        if (emailBalancerDTO.getId() != null) {
            throw new BadRequestAlertException("A new emailBalancer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EmailBalancerDTO result = emailBalancerService.save(emailBalancerDTO);
        return ResponseEntity.created(new URI("/api/email-balancers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /email-balancers : Updates an existing emailBalancer.
     *
     * @param emailBalancerDTO the emailBalancerDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated emailBalancerDTO,
     * or with status 400 (Bad Request) if the emailBalancerDTO is not valid,
     * or with status 500 (Internal Server Error) if the emailBalancerDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/email-balancers")
    @Timed
    public ResponseEntity<EmailBalancerDTO> updateEmailBalancer(@RequestBody EmailBalancerDTO emailBalancerDTO) throws URISyntaxException {
        log.debug("REST request to update EmailBalancer : {}", emailBalancerDTO);
        if (emailBalancerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EmailBalancerDTO result = emailBalancerService.save(emailBalancerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, emailBalancerDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /email-balancers : get all the emailBalancers.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of emailBalancers in body
     */
    @GetMapping("/email-balancers")
    @Timed
    public ResponseEntity<List<EmailBalancerDTO>> getAllEmailBalancers(EmailBalancerCriteria criteria, Pageable pageable) {
        log.debug("REST request to get EmailBalancers by criteria: {}", criteria);
        Page<EmailBalancerDTO> page = emailBalancerQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/email-balancers");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /email-balancers/count : count all the emailBalancers.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/email-balancers/count")
    @Timed
    public ResponseEntity<Long> countEmailBalancers(EmailBalancerCriteria criteria) {
        log.debug("REST request to count EmailBalancers by criteria: {}", criteria);
        return ResponseEntity.ok().body(emailBalancerQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /email-balancers/:id : get the "id" emailBalancer.
     *
     * @param id the id of the emailBalancerDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the emailBalancerDTO, or with status 404 (Not Found)
     */
    @GetMapping("/email-balancers/{id}")
    @Timed
    public ResponseEntity<EmailBalancerDTO> getEmailBalancer(@PathVariable Long id) {
        log.debug("REST request to get EmailBalancer : {}", id);
        Optional<EmailBalancerDTO> emailBalancerDTO = emailBalancerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(emailBalancerDTO);
    }

    /**
     * DELETE  /email-balancers/:id : delete the "id" emailBalancer.
     *
     * @param id the id of the emailBalancerDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/email-balancers/{id}")
    @Timed
    public ResponseEntity<Void> deleteEmailBalancer(@PathVariable Long id) {
        log.debug("REST request to delete EmailBalancer : {}", id);
        emailBalancerService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/email-balancers?query=:query : search for the emailBalancer corresponding
     * to the query.
     *
     * @param query the query of the emailBalancer search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/email-balancers")
    @Timed
    public ResponseEntity<List<EmailBalancerDTO>> searchEmailBalancers(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of EmailBalancers for query {}", query);
        Page<EmailBalancerDTO> page = emailBalancerService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/email-balancers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
