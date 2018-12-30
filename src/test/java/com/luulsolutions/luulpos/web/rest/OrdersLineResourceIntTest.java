package com.luulsolutions.luulpos.web.rest;

import com.luulsolutions.luulpos.LuulposApp;

import com.luulsolutions.luulpos.domain.OrdersLine;
import com.luulsolutions.luulpos.domain.Orders;
import com.luulsolutions.luulpos.domain.OrdersLineVariant;
import com.luulsolutions.luulpos.domain.Product;
import com.luulsolutions.luulpos.repository.OrdersLineRepository;
import com.luulsolutions.luulpos.repository.search.OrdersLineSearchRepository;
import com.luulsolutions.luulpos.service.OrdersLineService;
import com.luulsolutions.luulpos.service.dto.OrdersLineDTO;
import com.luulsolutions.luulpos.service.mapper.OrdersLineMapper;
import com.luulsolutions.luulpos.web.rest.errors.ExceptionTranslator;
import com.luulsolutions.luulpos.service.dto.OrdersLineCriteria;
import com.luulsolutions.luulpos.service.OrdersLineQueryService;

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
 * Test class for the OrdersLineResource REST controller.
 *
 * @see OrdersLineResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LuulposApp.class)
public class OrdersLineResourceIntTest {

    private static final String DEFAULT_ORDERS_LINE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ORDERS_LINE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ORDERS_LINE_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_ORDERS_LINE_VALUE = "BBBBBBBBBB";

    private static final Float DEFAULT_ORDERS_LINE_PRICE = 1F;
    private static final Float UPDATED_ORDERS_LINE_PRICE = 2F;

    private static final String DEFAULT_ORDERS_LINE_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_ORDERS_LINE_DESCRIPTION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_THUMBNAIL_PHOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_THUMBNAIL_PHOTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_THUMBNAIL_PHOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_THUMBNAIL_PHOTO_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_FULL_PHOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FULL_PHOTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FULL_PHOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FULL_PHOTO_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_FULL_PHOTO_URL = "AAAAAAAAAA";
    private static final String UPDATED_FULL_PHOTO_URL = "BBBBBBBBBB";

    private static final String DEFAULT_THUMBNAIL_PHOTO_URL = "AAAAAAAAAA";
    private static final String UPDATED_THUMBNAIL_PHOTO_URL = "BBBBBBBBBB";

    @Autowired
    private OrdersLineRepository ordersLineRepository;

    @Autowired
    private OrdersLineMapper ordersLineMapper;

    @Autowired
    private OrdersLineService ordersLineService;

    /**
     * This repository is mocked in the com.luulsolutions.luulpos.repository.search test package.
     *
     * @see com.luulsolutions.luulpos.repository.search.OrdersLineSearchRepositoryMockConfiguration
     */
    @Autowired
    private OrdersLineSearchRepository mockOrdersLineSearchRepository;

    @Autowired
    private OrdersLineQueryService ordersLineQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restOrdersLineMockMvc;

    private OrdersLine ordersLine;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OrdersLineResource ordersLineResource = new OrdersLineResource(ordersLineService, ordersLineQueryService);
        this.restOrdersLineMockMvc = MockMvcBuilders.standaloneSetup(ordersLineResource)
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
    public static OrdersLine createEntity(EntityManager em) {
        OrdersLine ordersLine = new OrdersLine()
            .ordersLineName(DEFAULT_ORDERS_LINE_NAME)
            .ordersLineValue(DEFAULT_ORDERS_LINE_VALUE)
            .ordersLinePrice(DEFAULT_ORDERS_LINE_PRICE)
            .ordersLineDescription(DEFAULT_ORDERS_LINE_DESCRIPTION)
            .thumbnailPhoto(DEFAULT_THUMBNAIL_PHOTO)
            .thumbnailPhotoContentType(DEFAULT_THUMBNAIL_PHOTO_CONTENT_TYPE)
            .fullPhoto(DEFAULT_FULL_PHOTO)
            .fullPhotoContentType(DEFAULT_FULL_PHOTO_CONTENT_TYPE)
            .fullPhotoUrl(DEFAULT_FULL_PHOTO_URL)
            .thumbnailPhotoUrl(DEFAULT_THUMBNAIL_PHOTO_URL);
        return ordersLine;
    }

