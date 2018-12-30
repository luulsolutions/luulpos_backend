package com.luulsolutions.luulpos.web.rest;

import com.luulsolutions.luulpos.LuulposApp;

import com.luulsolutions.luulpos.domain.Payment;
import com.luulsolutions.luulpos.domain.Shop;
import com.luulsolutions.luulpos.domain.Profile;
import com.luulsolutions.luulpos.domain.PaymentMethod;
import com.luulsolutions.luulpos.domain.Orders;
import com.luulsolutions.luulpos.repository.PaymentRepository;
import com.luulsolutions.luulpos.repository.search.PaymentSearchRepository;
import com.luulsolutions.luulpos.service.PaymentService;
import com.luulsolutions.luulpos.service.dto.PaymentDTO;
import com.luulsolutions.luulpos.service.mapper.PaymentMapper;
import com.luulsolutions.luulpos.web.rest.errors.ExceptionTranslator;
import com.luulsolutions.luulpos.service.dto.PaymentCriteria;
import com.luulsolutions.luulpos.service.PaymentQueryService;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;


import static com.luulsolutions.luulpos.web.rest.TestUtil.sameInstant;
import static com.luulsolutions.luulpos.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.luulsolutions.luulpos.domain.enumeration.PaymentStatus;
/**
 * Test class for the PaymentResource REST controller.
 *
 * @see PaymentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LuulposApp.class)
public class PaymentResourceIntTest {

    private static final ZonedDateTime DEFAULT_PAYMENT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_PAYMENT_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_PAYMENT_PROVIDER = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_PROVIDER = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final PaymentStatus DEFAULT_PAYMENT_STATUS = PaymentStatus.PENDING;
    private static final PaymentStatus UPDATED_PAYMENT_STATUS = PaymentStatus.PAID;

    private static final String DEFAULT_CURENCY = "AAAAAAAAAA";
    private static final String UPDATED_CURENCY = "BBBBBBBBBB";

    private static final String DEFAULT_CUSTOMER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_NAME = "BBBBBBBBBB";

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentMapper paymentMapper;

    @Autowired
    private PaymentService paymentService;

    /**
     * This repository is mocked in the com.luulsolutions.luulpos.repository.search test package.
     *
     * @see com.luulsolutions.luulpos.repository.search.PaymentSearchRepositoryMockConfiguration
     */
    @Autowired
    private PaymentSearchRepository mockPaymentSearchRepository;

    @Autowired
    private PaymentQueryService paymentQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPaymentMockMvc;

    private Payment payment;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PaymentResource paymentResource = new PaymentResource(paymentService, paymentQueryService);
        this.restPaymentMockMvc = MockMvcBuilders.standaloneSetup(paymentResource)
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
    public static Payment createEntity(EntityManager em) {
        Payment payment = new Payment()
            .paymentDate(DEFAULT_PAYMENT_DATE)
            .paymentProvider(DEFAULT_PAYMENT_PROVIDER)
            .amount(DEFAULT_AMOUNT)
            .paymentStatus(DEFAULT_PAYMENT_STATUS)
            .curency(DEFAULT_CURENCY)
            .customerName(DEFAULT_CUSTOMER_NAME);
        return payment;
    }

    @Before
    public void initTest() {
        payment = createEntity(em);
    }

    @Test
    @Transactional
    public void createPayment() throws Exception {
        int databaseSizeBeforeCreate = paymentRepository.findAll().size();

        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);
        restPaymentMockMvc.perform(post("/api/payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentDTO)))
            .andExpect(status().isCreated());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeCreate + 1);
        Payment testPayment = paymentList.get(paymentList.size() - 1);
        assertThat(testPayment.getPaymentDate()).isEqualTo(DEFAULT_PAYMENT_DATE);
        assertThat(testPayment.getPaymentProvider()).isEqualTo(DEFAULT_PAYMENT_PROVIDER);
        assertThat(testPayment.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testPayment.getPaymentStatus()).isEqualTo(DEFAULT_PAYMENT_STATUS);
        assertThat(testPayment.getCurency()).isEqualTo(DEFAULT_CURENCY);
        assertThat(testPayment.getCustomerName()).isEqualTo(DEFAULT_CUSTOMER_NAME);

        // Validate the Payment in Elasticsearch
        verify(mockPaymentSearchRepository, times(1)).save(testPayment);
    }

    @Test
    @Transactional
    public void createPaymentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = paymentRepository.findAll().size();

        // Create the Payment with an existing ID
        payment.setId(1L);
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentMockMvc.perform(post("/api/payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeCreate);

        // Validate the Payment in Elasticsearch
        verify(mockPaymentSearchRepository, times(0)).save(payment);
    }

    @Test
    @Transactional
    public void getAllPayments() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList
        restPaymentMockMvc.perform(get("/api/payments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payment.getId().intValue())))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(sameInstant(DEFAULT_PAYMENT_DATE))))
            .andExpect(jsonPath("$.[*].paymentProvider").value(hasItem(DEFAULT_PAYMENT_PROVIDER.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].paymentStatus").value(hasItem(DEFAULT_PAYMENT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].curency").value(hasItem(DEFAULT_CURENCY.toString())))
            .andExpect(jsonPath("$.[*].customerName").value(hasItem(DEFAULT_CUSTOMER_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getPayment() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get the payment
        restPaymentMockMvc.perform(get("/api/payments/{id}", payment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(payment.getId().intValue()))
            .andExpect(jsonPath("$.paymentDate").value(sameInstant(DEFAULT_PAYMENT_DATE)))
            .andExpect(jsonPath("$.paymentProvider").value(DEFAULT_PAYMENT_PROVIDER.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.paymentStatus").value(DEFAULT_PAYMENT_STATUS.toString()))
            .andExpect(jsonPath("$.curency").value(DEFAULT_CURENCY.toString()))
            .andExpect(jsonPath("$.customerName").value(DEFAULT_CUSTOMER_NAME.toString()));
    }

    @Test
    @Transactional
    public void getAllPaymentsByPaymentDateIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentDate equals to DEFAULT_PAYMENT_DATE
        defaultPaymentShouldBeFound("paymentDate.equals=" + DEFAULT_PAYMENT_DATE);

        // Get all the paymentList where paymentDate equals to UPDATED_PAYMENT_DATE
        defaultPaymentShouldNotBeFound("paymentDate.equals=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    public void getAllPaymentsByPaymentDateIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentDate in DEFAULT_PAYMENT_DATE or UPDATED_PAYMENT_DATE
        defaultPaymentShouldBeFound("paymentDate.in=" + DEFAULT_PAYMENT_DATE + "," + UPDATED_PAYMENT_DATE);

        // Get all the paymentList where paymentDate equals to UPDATED_PAYMENT_DATE
        defaultPaymentShouldNotBeFound("paymentDate.in=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    public void getAllPaymentsByPaymentDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentDate is not null
        defaultPaymentShouldBeFound("paymentDate.specified=true");

        // Get all the paymentList where paymentDate is null
        defaultPaymentShouldNotBeFound("paymentDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllPaymentsByPaymentDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentDate greater than or equals to DEFAULT_PAYMENT_DATE
        defaultPaymentShouldBeFound("paymentDate.greaterOrEqualThan=" + DEFAULT_PAYMENT_DATE);

        // Get all the paymentList where paymentDate greater than or equals to UPDATED_PAYMENT_DATE
        defaultPaymentShouldNotBeFound("paymentDate.greaterOrEqualThan=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    public void getAllPaymentsByPaymentDateIsLessThanSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentDate less than or equals to DEFAULT_PAYMENT_DATE
        defaultPaymentShouldNotBeFound("paymentDate.lessThan=" + DEFAULT_PAYMENT_DATE);

        // Get all the paymentList where paymentDate less than or equals to UPDATED_PAYMENT_DATE
        defaultPaymentShouldBeFound("paymentDate.lessThan=" + UPDATED_PAYMENT_DATE);
    }


    @Test
    @Transactional
    public void getAllPaymentsByPaymentProviderIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentProvider equals to DEFAULT_PAYMENT_PROVIDER
        defaultPaymentShouldBeFound("paymentProvider.equals=" + DEFAULT_PAYMENT_PROVIDER);

        // Get all the paymentList where paymentProvider equals to UPDATED_PAYMENT_PROVIDER
        defaultPaymentShouldNotBeFound("paymentProvider.equals=" + UPDATED_PAYMENT_PROVIDER);
    }

    @Test
    @Transactional
    public void getAllPaymentsByPaymentProviderIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentProvider in DEFAULT_PAYMENT_PROVIDER or UPDATED_PAYMENT_PROVIDER
        defaultPaymentShouldBeFound("paymentProvider.in=" + DEFAULT_PAYMENT_PROVIDER + "," + UPDATED_PAYMENT_PROVIDER);

        // Get all the paymentList where paymentProvider equals to UPDATED_PAYMENT_PROVIDER
        defaultPaymentShouldNotBeFound("paymentProvider.in=" + UPDATED_PAYMENT_PROVIDER);
    }

    @Test
    @Transactional
    public void getAllPaymentsByPaymentProviderIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentProvider is not null
        defaultPaymentShouldBeFound("paymentProvider.specified=true");

        // Get all the paymentList where paymentProvider is null
        defaultPaymentShouldNotBeFound("paymentProvider.specified=false");
    }

    @Test
    @Transactional
    public void getAllPaymentsByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where amount equals to DEFAULT_AMOUNT
        defaultPaymentShouldBeFound("amount.equals=" + DEFAULT_AMOUNT);

        // Get all the paymentList where amount equals to UPDATED_AMOUNT
        defaultPaymentShouldNotBeFound("amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllPaymentsByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where amount in DEFAULT_AMOUNT or UPDATED_AMOUNT
        defaultPaymentShouldBeFound("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT);

        // Get all the paymentList where amount equals to UPDATED_AMOUNT
        defaultPaymentShouldNotBeFound("amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllPaymentsByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where amount is not null
        defaultPaymentShouldBeFound("amount.specified=true");

        // Get all the paymentList where amount is null
        defaultPaymentShouldNotBeFound("amount.specified=false");
    }

    @Test
    @Transactional
    public void getAllPaymentsByPaymentStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentStatus equals to DEFAULT_PAYMENT_STATUS
        defaultPaymentShouldBeFound("paymentStatus.equals=" + DEFAULT_PAYMENT_STATUS);

        // Get all the paymentList where paymentStatus equals to UPDATED_PAYMENT_STATUS
        defaultPaymentShouldNotBeFound("paymentStatus.equals=" + UPDATED_PAYMENT_STATUS);
    }

    @Test
    @Transactional
    public void getAllPaymentsByPaymentStatusIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentStatus in DEFAULT_PAYMENT_STATUS or UPDATED_PAYMENT_STATUS
        defaultPaymentShouldBeFound("paymentStatus.in=" + DEFAULT_PAYMENT_STATUS + "," + UPDATED_PAYMENT_STATUS);

        // Get all the paymentList where paymentStatus equals to UPDATED_PAYMENT_STATUS
        defaultPaymentShouldNotBeFound("paymentStatus.in=" + UPDATED_PAYMENT_STATUS);
    }

    @Test
    @Transactional
    public void getAllPaymentsByPaymentStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentStatus is not null
        defaultPaymentShouldBeFound("paymentStatus.specified=true");

        // Get all the paymentList where paymentStatus is null
        defaultPaymentShouldNotBeFound("paymentStatus.specified=false");
    }

    @Test
    @Transactional
    public void getAllPaymentsByCurencyIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where curency equals to DEFAULT_CURENCY
        defaultPaymentShouldBeFound("curency.equals=" + DEFAULT_CURENCY);

        // Get all the paymentList where curency equals to UPDATED_CURENCY
        defaultPaymentShouldNotBeFound("curency.equals=" + UPDATED_CURENCY);
    }

    @Test
    @Transactional
    public void getAllPaymentsByCurencyIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where curency in DEFAULT_CURENCY or UPDATED_CURENCY
        defaultPaymentShouldBeFound("curency.in=" + DEFAULT_CURENCY + "," + UPDATED_CURENCY);

        // Get all the paymentList where curency equals to UPDATED_CURENCY
        defaultPaymentShouldNotBeFound("curency.in=" + UPDATED_CURENCY);
    }

    @Test
    @Transactional
    public void getAllPaymentsByCurencyIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where curency is not null
        defaultPaymentShouldBeFound("curency.specified=true");

        // Get all the paymentList where curency is null
        defaultPaymentShouldNotBeFound("curency.specified=false");
    }

    @Test
    @Transactional
    public void getAllPaymentsByCustomerNameIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where customerName equals to DEFAULT_CUSTOMER_NAME
        defaultPaymentShouldBeFound("customerName.equals=" + DEFAULT_CUSTOMER_NAME);

        // Get all the paymentList where customerName equals to UPDATED_CUSTOMER_NAME
        defaultPaymentShouldNotBeFound("customerName.equals=" + UPDATED_CUSTOMER_NAME);
    }

    @Test
    @Transactional
    public void getAllPaymentsByCustomerNameIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where customerName in DEFAULT_CUSTOMER_NAME or UPDATED_CUSTOMER_NAME
        defaultPaymentShouldBeFound("customerName.in=" + DEFAULT_CUSTOMER_NAME + "," + UPDATED_CUSTOMER_NAME);

        // Get all the paymentList where customerName equals to UPDATED_CUSTOMER_NAME
        defaultPaymentShouldNotBeFound("customerName.in=" + UPDATED_CUSTOMER_NAME);
    }

    @Test
    @Transactional
    public void getAllPaymentsByCustomerNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where customerName is not null
        defaultPaymentShouldBeFound("customerName.specified=true");

        // Get all the paymentList where customerName is null
        defaultPaymentShouldNotBeFound("customerName.specified=false");
    }

    @Test
    @Transactional
    public void getAllPaymentsByShopIsEqualToSomething() throws Exception {
        // Initialize the database
        Shop shop = ShopResourceIntTest.createEntity(em);
        em.persist(shop);
        em.flush();
        payment.setShop(shop);
        paymentRepository.saveAndFlush(payment);
        Long shopId = shop.getId();

        // Get all the paymentList where shop equals to shopId
        defaultPaymentShouldBeFound("shopId.equals=" + shopId);

        // Get all the paymentList where shop equals to shopId + 1
        defaultPaymentShouldNotBeFound("shopId.equals=" + (shopId + 1));
    }


    @Test
    @Transactional
    public void getAllPaymentsByProcessedByIsEqualToSomething() throws Exception {
        // Initialize the database
        Profile processedBy = ProfileResourceIntTest.createEntity(em);
        em.persist(processedBy);
        em.flush();
        payment.setProcessedBy(processedBy);
        paymentRepository.saveAndFlush(payment);
        Long processedById = processedBy.getId();

        // Get all the paymentList where processedBy equals to processedById
        defaultPaymentShouldBeFound("processedById.equals=" + processedById);

        // Get all the paymentList where processedBy equals to processedById + 1
        defaultPaymentShouldNotBeFound("processedById.equals=" + (processedById + 1));
    }


    @Test
    @Transactional
    public void getAllPaymentsByPaymentMethodIsEqualToSomething() throws Exception {
        // Initialize the database
        PaymentMethod paymentMethod = PaymentMethodResourceIntTest.createEntity(em);
        em.persist(paymentMethod);
        em.flush();
        payment.setPaymentMethod(paymentMethod);
        paymentRepository.saveAndFlush(payment);
        Long paymentMethodId = paymentMethod.getId();

        // Get all the paymentList where paymentMethod equals to paymentMethodId
        defaultPaymentShouldBeFound("paymentMethodId.equals=" + paymentMethodId);

        // Get all the paymentList where paymentMethod equals to paymentMethodId + 1
        defaultPaymentShouldNotBeFound("paymentMethodId.equals=" + (paymentMethodId + 1));
    }


    @Test
    @Transactional
    public void getAllPaymentsByOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        Orders order = OrdersResourceIntTest.createEntity(em);
        em.persist(order);
        em.flush();
        payment.setOrder(order);
        paymentRepository.saveAndFlush(payment);
        Long orderId = order.getId();

        // Get all the paymentList where order equals to orderId
        defaultPaymentShouldBeFound("orderId.equals=" + orderId);

        // Get all the paymentList where order equals to orderId + 1
        defaultPaymentShouldNotBeFound("orderId.equals=" + (orderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultPaymentShouldBeFound(String filter) throws Exception {
        restPaymentMockMvc.perform(get("/api/payments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payment.getId().intValue())))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(sameInstant(DEFAULT_PAYMENT_DATE))))
            .andExpect(jsonPath("$.[*].paymentProvider").value(hasItem(DEFAULT_PAYMENT_PROVIDER.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].paymentStatus").value(hasItem(DEFAULT_PAYMENT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].curency").value(hasItem(DEFAULT_CURENCY.toString())))
            .andExpect(jsonPath("$.[*].customerName").value(hasItem(DEFAULT_CUSTOMER_NAME.toString())));

        // Check, that the count call also returns 1
        restPaymentMockMvc.perform(get("/api/payments/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultPaymentShouldNotBeFound(String filter) throws Exception {
        restPaymentMockMvc.perform(get("/api/payments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPaymentMockMvc.perform(get("/api/payments/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPayment() throws Exception {
        // Get the payment
        restPaymentMockMvc.perform(get("/api/payments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePayment() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();

        // Update the payment
        Payment updatedPayment = paymentRepository.findById(payment.getId()).get();
        // Disconnect from session so that the updates on updatedPayment are not directly saved in db
        em.detach(updatedPayment);
        updatedPayment
            .paymentDate(UPDATED_PAYMENT_DATE)
            .paymentProvider(UPDATED_PAYMENT_PROVIDER)
            .amount(UPDATED_AMOUNT)
            .paymentStatus(UPDATED_PAYMENT_STATUS)
            .curency(UPDATED_CURENCY)
            .customerName(UPDATED_CUSTOMER_NAME);
        PaymentDTO paymentDTO = paymentMapper.toDto(updatedPayment);

        restPaymentMockMvc.perform(put("/api/payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentDTO)))
            .andExpect(status().isOk());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
        Payment testPayment = paymentList.get(paymentList.size() - 1);
        assertThat(testPayment.getPaymentDate()).isEqualTo(UPDATED_PAYMENT_DATE);
        assertThat(testPayment.getPaymentProvider()).isEqualTo(UPDATED_PAYMENT_PROVIDER);
        assertThat(testPayment.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testPayment.getPaymentStatus()).isEqualTo(UPDATED_PAYMENT_STATUS);
        assertThat(testPayment.getCurency()).isEqualTo(UPDATED_CURENCY);
        assertThat(testPayment.getCustomerName()).isEqualTo(UPDATED_CUSTOMER_NAME);

        // Validate the Payment in Elasticsearch
        verify(mockPaymentSearchRepository, times(1)).save(testPayment);
    }

    @Test
    @Transactional
    public void updateNonExistingPayment() throws Exception {
        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();

        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentMockMvc.perform(put("/api/payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Payment in Elasticsearch
        verify(mockPaymentSearchRepository, times(0)).save(payment);
    }

    @Test
    @Transactional
    public void deletePayment() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        int databaseSizeBeforeDelete = paymentRepository.findAll().size();

        // Get the payment
        restPaymentMockMvc.perform(delete("/api/payments/{id}", payment.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Payment in Elasticsearch
        verify(mockPaymentSearchRepository, times(1)).deleteById(payment.getId());
    }

    @Test
    @Transactional
    public void searchPayment() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);
        when(mockPaymentSearchRepository.search(queryStringQuery("id:" + payment.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(payment), PageRequest.of(0, 1), 1));
        // Search the payment
        restPaymentMockMvc.perform(get("/api/_search/payments?query=id:" + payment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payment.getId().intValue())))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(sameInstant(DEFAULT_PAYMENT_DATE))))
            .andExpect(jsonPath("$.[*].paymentProvider").value(hasItem(DEFAULT_PAYMENT_PROVIDER)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].paymentStatus").value(hasItem(DEFAULT_PAYMENT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].curency").value(hasItem(DEFAULT_CURENCY)))
            .andExpect(jsonPath("$.[*].customerName").value(hasItem(DEFAULT_CUSTOMER_NAME)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Payment.class);
        Payment payment1 = new Payment();
        payment1.setId(1L);
        Payment payment2 = new Payment();
        payment2.setId(payment1.getId());
        assertThat(payment1).isEqualTo(payment2);
        payment2.setId(2L);
        assertThat(payment1).isNotEqualTo(payment2);
        payment1.setId(null);
        assertThat(payment1).isNotEqualTo(payment2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentDTO.class);
        PaymentDTO paymentDTO1 = new PaymentDTO();
        paymentDTO1.setId(1L);
        PaymentDTO paymentDTO2 = new PaymentDTO();
        assertThat(paymentDTO1).isNotEqualTo(paymentDTO2);
        paymentDTO2.setId(paymentDTO1.getId());
        assertThat(paymentDTO1).isEqualTo(paymentDTO2);
        paymentDTO2.setId(2L);
        assertThat(paymentDTO1).isNotEqualTo(paymentDTO2);
        paymentDTO1.setId(null);
        assertThat(paymentDTO1).isNotEqualTo(paymentDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(paymentMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(paymentMapper.fromId(null)).isNull();
    }
}
