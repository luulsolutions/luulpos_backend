package com.luulsolutions.luulpos.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.luulsolutions.luulpos.service.ProductVariantService;
import com.luulsolutions.luulpos.web.rest.errors.BadRequestAlertException;
import com.luulsolutions.luulpos.web.rest.util.HeaderUtil;
import com.luulsolutions.luulpos.web.rest.util.PaginationUtil;
import com.luulsolutions.luulpos.service.dto.ProductVariantDTO;
import com.luulsolutions.luulpos.service.dto.ProductVariantCriteria;
import com.luulsolutions.luulpos.service.ProductVariantQueryService;
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
 * REST controller for managing ProductVariant.
 */
@RestController
@RequestMapping("/api")
public class ProductVariantResource {

    private final Logger log = LoggerFactory.getLogger(ProductVariantResource.class);

    private static final String ENTITY_NAME = "productVariant";

    private final ProductVariantService productVariantService;

    private final ProductVariantQueryService productVariantQueryService;

    public ProductVariantResource(ProductVariantService productVariantService, ProductVariantQueryService productVariantQueryService) {
        this.productVariantService = productVariantService;
        this.productVariantQueryService = productVariantQueryService;
    }

    /**
     * POST  /product-variants : Create a new productVariant.
     *
     * @param productVariantDTO the productVariantDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new productVariantDTO, or with status 400 (Bad Request) if the productVariant has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/product-variants")
    @Timed
    public ResponseEntity<ProductVariantDTO> createProductVariant(@Valid @RequestBody ProductVariantDTO productVariantDTO) throws URISyntaxException {
        log.debug("REST request to save ProductVariant : {}", productVariantDTO);
        if (productVariantDTO.getId() != null) {
            throw new BadRequestAlertException("A new productVariant cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductVariantDTO result = productVariantService.save(productVariantDTO);
        return ResponseEntity.created(new URI("/api/product-variants/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /product-variants : Updates an existing productVariant.
     *
     * @param productVariantDTO the productVariantDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated productVariantDTO,
     * or with status 400 (Bad Request) if the productVariantDTO is not valid,
     * or with status 500 (Internal Server Error) if the productVariantDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/product-variants")
    @Timed
    public ResponseEntity<ProductVariantDTO> updateProductVariant(@Valid @RequestBody ProductVariantDTO productVariantDTO) throws URISyntaxException {
        log.debug("REST request to update ProductVariant : {}", productVariantDTO);
        if (productVariantDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProductVariantDTO result = productVariantService.save(productVariantDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, productVariantDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /product-variants : get all the productVariants.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of productVariants in body
     */
    @GetMapping("/product-variants")
    @Timed
    public ResponseEntity<List<ProductVariantDTO>> getAllProductVariants(ProductVariantCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ProductVariants by criteria: {}", criteria);
        Page<ProductVariantDTO> page = productVariantQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/product-variants");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /product-variants/count : count all the productVariants.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/product-variants/count")
    @Timed
    public ResponseEntity<Long> countProductVariants(ProductVariantCriteria criteria) {
        log.debug("REST request to count ProductVariants by criteria: {}", criteria);
        return ResponseEntity.ok().body(productVariantQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /product-variants/:id : get the "id" productVariant.
     *
     * @param id the id of the productVariantDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the productVariantDTO, or with status 404 (Not Found)
     */
    @GetMapping("/product-variants/{id}")
    @Timed
    public ResponseEntity<ProductVariantDTO> getProductVariant(@PathVariable Long id) {
        log.debug("REST request to get ProductVariant : {}", id);
        Optional<ProductVariantDTO> productVariantDTO = productVariantService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productVariantDTO);
    }

    /**
     * DELETE  /product-variants/:id : delete the "id" productVariant.
     *
     * @param id the id of the productVariantDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/product-variants/{id}")
    @Timed
    public ResponseEntity<Void> deleteProductVariant(@PathVariable Long id) {
        log.debug("REST request to delete ProductVariant : {}", id);
        productVariantService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/product-variants?query=:query : search for the productVariant corresponding
     * to the query.
     *
     * @param query the query of the productVariant search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/product-variants")
    @Timed
    public ResponseEntity<List<ProductVariantDTO>> searchProductVariants(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ProductVariants for query {}", query);
        Page<ProductVariantDTO> page = productVariantService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/product-variants");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
