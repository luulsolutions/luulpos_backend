package com.luulsolutions.luulpos.web.rest;

import com.luulsolutions.luulpos.LuulposApp;

import com.luulsolutions.luulpos.domain.PaymentMethod;
import com.luulsolutions.luulpos.domain.Shop;
import com.luulsolutions.luulpos.repository.PaymentMethodRepository;
import com.luulsolutions.luulpos.repository.search.PaymentMethodSearchRepository;
import com.luulsolutions.luulpos.service.PaymentMethodService;
import com.luulsolutions.luulpos.service.dto.PaymentMethodDTO;
import com.luulsolutions.luulpos.service.mapper.PaymentMethodMapper;
import com.luulsolutions.luulpos.web.rest.errors.ExceptionTranslator;
import com.luulsolutions.luulpos.service.dto.PaymentMethodCriteria;
import com.luulsolutions.luulpos.service.PaymentMethodQueryService;

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
 * Test class for the PaymentMethodResource REST controller.
 *
 * @see PaymentMethodResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LuulposApp.class)
public class PaymentMethodResourceIntTest {

    private static final String DEFAULT_PAYMENT_METHOD = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_METHOD = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private PaymentMethodMapper paymentMethodMapper;

    @Autowired
    private PaymentMethodService paymentMethodService;

    /**
     * This repository is mocked in the com.luulsolutions.luulpos.repository.search test package.
     *
     * @see com.luulsolutions.luulpos.repository.search.PaymentMethodSearchRepositoryMockConfiguration
     */
    @Autowired
    private PaymentMethodSearchRepository mockPaymentMethodSearchRepository;

    @Autowired
    private PaymentMethodQueryService paymentMethodQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPaymentMethodMockMvc;

    private PaymentMethod paymentMethod;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PaymentMethodResource paymentMethodResource = new PaymentMethodResource(paymentMethodService, paymentMethodQueryService);
        this.restPaymentMethodMockMvc = MockMvcBuilders.standaloneSetup(paymentMethodResource)
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
    public static PaymentMethod createEntity(EntityManager em) {
        PaymentMethod paymentMethod = new PaymentMethod()
            .paymentMethod(DEFAULT_PAYMENT_METHOD)
            .description(DEFAULT_DESCRIPTION)
            .active(DEFAULT_ACTIVE);
        return paymentMethod;
    }

    @Before
    public void initTest() {
        paymentMethod = createEntity(em);
    }

