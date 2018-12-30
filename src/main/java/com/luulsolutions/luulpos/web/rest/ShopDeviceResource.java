package com.luulsolutions.luulpos.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.luulsolutions.luulpos.service.ShopDeviceService;
import com.luulsolutions.luulpos.web.rest.errors.BadRequestAlertException;
import com.luulsolutions.luulpos.web.rest.util.HeaderUtil;
import com.luulsolutions.luulpos.web.rest.util.PaginationUtil;
import com.luulsolutions.luulpos.service.dto.ShopDeviceDTO;
import com.luulsolutions.luulpos.service.dto.ShopDeviceCriteria;
import com.luulsolutions.luulpos.service.ShopDeviceQueryService;
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
 * REST controller for managing ShopDevice.
 */
@RestController
@RequestMapping("/api")
public class ShopDeviceResource {

    private final Logger log = LoggerFactory.getLogger(ShopDeviceResource.class);

    private static final String ENTITY_NAME = "shopDevice";

    private final ShopDeviceService shopDeviceService;

    private final ShopDeviceQueryService shopDeviceQueryService;

    public ShopDeviceResource(ShopDeviceService shopDeviceService, ShopDeviceQueryService shopDeviceQueryService) {
        this.shopDeviceService = shopDeviceService;
        this.shopDeviceQueryService = shopDeviceQueryService;
    }

    /**
     * POST  /shop-devices : Create a new shopDevice.
     *
     * @param shopDeviceDTO the shopDeviceDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new shopDeviceDTO, or with status 400 (Bad Request) if the shopDevice has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/shop-devices")
    @Timed
    public ResponseEntity<ShopDeviceDTO> createShopDevice(@RequestBody ShopDeviceDTO shopDeviceDTO) throws URISyntaxException {
        log.debug("REST request to save ShopDevice : {}", shopDeviceDTO);
        if (shopDeviceDTO.getId() != null) {
            throw new BadRequestAlertException("A new shopDevice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ShopDeviceDTO result = shopDeviceService.save(shopDeviceDTO);
        return ResponseEntity.created(new URI("/api/shop-devices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /shop-devices : Updates an existing shopDevice.
     *
     * @param shopDeviceDTO the shopDeviceDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated shopDeviceDTO,
     * or with status 400 (Bad Request) if the shopDeviceDTO is not valid,
     * or with status 500 (Internal Server Error) if the shopDeviceDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/shop-devices")
    @Timed
    public ResponseEntity<ShopDeviceDTO> updateShopDevice(@RequestBody ShopDeviceDTO shopDeviceDTO) throws URISyntaxException {
        log.debug("REST request to update ShopDevice : {}", shopDeviceDTO);
        if (shopDeviceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ShopDeviceDTO result = shopDeviceService.save(shopDeviceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, shopDeviceDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /shop-devices : get all the shopDevices.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of shopDevices in body
     */
    @GetMapping("/shop-devices")
    @Timed
    public ResponseEntity<List<ShopDeviceDTO>> getAllShopDevices(ShopDeviceCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ShopDevices by criteria: {}", criteria);
        Page<ShopDeviceDTO> page = shopDeviceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/shop-devices");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /shop-devices/count : count all the shopDevices.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/shop-devices/count")
    @Timed
    public ResponseEntity<Long> countShopDevices(ShopDeviceCriteria criteria) {
        log.debug("REST request to count ShopDevices by criteria: {}", criteria);
        return ResponseEntity.ok().body(shopDeviceQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /shop-devices/:id : get the "id" shopDevice.
     *
     * @param id the id of the shopDeviceDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the shopDeviceDTO, or with status 404 (Not Found)
     */
    @GetMapping("/shop-devices/{id}")
    @Timed
    public ResponseEntity<ShopDeviceDTO> getShopDevice(@PathVariable Long id) {
        log.debug("REST request to get ShopDevice : {}", id);
        Optional<ShopDeviceDTO> shopDeviceDTO = shopDeviceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shopDeviceDTO);
    }

    /**
     * DELETE  /shop-devices/:id : delete the "id" shopDevice.
     *
     * @param id the id of the shopDeviceDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/shop-devices/{id}")
    @Timed
    public ResponseEntity<Void> deleteShopDevice(@PathVariable Long id) {
        log.debug("REST request to delete ShopDevice : {}", id);
        shopDeviceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/shop-devices?query=:query : search for the shopDevice corresponding
     * to the query.
     *
     * @param query the query of the shopDevice search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/shop-devices")
    @Timed
    public ResponseEntity<List<ShopDeviceDTO>> searchShopDevices(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ShopDevices for query {}", query);
        Page<ShopDeviceDTO> page = shopDeviceService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/shop-devices");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
