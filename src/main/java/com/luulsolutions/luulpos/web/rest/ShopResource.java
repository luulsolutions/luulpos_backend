package com.luulsolutions.luulpos.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.luulsolutions.luulpos.security.SecurityUtils;
import com.luulsolutions.luulpos.service.ShopChangeService;
import com.luulsolutions.luulpos.service.ShopQueryService;
import com.luulsolutions.luulpos.service.ShopService;
import com.luulsolutions.luulpos.service.dto.ShopCriteria;
import com.luulsolutions.luulpos.service.dto.ShopDTO;
import com.luulsolutions.luulpos.service.s3.S3Service;
import com.luulsolutions.luulpos.utils.CommonUtils;
import com.luulsolutions.luulpos.utils.Constants;
import com.luulsolutions.luulpos.web.rest.errors.BadRequestAlertException;
import com.luulsolutions.luulpos.web.rest.util.HeaderUtil;
import com.luulsolutions.luulpos.web.rest.util.PaginationUtil;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Shop.
 */
@RestController
@RequestMapping("/api")
public class ShopResource {

    private final Logger log = LoggerFactory.getLogger(ShopResource.class);

    private static final String ENTITY_NAME = "shop";

    private final ShopService shopService;

    private final ShopQueryService shopQueryService;
    
    @Autowired
    ShopChangeService shopChangeService;
    
    @Autowired
    private S3Service s3Service;

    public ShopResource(ShopService shopService, ShopQueryService shopQueryService) {
        this.shopService = shopService;
        this.shopQueryService = shopQueryService;
    }

  

    /**
    * GET  /shops/count : count all the shops.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/shops/count")
    @Timed
    public ResponseEntity<Long> countShops(ShopCriteria criteria) {
        log.debug("REST request to count Shops by criteria: {}", criteria);
        return ResponseEntity.ok().body(shopQueryService.countByCriteria(criteria));
    }


    /**
     * POST  /shops : Create a new shop.
     *
     * @param shopDTO the shopDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new shopDTO, or with status 400 (Bad Request) if the shop has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/shops")
    @Timed
    public ResponseEntity<ShopDTO> createShop(@Valid @RequestBody ShopDTO shopDTO) throws URISyntaxException {
        log.debug("REST request to save Shop : {}", shopDTO);
        if (shopDTO.getId() != null) {
            throw new BadRequestAlertException("A new shop cannot already have an ID", ENTITY_NAME, "idexists");
        }
        /*  --START--
         * ADD DEFAULT VALUES TO THE FIELDS BELOW
         */
        shopDTO.setCreatedDate(ZonedDateTime.now());
        shopDTO.setActive(true);
        shopDTO.setApprovalDate(ZonedDateTime.now());
        shopDTO.setApprovedByFirstName(SecurityUtils.getCurrentUserLogin().get());
        
        /*  --END-- */
        ShopDTO result = shopService.save(shopDTO); 
        CommonUtils.saveShopChange(shopChangeService, shopDTO.getCompanyId(), "Shop", "New Shop created", shopDTO.getShopName()); 
        
        String fileName = "Shop" + result.getId()  + ".png";
        String shopLogoUrl = "https://s3-eu-west-1.amazonaws.com/luulpos/" + fileName;
        result.setShopLogoUrl(shopLogoUrl);
        byte[] imageBytes = CommonUtils.resize(CommonUtils.createImageFromBytes(shopDTO.getShopLogo()),  Constants.FULL_IMAGE_HEIGHT,  Constants.FULL_IMAGE_WIDTH);
        CommonUtils.uploadToS3(imageBytes,fileName,s3Service.getAmazonS3() );
        ShopDTO result2 = shopService.save(result);
        result2.setShopLogo(null);
        result2.setShopLogoContentType(null);
         shopService.save(result2);
        
        return ResponseEntity.created(new URI("/api/shops/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /shops : Updates an existing shop.
     *
     * @param shopDTO the shopDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated shopDTO,
     * or with status 400 (Bad Request) if the shopDTO is not valid,
     * or with status 500 (Internal Server Error) if the shopDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/shops")
    @Timed
    public ResponseEntity<ShopDTO> updateShop(@Valid @RequestBody ShopDTO shopDTO) throws URISyntaxException {
        log.debug("REST request to update Shop : {}", shopDTO);
        if (shopDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ShopDTO result = shopService.save(shopDTO);
        CommonUtils.saveShopChange(shopChangeService, shopDTO.getId(), "Shop", "Existing Shop updated", shopDTO.getShopName()); 
       if (shopDTO.getShopLogo() != null) {
        String fileName = "Shop" + result.getId()  + ".png";
        String shopLogoUrl = "https://s3-eu-west-1.amazonaws.com/luulpos/" + fileName;
        result.setShopLogoUrl(shopLogoUrl);
        byte[] imageBytes = CommonUtils.resize(CommonUtils.createImageFromBytes(shopDTO.getShopLogo()),  Constants.FULL_IMAGE_HEIGHT,  Constants.FULL_IMAGE_WIDTH);
        CommonUtils.uploadToS3(imageBytes,fileName,s3Service.getAmazonS3() );
        ShopDTO result2 = shopService.save(result);
        result2.setShopLogo(null);
        result2.setShopLogoContentType(null);
         shopService.save(result2);
       }
        
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, shopDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /shops : get all the shops.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of shops in body
     */
    @GetMapping("/shops")
    @Timed
    public ResponseEntity<List<ShopDTO>> getAllShops(Pageable pageable) {
        log.debug("REST request to get a page of Shops");
        Pageable pageable2 =  PageRequest.of(pageable.getPageNumber(),2000);
        Page<ShopDTO> page = shopService.findAll(pageable2);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/shops");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /shops/:id : get the "id" shop.
     *
     * @param id the id of the shopDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the shopDTO, or with status 404 (Not Found)
     */
    @GetMapping("/shops/{id}")
    @Timed
    public ResponseEntity<ShopDTO> getShop(@PathVariable Long id) {
        log.debug("REST request to get Shop : {}", id);
        Optional<ShopDTO> shopDTO = shopService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shopDTO);
    }

    /**
     * DELETE  /shops/:id : delete the "id" shop.
     *
     * @param id the id of the shopDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/shops/{id}")
    @Timed
    public ResponseEntity<Void> deleteShop(@PathVariable Long id) {
        log.debug("REST request to delete Shop : {}", id);
        Optional<ShopDTO> shopDTO = shopService.findOne(id);
        long shopId = shopDTO.get().getId();
        String shopName = shopDTO.get().getShopName();
        shopService.delete(id);
        CommonUtils.saveShopChange(shopChangeService, shopId, "Shop", "Existing Shop deleted", shopName); 
        CommonUtils.deleteFromS3("Shop" + id + ".png", s3Service.getAmazonS3());        
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/shops?query=:query : search for the shop corresponding
     * to the query.
     *
     * @param query the query of the shop search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/shops")
    @Timed
    public ResponseEntity<List<ShopDTO>> searchShops(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Shops for query {}", query);
        Page<ShopDTO> page = shopService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/shops");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