    @Test
    @Transactional
    public void createPaymentMethod() throws Exception {
        int databaseSizeBeforeCreate = paymentMethodRepository.findAll().size();

        // Create the PaymentMethod
        PaymentMethodDTO paymentMethodDTO = paymentMethodMapper.toDto(paymentMethod);
        restPaymentMethodMockMvc.perform(post("/api/payment-methods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentMethodDTO)))
            .andExpect(status().isCreated());

        // Validate the PaymentMethod in the database
        List<PaymentMethod> paymentMethodList = paymentMethodRepository.findAll();
        assertThat(paymentMethodList).hasSize(databaseSizeBeforeCreate + 1);
        PaymentMethod testPaymentMethod = paymentMethodList.get(paymentMethodList.size() - 1);
        assertThat(testPaymentMethod.getPaymentMethod()).isEqualTo(DEFAULT_PAYMENT_METHOD);
        assertThat(testPaymentMethod.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPaymentMethod.isActive()).isEqualTo(DEFAULT_ACTIVE);

        // Validate the PaymentMethod in Elasticsearch
        verify(mockPaymentMethodSearchRepository, times(1)).save(testPaymentMethod);
    }

    @Test
    @Transactional
    public void createPaymentMethodWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = paymentMethodRepository.findAll().size();

        // Create the PaymentMethod with an existing ID
        paymentMethod.setId(1L);
        PaymentMethodDTO paymentMethodDTO = paymentMethodMapper.toDto(paymentMethod);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentMethodMockMvc.perform(post("/api/payment-methods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentMethodDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PaymentMethod in the database
        List<PaymentMethod> paymentMethodList = paymentMethodRepository.findAll();
        assertThat(paymentMethodList).hasSize(databaseSizeBeforeCreate);

        // Validate the PaymentMethod in Elasticsearch
        verify(mockPaymentMethodSearchRepository, times(0)).save(paymentMethod);
    }

    @Test
    @Transactional
    public void checkPaymentMethodIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentMethodRepository.findAll().size();
        // set the field null
        paymentMethod.setPaymentMethod(null);

        // Create the PaymentMethod, which fails.
        PaymentMethodDTO paymentMethodDTO = paymentMethodMapper.toDto(paymentMethod);

        restPaymentMethodMockMvc.perform(post("/api/payment-methods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentMethodDTO)))
            .andExpect(status().isBadRequest());

        List<PaymentMethod> paymentMethodList = paymentMethodRepository.findAll();
        assertThat(paymentMethodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPaymentMethods() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList
        restPaymentMethodMockMvc.perform(get("/api/payment-methods?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentMethod.getId().intValue())))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getPaymentMethod() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get the paymentMethod
        restPaymentMethodMockMvc.perform(get("/api/payment-methods/{id}", paymentMethod.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(paymentMethod.getId().intValue()))
            .andExpect(jsonPath("$.paymentMethod").value(DEFAULT_PAYMENT_METHOD.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllPaymentMethodsByPaymentMethodIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where paymentMethod equals to DEFAULT_PAYMENT_METHOD
        defaultPaymentMethodShouldBeFound("paymentMethod.equals=" + DEFAULT_PAYMENT_METHOD);

        // Get all the paymentMethodList where paymentMethod equals to UPDATED_PAYMENT_METHOD
        defaultPaymentMethodShouldNotBeFound("paymentMethod.equals=" + UPDATED_PAYMENT_METHOD);
    }

    @Test
    @Transactional
    public void getAllPaymentMethodsByPaymentMethodIsInShouldWork() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where paymentMethod in DEFAULT_PAYMENT_METHOD or UPDATED_PAYMENT_METHOD
        defaultPaymentMethodShouldBeFound("paymentMethod.in=" + DEFAULT_PAYMENT_METHOD + "," + UPDATED_PAYMENT_METHOD);

        // Get all the paymentMethodList where paymentMethod equals to UPDATED_PAYMENT_METHOD
        defaultPaymentMethodShouldNotBeFound("paymentMethod.in=" + UPDATED_PAYMENT_METHOD);
    }

    @Test
    @Transactional
    public void getAllPaymentMethodsByPaymentMethodIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where paymentMethod is not null
        defaultPaymentMethodShouldBeFound("paymentMethod.specified=true");

        // Get all the paymentMethodList where paymentMethod is null
        defaultPaymentMethodShouldNotBeFound("paymentMethod.specified=false");
    }

    @Test
    @Transactional
    public void getAllPaymentMethodsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where description equals to DEFAULT_DESCRIPTION
        defaultPaymentMethodShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the paymentMethodList where description equals to UPDATED_DESCRIPTION
        defaultPaymentMethodShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllPaymentMethodsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultPaymentMethodShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the paymentMethodList where description equals to UPDATED_DESCRIPTION
        defaultPaymentMethodShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllPaymentMethodsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where description is not null
        defaultPaymentMethodShouldBeFound("description.specified=true");

        // Get all the paymentMethodList where description is null
        defaultPaymentMethodShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllPaymentMethodsByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where active equals to DEFAULT_ACTIVE
        defaultPaymentMethodShouldBeFound("active.equals=" + DEFAULT_ACTIVE);

        // Get all the paymentMethodList where active equals to UPDATED_ACTIVE
        defaultPaymentMethodShouldNotBeFound("active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllPaymentMethodsByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where active in DEFAULT_ACTIVE or UPDATED_ACTIVE
        defaultPaymentMethodShouldBeFound("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE);

        // Get all the paymentMethodList where active equals to UPDATED_ACTIVE
        defaultPaymentMethodShouldNotBeFound("active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllPaymentMethodsByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where active is not null
        defaultPaymentMethodShouldBeFound("active.specified=true");

        // Get all the paymentMethodList where active is null
        defaultPaymentMethodShouldNotBeFound("active.specified=false");
    }

    @Test
    @Transactional
    public void getAllPaymentMethodsByShopIsEqualToSomething() throws Exception {
        // Initialize the database
        Shop shop = ShopResourceIntTest.createEntity(em);
        em.persist(shop);
        em.flush();
        paymentMethod.setShop(shop);
        paymentMethodRepository.saveAndFlush(paymentMethod);
        Long shopId = shop.getId();

        // Get all the paymentMethodList where shop equals to shopId
        defaultPaymentMethodShouldBeFound("shopId.equals=" + shopId);

        // Get all the paymentMethodList where shop equals to shopId + 1
        defaultPaymentMethodShouldNotBeFound("shopId.equals=" + (shopId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultPaymentMethodShouldBeFound(String filter) throws Exception {
        restPaymentMethodMockMvc.perform(get("/api/payment-methods?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentMethod.getId().intValue())))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restPaymentMethodMockMvc.perform(get("/api/payment-methods/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultPaymentMethodShouldNotBeFound(String filter) throws Exception {
        restPaymentMethodMockMvc.perform(get("/api/payment-methods?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPaymentMethodMockMvc.perform(get("/api/payment-methods/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPaymentMethod() throws Exception {
        // Get the paymentMethod
        restPaymentMethodMockMvc.perform(get("/api/payment-methods/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePaymentMethod() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        int databaseSizeBeforeUpdate = paymentMethodRepository.findAll().size();

        // Update the paymentMethod
        PaymentMethod updatedPaymentMethod = paymentMethodRepository.findById(paymentMethod.getId()).get();
        // Disconnect from session so that the updates on updatedPaymentMethod are not directly saved in db
        em.detach(updatedPaymentMethod);
        updatedPaymentMethod
            .paymentMethod(UPDATED_PAYMENT_METHOD)
            .description(UPDATED_DESCRIPTION)
            .active(UPDATED_ACTIVE);
        PaymentMethodDTO paymentMethodDTO = paymentMethodMapper.toDto(updatedPaymentMethod);

        restPaymentMethodMockMvc.perform(put("/api/payment-methods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentMethodDTO)))
            .andExpect(status().isOk());

        // Validate the PaymentMethod in the database
        List<PaymentMethod> paymentMethodList = paymentMethodRepository.findAll();
        assertThat(paymentMethodList).hasSize(databaseSizeBeforeUpdate);
        PaymentMethod testPaymentMethod = paymentMethodList.get(paymentMethodList.size() - 1);
        assertThat(testPaymentMethod.getPaymentMethod()).isEqualTo(UPDATED_PAYMENT_METHOD);
        assertThat(testPaymentMethod.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPaymentMethod.isActive()).isEqualTo(UPDATED_ACTIVE);

        // Validate the PaymentMethod in Elasticsearch
        verify(mockPaymentMethodSearchRepository, times(1)).save(testPaymentMethod);
    }

    @Test
    @Transactional
    public void updateNonExistingPaymentMethod() throws Exception {
        int databaseSizeBeforeUpdate = paymentMethodRepository.findAll().size();

        // Create the PaymentMethod
        PaymentMethodDTO paymentMethodDTO = paymentMethodMapper.toDto(paymentMethod);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentMethodMockMvc.perform(put("/api/payment-methods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentMethodDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PaymentMethod in the database
        List<PaymentMethod> paymentMethodList = paymentMethodRepository.findAll();
        assertThat(paymentMethodList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PaymentMethod in Elasticsearch
        verify(mockPaymentMethodSearchRepository, times(0)).save(paymentMethod);
    }

    @Test
    @Transactional
    public void deletePaymentMethod() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        int databaseSizeBeforeDelete = paymentMethodRepository.findAll().size();

        // Get the paymentMethod
        restPaymentMethodMockMvc.perform(delete("/api/payment-methods/{id}", paymentMethod.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PaymentMethod> paymentMethodList = paymentMethodRepository.findAll();
        assertThat(paymentMethodList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the PaymentMethod in Elasticsearch
        verify(mockPaymentMethodSearchRepository, times(1)).deleteById(paymentMethod.getId());
    }

    @Test
    @Transactional
    public void searchPaymentMethod() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);
        when(mockPaymentMethodSearchRepository.search(queryStringQuery("id:" + paymentMethod.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(paymentMethod), PageRequest.of(0, 1), 1));
        // Search the paymentMethod
        restPaymentMethodMockMvc.perform(get("/api/_search/payment-methods?query=id:" + paymentMethod.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentMethod.getId().intValue())))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentMethod.class);
        PaymentMethod paymentMethod1 = new PaymentMethod();
        paymentMethod1.setId(1L);
        PaymentMethod paymentMethod2 = new PaymentMethod();
        paymentMethod2.setId(paymentMethod1.getId());
        assertThat(paymentMethod1).isEqualTo(paymentMethod2);
        paymentMethod2.setId(2L);
        assertThat(paymentMethod1).isNotEqualTo(paymentMethod2);
        paymentMethod1.setId(null);
        assertThat(paymentMethod1).isNotEqualTo(paymentMethod2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentMethodDTO.class);
        PaymentMethodDTO paymentMethodDTO1 = new PaymentMethodDTO();
        paymentMethodDTO1.setId(1L);
        PaymentMethodDTO paymentMethodDTO2 = new PaymentMethodDTO();
        assertThat(paymentMethodDTO1).isNotEqualTo(paymentMethodDTO2);
        paymentMethodDTO2.setId(paymentMethodDTO1.getId());
        assertThat(paymentMethodDTO1).isEqualTo(paymentMethodDTO2);
        paymentMethodDTO2.setId(2L);
        assertThat(paymentMethodDTO1).isNotEqualTo(paymentMethodDTO2);
        paymentMethodDTO1.setId(null);
        assertThat(paymentMethodDTO1).isNotEqualTo(paymentMethodDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(paymentMethodMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(paymentMethodMapper.fromId(null)).isNull();
    }
}
