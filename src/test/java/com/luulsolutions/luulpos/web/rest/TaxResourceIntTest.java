package com.luulsolutions.luulpos.web.rest;

import com.luulsolutions.luulpos.LuulposApp;

import com.luulsolutions.luulpos.domain.Tax;
import com.luulsolutions.luulpos.domain.Shop;
import com.luulsolutions.luulpos.domain.Product;
import com.luulsolutions.luulpos.repository.TaxRepository;
import com.luulsolutions.luulpos.repository.search.TaxSearchRepository;
import com.luulsolutions.luulpos.service.TaxService;
import com.luulsolutions.luulpos.service.dto.TaxDTO;
import com.luulsolutions.luulpos.service.mapper.TaxMapper;
import com.luulsolutions.luulpos.web.rest.errors.ExceptionTranslator;
import com.luulsolutions.luulpos.service.dto.TaxCriteria;
import com.luulsolutions.luulpos.service.TaxQueryService;

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
 * Test class for the TaxResource REST controller.
 *
 * @see TaxResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LuulposApp.class)
public class TaxResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Float DEFAULT_PERCENTAGE = 1F;
    private static final Float UPDATED_PERCENTAGE = 2F;

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    @Autowired
    private TaxRepository taxRepository;

    @Autowired
    private TaxMapper taxMapper;

    @Autowired
    private TaxService taxService;

    /**
     * This repository is mocked in the com.luulsolutions.luulpos.repository.search test package.
     *
     * @see com.luulsolutions.luulpos.repository.search.TaxSearchRepositoryMockConfiguration
     */
    @Autowired
    private TaxSearchRepository mockTaxSearchRepository;

    @Autowired
    private TaxQueryService taxQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTaxMockMvc;

    private Tax tax;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TaxResource taxResource = new TaxResource(taxService, taxQueryService);
        this.restTaxMockMvc = MockMvcBuilders.standaloneSetup(taxResource)
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
    public static Tax createEntity(EntityManager em) {
        Tax tax = new Tax()
            .description(DEFAULT_DESCRIPTION)
            .percentage(DEFAULT_PERCENTAGE)
            .amount(DEFAULT_AMOUNT)
            .active(DEFAULT_ACTIVE);
        return tax;
    }

    @Before
    public void initTest() {
        tax = createEntity(em);
    }

    @Test
    @Transactional
    public void createTax() throws Exception {
        int databaseSizeBeforeCreate = taxRepository.findAll().size();

        // Create the Tax
        TaxDTO taxDTO = taxMapper.toDto(tax);
        restTaxMockMvc.perform(post("/api/taxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taxDTO)))
            .andExpect(status().isCreated());

        // Validate the Tax in the database
        List<Tax> taxList = taxRepository.findAll();
        assertThat(taxList).hasSize(databaseSizeBeforeCreate + 1);
        Tax testTax = taxList.get(taxList.size() - 1);
        assertThat(testTax.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTax.getPercentage()).isEqualTo(DEFAULT_PERCENTAGE);
        assertThat(testTax.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testTax.isActive()).isEqualTo(DEFAULT_ACTIVE);

        // Validate the Tax in Elasticsearch
        verify(mockTaxSearchRepository, times(1)).save(testTax);
    }

    @Test
    @Transactional
    public void createTaxWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = taxRepository.findAll().size();

        // Create the Tax with an existing ID
        tax.setId(1L);
        TaxDTO taxDTO = taxMapper.toDto(tax);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaxMockMvc.perform(post("/api/taxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taxDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Tax in the database
        List<Tax> taxList = taxRepository.findAll();
        assertThat(taxList).hasSize(databaseSizeBeforeCreate);

        // Validate the Tax in Elasticsearch
        verify(mockTaxSearchRepository, times(0)).save(tax);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = taxRepository.findAll().size();
        // set the field null
        tax.setDescription(null);

        // Create the Tax, which fails.
        TaxDTO taxDTO = taxMapper.toDto(tax);

        restTaxMockMvc.perform(post("/api/taxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taxDTO)))
            .andExpect(status().isBadRequest());

        List<Tax> taxList = taxRepository.findAll();
        assertThat(taxList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPercentageIsRequired() throws Exception {
        int databaseSizeBeforeTest = taxRepository.findAll().size();
        // set the field null
        tax.setPercentage(null);

        // Create the Tax, which fails.
        TaxDTO taxDTO = taxMapper.toDto(tax);

        restTaxMockMvc.perform(post("/api/taxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taxDTO)))
            .andExpect(status().isBadRequest());

        List<Tax> taxList = taxRepository.findAll();
        assertThat(taxList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = taxRepository.findAll().size();
        // set the field null
        tax.setAmount(null);

        // Create the Tax, which fails.
        TaxDTO taxDTO = taxMapper.toDto(tax);

        restTaxMockMvc.perform(post("/api/taxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taxDTO)))
            .andExpect(status().isBadRequest());

        List<Tax> taxList = taxRepository.findAll();
        assertThat(taxList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTaxes() throws Exception {
        // Initialize the database
        taxRepository.saveAndFlush(tax);

        // Get all the taxList
        restTaxMockMvc.perform(get("/api/taxes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tax.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].percentage").value(hasItem(DEFAULT_PERCENTAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getTax() throws Exception {
        // Initialize the database
        taxRepository.saveAndFlush(tax);

        // Get the tax
        restTaxMockMvc.perform(get("/api/taxes/{id}", tax.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tax.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.percentage").value(DEFAULT_PERCENTAGE.doubleValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllTaxesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        taxRepository.saveAndFlush(tax);

        // Get all the taxList where description equals to DEFAULT_DESCRIPTION
        defaultTaxShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the taxList where description equals to UPDATED_DESCRIPTION
        defaultTaxShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllTaxesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        taxRepository.saveAndFlush(tax);

        // Get all the taxList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultTaxShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the taxList where description equals to UPDATED_DESCRIPTION
        defaultTaxShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllTaxesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        taxRepository.saveAndFlush(tax);

        // Get all the taxList where description is not null
        defaultTaxShouldBeFound("description.specified=true");

        // Get all the taxList where description is null
        defaultTaxShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllTaxesByPercentageIsEqualToSomething() throws Exception {
        // Initialize the database
        taxRepository.saveAndFlush(tax);

        // Get all the taxList where percentage equals to DEFAULT_PERCENTAGE
        defaultTaxShouldBeFound("percentage.equals=" + DEFAULT_PERCENTAGE);

        // Get all the taxList where percentage equals to UPDATED_PERCENTAGE
        defaultTaxShouldNotBeFound("percentage.equals=" + UPDATED_PERCENTAGE);
    }

    @Test
    @Transactional
    public void getAllTaxesByPercentageIsInShouldWork() throws Exception {
        // Initialize the database
        taxRepository.saveAndFlush(tax);

        // Get all the taxList where percentage in DEFAULT_PERCENTAGE or UPDATED_PERCENTAGE
        defaultTaxShouldBeFound("percentage.in=" + DEFAULT_PERCENTAGE + "," + UPDATED_PERCENTAGE);

        // Get all the taxList where percentage equals to UPDATED_PERCENTAGE
        defaultTaxShouldNotBeFound("percentage.in=" + UPDATED_PERCENTAGE);
    }

    @Test
    @Transactional
    public void getAllTaxesByPercentageIsNullOrNotNull() throws Exception {
        // Initialize the database
        taxRepository.saveAndFlush(tax);

        // Get all the taxList where percentage is not null
        defaultTaxShouldBeFound("percentage.specified=true");

        // Get all the taxList where percentage is null
        defaultTaxShouldNotBeFound("percentage.specified=false");
    }

    @Test
    @Transactional
    public void getAllTaxesByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        taxRepository.saveAndFlush(tax);

        // Get all the taxList where amount equals to DEFAULT_AMOUNT
        defaultTaxShouldBeFound("amount.equals=" + DEFAULT_AMOUNT);

        // Get all the taxList where amount equals to UPDATED_AMOUNT
        defaultTaxShouldNotBeFound("amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllTaxesByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        taxRepository.saveAndFlush(tax);

        // Get all the taxList where amount in DEFAULT_AMOUNT or UPDATED_AMOUNT
        defaultTaxShouldBeFound("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT);

        // Get all the taxList where amount equals to UPDATED_AMOUNT
        defaultTaxShouldNotBeFound("amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllTaxesByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        taxRepository.saveAndFlush(tax);

        // Get all the taxList where amount is not null
        defaultTaxShouldBeFound("amount.specified=true");

        // Get all the taxList where amount is null
        defaultTaxShouldNotBeFound("amount.specified=false");
    }

    @Test
    @Transactional
    public void getAllTaxesByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        taxRepository.saveAndFlush(tax);

        // Get all the taxList where active equals to DEFAULT_ACTIVE
        defaultTaxShouldBeFound("active.equals=" + DEFAULT_ACTIVE);

        // Get all the taxList where active equals to UPDATED_ACTIVE
        defaultTaxShouldNotBeFound("active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllTaxesByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        taxRepository.saveAndFlush(tax);

        // Get all the taxList where active in DEFAULT_ACTIVE or UPDATED_ACTIVE
        defaultTaxShouldBeFound("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE);

        // Get all the taxList where active equals to UPDATED_ACTIVE
        defaultTaxShouldNotBeFound("active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllTaxesByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        taxRepository.saveAndFlush(tax);

        // Get all the taxList where active is not null
        defaultTaxShouldBeFound("active.specified=true");

        // Get all the taxList where active is null
        defaultTaxShouldNotBeFound("active.specified=false");
    }

    @Test
    @Transactional
    public void getAllTaxesByShopIsEqualToSomething() throws Exception {
        // Initialize the database
        Shop shop = ShopResourceIntTest.createEntity(em);
        em.persist(shop);
        em.flush();
        tax.setShop(shop);
        taxRepository.saveAndFlush(tax);
        Long shopId = shop.getId();

        // Get all the taxList where shop equals to shopId
        defaultTaxShouldBeFound("shopId.equals=" + shopId);

        // Get all the taxList where shop equals to shopId + 1
        defaultTaxShouldNotBeFound("shopId.equals=" + (shopId + 1));
    }


    @Test
    @Transactional
    public void getAllTaxesByProductIsEqualToSomething() throws Exception {
        // Initialize the database
        Product product = ProductResourceIntTest.createEntity(em);
        em.persist(product);
        em.flush();
        tax.addProduct(product);
        taxRepository.saveAndFlush(tax);
        Long productId = product.getId();

        // Get all the taxList where product equals to productId
        defaultTaxShouldBeFound("productId.equals=" + productId);

        // Get all the taxList where product equals to productId + 1
        defaultTaxShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultTaxShouldBeFound(String filter) throws Exception {
        restTaxMockMvc.perform(get("/api/taxes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tax.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].percentage").value(hasItem(DEFAULT_PERCENTAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restTaxMockMvc.perform(get("/api/taxes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultTaxShouldNotBeFound(String filter) throws Exception {
        restTaxMockMvc.perform(get("/api/taxes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTaxMockMvc.perform(get("/api/taxes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingTax() throws Exception {
        // Get the tax
        restTaxMockMvc.perform(get("/api/taxes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTax() throws Exception {
        // Initialize the database
        taxRepository.saveAndFlush(tax);

        int databaseSizeBeforeUpdate = taxRepository.findAll().size();

        // Update the tax
        Tax updatedTax = taxRepository.findById(tax.getId()).get();
        // Disconnect from session so that the updates on updatedTax are not directly saved in db
        em.detach(updatedTax);
        updatedTax
            .description(UPDATED_DESCRIPTION)
            .percentage(UPDATED_PERCENTAGE)
            .amount(UPDATED_AMOUNT)
            .active(UPDATED_ACTIVE);
        TaxDTO taxDTO = taxMapper.toDto(updatedTax);

        restTaxMockMvc.perform(put("/api/taxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taxDTO)))
            .andExpect(status().isOk());

        // Validate the Tax in the database
        List<Tax> taxList = taxRepository.findAll();
        assertThat(taxList).hasSize(databaseSizeBeforeUpdate);
        Tax testTax = taxList.get(taxList.size() - 1);
        assertThat(testTax.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTax.getPercentage()).isEqualTo(UPDATED_PERCENTAGE);
        assertThat(testTax.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testTax.isActive()).isEqualTo(UPDATED_ACTIVE);

        // Validate the Tax in Elasticsearch
        verify(mockTaxSearchRepository, times(1)).save(testTax);
    }

    @Test
    @Transactional
    public void updateNonExistingTax() throws Exception {
        int databaseSizeBeforeUpdate = taxRepository.findAll().size();

        // Create the Tax
        TaxDTO taxDTO = taxMapper.toDto(tax);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaxMockMvc.perform(put("/api/taxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taxDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Tax in the database
        List<Tax> taxList = taxRepository.findAll();
        assertThat(taxList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Tax in Elasticsearch
        verify(mockTaxSearchRepository, times(0)).save(tax);
    }

    @Test
    @Transactional
    public void deleteTax() throws Exception {
        // Initialize the database
        taxRepository.saveAndFlush(tax);

        int databaseSizeBeforeDelete = taxRepository.findAll().size();

        // Get the tax
        restTaxMockMvc.perform(delete("/api/taxes/{id}", tax.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Tax> taxList = taxRepository.findAll();
        assertThat(taxList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Tax in Elasticsearch
        verify(mockTaxSearchRepository, times(1)).deleteById(tax.getId());
    }

    @Test
    @Transactional
    public void searchTax() throws Exception {
        // Initialize the database
        taxRepository.saveAndFlush(tax);
        when(mockTaxSearchRepository.search(queryStringQuery("id:" + tax.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(tax), PageRequest.of(0, 1), 1));
        // Search the tax
        restTaxMockMvc.perform(get("/api/_search/taxes?query=id:" + tax.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tax.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].percentage").value(hasItem(DEFAULT_PERCENTAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tax.class);
        Tax tax1 = new Tax();
        tax1.setId(1L);
        Tax tax2 = new Tax();
        tax2.setId(tax1.getId());
        assertThat(tax1).isEqualTo(tax2);
        tax2.setId(2L);
        assertThat(tax1).isNotEqualTo(tax2);
        tax1.setId(null);
        assertThat(tax1).isNotEqualTo(tax2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaxDTO.class);
        TaxDTO taxDTO1 = new TaxDTO();
        taxDTO1.setId(1L);
        TaxDTO taxDTO2 = new TaxDTO();
        assertThat(taxDTO1).isNotEqualTo(taxDTO2);
        taxDTO2.setId(taxDTO1.getId());
        assertThat(taxDTO1).isEqualTo(taxDTO2);
        taxDTO2.setId(2L);
        assertThat(taxDTO1).isNotEqualTo(taxDTO2);
        taxDTO1.setId(null);
        assertThat(taxDTO1).isNotEqualTo(taxDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(taxMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(taxMapper.fromId(null)).isNull();
    }
}
