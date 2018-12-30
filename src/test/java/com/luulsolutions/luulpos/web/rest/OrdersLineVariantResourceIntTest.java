package com.luulsolutions.luulpos.web.rest;

import com.luulsolutions.luulpos.LuulposApp;

import com.luulsolutions.luulpos.domain.OrdersLineVariant;
import com.luulsolutions.luulpos.domain.OrdersLine;
import com.luulsolutions.luulpos.domain.OrdersLineExtra;
import com.luulsolutions.luulpos.repository.OrdersLineVariantRepository;
import com.luulsolutions.luulpos.repository.search.OrdersLineVariantSearchRepository;
import com.luulsolutions.luulpos.service.OrdersLineVariantService;
import com.luulsolutions.luulpos.service.dto.OrdersLineVariantDTO;
import com.luulsolutions.luulpos.service.mapper.OrdersLineVariantMapper;
import com.luulsolutions.luulpos.web.rest.errors.ExceptionTranslator;
import com.luulsolutions.luulpos.service.dto.OrdersLineVariantCriteria;
import com.luulsolutions.luulpos.service.OrdersLineVariantQueryService;

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
 * Test class for the OrdersLineVariantResource REST controller.
 *
 * @see OrdersLineVariantResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LuulposApp.class)
public class OrdersLineVariantResourceIntTest {

