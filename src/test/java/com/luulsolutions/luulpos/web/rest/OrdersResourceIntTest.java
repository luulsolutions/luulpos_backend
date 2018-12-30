package com.luulsolutions.luulpos.web.rest;

import com.luulsolutions.luulpos.LuulposApp;

import com.luulsolutions.luulpos.domain.Orders;
import com.luulsolutions.luulpos.domain.OrdersLine;
import com.luulsolutions.luulpos.domain.PaymentMethod;
import com.luulsolutions.luulpos.domain.Profile;
import com.luulsolutions.luulpos.domain.Profile;
import com.luulsolutions.luulpos.domain.ShopDevice;
import com.luulsolutions.luulpos.domain.SectionTable;
import com.luulsolutions.luulpos.domain.Shop;
import com.luulsolutions.luulpos.repository.OrdersRepository;
import com.luulsolutions.luulpos.repository.search.OrdersSearchRepository;
import com.luulsolutions.luulpos.service.OrdersService;
import com.luulsolutions.luulpos.service.dto.OrdersDTO;
import com.luulsolutions.luulpos.service.mapper.OrdersMapper;
import com.luulsolutions.luulpos.web.rest.errors.ExceptionTranslator;
import com.luulsolutions.luulpos.service.dto.OrdersCriteria;
import com.luulsolutions.luulpos.service.OrdersQueryService;

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

