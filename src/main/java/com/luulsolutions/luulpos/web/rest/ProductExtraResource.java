package com.luulsolutions.luulpos.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.luulsolutions.luulpos.service.ProductExtraService;
import com.luulsolutions.luulpos.web.rest.errors.BadRequestAlertException;
import com.luulsolutions.luulpos.web.rest.util.HeaderUtil;
import com.luulsolutions.luulpos.web.rest.util.PaginationUtil;
import com.luulsolutions.luulpos.service.dto.ProductExtraDTO;
import com.luulsolutions.luulpos.service.dto.ProductExtraCriteria;
import com.luulsolutions.luulpos.service.ProductExtraQueryService;
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
 * REST controller for managing ProductExtra.
 */
@RestController
@RequestMapping("/api")
public class ProductExtraResource {

    private final Logger log = LoggerFactory.getLogger(ProductExtraResource.class);

    private static final String ENTITY_NAME = "productExtra";

    private final ProductExtraService productExtraService;

    private final ProductExtraQueryService productExtraQueryService;

    public ProductExtraResource(ProductExtraService productExtraService, ProductExtraQueryService productExtraQueryService) {
        this.productExtraService = productExtraService;
        this.productExtraQueryService = productExtraQueryService;
    }

    /**
     * POST  /product-extras : Create a new productExtra.
     *
     * @param productExtraDTO the productExtraDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new productExtraDTO, or with status 400 (Bad Request) if the productExtra has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/product-extras")
    @Timed
    public ResponseEntity<ProductExtraDTO> createProductExtra(@RequestBody ProductExtraDTO productExtraDTO) throws URISyntaxException {
        log.debug("REST request to save ProductExtra : {}", productExtraDTO);
        if (productExtraDTO.getId() != null) {
            throw new BadRequestAlertException("A new productExtra cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductExtraDTO result = productExtraService.save(productExtraDTO);
        return ResponseEntity.created(new URI("/api/product-extras/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /product-extras : Updates an existing productExtra.
     *
     * @param productExtraDTO the productExtraDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated productExtraDTO,
     * or with status 400 (Bad Request) if the productExtraDTO is not valid,
     * or with status 500 (Internal Server Error) if the productExtraDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/product-extras")
    @Timed
    public ResponseEntity<ProductExtraDTO> updateProductExtra(@RequestBody ProductExtraDTO productExtraDTO) throws URISyntaxException {
        log.debug("REST request to update ProductExtra : {}", productExtraDTO);
        if (productExtraDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProductExtraDTO result = productExtraService.save(productExtraDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, productExtraDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /product-extras : get all the productExtras.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of productExtras in body
     */
    @GetMapping("/product-extras")
    @Timed
    public ResponseEntity<List<ProductExtraDTO>> getAllProductExtras(ProductExtraCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ProductExtras by criteria: {}", criteria);
        Page<ProductExtraDTO> page = productExtraQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/product-extras");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /product-extras/count : count all the productExtras.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/product-extras/count")
    @Timed
    public ResponseEntity<Long> countProductExtras(ProductExtraCriteria criteria) {
        log.debug("REST request to count ProductExtras by criteria: {}", criteria);
        return ResponseEntity.ok().body(productExtraQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /product-extras/:id : get the "id" productExtra.
     *
     * @param id the id of the productExtraDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the productExtraDTO, or with status 404 (Not Found)
     */
    @GetMapping("/product-extras/{id}")
    @Timed
    public ResponseEntity<ProductExtraDTO> getProductExtra(@PathVariable Long id) {
        log.debug("REST request to get ProductExtra : {}", id);
        Optional<ProductExtraDTO> productExtraDTO = productExtraService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productExtraDTO);
    }

    /**
     * DELETE  /product-extras/:id : delete the "id" productExtra.
     *
     * @param id the id of the productExtraDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/product-extras/{id}")
    @Timed
    public ResponseEntity<Void> deleteProductExtra(@PathVariable Long id) {
        log.debug("REST request to delete ProductExtra : {}", id);
        productExtraService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/product-extras?query=:query : search for the productExtra corresponding
     * to the query.
     *
     * @param query the query of the productExtra search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/product-extras")
    @Timed
    public ResponseEntity<List<ProductExtraDTO>> searchProductExtras(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ProductExtras for query {}", query);
        Page<ProductExtraDTO> page = productExtraService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/product-extras");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
