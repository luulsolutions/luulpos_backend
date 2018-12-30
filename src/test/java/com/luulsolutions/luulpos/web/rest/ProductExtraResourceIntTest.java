package com.luulsolutions.luulpos.web.rest;

import com.luulsolutions.luulpos.LuulposApp;

import com.luulsolutions.luulpos.domain.ProductExtra;
import com.luulsolutions.luulpos.domain.Product;
import com.luulsolutions.luulpos.repository.ProductExtraRepository;
import com.luulsolutions.luulpos.repository.search.ProductExtraSearchRepository;
import com.luulsolutions.luulpos.service.ProductExtraService;
import com.luulsolutions.luulpos.service.dto.ProductExtraDTO;
import com.luulsolutions.luulpos.service.mapper.ProductExtraMapper;
import com.luulsolutions.luulpos.web.rest.errors.ExceptionTranslator;
import com.luulsolutions.luulpos.service.dto.ProductExtraCriteria;
import com.luulsolutions.luulpos.service.ProductExtraQueryService;

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
 * Test class for the ProductExtraResource REST controller.
 *
 * @see ProductExtraResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LuulposApp.class)
public class ProductExtraResourceIntTest {

    private static final String DEFAULT_EXTRA_NAME = "AAAAAAAAAA";
    private static final String UPDATED_EXTRA_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Float DEFAULT_EXTRA_VALUE = 1F;
    private static final Float UPDATED_EXTRA_VALUE = 2F;

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

    @Autowired
    private ProductExtraRepository productExtraRepository;

    @Autowired
    private ProductExtraMapper productExtraMapper;

    @Autowired
    private ProductExtraService productExtraService;

    /**
     * This repository is mocked in the com.luulsolutions.luulpos.repository.search test package.
     *
     * @see com.luulsolutions.luulpos.repository.search.ProductExtraSearchRepositoryMockConfiguration
     */
    @Autowired
    private ProductExtraSearchRepository mockProductExtraSearchRepository;

    @Autowired
    private ProductExtraQueryService productExtraQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProductExtraMockMvc;

    private ProductExtra productExtra;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProductExtraResource productExtraResource = new ProductExtraResource(productExtraService, productExtraQueryService);
        this.restProductExtraMockMvc = MockMvcBuilders.standaloneSetup(productExtraResource)
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
    public static ProductExtra createEntity(EntityManager em) {
        ProductExtra productExtra = new ProductExtra()
            .extraName(DEFAULT_EXTRA_NAME)
            .description(DEFAULT_DESCRIPTION)
            .extraValue(DEFAULT_EXTRA_VALUE)
            .fullPhoto(DEFAULT_FULL_PHOTO)
            .fullPhotoContentType(DEFAULT_FULL_PHOTO_CONTENT_TYPE)
            .fullPhotoUrl(DEFAULT_FULL_PHOTO_URL)
            .thumbnailPhoto(DEFAULT_THUMBNAIL_PHOTO)
            .thumbnailPhotoContentType(DEFAULT_THUMBNAIL_PHOTO_CONTENT_TYPE)
            .thumbnailPhotoUrl(DEFAULT_THUMBNAIL_PHOTO_URL);
        return productExtra;
    }

    @Before
    public void initTest() {
        productExtra = createEntity(em);
    }

