package com.luulsolutions.luulpos.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.luulsolutions.luulpos.service.OrdersLineService;
import com.luulsolutions.luulpos.web.rest.errors.BadRequestAlertException;
import com.luulsolutions.luulpos.web.rest.util.HeaderUtil;
import com.luulsolutions.luulpos.web.rest.util.PaginationUtil;
import com.luulsolutions.luulpos.service.dto.OrdersLineDTO;
import com.luulsolutions.luulpos.service.dto.OrdersLineCriteria;
import com.luulsolutions.luulpos.service.OrdersLineQueryService;
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
 * REST controller for managing OrdersLine.
 */
@RestController
@RequestMapping("/api")
public class OrdersLineResource {

    private final Logger log = LoggerFactory.getLogger(OrdersLineResource.class);

    private static final String ENTITY_NAME = "ordersLine";

    private final OrdersLineService ordersLineService;

    private final OrdersLineQueryService ordersLineQueryService;

    public OrdersLineResource(OrdersLineService ordersLineService, OrdersLineQueryService ordersLineQueryService) {
        this.ordersLineService = ordersLineService;
        this.ordersLineQueryService = ordersLineQueryService;
    }

    /**
     * POST  /orders-lines : Create a new ordersLine.
     *
     * @param ordersLineDTO the ordersLineDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ordersLineDTO, or with status 400 (Bad Request) if the ordersLine has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/orders-lines")
    @Timed
    public ResponseEntity<OrdersLineDTO> createOrdersLine(@Valid @RequestBody OrdersLineDTO ordersLineDTO) throws URISyntaxException {
        log.debug("REST request to save OrdersLine : {}", ordersLineDTO);
        if (ordersLineDTO.getId() != null) {
            throw new BadRequestAlertException("A new ordersLine cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrdersLineDTO result = ordersLineService.save(ordersLineDTO);
        return ResponseEntity.created(new URI("/api/orders-lines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /orders-lines : Updates an existing ordersLine.
     *
     * @param ordersLineDTO the ordersLineDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ordersLineDTO,
     * or with status 400 (Bad Request) if the ordersLineDTO is not valid,
     * or with status 500 (Internal Server Error) if the ordersLineDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/orders-lines")
    @Timed
    public ResponseEntity<OrdersLineDTO> updateOrdersLine(@Valid @RequestBody OrdersLineDTO ordersLineDTO) throws URISyntaxException {
        log.debug("REST request to update OrdersLine : {}", ordersLineDTO);
        if (ordersLineDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OrdersLineDTO result = ordersLineService.save(ordersLineDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, ordersLineDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /orders-lines : get all the ordersLines.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of ordersLines in body
     */
    @GetMapping("/orders-lines")
    @Timed
    public ResponseEntity<List<OrdersLineDTO>> getAllOrdersLines(OrdersLineCriteria criteria, Pageable pageable) {
        log.debug("REST request to get OrdersLines by criteria: {}", criteria);
        Page<OrdersLineDTO> page = ordersLineQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/orders-lines");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /orders-lines/count : count all the ordersLines.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/orders-lines/count")
    @Timed
    public ResponseEntity<Long> countOrdersLines(OrdersLineCriteria criteria) {
        log.debug("REST request to count OrdersLines by criteria: {}", criteria);
        return ResponseEntity.ok().body(ordersLineQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /orders-lines/:id : get the "id" ordersLine.
     *
     * @param id the id of the ordersLineDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ordersLineDTO, or with status 404 (Not Found)
     */
    @GetMapping("/orders-lines/{id}")
    @Timed
    public ResponseEntity<OrdersLineDTO> getOrdersLine(@PathVariable Long id) {
        log.debug("REST request to get OrdersLine : {}", id);
        Optional<OrdersLineDTO> ordersLineDTO = ordersLineService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ordersLineDTO);
    }

    /**
     * DELETE  /orders-lines/:id : delete the "id" ordersLine.
     *
     * @param id the id of the ordersLineDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/orders-lines/{id}")
    @Timed
    public ResponseEntity<Void> deleteOrdersLine(@PathVariable Long id) {
        log.debug("REST request to delete OrdersLine : {}", id);
        ordersLineService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/orders-lines?query=:query : search for the ordersLine corresponding
     * to the query.
     *
     * @param query the query of the ordersLine search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/orders-lines")
    @Timed
    public ResponseEntity<List<OrdersLineDTO>> searchOrdersLines(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of OrdersLines for query {}", query);
        Page<OrdersLineDTO> page = ordersLineService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/orders-lines");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
