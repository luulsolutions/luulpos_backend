package com.luulsolutions.luulpos.web.rest;

import com.luulsolutions.luulpos.LuulposApp;

import com.luulsolutions.luulpos.domain.Discount;
import com.luulsolutions.luulpos.domain.Shop;
import com.luulsolutions.luulpos.domain.Product;
import com.luulsolutions.luulpos.repository.DiscountRepository;
import com.luulsolutions.luulpos.repository.search.DiscountSearchRepository;
import com.luulsolutions.luulpos.service.DiscountService;
import com.luulsolutions.luulpos.service.dto.DiscountDTO;
import com.luulsolutions.luulpos.service.mapper.DiscountMapper;
import com.luulsolutions.luulpos.web.rest.errors.ExceptionTranslator;
import com.luulsolutions.luulpos.service.dto.DiscountCriteria;
import com.luulsolutions.luulpos.service.DiscountQueryService;

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
 * Test class for the DiscountResource REST controller.
 *
 * @see DiscountResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LuulposApp.class)
public class DiscountResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Float DEFAULT_PERCENTAGE = 1F;
    private static final Float UPDATED_PERCENTAGE = 2F;

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private DiscountMapper discountMapper;

    @Autowired
    private DiscountService discountService;

    /**
     * This repository is mocked in the com.luulsolutions.luulpos.repository.search test package.
     *
     * @see com.luulsolutions.luulpos.repository.search.DiscountSearchRepositoryMockConfiguration
     */
    @Autowired
    private DiscountSearchRepository mockDiscountSearchRepository;

    @Autowired
    private DiscountQueryService discountQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDiscountMockMvc;

    private Discount discount;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DiscountResource discountResource = new DiscountResource(discountService, discountQueryService);
        this.restDiscountMockMvc = MockMvcBuilders.standaloneSetup(discountResource)
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
    public static Discount createEntity(EntityManager em) {
        Discount discount = new Discount()
            .description(DEFAULT_DESCRIPTION)
            .percentage(DEFAULT_PERCENTAGE)
            .amount(DEFAULT_AMOUNT)
            .active(DEFAULT_ACTIVE);
        return discount;
    }

    @Before
    public void initTest() {
        discount = createEntity(em);
    }

    @Test
    @Transactional
    public void createDiscount() throws Exception {
        int databaseSizeBeforeCreate = discountRepository.findAll().size();

        // Create the Discount
        DiscountDTO discountDTO = discountMapper.toDto(discount);
        restDiscountMockMvc.perform(post("/api/discounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discountDTO)))
            .andExpect(status().isCreated());

        // Validate the Discount in the database
        List<Discount> discountList = discountRepository.findAll();
        assertThat(discountList).hasSize(databaseSizeBeforeCreate + 1);
        Discount testDiscount = discountList.get(discountList.size() - 1);
        assertThat(testDiscount.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDiscount.getPercentage()).isEqualTo(DEFAULT_PERCENTAGE);
        assertThat(testDiscount.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testDiscount.isActive()).isEqualTo(DEFAULT_ACTIVE);

        // Validate the Discount in Elasticsearch
        verify(mockDiscountSearchRepository, times(1)).save(testDiscount);
    }

    @Test
    @Transactional
    public void createDiscountWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = discountRepository.findAll().size();

        // Create the Discount with an existing ID
        discount.setId(1L);
        DiscountDTO discountDTO = discountMapper.toDto(discount);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDiscountMockMvc.perform(post("/api/discounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discountDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Discount in the database
        List<Discount> discountList = discountRepository.findAll();
        assertThat(discountList).hasSize(databaseSizeBeforeCreate);

        // Validate the Discount in Elasticsearch
        verify(mockDiscountSearchRepository, times(0)).save(discount);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = discountRepository.findAll().size();
        // set the field null
        discount.setDescription(null);

        // Create the Discount, which fails.
        DiscountDTO discountDTO = discountMapper.toDto(discount);

        restDiscountMockMvc.perform(post("/api/discounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discountDTO)))
            .andExpect(status().isBadRequest());

        List<Discount> discountList = discountRepository.findAll();
        assertThat(discountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPercentageIsRequired() throws Exception {
        int databaseSizeBeforeTest = discountRepository.findAll().size();
        // set the field null
        discount.setPercentage(null);

        // Create the Discount, which fails.
        DiscountDTO discountDTO = discountMapper.toDto(discount);

        restDiscountMockMvc.perform(post("/api/discounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discountDTO)))
            .andExpect(status().isBadRequest());

        List<Discount> discountList = discountRepository.findAll();
        assertThat(discountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = discountRepository.findAll().size();
        // set the field null
        discount.setAmount(null);

        // Create the Discount, which fails.
        DiscountDTO discountDTO = discountMapper.toDto(discount);

        restDiscountMockMvc.perform(post("/api/discounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discountDTO)))
            .andExpect(status().isBadRequest());

        List<Discount> discountList = discountRepository.findAll();
        assertThat(discountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDiscounts() throws Exception {
        // Initialize the database
        discountRepository.saveAndFlush(discount);

        // Get all the discountList
        restDiscountMockMvc.perform(get("/api/discounts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(discount.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].percentage").value(hasItem(DEFAULT_PERCENTAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getDiscount() throws Exception {
        // Initialize the database
        discountRepository.saveAndFlush(discount);

        // Get the discount
        restDiscountMockMvc.perform(get("/api/discounts/{id}", discount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(discount.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.percentage").value(DEFAULT_PERCENTAGE.doubleValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllDiscountsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        discountRepository.saveAndFlush(discount);

        // Get all the discountList where description equals to DEFAULT_DESCRIPTION
        defaultDiscountShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the discountList where description equals to UPDATED_DESCRIPTION
        defaultDiscountShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllDiscountsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        discountRepository.saveAndFlush(discount);

        // Get all the discountList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultDiscountShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the discountList where description equals to UPDATED_DESCRIPTION
        defaultDiscountShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllDiscountsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        discountRepository.saveAndFlush(discount);

        // Get all the discountList where description is not null
        defaultDiscountShouldBeFound("description.specified=true");

        // Get all the discountList where description is null
        defaultDiscountShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllDiscountsByPercentageIsEqualToSomething() throws Exception {
        // Initialize the database
        discountRepository.saveAndFlush(discount);

        // Get all the discountList where percentage equals to DEFAULT_PERCENTAGE
        defaultDiscountShouldBeFound("percentage.equals=" + DEFAULT_PERCENTAGE);

        // Get all the discountList where percentage equals to UPDATED_PERCENTAGE
        defaultDiscountShouldNotBeFound("percentage.equals=" + UPDATED_PERCENTAGE);
    }

    @Test
    @Transactional
    public void getAllDiscountsByPercentageIsInShouldWork() throws Exception {
        // Initialize the database
        discountRepository.saveAndFlush(discount);

        // Get all the discountList where percentage in DEFAULT_PERCENTAGE or UPDATED_PERCENTAGE
        defaultDiscountShouldBeFound("percentage.in=" + DEFAULT_PERCENTAGE + "," + UPDATED_PERCENTAGE);

        // Get all the discountList where percentage equals to UPDATED_PERCENTAGE
        defaultDiscountShouldNotBeFound("percentage.in=" + UPDATED_PERCENTAGE);
    }

    @Test
    @Transactional
    public void getAllDiscountsByPercentageIsNullOrNotNull() throws Exception {
        // Initialize the database
        discountRepository.saveAndFlush(discount);

        // Get all the discountList where percentage is not null
        defaultDiscountShouldBeFound("percentage.specified=true");

        // Get all the discountList where percentage is null
        defaultDiscountShouldNotBeFound("percentage.specified=false");
    }

    @Test
    @Transactional
    public void getAllDiscountsByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        discountRepository.saveAndFlush(discount);

        // Get all the discountList where amount equals to DEFAULT_AMOUNT
        defaultDiscountShouldBeFound("amount.equals=" + DEFAULT_AMOUNT);

        // Get all the discountList where amount equals to UPDATED_AMOUNT
        defaultDiscountShouldNotBeFound("amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllDiscountsByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        discountRepository.saveAndFlush(discount);

        // Get all the discountList where amount in DEFAULT_AMOUNT or UPDATED_AMOUNT
        defaultDiscountShouldBeFound("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT);

        // Get all the discountList where amount equals to UPDATED_AMOUNT
        defaultDiscountShouldNotBeFound("amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllDiscountsByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        discountRepository.saveAndFlush(discount);

        // Get all the discountList where amount is not null
        defaultDiscountShouldBeFound("amount.specified=true");

        // Get all the discountList where amount is null
        defaultDiscountShouldNotBeFound("amount.specified=false");
    }

    @Test
    @Transactional
    public void getAllDiscountsByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        discountRepository.saveAndFlush(discount);

        // Get all the discountList where active equals to DEFAULT_ACTIVE
        defaultDiscountShouldBeFound("active.equals=" + DEFAULT_ACTIVE);

        // Get all the discountList where active equals to UPDATED_ACTIVE
        defaultDiscountShouldNotBeFound("active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllDiscountsByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        discountRepository.saveAndFlush(discount);

        // Get all the discountList where active in DEFAULT_ACTIVE or UPDATED_ACTIVE
        defaultDiscountShouldBeFound("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE);

        // Get all the discountList where active equals to UPDATED_ACTIVE
        defaultDiscountShouldNotBeFound("active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllDiscountsByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        discountRepository.saveAndFlush(discount);

        // Get all the discountList where active is not null
        defaultDiscountShouldBeFound("active.specified=true");

        // Get all the discountList where active is null
        defaultDiscountShouldNotBeFound("active.specified=false");
    }

    @Test
    @Transactional
    public void getAllDiscountsByShopIsEqualToSomething() throws Exception {
        // Initialize the database
        Shop shop = ShopResourceIntTest.createEntity(em);
        em.persist(shop);
        em.flush();
        discount.setShop(shop);
        discountRepository.saveAndFlush(discount);
        Long shopId = shop.getId();

        // Get all the discountList where shop equals to shopId
        defaultDiscountShouldBeFound("shopId.equals=" + shopId);

        // Get all the discountList where shop equals to shopId + 1
        defaultDiscountShouldNotBeFound("shopId.equals=" + (shopId + 1));
    }


    @Test
    @Transactional
    public void getAllDiscountsByProductIsEqualToSomething() throws Exception {
        // Initialize the database
        Product product = ProductResourceIntTest.createEntity(em);
        em.persist(product);
        em.flush();
        discount.addProduct(product);
        discountRepository.saveAndFlush(discount);
        Long productId = product.getId();

        // Get all the discountList where product equals to productId
        defaultDiscountShouldBeFound("productId.equals=" + productId);

        // Get all the discountList where product equals to productId + 1
        defaultDiscountShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultDiscountShouldBeFound(String filter) throws Exception {
        restDiscountMockMvc.perform(get("/api/discounts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(discount.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].percentage").value(hasItem(DEFAULT_PERCENTAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restDiscountMockMvc.perform(get("/api/discounts/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultDiscountShouldNotBeFound(String filter) throws Exception {
        restDiscountMockMvc.perform(get("/api/discounts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDiscountMockMvc.perform(get("/api/discounts/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingDiscount() throws Exception {
        // Get the discount
        restDiscountMockMvc.perform(get("/api/discounts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDiscount() throws Exception {
        // Initialize the database
        discountRepository.saveAndFlush(discount);

        int databaseSizeBeforeUpdate = discountRepository.findAll().size();

        // Update the discount
        Discount updatedDiscount = discountRepository.findById(discount.getId()).get();
        // Disconnect from session so that the updates on updatedDiscount are not directly saved in db
        em.detach(updatedDiscount);
        updatedDiscount
            .description(UPDATED_DESCRIPTION)
            .percentage(UPDATED_PERCENTAGE)
            .amount(UPDATED_AMOUNT)
            .active(UPDATED_ACTIVE);
        DiscountDTO discountDTO = discountMapper.toDto(updatedDiscount);

        restDiscountMockMvc.perform(put("/api/discounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discountDTO)))
            .andExpect(status().isOk());

        // Validate the Discount in the database
        List<Discount> discountList = discountRepository.findAll();
        assertThat(discountList).hasSize(databaseSizeBeforeUpdate);
        Discount testDiscount = discountList.get(discountList.size() - 1);
        assertThat(testDiscount.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDiscount.getPercentage()).isEqualTo(UPDATED_PERCENTAGE);
        assertThat(testDiscount.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testDiscount.isActive()).isEqualTo(UPDATED_ACTIVE);

        // Validate the Discount in Elasticsearch
        verify(mockDiscountSearchRepository, times(1)).save(testDiscount);
    }

    @Test
    @Transactional
    public void updateNonExistingDiscount() throws Exception {
        int databaseSizeBeforeUpdate = discountRepository.findAll().size();

        // Create the Discount
        DiscountDTO discountDTO = discountMapper.toDto(discount);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDiscountMockMvc.perform(put("/api/discounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discountDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Discount in the database
        List<Discount> discountList = discountRepository.findAll();
        assertThat(discountList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Discount in Elasticsearch
        verify(mockDiscountSearchRepository, times(0)).save(discount);
    }

    @Test
    @Transactional
    public void deleteDiscount() throws Exception {
        // Initialize the database
        discountRepository.saveAndFlush(discount);

        int databaseSizeBeforeDelete = discountRepository.findAll().size();

        // Get the discount
        restDiscountMockMvc.perform(delete("/api/discounts/{id}", discount.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Discount> discountList = discountRepository.findAll();
        assertThat(discountList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Discount in Elasticsearch
        verify(mockDiscountSearchRepository, times(1)).deleteById(discount.getId());
    }

    @Test
    @Transactional
    public void searchDiscount() throws Exception {
        // Initialize the database
        discountRepository.saveAndFlush(discount);
        when(mockDiscountSearchRepository.search(queryStringQuery("id:" + discount.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(discount), PageRequest.of(0, 1), 1));
        // Search the discount
        restDiscountMockMvc.perform(get("/api/_search/discounts?query=id:" + discount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(discount.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].percentage").value(hasItem(DEFAULT_PERCENTAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Discount.class);
        Discount discount1 = new Discount();
        discount1.setId(1L);
        Discount discount2 = new Discount();
        discount2.setId(discount1.getId());
        assertThat(discount1).isEqualTo(discount2);
        discount2.setId(2L);
        assertThat(discount1).isNotEqualTo(discount2);
        discount1.setId(null);
        assertThat(discount1).isNotEqualTo(discount2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DiscountDTO.class);
        DiscountDTO discountDTO1 = new DiscountDTO();
        discountDTO1.setId(1L);
        DiscountDTO discountDTO2 = new DiscountDTO();
        assertThat(discountDTO1).isNotEqualTo(discountDTO2);
        discountDTO2.setId(discountDTO1.getId());
        assertThat(discountDTO1).isEqualTo(discountDTO2);
        discountDTO2.setId(2L);
        assertThat(discountDTO1).isNotEqualTo(discountDTO2);
        discountDTO1.setId(null);
        assertThat(discountDTO1).isNotEqualTo(discountDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(discountMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(discountMapper.fromId(null)).isNull();
    }
}
