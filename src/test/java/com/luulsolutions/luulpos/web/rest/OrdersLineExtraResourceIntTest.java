package com.luulsolutions.luulpos.web.rest;

import com.luulsolutions.luulpos.LuulposApp;

import com.luulsolutions.luulpos.domain.OrdersLineExtra;
import com.luulsolutions.luulpos.domain.OrdersLineVariant;
import com.luulsolutions.luulpos.repository.OrdersLineExtraRepository;
import com.luulsolutions.luulpos.repository.search.OrdersLineExtraSearchRepository;
import com.luulsolutions.luulpos.service.OrdersLineExtraService;
import com.luulsolutions.luulpos.service.dto.OrdersLineExtraDTO;
import com.luulsolutions.luulpos.service.mapper.OrdersLineExtraMapper;
import com.luulsolutions.luulpos.web.rest.errors.ExceptionTranslator;
import com.luulsolutions.luulpos.service.dto.OrdersLineExtraCriteria;
import com.luulsolutions.luulpos.service.OrdersLineExtraQueryService;

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
 * Test class for the OrdersLineExtraResource REST controller.
 *
 * @see OrdersLineExtraResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LuulposApp.class)
public class OrdersLineExtraResourceIntTest {

    private static final String DEFAULT_ORDERS_LINE_EXTRA_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ORDERS_LINE_EXTRA_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ORDERS_LINE_EXTRA_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_ORDERS_LINE_EXTRA_VALUE = "BBBBBBBBBB";

    private static final Float DEFAULT_ORDERS_LINE_EXTRA_PRICE = 1F;
    private static final Float UPDATED_ORDERS_LINE_EXTRA_PRICE = 2F;

    private static final String DEFAULT_ORDERS_OPTION_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_ORDERS_OPTION_DESCRIPTION = "BBBBBBBBBB";

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
    private OrdersLineExtraRepository ordersLineExtraRepository;

    @Autowired
    private OrdersLineExtraMapper ordersLineExtraMapper;

    @Autowired
    private OrdersLineExtraService ordersLineExtraService;

    /**
     * This repository is mocked in the com.luulsolutions.luulpos.repository.search test package.
     *
     * @see com.luulsolutions.luulpos.repository.search.OrdersLineExtraSearchRepositoryMockConfiguration
     */
    @Autowired
    private OrdersLineExtraSearchRepository mockOrdersLineExtraSearchRepository;

    @Autowired
    private OrdersLineExtraQueryService ordersLineExtraQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restOrdersLineExtraMockMvc;

    private OrdersLineExtra ordersLineExtra;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OrdersLineExtraResource ordersLineExtraResource = new OrdersLineExtraResource(ordersLineExtraService, ordersLineExtraQueryService);
        this.restOrdersLineExtraMockMvc = MockMvcBuilders.standaloneSetup(ordersLineExtraResource)
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
    public static OrdersLineExtra createEntity(EntityManager em) {
        OrdersLineExtra ordersLineExtra = new OrdersLineExtra()
            .ordersLineExtraName(DEFAULT_ORDERS_LINE_EXTRA_NAME)
            .ordersLineExtraValue(DEFAULT_ORDERS_LINE_EXTRA_VALUE)
            .ordersLineExtraPrice(DEFAULT_ORDERS_LINE_EXTRA_PRICE)
            .ordersOptionDescription(DEFAULT_ORDERS_OPTION_DESCRIPTION)
            .fullPhoto(DEFAULT_FULL_PHOTO)
            .fullPhotoContentType(DEFAULT_FULL_PHOTO_CONTENT_TYPE)
            .fullPhotoUrl(DEFAULT_FULL_PHOTO_URL)
            .thumbnailPhoto(DEFAULT_THUMBNAIL_PHOTO)
            .thumbnailPhotoContentType(DEFAULT_THUMBNAIL_PHOTO_CONTENT_TYPE)
            .thumbnailPhotoUrl(DEFAULT_THUMBNAIL_PHOTO_URL);
        return ordersLineExtra;
    }

    @Before
    public void initTest() {
        ordersLineExtra = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrdersLineExtra() throws Exception {
        int databaseSizeBeforeCreate = ordersLineExtraRepository.findAll().size();

        // Create the OrdersLineExtra
        OrdersLineExtraDTO ordersLineExtraDTO = ordersLineExtraMapper.toDto(ordersLineExtra);
        restOrdersLineExtraMockMvc.perform(post("/api/orders-line-extras")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ordersLineExtraDTO)))
            .andExpect(status().isCreated());

