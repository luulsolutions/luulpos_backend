package com.luulsolutions.luulpos.web.rest;

import com.luulsolutions.luulpos.LuulposApp;

import com.luulsolutions.luulpos.domain.ProductType;
import com.luulsolutions.luulpos.domain.Shop;
import com.luulsolutions.luulpos.repository.ProductTypeRepository;
import com.luulsolutions.luulpos.repository.search.ProductTypeSearchRepository;
import com.luulsolutions.luulpos.service.ProductTypeService;
import com.luulsolutions.luulpos.service.dto.ProductTypeDTO;
import com.luulsolutions.luulpos.service.mapper.ProductTypeMapper;
import com.luulsolutions.luulpos.web.rest.errors.ExceptionTranslator;
import com.luulsolutions.luulpos.service.dto.ProductTypeCriteria;
import com.luulsolutions.luulpos.service.ProductTypeQueryService;

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
 * Test class for the ProductTypeResource REST controller.
 *
 * @see ProductTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LuulposApp.class)
public class ProductTypeResourceIntTest {

    private static final String DEFAULT_PRODUCT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCT_TYPE_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_TYPE_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private ProductTypeRepository productTypeRepository;

    @Autowired
    private ProductTypeMapper productTypeMapper;

    @Autowired
    private ProductTypeService productTypeService;

