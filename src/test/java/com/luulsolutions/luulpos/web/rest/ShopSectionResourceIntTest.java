package com.luulsolutions.luulpos.web.rest;

import com.luulsolutions.luulpos.LuulposApp;

import com.luulsolutions.luulpos.domain.ShopSection;
import com.luulsolutions.luulpos.domain.Shop;
import com.luulsolutions.luulpos.repository.ShopSectionRepository;
import com.luulsolutions.luulpos.repository.search.ShopSectionSearchRepository;
import com.luulsolutions.luulpos.service.ShopSectionService;
import com.luulsolutions.luulpos.service.dto.ShopSectionDTO;
import com.luulsolutions.luulpos.service.mapper.ShopSectionMapper;
import com.luulsolutions.luulpos.web.rest.errors.ExceptionTranslator;
import com.luulsolutions.luulpos.service.dto.ShopSectionCriteria;
import com.luulsolutions.luulpos.service.ShopSectionQueryService;

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
 * Test class for the ShopSectionResource REST controller.
 *
 * @see ShopSectionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LuulposApp.class)
public class ShopSectionResourceIntTest {

    private static final String DEFAULT_SECTION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SECTION_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_SURCHARGE_PERCENTAGE = 1;
    private static final Integer UPDATED_SURCHARGE_PERCENTAGE = 2;

    private static final BigDecimal DEFAULT_SURCHARGE_FLAT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_SURCHARGE_FLAT_AMOUNT = new BigDecimal(2);

    private static final Boolean DEFAULT_USE_PERCENTAGE = false;
    private static final Boolean UPDATED_USE_PERCENTAGE = true;

    @Autowired
    private ShopSectionRepository shopSectionRepository;

    @Autowired
    private ShopSectionMapper shopSectionMapper;

    @Autowired
    private ShopSectionService shopSectionService;

    /**
     * This repository is mocked in the com.luulsolutions.luulpos.repository.search test package.
     *
     * @see com.luulsolutions.luulpos.repository.search.ShopSectionSearchRepositoryMockConfiguration
     */
    @Autowired
    private ShopSectionSearchRepository mockShopSectionSearchRepository;

    @Autowired
    private ShopSectionQueryService shopSectionQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restShopSectionMockMvc;

    private ShopSection shopSection;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ShopSectionResource shopSectionResource = new ShopSectionResource(shopSectionService, shopSectionQueryService);
        this.restShopSectionMockMvc = MockMvcBuilders.standaloneSetup(shopSectionResource)
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
    public static ShopSection createEntity(EntityManager em) {
        ShopSection shopSection = new ShopSection()
            .sectionName(DEFAULT_SECTION_NAME)
            .description(DEFAULT_DESCRIPTION)
            .surchargePercentage(DEFAULT_SURCHARGE_PERCENTAGE)
            .surchargeFlatAmount(DEFAULT_SURCHARGE_FLAT_AMOUNT)
            .usePercentage(DEFAULT_USE_PERCENTAGE);
        return shopSection;
    }

    @Before
    public void initTest() {
        shopSection = createEntity(em);
    }

