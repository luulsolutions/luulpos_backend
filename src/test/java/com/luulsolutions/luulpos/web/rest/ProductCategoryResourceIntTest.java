package com.luulsolutions.luulpos.web.rest;

import com.luulsolutions.luulpos.LuulposApp;

import com.luulsolutions.luulpos.domain.ProductCategory;
import com.luulsolutions.luulpos.domain.Shop;
import com.luulsolutions.luulpos.domain.Product;
import com.luulsolutions.luulpos.repository.ProductCategoryRepository;
import com.luulsolutions.luulpos.repository.search.ProductCategorySearchRepository;
import com.luulsolutions.luulpos.service.ProductCategoryService;
import com.luulsolutions.luulpos.service.dto.ProductCategoryDTO;
import com.luulsolutions.luulpos.service.mapper.ProductCategoryMapper;
import com.luulsolutions.luulpos.web.rest.errors.ExceptionTranslator;
import com.luulsolutions.luulpos.service.dto.ProductCategoryCriteria;
import com.luulsolutions.luulpos.service.ProductCategoryQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;


import static com.luulsolutions.luulpos.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ProductCategoryResource REST controller.
 *
 * @see ProductCategoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LuulposApp.class)
public class ProductCategoryResourceIntTest {

    private static final String DEFAULT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE_FULL = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE_FULL = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_FULL_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_FULL_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_IMAGE_FULL_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_FULL_URL = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE_THUMB = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE_THUMB = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_THUMB_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_THUMB_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_IMAGE_THUMB_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_THUMB_URL = "BBBBBBBBBB";

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    @Autowired
    private ProductCategoryService productCategoryService;

    /**
     * This repository is mocked in the com.luulsolutions.luulpos.repository.search test package.
     *
     * @see com.luulsolutions.luulpos.repository.search.ProductCategorySearchRepositoryMockConfiguration
     */
    @Autowired
    private ProductCategorySearchRepository mockProductCategorySearchRepository;

    @Autowired
    private ProductCategoryQueryService productCategoryQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProductCategoryMockMvc;

