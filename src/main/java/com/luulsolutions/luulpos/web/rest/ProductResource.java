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
import com.luulsolutions.luulpos.service.ProductQueryService;
import com.luulsolutions.luulpos.service.ProductService;
import com.luulsolutions.luulpos.service.ShopChangeService;
import com.luulsolutions.luulpos.service.dto.ProductCriteria;
import com.luulsolutions.luulpos.service.dto.ProductDTO;
import com.luulsolutions.luulpos.service.dto.ProductDTOFull;
import com.luulsolutions.luulpos.service.s3.S3Service;
import com.luulsolutions.luulpos.utils.CommonUtils;
import com.luulsolutions.luulpos.utils.Constants;
import com.luulsolutions.luulpos.web.rest.errors.BadRequestAlertException;
import com.luulsolutions.luulpos.web.rest.util.HeaderUtil;
import com.luulsolutions.luulpos.web.rest.util.PaginationUtil;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Product.
 */
@RestController
@RequestMapping("/api")
public class ProductResource {

    private final Logger log = LoggerFactory.getLogger(ProductResource.class);

    private static final String ENTITY_NAME = "product";

    private final ProductService productService;

    private final ProductQueryService productQueryService;
    
    @Autowired
    ShopChangeService shopChangeService;
    
    @Autowired
    private S3Service s3Service;
    
    public ProductResource(ProductService productService, ProductQueryService productQueryService) {
        this.productService = productService;
        this.productQueryService = productQueryService;
    }

   
    /**
    * GET  /products/count : count all the products.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/products/count")
    @Timed
    public ResponseEntity<Long> countProducts(ProductCriteria criteria) {
        log.debug("REST request to count Products by criteria: {}", criteria);
        return ResponseEntity.ok().body(productQueryService.countByCriteria(criteria));
    }

    /**
     * POST  /products : Create a new product.
     *
     * @param productDTO the productDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new productDTO, or with status 400 (Bad Request) if the product has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/products")
    @Timed
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO) throws URISyntaxException {
        log.debug("REST request to save Product : {}", productDTO);
        if (productDTO.getId() != null) {
            throw new BadRequestAlertException("A new product cannot already have an ID", ENTITY_NAME, "idexists");
        }
        /* -START-
        * ADD DEFAULT VALUES TO THE FIELDS BELOW
        */
        productDTO.setDateCreated(ZonedDateTime.now());
        productDTO.setActive(true);
        /* -END- */
        ProductDTO result = productService.save(productDTO);
        CommonUtils.saveShopChange(shopChangeService, productDTO.getShopId(), "Product", "New Product created", productDTO.getShopShopName()); 
     
        String fileName = "Product" + result.getId()  + ".png";
        String url = "https://s3-eu-west-1.amazonaws.com/luulpos/" + fileName;
        result.setProductImageFullUrl(url);
        byte[] imageBytes = CommonUtils.resize(CommonUtils.createImageFromBytes(productDTO.getProductImageFull()),  Constants.FULL_IMAGE_HEIGHT,  Constants.FULL_IMAGE_WIDTH);
        CommonUtils.uploadToS3(imageBytes,fileName,s3Service.getAmazonS3() );
        ProductDTO result2 = productService.save(result);
        result2.setProductImageFull(null);
        result2.setProductImageFullContentType(null);
        
        String fileName2 = "ProductThumb" + result.getId()  + ".png";
        String url2 = "https://s3-eu-west-1.amazonaws.com/luulpos/" + fileName2;
        result2.setProductImageThumbUrl(url2);
        byte[] imageBytes2 = CommonUtils.resize(CommonUtils.createImageFromBytes(productDTO.getProductImageThumb()),  Constants.THUMBNAIL_HEIGHT,  Constants.THUMBNAIL_WIDTH);
        CommonUtils.uploadToS3(imageBytes2,fileName2,s3Service.getAmazonS3() );
        result2.setProductImageThumb(null);
        result2.setProductImageThumbContentType(null);
        
