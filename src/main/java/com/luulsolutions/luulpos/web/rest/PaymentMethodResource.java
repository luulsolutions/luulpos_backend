package com.luulsolutions.luulpos.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.luulsolutions.luulpos.service.PaymentMethodService;
import com.luulsolutions.luulpos.service.ShopChangeService;
import com.luulsolutions.luulpos.web.rest.errors.BadRequestAlertException;
import com.luulsolutions.luulpos.web.rest.util.HeaderUtil;
import com.luulsolutions.luulpos.web.rest.util.PaginationUtil;
import com.luulsolutions.luulpos.service.dto.PaymentMethodDTO;
import com.luulsolutions.luulpos.utils.CommonUtils;
import com.luulsolutions.luulpos.service.dto.PaymentMethodCriteria;
import com.luulsolutions.luulpos.service.PaymentMethodQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
 * REST controller for managing PaymentMethod.
 */
@RestController
@RequestMapping("/api")
public class PaymentMethodResource {

    private final Logger log = LoggerFactory.getLogger(PaymentMethodResource.class);

    private static final String ENTITY_NAME = "paymentMethod";

    private final PaymentMethodService paymentMethodService;

    private final PaymentMethodQueryService paymentMethodQueryService;
    
    @Autowired
    ShopChangeService shopChangeService;

    public PaymentMethodResource(PaymentMethodService paymentMethodService, PaymentMethodQueryService paymentMethodQueryService) {
        this.paymentMethodService = paymentMethodService;
        this.paymentMethodQueryService = paymentMethodQueryService;
    }

  

    /**
    * GET  /payment-methods/count : count all the paymentMethods.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/payment-methods/count")
    @Timed
    public ResponseEntity<Long> countPaymentMethods(PaymentMethodCriteria criteria) {
        log.debug("REST request to count PaymentMethods by criteria: {}", criteria);
        return ResponseEntity.ok().body(paymentMethodQueryService.countByCriteria(criteria));
    }

    /**
     * POST  /payment-methods : Create a new paymentMethod.
     *
     * @param paymentMethodDTO the paymentMethodDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new paymentMethodDTO, or with status 400 (Bad Request) if the paymentMethod has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/payment-methods")
    @Timed
    public ResponseEntity<PaymentMethodDTO> createPaymentMethod(@Valid @RequestBody PaymentMethodDTO paymentMethodDTO) throws URISyntaxException {
        log.debug("REST request to save PaymentMethod : {}", paymentMethodDTO);
        if (paymentMethodDTO.getId() != null) {
            throw new BadRequestAlertException("A new paymentMethod cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PaymentMethodDTO result = paymentMethodService.save(paymentMethodDTO);
        CommonUtils.saveShopChange(shopChangeService, paymentMethodDTO.getShopId(), "PaymentMethod", "New PaymentMethod created", paymentMethodDTO.getShopShopName()); 

        return ResponseEntity.created(new URI("/api/payment-methods/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /payment-methods : Updates an existing paymentMethod.
     *
     * @param paymentMethodDTO the paymentMethodDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated paymentMethodDTO,
     * or with status 400 (Bad Request) if the paymentMethodDTO is not valid,
     * or with status 500 (Internal Server Error) if the paymentMethodDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/payment-methods")
    @Timed
    public ResponseEntity<PaymentMethodDTO> updatePaymentMethod(@Valid @RequestBody PaymentMethodDTO paymentMethodDTO) throws URISyntaxException {
        log.debug("REST request to update PaymentMethod : {}", paymentMethodDTO);
        if (paymentMethodDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PaymentMethodDTO result = paymentMethodService.save(paymentMethodDTO);
        CommonUtils.saveShopChange(shopChangeService, paymentMethodDTO.getShopId(), "PaymentMethod", "Existing PaymentMethod updated", paymentMethodDTO.getShopShopName()); 
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, paymentMethodDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /payment-methods : get all the paymentMethods.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of paymentMethods in body
     */
    @GetMapping("/payment-methods")
    @Timed
    public ResponseEntity<List<PaymentMethodDTO>> getAllPaymentMethods(Pageable pageable) {
        log.debug("REST request to get a page of PaymentMethods");
        Pageable pageable2 =  PageRequest.of(pageable.getPageNumber(),2000);
        Page<PaymentMethodDTO> page = paymentMethodService.findAll(pageable2);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/payment-methods");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /payment-methods/:id : get the "id" paymentMethod.
     *
     * @param id the id of the paymentMethodDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the paymentMethodDTO, or with status 404 (Not Found)
     */
    @GetMapping("/payment-methods/{id}")
    @Timed
    public ResponseEntity<PaymentMethodDTO> getPaymentMethod(@PathVariable Long id) {
        log.debug("REST request to get PaymentMethod : {}", id);
        Optional<PaymentMethodDTO> paymentMethodDTO = paymentMethodService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paymentMethodDTO);
    }

    /**
     * DELETE  /payment-methods/:id : delete the "id" paymentMethod.
     *
     * @param id the id of the paymentMethodDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/payment-methods/{id}")
    @Timed
    public ResponseEntity<Void> deletePaymentMethod(@PathVariable Long id) {
        log.debug("REST request to delete PaymentMethod : {}", id);
        Optional<PaymentMethodDTO> paymentMethodDTO = paymentMethodService.findOne(id);
        long shopId = paymentMethodDTO.get().getShopId();
        String shopName = paymentMethodDTO.get().getShopShopName();
        paymentMethodService.delete(id);
        CommonUtils.saveShopChange(shopChangeService, shopId, "PaymentMethod", "Existing PaymentMethod deleted", shopName); 
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/payment-methods?query=:query : search for the paymentMethod corresponding
     * to the query.
     *
     * @param query the query of the paymentMethod search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/payment-methods")
    @Timed
    public ResponseEntity<List<PaymentMethodDTO>> searchPaymentMethods(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of PaymentMethods for query {}", query);
        Page<PaymentMethodDTO> page = paymentMethodService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/payment-methods");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    } 

}