    private ProductCategory productCategory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProductCategoryResource productCategoryResource = new ProductCategoryResource(productCategoryService, productCategoryQueryService);
        this.restProductCategoryMockMvc = MockMvcBuilders.standaloneSetup(productCategoryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductCategory createEntity(EntityManager em) {
        ProductCategory productCategory = new ProductCategory()
            .category(DEFAULT_CATEGORY)
            .description(DEFAULT_DESCRIPTION)
            .imageFull(DEFAULT_IMAGE_FULL)
            .imageFullContentType(DEFAULT_IMAGE_FULL_CONTENT_TYPE)
            .imageFullUrl(DEFAULT_IMAGE_FULL_URL)
            .imageThumb(DEFAULT_IMAGE_THUMB)
            .imageThumbContentType(DEFAULT_IMAGE_THUMB_CONTENT_TYPE)
            .imageThumbUrl(DEFAULT_IMAGE_THUMB_URL);
        return productCategory;
    }

    @Before
    public void initTest() {
        productCategory = createEntity(em);
    }

    @Test
    @Transactional
    public void createProductCategory() throws Exception {
        int databaseSizeBeforeCreate = productCategoryRepository.findAll().size();

        // Create the ProductCategory
        ProductCategoryDTO productCategoryDTO = productCategoryMapper.toDto(productCategory);
        restProductCategoryMockMvc.perform(post("/api/product-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productCategoryDTO)))
            .andExpect(status().isCreated());

        // Validate the ProductCategory in the database
        List<ProductCategory> productCategoryList = productCategoryRepository.findAll();
        assertThat(productCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        ProductCategory testProductCategory = productCategoryList.get(productCategoryList.size() - 1);
        assertThat(testProductCategory.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testProductCategory.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProductCategory.getImageFull()).isEqualTo(DEFAULT_IMAGE_FULL);
        assertThat(testProductCategory.getImageFullContentType()).isEqualTo(DEFAULT_IMAGE_FULL_CONTENT_TYPE);
        assertThat(testProductCategory.getImageFullUrl()).isEqualTo(DEFAULT_IMAGE_FULL_URL);
        assertThat(testProductCategory.getImageThumb()).isEqualTo(DEFAULT_IMAGE_THUMB);
        assertThat(testProductCategory.getImageThumbContentType()).isEqualTo(DEFAULT_IMAGE_THUMB_CONTENT_TYPE);
        assertThat(testProductCategory.getImageThumbUrl()).isEqualTo(DEFAULT_IMAGE_THUMB_URL);

        // Validate the ProductCategory in Elasticsearch
        verify(mockProductCategorySearchRepository, times(1)).save(testProductCategory);
    }

    @Test
    @Transactional
    public void createProductCategoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = productCategoryRepository.findAll().size();

        // Create the ProductCategory with an existing ID
        productCategory.setId(1L);
        ProductCategoryDTO productCategoryDTO = productCategoryMapper.toDto(productCategory);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductCategoryMockMvc.perform(post("/api/product-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productCategoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProductCategory in the database
        List<ProductCategory> productCategoryList = productCategoryRepository.findAll();
        assertThat(productCategoryList).hasSize(databaseSizeBeforeCreate);

        // Validate the ProductCategory in Elasticsearch
        verify(mockProductCategorySearchRepository, times(0)).save(productCategory);
    }

    @Test
    @Transactional
    public void getAllProductCategories() throws Exception {
        // Initialize the database
        productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList
        restProductCategoryMockMvc.perform(get("/api/product-categories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].imageFullContentType").value(hasItem(DEFAULT_IMAGE_FULL_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imageFull").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE_FULL))))
            .andExpect(jsonPath("$.[*].imageFullUrl").value(hasItem(DEFAULT_IMAGE_FULL_URL.toString())))
            .andExpect(jsonPath("$.[*].imageThumbContentType").value(hasItem(DEFAULT_IMAGE_THUMB_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imageThumb").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE_THUMB))))
            .andExpect(jsonPath("$.[*].imageThumbUrl").value(hasItem(DEFAULT_IMAGE_THUMB_URL.toString())));
    }
    
    @Test
    @Transactional
    public void getProductCategory() throws Exception {
        // Initialize the database
        productCategoryRepository.saveAndFlush(productCategory);

        // Get the productCategory
        restProductCategoryMockMvc.perform(get("/api/product-categories/{id}", productCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(productCategory.getId().intValue()))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.imageFullContentType").value(DEFAULT_IMAGE_FULL_CONTENT_TYPE))
            .andExpect(jsonPath("$.imageFull").value(Base64Utils.encodeToString(DEFAULT_IMAGE_FULL)))
            .andExpect(jsonPath("$.imageFullUrl").value(DEFAULT_IMAGE_FULL_URL.toString()))
            .andExpect(jsonPath("$.imageThumbContentType").value(DEFAULT_IMAGE_THUMB_CONTENT_TYPE))
            .andExpect(jsonPath("$.imageThumb").value(Base64Utils.encodeToString(DEFAULT_IMAGE_THUMB)))
            .andExpect(jsonPath("$.imageThumbUrl").value(DEFAULT_IMAGE_THUMB_URL.toString()));
    }

    @Test
    @Transactional
    public void getAllProductCategoriesByCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where category equals to DEFAULT_CATEGORY
        defaultProductCategoryShouldBeFound("category.equals=" + DEFAULT_CATEGORY);

        // Get all the productCategoryList where category equals to UPDATED_CATEGORY
        defaultProductCategoryShouldNotBeFound("category.equals=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    public void getAllProductCategoriesByCategoryIsInShouldWork() throws Exception {
        // Initialize the database
        productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where category in DEFAULT_CATEGORY or UPDATED_CATEGORY
        defaultProductCategoryShouldBeFound("category.in=" + DEFAULT_CATEGORY + "," + UPDATED_CATEGORY);

        // Get all the productCategoryList where category equals to UPDATED_CATEGORY
        defaultProductCategoryShouldNotBeFound("category.in=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    public void getAllProductCategoriesByCategoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where category is not null
        defaultProductCategoryShouldBeFound("category.specified=true");

        // Get all the productCategoryList where category is null
        defaultProductCategoryShouldNotBeFound("category.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductCategoriesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where description equals to DEFAULT_DESCRIPTION
        defaultProductCategoryShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the productCategoryList where description equals to UPDATED_DESCRIPTION
        defaultProductCategoryShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllProductCategoriesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultProductCategoryShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the productCategoryList where description equals to UPDATED_DESCRIPTION
        defaultProductCategoryShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllProductCategoriesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where description is not null
        defaultProductCategoryShouldBeFound("description.specified=true");

        // Get all the productCategoryList where description is null
        defaultProductCategoryShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductCategoriesByImageFullUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where imageFullUrl equals to DEFAULT_IMAGE_FULL_URL
        defaultProductCategoryShouldBeFound("imageFullUrl.equals=" + DEFAULT_IMAGE_FULL_URL);

        // Get all the productCategoryList where imageFullUrl equals to UPDATED_IMAGE_FULL_URL
        defaultProductCategoryShouldNotBeFound("imageFullUrl.equals=" + UPDATED_IMAGE_FULL_URL);
    }

    @Test
    @Transactional
    public void getAllProductCategoriesByImageFullUrlIsInShouldWork() throws Exception {
        // Initialize the database
        productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where imageFullUrl in DEFAULT_IMAGE_FULL_URL or UPDATED_IMAGE_FULL_URL
        defaultProductCategoryShouldBeFound("imageFullUrl.in=" + DEFAULT_IMAGE_FULL_URL + "," + UPDATED_IMAGE_FULL_URL);

        // Get all the productCategoryList where imageFullUrl equals to UPDATED_IMAGE_FULL_URL
        defaultProductCategoryShouldNotBeFound("imageFullUrl.in=" + UPDATED_IMAGE_FULL_URL);
    }

    @Test
    @Transactional
    public void getAllProductCategoriesByImageFullUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where imageFullUrl is not null
        defaultProductCategoryShouldBeFound("imageFullUrl.specified=true");

        // Get all the productCategoryList where imageFullUrl is null
        defaultProductCategoryShouldNotBeFound("imageFullUrl.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductCategoriesByImageThumbUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where imageThumbUrl equals to DEFAULT_IMAGE_THUMB_URL
        defaultProductCategoryShouldBeFound("imageThumbUrl.equals=" + DEFAULT_IMAGE_THUMB_URL);

        // Get all the productCategoryList where imageThumbUrl equals to UPDATED_IMAGE_THUMB_URL
        defaultProductCategoryShouldNotBeFound("imageThumbUrl.equals=" + UPDATED_IMAGE_THUMB_URL);
    }

    @Test
    @Transactional
    public void getAllProductCategoriesByImageThumbUrlIsInShouldWork() throws Exception {
        // Initialize the database
        productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where imageThumbUrl in DEFAULT_IMAGE_THUMB_URL or UPDATED_IMAGE_THUMB_URL
        defaultProductCategoryShouldBeFound("imageThumbUrl.in=" + DEFAULT_IMAGE_THUMB_URL + "," + UPDATED_IMAGE_THUMB_URL);

        // Get all the productCategoryList where imageThumbUrl equals to UPDATED_IMAGE_THUMB_URL
        defaultProductCategoryShouldNotBeFound("imageThumbUrl.in=" + UPDATED_IMAGE_THUMB_URL);
    }

    @Test
    @Transactional
    public void getAllProductCategoriesByImageThumbUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where imageThumbUrl is not null
        defaultProductCategoryShouldBeFound("imageThumbUrl.specified=true");

        // Get all the productCategoryList where imageThumbUrl is null
        defaultProductCategoryShouldNotBeFound("imageThumbUrl.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductCategoriesByShopIsEqualToSomething() throws Exception {
        // Initialize the database
        Shop shop = ShopResourceIntTest.createEntity(em);
        em.persist(shop);
        em.flush();
        productCategory.setShop(shop);
        productCategoryRepository.saveAndFlush(productCategory);
        Long shopId = shop.getId();

        // Get all the productCategoryList where shop equals to shopId
        defaultProductCategoryShouldBeFound("shopId.equals=" + shopId);

        // Get all the productCategoryList where shop equals to shopId + 1
        defaultProductCategoryShouldNotBeFound("shopId.equals=" + (shopId + 1));
    }


    @Test
    @Transactional
    public void getAllProductCategoriesByProductsIsEqualToSomething() throws Exception {
        // Initialize the database
        Product products = ProductResourceIntTest.createEntity(em);
        em.persist(products);
        em.flush();
        productCategory.addProducts(products);
        productCategoryRepository.saveAndFlush(productCategory);
        Long productsId = products.getId();

        // Get all the productCategoryList where products equals to productsId
        defaultProductCategoryShouldBeFound("productsId.equals=" + productsId);

        // Get all the productCategoryList where products equals to productsId + 1
        defaultProductCategoryShouldNotBeFound("productsId.equals=" + (productsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultProductCategoryShouldBeFound(String filter) throws Exception {
        restProductCategoryMockMvc.perform(get("/api/product-categories?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].imageFullContentType").value(hasItem(DEFAULT_IMAGE_FULL_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imageFull").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE_FULL))))
            .andExpect(jsonPath("$.[*].imageFullUrl").value(hasItem(DEFAULT_IMAGE_FULL_URL.toString())))
            .andExpect(jsonPath("$.[*].imageThumbContentType").value(hasItem(DEFAULT_IMAGE_THUMB_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imageThumb").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE_THUMB))))
            .andExpect(jsonPath("$.[*].imageThumbUrl").value(hasItem(DEFAULT_IMAGE_THUMB_URL.toString())));

        // Check, that the count call also returns 1
        restProductCategoryMockMvc.perform(get("/api/product-categories/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultProductCategoryShouldNotBeFound(String filter) throws Exception {
        restProductCategoryMockMvc.perform(get("/api/product-categories?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductCategoryMockMvc.perform(get("/api/product-categories/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingProductCategory() throws Exception {
        // Get the productCategory
        restProductCategoryMockMvc.perform(get("/api/product-categories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProductCategory() throws Exception {
        // Initialize the database
        productCategoryRepository.saveAndFlush(productCategory);

        int databaseSizeBeforeUpdate = productCategoryRepository.findAll().size();

        // Update the productCategory
        ProductCategory updatedProductCategory = productCategoryRepository.findById(productCategory.getId()).get();
        // Disconnect from session so that the updates on updatedProductCategory are not directly saved in db
        em.detach(updatedProductCategory);
        updatedProductCategory
            .category(UPDATED_CATEGORY)
            .description(UPDATED_DESCRIPTION)
            .imageFull(UPDATED_IMAGE_FULL)
            .imageFullContentType(UPDATED_IMAGE_FULL_CONTENT_TYPE)
            .imageFullUrl(UPDATED_IMAGE_FULL_URL)
            .imageThumb(UPDATED_IMAGE_THUMB)
            .imageThumbContentType(UPDATED_IMAGE_THUMB_CONTENT_TYPE)
            .imageThumbUrl(UPDATED_IMAGE_THUMB_URL);
        ProductCategoryDTO productCategoryDTO = productCategoryMapper.toDto(updatedProductCategory);

        restProductCategoryMockMvc.perform(put("/api/product-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productCategoryDTO)))
            .andExpect(status().isOk());

        // Validate the ProductCategory in the database
        List<ProductCategory> productCategoryList = productCategoryRepository.findAll();
        assertThat(productCategoryList).hasSize(databaseSizeBeforeUpdate);
        ProductCategory testProductCategory = productCategoryList.get(productCategoryList.size() - 1);
        assertThat(testProductCategory.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testProductCategory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProductCategory.getImageFull()).isEqualTo(UPDATED_IMAGE_FULL);
        assertThat(testProductCategory.getImageFullContentType()).isEqualTo(UPDATED_IMAGE_FULL_CONTENT_TYPE);
        assertThat(testProductCategory.getImageFullUrl()).isEqualTo(UPDATED_IMAGE_FULL_URL);
        assertThat(testProductCategory.getImageThumb()).isEqualTo(UPDATED_IMAGE_THUMB);
        assertThat(testProductCategory.getImageThumbContentType()).isEqualTo(UPDATED_IMAGE_THUMB_CONTENT_TYPE);
        assertThat(testProductCategory.getImageThumbUrl()).isEqualTo(UPDATED_IMAGE_THUMB_URL);

        // Validate the ProductCategory in Elasticsearch
        verify(mockProductCategorySearchRepository, times(1)).save(testProductCategory);
    }

    @Test
    @Transactional
    public void updateNonExistingProductCategory() throws Exception {
        int databaseSizeBeforeUpdate = productCategoryRepository.findAll().size();

        // Create the ProductCategory
        ProductCategoryDTO productCategoryDTO = productCategoryMapper.toDto(productCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductCategoryMockMvc.perform(put("/api/product-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productCategoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProductCategory in the database
        List<ProductCategory> productCategoryList = productCategoryRepository.findAll();
        assertThat(productCategoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ProductCategory in Elasticsearch
        verify(mockProductCategorySearchRepository, times(0)).save(productCategory);
    }

    @Test
    @Transactional
    public void deleteProductCategory() throws Exception {
        // Initialize the database
        productCategoryRepository.saveAndFlush(productCategory);

        int databaseSizeBeforeDelete = productCategoryRepository.findAll().size();

        // Get the productCategory
        restProductCategoryMockMvc.perform(delete("/api/product-categories/{id}", productCategory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ProductCategory> productCategoryList = productCategoryRepository.findAll();
        assertThat(productCategoryList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ProductCategory in Elasticsearch
        verify(mockProductCategorySearchRepository, times(1)).deleteById(productCategory.getId());
    }

    @Test
    @Transactional
    public void searchProductCategory() throws Exception {
        // Initialize the database
        productCategoryRepository.saveAndFlush(productCategory);
        when(mockProductCategorySearchRepository.search(queryStringQuery("id:" + productCategory.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(productCategory), PageRequest.of(0, 1), 1));
        // Search the productCategory
        restProductCategoryMockMvc.perform(get("/api/_search/product-categories?query=id:" + productCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].imageFullContentType").value(hasItem(DEFAULT_IMAGE_FULL_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imageFull").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE_FULL))))
            .andExpect(jsonPath("$.[*].imageFullUrl").value(hasItem(DEFAULT_IMAGE_FULL_URL)))
            .andExpect(jsonPath("$.[*].imageThumbContentType").value(hasItem(DEFAULT_IMAGE_THUMB_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imageThumb").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE_THUMB))))
            .andExpect(jsonPath("$.[*].imageThumbUrl").value(hasItem(DEFAULT_IMAGE_THUMB_URL)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductCategory.class);
        ProductCategory productCategory1 = new ProductCategory();
        productCategory1.setId(1L);
        ProductCategory productCategory2 = new ProductCategory();
        productCategory2.setId(productCategory1.getId());
        assertThat(productCategory1).isEqualTo(productCategory2);
        productCategory2.setId(2L);
        assertThat(productCategory1).isNotEqualTo(productCategory2);
        productCategory1.setId(null);
        assertThat(productCategory1).isNotEqualTo(productCategory2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductCategoryDTO.class);
        ProductCategoryDTO productCategoryDTO1 = new ProductCategoryDTO();
        productCategoryDTO1.setId(1L);
        ProductCategoryDTO productCategoryDTO2 = new ProductCategoryDTO();
        assertThat(productCategoryDTO1).isNotEqualTo(productCategoryDTO2);
        productCategoryDTO2.setId(productCategoryDTO1.getId());
        assertThat(productCategoryDTO1).isEqualTo(productCategoryDTO2);
        productCategoryDTO2.setId(2L);
        assertThat(productCategoryDTO1).isNotEqualTo(productCategoryDTO2);
        productCategoryDTO1.setId(null);
        assertThat(productCategoryDTO1).isNotEqualTo(productCategoryDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(productCategoryMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(productCategoryMapper.fromId(null)).isNull();
    }
}