    @Test
    @Transactional
    public void createShopSection() throws Exception {
        int databaseSizeBeforeCreate = shopSectionRepository.findAll().size();

        // Create the ShopSection
        ShopSectionDTO shopSectionDTO = shopSectionMapper.toDto(shopSection);
        restShopSectionMockMvc.perform(post("/api/shop-sections")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shopSectionDTO)))
            .andExpect(status().isCreated());

        // Validate the ShopSection in the database
        List<ShopSection> shopSectionList = shopSectionRepository.findAll();
        assertThat(shopSectionList).hasSize(databaseSizeBeforeCreate + 1);
        ShopSection testShopSection = shopSectionList.get(shopSectionList.size() - 1);
        assertThat(testShopSection.getSectionName()).isEqualTo(DEFAULT_SECTION_NAME);
        assertThat(testShopSection.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testShopSection.getSurchargePercentage()).isEqualTo(DEFAULT_SURCHARGE_PERCENTAGE);
        assertThat(testShopSection.getSurchargeFlatAmount()).isEqualTo(DEFAULT_SURCHARGE_FLAT_AMOUNT);
        assertThat(testShopSection.isUsePercentage()).isEqualTo(DEFAULT_USE_PERCENTAGE);

        // Validate the ShopSection in Elasticsearch
        verify(mockShopSectionSearchRepository, times(1)).save(testShopSection);
    }

    @Test
    @Transactional
    public void createShopSectionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = shopSectionRepository.findAll().size();

        // Create the ShopSection with an existing ID
        shopSection.setId(1L);
        ShopSectionDTO shopSectionDTO = shopSectionMapper.toDto(shopSection);

        // An entity with an existing ID cannot be created, so this API call must fail
        restShopSectionMockMvc.perform(post("/api/shop-sections")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shopSectionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ShopSection in the database
        List<ShopSection> shopSectionList = shopSectionRepository.findAll();
        assertThat(shopSectionList).hasSize(databaseSizeBeforeCreate);

        // Validate the ShopSection in Elasticsearch
        verify(mockShopSectionSearchRepository, times(0)).save(shopSection);
    }

    @Test
    @Transactional
    public void getAllShopSections() throws Exception {
        // Initialize the database
        shopSectionRepository.saveAndFlush(shopSection);

        // Get all the shopSectionList
        restShopSectionMockMvc.perform(get("/api/shop-sections?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shopSection.getId().intValue())))
            .andExpect(jsonPath("$.[*].sectionName").value(hasItem(DEFAULT_SECTION_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].surchargePercentage").value(hasItem(DEFAULT_SURCHARGE_PERCENTAGE)))
            .andExpect(jsonPath("$.[*].surchargeFlatAmount").value(hasItem(DEFAULT_SURCHARGE_FLAT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].usePercentage").value(hasItem(DEFAULT_USE_PERCENTAGE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getShopSection() throws Exception {
        // Initialize the database
        shopSectionRepository.saveAndFlush(shopSection);

        // Get the shopSection
        restShopSectionMockMvc.perform(get("/api/shop-sections/{id}", shopSection.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(shopSection.getId().intValue()))
            .andExpect(jsonPath("$.sectionName").value(DEFAULT_SECTION_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.surchargePercentage").value(DEFAULT_SURCHARGE_PERCENTAGE))
            .andExpect(jsonPath("$.surchargeFlatAmount").value(DEFAULT_SURCHARGE_FLAT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.usePercentage").value(DEFAULT_USE_PERCENTAGE.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllShopSectionsBySectionNameIsEqualToSomething() throws Exception {
        // Initialize the database
        shopSectionRepository.saveAndFlush(shopSection);

        // Get all the shopSectionList where sectionName equals to DEFAULT_SECTION_NAME
        defaultShopSectionShouldBeFound("sectionName.equals=" + DEFAULT_SECTION_NAME);

        // Get all the shopSectionList where sectionName equals to UPDATED_SECTION_NAME
        defaultShopSectionShouldNotBeFound("sectionName.equals=" + UPDATED_SECTION_NAME);
    }

    @Test
    @Transactional
    public void getAllShopSectionsBySectionNameIsInShouldWork() throws Exception {
        // Initialize the database
        shopSectionRepository.saveAndFlush(shopSection);

        // Get all the shopSectionList where sectionName in DEFAULT_SECTION_NAME or UPDATED_SECTION_NAME
        defaultShopSectionShouldBeFound("sectionName.in=" + DEFAULT_SECTION_NAME + "," + UPDATED_SECTION_NAME);

        // Get all the shopSectionList where sectionName equals to UPDATED_SECTION_NAME
        defaultShopSectionShouldNotBeFound("sectionName.in=" + UPDATED_SECTION_NAME);
    }

    @Test
    @Transactional
    public void getAllShopSectionsBySectionNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        shopSectionRepository.saveAndFlush(shopSection);

        // Get all the shopSectionList where sectionName is not null
        defaultShopSectionShouldBeFound("sectionName.specified=true");

        // Get all the shopSectionList where sectionName is null
        defaultShopSectionShouldNotBeFound("sectionName.specified=false");
    }

    @Test
    @Transactional
    public void getAllShopSectionsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        shopSectionRepository.saveAndFlush(shopSection);

        // Get all the shopSectionList where description equals to DEFAULT_DESCRIPTION
        defaultShopSectionShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the shopSectionList where description equals to UPDATED_DESCRIPTION
        defaultShopSectionShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllShopSectionsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        shopSectionRepository.saveAndFlush(shopSection);

        // Get all the shopSectionList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultShopSectionShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the shopSectionList where description equals to UPDATED_DESCRIPTION
        defaultShopSectionShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllShopSectionsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        shopSectionRepository.saveAndFlush(shopSection);

        // Get all the shopSectionList where description is not null
        defaultShopSectionShouldBeFound("description.specified=true");

        // Get all the shopSectionList where description is null
        defaultShopSectionShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllShopSectionsBySurchargePercentageIsEqualToSomething() throws Exception {
        // Initialize the database
        shopSectionRepository.saveAndFlush(shopSection);

        // Get all the shopSectionList where surchargePercentage equals to DEFAULT_SURCHARGE_PERCENTAGE
        defaultShopSectionShouldBeFound("surchargePercentage.equals=" + DEFAULT_SURCHARGE_PERCENTAGE);

        // Get all the shopSectionList where surchargePercentage equals to UPDATED_SURCHARGE_PERCENTAGE
        defaultShopSectionShouldNotBeFound("surchargePercentage.equals=" + UPDATED_SURCHARGE_PERCENTAGE);
    }

    @Test
    @Transactional
    public void getAllShopSectionsBySurchargePercentageIsInShouldWork() throws Exception {
        // Initialize the database
        shopSectionRepository.saveAndFlush(shopSection);

        // Get all the shopSectionList where surchargePercentage in DEFAULT_SURCHARGE_PERCENTAGE or UPDATED_SURCHARGE_PERCENTAGE
        defaultShopSectionShouldBeFound("surchargePercentage.in=" + DEFAULT_SURCHARGE_PERCENTAGE + "," + UPDATED_SURCHARGE_PERCENTAGE);

        // Get all the shopSectionList where surchargePercentage equals to UPDATED_SURCHARGE_PERCENTAGE
        defaultShopSectionShouldNotBeFound("surchargePercentage.in=" + UPDATED_SURCHARGE_PERCENTAGE);
    }

    @Test
    @Transactional
    public void getAllShopSectionsBySurchargePercentageIsNullOrNotNull() throws Exception {
        // Initialize the database
        shopSectionRepository.saveAndFlush(shopSection);

        // Get all the shopSectionList where surchargePercentage is not null
        defaultShopSectionShouldBeFound("surchargePercentage.specified=true");

        // Get all the shopSectionList where surchargePercentage is null
        defaultShopSectionShouldNotBeFound("surchargePercentage.specified=false");
    }

    @Test
    @Transactional
    public void getAllShopSectionsBySurchargePercentageIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        shopSectionRepository.saveAndFlush(shopSection);

        // Get all the shopSectionList where surchargePercentage greater than or equals to DEFAULT_SURCHARGE_PERCENTAGE
        defaultShopSectionShouldBeFound("surchargePercentage.greaterOrEqualThan=" + DEFAULT_SURCHARGE_PERCENTAGE);

        // Get all the shopSectionList where surchargePercentage greater than or equals to UPDATED_SURCHARGE_PERCENTAGE
        defaultShopSectionShouldNotBeFound("surchargePercentage.greaterOrEqualThan=" + UPDATED_SURCHARGE_PERCENTAGE);
    }

    @Test
    @Transactional
    public void getAllShopSectionsBySurchargePercentageIsLessThanSomething() throws Exception {
        // Initialize the database
        shopSectionRepository.saveAndFlush(shopSection);

        // Get all the shopSectionList where surchargePercentage less than or equals to DEFAULT_SURCHARGE_PERCENTAGE
        defaultShopSectionShouldNotBeFound("surchargePercentage.lessThan=" + DEFAULT_SURCHARGE_PERCENTAGE);

        // Get all the shopSectionList where surchargePercentage less than or equals to UPDATED_SURCHARGE_PERCENTAGE
        defaultShopSectionShouldBeFound("surchargePercentage.lessThan=" + UPDATED_SURCHARGE_PERCENTAGE);
    }


    @Test
    @Transactional
    public void getAllShopSectionsBySurchargeFlatAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        shopSectionRepository.saveAndFlush(shopSection);

        // Get all the shopSectionList where surchargeFlatAmount equals to DEFAULT_SURCHARGE_FLAT_AMOUNT
        defaultShopSectionShouldBeFound("surchargeFlatAmount.equals=" + DEFAULT_SURCHARGE_FLAT_AMOUNT);

        // Get all the shopSectionList where surchargeFlatAmount equals to UPDATED_SURCHARGE_FLAT_AMOUNT
        defaultShopSectionShouldNotBeFound("surchargeFlatAmount.equals=" + UPDATED_SURCHARGE_FLAT_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllShopSectionsBySurchargeFlatAmountIsInShouldWork() throws Exception {
        // Initialize the database
        shopSectionRepository.saveAndFlush(shopSection);

        // Get all the shopSectionList where surchargeFlatAmount in DEFAULT_SURCHARGE_FLAT_AMOUNT or UPDATED_SURCHARGE_FLAT_AMOUNT
        defaultShopSectionShouldBeFound("surchargeFlatAmount.in=" + DEFAULT_SURCHARGE_FLAT_AMOUNT + "," + UPDATED_SURCHARGE_FLAT_AMOUNT);

        // Get all the shopSectionList where surchargeFlatAmount equals to UPDATED_SURCHARGE_FLAT_AMOUNT
        defaultShopSectionShouldNotBeFound("surchargeFlatAmount.in=" + UPDATED_SURCHARGE_FLAT_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllShopSectionsBySurchargeFlatAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        shopSectionRepository.saveAndFlush(shopSection);

        // Get all the shopSectionList where surchargeFlatAmount is not null
        defaultShopSectionShouldBeFound("surchargeFlatAmount.specified=true");

        // Get all the shopSectionList where surchargeFlatAmount is null
        defaultShopSectionShouldNotBeFound("surchargeFlatAmount.specified=false");
    }

    @Test
    @Transactional
    public void getAllShopSectionsByUsePercentageIsEqualToSomething() throws Exception {
        // Initialize the database
        shopSectionRepository.saveAndFlush(shopSection);

        // Get all the shopSectionList where usePercentage equals to DEFAULT_USE_PERCENTAGE
        defaultShopSectionShouldBeFound("usePercentage.equals=" + DEFAULT_USE_PERCENTAGE);

        // Get all the shopSectionList where usePercentage equals to UPDATED_USE_PERCENTAGE
        defaultShopSectionShouldNotBeFound("usePercentage.equals=" + UPDATED_USE_PERCENTAGE);
    }

    @Test
    @Transactional
    public void getAllShopSectionsByUsePercentageIsInShouldWork() throws Exception {
        // Initialize the database
        shopSectionRepository.saveAndFlush(shopSection);

        // Get all the shopSectionList where usePercentage in DEFAULT_USE_PERCENTAGE or UPDATED_USE_PERCENTAGE
        defaultShopSectionShouldBeFound("usePercentage.in=" + DEFAULT_USE_PERCENTAGE + "," + UPDATED_USE_PERCENTAGE);

        // Get all the shopSectionList where usePercentage equals to UPDATED_USE_PERCENTAGE
        defaultShopSectionShouldNotBeFound("usePercentage.in=" + UPDATED_USE_PERCENTAGE);
    }

    @Test
    @Transactional
    public void getAllShopSectionsByUsePercentageIsNullOrNotNull() throws Exception {
        // Initialize the database
        shopSectionRepository.saveAndFlush(shopSection);

        // Get all the shopSectionList where usePercentage is not null
        defaultShopSectionShouldBeFound("usePercentage.specified=true");

        // Get all the shopSectionList where usePercentage is null
        defaultShopSectionShouldNotBeFound("usePercentage.specified=false");
    }

    @Test
    @Transactional
    public void getAllShopSectionsByShopIsEqualToSomething() throws Exception {
        // Initialize the database
        Shop shop = ShopResourceIntTest.createEntity(em);
        em.persist(shop);
        em.flush();
        shopSection.setShop(shop);
        shopSectionRepository.saveAndFlush(shopSection);
        Long shopId = shop.getId();

        // Get all the shopSectionList where shop equals to shopId
        defaultShopSectionShouldBeFound("shopId.equals=" + shopId);

        // Get all the shopSectionList where shop equals to shopId + 1
        defaultShopSectionShouldNotBeFound("shopId.equals=" + (shopId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultShopSectionShouldBeFound(String filter) throws Exception {
        restShopSectionMockMvc.perform(get("/api/shop-sections?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shopSection.getId().intValue())))
            .andExpect(jsonPath("$.[*].sectionName").value(hasItem(DEFAULT_SECTION_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].surchargePercentage").value(hasItem(DEFAULT_SURCHARGE_PERCENTAGE)))
            .andExpect(jsonPath("$.[*].surchargeFlatAmount").value(hasItem(DEFAULT_SURCHARGE_FLAT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].usePercentage").value(hasItem(DEFAULT_USE_PERCENTAGE.booleanValue())));

        // Check, that the count call also returns 1
        restShopSectionMockMvc.perform(get("/api/shop-sections/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultShopSectionShouldNotBeFound(String filter) throws Exception {
        restShopSectionMockMvc.perform(get("/api/shop-sections?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restShopSectionMockMvc.perform(get("/api/shop-sections/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingShopSection() throws Exception {
        // Get the shopSection
        restShopSectionMockMvc.perform(get("/api/shop-sections/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateShopSection() throws Exception {
        // Initialize the database
        shopSectionRepository.saveAndFlush(shopSection);

        int databaseSizeBeforeUpdate = shopSectionRepository.findAll().size();

        // Update the shopSection
        ShopSection updatedShopSection = shopSectionRepository.findById(shopSection.getId()).get();
        // Disconnect from session so that the updates on updatedShopSection are not directly saved in db
        em.detach(updatedShopSection);
        updatedShopSection
            .sectionName(UPDATED_SECTION_NAME)
            .description(UPDATED_DESCRIPTION)
            .surchargePercentage(UPDATED_SURCHARGE_PERCENTAGE)
            .surchargeFlatAmount(UPDATED_SURCHARGE_FLAT_AMOUNT)
            .usePercentage(UPDATED_USE_PERCENTAGE);
        ShopSectionDTO shopSectionDTO = shopSectionMapper.toDto(updatedShopSection);

        restShopSectionMockMvc.perform(put("/api/shop-sections")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shopSectionDTO)))
            .andExpect(status().isOk());

        // Validate the ShopSection in the database
        List<ShopSection> shopSectionList = shopSectionRepository.findAll();
        assertThat(shopSectionList).hasSize(databaseSizeBeforeUpdate);
        ShopSection testShopSection = shopSectionList.get(shopSectionList.size() - 1);
        assertThat(testShopSection.getSectionName()).isEqualTo(UPDATED_SECTION_NAME);
        assertThat(testShopSection.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testShopSection.getSurchargePercentage()).isEqualTo(UPDATED_SURCHARGE_PERCENTAGE);
        assertThat(testShopSection.getSurchargeFlatAmount()).isEqualTo(UPDATED_SURCHARGE_FLAT_AMOUNT);
        assertThat(testShopSection.isUsePercentage()).isEqualTo(UPDATED_USE_PERCENTAGE);

        // Validate the ShopSection in Elasticsearch
        verify(mockShopSectionSearchRepository, times(1)).save(testShopSection);
    }

    @Test
    @Transactional
    public void updateNonExistingShopSection() throws Exception {
        int databaseSizeBeforeUpdate = shopSectionRepository.findAll().size();

        // Create the ShopSection
        ShopSectionDTO shopSectionDTO = shopSectionMapper.toDto(shopSection);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShopSectionMockMvc.perform(put("/api/shop-sections")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shopSectionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ShopSection in the database
        List<ShopSection> shopSectionList = shopSectionRepository.findAll();
        assertThat(shopSectionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ShopSection in Elasticsearch
        verify(mockShopSectionSearchRepository, times(0)).save(shopSection);
    }

    @Test
    @Transactional
    public void deleteShopSection() throws Exception {
        // Initialize the database
        shopSectionRepository.saveAndFlush(shopSection);

        int databaseSizeBeforeDelete = shopSectionRepository.findAll().size();

        // Get the shopSection
        restShopSectionMockMvc.perform(delete("/api/shop-sections/{id}", shopSection.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ShopSection> shopSectionList = shopSectionRepository.findAll();
        assertThat(shopSectionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ShopSection in Elasticsearch
        verify(mockShopSectionSearchRepository, times(1)).deleteById(shopSection.getId());
    }

    @Test
    @Transactional
    public void searchShopSection() throws Exception {
        // Initialize the database
        shopSectionRepository.saveAndFlush(shopSection);
        when(mockShopSectionSearchRepository.search(queryStringQuery("id:" + shopSection.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(shopSection), PageRequest.of(0, 1), 1));
        // Search the shopSection
        restShopSectionMockMvc.perform(get("/api/_search/shop-sections?query=id:" + shopSection.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shopSection.getId().intValue())))
            .andExpect(jsonPath("$.[*].sectionName").value(hasItem(DEFAULT_SECTION_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].surchargePercentage").value(hasItem(DEFAULT_SURCHARGE_PERCENTAGE)))
            .andExpect(jsonPath("$.[*].surchargeFlatAmount").value(hasItem(DEFAULT_SURCHARGE_FLAT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].usePercentage").value(hasItem(DEFAULT_USE_PERCENTAGE.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShopSection.class);
        ShopSection shopSection1 = new ShopSection();
        shopSection1.setId(1L);
        ShopSection shopSection2 = new ShopSection();
        shopSection2.setId(shopSection1.getId());
        assertThat(shopSection1).isEqualTo(shopSection2);
        shopSection2.setId(2L);
        assertThat(shopSection1).isNotEqualTo(shopSection2);
        shopSection1.setId(null);
        assertThat(shopSection1).isNotEqualTo(shopSection2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShopSectionDTO.class);
        ShopSectionDTO shopSectionDTO1 = new ShopSectionDTO();
        shopSectionDTO1.setId(1L);
        ShopSectionDTO shopSectionDTO2 = new ShopSectionDTO();
        assertThat(shopSectionDTO1).isNotEqualTo(shopSectionDTO2);
        shopSectionDTO2.setId(shopSectionDTO1.getId());
        assertThat(shopSectionDTO1).isEqualTo(shopSectionDTO2);
        shopSectionDTO2.setId(2L);
        assertThat(shopSectionDTO1).isNotEqualTo(shopSectionDTO2);
        shopSectionDTO1.setId(null);
        assertThat(shopSectionDTO1).isNotEqualTo(shopSectionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(shopSectionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(shopSectionMapper.fromId(null)).isNull();
    }
}
