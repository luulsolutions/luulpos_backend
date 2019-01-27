package com.luulsolutions.luulpos.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.luulsolutions.luulpos.service.ProductCategoryService;
import com.luulsolutions.luulpos.service.ShopChangeService;
import com.luulsolutions.luulpos.web.rest.errors.BadRequestAlertException;
import com.luulsolutions.luulpos.web.rest.util.HeaderUtil;
import com.luulsolutions.luulpos.web.rest.util.PaginationUtil;
import com.luulsolutions.luulpos.service.dto.ProductCategoryDTO;
import com.luulsolutions.luulpos.service.s3.S3Service;
import com.luulsolutions.luulpos.utils.CommonUtils;
import com.luulsolutions.luulpos.utils.Constants;
import com.luulsolutions.luulpos.service.dto.ProductCategoryCriteria;
import com.luulsolutions.luulpos.service.ProductCategoryQueryService;
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

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing ProductCategory.
 */
@RestController
@RequestMapping("/api")
public class ProductCategoryResource {

    private final Logger log = LoggerFactory.getLogger(ProductCategoryResource.class);

    private static final String ENTITY_NAME = "productCategory";

    private final ProductCategoryService productCategoryService;

    private final ProductCategoryQueryService productCategoryQueryService;
    
    @Autowired
    ShopChangeService shopChangeService;
    
    @Autowired
    private S3Service s3Service;

    public ProductCategoryResource(ProductCategoryService productCategoryService, ProductCategoryQueryService productCategoryQueryService) {
        this.productCategoryService = productCategoryService;
        this.productCategoryQueryService = productCategoryQueryService;
    }

   

    /**
    * GET  /product-categories/count : count all the productCategories.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/product-categories/count")
    @Timed
    public ResponseEntity<Long> countProductCategories(ProductCategoryCriteria criteria) {
        log.debug("REST request to count ProductCategories by criteria: {}", criteria);
        return ResponseEntity.ok().body(productCategoryQueryService.countByCriteria(criteria));
    }

    /**
     * POST  /product-categories : Create a new productCategory.
     *
     * @param productCategoryDTO the productCategoryDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new productCategoryDTO, or with status 400 (Bad Request) if the productCategory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/product-categories")
    @Timed
    public ResponseEntity<ProductCategoryDTO> createProductCategory(@RequestBody ProductCategoryDTO productCategoryDTO) throws URISyntaxException {
        log.debug("REST request to save ProductCategory : {}", productCategoryDTO);
        if (productCategoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new productCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductCategoryDTO result = productCategoryService.save(productCategoryDTO);
        String fileName = "ProductCategory" + result.getId()  + ".png";
        String url = "https://s3-eu-west-1.amazonaws.com/luulpos/" + fileName;
        result.setImageFullUrl(url);
        byte[] imageBytes = CommonUtils.resize(CommonUtils.createImageFromBytes(productCategoryDTO.getImageFull()),  Constants.FULL_IMAGE_HEIGHT,  Constants.FULL_IMAGE_WIDTH);
        CommonUtils.uploadToS3(imageBytes,fileName,s3Service.getAmazonS3() );
        ProductCategoryDTO result2 = productCategoryService.save(result);
        result2.setImageFull(null);
        result2.setImageFullContentType(null);
        
        String fileName2 = "ProductCategoryThumb" + result.getId()  + ".png";
        String url2 = "https://s3-eu-west-1.amazonaws.com/luulpos/" + fileName2;
        result2.setImageThumbUrl(url2);
        byte[] imageBytes2 = CommonUtils.resize(CommonUtils.createImageFromBytes(productCategoryDTO.getImageThumb()),  Constants.THUMBNAIL_HEIGHT,  Constants.THUMBNAIL_WIDTH);

        CommonUtils.uploadToS3(imageBytes2,fileName2,s3Service.getAmazonS3() );
        result2.setImageThumb(null);
        result2.setImageThumbContentType(null);
        
  
        productCategoryService.save(result2);
        
        CommonUtils.saveShopChange(shopChangeService, productCategoryDTO.getShopId(), "ProductCategory", "New ProductCategory created", productCategoryDTO.getShopShopName()); 
        return ResponseEntity.created(new URI("/api/product-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /product-categories : Updates an existing productCategory.
     *
     * @param productCategoryDTO the productCategoryDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated productCategoryDTO,
     * or with status 400 (Bad Request) if the productCategoryDTO is not valid,
     * or with status 500 (Internal Server Error) if the productCategoryDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/product-categories")
    @Timed
    public ResponseEntity<ProductCategoryDTO> updateProductCategory(@RequestBody ProductCategoryDTO productCategoryDTO) throws URISyntaxException {
        log.debug("REST request to update ProductCategory : {}", productCategoryDTO);
        if (productCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProductCategoryDTO result = productCategoryService.save(productCategoryDTO);
        CommonUtils.saveShopChange(shopChangeService, productCategoryDTO.getShopId(), "ProductCategory", "Existing ProductCategory updated", productCategoryDTO.getShopShopName()); 
        if (productCategoryDTO.getImageFull() != null) {
        	String fileName = "ProductCategory" + result.getId()  + ".png";
            String url = "https://s3-eu-west-1.amazonaws.com/luulpos/" + fileName;
            result.setImageFullUrl(url);
            byte[] imageBytes = CommonUtils.resize(CommonUtils.createImageFromBytes(productCategoryDTO.getImageFull()),  Constants.FULL_IMAGE_HEIGHT,  Constants.FULL_IMAGE_WIDTH);
            CommonUtils.uploadToS3(imageBytes,fileName,s3Service.getAmazonS3() );
            
            result.setImageFull(null);
            result.setImageFullContentType(null);
            productCategoryService.save(result);
        }
        
        if (productCategoryDTO.getImageThumb() != null) {
            String fileName2 = "ProductCategoryThumb" + result.getId()  + ".png";
            String url2 = "https://s3-eu-west-1.amazonaws.com/luulpos/" + fileName2;
            ProductCategoryDTO result2 = productCategoryService.save(result);
            result2.setImageThumbUrl(url2);
            byte[] imageBytes = CommonUtils.resize(CommonUtils.createImageFromBytes(productCategoryDTO.getImageThumb()),  Constants.THUMBNAIL_HEIGHT,  Constants.THUMBNAIL_WIDTH);

            CommonUtils.uploadToS3(imageBytes,fileName2,s3Service.getAmazonS3() );
            result2.setImageThumb(null);
            result2.setImageThumbContentType(null);    
            productCategoryService.save(result2);
        }
        
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, productCategoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /product-categories : get all the productCategories.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of productCategories in body
     */
    @GetMapping("/product-categories")
    @Timed
    public ResponseEntity<List<ProductCategoryDTO>> getAllProductCategories(Pageable pageable) {
        log.debug("REST request to get a page of ProductCategories");
        Pageable pageable2 =  PageRequest.of(pageable.getPageNumber(),2000);
        Page<ProductCategoryDTO> page = productCategoryService.findAll(pageable2);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/product-categories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /product-categories/:id : get the "id" productCategory.
     *
     * @param id the id of the productCategoryDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the productCategoryDTO, or with status 404 (Not Found)
     */
    @GetMapping("/product-categories/{id}")
    @Timed
    public ResponseEntity<ProductCategoryDTO> getProductCategory(@PathVariable Long id) {
        log.debug("REST request to get ProductCategory : {}", id);
        Optional<ProductCategoryDTO> productCategoryDTO = productCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productCategoryDTO);
    }