        // Validate the OrdersLineExtra in the database
        List<OrdersLineExtra> ordersLineExtraList = ordersLineExtraRepository.findAll();
        assertThat(ordersLineExtraList).hasSize(databaseSizeBeforeCreate + 1);
        OrdersLineExtra testOrdersLineExtra = ordersLineExtraList.get(ordersLineExtraList.size() - 1);
        assertThat(testOrdersLineExtra.getOrdersLineExtraName()).isEqualTo(DEFAULT_ORDERS_LINE_EXTRA_NAME);
        assertThat(testOrdersLineExtra.getOrdersLineExtraValue()).isEqualTo(DEFAULT_ORDERS_LINE_EXTRA_VALUE);
        assertThat(testOrdersLineExtra.getOrdersLineExtraPrice()).isEqualTo(DEFAULT_ORDERS_LINE_EXTRA_PRICE);
        assertThat(testOrdersLineExtra.getOrdersOptionDescription()).isEqualTo(DEFAULT_ORDERS_OPTION_DESCRIPTION);
        assertThat(testOrdersLineExtra.getFullPhoto()).isEqualTo(DEFAULT_FULL_PHOTO);
        assertThat(testOrdersLineExtra.getFullPhotoContentType()).isEqualTo(DEFAULT_FULL_PHOTO_CONTENT_TYPE);
        assertThat(testOrdersLineExtra.getFullPhotoUrl()).isEqualTo(DEFAULT_FULL_PHOTO_URL);
        assertThat(testOrdersLineExtra.getThumbnailPhoto()).isEqualTo(DEFAULT_THUMBNAIL_PHOTO);
        assertThat(testOrdersLineExtra.getThumbnailPhotoContentType()).isEqualTo(DEFAULT_THUMBNAIL_PHOTO_CONTENT_TYPE);
        assertThat(testOrdersLineExtra.getThumbnailPhotoUrl()).isEqualTo(DEFAULT_THUMBNAIL_PHOTO_URL);