    /**
     * This repository is mocked in the com.luulsolutions.luulpos.repository.search test package.
     *
     * @see com.luulsolutions.luulpos.repository.search.ProductTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private ProductTypeSearchRepository mockProductTypeSearchRepository;

    @Autowired
    private ProductTypeQueryService productTypeQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProductTypeMockMvc;

    private ProductType productType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProductTypeResource productTypeResource = new ProductTypeResource(productTypeService, productTypeQueryService);
        this.restProductTypeMockMvc = MockMvcBuilders.standaloneSetup(productTypeResource)
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
    public static ProductType createEntity(EntityManager em) {
        ProductType productType = new ProductType()
            .productType(DEFAULT_PRODUCT_TYPE)
            .productTypeDescription(DEFAULT_PRODUCT_TYPE_DESCRIPTION);
        return productType;
    }

    @Before
    public void initTest() {
        productType = createEntity(em);
    }

    @Test
    @Transactional
    public void createProductType() throws Exception {
        int databaseSizeBeforeCreate = productTypeRepository.findAll().size();

        // Create the ProductType
        ProductTypeDTO productTypeDTO = productTypeMapper.toDto(productType);
        restProductTypeMockMvc.perform(post("/api/product-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the ProductType in the database
        List<ProductType> productTypeList = productTypeRepository.findAll();
        assertThat(productTypeList).hasSize(databaseSizeBeforeCreate + 1);
        ProductType testProductType = productTypeList.get(productTypeList.size() - 1);
        assertThat(testProductType.getProductType()).isEqualTo(DEFAULT_PRODUCT_TYPE);
        assertThat(testProductType.getProductTypeDescription()).isEqualTo(DEFAULT_PRODUCT_TYPE_DESCRIPTION);

        // Validate the ProductType in Elasticsearch
        verify(mockProductTypeSearchRepository, times(1)).save(testProductType);
    }

    @Test
    @Transactional
    public void createProductTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = productTypeRepository.findAll().size();

        // Create the ProductType with an existing ID
        productType.setId(1L);
        ProductTypeDTO productTypeDTO = productTypeMapper.toDto(productType);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductTypeMockMvc.perform(post("/api/product-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProductType in the database
        List<ProductType> productTypeList = productTypeRepository.findAll();
        assertThat(productTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the ProductType in Elasticsearch
        verify(mockProductTypeSearchRepository, times(0)).save(productType);
    }

    @Test
    @Transactional
    public void checkProductTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = productTypeRepository.findAll().size();
        // set the field null
        productType.setProductType(null);

        // Create the ProductType, which fails.
        ProductTypeDTO productTypeDTO = productTypeMapper.toDto(productType);

        restProductTypeMockMvc.perform(post("/api/product-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productTypeDTO)))
            .andExpect(status().isBadRequest());

        List<ProductType> productTypeList = productTypeRepository.findAll();
        assertThat(productTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProductTypes() throws Exception {
        // Initialize the database
        productTypeRepository.saveAndFlush(productType);

        // Get all the productTypeList
        restProductTypeMockMvc.perform(get("/api/product-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productType.getId().intValue())))
            .andExpect(jsonPath("$.[*].productType").value(hasItem(DEFAULT_PRODUCT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].productTypeDescription").value(hasItem(DEFAULT_PRODUCT_TYPE_DESCRIPTION.toString())));
    }
    
    @Test
    @Transactional
    public void getProductType() throws Exception {
        // Initialize the database
        productTypeRepository.saveAndFlush(productType);

        // Get the productType
        restProductTypeMockMvc.perform(get("/api/product-types/{id}", productType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(productType.getId().intValue()))
            .andExpect(jsonPath("$.productType").value(DEFAULT_PRODUCT_TYPE.toString()))
            .andExpect(jsonPath("$.productTypeDescription").value(DEFAULT_PRODUCT_TYPE_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getAllProductTypesByProductTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        productTypeRepository.saveAndFlush(productType);

        // Get all the productTypeList where productType equals to DEFAULT_PRODUCT_TYPE
        defaultProductTypeShouldBeFound("productType.equals=" + DEFAULT_PRODUCT_TYPE);

        // Get all the productTypeList where productType equals to UPDATED_PRODUCT_TYPE
        defaultProductTypeShouldNotBeFound("productType.equals=" + UPDATED_PRODUCT_TYPE);
    }

    @Test
    @Transactional
    public void getAllProductTypesByProductTypeIsInShouldWork() throws Exception {
        // Initialize the database
        productTypeRepository.saveAndFlush(productType);

        // Get all the productTypeList where productType in DEFAULT_PRODUCT_TYPE or UPDATED_PRODUCT_TYPE
        defaultProductTypeShouldBeFound("productType.in=" + DEFAULT_PRODUCT_TYPE + "," + UPDATED_PRODUCT_TYPE);

        // Get all the productTypeList where productType equals to UPDATED_PRODUCT_TYPE
        defaultProductTypeShouldNotBeFound("productType.in=" + UPDATED_PRODUCT_TYPE);
    }

    @Test
    @Transactional
    public void getAllProductTypesByProductTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        productTypeRepository.saveAndFlush(productType);

        // Get all the productTypeList where productType is not null
        defaultProductTypeShouldBeFound("productType.specified=true");

        // Get all the productTypeList where productType is null
        defaultProductTypeShouldNotBeFound("productType.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductTypesByProductTypeDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        productTypeRepository.saveAndFlush(productType);

        // Get all the productTypeList where productTypeDescription equals to DEFAULT_PRODUCT_TYPE_DESCRIPTION
        defaultProductTypeShouldBeFound("productTypeDescription.equals=" + DEFAULT_PRODUCT_TYPE_DESCRIPTION);

        // Get all the productTypeList where productTypeDescription equals to UPDATED_PRODUCT_TYPE_DESCRIPTION
        defaultProductTypeShouldNotBeFound("productTypeDescription.equals=" + UPDATED_PRODUCT_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllProductTypesByProductTypeDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        productTypeRepository.saveAndFlush(productType);

        // Get all the productTypeList where productTypeDescription in DEFAULT_PRODUCT_TYPE_DESCRIPTION or UPDATED_PRODUCT_TYPE_DESCRIPTION
        defaultProductTypeShouldBeFound("productTypeDescription.in=" + DEFAULT_PRODUCT_TYPE_DESCRIPTION + "," + UPDATED_PRODUCT_TYPE_DESCRIPTION);

        // Get all the productTypeList where productTypeDescription equals to UPDATED_PRODUCT_TYPE_DESCRIPTION
        defaultProductTypeShouldNotBeFound("productTypeDescription.in=" + UPDATED_PRODUCT_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllProductTypesByProductTypeDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        productTypeRepository.saveAndFlush(productType);

        // Get all the productTypeList where productTypeDescription is not null
        defaultProductTypeShouldBeFound("productTypeDescription.specified=true");

        // Get all the productTypeList where productTypeDescription is null
        defaultProductTypeShouldNotBeFound("productTypeDescription.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductTypesByShopIsEqualToSomething() throws Exception {
        // Initialize the database
        Shop shop = ShopResourceIntTest.createEntity(em);
        em.persist(shop);
        em.flush();
        productType.setShop(shop);
        productTypeRepository.saveAndFlush(productType);
        Long shopId = shop.getId();

        // Get all the productTypeList where shop equals to shopId
        defaultProductTypeShouldBeFound("shopId.equals=" + shopId);

        // Get all the productTypeList where shop equals to shopId + 1
        defaultProductTypeShouldNotBeFound("shopId.equals=" + (shopId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultProductTypeShouldBeFound(String filter) throws Exception {
        restProductTypeMockMvc.perform(get("/api/product-types?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productType.getId().intValue())))
            .andExpect(jsonPath("$.[*].productType").value(hasItem(DEFAULT_PRODUCT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].productTypeDescription").value(hasItem(DEFAULT_PRODUCT_TYPE_DESCRIPTION.toString())));

        // Check, that the count call also returns 1
        restProductTypeMockMvc.perform(get("/api/product-types/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultProductTypeShouldNotBeFound(String filter) throws Exception {
        restProductTypeMockMvc.perform(get("/api/product-types?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductTypeMockMvc.perform(get("/api/product-types/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingProductType() throws Exception {
        // Get the productType
        restProductTypeMockMvc.perform(get("/api/product-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProductType() throws Exception {
        // Initialize the database
        productTypeRepository.saveAndFlush(productType);

        int databaseSizeBeforeUpdate = productTypeRepository.findAll().size();

        // Update the productType
        ProductType updatedProductType = productTypeRepository.findById(productType.getId()).get();
        // Disconnect from session so that the updates on updatedProductType are not directly saved in db
        em.detach(updatedProductType);
        updatedProductType
            .productType(UPDATED_PRODUCT_TYPE)
            .productTypeDescription(UPDATED_PRODUCT_TYPE_DESCRIPTION);
        ProductTypeDTO productTypeDTO = productTypeMapper.toDto(updatedProductType);

        restProductTypeMockMvc.perform(put("/api/product-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productTypeDTO)))
            .andExpect(status().isOk());

        // Validate the ProductType in the database
        List<ProductType> productTypeList = productTypeRepository.findAll();
        assertThat(productTypeList).hasSize(databaseSizeBeforeUpdate);
        ProductType testProductType = productTypeList.get(productTypeList.size() - 1);
        assertThat(testProductType.getProductType()).isEqualTo(UPDATED_PRODUCT_TYPE);
        assertThat(testProductType.getProductTypeDescription()).isEqualTo(UPDATED_PRODUCT_TYPE_DESCRIPTION);

        // Validate the ProductType in Elasticsearch
        verify(mockProductTypeSearchRepository, times(1)).save(testProductType);
    }

    @Test
    @Transactional
    public void updateNonExistingProductType() throws Exception {
        int databaseSizeBeforeUpdate = productTypeRepository.findAll().size();

        // Create the ProductType
        ProductTypeDTO productTypeDTO = productTypeMapper.toDto(productType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductTypeMockMvc.perform(put("/api/product-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProductType in the database
        List<ProductType> productTypeList = productTypeRepository.findAll();
        assertThat(productTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ProductType in Elasticsearch
        verify(mockProductTypeSearchRepository, times(0)).save(productType);
    }

    @Test
    @Transactional
    public void deleteProductType() throws Exception {
        // Initialize the database
        productTypeRepository.saveAndFlush(productType);

        int databaseSizeBeforeDelete = productTypeRepository.findAll().size();

        // Get the productType
        restProductTypeMockMvc.perform(delete("/api/product-types/{id}", productType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ProductType> productTypeList = productTypeRepository.findAll();
        assertThat(productTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ProductType in Elasticsearch
        verify(mockProductTypeSearchRepository, times(1)).deleteById(productType.getId());
    }

    @Test
    @Transactional
    public void searchProductType() throws Exception {
        // Initialize the database
        productTypeRepository.saveAndFlush(productType);
        when(mockProductTypeSearchRepository.search(queryStringQuery("id:" + productType.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(productType), PageRequest.of(0, 1), 1));
        // Search the productType
        restProductTypeMockMvc.perform(get("/api/_search/product-types?query=id:" + productType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productType.getId().intValue())))
            .andExpect(jsonPath("$.[*].productType").value(hasItem(DEFAULT_PRODUCT_TYPE)))
            .andExpect(jsonPath("$.[*].productTypeDescription").value(hasItem(DEFAULT_PRODUCT_TYPE_DESCRIPTION)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductType.class);
        ProductType productType1 = new ProductType();
        productType1.setId(1L);
        ProductType productType2 = new ProductType();
        productType2.setId(productType1.getId());
        assertThat(productType1).isEqualTo(productType2);
        productType2.setId(2L);
        assertThat(productType1).isNotEqualTo(productType2);
        productType1.setId(null);
        assertThat(productType1).isNotEqualTo(productType2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductTypeDTO.class);
        ProductTypeDTO productTypeDTO1 = new ProductTypeDTO();
        productTypeDTO1.setId(1L);
        ProductTypeDTO productTypeDTO2 = new ProductTypeDTO();
        assertThat(productTypeDTO1).isNotEqualTo(productTypeDTO2);
        productTypeDTO2.setId(productTypeDTO1.getId());
        assertThat(productTypeDTO1).isEqualTo(productTypeDTO2);
        productTypeDTO2.setId(2L);
        assertThat(productTypeDTO1).isNotEqualTo(productTypeDTO2);
        productTypeDTO1.setId(null);
        assertThat(productTypeDTO1).isNotEqualTo(productTypeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(productTypeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(productTypeMapper.fromId(null)).isNull();
    }
}