    /**
     * DELETE  /product-categories/:id : delete the "id" productCategory.
     *
     * @param id the id of the productCategoryDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/product-categories/{id}")
    @Timed
    public ResponseEntity<Void> deleteProductCategory(@PathVariable Long id) {
        log.debug("REST request to delete ProductCategory : {}", id);
        Optional<ProductCategoryDTO> productCategoryDTO = productCategoryService.findOne(id);
        long shopId = productCategoryDTO.get().getShopId();
        String shopName = productCategoryDTO.get().getShopShopName();
        productCategoryService.delete(id);
        CommonUtils.saveShopChange(shopChangeService, shopId, "ProductCategory", "Existing ProductCategory deleted", shopName); 
        CommonUtils.deleteFromS3("ProductCategory" + id + ".png", s3Service.getAmazonS3());
        CommonUtils.deleteFromS3("ProductCategoryThumb" + id + ".png", s3Service.getAmazonS3());

        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/product-categories?query=:query : search for the productCategory corresponding
     * to the query.
     *
     * @param query the query of the productCategory search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/product-categories")
    @Timed
    public ResponseEntity<List<ProductCategoryDTO>> searchProductCategories(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ProductCategories for query {}", query);
        Page<ProductCategoryDTO> page = productCategoryService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/product-categories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    
    
    /**
     * GET  /product-category-by-shop-id: get all the productCategoryByShopId.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of product categories in body
     */
    @GetMapping("/product-categor-by-shop-id/{shopId}")
    @Timed
    public ResponseEntity<List<ProductCategoryDTO>> getAllProductCategorByShopId(Pageable pageable, @PathVariable Long shopId) {
        log.debug("REST request to get a page of getAllProductCategorByShopId");
        Page<ProductCategoryDTO> page = productCategoryService.findAllProductCategoryByShopId(pageable, shopId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/product-categor-by-shop-id");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
         

}