        // Validate the OrdersLineExtra in Elasticsearch
        verify(mockOrdersLineExtraSearchRepository, times(1)).save(testOrdersLineExtra);
    }

    @Test
    @Transactional
    public void createOrdersLineExtraWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ordersLineExtraRepository.findAll().size();

        // Create the OrdersLineExtra with an existing ID
        ordersLineExtra.setId(1L);
        OrdersLineExtraDTO ordersLineExtraDTO = ordersLineExtraMapper.toDto(ordersLineExtra);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrdersLineExtraMockMvc.perform(post("/api/orders-line-extras")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ordersLineExtraDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OrdersLineExtra in the database
        List<OrdersLineExtra> ordersLineExtraList = ordersLineExtraRepository.findAll();
        assertThat(ordersLineExtraList).hasSize(databaseSizeBeforeCreate);

        // Validate the OrdersLineExtra in Elasticsearch
        verify(mockOrdersLineExtraSearchRepository, times(0)).save(ordersLineExtra);
    }

    @Test
    @Transactional
    public void checkOrdersLineExtraNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordersLineExtraRepository.findAll().size();
        // set the field null
        ordersLineExtra.setOrdersLineExtraName(null);

        // Create the OrdersLineExtra, which fails.
        OrdersLineExtraDTO ordersLineExtraDTO = ordersLineExtraMapper.toDto(ordersLineExtra);

        restOrdersLineExtraMockMvc.perform(post("/api/orders-line-extras")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ordersLineExtraDTO)))
            .andExpect(status().isBadRequest());

        List<OrdersLineExtra> ordersLineExtraList = ordersLineExtraRepository.findAll();
        assertThat(ordersLineExtraList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOrdersLineExtraValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordersLineExtraRepository.findAll().size();
        // set the field null
        ordersLineExtra.setOrdersLineExtraValue(null);

        // Create the OrdersLineExtra, which fails.
        OrdersLineExtraDTO ordersLineExtraDTO = ordersLineExtraMapper.toDto(ordersLineExtra);

        restOrdersLineExtraMockMvc.perform(post("/api/orders-line-extras")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ordersLineExtraDTO)))
            .andExpect(status().isBadRequest());

        List<OrdersLineExtra> ordersLineExtraList = ordersLineExtraRepository.findAll();
        assertThat(ordersLineExtraList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOrdersLineExtras() throws Exception {
        // Initialize the database
        ordersLineExtraRepository.saveAndFlush(ordersLineExtra);

        // Get all the ordersLineExtraList
        restOrdersLineExtraMockMvc.perform(get("/api/orders-line-extras?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ordersLineExtra.getId().intValue())))
            .andExpect(jsonPath("$.[*].ordersLineExtraName").value(hasItem(DEFAULT_ORDERS_LINE_EXTRA_NAME.toString())))
            .andExpect(jsonPath("$.[*].ordersLineExtraValue").value(hasItem(DEFAULT_ORDERS_LINE_EXTRA_VALUE.toString())))
            .andExpect(jsonPath("$.[*].ordersLineExtraPrice").value(hasItem(DEFAULT_ORDERS_LINE_EXTRA_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].ordersOptionDescription").value(hasItem(DEFAULT_ORDERS_OPTION_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].fullPhotoContentType").value(hasItem(DEFAULT_FULL_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fullPhoto").value(hasItem(Base64Utils.encodeToString(DEFAULT_FULL_PHOTO))))
            .andExpect(jsonPath("$.[*].fullPhotoUrl").value(hasItem(DEFAULT_FULL_PHOTO_URL.toString())))
            .andExpect(jsonPath("$.[*].thumbnailPhotoContentType").value(hasItem(DEFAULT_THUMBNAIL_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].thumbnailPhoto").value(hasItem(Base64Utils.encodeToString(DEFAULT_THUMBNAIL_PHOTO))))
            .andExpect(jsonPath("$.[*].thumbnailPhotoUrl").value(hasItem(DEFAULT_THUMBNAIL_PHOTO_URL.toString())));
    }
    
    @Test
    @Transactional
    public void getOrdersLineExtra() throws Exception {
        // Initialize the database
        ordersLineExtraRepository.saveAndFlush(ordersLineExtra);

        // Get the ordersLineExtra
        restOrdersLineExtraMockMvc.perform(get("/api/orders-line-extras/{id}", ordersLineExtra.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ordersLineExtra.getId().intValue()))
            .andExpect(jsonPath("$.ordersLineExtraName").value(DEFAULT_ORDERS_LINE_EXTRA_NAME.toString()))
            .andExpect(jsonPath("$.ordersLineExtraValue").value(DEFAULT_ORDERS_LINE_EXTRA_VALUE.toString()))
            .andExpect(jsonPath("$.ordersLineExtraPrice").value(DEFAULT_ORDERS_LINE_EXTRA_PRICE.doubleValue()))
            .andExpect(jsonPath("$.ordersOptionDescription").value(DEFAULT_ORDERS_OPTION_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.fullPhotoContentType").value(DEFAULT_FULL_PHOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.fullPhoto").value(Base64Utils.encodeToString(DEFAULT_FULL_PHOTO)))
            .andExpect(jsonPath("$.fullPhotoUrl").value(DEFAULT_FULL_PHOTO_URL.toString()))
            .andExpect(jsonPath("$.thumbnailPhotoContentType").value(DEFAULT_THUMBNAIL_PHOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.thumbnailPhoto").value(Base64Utils.encodeToString(DEFAULT_THUMBNAIL_PHOTO)))
            .andExpect(jsonPath("$.thumbnailPhotoUrl").value(DEFAULT_THUMBNAIL_PHOTO_URL.toString()));
    }

    @Test
    @Transactional
    public void getAllOrdersLineExtrasByOrdersLineExtraNameIsEqualToSomething() throws Exception {
        // Initialize the database
        ordersLineExtraRepository.saveAndFlush(ordersLineExtra);

        // Get all the ordersLineExtraList where ordersLineExtraName equals to DEFAULT_ORDERS_LINE_EXTRA_NAME
        defaultOrdersLineExtraShouldBeFound("ordersLineExtraName.equals=" + DEFAULT_ORDERS_LINE_EXTRA_NAME);

        // Get all the ordersLineExtraList where ordersLineExtraName equals to UPDATED_ORDERS_LINE_EXTRA_NAME
        defaultOrdersLineExtraShouldNotBeFound("ordersLineExtraName.equals=" + UPDATED_ORDERS_LINE_EXTRA_NAME);
    }

    @Test
    @Transactional
    public void getAllOrdersLineExtrasByOrdersLineExtraNameIsInShouldWork() throws Exception {
        // Initialize the database
        ordersLineExtraRepository.saveAndFlush(ordersLineExtra);

        // Get all the ordersLineExtraList where ordersLineExtraName in DEFAULT_ORDERS_LINE_EXTRA_NAME or UPDATED_ORDERS_LINE_EXTRA_NAME
        defaultOrdersLineExtraShouldBeFound("ordersLineExtraName.in=" + DEFAULT_ORDERS_LINE_EXTRA_NAME + "," + UPDATED_ORDERS_LINE_EXTRA_NAME);

        // Get all the ordersLineExtraList where ordersLineExtraName equals to UPDATED_ORDERS_LINE_EXTRA_NAME
        defaultOrdersLineExtraShouldNotBeFound("ordersLineExtraName.in=" + UPDATED_ORDERS_LINE_EXTRA_NAME);
    }

    @Test
    @Transactional
    public void getAllOrdersLineExtrasByOrdersLineExtraNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordersLineExtraRepository.saveAndFlush(ordersLineExtra);

        // Get all the ordersLineExtraList where ordersLineExtraName is not null
        defaultOrdersLineExtraShouldBeFound("ordersLineExtraName.specified=true");

        // Get all the ordersLineExtraList where ordersLineExtraName is null
        defaultOrdersLineExtraShouldNotBeFound("ordersLineExtraName.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrdersLineExtrasByOrdersLineExtraValueIsEqualToSomething() throws Exception {
        // Initialize the database
        ordersLineExtraRepository.saveAndFlush(ordersLineExtra);

        // Get all the ordersLineExtraList where ordersLineExtraValue equals to DEFAULT_ORDERS_LINE_EXTRA_VALUE
        defaultOrdersLineExtraShouldBeFound("ordersLineExtraValue.equals=" + DEFAULT_ORDERS_LINE_EXTRA_VALUE);

        // Get all the ordersLineExtraList where ordersLineExtraValue equals to UPDATED_ORDERS_LINE_EXTRA_VALUE
        defaultOrdersLineExtraShouldNotBeFound("ordersLineExtraValue.equals=" + UPDATED_ORDERS_LINE_EXTRA_VALUE);
    }

    @Test
    @Transactional
    public void getAllOrdersLineExtrasByOrdersLineExtraValueIsInShouldWork() throws Exception {
        // Initialize the database
        ordersLineExtraRepository.saveAndFlush(ordersLineExtra);

        // Get all the ordersLineExtraList where ordersLineExtraValue in DEFAULT_ORDERS_LINE_EXTRA_VALUE or UPDATED_ORDERS_LINE_EXTRA_VALUE
        defaultOrdersLineExtraShouldBeFound("ordersLineExtraValue.in=" + DEFAULT_ORDERS_LINE_EXTRA_VALUE + "," + UPDATED_ORDERS_LINE_EXTRA_VALUE);

        // Get all the ordersLineExtraList where ordersLineExtraValue equals to UPDATED_ORDERS_LINE_EXTRA_VALUE
        defaultOrdersLineExtraShouldNotBeFound("ordersLineExtraValue.in=" + UPDATED_ORDERS_LINE_EXTRA_VALUE);
    }

    @Test
    @Transactional
    public void getAllOrdersLineExtrasByOrdersLineExtraValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordersLineExtraRepository.saveAndFlush(ordersLineExtra);

        // Get all the ordersLineExtraList where ordersLineExtraValue is not null
        defaultOrdersLineExtraShouldBeFound("ordersLineExtraValue.specified=true");

        // Get all the ordersLineExtraList where ordersLineExtraValue is null
        defaultOrdersLineExtraShouldNotBeFound("ordersLineExtraValue.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrdersLineExtrasByOrdersLineExtraPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        ordersLineExtraRepository.saveAndFlush(ordersLineExtra);

        // Get all the ordersLineExtraList where ordersLineExtraPrice equals to DEFAULT_ORDERS_LINE_EXTRA_PRICE
        defaultOrdersLineExtraShouldBeFound("ordersLineExtraPrice.equals=" + DEFAULT_ORDERS_LINE_EXTRA_PRICE);

        // Get all the ordersLineExtraList where ordersLineExtraPrice equals to UPDATED_ORDERS_LINE_EXTRA_PRICE
        defaultOrdersLineExtraShouldNotBeFound("ordersLineExtraPrice.equals=" + UPDATED_ORDERS_LINE_EXTRA_PRICE);
    }

    @Test
    @Transactional
    public void getAllOrdersLineExtrasByOrdersLineExtraPriceIsInShouldWork() throws Exception {
        // Initialize the database
        ordersLineExtraRepository.saveAndFlush(ordersLineExtra);

        // Get all the ordersLineExtraList where ordersLineExtraPrice in DEFAULT_ORDERS_LINE_EXTRA_PRICE or UPDATED_ORDERS_LINE_EXTRA_PRICE
        defaultOrdersLineExtraShouldBeFound("ordersLineExtraPrice.in=" + DEFAULT_ORDERS_LINE_EXTRA_PRICE + "," + UPDATED_ORDERS_LINE_EXTRA_PRICE);

        // Get all the ordersLineExtraList where ordersLineExtraPrice equals to UPDATED_ORDERS_LINE_EXTRA_PRICE
        defaultOrdersLineExtraShouldNotBeFound("ordersLineExtraPrice.in=" + UPDATED_ORDERS_LINE_EXTRA_PRICE);
    }

    @Test
    @Transactional
    public void getAllOrdersLineExtrasByOrdersLineExtraPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordersLineExtraRepository.saveAndFlush(ordersLineExtra);

        // Get all the ordersLineExtraList where ordersLineExtraPrice is not null
        defaultOrdersLineExtraShouldBeFound("ordersLineExtraPrice.specified=true");

        // Get all the ordersLineExtraList where ordersLineExtraPrice is null
        defaultOrdersLineExtraShouldNotBeFound("ordersLineExtraPrice.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrdersLineExtrasByOrdersOptionDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        ordersLineExtraRepository.saveAndFlush(ordersLineExtra);

        // Get all the ordersLineExtraList where ordersOptionDescription equals to DEFAULT_ORDERS_OPTION_DESCRIPTION
        defaultOrdersLineExtraShouldBeFound("ordersOptionDescription.equals=" + DEFAULT_ORDERS_OPTION_DESCRIPTION);

        // Get all the ordersLineExtraList where ordersOptionDescription equals to UPDATED_ORDERS_OPTION_DESCRIPTION
        defaultOrdersLineExtraShouldNotBeFound("ordersOptionDescription.equals=" + UPDATED_ORDERS_OPTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllOrdersLineExtrasByOrdersOptionDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        ordersLineExtraRepository.saveAndFlush(ordersLineExtra);

        // Get all the ordersLineExtraList where ordersOptionDescription in DEFAULT_ORDERS_OPTION_DESCRIPTION or UPDATED_ORDERS_OPTION_DESCRIPTION
        defaultOrdersLineExtraShouldBeFound("ordersOptionDescription.in=" + DEFAULT_ORDERS_OPTION_DESCRIPTION + "," + UPDATED_ORDERS_OPTION_DESCRIPTION);

        // Get all the ordersLineExtraList where ordersOptionDescription equals to UPDATED_ORDERS_OPTION_DESCRIPTION
        defaultOrdersLineExtraShouldNotBeFound("ordersOptionDescription.in=" + UPDATED_ORDERS_OPTION_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllOrdersLineExtrasByOrdersOptionDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordersLineExtraRepository.saveAndFlush(ordersLineExtra);

        // Get all the ordersLineExtraList where ordersOptionDescription is not null
        defaultOrdersLineExtraShouldBeFound("ordersOptionDescription.specified=true");

        // Get all the ordersLineExtraList where ordersOptionDescription is null
        defaultOrdersLineExtraShouldNotBeFound("ordersOptionDescription.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrdersLineExtrasByFullPhotoUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        ordersLineExtraRepository.saveAndFlush(ordersLineExtra);

        // Get all the ordersLineExtraList where fullPhotoUrl equals to DEFAULT_FULL_PHOTO_URL
        defaultOrdersLineExtraShouldBeFound("fullPhotoUrl.equals=" + DEFAULT_FULL_PHOTO_URL);

        // Get all the ordersLineExtraList where fullPhotoUrl equals to UPDATED_FULL_PHOTO_URL
        defaultOrdersLineExtraShouldNotBeFound("fullPhotoUrl.equals=" + UPDATED_FULL_PHOTO_URL);
    }

    @Test
    @Transactional
    public void getAllOrdersLineExtrasByFullPhotoUrlIsInShouldWork() throws Exception {
        // Initialize the database
        ordersLineExtraRepository.saveAndFlush(ordersLineExtra);

        // Get all the ordersLineExtraList where fullPhotoUrl in DEFAULT_FULL_PHOTO_URL or UPDATED_FULL_PHOTO_URL
        defaultOrdersLineExtraShouldBeFound("fullPhotoUrl.in=" + DEFAULT_FULL_PHOTO_URL + "," + UPDATED_FULL_PHOTO_URL);

        // Get all the ordersLineExtraList where fullPhotoUrl equals to UPDATED_FULL_PHOTO_URL
        defaultOrdersLineExtraShouldNotBeFound("fullPhotoUrl.in=" + UPDATED_FULL_PHOTO_URL);
    }

    @Test
    @Transactional
    public void getAllOrdersLineExtrasByFullPhotoUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordersLineExtraRepository.saveAndFlush(ordersLineExtra);

        // Get all the ordersLineExtraList where fullPhotoUrl is not null
        defaultOrdersLineExtraShouldBeFound("fullPhotoUrl.specified=true");

        // Get all the ordersLineExtraList where fullPhotoUrl is null
        defaultOrdersLineExtraShouldNotBeFound("fullPhotoUrl.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrdersLineExtrasByThumbnailPhotoUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        ordersLineExtraRepository.saveAndFlush(ordersLineExtra);

        // Get all the ordersLineExtraList where thumbnailPhotoUrl equals to DEFAULT_THUMBNAIL_PHOTO_URL
        defaultOrdersLineExtraShouldBeFound("thumbnailPhotoUrl.equals=" + DEFAULT_THUMBNAIL_PHOTO_URL);

        // Get all the ordersLineExtraList where thumbnailPhotoUrl equals to UPDATED_THUMBNAIL_PHOTO_URL
        defaultOrdersLineExtraShouldNotBeFound("thumbnailPhotoUrl.equals=" + UPDATED_THUMBNAIL_PHOTO_URL);
    }

    @Test
    @Transactional
    public void getAllOrdersLineExtrasByThumbnailPhotoUrlIsInShouldWork() throws Exception {
        // Initialize the database
        ordersLineExtraRepository.saveAndFlush(ordersLineExtra);

        // Get all the ordersLineExtraList where thumbnailPhotoUrl in DEFAULT_THUMBNAIL_PHOTO_URL or UPDATED_THUMBNAIL_PHOTO_URL
        defaultOrdersLineExtraShouldBeFound("thumbnailPhotoUrl.in=" + DEFAULT_THUMBNAIL_PHOTO_URL + "," + UPDATED_THUMBNAIL_PHOTO_URL);

        // Get all the ordersLineExtraList where thumbnailPhotoUrl equals to UPDATED_THUMBNAIL_PHOTO_URL
        defaultOrdersLineExtraShouldNotBeFound("thumbnailPhotoUrl.in=" + UPDATED_THUMBNAIL_PHOTO_URL);
    }

    @Test
    @Transactional
    public void getAllOrdersLineExtrasByThumbnailPhotoUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordersLineExtraRepository.saveAndFlush(ordersLineExtra);

        // Get all the ordersLineExtraList where thumbnailPhotoUrl is not null
        defaultOrdersLineExtraShouldBeFound("thumbnailPhotoUrl.specified=true");

        // Get all the ordersLineExtraList where thumbnailPhotoUrl is null
        defaultOrdersLineExtraShouldNotBeFound("thumbnailPhotoUrl.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrdersLineExtrasByOrdersLineVariantIsEqualToSomething() throws Exception {
        // Initialize the database
        OrdersLineVariant ordersLineVariant = OrdersLineVariantResourceIntTest.createEntity(em);
        em.persist(ordersLineVariant);
        em.flush();
        ordersLineExtra.setOrdersLineVariant(ordersLineVariant);
        ordersLineExtraRepository.saveAndFlush(ordersLineExtra);
        Long ordersLineVariantId = ordersLineVariant.getId();

        // Get all the ordersLineExtraList where ordersLineVariant equals to ordersLineVariantId
        defaultOrdersLineExtraShouldBeFound("ordersLineVariantId.equals=" + ordersLineVariantId);

        // Get all the ordersLineExtraList where ordersLineVariant equals to ordersLineVariantId + 1
        defaultOrdersLineExtraShouldNotBeFound("ordersLineVariantId.equals=" + (ordersLineVariantId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultOrdersLineExtraShouldBeFound(String filter) throws Exception {
        restOrdersLineExtraMockMvc.perform(get("/api/orders-line-extras?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ordersLineExtra.getId().intValue())))
            .andExpect(jsonPath("$.[*].ordersLineExtraName").value(hasItem(DEFAULT_ORDERS_LINE_EXTRA_NAME.toString())))
            .andExpect(jsonPath("$.[*].ordersLineExtraValue").value(hasItem(DEFAULT_ORDERS_LINE_EXTRA_VALUE.toString())))
            .andExpect(jsonPath("$.[*].ordersLineExtraPrice").value(hasItem(DEFAULT_ORDERS_LINE_EXTRA_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].ordersOptionDescription").value(hasItem(DEFAULT_ORDERS_OPTION_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].fullPhotoContentType").value(hasItem(DEFAULT_FULL_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fullPhoto").value(hasItem(Base64Utils.encodeToString(DEFAULT_FULL_PHOTO))))
            .andExpect(jsonPath("$.[*].fullPhotoUrl").value(hasItem(DEFAULT_FULL_PHOTO_URL.toString())))
            .andExpect(jsonPath("$.[*].thumbnailPhotoContentType").value(hasItem(DEFAULT_THUMBNAIL_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].thumbnailPhoto").value(hasItem(Base64Utils.encodeToString(DEFAULT_THUMBNAIL_PHOTO))))
            .andExpect(jsonPath("$.[*].thumbnailPhotoUrl").value(hasItem(DEFAULT_THUMBNAIL_PHOTO_URL.toString())));

        // Check, that the count call also returns 1
        restOrdersLineExtraMockMvc.perform(get("/api/orders-line-extras/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultOrdersLineExtraShouldNotBeFound(String filter) throws Exception {
        restOrdersLineExtraMockMvc.perform(get("/api/orders-line-extras?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrdersLineExtraMockMvc.perform(get("/api/orders-line-extras/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingOrdersLineExtra() throws Exception {
        // Get the ordersLineExtra
        restOrdersLineExtraMockMvc.perform(get("/api/orders-line-extras/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrdersLineExtra() throws Exception {
        // Initialize the database
        ordersLineExtraRepository.saveAndFlush(ordersLineExtra);

        int databaseSizeBeforeUpdate = ordersLineExtraRepository.findAll().size();

        // Update the ordersLineExtra
        OrdersLineExtra updatedOrdersLineExtra = ordersLineExtraRepository.findById(ordersLineExtra.getId()).get();
        // Disconnect from session so that the updates on updatedOrdersLineExtra are not directly saved in db
        em.detach(updatedOrdersLineExtra);
        updatedOrdersLineExtra
            .ordersLineExtraName(UPDATED_ORDERS_LINE_EXTRA_NAME)
            .ordersLineExtraValue(UPDATED_ORDERS_LINE_EXTRA_VALUE)
            .ordersLineExtraPrice(UPDATED_ORDERS_LINE_EXTRA_PRICE)
            .ordersOptionDescription(UPDATED_ORDERS_OPTION_DESCRIPTION)
            .fullPhoto(UPDATED_FULL_PHOTO)
            .fullPhotoContentType(UPDATED_FULL_PHOTO_CONTENT_TYPE)
            .fullPhotoUrl(UPDATED_FULL_PHOTO_URL)
            .thumbnailPhoto(UPDATED_THUMBNAIL_PHOTO)
            .thumbnailPhotoContentType(UPDATED_THUMBNAIL_PHOTO_CONTENT_TYPE)
            .thumbnailPhotoUrl(UPDATED_THUMBNAIL_PHOTO_URL);
        OrdersLineExtraDTO ordersLineExtraDTO = ordersLineExtraMapper.toDto(updatedOrdersLineExtra);

        restOrdersLineExtraMockMvc.perform(put("/api/orders-line-extras")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ordersLineExtraDTO)))
            .andExpect(status().isOk());

        // Validate the OrdersLineExtra in the database
        List<OrdersLineExtra> ordersLineExtraList = ordersLineExtraRepository.findAll();
        assertThat(ordersLineExtraList).hasSize(databaseSizeBeforeUpdate);
        OrdersLineExtra testOrdersLineExtra = ordersLineExtraList.get(ordersLineExtraList.size() - 1);
        assertThat(testOrdersLineExtra.getOrdersLineExtraName()).isEqualTo(UPDATED_ORDERS_LINE_EXTRA_NAME);
        assertThat(testOrdersLineExtra.getOrdersLineExtraValue()).isEqualTo(UPDATED_ORDERS_LINE_EXTRA_VALUE);
        assertThat(testOrdersLineExtra.getOrdersLineExtraPrice()).isEqualTo(UPDATED_ORDERS_LINE_EXTRA_PRICE);
        assertThat(testOrdersLineExtra.getOrdersOptionDescription()).isEqualTo(UPDATED_ORDERS_OPTION_DESCRIPTION);
        assertThat(testOrdersLineExtra.getFullPhoto()).isEqualTo(UPDATED_FULL_PHOTO);
        assertThat(testOrdersLineExtra.getFullPhotoContentType()).isEqualTo(UPDATED_FULL_PHOTO_CONTENT_TYPE);
        assertThat(testOrdersLineExtra.getFullPhotoUrl()).isEqualTo(UPDATED_FULL_PHOTO_URL);
        assertThat(testOrdersLineExtra.getThumbnailPhoto()).isEqualTo(UPDATED_THUMBNAIL_PHOTO);
        assertThat(testOrdersLineExtra.getThumbnailPhotoContentType()).isEqualTo(UPDATED_THUMBNAIL_PHOTO_CONTENT_TYPE);
        assertThat(testOrdersLineExtra.getThumbnailPhotoUrl()).isEqualTo(UPDATED_THUMBNAIL_PHOTO_URL);

        // Validate the OrdersLineExtra in Elasticsearch
        verify(mockOrdersLineExtraSearchRepository, times(1)).save(testOrdersLineExtra);
    }

    @Test
    @Transactional
    public void updateNonExistingOrdersLineExtra() throws Exception {
        int databaseSizeBeforeUpdate = ordersLineExtraRepository.findAll().size();

        // Create the OrdersLineExtra
        OrdersLineExtraDTO ordersLineExtraDTO = ordersLineExtraMapper.toDto(ordersLineExtra);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdersLineExtraMockMvc.perform(put("/api/orders-line-extras")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ordersLineExtraDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OrdersLineExtra in the database
        List<OrdersLineExtra> ordersLineExtraList = ordersLineExtraRepository.findAll();
        assertThat(ordersLineExtraList).hasSize(databaseSizeBeforeUpdate);

        // Validate the OrdersLineExtra in Elasticsearch
        verify(mockOrdersLineExtraSearchRepository, times(0)).save(ordersLineExtra);
    }

    @Test
    @Transactional
    public void deleteOrdersLineExtra() throws Exception {
        // Initialize the database
        ordersLineExtraRepository.saveAndFlush(ordersLineExtra);

        int databaseSizeBeforeDelete = ordersLineExtraRepository.findAll().size();

        // Get the ordersLineExtra
        restOrdersLineExtraMockMvc.perform(delete("/api/orders-line-extras/{id}", ordersLineExtra.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<OrdersLineExtra> ordersLineExtraList = ordersLineExtraRepository.findAll();
        assertThat(ordersLineExtraList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the OrdersLineExtra in Elasticsearch
        verify(mockOrdersLineExtraSearchRepository, times(1)).deleteById(ordersLineExtra.getId());
    }

    @Test
    @Transactional
    public void searchOrdersLineExtra() throws Exception {
        // Initialize the database
        ordersLineExtraRepository.saveAndFlush(ordersLineExtra);
        when(mockOrdersLineExtraSearchRepository.search(queryStringQuery("id:" + ordersLineExtra.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(ordersLineExtra), PageRequest.of(0, 1), 1));
        // Search the ordersLineExtra
        restOrdersLineExtraMockMvc.perform(get("/api/_search/orders-line-extras?query=id:" + ordersLineExtra.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ordersLineExtra.getId().intValue())))
            .andExpect(jsonPath("$.[*].ordersLineExtraName").value(hasItem(DEFAULT_ORDERS_LINE_EXTRA_NAME)))
            .andExpect(jsonPath("$.[*].ordersLineExtraValue").value(hasItem(DEFAULT_ORDERS_LINE_EXTRA_VALUE)))
            .andExpect(jsonPath("$.[*].ordersLineExtraPrice").value(hasItem(DEFAULT_ORDERS_LINE_EXTRA_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].ordersOptionDescription").value(hasItem(DEFAULT_ORDERS_OPTION_DESCRIPTION)))
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
        TestUtil.equalsVerifier(OrdersLineExtra.class);
        OrdersLineExtra ordersLineExtra1 = new OrdersLineExtra();
        ordersLineExtra1.setId(1L);
        OrdersLineExtra ordersLineExtra2 = new OrdersLineExtra();
        ordersLineExtra2.setId(ordersLineExtra1.getId());
        assertThat(ordersLineExtra1).isEqualTo(ordersLineExtra2);
        ordersLineExtra2.setId(2L);
        assertThat(ordersLineExtra1).isNotEqualTo(ordersLineExtra2);
        ordersLineExtra1.setId(null);
        assertThat(ordersLineExtra1).isNotEqualTo(ordersLineExtra2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrdersLineExtraDTO.class);
        OrdersLineExtraDTO ordersLineExtraDTO1 = new OrdersLineExtraDTO();
        ordersLineExtraDTO1.setId(1L);
        OrdersLineExtraDTO ordersLineExtraDTO2 = new OrdersLineExtraDTO();
        assertThat(ordersLineExtraDTO1).isNotEqualTo(ordersLineExtraDTO2);
        ordersLineExtraDTO2.setId(ordersLineExtraDTO1.getId());
        assertThat(ordersLineExtraDTO1).isEqualTo(ordersLineExtraDTO2);
        ordersLineExtraDTO2.setId(2L);
        assertThat(ordersLineExtraDTO1).isNotEqualTo(ordersLineExtraDTO2);
        ordersLineExtraDTO1.setId(null);
        assertThat(ordersLineExtraDTO1).isNotEqualTo(ordersLineExtraDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(ordersLineExtraMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(ordersLineExtraMapper.fromId(null)).isNull();
    }
}
