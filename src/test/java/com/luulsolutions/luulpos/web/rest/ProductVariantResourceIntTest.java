package com.luulsolutions.luulpos.web.rest;

import com.luulsolutions.luulpos.LuulposApp;

import com.luulsolutions.luulpos.domain.ProductVariant;
import com.luulsolutions.luulpos.domain.Product;
import com.luulsolutions.luulpos.repository.ProductVariantRepository;
import com.luulsolutions.luulpos.repository.search.ProductVariantSearchRepository;
import com.luulsolutions.luulpos.service.ProductVariantService;
import com.luulsolutions.luulpos.service.dto.ProductVariantDTO;
import com.luulsolutions.luulpos.service.mapper.ProductVariantMapper;
import com.luulsolutions.luulpos.web.rest.errors.ExceptionTranslator;
import com.luulsolutions.luulpos.service.dto.ProductVariantCriteria;
import com.luulsolutions.luulpos.service.ProductVariantQueryService;

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
import java.math.BigDecimal;
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
 * Test class for the ProductVariantResource REST controller.
 *
 * @see ProductVariantResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LuulposApp.class)
public class ProductVariantResourceIntTest {

    private static final String DEFAULT_VARIANT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_VARIANT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Float DEFAULT_PERCENTAGE = 1F;
    private static final Float UPDATED_PERCENTAGE = 2F;

    private static final byte[] DEFAULT_FULL_PHOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FULL_PHOTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FULL_PHOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FULL_PHOTO_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_FULL_PHOTO_URL = "AAAAAAAAAA";
    private static final String UPDATED_FULL_PHOTO_URL = "BBBBBBBBBB";

