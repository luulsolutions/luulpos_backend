package com.luulsolutions.luulpos.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.luulsolutions.luulpos.service.OrdersLineVariantService;
import com.luulsolutions.luulpos.web.rest.errors.BadRequestAlertException;
import com.luulsolutions.luulpos.web.rest.util.HeaderUtil;
import com.luulsolutions.luulpos.web.rest.util.PaginationUtil;
import com.luulsolutions.luulpos.service.dto.OrdersLineVariantDTO;
import com.luulsolutions.luulpos.service.dto.OrdersLineVariantCriteria;
import com.luulsolutions.luulpos.service.OrdersLineVariantQueryService;
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
 * REST controller for managing OrdersLineVariant.
 */
@RestController
@RequestMapping("/api")
public class OrdersLineVariantResource {

    private final Logger log = LoggerFactory.getLogger(OrdersLineVariantResource.class);

    private static final String ENTITY_NAME = "ordersLineVariant";

    private final OrdersLineVariantService ordersLineVariantService;

    private final OrdersLineVariantQueryService ordersLineVariantQueryService;

    public OrdersLineVariantResource(OrdersLineVariantService ordersLineVariantService, OrdersLineVariantQueryService ordersLineVariantQueryService) {
        this.ordersLineVariantService = ordersLineVariantService;
        this.ordersLineVariantQueryService = ordersLineVariantQueryService;
    }

    /**
     * POST  /orders-line-variants : Create a new ordersLineVariant.
     *
     * @param ordersLineVariantDTO the ordersLineVariantDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ordersLineVariantDTO, or with status 400 (Bad Request) if the ordersLineVariant has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/orders-line-variants")
    @Timed
    public ResponseEntity<OrdersLineVariantDTO> createOrdersLineVariant(@Valid @RequestBody OrdersLineVariantDTO ordersLineVariantDTO) throws URISyntaxException {
        log.debug("REST request to save OrdersLineVariant : {}", ordersLineVariantDTO);
        if (ordersLineVariantDTO.getId() != null) {
            throw new BadRequestAlertException("A new ordersLineVariant cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrdersLineVariantDTO result = ordersLineVariantService.save(ordersLineVariantDTO);
        return ResponseEntity.created(new URI("/api/orders-line-variants/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /orders-line-variants : Updates an existing ordersLineVariant.
     *
     * @param ordersLineVariantDTO the ordersLineVariantDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ordersLineVariantDTO,
     * or with status 400 (Bad Request) if the ordersLineVariantDTO is not valid,
     * or with status 500 (Internal Server Error) if the ordersLineVariantDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/orders-line-variants")
    @Timed
    public ResponseEntity<OrdersLineVariantDTO> updateOrdersLineVariant(@Valid @RequestBody OrdersLineVariantDTO ordersLineVariantDTO) throws URISyntaxException {
        log.debug("REST request to update OrdersLineVariant : {}", ordersLineVariantDTO);
        if (ordersLineVariantDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OrdersLineVariantDTO result = ordersLineVariantService.save(ordersLineVariantDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, ordersLineVariantDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /orders-line-variants : get all the ordersLineVariants.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of ordersLineVariants in body
     */
    @GetMapping("/orders-line-variants")
    @Timed
    public ResponseEntity<List<OrdersLineVariantDTO>> getAllOrdersLineVariants(OrdersLineVariantCriteria criteria, Pageable pageable) {
        log.debug("REST request to get OrdersLineVariants by criteria: {}", criteria);
        Page<OrdersLineVariantDTO> page = ordersLineVariantQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/orders-line-variants");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /orders-line-variants/count : count all the ordersLineVariants.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/orders-line-variants/count")
    @Timed
    public ResponseEntity<Long> countOrdersLineVariants(OrdersLineVariantCriteria criteria) {
        log.debug("REST request to count OrdersLineVariants by criteria: {}", criteria);
        return ResponseEntity.ok().body(ordersLineVariantQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /orders-line-variants/:id : get the "id" ordersLineVariant.
     *
     * @param id the id of the ordersLineVariantDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ordersLineVariantDTO, or with status 404 (Not Found)
     */
    @GetMapping("/orders-line-variants/{id}")
    @Timed
    public ResponseEntity<OrdersLineVariantDTO> getOrdersLineVariant(@PathVariable Long id) {
        log.debug("REST request to get OrdersLineVariant : {}", id);
        Optional<OrdersLineVariantDTO> ordersLineVariantDTO = ordersLineVariantService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ordersLineVariantDTO);
    }

    /**
     * DELETE  /orders-line-variants/:id : delete the "id" ordersLineVariant.
     *
     * @param id the id of the ordersLineVariantDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/orders-line-variants/{id}")
    @Timed
    public ResponseEntity<Void> deleteOrdersLineVariant(@PathVariable Long id) {
        log.debug("REST request to delete OrdersLineVariant : {}", id);
        ordersLineVariantService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/orders-line-variants?query=:query : search for the ordersLineVariant corresponding
     * to the query.
     *
     * @param query the query of the ordersLineVariant search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/orders-line-variants")
    @Timed
    public ResponseEntity<List<OrdersLineVariantDTO>> searchOrdersLineVariants(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of OrdersLineVariants for query {}", query);
        Page<OrdersLineVariantDTO> page = ordersLineVariantService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/orders-line-variants");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