import com.luulsolutions.luulpos.domain.enumeration.OrderStatus;
/**
 * Test class for the OrdersResource REST controller.
 *
 * @see OrdersResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LuulposApp.class)
public class OrdersResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CUSTOMER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_NAME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_TOTAL_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_PRICE = new BigDecimal(2);

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final Float DEFAULT_DISCOUNT_PERCENTAGE = 1F;
    private static final Float UPDATED_DISCOUNT_PERCENTAGE = 2F;

    private static final BigDecimal DEFAULT_DISCOUNT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_DISCOUNT_AMOUNT = new BigDecimal(2);

    private static final Float DEFAULT_TAX_PERCENTAGE = 1F;
    private static final Float UPDATED_TAX_PERCENTAGE = 2F;

    private static final BigDecimal DEFAULT_TAX_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TAX_AMOUNT = new BigDecimal(2);

    private static final OrderStatus DEFAULT_ORDER_STATUS = OrderStatus.INCOMPLETE;
    private static final OrderStatus UPDATED_ORDER_STATUS = OrderStatus.COMPLETED;

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_ORDER_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_ORDER_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_IS_VARIANT_ORDER = false;
    private static final Boolean UPDATED_IS_VARIANT_ORDER = true;

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private OrdersService ordersService;

    /**
     * This repository is mocked in the com.luulsolutions.luulpos.repository.search test package.
     *
     * @see com.luulsolutions.luulpos.repository.search.OrdersSearchRepositoryMockConfiguration
     */
    @Autowired
    private OrdersSearchRepository mockOrdersSearchRepository;

    @Autowired
    private OrdersQueryService ordersQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restOrdersMockMvc;

    private Orders orders;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OrdersResource ordersResource = new OrdersResource(ordersService, ordersQueryService);
        this.restOrdersMockMvc = MockMvcBuilders.standaloneSetup(ordersResource)
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
    public static Orders createEntity(EntityManager em) {
        Orders orders = new Orders()
            .description(DEFAULT_DESCRIPTION)
            .customerName(DEFAULT_CUSTOMER_NAME)
            .totalPrice(DEFAULT_TOTAL_PRICE)
            .quantity(DEFAULT_QUANTITY)
            .discountPercentage(DEFAULT_DISCOUNT_PERCENTAGE)
            .discountAmount(DEFAULT_DISCOUNT_AMOUNT)
            .taxPercentage(DEFAULT_TAX_PERCENTAGE)
            .taxAmount(DEFAULT_TAX_AMOUNT)
            .orderStatus(DEFAULT_ORDER_STATUS)
            .note(DEFAULT_NOTE)
            .orderDate(DEFAULT_ORDER_DATE)
            .isVariantOrder(DEFAULT_IS_VARIANT_ORDER);
        return orders;
    }

    @Before
    public void initTest() {
        orders = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrders() throws Exception {
        int databaseSizeBeforeCreate = ordersRepository.findAll().size();

        // Create the Orders
        OrdersDTO ordersDTO = ordersMapper.toDto(orders);
        restOrdersMockMvc.perform(post("/api/orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ordersDTO)))
            .andExpect(status().isCreated());

        // Validate the Orders in the database
        List<Orders> ordersList = ordersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeCreate + 1);
        Orders testOrders = ordersList.get(ordersList.size() - 1);
        assertThat(testOrders.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testOrders.getCustomerName()).isEqualTo(DEFAULT_CUSTOMER_NAME);
        assertThat(testOrders.getTotalPrice()).isEqualTo(DEFAULT_TOTAL_PRICE);
        assertThat(testOrders.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testOrders.getDiscountPercentage()).isEqualTo(DEFAULT_DISCOUNT_PERCENTAGE);
        assertThat(testOrders.getDiscountAmount()).isEqualTo(DEFAULT_DISCOUNT_AMOUNT);
        assertThat(testOrders.getTaxPercentage()).isEqualTo(DEFAULT_TAX_PERCENTAGE);
        assertThat(testOrders.getTaxAmount()).isEqualTo(DEFAULT_TAX_AMOUNT);
        assertThat(testOrders.getOrderStatus()).isEqualTo(DEFAULT_ORDER_STATUS);
        assertThat(testOrders.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testOrders.getOrderDate()).isEqualTo(DEFAULT_ORDER_DATE);
        assertThat(testOrders.isIsVariantOrder()).isEqualTo(DEFAULT_IS_VARIANT_ORDER);

        // Validate the Orders in Elasticsearch
        verify(mockOrdersSearchRepository, times(1)).save(testOrders);
    }

    @Test
    @Transactional
    public void createOrdersWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ordersRepository.findAll().size();

        // Create the Orders with an existing ID
        orders.setId(1L);
        OrdersDTO ordersDTO = ordersMapper.toDto(orders);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrdersMockMvc.perform(post("/api/orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ordersDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Orders in the database
        List<Orders> ordersList = ordersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeCreate);

        // Validate the Orders in Elasticsearch
        verify(mockOrdersSearchRepository, times(0)).save(orders);
    }

    @Test
    @Transactional
    public void checkTotalPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordersRepository.findAll().size();
        // set the field null
        orders.setTotalPrice(null);

        // Create the Orders, which fails.
        OrdersDTO ordersDTO = ordersMapper.toDto(orders);

        restOrdersMockMvc.perform(post("/api/orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ordersDTO)))
            .andExpect(status().isBadRequest());

        List<Orders> ordersList = ordersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordersRepository.findAll().size();
        // set the field null
        orders.setQuantity(null);

        // Create the Orders, which fails.
        OrdersDTO ordersDTO = ordersMapper.toDto(orders);

        restOrdersMockMvc.perform(post("/api/orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ordersDTO)))
            .andExpect(status().isBadRequest());

        List<Orders> ordersList = ordersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOrders() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList
        restOrdersMockMvc.perform(get("/api/orders?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orders.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].customerName").value(hasItem(DEFAULT_CUSTOMER_NAME.toString())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].discountPercentage").value(hasItem(DEFAULT_DISCOUNT_PERCENTAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].discountAmount").value(hasItem(DEFAULT_DISCOUNT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].taxPercentage").value(hasItem(DEFAULT_TAX_PERCENTAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].taxAmount").value(hasItem(DEFAULT_TAX_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].orderStatus").value(hasItem(DEFAULT_ORDER_STATUS.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.toString())))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(sameInstant(DEFAULT_ORDER_DATE))))
            .andExpect(jsonPath("$.[*].isVariantOrder").value(hasItem(DEFAULT_IS_VARIANT_ORDER.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getOrders() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get the orders
        restOrdersMockMvc.perform(get("/api/orders/{id}", orders.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(orders.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.customerName").value(DEFAULT_CUSTOMER_NAME.toString()))
            .andExpect(jsonPath("$.totalPrice").value(DEFAULT_TOTAL_PRICE.intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.discountPercentage").value(DEFAULT_DISCOUNT_PERCENTAGE.doubleValue()))
            .andExpect(jsonPath("$.discountAmount").value(DEFAULT_DISCOUNT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.taxPercentage").value(DEFAULT_TAX_PERCENTAGE.doubleValue()))
            .andExpect(jsonPath("$.taxAmount").value(DEFAULT_TAX_AMOUNT.intValue()))
            .andExpect(jsonPath("$.orderStatus").value(DEFAULT_ORDER_STATUS.toString()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE.toString()))
            .andExpect(jsonPath("$.orderDate").value(sameInstant(DEFAULT_ORDER_DATE)))
            .andExpect(jsonPath("$.isVariantOrder").value(DEFAULT_IS_VARIANT_ORDER.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllOrdersByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where description equals to DEFAULT_DESCRIPTION
        defaultOrdersShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the ordersList where description equals to UPDATED_DESCRIPTION
        defaultOrdersShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllOrdersByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultOrdersShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the ordersList where description equals to UPDATED_DESCRIPTION
        defaultOrdersShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllOrdersByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where description is not null
        defaultOrdersShouldBeFound("description.specified=true");

        // Get all the ordersList where description is null
        defaultOrdersShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrdersByCustomerNameIsEqualToSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where customerName equals to DEFAULT_CUSTOMER_NAME
        defaultOrdersShouldBeFound("customerName.equals=" + DEFAULT_CUSTOMER_NAME);

        // Get all the ordersList where customerName equals to UPDATED_CUSTOMER_NAME
        defaultOrdersShouldNotBeFound("customerName.equals=" + UPDATED_CUSTOMER_NAME);
    }

    @Test
    @Transactional
    public void getAllOrdersByCustomerNameIsInShouldWork() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where customerName in DEFAULT_CUSTOMER_NAME or UPDATED_CUSTOMER_NAME
        defaultOrdersShouldBeFound("customerName.in=" + DEFAULT_CUSTOMER_NAME + "," + UPDATED_CUSTOMER_NAME);

        // Get all the ordersList where customerName equals to UPDATED_CUSTOMER_NAME
        defaultOrdersShouldNotBeFound("customerName.in=" + UPDATED_CUSTOMER_NAME);
    }

    @Test
    @Transactional
    public void getAllOrdersByCustomerNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where customerName is not null
        defaultOrdersShouldBeFound("customerName.specified=true");

        // Get all the ordersList where customerName is null
        defaultOrdersShouldNotBeFound("customerName.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrdersByTotalPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where totalPrice equals to DEFAULT_TOTAL_PRICE
        defaultOrdersShouldBeFound("totalPrice.equals=" + DEFAULT_TOTAL_PRICE);

        // Get all the ordersList where totalPrice equals to UPDATED_TOTAL_PRICE
        defaultOrdersShouldNotBeFound("totalPrice.equals=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    public void getAllOrdersByTotalPriceIsInShouldWork() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where totalPrice in DEFAULT_TOTAL_PRICE or UPDATED_TOTAL_PRICE
        defaultOrdersShouldBeFound("totalPrice.in=" + DEFAULT_TOTAL_PRICE + "," + UPDATED_TOTAL_PRICE);

        // Get all the ordersList where totalPrice equals to UPDATED_TOTAL_PRICE
        defaultOrdersShouldNotBeFound("totalPrice.in=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    public void getAllOrdersByTotalPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where totalPrice is not null
        defaultOrdersShouldBeFound("totalPrice.specified=true");

        // Get all the ordersList where totalPrice is null
        defaultOrdersShouldNotBeFound("totalPrice.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrdersByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where quantity equals to DEFAULT_QUANTITY
        defaultOrdersShouldBeFound("quantity.equals=" + DEFAULT_QUANTITY);

        // Get all the ordersList where quantity equals to UPDATED_QUANTITY
        defaultOrdersShouldNotBeFound("quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllOrdersByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where quantity in DEFAULT_QUANTITY or UPDATED_QUANTITY
        defaultOrdersShouldBeFound("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY);

        // Get all the ordersList where quantity equals to UPDATED_QUANTITY
        defaultOrdersShouldNotBeFound("quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllOrdersByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where quantity is not null
        defaultOrdersShouldBeFound("quantity.specified=true");

        // Get all the ordersList where quantity is null
        defaultOrdersShouldNotBeFound("quantity.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrdersByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where quantity greater than or equals to DEFAULT_QUANTITY
        defaultOrdersShouldBeFound("quantity.greaterOrEqualThan=" + DEFAULT_QUANTITY);

        // Get all the ordersList where quantity greater than or equals to UPDATED_QUANTITY
        defaultOrdersShouldNotBeFound("quantity.greaterOrEqualThan=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllOrdersByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where quantity less than or equals to DEFAULT_QUANTITY
        defaultOrdersShouldNotBeFound("quantity.lessThan=" + DEFAULT_QUANTITY);

        // Get all the ordersList where quantity less than or equals to UPDATED_QUANTITY
        defaultOrdersShouldBeFound("quantity.lessThan=" + UPDATED_QUANTITY);
    }


    @Test
    @Transactional
    public void getAllOrdersByDiscountPercentageIsEqualToSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where discountPercentage equals to DEFAULT_DISCOUNT_PERCENTAGE
        defaultOrdersShouldBeFound("discountPercentage.equals=" + DEFAULT_DISCOUNT_PERCENTAGE);

        // Get all the ordersList where discountPercentage equals to UPDATED_DISCOUNT_PERCENTAGE
        defaultOrdersShouldNotBeFound("discountPercentage.equals=" + UPDATED_DISCOUNT_PERCENTAGE);
    }

    @Test
    @Transactional
    public void getAllOrdersByDiscountPercentageIsInShouldWork() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where discountPercentage in DEFAULT_DISCOUNT_PERCENTAGE or UPDATED_DISCOUNT_PERCENTAGE
        defaultOrdersShouldBeFound("discountPercentage.in=" + DEFAULT_DISCOUNT_PERCENTAGE + "," + UPDATED_DISCOUNT_PERCENTAGE);

        // Get all the ordersList where discountPercentage equals to UPDATED_DISCOUNT_PERCENTAGE
        defaultOrdersShouldNotBeFound("discountPercentage.in=" + UPDATED_DISCOUNT_PERCENTAGE);
    }

    @Test
    @Transactional
    public void getAllOrdersByDiscountPercentageIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where discountPercentage is not null
        defaultOrdersShouldBeFound("discountPercentage.specified=true");

        // Get all the ordersList where discountPercentage is null
        defaultOrdersShouldNotBeFound("discountPercentage.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrdersByDiscountAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where discountAmount equals to DEFAULT_DISCOUNT_AMOUNT
        defaultOrdersShouldBeFound("discountAmount.equals=" + DEFAULT_DISCOUNT_AMOUNT);

        // Get all the ordersList where discountAmount equals to UPDATED_DISCOUNT_AMOUNT
        defaultOrdersShouldNotBeFound("discountAmount.equals=" + UPDATED_DISCOUNT_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllOrdersByDiscountAmountIsInShouldWork() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where discountAmount in DEFAULT_DISCOUNT_AMOUNT or UPDATED_DISCOUNT_AMOUNT
        defaultOrdersShouldBeFound("discountAmount.in=" + DEFAULT_DISCOUNT_AMOUNT + "," + UPDATED_DISCOUNT_AMOUNT);

        // Get all the ordersList where discountAmount equals to UPDATED_DISCOUNT_AMOUNT
        defaultOrdersShouldNotBeFound("discountAmount.in=" + UPDATED_DISCOUNT_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllOrdersByDiscountAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where discountAmount is not null
        defaultOrdersShouldBeFound("discountAmount.specified=true");

        // Get all the ordersList where discountAmount is null
        defaultOrdersShouldNotBeFound("discountAmount.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrdersByTaxPercentageIsEqualToSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where taxPercentage equals to DEFAULT_TAX_PERCENTAGE
        defaultOrdersShouldBeFound("taxPercentage.equals=" + DEFAULT_TAX_PERCENTAGE);

        // Get all the ordersList where taxPercentage equals to UPDATED_TAX_PERCENTAGE
        defaultOrdersShouldNotBeFound("taxPercentage.equals=" + UPDATED_TAX_PERCENTAGE);
    }

    @Test
    @Transactional
    public void getAllOrdersByTaxPercentageIsInShouldWork() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where taxPercentage in DEFAULT_TAX_PERCENTAGE or UPDATED_TAX_PERCENTAGE
        defaultOrdersShouldBeFound("taxPercentage.in=" + DEFAULT_TAX_PERCENTAGE + "," + UPDATED_TAX_PERCENTAGE);

        // Get all the ordersList where taxPercentage equals to UPDATED_TAX_PERCENTAGE
        defaultOrdersShouldNotBeFound("taxPercentage.in=" + UPDATED_TAX_PERCENTAGE);
    }

    @Test
    @Transactional
    public void getAllOrdersByTaxPercentageIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where taxPercentage is not null
        defaultOrdersShouldBeFound("taxPercentage.specified=true");

        // Get all the ordersList where taxPercentage is null
        defaultOrdersShouldNotBeFound("taxPercentage.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrdersByTaxAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where taxAmount equals to DEFAULT_TAX_AMOUNT
        defaultOrdersShouldBeFound("taxAmount.equals=" + DEFAULT_TAX_AMOUNT);

        // Get all the ordersList where taxAmount equals to UPDATED_TAX_AMOUNT
        defaultOrdersShouldNotBeFound("taxAmount.equals=" + UPDATED_TAX_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllOrdersByTaxAmountIsInShouldWork() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where taxAmount in DEFAULT_TAX_AMOUNT or UPDATED_TAX_AMOUNT
        defaultOrdersShouldBeFound("taxAmount.in=" + DEFAULT_TAX_AMOUNT + "," + UPDATED_TAX_AMOUNT);

        // Get all the ordersList where taxAmount equals to UPDATED_TAX_AMOUNT
        defaultOrdersShouldNotBeFound("taxAmount.in=" + UPDATED_TAX_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllOrdersByTaxAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where taxAmount is not null
        defaultOrdersShouldBeFound("taxAmount.specified=true");

        // Get all the ordersList where taxAmount is null
        defaultOrdersShouldNotBeFound("taxAmount.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrdersByOrderStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where orderStatus equals to DEFAULT_ORDER_STATUS
        defaultOrdersShouldBeFound("orderStatus.equals=" + DEFAULT_ORDER_STATUS);

        // Get all the ordersList where orderStatus equals to UPDATED_ORDER_STATUS
        defaultOrdersShouldNotBeFound("orderStatus.equals=" + UPDATED_ORDER_STATUS);
    }

    @Test
    @Transactional
    public void getAllOrdersByOrderStatusIsInShouldWork() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where orderStatus in DEFAULT_ORDER_STATUS or UPDATED_ORDER_STATUS
        defaultOrdersShouldBeFound("orderStatus.in=" + DEFAULT_ORDER_STATUS + "," + UPDATED_ORDER_STATUS);

        // Get all the ordersList where orderStatus equals to UPDATED_ORDER_STATUS
        defaultOrdersShouldNotBeFound("orderStatus.in=" + UPDATED_ORDER_STATUS);
    }

    @Test
    @Transactional
    public void getAllOrdersByOrderStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where orderStatus is not null
        defaultOrdersShouldBeFound("orderStatus.specified=true");

        // Get all the ordersList where orderStatus is null
        defaultOrdersShouldNotBeFound("orderStatus.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrdersByNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where note equals to DEFAULT_NOTE
        defaultOrdersShouldBeFound("note.equals=" + DEFAULT_NOTE);

        // Get all the ordersList where note equals to UPDATED_NOTE
        defaultOrdersShouldNotBeFound("note.equals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllOrdersByNoteIsInShouldWork() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where note in DEFAULT_NOTE or UPDATED_NOTE
        defaultOrdersShouldBeFound("note.in=" + DEFAULT_NOTE + "," + UPDATED_NOTE);

        // Get all the ordersList where note equals to UPDATED_NOTE
        defaultOrdersShouldNotBeFound("note.in=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllOrdersByNoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where note is not null
        defaultOrdersShouldBeFound("note.specified=true");

        // Get all the ordersList where note is null
        defaultOrdersShouldNotBeFound("note.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrdersByOrderDateIsEqualToSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where orderDate equals to DEFAULT_ORDER_DATE
        defaultOrdersShouldBeFound("orderDate.equals=" + DEFAULT_ORDER_DATE);

        // Get all the ordersList where orderDate equals to UPDATED_ORDER_DATE
        defaultOrdersShouldNotBeFound("orderDate.equals=" + UPDATED_ORDER_DATE);
    }

    @Test
    @Transactional
    public void getAllOrdersByOrderDateIsInShouldWork() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where orderDate in DEFAULT_ORDER_DATE or UPDATED_ORDER_DATE
        defaultOrdersShouldBeFound("orderDate.in=" + DEFAULT_ORDER_DATE + "," + UPDATED_ORDER_DATE);

        // Get all the ordersList where orderDate equals to UPDATED_ORDER_DATE
        defaultOrdersShouldNotBeFound("orderDate.in=" + UPDATED_ORDER_DATE);
    }

    @Test
    @Transactional
    public void getAllOrdersByOrderDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where orderDate is not null
        defaultOrdersShouldBeFound("orderDate.specified=true");

        // Get all the ordersList where orderDate is null
        defaultOrdersShouldNotBeFound("orderDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrdersByOrderDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where orderDate greater than or equals to DEFAULT_ORDER_DATE
        defaultOrdersShouldBeFound("orderDate.greaterOrEqualThan=" + DEFAULT_ORDER_DATE);

        // Get all the ordersList where orderDate greater than or equals to UPDATED_ORDER_DATE
        defaultOrdersShouldNotBeFound("orderDate.greaterOrEqualThan=" + UPDATED_ORDER_DATE);
    }

    @Test
    @Transactional
    public void getAllOrdersByOrderDateIsLessThanSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where orderDate less than or equals to DEFAULT_ORDER_DATE
        defaultOrdersShouldNotBeFound("orderDate.lessThan=" + DEFAULT_ORDER_DATE);

        // Get all the ordersList where orderDate less than or equals to UPDATED_ORDER_DATE
        defaultOrdersShouldBeFound("orderDate.lessThan=" + UPDATED_ORDER_DATE);
    }


    @Test
    @Transactional
    public void getAllOrdersByIsVariantOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where isVariantOrder equals to DEFAULT_IS_VARIANT_ORDER
        defaultOrdersShouldBeFound("isVariantOrder.equals=" + DEFAULT_IS_VARIANT_ORDER);

        // Get all the ordersList where isVariantOrder equals to UPDATED_IS_VARIANT_ORDER
        defaultOrdersShouldNotBeFound("isVariantOrder.equals=" + UPDATED_IS_VARIANT_ORDER);
    }

    @Test
    @Transactional
    public void getAllOrdersByIsVariantOrderIsInShouldWork() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where isVariantOrder in DEFAULT_IS_VARIANT_ORDER or UPDATED_IS_VARIANT_ORDER
        defaultOrdersShouldBeFound("isVariantOrder.in=" + DEFAULT_IS_VARIANT_ORDER + "," + UPDATED_IS_VARIANT_ORDER);

        // Get all the ordersList where isVariantOrder equals to UPDATED_IS_VARIANT_ORDER
        defaultOrdersShouldNotBeFound("isVariantOrder.in=" + UPDATED_IS_VARIANT_ORDER);
    }

    @Test
    @Transactional
    public void getAllOrdersByIsVariantOrderIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where isVariantOrder is not null
        defaultOrdersShouldBeFound("isVariantOrder.specified=true");

        // Get all the ordersList where isVariantOrder is null
        defaultOrdersShouldNotBeFound("isVariantOrder.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrdersByOrdersLinesIsEqualToSomething() throws Exception {
        // Initialize the database
        OrdersLine ordersLines = OrdersLineResourceIntTest.createEntity(em);
        em.persist(ordersLines);
        em.flush();
        orders.addOrdersLines(ordersLines);
        ordersRepository.saveAndFlush(orders);
        Long ordersLinesId = ordersLines.getId();

        // Get all the ordersList where ordersLines equals to ordersLinesId
        defaultOrdersShouldBeFound("ordersLinesId.equals=" + ordersLinesId);

        // Get all the ordersList where ordersLines equals to ordersLinesId + 1
        defaultOrdersShouldNotBeFound("ordersLinesId.equals=" + (ordersLinesId + 1));
    }


    @Test
    @Transactional
    public void getAllOrdersByPaymentMethodIsEqualToSomething() throws Exception {
        // Initialize the database
        PaymentMethod paymentMethod = PaymentMethodResourceIntTest.createEntity(em);
        em.persist(paymentMethod);
        em.flush();
        orders.setPaymentMethod(paymentMethod);
        ordersRepository.saveAndFlush(orders);
        Long paymentMethodId = paymentMethod.getId();

        // Get all the ordersList where paymentMethod equals to paymentMethodId
        defaultOrdersShouldBeFound("paymentMethodId.equals=" + paymentMethodId);

        // Get all the ordersList where paymentMethod equals to paymentMethodId + 1
        defaultOrdersShouldNotBeFound("paymentMethodId.equals=" + (paymentMethodId + 1));
    }


    @Test
    @Transactional
    public void getAllOrdersBySoldByIsEqualToSomething() throws Exception {
        // Initialize the database
        Profile soldBy = ProfileResourceIntTest.createEntity(em);
        em.persist(soldBy);
        em.flush();
        orders.setSoldBy(soldBy);
        ordersRepository.saveAndFlush(orders);
        Long soldById = soldBy.getId();

        // Get all the ordersList where soldBy equals to soldById
        defaultOrdersShouldBeFound("soldById.equals=" + soldById);

        // Get all the ordersList where soldBy equals to soldById + 1
        defaultOrdersShouldNotBeFound("soldById.equals=" + (soldById + 1));
    }


    @Test
    @Transactional
    public void getAllOrdersByPreparedByIsEqualToSomething() throws Exception {
        // Initialize the database
        Profile preparedBy = ProfileResourceIntTest.createEntity(em);
        em.persist(preparedBy);
        em.flush();
        orders.setPreparedBy(preparedBy);
        ordersRepository.saveAndFlush(orders);
        Long preparedById = preparedBy.getId();

        // Get all the ordersList where preparedBy equals to preparedById
        defaultOrdersShouldBeFound("preparedById.equals=" + preparedById);

        // Get all the ordersList where preparedBy equals to preparedById + 1
        defaultOrdersShouldNotBeFound("preparedById.equals=" + (preparedById + 1));
    }


    @Test
    @Transactional
    public void getAllOrdersByShopDeviceIsEqualToSomething() throws Exception {
        // Initialize the database
        ShopDevice shopDevice = ShopDeviceResourceIntTest.createEntity(em);
        em.persist(shopDevice);
        em.flush();
        orders.setShopDevice(shopDevice);
        ordersRepository.saveAndFlush(orders);
        Long shopDeviceId = shopDevice.getId();

        // Get all the ordersList where shopDevice equals to shopDeviceId
        defaultOrdersShouldBeFound("shopDeviceId.equals=" + shopDeviceId);

        // Get all the ordersList where shopDevice equals to shopDeviceId + 1
        defaultOrdersShouldNotBeFound("shopDeviceId.equals=" + (shopDeviceId + 1));
    }


    @Test
    @Transactional
    public void getAllOrdersBySectionTableIsEqualToSomething() throws Exception {
        // Initialize the database
        SectionTable sectionTable = SectionTableResourceIntTest.createEntity(em);
        em.persist(sectionTable);
        em.flush();
        orders.setSectionTable(sectionTable);
        ordersRepository.saveAndFlush(orders);
        Long sectionTableId = sectionTable.getId();

        // Get all the ordersList where sectionTable equals to sectionTableId
        defaultOrdersShouldBeFound("sectionTableId.equals=" + sectionTableId);

        // Get all the ordersList where sectionTable equals to sectionTableId + 1
        defaultOrdersShouldNotBeFound("sectionTableId.equals=" + (sectionTableId + 1));
    }


    @Test
    @Transactional
    public void getAllOrdersByShopIsEqualToSomething() throws Exception {
        // Initialize the database
        Shop shop = ShopResourceIntTest.createEntity(em);
        em.persist(shop);
        em.flush();
        orders.setShop(shop);
        ordersRepository.saveAndFlush(orders);
        Long shopId = shop.getId();

        // Get all the ordersList where shop equals to shopId
        defaultOrdersShouldBeFound("shopId.equals=" + shopId);

        // Get all the ordersList where shop equals to shopId + 1
        defaultOrdersShouldNotBeFound("shopId.equals=" + (shopId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultOrdersShouldBeFound(String filter) throws Exception {
        restOrdersMockMvc.perform(get("/api/orders?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orders.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].customerName").value(hasItem(DEFAULT_CUSTOMER_NAME.toString())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].discountPercentage").value(hasItem(DEFAULT_DISCOUNT_PERCENTAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].discountAmount").value(hasItem(DEFAULT_DISCOUNT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].taxPercentage").value(hasItem(DEFAULT_TAX_PERCENTAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].taxAmount").value(hasItem(DEFAULT_TAX_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].orderStatus").value(hasItem(DEFAULT_ORDER_STATUS.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.toString())))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(sameInstant(DEFAULT_ORDER_DATE))))
            .andExpect(jsonPath("$.[*].isVariantOrder").value(hasItem(DEFAULT_IS_VARIANT_ORDER.booleanValue())));

        // Check, that the count call also returns 1
        restOrdersMockMvc.perform(get("/api/orders/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultOrdersShouldNotBeFound(String filter) throws Exception {
        restOrdersMockMvc.perform(get("/api/orders?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrdersMockMvc.perform(get("/api/orders/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingOrders() throws Exception {
        // Get the orders
        restOrdersMockMvc.perform(get("/api/orders/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrders() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        int databaseSizeBeforeUpdate = ordersRepository.findAll().size();

        // Update the orders
        Orders updatedOrders = ordersRepository.findById(orders.getId()).get();
        // Disconnect from session so that the updates on updatedOrders are not directly saved in db
        em.detach(updatedOrders);
        updatedOrders
            .description(UPDATED_DESCRIPTION)
            .customerName(UPDATED_CUSTOMER_NAME)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .quantity(UPDATED_QUANTITY)
            .discountPercentage(UPDATED_DISCOUNT_PERCENTAGE)
            .discountAmount(UPDATED_DISCOUNT_AMOUNT)
            .taxPercentage(UPDATED_TAX_PERCENTAGE)
            .taxAmount(UPDATED_TAX_AMOUNT)
            .orderStatus(UPDATED_ORDER_STATUS)
            .note(UPDATED_NOTE)
            .orderDate(UPDATED_ORDER_DATE)
            .isVariantOrder(UPDATED_IS_VARIANT_ORDER);
        OrdersDTO ordersDTO = ordersMapper.toDto(updatedOrders);

        restOrdersMockMvc.perform(put("/api/orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ordersDTO)))
            .andExpect(status().isOk());

        // Validate the Orders in the database
        List<Orders> ordersList = ordersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeUpdate);
        Orders testOrders = ordersList.get(ordersList.size() - 1);
        assertThat(testOrders.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testOrders.getCustomerName()).isEqualTo(UPDATED_CUSTOMER_NAME);
        assertThat(testOrders.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);
        assertThat(testOrders.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testOrders.getDiscountPercentage()).isEqualTo(UPDATED_DISCOUNT_PERCENTAGE);
        assertThat(testOrders.getDiscountAmount()).isEqualTo(UPDATED_DISCOUNT_AMOUNT);
        assertThat(testOrders.getTaxPercentage()).isEqualTo(UPDATED_TAX_PERCENTAGE);
        assertThat(testOrders.getTaxAmount()).isEqualTo(UPDATED_TAX_AMOUNT);
        assertThat(testOrders.getOrderStatus()).isEqualTo(UPDATED_ORDER_STATUS);
        assertThat(testOrders.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testOrders.getOrderDate()).isEqualTo(UPDATED_ORDER_DATE);
        assertThat(testOrders.isIsVariantOrder()).isEqualTo(UPDATED_IS_VARIANT_ORDER);

        // Validate the Orders in Elasticsearch
        verify(mockOrdersSearchRepository, times(1)).save(testOrders);
    }

    @Test
    @Transactional
    public void updateNonExistingOrders() throws Exception {
        int databaseSizeBeforeUpdate = ordersRepository.findAll().size();

        // Create the Orders
        OrdersDTO ordersDTO = ordersMapper.toDto(orders);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdersMockMvc.perform(put("/api/orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ordersDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Orders in the database
        List<Orders> ordersList = ordersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Orders in Elasticsearch
        verify(mockOrdersSearchRepository, times(0)).save(orders);
    }

    @Test
    @Transactional
    public void deleteOrders() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        int databaseSizeBeforeDelete = ordersRepository.findAll().size();

        // Get the orders
        restOrdersMockMvc.perform(delete("/api/orders/{id}", orders.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Orders> ordersList = ordersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Orders in Elasticsearch
        verify(mockOrdersSearchRepository, times(1)).deleteById(orders.getId());
    }

    @Test
    @Transactional
    public void searchOrders() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);
        when(mockOrdersSearchRepository.search(queryStringQuery("id:" + orders.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(orders), PageRequest.of(0, 1), 1));
        // Search the orders
        restOrdersMockMvc.perform(get("/api/_search/orders?query=id:" + orders.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orders.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].customerName").value(hasItem(DEFAULT_CUSTOMER_NAME)))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].discountPercentage").value(hasItem(DEFAULT_DISCOUNT_PERCENTAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].discountAmount").value(hasItem(DEFAULT_DISCOUNT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].taxPercentage").value(hasItem(DEFAULT_TAX_PERCENTAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].taxAmount").value(hasItem(DEFAULT_TAX_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].orderStatus").value(hasItem(DEFAULT_ORDER_STATUS.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(sameInstant(DEFAULT_ORDER_DATE))))
            .andExpect(jsonPath("$.[*].isVariantOrder").value(hasItem(DEFAULT_IS_VARIANT_ORDER.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Orders.class);
        Orders orders1 = new Orders();
        orders1.setId(1L);
        Orders orders2 = new Orders();
        orders2.setId(orders1.getId());
        assertThat(orders1).isEqualTo(orders2);
        orders2.setId(2L);
        assertThat(orders1).isNotEqualTo(orders2);
        orders1.setId(null);
        assertThat(orders1).isNotEqualTo(orders2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrdersDTO.class);
        OrdersDTO ordersDTO1 = new OrdersDTO();
        ordersDTO1.setId(1L);
        OrdersDTO ordersDTO2 = new OrdersDTO();
        assertThat(ordersDTO1).isNotEqualTo(ordersDTO2);
        ordersDTO2.setId(ordersDTO1.getId());
        assertThat(ordersDTO1).isEqualTo(ordersDTO2);
        ordersDTO2.setId(2L);
        assertThat(ordersDTO1).isNotEqualTo(ordersDTO2);
        ordersDTO1.setId(null);
        assertThat(ordersDTO1).isNotEqualTo(ordersDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(ordersMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(ordersMapper.fromId(null)).isNull();
    }
}
