package com.luulsolutions.luulpos.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.luulsolutions.luulpos.service.ShopSectionService;
import com.luulsolutions.luulpos.web.rest.errors.BadRequestAlertException;
import com.luulsolutions.luulpos.web.rest.util.HeaderUtil;
import com.luulsolutions.luulpos.web.rest.util.PaginationUtil;
import com.luulsolutions.luulpos.service.dto.ShopSectionDTO;
import com.luulsolutions.luulpos.service.dto.ShopSectionCriteria;
import com.luulsolutions.luulpos.service.ShopSectionQueryService;
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
 * REST controller for managing ShopSection.
 */
@RestController
@RequestMapping("/api")
public class ShopSectionResource {

    private final Logger log = LoggerFactory.getLogger(ShopSectionResource.class);

    private static final String ENTITY_NAME = "shopSection";

    private final ShopSectionService shopSectionService;

    private final ShopSectionQueryService shopSectionQueryService;

    public ShopSectionResource(ShopSectionService shopSectionService, ShopSectionQueryService shopSectionQueryService) {
        this.shopSectionService = shopSectionService;
        this.shopSectionQueryService = shopSectionQueryService;
    }

    /**
     * POST  /shop-sections : Create a new shopSection.
     *
     * @param shopSectionDTO the shopSectionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new shopSectionDTO, or with status 400 (Bad Request) if the shopSection has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/shop-sections")
    @Timed
    public ResponseEntity<ShopSectionDTO> createShopSection(@RequestBody ShopSectionDTO shopSectionDTO) throws URISyntaxException {
        log.debug("REST request to save ShopSection : {}", shopSectionDTO);
        if (shopSectionDTO.getId() != null) {
            throw new BadRequestAlertException("A new shopSection cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ShopSectionDTO result = shopSectionService.save(shopSectionDTO);
        return ResponseEntity.created(new URI("/api/shop-sections/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /shop-sections : Updates an existing shopSection.
     *
     * @param shopSectionDTO the shopSectionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated shopSectionDTO,
     * or with status 400 (Bad Request) if the shopSectionDTO is not valid,
     * or with status 500 (Internal Server Error) if the shopSectionDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/shop-sections")
    @Timed
    public ResponseEntity<ShopSectionDTO> updateShopSection(@RequestBody ShopSectionDTO shopSectionDTO) throws URISyntaxException {
        log.debug("REST request to update ShopSection : {}", shopSectionDTO);
        if (shopSectionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ShopSectionDTO result = shopSectionService.save(shopSectionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, shopSectionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /shop-sections : get all the shopSections.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of shopSections in body
     */
    @GetMapping("/shop-sections")
    @Timed
    public ResponseEntity<List<ShopSectionDTO>> getAllShopSections(ShopSectionCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ShopSections by criteria: {}", criteria);
        Page<ShopSectionDTO> page = shopSectionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/shop-sections");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /shop-sections/count : count all the shopSections.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/shop-sections/count")
    @Timed
    public ResponseEntity<Long> countShopSections(ShopSectionCriteria criteria) {
        log.debug("REST request to count ShopSections by criteria: {}", criteria);
        return ResponseEntity.ok().body(shopSectionQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /shop-sections/:id : get the "id" shopSection.
     *
     * @param id the id of the shopSectionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the shopSectionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/shop-sections/{id}")
    @Timed
    public ResponseEntity<ShopSectionDTO> getShopSection(@PathVariable Long id) {
        log.debug("REST request to get ShopSection : {}", id);
        Optional<ShopSectionDTO> shopSectionDTO = shopSectionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shopSectionDTO);
    }

    /**
     * DELETE  /shop-sections/:id : delete the "id" shopSection.
     *
     * @param id the id of the shopSectionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/shop-sections/{id}")
    @Timed
    public ResponseEntity<Void> deleteShopSection(@PathVariable Long id) {
        log.debug("REST request to delete ShopSection : {}", id);
        shopSectionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/shop-sections?query=:query : search for the shopSection corresponding
     * to the query.
     *
     * @param query the query of the shopSection search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/shop-sections")
    @Timed
    public ResponseEntity<List<ShopSectionDTO>> searchShopSections(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ShopSections for query {}", query);
        Page<ShopSectionDTO> page = shopSectionService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/shop-sections");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