        productService.save(result2);
        
        
        return ResponseEntity.created(new URI("/api/products/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /products : Updates an existing product.
     *
     * @param productDTO the productDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated productDTO,
     * or with status 400 (Bad Request) if the productDTO is not valid,
     * or with status 500 (Internal Server Error) if the productDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/products")
    @Timed
    public ResponseEntity<ProductDTO> updateProduct(@Valid @RequestBody ProductDTO productDTO) throws URISyntaxException {
        log.debug("REST request to update Product : {}", productDTO);
        if (productDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProductDTO result = productService.save(productDTO);
        CommonUtils.saveShopChange(shopChangeService, productDTO.getShopId(), "Product", "Existng Product updated", productDTO.getShopShopName()); 
        if (productDTO.getProductImageFull() != null) {
        String fileName = "Product" + result.getId()  + ".png";
        String url = "https://s3-eu-west-1.amazonaws.com/luulpos/" + fileName;
        result.setProductImageFullUrl(url);
        byte[] imageBytes = CommonUtils.resize(CommonUtils.createImageFromBytes(productDTO.getProductImageFull()),  Constants.FULL_IMAGE_HEIGHT,  Constants.FULL_IMAGE_WIDTH);
        CommonUtils.uploadToS3(imageBytes,fileName,s3Service.getAmazonS3() );
        result.setProductImageFull(null);
        result.setProductImageFullContentType(null);
        productService.save(result);
        }
        
        if (productDTO.getProductImageThumb() != null) {

        String fileName2 = "ProductThumb" + result.getId()  + ".png";
        String url2 = "https://s3-eu-west-1.amazonaws.com/luulpos/" + fileName2;
        ProductDTO result2 = productService.save(result);
        result2.setProductImageThumbUrl(url2);
        byte[] imageBytes2 = CommonUtils.resize(CommonUtils.createImageFromBytes(productDTO.getProductImageThumb()),  Constants.THUMBNAIL_HEIGHT,  Constants.THUMBNAIL_WIDTH);
        CommonUtils.uploadToS3(imageBytes2,fileName2,s3Service.getAmazonS3() );
        result2.setProductImageThumb(null);
        result2.setProductImageThumbContentType(null);      
        productService.save(result2);
        }
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, productDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /products : get all the products.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of products in body
     */
    @GetMapping("/products")
    @Timed
    public ResponseEntity<List<ProductDTO>> getAllProducts(Pageable pageable) {
        log.debug("REST request to get a page of Products");
        Pageable pageable2 =  PageRequest.of(pageable.getPageNumber(),2000);
        Page<ProductDTO> page = productService.findAll(pageable2);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/products");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /products/:id : get the "id" product.
     *
     * @param id the id of the productDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the productDTO, or with status 404 (Not Found)
     */
    @GetMapping("/products/{id}")
    @Timed
    public ResponseEntity<ProductDTO> getProduct(@PathVariable Long id) {
        log.debug("REST request to get Product : {}", id);
        Optional<ProductDTO> productDTO = productService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productDTO);
    }

    /**
     * DELETE  /products/:id : delete the "id" product.
     *
     * @param id the id of the productDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/products/{id}")
    @Timed
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.debug("REST request to delete Product : {}", id);
        Optional<ProductDTO> productDTO = productService.findOne(id);
        long shopId = productDTO.get().getShopId();
        String shopName = productDTO.get().getShopShopName();
        productService.delete(id);
        CommonUtils.saveShopChange(shopChangeService, shopId, "Product", "Existng Product deleted", shopName); 
        CommonUtils.deleteFromS3("Product" + id + ".png", s3Service.getAmazonS3());
        CommonUtils.deleteFromS3("ProductThumb" + id + ".png", s3Service.getAmazonS3());     
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/products?query=:query : search for the product corresponding
     * to the query.
     *
     * @param query the query of the product search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/products")
    @Timed
    public ResponseEntity<List<ProductDTO>> searchProducts(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Products for query {}", query);
        Page<ProductDTO> page = productService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/products");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    /**
     * GET  /products-by-product-category-id : get all the productsByProductCategoryId.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of products in body
     */
    @GetMapping("/products-by-product-category-id/{categoryId}")
    @Timed
    public ResponseEntity<List<ProductDTO>> getAllProductsByProductCategoryId(@PathVariable Long categoryId, Pageable pageable) {
        log.debug("REST request to get a page of getAllProductsByProductCategoryId");
        Page<ProductDTO> page = productService.findAllByProductCategoryId(pageable,categoryId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/products-by-product-category-id");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    /**
     * GET  /products : get all the products.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of products in body
     */
    @GetMapping("products-by-product-shop-id/{shopId}")
    @Timed
    public ResponseEntity<List<ProductDTOFull>> getAllProductsByShopId(Long shopId) {
        log.debug("REST request to get a page of getAllProductsByShopId");
        List<ProductDTOFull> productDTOLit = productService.findAllByShopId(shopId);
        return new ResponseEntity<>(productDTOLit, HttpStatus.OK); 
    }

}