    @Before
    public void initTest() {
        ordersLine = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrdersLine() throws Exception {
        int databaseSizeBeforeCreate = ordersLineRepository.findAll().size();

        // Create the OrdersLine
        OrdersLineDTO ordersLineDTO = ordersLineMapper.toDto(ordersLine);
        restOrdersLineMockMvc.perform(post("/api/orders-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ordersLineDTO)))
            .andExpect(status().isCreated());

        // Validate the OrdersLine in the database
        List<OrdersLine> ordersLineList = ordersLineRepository.findAll();
        assertThat(ordersLineList).hasSize(databaseSizeBeforeCreate + 1);
        OrdersLine testOrdersLine = ordersLineList.get(ordersLineList.size() - 1);
        assertThat(testOrdersLine.getOrdersLineName()).isEqualTo(DEFAULT_ORDERS_LINE_NAME);
        assertThat(testOrdersLine.getOrdersLineValue()).isEqualTo(DEFAULT_ORDERS_LINE_VALUE);
        assertThat(testOrdersLine.getOrdersLinePrice()).isEqualTo(DEFAULT_ORDERS_LINE_PRICE);
        assertThat(testOrdersLine.getOrdersLineDescription()).isEqualTo(DEFAULT_ORDERS_LINE_DESCRIPTION);
        assertThat(testOrdersLine.getThumbnailPhoto()).isEqualTo(DEFAULT_THUMBNAIL_PHOTO);
        assertThat(testOrdersLine.getThumbnailPhotoContentType()).isEqualTo(DEFAULT_THUMBNAIL_PHOTO_CONTENT_TYPE);
        assertThat(testOrdersLine.getFullPhoto()).isEqualTo(DEFAULT_FULL_PHOTO);
        assertThat(testOrdersLine.getFullPhotoContentType()).isEqualTo(DEFAULT_FULL_PHOTO_CONTENT_TYPE);
        assertThat(testOrdersLine.getFullPhotoUrl()).isEqualTo(DEFAULT_FULL_PHOTO_URL);
        assertThat(testOrdersLine.getThumbnailPhotoUrl()).isEqualTo(DEFAULT_THUMBNAIL_PHOTO_URL);

        // Validate the OrdersLine in Elasticsearch
        verify(mockOrdersLineSearchRepository, times(1)).save(testOrdersLine);
    }

    @Test
    @Transactional
    public void createOrdersLineWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ordersLineRepository.findAll().size();

        // Create the OrdersLine with an existing ID
        ordersLine.setId(1L);
        OrdersLineDTO ordersLineDTO = ordersLineMapper.toDto(ordersLine);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrdersLineMockMvc.perform(post("/api/orders-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ordersLineDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OrdersLine in the database
        List<OrdersLine> ordersLineList = ordersLineRepository.findAll();
        assertThat(ordersLineList).hasSize(databaseSizeBeforeCreate);

        // Validate the OrdersLine in Elasticsearch
        verify(mockOrdersLineSearchRepository, times(0)).save(ordersLine);
    }

    @Test
    @Transactional
    public void checkOrdersLineNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordersLineRepository.findAll().size();
        // set the field null
        ordersLine.setOrdersLineName(null);

        // Create the OrdersLine, which fails.
        OrdersLineDTO ordersLineDTO = ordersLineMapper.toDto(ordersLine);

        restOrdersLineMockMvc.perform(post("/api/orders-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ordersLineDTO)))
            .andExpect(status().isBadRequest());

        List<OrdersLine> ordersLineList = ordersLineRepository.findAll();
        assertThat(ordersLineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOrdersLineValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordersLineRepository.findAll().size();
        // set the field null
        ordersLine.setOrdersLineValue(null);

        // Create the OrdersLine, which fails.
        OrdersLineDTO ordersLineDTO = ordersLineMapper.toDto(ordersLine);

        restOrdersLineMockMvc.perform(post("/api/orders-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ordersLineDTO)))
            .andExpect(status().isBadRequest());

        List<OrdersLine> ordersLineList = ordersLineRepository.findAll();
        assertThat(ordersLineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOrdersLines() throws Exception {
        // Initialize the database
        ordersLineRepository.saveAndFlush(ordersLine);

        // Get all the ordersLineList
        restOrdersLineMockMvc.perform(get("/api/orders-lines?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ordersLine.getId().intValue())))
            .andExpect(jsonPath("$.[*].ordersLineName").value(hasItem(DEFAULT_ORDERS_LINE_NAME.toString())))
            .andExpect(jsonPath("$.[*].ordersLineValue").value(hasItem(DEFAULT_ORDERS_LINE_VALUE.toString())))
            .andExpect(jsonPath("$.[*].ordersLinePrice").value(hasItem(DEFAULT_ORDERS_LINE_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].ordersLineDescription").value(hasItem(DEFAULT_ORDERS_LINE_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].thumbnailPhotoContentType").value(hasItem(DEFAULT_THUMBNAIL_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].thumbnailPhoto").value(hasItem(Base64Utils.encodeToString(DEFAULT_THUMBNAIL_PHOTO))))
            .andExpect(jsonPath("$.[*].fullPhotoContentType").value(hasItem(DEFAULT_FULL_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fullPhoto").value(hasItem(Base64Utils.encodeToString(DEFAULT_FULL_PHOTO))))
            .andExpect(jsonPath("$.[*].fullPhotoUrl").value(hasItem(DEFAULT_FULL_PHOTO_URL.toString())))
            .andExpect(jsonPath("$.[*].thumbnailPhotoUrl").value(hasItem(DEFAULT_THUMBNAIL_PHOTO_URL.toString())));
    }
    
    @Test
    @Transactional
    public void getOrdersLine() throws Exception {
        // Initialize the database
        ordersLineRepository.saveAndFlush(ordersLine);

        // Get the ordersLine
        restOrdersLineMockMvc.perform(get("/api/orders-lines/{id}", ordersLine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ordersLine.getId().intValue()))
            .andExpect(jsonPath("$.ordersLineName").value(DEFAULT_ORDERS_LINE_NAME.toString()))
            .andExpect(jsonPath("$.ordersLineValue").value(DEFAULT_ORDERS_LINE_VALUE.toString()))
            .andExpect(jsonPath("$.ordersLinePrice").value(DEFAULT_ORDERS_LINE_PRICE.doubleValue()))
            .andExpect(jsonPath("$.ordersLineDescription").value(DEFAULT_ORDERS_LINE_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.thumbnailPhotoContentType").value(DEFAULT_THUMBNAIL_PHOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.thumbnailPhoto").value(Base64Utils.encodeToString(DEFAULT_THUMBNAIL_PHOTO)))
            .andExpect(jsonPath("$.fullPhotoContentType").value(DEFAULT_FULL_PHOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.fullPhoto").value(Base64Utils.encodeToString(DEFAULT_FULL_PHOTO)))
            .andExpect(jsonPath("$.fullPhotoUrl").value(DEFAULT_FULL_PHOTO_URL.toString()))
            .andExpect(jsonPath("$.thumbnailPhotoUrl").value(DEFAULT_THUMBNAIL_PHOTO_URL.toString()));
    }

    @Test
    @Transactional
    public void getAllOrdersLinesByOrdersLineNameIsEqualToSomething() throws Exception {
        // Initialize the database
        ordersLineRepository.saveAndFlush(ordersLine);

        // Get all the ordersLineList where ordersLineName equals to DEFAULT_ORDERS_LINE_NAME
        defaultOrdersLineShouldBeFound("ordersLineName.equals=" + DEFAULT_ORDERS_LINE_NAME);

        // Get all the ordersLineList where ordersLineName equals to UPDATED_ORDERS_LINE_NAME
        defaultOrdersLineShouldNotBeFound("ordersLineName.equals=" + UPDATED_ORDERS_LINE_NAME);
    }

    @Test
    @Transactional
    public void getAllOrdersLinesByOrdersLineNameIsInShouldWork() throws Exception {
        // Initialize the database
        ordersLineRepository.saveAndFlush(ordersLine);

        // Get all the ordersLineList where ordersLineName in DEFAULT_ORDERS_LINE_NAME or UPDATED_ORDERS_LINE_NAME
        defaultOrdersLineShouldBeFound("ordersLineName.in=" + DEFAULT_ORDERS_LINE_NAME + "," + UPDATED_ORDERS_LINE_NAME);

        // Get all the ordersLineList where ordersLineName equals to UPDATED_ORDERS_LINE_NAME
        defaultOrdersLineShouldNotBeFound("ordersLineName.in=" + UPDATED_ORDERS_LINE_NAME);
    }

    @Test
    @Transactional
    public void getAllOrdersLinesByOrdersLineNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordersLineRepository.saveAndFlush(ordersLine);

        // Get all the ordersLineList where ordersLineName is not null
        defaultOrdersLineShouldBeFound("ordersLineName.specified=true");

        // Get all the ordersLineList where ordersLineName is null
        defaultOrdersLineShouldNotBeFound("ordersLineName.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrdersLinesByOrdersLineValueIsEqualToSomething() throws Exception {
        // Initialize the database
        ordersLineRepository.saveAndFlush(ordersLine);

        // Get all the ordersLineList where ordersLineValue equals to DEFAULT_ORDERS_LINE_VALUE
        defaultOrdersLineShouldBeFound("ordersLineValue.equals=" + DEFAULT_ORDERS_LINE_VALUE);

        // Get all the ordersLineList where ordersLineValue equals to UPDATED_ORDERS_LINE_VALUE
        defaultOrdersLineShouldNotBeFound("ordersLineValue.equals=" + UPDATED_ORDERS_LINE_VALUE);
    }

    @Test
    @Transactional
    public void getAllOrdersLinesByOrdersLineValueIsInShouldWork() throws Exception {
        // Initialize the database
        ordersLineRepository.saveAndFlush(ordersLine);

        // Get all the ordersLineList where ordersLineValue in DEFAULT_ORDERS_LINE_VALUE or UPDATED_ORDERS_LINE_VALUE
        defaultOrdersLineShouldBeFound("ordersLineValue.in=" + DEFAULT_ORDERS_LINE_VALUE + "," + UPDATED_ORDERS_LINE_VALUE);

        // Get all the ordersLineList where ordersLineValue equals to UPDATED_ORDERS_LINE_VALUE
        defaultOrdersLineShouldNotBeFound("ordersLineValue.in=" + UPDATED_ORDERS_LINE_VALUE);
    }

    @Test
    @Transactional
    public void getAllOrdersLinesByOrdersLineValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordersLineRepository.saveAndFlush(ordersLine);

        // Get all the ordersLineList where ordersLineValue is not null
        defaultOrdersLineShouldBeFound("ordersLineValue.specified=true");

        // Get all the ordersLineList where ordersLineValue is null
        defaultOrdersLineShouldNotBeFound("ordersLineValue.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrdersLinesByOrdersLinePriceIsEqualToSomething() throws Exception {
        // Initialize the database
        ordersLineRepository.saveAndFlush(ordersLine);

        // Get all the ordersLineList where ordersLinePrice equals to DEFAULT_ORDERS_LINE_PRICE
        defaultOrdersLineShouldBeFound("ordersLinePrice.equals=" + DEFAULT_ORDERS_LINE_PRICE);

        // Get all the ordersLineList where ordersLinePrice equals to UPDATED_ORDERS_LINE_PRICE
        defaultOrdersLineShouldNotBeFound("ordersLinePrice.equals=" + UPDATED_ORDERS_LINE_PRICE);
    }

    @Test
    @Transactional
    public void getAllOrdersLinesByOrdersLinePriceIsInShouldWork() throws Exception {
        // Initialize the database
        ordersLineRepository.saveAndFlush(ordersLine);

        // Get all the ordersLineList where ordersLinePrice in DEFAULT_ORDERS_LINE_PRICE or UPDATED_ORDERS_LINE_PRICE
        defaultOrdersLineShouldBeFound("ordersLinePrice.in=" + DEFAULT_ORDERS_LINE_PRICE + "," + UPDATED_ORDERS_LINE_PRICE);

        // Get all the ordersLineList where ordersLinePrice equals to UPDATED_ORDERS_LINE_PRICE
        defaultOrdersLineShouldNotBeFound("ordersLinePrice.in=" + UPDATED_ORDERS_LINE_PRICE);
    }

    @Test
    @Transactional
    public void getAllOrdersLinesByOrdersLinePriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordersLineRepository.saveAndFlush(ordersLine);

        // Get all the ordersLineList where ordersLinePrice is not null
        defaultOrdersLineShouldBeFound("ordersLinePrice.specified=true");

        // Get all the ordersLineList where ordersLinePrice is null
        defaultOrdersLineShouldNotBeFound("ordersLinePrice.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrdersLinesByOrdersLineDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        ordersLineRepository.saveAndFlush(ordersLine);

        // Get all the ordersLineList where ordersLineDescription equals to DEFAULT_ORDERS_LINE_DESCRIPTION
        defaultOrdersLineShouldBeFound("ordersLineDescription.equals=" + DEFAULT_ORDERS_LINE_DESCRIPTION);

        // Get all the ordersLineList where ordersLineDescription equals to UPDATED_ORDERS_LINE_DESCRIPTION
        defaultOrdersLineShouldNotBeFound("ordersLineDescription.equals=" + UPDATED_ORDERS_LINE_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllOrdersLinesByOrdersLineDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        ordersLineRepository.saveAndFlush(ordersLine);

        // Get all the ordersLineList where ordersLineDescription in DEFAULT_ORDERS_LINE_DESCRIPTION or UPDATED_ORDERS_LINE_DESCRIPTION
        defaultOrdersLineShouldBeFound("ordersLineDescription.in=" + DEFAULT_ORDERS_LINE_DESCRIPTION + "," + UPDATED_ORDERS_LINE_DESCRIPTION);

        // Get all the ordersLineList where ordersLineDescription equals to UPDATED_ORDERS_LINE_DESCRIPTION
        defaultOrdersLineShouldNotBeFound("ordersLineDescription.in=" + UPDATED_ORDERS_LINE_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllOrdersLinesByOrdersLineDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordersLineRepository.saveAndFlush(ordersLine);

        // Get all the ordersLineList where ordersLineDescription is not null
        defaultOrdersLineShouldBeFound("ordersLineDescription.specified=true");

        // Get all the ordersLineList where ordersLineDescription is null
        defaultOrdersLineShouldNotBeFound("ordersLineDescription.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrdersLinesByFullPhotoUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        ordersLineRepository.saveAndFlush(ordersLine);

        // Get all the ordersLineList where fullPhotoUrl equals to DEFAULT_FULL_PHOTO_URL
        defaultOrdersLineShouldBeFound("fullPhotoUrl.equals=" + DEFAULT_FULL_PHOTO_URL);

        // Get all the ordersLineList where fullPhotoUrl equals to UPDATED_FULL_PHOTO_URL
        defaultOrdersLineShouldNotBeFound("fullPhotoUrl.equals=" + UPDATED_FULL_PHOTO_URL);
    }

    @Test
    @Transactional
    public void getAllOrdersLinesByFullPhotoUrlIsInShouldWork() throws Exception {
        // Initialize the database
        ordersLineRepository.saveAndFlush(ordersLine);

        // Get all the ordersLineList where fullPhotoUrl in DEFAULT_FULL_PHOTO_URL or UPDATED_FULL_PHOTO_URL
        defaultOrdersLineShouldBeFound("fullPhotoUrl.in=" + DEFAULT_FULL_PHOTO_URL + "," + UPDATED_FULL_PHOTO_URL);

        // Get all the ordersLineList where fullPhotoUrl equals to UPDATED_FULL_PHOTO_URL
        defaultOrdersLineShouldNotBeFound("fullPhotoUrl.in=" + UPDATED_FULL_PHOTO_URL);
    }

    @Test
    @Transactional
    public void getAllOrdersLinesByFullPhotoUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordersLineRepository.saveAndFlush(ordersLine);

        // Get all the ordersLineList where fullPhotoUrl is not null
        defaultOrdersLineShouldBeFound("fullPhotoUrl.specified=true");

        // Get all the ordersLineList where fullPhotoUrl is null
        defaultOrdersLineShouldNotBeFound("fullPhotoUrl.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrdersLinesByThumbnailPhotoUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        ordersLineRepository.saveAndFlush(ordersLine);

        // Get all the ordersLineList where thumbnailPhotoUrl equals to DEFAULT_THUMBNAIL_PHOTO_URL
        defaultOrdersLineShouldBeFound("thumbnailPhotoUrl.equals=" + DEFAULT_THUMBNAIL_PHOTO_URL);

        // Get all the ordersLineList where thumbnailPhotoUrl equals to UPDATED_THUMBNAIL_PHOTO_URL
        defaultOrdersLineShouldNotBeFound("thumbnailPhotoUrl.equals=" + UPDATED_THUMBNAIL_PHOTO_URL);
    }

    @Test
    @Transactional
    public void getAllOrdersLinesByThumbnailPhotoUrlIsInShouldWork() throws Exception {
        // Initialize the database
        ordersLineRepository.saveAndFlush(ordersLine);

        // Get all the ordersLineList where thumbnailPhotoUrl in DEFAULT_THUMBNAIL_PHOTO_URL or UPDATED_THUMBNAIL_PHOTO_URL
        defaultOrdersLineShouldBeFound("thumbnailPhotoUrl.in=" + DEFAULT_THUMBNAIL_PHOTO_URL + "," + UPDATED_THUMBNAIL_PHOTO_URL);

        // Get all the ordersLineList where thumbnailPhotoUrl equals to UPDATED_THUMBNAIL_PHOTO_URL
        defaultOrdersLineShouldNotBeFound("thumbnailPhotoUrl.in=" + UPDATED_THUMBNAIL_PHOTO_URL);
    }

    @Test
    @Transactional
    public void getAllOrdersLinesByThumbnailPhotoUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordersLineRepository.saveAndFlush(ordersLine);

        // Get all the ordersLineList where thumbnailPhotoUrl is not null
        defaultOrdersLineShouldBeFound("thumbnailPhotoUrl.specified=true");

        // Get all the ordersLineList where thumbnailPhotoUrl is null
        defaultOrdersLineShouldNotBeFound("thumbnailPhotoUrl.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrdersLinesByOrdersIsEqualToSomething() throws Exception {
        // Initialize the database
        Orders orders = OrdersResourceIntTest.createEntity(em);
        em.persist(orders);
        em.flush();
        ordersLine.setOrders(orders);
        ordersLineRepository.saveAndFlush(ordersLine);
        Long ordersId = orders.getId();

        // Get all the ordersLineList where orders equals to ordersId
        defaultOrdersLineShouldBeFound("ordersId.equals=" + ordersId);

        // Get all the ordersLineList where orders equals to ordersId + 1
        defaultOrdersLineShouldNotBeFound("ordersId.equals=" + (ordersId + 1));
    }


    @Test
    @Transactional
    public void getAllOrdersLinesByOrdersLineVariantsIsEqualToSomething() throws Exception {
        // Initialize the database
        OrdersLineVariant ordersLineVariants = OrdersLineVariantResourceIntTest.createEntity(em);
        em.persist(ordersLineVariants);
        em.flush();
        ordersLine.addOrdersLineVariants(ordersLineVariants);
        ordersLineRepository.saveAndFlush(ordersLine);
        Long ordersLineVariantsId = ordersLineVariants.getId();

        // Get all the ordersLineList where ordersLineVariants equals to ordersLineVariantsId
        defaultOrdersLineShouldBeFound("ordersLineVariantsId.equals=" + ordersLineVariantsId);

        // Get all the ordersLineList where ordersLineVariants equals to ordersLineVariantsId + 1
        defaultOrdersLineShouldNotBeFound("ordersLineVariantsId.equals=" + (ordersLineVariantsId + 1));
    }


    @Test
    @Transactional
    public void getAllOrdersLinesByProductIsEqualToSomething() throws Exception {
        // Initialize the database
        Product product = ProductResourceIntTest.createEntity(em);
        em.persist(product);
        em.flush();
        ordersLine.setProduct(product);
        ordersLineRepository.saveAndFlush(ordersLine);
        Long productId = product.getId();

        // Get all the ordersLineList where product equals to productId
        defaultOrdersLineShouldBeFound("productId.equals=" + productId);

        // Get all the ordersLineList where product equals to productId + 1
        defaultOrdersLineShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultOrdersLineShouldBeFound(String filter) throws Exception {
        restOrdersLineMockMvc.perform(get("/api/orders-lines?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ordersLine.getId().intValue())))
            .andExpect(jsonPath("$.[*].ordersLineName").value(hasItem(DEFAULT_ORDERS_LINE_NAME.toString())))
            .andExpect(jsonPath("$.[*].ordersLineValue").value(hasItem(DEFAULT_ORDERS_LINE_VALUE.toString())))
            .andExpect(jsonPath("$.[*].ordersLinePrice").value(hasItem(DEFAULT_ORDERS_LINE_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].ordersLineDescription").value(hasItem(DEFAULT_ORDERS_LINE_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].thumbnailPhotoContentType").value(hasItem(DEFAULT_THUMBNAIL_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].thumbnailPhoto").value(hasItem(Base64Utils.encodeToString(DEFAULT_THUMBNAIL_PHOTO))))
            .andExpect(jsonPath("$.[*].fullPhotoContentType").value(hasItem(DEFAULT_FULL_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fullPhoto").value(hasItem(Base64Utils.encodeToString(DEFAULT_FULL_PHOTO))))
            .andExpect(jsonPath("$.[*].fullPhotoUrl").value(hasItem(DEFAULT_FULL_PHOTO_URL.toString())))
            .andExpect(jsonPath("$.[*].thumbnailPhotoUrl").value(hasItem(DEFAULT_THUMBNAIL_PHOTO_URL.toString())));

        // Check, that the count call also returns 1
        restOrdersLineMockMvc.perform(get("/api/orders-lines/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultOrdersLineShouldNotBeFound(String filter) throws Exception {
        restOrdersLineMockMvc.perform(get("/api/orders-lines?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrdersLineMockMvc.perform(get("/api/orders-lines/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingOrdersLine() throws Exception {
        // Get the ordersLine
        restOrdersLineMockMvc.perform(get("/api/orders-lines/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrdersLine() throws Exception {
        // Initialize the database
        ordersLineRepository.saveAndFlush(ordersLine);

        int databaseSizeBeforeUpdate = ordersLineRepository.findAll().size();

        // Update the ordersLine
        OrdersLine updatedOrdersLine = ordersLineRepository.findById(ordersLine.getId()).get();
        // Disconnect from session so that the updates on updatedOrdersLine are not directly saved in db
        em.detach(updatedOrdersLine);
        updatedOrdersLine
            .ordersLineName(UPDATED_ORDERS_LINE_NAME)
            .ordersLineValue(UPDATED_ORDERS_LINE_VALUE)
            .ordersLinePrice(UPDATED_ORDERS_LINE_PRICE)
            .ordersLineDescription(UPDATED_ORDERS_LINE_DESCRIPTION)
            .thumbnailPhoto(UPDATED_THUMBNAIL_PHOTO)
            .thumbnailPhotoContentType(UPDATED_THUMBNAIL_PHOTO_CONTENT_TYPE)
            .fullPhoto(UPDATED_FULL_PHOTO)
            .fullPhotoContentType(UPDATED_FULL_PHOTO_CONTENT_TYPE)
            .fullPhotoUrl(UPDATED_FULL_PHOTO_URL)
            .thumbnailPhotoUrl(UPDATED_THUMBNAIL_PHOTO_URL);
        OrdersLineDTO ordersLineDTO = ordersLineMapper.toDto(updatedOrdersLine);

        restOrdersLineMockMvc.perform(put("/api/orders-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ordersLineDTO)))
            .andExpect(status().isOk());

        // Validate the OrdersLine in the database
        List<OrdersLine> ordersLineList = ordersLineRepository.findAll();
        assertThat(ordersLineList).hasSize(databaseSizeBeforeUpdate);
        OrdersLine testOrdersLine = ordersLineList.get(ordersLineList.size() - 1);
        assertThat(testOrdersLine.getOrdersLineName()).isEqualTo(UPDATED_ORDERS_LINE_NAME);
        assertThat(testOrdersLine.getOrdersLineValue()).isEqualTo(UPDATED_ORDERS_LINE_VALUE);
        assertThat(testOrdersLine.getOrdersLinePrice()).isEqualTo(UPDATED_ORDERS_LINE_PRICE);
        assertThat(testOrdersLine.getOrdersLineDescription()).isEqualTo(UPDATED_ORDERS_LINE_DESCRIPTION);
        assertThat(testOrdersLine.getThumbnailPhoto()).isEqualTo(UPDATED_THUMBNAIL_PHOTO);
        assertThat(testOrdersLine.getThumbnailPhotoContentType()).isEqualTo(UPDATED_THUMBNAIL_PHOTO_CONTENT_TYPE);
        assertThat(testOrdersLine.getFullPhoto()).isEqualTo(UPDATED_FULL_PHOTO);
        assertThat(testOrdersLine.getFullPhotoContentType()).isEqualTo(UPDATED_FULL_PHOTO_CONTENT_TYPE);
        assertThat(testOrdersLine.getFullPhotoUrl()).isEqualTo(UPDATED_FULL_PHOTO_URL);
        assertThat(testOrdersLine.getThumbnailPhotoUrl()).isEqualTo(UPDATED_THUMBNAIL_PHOTO_URL);

        // Validate the OrdersLine in Elasticsearch
        verify(mockOrdersLineSearchRepository, times(1)).save(testOrdersLine);
    }

    @Test
    @Transactional
    public void updateNonExistingOrdersLine() throws Exception {
        int databaseSizeBeforeUpdate = ordersLineRepository.findAll().size();

        // Create the OrdersLine
        OrdersLineDTO ordersLineDTO = ordersLineMapper.toDto(ordersLine);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdersLineMockMvc.perform(put("/api/orders-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ordersLineDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OrdersLine in the database
        List<OrdersLine> ordersLineList = ordersLineRepository.findAll();
        assertThat(ordersLineList).hasSize(databaseSizeBeforeUpdate);

        // Validate the OrdersLine in Elasticsearch
        verify(mockOrdersLineSearchRepository, times(0)).save(ordersLine);
    }

    @Test
    @Transactional
    public void deleteOrdersLine() throws Exception {
        // Initialize the database
        ordersLineRepository.saveAndFlush(ordersLine);

        int databaseSizeBeforeDelete = ordersLineRepository.findAll().size();

        // Get the ordersLine
        restOrdersLineMockMvc.perform(delete("/api/orders-lines/{id}", ordersLine.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<OrdersLine> ordersLineList = ordersLineRepository.findAll();
        assertThat(ordersLineList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the OrdersLine in Elasticsearch
        verify(mockOrdersLineSearchRepository, times(1)).deleteById(ordersLine.getId());
    }

    @Test
    @Transactional
    public void searchOrdersLine() throws Exception {
        // Initialize the database
        ordersLineRepository.saveAndFlush(ordersLine);
        when(mockOrdersLineSearchRepository.search(queryStringQuery("id:" + ordersLine.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(ordersLine), PageRequest.of(0, 1), 1));
        // Search the ordersLine
        restOrdersLineMockMvc.perform(get("/api/_search/orders-lines?query=id:" + ordersLine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ordersLine.getId().intValue())))
            .andExpect(jsonPath("$.[*].ordersLineName").value(hasItem(DEFAULT_ORDERS_LINE_NAME)))
            .andExpect(jsonPath("$.[*].ordersLineValue").value(hasItem(DEFAULT_ORDERS_LINE_VALUE)))
            .andExpect(jsonPath("$.[*].ordersLinePrice").value(hasItem(DEFAULT_ORDERS_LINE_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].ordersLineDescription").value(hasItem(DEFAULT_ORDERS_LINE_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].thumbnailPhotoContentType").value(hasItem(DEFAULT_THUMBNAIL_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].thumbnailPhoto").value(hasItem(Base64Utils.encodeToString(DEFAULT_THUMBNAIL_PHOTO))))
            .andExpect(jsonPath("$.[*].fullPhotoContentType").value(hasItem(DEFAULT_FULL_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fullPhoto").value(hasItem(Base64Utils.encodeToString(DEFAULT_FULL_PHOTO))))
            .andExpect(jsonPath("$.[*].fullPhotoUrl").value(hasItem(DEFAULT_FULL_PHOTO_URL)))
            .andExpect(jsonPath("$.[*].thumbnailPhotoUrl").value(hasItem(DEFAULT_THUMBNAIL_PHOTO_URL)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrdersLine.class);
        OrdersLine ordersLine1 = new OrdersLine();
        ordersLine1.setId(1L);
        OrdersLine ordersLine2 = new OrdersLine();
        ordersLine2.setId(ordersLine1.getId());
        assertThat(ordersLine1).isEqualTo(ordersLine2);
        ordersLine2.setId(2L);
        assertThat(ordersLine1).isNotEqualTo(ordersLine2);
        ordersLine1.setId(null);
        assertThat(ordersLine1).isNotEqualTo(ordersLine2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrdersLineDTO.class);
        OrdersLineDTO ordersLineDTO1 = new OrdersLineDTO();
        ordersLineDTO1.setId(1L);
        OrdersLineDTO ordersLineDTO2 = new OrdersLineDTO();
        assertThat(ordersLineDTO1).isNotEqualTo(ordersLineDTO2);
        ordersLineDTO2.setId(ordersLineDTO1.getId());
        assertThat(ordersLineDTO1).isEqualTo(ordersLineDTO2);
        ordersLineDTO2.setId(2L);
        assertThat(ordersLineDTO1).isNotEqualTo(ordersLineDTO2);
        ordersLineDTO1.setId(null);
        assertThat(ordersLineDTO1).isNotEqualTo(ordersLineDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(ordersLineMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(ordersLineMapper.fromId(null)).isNull();
    }
}
