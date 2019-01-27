package com.luulsolutions.luulpos.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
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
import com.luulsolutions.luulpos.service.ProductService;
import com.luulsolutions.luulpos.service.ProductVariantQueryService;
import com.luulsolutions.luulpos.service.ProductVariantService;
import com.luulsolutions.luulpos.service.ShopChangeService;
import com.luulsolutions.luulpos.service.dto.ProductDTO;
import com.luulsolutions.luulpos.service.dto.ProductVariantCriteria;
import com.luulsolutions.luulpos.service.dto.ProductVariantDTO;
import com.luulsolutions.luulpos.service.s3.S3Service;
import com.luulsolutions.luulpos.utils.CommonUtils;
import com.luulsolutions.luulpos.utils.Constants;
import com.luulsolutions.luulpos.web.rest.errors.BadRequestAlertException;
import com.luulsolutions.luulpos.web.rest.util.HeaderUtil;
import com.luulsolutions.luulpos.web.rest.util.PaginationUtil;

import io.github.jhipster.web.util.ResponseUtil;

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
    
    @Autowired
    ProductService productService;
   
    @Autowired
    ShopChangeService shopChangeService;
    
    @Autowired
    private S3Service s3Service;


    public ProductVariantResource(ProductVariantService productVariantService, ProductVariantQueryService productVariantQueryService) {
        this.productVariantService = productVariantService;
        this.productVariantQueryService = productVariantQueryService;
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
        Optional <ProductDTO> productDTO = productService.findOne(productVariantDTO.getProductId());
        CommonUtils.saveShopChange(shopChangeService, productDTO.get().getShopId(), "ProductVariant", "New ProductVariant created", productDTO.get().getShopShopName()); 
      
        String fileName = "ProductVariant" + result.getId()  + ".png";
        String url = "https://s3-eu-west-1.amazonaws.com/luulpos/" + fileName;
        result.setFullPhotoUrl(url);
        byte[] imageBytes = CommonUtils.resize(CommonUtils.createImageFromBytes(productVariantDTO.getFullPhoto()),  Constants.FULL_IMAGE_HEIGHT,  Constants.FULL_IMAGE_WIDTH);
        CommonUtils.uploadToS3(imageBytes,fileName,s3Service.getAmazonS3() );
        ProductVariantDTO result2 = productVariantService.save(result);
        result2.setFullPhoto(null);
        result2.setFullPhotoContentType(null);
        
        String fileName2 = "ProductVariantThumb" + result.getId()  + ".png";
        String url2 = "https://s3-eu-west-1.amazonaws.com/luulpos/" + fileName2;
        result2.setThumbnailPhotoUrl(url2);
        byte[] imageBytes2 = CommonUtils.resize(CommonUtils.createImageFromBytes(productVariantDTO.getThumbnailPhoto()),  Constants.THUMBNAIL_HEIGHT,  Constants.THUMBNAIL_WIDTH);
        CommonUtils.uploadToS3(imageBytes2,fileName2,s3Service.getAmazonS3() );
        result2.setThumbnailPhoto(null);
        result2.setThumbnailPhotoContentType(null);
        
  
        productVariantService.save(result2);
        
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
        Optional <ProductDTO> productDTO = productService.findOne(productVariantDTO.getProductId());
        CommonUtils.saveShopChange(shopChangeService, productDTO.get().getShopId(), "ProductVariant", "Existing ProductVariant updated", productDTO.get().getShopShopName()); 
       
   if (productVariantDTO.getFullPhoto() != null) {
        String fileName = "ProductVariant" + result.getId()  + ".png";
        String url = "https://s3-eu-west-1.amazonaws.com/luulpos/" + fileName;
        result.setFullPhotoUrl(url);
        byte[] imageBytes = CommonUtils.resize(CommonUtils.createImageFromBytes(productVariantDTO.getFullPhoto()),  Constants.FULL_IMAGE_HEIGHT,  Constants.FULL_IMAGE_WIDTH);
        CommonUtils.uploadToS3(imageBytes,fileName,s3Service.getAmazonS3() );
        result.setFullPhoto(null);
        result.setFullPhotoContentType(null);
        productVariantService.save(result);

   }
   
   if (productVariantDTO.getThumbnailPhoto() != null) {

        String fileName2 = "ProductVariantThumb" + result.getId()  + ".png";
        String url2 = "https://s3-eu-west-1.amazonaws.com/luulpos/" + fileName2;
        ProductVariantDTO result2 = productVariantService.save(result);
        result2.setThumbnailPhotoUrl(url2);
        byte[] imageBytes2 = CommonUtils.resize(CommonUtils.createImageFromBytes(productVariantDTO.getThumbnailPhoto()),  Constants.THUMBNAIL_HEIGHT,  Constants.THUMBNAIL_WIDTH);
        CommonUtils.uploadToS3(imageBytes2,fileName2,s3Service.getAmazonS3() );
        result2.setThumbnailPhoto(null);
        result2.setThumbnailPhotoContentType(null);
        productVariantService.save(result2);
   }
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, productVariantDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /product-variants : get all the productVariants.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of productVariants in body
     */
    @GetMapping("/product-variants")
    @Timed
    public ResponseEntity<List<ProductVariantDTO>> getAllProductVariants(Pageable pageable) {
        log.debug("REST request to get a page of ProductVariants");
        Pageable pageable2 =  PageRequest.of(pageable.getPageNumber(),2000);
        Page<ProductVariantDTO> page = productVariantService.findAll(pageable2);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/product-variants");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
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
        Optional<ProductVariantDTO> productVariantDTO = productVariantService.findOne(id);
        Optional <ProductDTO> productDTO = productService.findOne(productVariantDTO.get().getProductId());
        long shopId  = productDTO.get().getShopId();
        String shopName = productDTO.get().getShopShopName();
        productVariantService.delete(id);
        CommonUtils.saveShopChange(shopChangeService, shopId, "ProductVariant", "Existing ProductVariant updated", shopName); 
        CommonUtils.deleteFromS3("ProductVariant" + id + ".png", s3Service.getAmazonS3());
        CommonUtils.deleteFromS3("ProductVariantThumb" + id + ".png", s3Service.getAmazonS3());      
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