    @Test
    @Transactional
    public void createProductExtra() throws Exception {
        int databaseSizeBeforeCreate = productExtraRepository.findAll().size();

        // Create the ProductExtra
        ProductExtraDTO productExtraDTO = productExtraMapper.toDto(productExtra);
        restProductExtraMockMvc.perform(post("/api/product-extras")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productExtraDTO)))
            .andExpect(status().isCreated());

        // Validate the ProductExtra in the database
        List<ProductExtra> productExtraList = productExtraRepository.findAll();
        assertThat(productExtraList).hasSize(databaseSizeBeforeCreate + 1);
        ProductExtra testProductExtra = productExtraList.get(productExtraList.size() - 1);
        assertThat(testProductExtra.getExtraName()).isEqualTo(DEFAULT_EXTRA_NAME);
        assertThat(testProductExtra.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProductExtra.getExtraValue()).isEqualTo(DEFAULT_EXTRA_VALUE);
        assertThat(testProductExtra.getFullPhoto()).isEqualTo(DEFAULT_FULL_PHOTO);
        assertThat(testProductExtra.getFullPhotoContentType()).isEqualTo(DEFAULT_FULL_PHOTO_CONTENT_TYPE);
        assertThat(testProductExtra.getFullPhotoUrl()).isEqualTo(DEFAULT_FULL_PHOTO_URL);
        assertThat(testProductExtra.getThumbnailPhoto()).isEqualTo(DEFAULT_THUMBNAIL_PHOTO);
        assertThat(testProductExtra.getThumbnailPhotoContentType()).isEqualTo(DEFAULT_THUMBNAIL_PHOTO_CONTENT_TYPE);
        assertThat(testProductExtra.getThumbnailPhotoUrl()).isEqualTo(DEFAULT_THUMBNAIL_PHOTO_URL);

        // Validate the ProductExtra in Elasticsearch
        verify(mockProductExtraSearchRepository, times(1)).save(testProductExtra);
    }

    @Test
    @Transactional
    public void createProductExtraWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = productExtraRepository.findAll().size();

        // Create the ProductExtra with an existing ID
        productExtra.setId(1L);
        ProductExtraDTO productExtraDTO = productExtraMapper.toDto(productExtra);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductExtraMockMvc.perform(post("/api/product-extras")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productExtraDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProductExtra in the database
        List<ProductExtra> productExtraList = productExtraRepository.findAll();
        assertThat(productExtraList).hasSize(databaseSizeBeforeCreate);

        // Validate the ProductExtra in Elasticsearch
        verify(mockProductExtraSearchRepository, times(0)).save(productExtra);
    }

    @Test
    @Transactional
    public void getAllProductExtras() throws Exception {
        // Initialize the database
        productExtraRepository.saveAndFlush(productExtra);

        // Get all the productExtraList
        restProductExtraMockMvc.perform(get("/api/product-extras?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productExtra.getId().intValue())))
            .andExpect(jsonPath("$.[*].extraName").value(hasItem(DEFAULT_EXTRA_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].extraValue").value(hasItem(DEFAULT_EXTRA_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].fullPhotoContentType").value(hasItem(DEFAULT_FULL_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fullPhoto").value(hasItem(Base64Utils.encodeToString(DEFAULT_FULL_PHOTO))))
            .andExpect(jsonPath("$.[*].fullPhotoUrl").value(hasItem(DEFAULT_FULL_PHOTO_URL.toString())))
            .andExpect(jsonPath("$.[*].thumbnailPhotoContentType").value(hasItem(DEFAULT_THUMBNAIL_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].thumbnailPhoto").value(hasItem(Base64Utils.encodeToString(DEFAULT_THUMBNAIL_PHOTO))))
            .andExpect(jsonPath("$.[*].thumbnailPhotoUrl").value(hasItem(DEFAULT_THUMBNAIL_PHOTO_URL.toString())));
    }
    
    @Test
    @Transactional
    public void getProductExtra() throws Exception {
        // Initialize the database
        productExtraRepository.saveAndFlush(productExtra);

        // Get the productExtra
        restProductExtraMockMvc.perform(get("/api/product-extras/{id}", productExtra.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(productExtra.getId().intValue()))
            .andExpect(jsonPath("$.extraName").value(DEFAULT_EXTRA_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.extraValue").value(DEFAULT_EXTRA_VALUE.doubleValue()))
            .andExpect(jsonPath("$.fullPhotoContentType").value(DEFAULT_FULL_PHOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.fullPhoto").value(Base64Utils.encodeToString(DEFAULT_FULL_PHOTO)))
            .andExpect(jsonPath("$.fullPhotoUrl").value(DEFAULT_FULL_PHOTO_URL.toString()))
            .andExpect(jsonPath("$.thumbnailPhotoContentType").value(DEFAULT_THUMBNAIL_PHOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.thumbnailPhoto").value(Base64Utils.encodeToString(DEFAULT_THUMBNAIL_PHOTO)))
            .andExpect(jsonPath("$.thumbnailPhotoUrl").value(DEFAULT_THUMBNAIL_PHOTO_URL.toString()));
    }

    @Test
    @Transactional
    public void getAllProductExtrasByExtraNameIsEqualToSomething() throws Exception {
        // Initialize the database
        productExtraRepository.saveAndFlush(productExtra);

        // Get all the productExtraList where extraName equals to DEFAULT_EXTRA_NAME
        defaultProductExtraShouldBeFound("extraName.equals=" + DEFAULT_EXTRA_NAME);

        // Get all the productExtraList where extraName equals to UPDATED_EXTRA_NAME
        defaultProductExtraShouldNotBeFound("extraName.equals=" + UPDATED_EXTRA_NAME);
    }

    @Test
    @Transactional
    public void getAllProductExtrasByExtraNameIsInShouldWork() throws Exception {
        // Initialize the database
        productExtraRepository.saveAndFlush(productExtra);

        // Get all the productExtraList where extraName in DEFAULT_EXTRA_NAME or UPDATED_EXTRA_NAME
        defaultProductExtraShouldBeFound("extraName.in=" + DEFAULT_EXTRA_NAME + "," + UPDATED_EXTRA_NAME);

        // Get all the productExtraList where extraName equals to UPDATED_EXTRA_NAME
        defaultProductExtraShouldNotBeFound("extraName.in=" + UPDATED_EXTRA_NAME);
    }

    @Test
    @Transactional
    public void getAllProductExtrasByExtraNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        productExtraRepository.saveAndFlush(productExtra);

        // Get all the productExtraList where extraName is not null
        defaultProductExtraShouldBeFound("extraName.specified=true");

        // Get all the productExtraList where extraName is null
        defaultProductExtraShouldNotBeFound("extraName.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductExtrasByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        productExtraRepository.saveAndFlush(productExtra);

        // Get all the productExtraList where description equals to DEFAULT_DESCRIPTION
        defaultProductExtraShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the productExtraList where description equals to UPDATED_DESCRIPTION
        defaultProductExtraShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllProductExtrasByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        productExtraRepository.saveAndFlush(productExtra);

        // Get all the productExtraList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultProductExtraShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the productExtraList where description equals to UPDATED_DESCRIPTION
        defaultProductExtraShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllProductExtrasByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        productExtraRepository.saveAndFlush(productExtra);

        // Get all the productExtraList where description is not null
        defaultProductExtraShouldBeFound("description.specified=true");

        // Get all the productExtraList where description is null
        defaultProductExtraShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductExtrasByExtraValueIsEqualToSomething() throws Exception {
        // Initialize the database
        productExtraRepository.saveAndFlush(productExtra);

        // Get all the productExtraList where extraValue equals to DEFAULT_EXTRA_VALUE
        defaultProductExtraShouldBeFound("extraValue.equals=" + DEFAULT_EXTRA_VALUE);

        // Get all the productExtraList where extraValue equals to UPDATED_EXTRA_VALUE
        defaultProductExtraShouldNotBeFound("extraValue.equals=" + UPDATED_EXTRA_VALUE);
    }

    @Test
    @Transactional
    public void getAllProductExtrasByExtraValueIsInShouldWork() throws Exception {
        // Initialize the database
        productExtraRepository.saveAndFlush(productExtra);

        // Get all the productExtraList where extraValue in DEFAULT_EXTRA_VALUE or UPDATED_EXTRA_VALUE
        defaultProductExtraShouldBeFound("extraValue.in=" + DEFAULT_EXTRA_VALUE + "," + UPDATED_EXTRA_VALUE);

        // Get all the productExtraList where extraValue equals to UPDATED_EXTRA_VALUE
        defaultProductExtraShouldNotBeFound("extraValue.in=" + UPDATED_EXTRA_VALUE);
    }

    @Test
    @Transactional
    public void getAllProductExtrasByExtraValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        productExtraRepository.saveAndFlush(productExtra);

        // Get all the productExtraList where extraValue is not null
        defaultProductExtraShouldBeFound("extraValue.specified=true");

        // Get all the productExtraList where extraValue is null
        defaultProductExtraShouldNotBeFound("extraValue.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductExtrasByFullPhotoUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        productExtraRepository.saveAndFlush(productExtra);

        // Get all the productExtraList where fullPhotoUrl equals to DEFAULT_FULL_PHOTO_URL
        defaultProductExtraShouldBeFound("fullPhotoUrl.equals=" + DEFAULT_FULL_PHOTO_URL);

        // Get all the productExtraList where fullPhotoUrl equals to UPDATED_FULL_PHOTO_URL
        defaultProductExtraShouldNotBeFound("fullPhotoUrl.equals=" + UPDATED_FULL_PHOTO_URL);
    }

    @Test
    @Transactional
    public void getAllProductExtrasByFullPhotoUrlIsInShouldWork() throws Exception {
        // Initialize the database
        productExtraRepository.saveAndFlush(productExtra);

        // Get all the productExtraList where fullPhotoUrl in DEFAULT_FULL_PHOTO_URL or UPDATED_FULL_PHOTO_URL
        defaultProductExtraShouldBeFound("fullPhotoUrl.in=" + DEFAULT_FULL_PHOTO_URL + "," + UPDATED_FULL_PHOTO_URL);

        // Get all the productExtraList where fullPhotoUrl equals to UPDATED_FULL_PHOTO_URL
        defaultProductExtraShouldNotBeFound("fullPhotoUrl.in=" + UPDATED_FULL_PHOTO_URL);
    }

    @Test
    @Transactional
    public void getAllProductExtrasByFullPhotoUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        productExtraRepository.saveAndFlush(productExtra);

        // Get all the productExtraList where fullPhotoUrl is not null
        defaultProductExtraShouldBeFound("fullPhotoUrl.specified=true");

        // Get all the productExtraList where fullPhotoUrl is null
        defaultProductExtraShouldNotBeFound("fullPhotoUrl.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductExtrasByThumbnailPhotoUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        productExtraRepository.saveAndFlush(productExtra);

        // Get all the productExtraList where thumbnailPhotoUrl equals to DEFAULT_THUMBNAIL_PHOTO_URL
        defaultProductExtraShouldBeFound("thumbnailPhotoUrl.equals=" + DEFAULT_THUMBNAIL_PHOTO_URL);

        // Get all the productExtraList where thumbnailPhotoUrl equals to UPDATED_THUMBNAIL_PHOTO_URL
        defaultProductExtraShouldNotBeFound("thumbnailPhotoUrl.equals=" + UPDATED_THUMBNAIL_PHOTO_URL);
    }

    @Test
    @Transactional
    public void getAllProductExtrasByThumbnailPhotoUrlIsInShouldWork() throws Exception {
        // Initialize the database
        productExtraRepository.saveAndFlush(productExtra);

        // Get all the productExtraList where thumbnailPhotoUrl in DEFAULT_THUMBNAIL_PHOTO_URL or UPDATED_THUMBNAIL_PHOTO_URL
        defaultProductExtraShouldBeFound("thumbnailPhotoUrl.in=" + DEFAULT_THUMBNAIL_PHOTO_URL + "," + UPDATED_THUMBNAIL_PHOTO_URL);

        // Get all the productExtraList where thumbnailPhotoUrl equals to UPDATED_THUMBNAIL_PHOTO_URL
        defaultProductExtraShouldNotBeFound("thumbnailPhotoUrl.in=" + UPDATED_THUMBNAIL_PHOTO_URL);
    }

    @Test
    @Transactional
    public void getAllProductExtrasByThumbnailPhotoUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        productExtraRepository.saveAndFlush(productExtra);

        // Get all the productExtraList where thumbnailPhotoUrl is not null
        defaultProductExtraShouldBeFound("thumbnailPhotoUrl.specified=true");

        // Get all the productExtraList where thumbnailPhotoUrl is null
        defaultProductExtraShouldNotBeFound("thumbnailPhotoUrl.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductExtrasByProductIsEqualToSomething() throws Exception {
        // Initialize the database
        Product product = ProductResourceIntTest.createEntity(em);
        em.persist(product);
        em.flush();
        productExtra.setProduct(product);
        productExtraRepository.saveAndFlush(productExtra);
        Long productId = product.getId();

        // Get all the productExtraList where product equals to productId
        defaultProductExtraShouldBeFound("productId.equals=" + productId);

        // Get all the productExtraList where product equals to productId + 1
        defaultProductExtraShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultProductExtraShouldBeFound(String filter) throws Exception {
        restProductExtraMockMvc.perform(get("/api/product-extras?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productExtra.getId().intValue())))
            .andExpect(jsonPath("$.[*].extraName").value(hasItem(DEFAULT_EXTRA_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].extraValue").value(hasItem(DEFAULT_EXTRA_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].fullPhotoContentType").value(hasItem(DEFAULT_FULL_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fullPhoto").value(hasItem(Base64Utils.encodeToString(DEFAULT_FULL_PHOTO))))
            .andExpect(jsonPath("$.[*].fullPhotoUrl").value(hasItem(DEFAULT_FULL_PHOTO_URL.toString())))
            .andExpect(jsonPath("$.[*].thumbnailPhotoContentType").value(hasItem(DEFAULT_THUMBNAIL_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].thumbnailPhoto").value(hasItem(Base64Utils.encodeToString(DEFAULT_THUMBNAIL_PHOTO))))
            .andExpect(jsonPath("$.[*].thumbnailPhotoUrl").value(hasItem(DEFAULT_THUMBNAIL_PHOTO_URL.toString())));

        // Check, that the count call also returns 1
        restProductExtraMockMvc.perform(get("/api/product-extras/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultProductExtraShouldNotBeFound(String filter) throws Exception {
        restProductExtraMockMvc.perform(get("/api/product-extras?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductExtraMockMvc.perform(get("/api/product-extras/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingProductExtra() throws Exception {
        // Get the productExtra
        restProductExtraMockMvc.perform(get("/api/product-extras/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProductExtra() throws Exception {
        // Initialize the database
        productExtraRepository.saveAndFlush(productExtra);

        int databaseSizeBeforeUpdate = productExtraRepository.findAll().size();

        // Update the productExtra
        ProductExtra updatedProductExtra = productExtraRepository.findById(productExtra.getId()).get();
        // Disconnect from session so that the updates on updatedProductExtra are not directly saved in db
        em.detach(updatedProductExtra);
        updatedProductExtra
            .extraName(UPDATED_EXTRA_NAME)
            .description(UPDATED_DESCRIPTION)
            .extraValue(UPDATED_EXTRA_VALUE)
            .fullPhoto(UPDATED_FULL_PHOTO)
            .fullPhotoContentType(UPDATED_FULL_PHOTO_CONTENT_TYPE)
            .fullPhotoUrl(UPDATED_FULL_PHOTO_URL)
            .thumbnailPhoto(UPDATED_THUMBNAIL_PHOTO)
            .thumbnailPhotoContentType(UPDATED_THUMBNAIL_PHOTO_CONTENT_TYPE)
            .thumbnailPhotoUrl(UPDATED_THUMBNAIL_PHOTO_URL);
        ProductExtraDTO productExtraDTO = productExtraMapper.toDto(updatedProductExtra);

        restProductExtraMockMvc.perform(put("/api/product-extras")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productExtraDTO)))
            .andExpect(status().isOk());

        // Validate the ProductExtra in the database
        List<ProductExtra> productExtraList = productExtraRepository.findAll();
        assertThat(productExtraList).hasSize(databaseSizeBeforeUpdate);
        ProductExtra testProductExtra = productExtraList.get(productExtraList.size() - 1);
        assertThat(testProductExtra.getExtraName()).isEqualTo(UPDATED_EXTRA_NAME);
        assertThat(testProductExtra.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProductExtra.getExtraValue()).isEqualTo(UPDATED_EXTRA_VALUE);
        assertThat(testProductExtra.getFullPhoto()).isEqualTo(UPDATED_FULL_PHOTO);
        assertThat(testProductExtra.getFullPhotoContentType()).isEqualTo(UPDATED_FULL_PHOTO_CONTENT_TYPE);
        assertThat(testProductExtra.getFullPhotoUrl()).isEqualTo(UPDATED_FULL_PHOTO_URL);
        assertThat(testProductExtra.getThumbnailPhoto()).isEqualTo(UPDATED_THUMBNAIL_PHOTO);
        assertThat(testProductExtra.getThumbnailPhotoContentType()).isEqualTo(UPDATED_THUMBNAIL_PHOTO_CONTENT_TYPE);
        assertThat(testProductExtra.getThumbnailPhotoUrl()).isEqualTo(UPDATED_THUMBNAIL_PHOTO_URL);

        // Validate the ProductExtra in Elasticsearch
        verify(mockProductExtraSearchRepository, times(1)).save(testProductExtra);
    }

    @Test
    @Transactional
    public void updateNonExistingProductExtra() throws Exception {
        int databaseSizeBeforeUpdate = productExtraRepository.findAll().size();

        // Create the ProductExtra
        ProductExtraDTO productExtraDTO = productExtraMapper.toDto(productExtra);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductExtraMockMvc.perform(put("/api/product-extras")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productExtraDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProductExtra in the database
        List<ProductExtra> productExtraList = productExtraRepository.findAll();
        assertThat(productExtraList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ProductExtra in Elasticsearch
        verify(mockProductExtraSearchRepository, times(0)).save(productExtra);
    }

    @Test
    @Transactional
    public void deleteProductExtra() throws Exception {
        // Initialize the database
        productExtraRepository.saveAndFlush(productExtra);

        int databaseSizeBeforeDelete = productExtraRepository.findAll().size();

        // Get the productExtra
        restProductExtraMockMvc.perform(delete("/api/product-extras/{id}", productExtra.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ProductExtra> productExtraList = productExtraRepository.findAll();
        assertThat(productExtraList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ProductExtra in Elasticsearch
        verify(mockProductExtraSearchRepository, times(1)).deleteById(productExtra.getId());
    }

    @Test
    @Transactional
    public void searchProductExtra() throws Exception {
        // Initialize the database
        productExtraRepository.saveAndFlush(productExtra);
        when(mockProductExtraSearchRepository.search(queryStringQuery("id:" + productExtra.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(productExtra), PageRequest.of(0, 1), 1));
        // Search the productExtra
        restProductExtraMockMvc.perform(get("/api/_search/product-extras?query=id:" + productExtra.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productExtra.getId().intValue())))
            .andExpect(jsonPath("$.[*].extraName").value(hasItem(DEFAULT_EXTRA_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].extraValue").value(hasItem(DEFAULT_EXTRA_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].fullPhotoContentType").value(hasItem(DEFAULT_FULL_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fullPhoto").value(hasItem(Base64Utils.encodeToString(DEFAULT_FULL_PHOTO))))
            .andExpect(jsonPath("$.[*].fullPhotoUrl").value(hasItem(DEFAULT_FULL_PHOTO_URL)))
            .andExpect(jsonPath("$.[*].thumbnailPhotoContentType").value(hasItem(DEFAULT_THUMBNAIL_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].thumbnailPhoto").value(hasItem(Base64Utils.encodeToString(DEFAULT_THUMBNAIL_PHOTO))))
            .andExpect(jsonPath("$.[*].thumbnailPhotoUrl").value(hasItem(DEFAULT_THUMBNAIL_PHOTO_URL)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductExtra.class);
        ProductExtra productExtra1 = new ProductExtra();
        productExtra1.setId(1L);
        ProductExtra productExtra2 = new ProductExtra();
        productExtra2.setId(productExtra1.getId());
        assertThat(productExtra1).isEqualTo(productExtra2);
        productExtra2.setId(2L);
        assertThat(productExtra1).isNotEqualTo(productExtra2);
        productExtra1.setId(null);
        assertThat(productExtra1).isNotEqualTo(productExtra2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductExtraDTO.class);
        ProductExtraDTO productExtraDTO1 = new ProductExtraDTO();
        productExtraDTO1.setId(1L);
        ProductExtraDTO productExtraDTO2 = new ProductExtraDTO();
        assertThat(productExtraDTO1).isNotEqualTo(productExtraDTO2);
        productExtraDTO2.setId(productExtraDTO1.getId());
        assertThat(productExtraDTO1).isEqualTo(productExtraDTO2);
        productExtraDTO2.setId(2L);
        assertThat(productExtraDTO1).isNotEqualTo(productExtraDTO2);
        productExtraDTO1.setId(null);
        assertThat(productExtraDTO1).isNotEqualTo(productExtraDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(productExtraMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(productExtraMapper.fromId(null)).isNull();
    }
}