    private static final byte[] DEFAULT_THUMBNAIL_PHOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_THUMBNAIL_PHOTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_THUMBNAIL_PHOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_THUMBNAIL_PHOTO_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_THUMBNAIL_PHOTO_URL = "AAAAAAAAAA";
    private static final String UPDATED_THUMBNAIL_PHOTO_URL = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);

    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Autowired
    private ProductVariantMapper productVariantMapper;

    @Autowired
    private ProductVariantService productVariantService;

    /**
     * This repository is mocked in the com.luulsolutions.luulpos.repository.search test package.
     *
     * @see com.luulsolutions.luulpos.repository.search.ProductVariantSearchRepositoryMockConfiguration
     */
    @Autowired
    private ProductVariantSearchRepository mockProductVariantSearchRepository;

    @Autowired
    private ProductVariantQueryService productVariantQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProductVariantMockMvc;

    private ProductVariant productVariant;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProductVariantResource productVariantResource = new ProductVariantResource(productVariantService, productVariantQueryService);
        this.restProductVariantMockMvc = MockMvcBuilders.standaloneSetup(productVariantResource)
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
    public static ProductVariant createEntity(EntityManager em) {
        ProductVariant productVariant = new ProductVariant()
            .variantName(DEFAULT_VARIANT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .percentage(DEFAULT_PERCENTAGE)
            .fullPhoto(DEFAULT_FULL_PHOTO)
            .fullPhotoContentType(DEFAULT_FULL_PHOTO_CONTENT_TYPE)
            .fullPhotoUrl(DEFAULT_FULL_PHOTO_URL)
            .thumbnailPhoto(DEFAULT_THUMBNAIL_PHOTO)
            .thumbnailPhotoContentType(DEFAULT_THUMBNAIL_PHOTO_CONTENT_TYPE)
            .thumbnailPhotoUrl(DEFAULT_THUMBNAIL_PHOTO_URL)
            .price(DEFAULT_PRICE);
        return productVariant;
    }

    @Before
    public void initTest() {
        productVariant = createEntity(em);
    }

    @Test
    @Transactional
    public void createProductVariant() throws Exception {
        int databaseSizeBeforeCreate = productVariantRepository.findAll().size();

        // Create the ProductVariant
        ProductVariantDTO productVariantDTO = productVariantMapper.toDto(productVariant);
        restProductVariantMockMvc.perform(post("/api/product-variants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productVariantDTO)))
            .andExpect(status().isCreated());

        // Validate the ProductVariant in the database
        List<ProductVariant> productVariantList = productVariantRepository.findAll();
        assertThat(productVariantList).hasSize(databaseSizeBeforeCreate + 1);
        ProductVariant testProductVariant = productVariantList.get(productVariantList.size() - 1);
        assertThat(testProductVariant.getVariantName()).isEqualTo(DEFAULT_VARIANT_NAME);
        assertThat(testProductVariant.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProductVariant.getPercentage()).isEqualTo(DEFAULT_PERCENTAGE);
        assertThat(testProductVariant.getFullPhoto()).isEqualTo(DEFAULT_FULL_PHOTO);
        assertThat(testProductVariant.getFullPhotoContentType()).isEqualTo(DEFAULT_FULL_PHOTO_CONTENT_TYPE);
        assertThat(testProductVariant.getFullPhotoUrl()).isEqualTo(DEFAULT_FULL_PHOTO_URL);
        assertThat(testProductVariant.getThumbnailPhoto()).isEqualTo(DEFAULT_THUMBNAIL_PHOTO);
        assertThat(testProductVariant.getThumbnailPhotoContentType()).isEqualTo(DEFAULT_THUMBNAIL_PHOTO_CONTENT_TYPE);
        assertThat(testProductVariant.getThumbnailPhotoUrl()).isEqualTo(DEFAULT_THUMBNAIL_PHOTO_URL);
        assertThat(testProductVariant.getPrice()).isEqualTo(DEFAULT_PRICE);

        // Validate the ProductVariant in Elasticsearch
        verify(mockProductVariantSearchRepository, times(1)).save(testProductVariant);
    }

    @Test
    @Transactional
    public void createProductVariantWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = productVariantRepository.findAll().size();

        // Create the ProductVariant with an existing ID
        productVariant.setId(1L);
        ProductVariantDTO productVariantDTO = productVariantMapper.toDto(productVariant);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductVariantMockMvc.perform(post("/api/product-variants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productVariantDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProductVariant in the database
        List<ProductVariant> productVariantList = productVariantRepository.findAll();
        assertThat(productVariantList).hasSize(databaseSizeBeforeCreate);

        // Validate the ProductVariant in Elasticsearch
        verify(mockProductVariantSearchRepository, times(0)).save(productVariant);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = productVariantRepository.findAll().size();
        // set the field null
        productVariant.setPrice(null);

        // Create the ProductVariant, which fails.
        ProductVariantDTO productVariantDTO = productVariantMapper.toDto(productVariant);

        restProductVariantMockMvc.perform(post("/api/product-variants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productVariantDTO)))
            .andExpect(status().isBadRequest());

        List<ProductVariant> productVariantList = productVariantRepository.findAll();
        assertThat(productVariantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProductVariants() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList
        restProductVariantMockMvc.perform(get("/api/product-variants?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productVariant.getId().intValue())))
            .andExpect(jsonPath("$.[*].variantName").value(hasItem(DEFAULT_VARIANT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].percentage").value(hasItem(DEFAULT_PERCENTAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].fullPhotoContentType").value(hasItem(DEFAULT_FULL_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fullPhoto").value(hasItem(Base64Utils.encodeToString(DEFAULT_FULL_PHOTO))))
            .andExpect(jsonPath("$.[*].fullPhotoUrl").value(hasItem(DEFAULT_FULL_PHOTO_URL.toString())))
            .andExpect(jsonPath("$.[*].thumbnailPhotoContentType").value(hasItem(DEFAULT_THUMBNAIL_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].thumbnailPhoto").value(hasItem(Base64Utils.encodeToString(DEFAULT_THUMBNAIL_PHOTO))))
            .andExpect(jsonPath("$.[*].thumbnailPhotoUrl").value(hasItem(DEFAULT_THUMBNAIL_PHOTO_URL.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())));
    }
    
    @Test
    @Transactional
    public void getProductVariant() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get the productVariant
        restProductVariantMockMvc.perform(get("/api/product-variants/{id}", productVariant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(productVariant.getId().intValue()))
            .andExpect(jsonPath("$.variantName").value(DEFAULT_VARIANT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.percentage").value(DEFAULT_PERCENTAGE.doubleValue()))
            .andExpect(jsonPath("$.fullPhotoContentType").value(DEFAULT_FULL_PHOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.fullPhoto").value(Base64Utils.encodeToString(DEFAULT_FULL_PHOTO)))
            .andExpect(jsonPath("$.fullPhotoUrl").value(DEFAULT_FULL_PHOTO_URL.toString()))
            .andExpect(jsonPath("$.thumbnailPhotoContentType").value(DEFAULT_THUMBNAIL_PHOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.thumbnailPhoto").value(Base64Utils.encodeToString(DEFAULT_THUMBNAIL_PHOTO)))
            .andExpect(jsonPath("$.thumbnailPhotoUrl").value(DEFAULT_THUMBNAIL_PHOTO_URL.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()));
    }

    @Test
    @Transactional
    public void getAllProductVariantsByVariantNameIsEqualToSomething() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where variantName equals to DEFAULT_VARIANT_NAME
        defaultProductVariantShouldBeFound("variantName.equals=" + DEFAULT_VARIANT_NAME);

        // Get all the productVariantList where variantName equals to UPDATED_VARIANT_NAME
        defaultProductVariantShouldNotBeFound("variantName.equals=" + UPDATED_VARIANT_NAME);
    }

    @Test
    @Transactional
    public void getAllProductVariantsByVariantNameIsInShouldWork() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where variantName in DEFAULT_VARIANT_NAME or UPDATED_VARIANT_NAME
        defaultProductVariantShouldBeFound("variantName.in=" + DEFAULT_VARIANT_NAME + "," + UPDATED_VARIANT_NAME);

        // Get all the productVariantList where variantName equals to UPDATED_VARIANT_NAME
        defaultProductVariantShouldNotBeFound("variantName.in=" + UPDATED_VARIANT_NAME);
    }

    @Test
    @Transactional
    public void getAllProductVariantsByVariantNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where variantName is not null
        defaultProductVariantShouldBeFound("variantName.specified=true");

        // Get all the productVariantList where variantName is null
        defaultProductVariantShouldNotBeFound("variantName.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductVariantsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where description equals to DEFAULT_DESCRIPTION
        defaultProductVariantShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the productVariantList where description equals to UPDATED_DESCRIPTION
        defaultProductVariantShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllProductVariantsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultProductVariantShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the productVariantList where description equals to UPDATED_DESCRIPTION
        defaultProductVariantShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllProductVariantsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where description is not null
        defaultProductVariantShouldBeFound("description.specified=true");

        // Get all the productVariantList where description is null
        defaultProductVariantShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductVariantsByPercentageIsEqualToSomething() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where percentage equals to DEFAULT_PERCENTAGE
        defaultProductVariantShouldBeFound("percentage.equals=" + DEFAULT_PERCENTAGE);

        // Get all the productVariantList where percentage equals to UPDATED_PERCENTAGE
        defaultProductVariantShouldNotBeFound("percentage.equals=" + UPDATED_PERCENTAGE);
    }

    @Test
    @Transactional
    public void getAllProductVariantsByPercentageIsInShouldWork() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where percentage in DEFAULT_PERCENTAGE or UPDATED_PERCENTAGE
        defaultProductVariantShouldBeFound("percentage.in=" + DEFAULT_PERCENTAGE + "," + UPDATED_PERCENTAGE);

        // Get all the productVariantList where percentage equals to UPDATED_PERCENTAGE
        defaultProductVariantShouldNotBeFound("percentage.in=" + UPDATED_PERCENTAGE);
    }

    @Test
    @Transactional
    public void getAllProductVariantsByPercentageIsNullOrNotNull() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where percentage is not null
        defaultProductVariantShouldBeFound("percentage.specified=true");

        // Get all the productVariantList where percentage is null
        defaultProductVariantShouldNotBeFound("percentage.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductVariantsByFullPhotoUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where fullPhotoUrl equals to DEFAULT_FULL_PHOTO_URL
        defaultProductVariantShouldBeFound("fullPhotoUrl.equals=" + DEFAULT_FULL_PHOTO_URL);

        // Get all the productVariantList where fullPhotoUrl equals to UPDATED_FULL_PHOTO_URL
        defaultProductVariantShouldNotBeFound("fullPhotoUrl.equals=" + UPDATED_FULL_PHOTO_URL);
    }

    @Test
    @Transactional
    public void getAllProductVariantsByFullPhotoUrlIsInShouldWork() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where fullPhotoUrl in DEFAULT_FULL_PHOTO_URL or UPDATED_FULL_PHOTO_URL
        defaultProductVariantShouldBeFound("fullPhotoUrl.in=" + DEFAULT_FULL_PHOTO_URL + "," + UPDATED_FULL_PHOTO_URL);

        // Get all the productVariantList where fullPhotoUrl equals to UPDATED_FULL_PHOTO_URL
        defaultProductVariantShouldNotBeFound("fullPhotoUrl.in=" + UPDATED_FULL_PHOTO_URL);
    }

    @Test
    @Transactional
    public void getAllProductVariantsByFullPhotoUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where fullPhotoUrl is not null
        defaultProductVariantShouldBeFound("fullPhotoUrl.specified=true");

        // Get all the productVariantList where fullPhotoUrl is null
        defaultProductVariantShouldNotBeFound("fullPhotoUrl.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductVariantsByThumbnailPhotoUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where thumbnailPhotoUrl equals to DEFAULT_THUMBNAIL_PHOTO_URL
        defaultProductVariantShouldBeFound("thumbnailPhotoUrl.equals=" + DEFAULT_THUMBNAIL_PHOTO_URL);

        // Get all the productVariantList where thumbnailPhotoUrl equals to UPDATED_THUMBNAIL_PHOTO_URL
        defaultProductVariantShouldNotBeFound("thumbnailPhotoUrl.equals=" + UPDATED_THUMBNAIL_PHOTO_URL);
    }

    @Test
    @Transactional
    public void getAllProductVariantsByThumbnailPhotoUrlIsInShouldWork() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where thumbnailPhotoUrl in DEFAULT_THUMBNAIL_PHOTO_URL or UPDATED_THUMBNAIL_PHOTO_URL
        defaultProductVariantShouldBeFound("thumbnailPhotoUrl.in=" + DEFAULT_THUMBNAIL_PHOTO_URL + "," + UPDATED_THUMBNAIL_PHOTO_URL);

        // Get all the productVariantList where thumbnailPhotoUrl equals to UPDATED_THUMBNAIL_PHOTO_URL
        defaultProductVariantShouldNotBeFound("thumbnailPhotoUrl.in=" + UPDATED_THUMBNAIL_PHOTO_URL);
    }

    @Test
    @Transactional
    public void getAllProductVariantsByThumbnailPhotoUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where thumbnailPhotoUrl is not null
        defaultProductVariantShouldBeFound("thumbnailPhotoUrl.specified=true");

        // Get all the productVariantList where thumbnailPhotoUrl is null
        defaultProductVariantShouldNotBeFound("thumbnailPhotoUrl.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductVariantsByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where price equals to DEFAULT_PRICE
        defaultProductVariantShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the productVariantList where price equals to UPDATED_PRICE
        defaultProductVariantShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllProductVariantsByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultProductVariantShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the productVariantList where price equals to UPDATED_PRICE
        defaultProductVariantShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllProductVariantsByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where price is not null
        defaultProductVariantShouldBeFound("price.specified=true");

        // Get all the productVariantList where price is null
        defaultProductVariantShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductVariantsByProductIsEqualToSomething() throws Exception {
        // Initialize the database
        Product product = ProductResourceIntTest.createEntity(em);
        em.persist(product);
        em.flush();
        productVariant.setProduct(product);
        productVariantRepository.saveAndFlush(productVariant);
        Long productId = product.getId();

        // Get all the productVariantList where product equals to productId
        defaultProductVariantShouldBeFound("productId.equals=" + productId);

        // Get all the productVariantList where product equals to productId + 1
        defaultProductVariantShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultProductVariantShouldBeFound(String filter) throws Exception {
        restProductVariantMockMvc.perform(get("/api/product-variants?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productVariant.getId().intValue())))
            .andExpect(jsonPath("$.[*].variantName").value(hasItem(DEFAULT_VARIANT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].percentage").value(hasItem(DEFAULT_PERCENTAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].fullPhotoContentType").value(hasItem(DEFAULT_FULL_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fullPhoto").value(hasItem(Base64Utils.encodeToString(DEFAULT_FULL_PHOTO))))
            .andExpect(jsonPath("$.[*].fullPhotoUrl").value(hasItem(DEFAULT_FULL_PHOTO_URL.toString())))
            .andExpect(jsonPath("$.[*].thumbnailPhotoContentType").value(hasItem(DEFAULT_THUMBNAIL_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].thumbnailPhoto").value(hasItem(Base64Utils.encodeToString(DEFAULT_THUMBNAIL_PHOTO))))
            .andExpect(jsonPath("$.[*].thumbnailPhotoUrl").value(hasItem(DEFAULT_THUMBNAIL_PHOTO_URL.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())));

        // Check, that the count call also returns 1
        restProductVariantMockMvc.perform(get("/api/product-variants/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultProductVariantShouldNotBeFound(String filter) throws Exception {
        restProductVariantMockMvc.perform(get("/api/product-variants?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductVariantMockMvc.perform(get("/api/product-variants/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingProductVariant() throws Exception {
        // Get the productVariant
        restProductVariantMockMvc.perform(get("/api/product-variants/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProductVariant() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        int databaseSizeBeforeUpdate = productVariantRepository.findAll().size();

        // Update the productVariant
        ProductVariant updatedProductVariant = productVariantRepository.findById(productVariant.getId()).get();
        // Disconnect from session so that the updates on updatedProductVariant are not directly saved in db
        em.detach(updatedProductVariant);
        updatedProductVariant
            .variantName(UPDATED_VARIANT_NAME)
            .description(UPDATED_DESCRIPTION)
            .percentage(UPDATED_PERCENTAGE)
            .fullPhoto(UPDATED_FULL_PHOTO)
            .fullPhotoContentType(UPDATED_FULL_PHOTO_CONTENT_TYPE)
            .fullPhotoUrl(UPDATED_FULL_PHOTO_URL)
            .thumbnailPhoto(UPDATED_THUMBNAIL_PHOTO)
            .thumbnailPhotoContentType(UPDATED_THUMBNAIL_PHOTO_CONTENT_TYPE)
            .thumbnailPhotoUrl(UPDATED_THUMBNAIL_PHOTO_URL)
            .price(UPDATED_PRICE);
        ProductVariantDTO productVariantDTO = productVariantMapper.toDto(updatedProductVariant);

        restProductVariantMockMvc.perform(put("/api/product-variants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productVariantDTO)))
            .andExpect(status().isOk());

        // Validate the ProductVariant in the database
        List<ProductVariant> productVariantList = productVariantRepository.findAll();
        assertThat(productVariantList).hasSize(databaseSizeBeforeUpdate);
        ProductVariant testProductVariant = productVariantList.get(productVariantList.size() - 1);
        assertThat(testProductVariant.getVariantName()).isEqualTo(UPDATED_VARIANT_NAME);
        assertThat(testProductVariant.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProductVariant.getPercentage()).isEqualTo(UPDATED_PERCENTAGE);
        assertThat(testProductVariant.getFullPhoto()).isEqualTo(UPDATED_FULL_PHOTO);
        assertThat(testProductVariant.getFullPhotoContentType()).isEqualTo(UPDATED_FULL_PHOTO_CONTENT_TYPE);
        assertThat(testProductVariant.getFullPhotoUrl()).isEqualTo(UPDATED_FULL_PHOTO_URL);
        assertThat(testProductVariant.getThumbnailPhoto()).isEqualTo(UPDATED_THUMBNAIL_PHOTO);
        assertThat(testProductVariant.getThumbnailPhotoContentType()).isEqualTo(UPDATED_THUMBNAIL_PHOTO_CONTENT_TYPE);
        assertThat(testProductVariant.getThumbnailPhotoUrl()).isEqualTo(UPDATED_THUMBNAIL_PHOTO_URL);
        assertThat(testProductVariant.getPrice()).isEqualTo(UPDATED_PRICE);

        // Validate the ProductVariant in Elasticsearch
        verify(mockProductVariantSearchRepository, times(1)).save(testProductVariant);
    }

    @Test
    @Transactional
    public void updateNonExistingProductVariant() throws Exception {
        int databaseSizeBeforeUpdate = productVariantRepository.findAll().size();

        // Create the ProductVariant
        ProductVariantDTO productVariantDTO = productVariantMapper.toDto(productVariant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductVariantMockMvc.perform(put("/api/product-variants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productVariantDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProductVariant in the database
        List<ProductVariant> productVariantList = productVariantRepository.findAll();
        assertThat(productVariantList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ProductVariant in Elasticsearch
        verify(mockProductVariantSearchRepository, times(0)).save(productVariant);
    }

    @Test
    @Transactional
    public void deleteProductVariant() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        int databaseSizeBeforeDelete = productVariantRepository.findAll().size();

        // Get the productVariant
        restProductVariantMockMvc.perform(delete("/api/product-variants/{id}", productVariant.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ProductVariant> productVariantList = productVariantRepository.findAll();
        assertThat(productVariantList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ProductVariant in Elasticsearch
        verify(mockProductVariantSearchRepository, times(1)).deleteById(productVariant.getId());
    }

    @Test
    @Transactional
    public void searchProductVariant() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);
        when(mockProductVariantSearchRepository.search(queryStringQuery("id:" + productVariant.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(productVariant), PageRequest.of(0, 1), 1));
        // Search the productVariant
        restProductVariantMockMvc.perform(get("/api/_search/product-variants?query=id:" + productVariant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productVariant.getId().intValue())))
            .andExpect(jsonPath("$.[*].variantName").value(hasItem(DEFAULT_VARIANT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].percentage").value(hasItem(DEFAULT_PERCENTAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].fullPhotoContentType").value(hasItem(DEFAULT_FULL_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fullPhoto").value(hasItem(Base64Utils.encodeToString(DEFAULT_FULL_PHOTO))))
            .andExpect(jsonPath("$.[*].fullPhotoUrl").value(hasItem(DEFAULT_FULL_PHOTO_URL)))
            .andExpect(jsonPath("$.[*].thumbnailPhotoContentType").value(hasItem(DEFAULT_THUMBNAIL_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].thumbnailPhoto").value(hasItem(Base64Utils.encodeToString(DEFAULT_THUMBNAIL_PHOTO))))
            .andExpect(jsonPath("$.[*].thumbnailPhotoUrl").value(hasItem(DEFAULT_THUMBNAIL_PHOTO_URL)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductVariant.class);
        ProductVariant productVariant1 = new ProductVariant();
        productVariant1.setId(1L);
        ProductVariant productVariant2 = new ProductVariant();
        productVariant2.setId(productVariant1.getId());
        assertThat(productVariant1).isEqualTo(productVariant2);
        productVariant2.setId(2L);
        assertThat(productVariant1).isNotEqualTo(productVariant2);
        productVariant1.setId(null);
        assertThat(productVariant1).isNotEqualTo(productVariant2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductVariantDTO.class);
        ProductVariantDTO productVariantDTO1 = new ProductVariantDTO();
        productVariantDTO1.setId(1L);
        ProductVariantDTO productVariantDTO2 = new ProductVariantDTO();
        assertThat(productVariantDTO1).isNotEqualTo(productVariantDTO2);
        productVariantDTO2.setId(productVariantDTO1.getId());
        assertThat(productVariantDTO1).isEqualTo(productVariantDTO2);
        productVariantDTO2.setId(2L);
        assertThat(productVariantDTO1).isNotEqualTo(productVariantDTO2);
        productVariantDTO1.setId(null);
        assertThat(productVariantDTO1).isNotEqualTo(productVariantDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(productVariantMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(productVariantMapper.fromId(null)).isNull();
    }
}