    private static final String DEFAULT_VARIANT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_VARIANT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_VARIANT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VARIANT_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Float DEFAULT_PERCENTAGE = 1F;
    private static final Float UPDATED_PERCENTAGE = 2F;

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

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);

    @Autowired
    private OrdersLineVariantRepository ordersLineVariantRepository;

    @Autowired
    private OrdersLineVariantMapper ordersLineVariantMapper;

    @Autowired
    private OrdersLineVariantService ordersLineVariantService;

    /**
     * This repository is mocked in the com.luulsolutions.luulpos.repository.search test package.
     *
     * @see com.luulsolutions.luulpos.repository.search.OrdersLineVariantSearchRepositoryMockConfiguration
     */
    @Autowired
    private OrdersLineVariantSearchRepository mockOrdersLineVariantSearchRepository;

    @Autowired
    private OrdersLineVariantQueryService ordersLineVariantQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restOrdersLineVariantMockMvc;

    private OrdersLineVariant ordersLineVariant;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OrdersLineVariantResource ordersLineVariantResource = new OrdersLineVariantResource(ordersLineVariantService, ordersLineVariantQueryService);
        this.restOrdersLineVariantMockMvc = MockMvcBuilders.standaloneSetup(ordersLineVariantResource)
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
    public static OrdersLineVariant createEntity(EntityManager em) {
        OrdersLineVariant ordersLineVariant = new OrdersLineVariant()
            .variantName(DEFAULT_VARIANT_NAME)
            .variantValue(DEFAULT_VARIANT_VALUE)
            .description(DEFAULT_DESCRIPTION)
            .percentage(DEFAULT_PERCENTAGE)
            .fullPhoto(DEFAULT_FULL_PHOTO)
            .fullPhotoContentType(DEFAULT_FULL_PHOTO_CONTENT_TYPE)
            .fullPhotoUrl(DEFAULT_FULL_PHOTO_URL)
            .thumbnailPhoto(DEFAULT_THUMBNAIL_PHOTO)
            .thumbnailPhotoContentType(DEFAULT_THUMBNAIL_PHOTO_CONTENT_TYPE)
            .thumbnailPhotoUrl(DEFAULT_THUMBNAIL_PHOTO_URL)
            .price(DEFAULT_PRICE);
        return ordersLineVariant;
    }

    @Before
    public void initTest() {
        ordersLineVariant = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrdersLineVariant() throws Exception {
        int databaseSizeBeforeCreate = ordersLineVariantRepository.findAll().size();

        // Create the OrdersLineVariant
        OrdersLineVariantDTO ordersLineVariantDTO = ordersLineVariantMapper.toDto(ordersLineVariant);
        restOrdersLineVariantMockMvc.perform(post("/api/orders-line-variants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ordersLineVariantDTO)))
            .andExpect(status().isCreated());

        // Validate the OrdersLineVariant in the database
        List<OrdersLineVariant> ordersLineVariantList = ordersLineVariantRepository.findAll();
        assertThat(ordersLineVariantList).hasSize(databaseSizeBeforeCreate + 1);
        OrdersLineVariant testOrdersLineVariant = ordersLineVariantList.get(ordersLineVariantList.size() - 1);
        assertThat(testOrdersLineVariant.getVariantName()).isEqualTo(DEFAULT_VARIANT_NAME);
        assertThat(testOrdersLineVariant.getVariantValue()).isEqualTo(DEFAULT_VARIANT_VALUE);
        assertThat(testOrdersLineVariant.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testOrdersLineVariant.getPercentage()).isEqualTo(DEFAULT_PERCENTAGE);
        assertThat(testOrdersLineVariant.getFullPhoto()).isEqualTo(DEFAULT_FULL_PHOTO);
        assertThat(testOrdersLineVariant.getFullPhotoContentType()).isEqualTo(DEFAULT_FULL_PHOTO_CONTENT_TYPE);
        assertThat(testOrdersLineVariant.getFullPhotoUrl()).isEqualTo(DEFAULT_FULL_PHOTO_URL);
        assertThat(testOrdersLineVariant.getThumbnailPhoto()).isEqualTo(DEFAULT_THUMBNAIL_PHOTO);
        assertThat(testOrdersLineVariant.getThumbnailPhotoContentType()).isEqualTo(DEFAULT_THUMBNAIL_PHOTO_CONTENT_TYPE);
        assertThat(testOrdersLineVariant.getThumbnailPhotoUrl()).isEqualTo(DEFAULT_THUMBNAIL_PHOTO_URL);
        assertThat(testOrdersLineVariant.getPrice()).isEqualTo(DEFAULT_PRICE);

        // Validate the OrdersLineVariant in Elasticsearch
        verify(mockOrdersLineVariantSearchRepository, times(1)).save(testOrdersLineVariant);
    }

    @Test
    @Transactional
    public void createOrdersLineVariantWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ordersLineVariantRepository.findAll().size();

        // Create the OrdersLineVariant with an existing ID
        ordersLineVariant.setId(1L);
        OrdersLineVariantDTO ordersLineVariantDTO = ordersLineVariantMapper.toDto(ordersLineVariant);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrdersLineVariantMockMvc.perform(post("/api/orders-line-variants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ordersLineVariantDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OrdersLineVariant in the database
        List<OrdersLineVariant> ordersLineVariantList = ordersLineVariantRepository.findAll();
        assertThat(ordersLineVariantList).hasSize(databaseSizeBeforeCreate);

        // Validate the OrdersLineVariant in Elasticsearch
        verify(mockOrdersLineVariantSearchRepository, times(0)).save(ordersLineVariant);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordersLineVariantRepository.findAll().size();
        // set the field null
        ordersLineVariant.setPrice(null);

        // Create the OrdersLineVariant, which fails.
        OrdersLineVariantDTO ordersLineVariantDTO = ordersLineVariantMapper.toDto(ordersLineVariant);

        restOrdersLineVariantMockMvc.perform(post("/api/orders-line-variants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ordersLineVariantDTO)))
            .andExpect(status().isBadRequest());

        List<OrdersLineVariant> ordersLineVariantList = ordersLineVariantRepository.findAll();
        assertThat(ordersLineVariantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOrdersLineVariants() throws Exception {
        // Initialize the database
        ordersLineVariantRepository.saveAndFlush(ordersLineVariant);

        // Get all the ordersLineVariantList
        restOrdersLineVariantMockMvc.perform(get("/api/orders-line-variants?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ordersLineVariant.getId().intValue())))
            .andExpect(jsonPath("$.[*].variantName").value(hasItem(DEFAULT_VARIANT_NAME.toString())))
            .andExpect(jsonPath("$.[*].variantValue").value(hasItem(DEFAULT_VARIANT_VALUE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].percentage").value(hasItem(DEFAULT_PERCENTAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].fullPhotoContentType").value(hasItem(DEFAULT_FULL_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fullPhoto").value(hasItem(Base64Utils.encodeToString(DEFAULT_FULL_PHOTO))))
            .andExpect(jsonPath("$.[*].fullPhotoUrl").value(hasItem(DEFAULT_FULL_PHOTO_URL.toString())))
            .andExpect(jsonPath("$.[*].thumbnailPhotoContentType").value(hasItem(DEFAULT_THUMBNAIL_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].thumbnailPhoto").value(hasItem(Base64Utils.encodeToString(DEFAULT_THUMBNAIL_PHOTO))))
            .andExpect(jsonPath("$.[*].thumbnailPhotoUrl").value(hasItem(DEFAULT_THUMBNAIL_PHOTO_URL.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())));
    }
    
    @Test
    @Transactional
    public void getOrdersLineVariant() throws Exception {
        // Initialize the database
        ordersLineVariantRepository.saveAndFlush(ordersLineVariant);

        // Get the ordersLineVariant
        restOrdersLineVariantMockMvc.perform(get("/api/orders-line-variants/{id}", ordersLineVariant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ordersLineVariant.getId().intValue()))
            .andExpect(jsonPath("$.variantName").value(DEFAULT_VARIANT_NAME.toString()))
            .andExpect(jsonPath("$.variantValue").value(DEFAULT_VARIANT_VALUE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.percentage").value(DEFAULT_PERCENTAGE.doubleValue()))
            .andExpect(jsonPath("$.fullPhotoContentType").value(DEFAULT_FULL_PHOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.fullPhoto").value(Base64Utils.encodeToString(DEFAULT_FULL_PHOTO)))
            .andExpect(jsonPath("$.fullPhotoUrl").value(DEFAULT_FULL_PHOTO_URL.toString()))
            .andExpect(jsonPath("$.thumbnailPhotoContentType").value(DEFAULT_THUMBNAIL_PHOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.thumbnailPhoto").value(Base64Utils.encodeToString(DEFAULT_THUMBNAIL_PHOTO)))
            .andExpect(jsonPath("$.thumbnailPhotoUrl").value(DEFAULT_THUMBNAIL_PHOTO_URL.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()));
    }

    @Test
    @Transactional
    public void getAllOrdersLineVariantsByVariantNameIsEqualToSomething() throws Exception {
        // Initialize the database
        ordersLineVariantRepository.saveAndFlush(ordersLineVariant);

        // Get all the ordersLineVariantList where variantName equals to DEFAULT_VARIANT_NAME
        defaultOrdersLineVariantShouldBeFound("variantName.equals=" + DEFAULT_VARIANT_NAME);

        // Get all the ordersLineVariantList where variantName equals to UPDATED_VARIANT_NAME
        defaultOrdersLineVariantShouldNotBeFound("variantName.equals=" + UPDATED_VARIANT_NAME);
    }

    @Test
    @Transactional
    public void getAllOrdersLineVariantsByVariantNameIsInShouldWork() throws Exception {
        // Initialize the database
        ordersLineVariantRepository.saveAndFlush(ordersLineVariant);

        // Get all the ordersLineVariantList where variantName in DEFAULT_VARIANT_NAME or UPDATED_VARIANT_NAME
        defaultOrdersLineVariantShouldBeFound("variantName.in=" + DEFAULT_VARIANT_NAME + "," + UPDATED_VARIANT_NAME);

        // Get all the ordersLineVariantList where variantName equals to UPDATED_VARIANT_NAME
        defaultOrdersLineVariantShouldNotBeFound("variantName.in=" + UPDATED_VARIANT_NAME);
    }

    @Test
    @Transactional
    public void getAllOrdersLineVariantsByVariantNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordersLineVariantRepository.saveAndFlush(ordersLineVariant);

        // Get all the ordersLineVariantList where variantName is not null
        defaultOrdersLineVariantShouldBeFound("variantName.specified=true");

        // Get all the ordersLineVariantList where variantName is null
        defaultOrdersLineVariantShouldNotBeFound("variantName.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrdersLineVariantsByVariantValueIsEqualToSomething() throws Exception {
        // Initialize the database
        ordersLineVariantRepository.saveAndFlush(ordersLineVariant);

        // Get all the ordersLineVariantList where variantValue equals to DEFAULT_VARIANT_VALUE
        defaultOrdersLineVariantShouldBeFound("variantValue.equals=" + DEFAULT_VARIANT_VALUE);

        // Get all the ordersLineVariantList where variantValue equals to UPDATED_VARIANT_VALUE
        defaultOrdersLineVariantShouldNotBeFound("variantValue.equals=" + UPDATED_VARIANT_VALUE);
    }

    @Test
    @Transactional
    public void getAllOrdersLineVariantsByVariantValueIsInShouldWork() throws Exception {
        // Initialize the database
        ordersLineVariantRepository.saveAndFlush(ordersLineVariant);

        // Get all the ordersLineVariantList where variantValue in DEFAULT_VARIANT_VALUE or UPDATED_VARIANT_VALUE
        defaultOrdersLineVariantShouldBeFound("variantValue.in=" + DEFAULT_VARIANT_VALUE + "," + UPDATED_VARIANT_VALUE);

        // Get all the ordersLineVariantList where variantValue equals to UPDATED_VARIANT_VALUE
        defaultOrdersLineVariantShouldNotBeFound("variantValue.in=" + UPDATED_VARIANT_VALUE);
    }

    @Test
    @Transactional
    public void getAllOrdersLineVariantsByVariantValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordersLineVariantRepository.saveAndFlush(ordersLineVariant);

        // Get all the ordersLineVariantList where variantValue is not null
        defaultOrdersLineVariantShouldBeFound("variantValue.specified=true");

        // Get all the ordersLineVariantList where variantValue is null
        defaultOrdersLineVariantShouldNotBeFound("variantValue.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrdersLineVariantsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        ordersLineVariantRepository.saveAndFlush(ordersLineVariant);

        // Get all the ordersLineVariantList where description equals to DEFAULT_DESCRIPTION
        defaultOrdersLineVariantShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the ordersLineVariantList where description equals to UPDATED_DESCRIPTION
        defaultOrdersLineVariantShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllOrdersLineVariantsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        ordersLineVariantRepository.saveAndFlush(ordersLineVariant);

        // Get all the ordersLineVariantList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultOrdersLineVariantShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the ordersLineVariantList where description equals to UPDATED_DESCRIPTION
        defaultOrdersLineVariantShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllOrdersLineVariantsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordersLineVariantRepository.saveAndFlush(ordersLineVariant);

        // Get all the ordersLineVariantList where description is not null
        defaultOrdersLineVariantShouldBeFound("description.specified=true");

        // Get all the ordersLineVariantList where description is null
        defaultOrdersLineVariantShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrdersLineVariantsByPercentageIsEqualToSomething() throws Exception {
        // Initialize the database
        ordersLineVariantRepository.saveAndFlush(ordersLineVariant);

        // Get all the ordersLineVariantList where percentage equals to DEFAULT_PERCENTAGE
        defaultOrdersLineVariantShouldBeFound("percentage.equals=" + DEFAULT_PERCENTAGE);

        // Get all the ordersLineVariantList where percentage equals to UPDATED_PERCENTAGE
        defaultOrdersLineVariantShouldNotBeFound("percentage.equals=" + UPDATED_PERCENTAGE);
    }

    @Test
    @Transactional
    public void getAllOrdersLineVariantsByPercentageIsInShouldWork() throws Exception {
        // Initialize the database
        ordersLineVariantRepository.saveAndFlush(ordersLineVariant);

        // Get all the ordersLineVariantList where percentage in DEFAULT_PERCENTAGE or UPDATED_PERCENTAGE
        defaultOrdersLineVariantShouldBeFound("percentage.in=" + DEFAULT_PERCENTAGE + "," + UPDATED_PERCENTAGE);

        // Get all the ordersLineVariantList where percentage equals to UPDATED_PERCENTAGE
        defaultOrdersLineVariantShouldNotBeFound("percentage.in=" + UPDATED_PERCENTAGE);
    }

    @Test
    @Transactional
    public void getAllOrdersLineVariantsByPercentageIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordersLineVariantRepository.saveAndFlush(ordersLineVariant);

        // Get all the ordersLineVariantList where percentage is not null
        defaultOrdersLineVariantShouldBeFound("percentage.specified=true");

        // Get all the ordersLineVariantList where percentage is null
        defaultOrdersLineVariantShouldNotBeFound("percentage.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrdersLineVariantsByFullPhotoUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        ordersLineVariantRepository.saveAndFlush(ordersLineVariant);

        // Get all the ordersLineVariantList where fullPhotoUrl equals to DEFAULT_FULL_PHOTO_URL
        defaultOrdersLineVariantShouldBeFound("fullPhotoUrl.equals=" + DEFAULT_FULL_PHOTO_URL);

        // Get all the ordersLineVariantList where fullPhotoUrl equals to UPDATED_FULL_PHOTO_URL
        defaultOrdersLineVariantShouldNotBeFound("fullPhotoUrl.equals=" + UPDATED_FULL_PHOTO_URL);
    }

    @Test
    @Transactional
    public void getAllOrdersLineVariantsByFullPhotoUrlIsInShouldWork() throws Exception {
        // Initialize the database
        ordersLineVariantRepository.saveAndFlush(ordersLineVariant);

        // Get all the ordersLineVariantList where fullPhotoUrl in DEFAULT_FULL_PHOTO_URL or UPDATED_FULL_PHOTO_URL
        defaultOrdersLineVariantShouldBeFound("fullPhotoUrl.in=" + DEFAULT_FULL_PHOTO_URL + "," + UPDATED_FULL_PHOTO_URL);

        // Get all the ordersLineVariantList where fullPhotoUrl equals to UPDATED_FULL_PHOTO_URL
        defaultOrdersLineVariantShouldNotBeFound("fullPhotoUrl.in=" + UPDATED_FULL_PHOTO_URL);
    }

    @Test
    @Transactional
    public void getAllOrdersLineVariantsByFullPhotoUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordersLineVariantRepository.saveAndFlush(ordersLineVariant);

        // Get all the ordersLineVariantList where fullPhotoUrl is not null
        defaultOrdersLineVariantShouldBeFound("fullPhotoUrl.specified=true");

        // Get all the ordersLineVariantList where fullPhotoUrl is null
        defaultOrdersLineVariantShouldNotBeFound("fullPhotoUrl.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrdersLineVariantsByThumbnailPhotoUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        ordersLineVariantRepository.saveAndFlush(ordersLineVariant);

        // Get all the ordersLineVariantList where thumbnailPhotoUrl equals to DEFAULT_THUMBNAIL_PHOTO_URL
        defaultOrdersLineVariantShouldBeFound("thumbnailPhotoUrl.equals=" + DEFAULT_THUMBNAIL_PHOTO_URL);

        // Get all the ordersLineVariantList where thumbnailPhotoUrl equals to UPDATED_THUMBNAIL_PHOTO_URL
        defaultOrdersLineVariantShouldNotBeFound("thumbnailPhotoUrl.equals=" + UPDATED_THUMBNAIL_PHOTO_URL);
    }

    @Test
    @Transactional
    public void getAllOrdersLineVariantsByThumbnailPhotoUrlIsInShouldWork() throws Exception {
        // Initialize the database
        ordersLineVariantRepository.saveAndFlush(ordersLineVariant);

        // Get all the ordersLineVariantList where thumbnailPhotoUrl in DEFAULT_THUMBNAIL_PHOTO_URL or UPDATED_THUMBNAIL_PHOTO_URL
        defaultOrdersLineVariantShouldBeFound("thumbnailPhotoUrl.in=" + DEFAULT_THUMBNAIL_PHOTO_URL + "," + UPDATED_THUMBNAIL_PHOTO_URL);

        // Get all the ordersLineVariantList where thumbnailPhotoUrl equals to UPDATED_THUMBNAIL_PHOTO_URL
        defaultOrdersLineVariantShouldNotBeFound("thumbnailPhotoUrl.in=" + UPDATED_THUMBNAIL_PHOTO_URL);
    }

    @Test
    @Transactional
    public void getAllOrdersLineVariantsByThumbnailPhotoUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordersLineVariantRepository.saveAndFlush(ordersLineVariant);

        // Get all the ordersLineVariantList where thumbnailPhotoUrl is not null
        defaultOrdersLineVariantShouldBeFound("thumbnailPhotoUrl.specified=true");

        // Get all the ordersLineVariantList where thumbnailPhotoUrl is null
        defaultOrdersLineVariantShouldNotBeFound("thumbnailPhotoUrl.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrdersLineVariantsByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        ordersLineVariantRepository.saveAndFlush(ordersLineVariant);

        // Get all the ordersLineVariantList where price equals to DEFAULT_PRICE
        defaultOrdersLineVariantShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the ordersLineVariantList where price equals to UPDATED_PRICE
        defaultOrdersLineVariantShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllOrdersLineVariantsByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        ordersLineVariantRepository.saveAndFlush(ordersLineVariant);

        // Get all the ordersLineVariantList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultOrdersLineVariantShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the ordersLineVariantList where price equals to UPDATED_PRICE
        defaultOrdersLineVariantShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllOrdersLineVariantsByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordersLineVariantRepository.saveAndFlush(ordersLineVariant);

        // Get all the ordersLineVariantList where price is not null
        defaultOrdersLineVariantShouldBeFound("price.specified=true");

        // Get all the ordersLineVariantList where price is null
        defaultOrdersLineVariantShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrdersLineVariantsByOrdersLineIsEqualToSomething() throws Exception {
        // Initialize the database
        OrdersLine ordersLine = OrdersLineResourceIntTest.createEntity(em);
        em.persist(ordersLine);
        em.flush();
        ordersLineVariant.setOrdersLine(ordersLine);
        ordersLineVariantRepository.saveAndFlush(ordersLineVariant);
        Long ordersLineId = ordersLine.getId();

        // Get all the ordersLineVariantList where ordersLine equals to ordersLineId
        defaultOrdersLineVariantShouldBeFound("ordersLineId.equals=" + ordersLineId);

        // Get all the ordersLineVariantList where ordersLine equals to ordersLineId + 1
        defaultOrdersLineVariantShouldNotBeFound("ordersLineId.equals=" + (ordersLineId + 1));
    }


    @Test
    @Transactional
    public void getAllOrdersLineVariantsByOrdersLineExtrasIsEqualToSomething() throws Exception {
        // Initialize the database
        OrdersLineExtra ordersLineExtras = OrdersLineExtraResourceIntTest.createEntity(em);
        em.persist(ordersLineExtras);
        em.flush();
        ordersLineVariant.addOrdersLineExtras(ordersLineExtras);
        ordersLineVariantRepository.saveAndFlush(ordersLineVariant);
        Long ordersLineExtrasId = ordersLineExtras.getId();

        // Get all the ordersLineVariantList where ordersLineExtras equals to ordersLineExtrasId
        defaultOrdersLineVariantShouldBeFound("ordersLineExtrasId.equals=" + ordersLineExtrasId);

        // Get all the ordersLineVariantList where ordersLineExtras equals to ordersLineExtrasId + 1
        defaultOrdersLineVariantShouldNotBeFound("ordersLineExtrasId.equals=" + (ordersLineExtrasId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultOrdersLineVariantShouldBeFound(String filter) throws Exception {
        restOrdersLineVariantMockMvc.perform(get("/api/orders-line-variants?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ordersLineVariant.getId().intValue())))
            .andExpect(jsonPath("$.[*].variantName").value(hasItem(DEFAULT_VARIANT_NAME.toString())))
            .andExpect(jsonPath("$.[*].variantValue").value(hasItem(DEFAULT_VARIANT_VALUE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].percentage").value(hasItem(DEFAULT_PERCENTAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].fullPhotoContentType").value(hasItem(DEFAULT_FULL_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fullPhoto").value(hasItem(Base64Utils.encodeToString(DEFAULT_FULL_PHOTO))))
            .andExpect(jsonPath("$.[*].fullPhotoUrl").value(hasItem(DEFAULT_FULL_PHOTO_URL.toString())))
            .andExpect(jsonPath("$.[*].thumbnailPhotoContentType").value(hasItem(DEFAULT_THUMBNAIL_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].thumbnailPhoto").value(hasItem(Base64Utils.encodeToString(DEFAULT_THUMBNAIL_PHOTO))))
            .andExpect(jsonPath("$.[*].thumbnailPhotoUrl").value(hasItem(DEFAULT_THUMBNAIL_PHOTO_URL.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())));

        // Check, that the count call also returns 1
        restOrdersLineVariantMockMvc.perform(get("/api/orders-line-variants/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultOrdersLineVariantShouldNotBeFound(String filter) throws Exception {
        restOrdersLineVariantMockMvc.perform(get("/api/orders-line-variants?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrdersLineVariantMockMvc.perform(get("/api/orders-line-variants/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingOrdersLineVariant() throws Exception {
        // Get the ordersLineVariant
        restOrdersLineVariantMockMvc.perform(get("/api/orders-line-variants/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrdersLineVariant() throws Exception {
        // Initialize the database
        ordersLineVariantRepository.saveAndFlush(ordersLineVariant);

        int databaseSizeBeforeUpdate = ordersLineVariantRepository.findAll().size();

        // Update the ordersLineVariant
        OrdersLineVariant updatedOrdersLineVariant = ordersLineVariantRepository.findById(ordersLineVariant.getId()).get();
        // Disconnect from session so that the updates on updatedOrdersLineVariant are not directly saved in db
        em.detach(updatedOrdersLineVariant);
        updatedOrdersLineVariant
            .variantName(UPDATED_VARIANT_NAME)
            .variantValue(UPDATED_VARIANT_VALUE)
            .description(UPDATED_DESCRIPTION)
            .percentage(UPDATED_PERCENTAGE)
            .fullPhoto(UPDATED_FULL_PHOTO)
            .fullPhotoContentType(UPDATED_FULL_PHOTO_CONTENT_TYPE)
            .fullPhotoUrl(UPDATED_FULL_PHOTO_URL)
            .thumbnailPhoto(UPDATED_THUMBNAIL_PHOTO)
            .thumbnailPhotoContentType(UPDATED_THUMBNAIL_PHOTO_CONTENT_TYPE)
            .thumbnailPhotoUrl(UPDATED_THUMBNAIL_PHOTO_URL)
            .price(UPDATED_PRICE);
        OrdersLineVariantDTO ordersLineVariantDTO = ordersLineVariantMapper.toDto(updatedOrdersLineVariant);

        restOrdersLineVariantMockMvc.perform(put("/api/orders-line-variants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ordersLineVariantDTO)))
            .andExpect(status().isOk());

        // Validate the OrdersLineVariant in the database
        List<OrdersLineVariant> ordersLineVariantList = ordersLineVariantRepository.findAll();
        assertThat(ordersLineVariantList).hasSize(databaseSizeBeforeUpdate);
        OrdersLineVariant testOrdersLineVariant = ordersLineVariantList.get(ordersLineVariantList.size() - 1);
        assertThat(testOrdersLineVariant.getVariantName()).isEqualTo(UPDATED_VARIANT_NAME);
        assertThat(testOrdersLineVariant.getVariantValue()).isEqualTo(UPDATED_VARIANT_VALUE);
        assertThat(testOrdersLineVariant.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testOrdersLineVariant.getPercentage()).isEqualTo(UPDATED_PERCENTAGE);
        assertThat(testOrdersLineVariant.getFullPhoto()).isEqualTo(UPDATED_FULL_PHOTO);
        assertThat(testOrdersLineVariant.getFullPhotoContentType()).isEqualTo(UPDATED_FULL_PHOTO_CONTENT_TYPE);
        assertThat(testOrdersLineVariant.getFullPhotoUrl()).isEqualTo(UPDATED_FULL_PHOTO_URL);
        assertThat(testOrdersLineVariant.getThumbnailPhoto()).isEqualTo(UPDATED_THUMBNAIL_PHOTO);
        assertThat(testOrdersLineVariant.getThumbnailPhotoContentType()).isEqualTo(UPDATED_THUMBNAIL_PHOTO_CONTENT_TYPE);
        assertThat(testOrdersLineVariant.getThumbnailPhotoUrl()).isEqualTo(UPDATED_THUMBNAIL_PHOTO_URL);
        assertThat(testOrdersLineVariant.getPrice()).isEqualTo(UPDATED_PRICE);

        // Validate the OrdersLineVariant in Elasticsearch
        verify(mockOrdersLineVariantSearchRepository, times(1)).save(testOrdersLineVariant);
    }

    @Test
    @Transactional
    public void updateNonExistingOrdersLineVariant() throws Exception {
        int databaseSizeBeforeUpdate = ordersLineVariantRepository.findAll().size();

        // Create the OrdersLineVariant
        OrdersLineVariantDTO ordersLineVariantDTO = ordersLineVariantMapper.toDto(ordersLineVariant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdersLineVariantMockMvc.perform(put("/api/orders-line-variants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ordersLineVariantDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OrdersLineVariant in the database
        List<OrdersLineVariant> ordersLineVariantList = ordersLineVariantRepository.findAll();
        assertThat(ordersLineVariantList).hasSize(databaseSizeBeforeUpdate);

        // Validate the OrdersLineVariant in Elasticsearch
        verify(mockOrdersLineVariantSearchRepository, times(0)).save(ordersLineVariant);
    }

    @Test
    @Transactional
    public void deleteOrdersLineVariant() throws Exception {
        // Initialize the database
        ordersLineVariantRepository.saveAndFlush(ordersLineVariant);

        int databaseSizeBeforeDelete = ordersLineVariantRepository.findAll().size();

        // Get the ordersLineVariant
        restOrdersLineVariantMockMvc.perform(delete("/api/orders-line-variants/{id}", ordersLineVariant.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<OrdersLineVariant> ordersLineVariantList = ordersLineVariantRepository.findAll();
        assertThat(ordersLineVariantList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the OrdersLineVariant in Elasticsearch
        verify(mockOrdersLineVariantSearchRepository, times(1)).deleteById(ordersLineVariant.getId());
    }

    @Test
    @Transactional
    public void searchOrdersLineVariant() throws Exception {
        // Initialize the database
        ordersLineVariantRepository.saveAndFlush(ordersLineVariant);
        when(mockOrdersLineVariantSearchRepository.search(queryStringQuery("id:" + ordersLineVariant.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(ordersLineVariant), PageRequest.of(0, 1), 1));
        // Search the ordersLineVariant
        restOrdersLineVariantMockMvc.perform(get("/api/_search/orders-line-variants?query=id:" + ordersLineVariant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ordersLineVariant.getId().intValue())))
            .andExpect(jsonPath("$.[*].variantName").value(hasItem(DEFAULT_VARIANT_NAME)))
            .andExpect(jsonPath("$.[*].variantValue").value(hasItem(DEFAULT_VARIANT_VALUE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].percentage").value(hasItem(DEFAULT_PERCENTAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].fullPhotoContentType").value(hasItem(DEFAULT_FULL_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fullPhoto").value(hasItem(Base64Utils.encodeToString(DEFAULT_FULL_PHOTO))))
            .andExpect(jsonPath("$.[*].fullPhotoUrl").value(hasItem(DEFAULT_FULL_PHOTO_URL)))
            .andExpect(jsonPath("$.[*].thumbnailPhotoContentType").value(hasItem(DEFAULT_THUMBNAIL_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].thumbnailPhoto").value(hasItem(Base64Utils.encodeToString(DEFAULT_THUMBNAIL_PHOTO))))
            .andExpect(jsonPath("$.[*].thumbnailPhotoUrl").value(hasItem(DEFAULT_THUMBNAIL_PHOTO_URL)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrdersLineVariant.class);
        OrdersLineVariant ordersLineVariant1 = new OrdersLineVariant();
        ordersLineVariant1.setId(1L);
        OrdersLineVariant ordersLineVariant2 = new OrdersLineVariant();
        ordersLineVariant2.setId(ordersLineVariant1.getId());
        assertThat(ordersLineVariant1).isEqualTo(ordersLineVariant2);
        ordersLineVariant2.setId(2L);
        assertThat(ordersLineVariant1).isNotEqualTo(ordersLineVariant2);
        ordersLineVariant1.setId(null);
        assertThat(ordersLineVariant1).isNotEqualTo(ordersLineVariant2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrdersLineVariantDTO.class);
        OrdersLineVariantDTO ordersLineVariantDTO1 = new OrdersLineVariantDTO();
        ordersLineVariantDTO1.setId(1L);
        OrdersLineVariantDTO ordersLineVariantDTO2 = new OrdersLineVariantDTO();
        assertThat(ordersLineVariantDTO1).isNotEqualTo(ordersLineVariantDTO2);
        ordersLineVariantDTO2.setId(ordersLineVariantDTO1.getId());
        assertThat(ordersLineVariantDTO1).isEqualTo(ordersLineVariantDTO2);
        ordersLineVariantDTO2.setId(2L);
        assertThat(ordersLineVariantDTO1).isNotEqualTo(ordersLineVariantDTO2);
        ordersLineVariantDTO1.setId(null);
        assertThat(ordersLineVariantDTO1).isNotEqualTo(ordersLineVariantDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(ordersLineVariantMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(ordersLineVariantMapper.fromId(null)).isNull();
    }
}
