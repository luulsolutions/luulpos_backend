package com.luulsolutions.luulpos.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.luulsolutions.luulpos.service.ShopChangeService;
import com.luulsolutions.luulpos.web.rest.errors.BadRequestAlertException;
import com.luulsolutions.luulpos.web.rest.util.HeaderUtil;
import com.luulsolutions.luulpos.web.rest.util.PaginationUtil;
import com.luulsolutions.luulpos.service.dto.ShopChangeDTO;
import com.luulsolutions.luulpos.service.dto.ShopChangeCriteria;
import com.luulsolutions.luulpos.service.ShopChangeQueryService;
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
 * REST controller for managing ShopChange.
 */
@RestController
@RequestMapping("/api")
public class ShopChangeResource {

    private final Logger log = LoggerFactory.getLogger(ShopChangeResource.class);

    private static final String ENTITY_NAME = "shopChange";

    private final ShopChangeService shopChangeService;

    private final ShopChangeQueryService shopChangeQueryService;

    public ShopChangeResource(ShopChangeService shopChangeService, ShopChangeQueryService shopChangeQueryService) {
        this.shopChangeService = shopChangeService;
        this.shopChangeQueryService = shopChangeQueryService;
    }

    /**
     * POST  /shop-changes : Create a new shopChange.
     *
     * @param shopChangeDTO the shopChangeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new shopChangeDTO, or with status 400 (Bad Request) if the shopChange has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/shop-changes")
    @Timed
    public ResponseEntity<ShopChangeDTO> createShopChange(@RequestBody ShopChangeDTO shopChangeDTO) throws URISyntaxException {
        log.debug("REST request to save ShopChange : {}", shopChangeDTO);
        if (shopChangeDTO.getId() != null) {
            throw new BadRequestAlertException("A new shopChange cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ShopChangeDTO result = shopChangeService.save(shopChangeDTO);
        return ResponseEntity.created(new URI("/api/shop-changes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /shop-changes : Updates an existing shopChange.
     *
     * @param shopChangeDTO the shopChangeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated shopChangeDTO,
     * or with status 400 (Bad Request) if the shopChangeDTO is not valid,
     * or with status 500 (Internal Server Error) if the shopChangeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/shop-changes")
    @Timed
    public ResponseEntity<ShopChangeDTO> updateShopChange(@RequestBody ShopChangeDTO shopChangeDTO) throws URISyntaxException {
        log.debug("REST request to update ShopChange : {}", shopChangeDTO);
        if (shopChangeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ShopChangeDTO result = shopChangeService.save(shopChangeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, shopChangeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /shop-changes : get all the shopChanges.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of shopChanges in body
     */
    @GetMapping("/shop-changes")
    @Timed
    public ResponseEntity<List<ShopChangeDTO>> getAllShopChanges(ShopChangeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ShopChanges by criteria: {}", criteria);
        Page<ShopChangeDTO> page = shopChangeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/shop-changes");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /shop-changes/count : count all the shopChanges.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/shop-changes/count")
    @Timed
    public ResponseEntity<Long> countShopChanges(ShopChangeCriteria criteria) {
        log.debug("REST request to count ShopChanges by criteria: {}", criteria);
        return ResponseEntity.ok().body(shopChangeQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /shop-changes/:id : get the "id" shopChange.
     *
     * @param id the id of the shopChangeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the shopChangeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/shop-changes/{id}")
    @Timed
    public ResponseEntity<ShopChangeDTO> getShopChange(@PathVariable Long id) {
        log.debug("REST request to get ShopChange : {}", id);
        Optional<ShopChangeDTO> shopChangeDTO = shopChangeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shopChangeDTO);
    }

    /**
     * DELETE  /shop-changes/:id : delete the "id" shopChange.
     *
     * @param id the id of the shopChangeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/shop-changes/{id}")
    @Timed
    public ResponseEntity<Void> deleteShopChange(@PathVariable Long id) {
        log.debug("REST request to delete ShopChange : {}", id);
        shopChangeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/shop-changes?query=:query : search for the shopChange corresponding
     * to the query.
     *
     * @param query the query of the shopChange search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/shop-changes")
    @Timed
    public ResponseEntity<List<ShopChangeDTO>> searchShopChanges(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ShopChanges for query {}", query);
        Page<ShopChangeDTO> page = shopChangeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/shop-changes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    @GetMapping("/shop-changes-by-shop-id/{shopId}")
    @Timed
    public ResponseEntity<Long> getLastShopChangeIdByShopId(@PathVariable Long shopId) {
        log.debug("REST request to get a page of getLastShopChangeIdByShopId");
        ShopChangeDTO shopChangeDTO = shopChangeService.findFirstByShopId(shopId);
        Long lastRecord = 0L;
        if (shopChangeDTO != null ) {
        	lastRecord = shopChangeDTO.getId();
        }
        return new ResponseEntity<>(lastRecord, HttpStatus.OK);
    }

}
