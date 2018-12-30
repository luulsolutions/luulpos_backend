package com.luulsolutions.luulpos.web.rest;

import com.luulsolutions.luulpos.LuulposApp;

import com.luulsolutions.luulpos.domain.Shop;
import com.luulsolutions.luulpos.domain.Company;
import com.luulsolutions.luulpos.domain.Profile;
import com.luulsolutions.luulpos.domain.Profile;
import com.luulsolutions.luulpos.domain.ProductCategory;
import com.luulsolutions.luulpos.domain.ProductType;
import com.luulsolutions.luulpos.domain.SystemConfig;
import com.luulsolutions.luulpos.domain.Discount;
import com.luulsolutions.luulpos.domain.Tax;
import com.luulsolutions.luulpos.repository.ShopRepository;
import com.luulsolutions.luulpos.repository.search.ShopSearchRepository;
import com.luulsolutions.luulpos.service.ShopService;
import com.luulsolutions.luulpos.service.dto.ShopDTO;
import com.luulsolutions.luulpos.service.mapper.ShopMapper;
import com.luulsolutions.luulpos.web.rest.errors.ExceptionTranslator;
import com.luulsolutions.luulpos.service.dto.ShopCriteria;
import com.luulsolutions.luulpos.service.ShopQueryService;

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

import com.luulsolutions.luulpos.domain.enumeration.ShopAccountType;
/**
 * Test class for the ShopResource REST controller.
 *
 * @see ShopResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LuulposApp.class)
public class ShopResourceIntTest {

    private static final String DEFAULT_SHOP_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SHOP_NAME = "BBBBBBBBBB";

    private static final ShopAccountType DEFAULT_SHOP_ACCOUNT_TYPE = ShopAccountType.TRIAL_ACCOUNT;
    private static final ShopAccountType UPDATED_SHOP_ACCOUNT_TYPE = ShopAccountType.SILVER_ACCOUNT;

    private static final ZonedDateTime DEFAULT_APPROVAL_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_APPROVAL_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final String DEFAULT_LANDLAND = "AAAAAAAAAA";
    private static final String UPDATED_LANDLAND = "BBBBBBBBBB";

    private static final String DEFAULT_MOBILE = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final byte[] DEFAULT_SHOP_LOGO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_SHOP_LOGO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_SHOP_LOGO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_SHOP_LOGO_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_SHOP_LOGO_URL = "AAAAAAAAAA";
    private static final String UPDATED_SHOP_LOGO_URL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String DEFAULT_CURRENCY = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCY = "BBBBBBBBBB";

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private ShopService shopService;

    /**
     * This repository is mocked in the com.luulsolutions.luulpos.repository.search test package.
     *
     * @see com.luulsolutions.luulpos.repository.search.ShopSearchRepositoryMockConfiguration
     */
    @Autowired
    private ShopSearchRepository mockShopSearchRepository;

    @Autowired
    private ShopQueryService shopQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restShopMockMvc;

    private Shop shop;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ShopResource shopResource = new ShopResource(shopService, shopQueryService);
        this.restShopMockMvc = MockMvcBuilders.standaloneSetup(shopResource)
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
    public static Shop createEntity(EntityManager em) {
        Shop shop = new Shop()
            .shopName(DEFAULT_SHOP_NAME)
            .shopAccountType(DEFAULT_SHOP_ACCOUNT_TYPE)
            .approvalDate(DEFAULT_APPROVAL_DATE)
            .address(DEFAULT_ADDRESS)
            .email(DEFAULT_EMAIL)
            .description(DEFAULT_DESCRIPTION)
            .note(DEFAULT_NOTE)
            .landland(DEFAULT_LANDLAND)
            .mobile(DEFAULT_MOBILE)
            .createdDate(DEFAULT_CREATED_DATE)
            .shopLogo(DEFAULT_SHOP_LOGO)
            .shopLogoContentType(DEFAULT_SHOP_LOGO_CONTENT_TYPE)
            .shopLogoUrl(DEFAULT_SHOP_LOGO_URL)
            .active(DEFAULT_ACTIVE)
            .currency(DEFAULT_CURRENCY);
        return shop;
    }

    @Before
    public void initTest() {
        shop = createEntity(em);
    }

    @Test
    @Transactional
    public void createShop() throws Exception {
        int databaseSizeBeforeCreate = shopRepository.findAll().size();

        // Create the Shop
        ShopDTO shopDTO = shopMapper.toDto(shop);
        restShopMockMvc.perform(post("/api/shops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shopDTO)))
            .andExpect(status().isCreated());

        // Validate the Shop in the database
        List<Shop> shopList = shopRepository.findAll();
        assertThat(shopList).hasSize(databaseSizeBeforeCreate + 1);
        Shop testShop = shopList.get(shopList.size() - 1);
        assertThat(testShop.getShopName()).isEqualTo(DEFAULT_SHOP_NAME);
        assertThat(testShop.getShopAccountType()).isEqualTo(DEFAULT_SHOP_ACCOUNT_TYPE);
        assertThat(testShop.getApprovalDate()).isEqualTo(DEFAULT_APPROVAL_DATE);
        assertThat(testShop.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testShop.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testShop.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testShop.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testShop.getLandland()).isEqualTo(DEFAULT_LANDLAND);
        assertThat(testShop.getMobile()).isEqualTo(DEFAULT_MOBILE);
        assertThat(testShop.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testShop.getShopLogo()).isEqualTo(DEFAULT_SHOP_LOGO);
        assertThat(testShop.getShopLogoContentType()).isEqualTo(DEFAULT_SHOP_LOGO_CONTENT_TYPE);
        assertThat(testShop.getShopLogoUrl()).isEqualTo(DEFAULT_SHOP_LOGO_URL);
        assertThat(testShop.isActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testShop.getCurrency()).isEqualTo(DEFAULT_CURRENCY);

        // Validate the Shop in Elasticsearch
        verify(mockShopSearchRepository, times(1)).save(testShop);
    }

    @Test
    @Transactional
    public void createShopWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = shopRepository.findAll().size();

        // Create the Shop with an existing ID
        shop.setId(1L);
        ShopDTO shopDTO = shopMapper.toDto(shop);

        // An entity with an existing ID cannot be created, so this API call must fail
        restShopMockMvc.perform(post("/api/shops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shopDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Shop in the database
        List<Shop> shopList = shopRepository.findAll();
        assertThat(shopList).hasSize(databaseSizeBeforeCreate);

        // Validate the Shop in Elasticsearch
        verify(mockShopSearchRepository, times(0)).save(shop);
    }

    @Test
    @Transactional
    public void checkShopNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = shopRepository.findAll().size();
        // set the field null
        shop.setShopName(null);

        // Create the Shop, which fails.
        ShopDTO shopDTO = shopMapper.toDto(shop);

        restShopMockMvc.perform(post("/api/shops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shopDTO)))
            .andExpect(status().isBadRequest());

        List<Shop> shopList = shopRepository.findAll();
        assertThat(shopList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllShops() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList
        restShopMockMvc.perform(get("/api/shops?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shop.getId().intValue())))
            .andExpect(jsonPath("$.[*].shopName").value(hasItem(DEFAULT_SHOP_NAME.toString())))
            .andExpect(jsonPath("$.[*].shopAccountType").value(hasItem(DEFAULT_SHOP_ACCOUNT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].approvalDate").value(hasItem(sameInstant(DEFAULT_APPROVAL_DATE))))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.toString())))
            .andExpect(jsonPath("$.[*].landland").value(hasItem(DEFAULT_LANDLAND.toString())))
            .andExpect(jsonPath("$.[*].mobile").value(hasItem(DEFAULT_MOBILE.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].shopLogoContentType").value(hasItem(DEFAULT_SHOP_LOGO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].shopLogo").value(hasItem(Base64Utils.encodeToString(DEFAULT_SHOP_LOGO))))
            .andExpect(jsonPath("$.[*].shopLogoUrl").value(hasItem(DEFAULT_SHOP_LOGO_URL.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY.toString())));
    }
    
    @Test
    @Transactional
    public void getShop() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get the shop
        restShopMockMvc.perform(get("/api/shops/{id}", shop.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(shop.getId().intValue()))
            .andExpect(jsonPath("$.shopName").value(DEFAULT_SHOP_NAME.toString()))
            .andExpect(jsonPath("$.shopAccountType").value(DEFAULT_SHOP_ACCOUNT_TYPE.toString()))
            .andExpect(jsonPath("$.approvalDate").value(sameInstant(DEFAULT_APPROVAL_DATE)))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE.toString()))
            .andExpect(jsonPath("$.landland").value(DEFAULT_LANDLAND.toString()))
            .andExpect(jsonPath("$.mobile").value(DEFAULT_MOBILE.toString()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.shopLogoContentType").value(DEFAULT_SHOP_LOGO_CONTENT_TYPE))
            .andExpect(jsonPath("$.shopLogo").value(Base64Utils.encodeToString(DEFAULT_SHOP_LOGO)))
            .andExpect(jsonPath("$.shopLogoUrl").value(DEFAULT_SHOP_LOGO_URL.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY.toString()));
    }

    @Test
    @Transactional
    public void getAllShopsByShopNameIsEqualToSomething() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where shopName equals to DEFAULT_SHOP_NAME
        defaultShopShouldBeFound("shopName.equals=" + DEFAULT_SHOP_NAME);

        // Get all the shopList where shopName equals to UPDATED_SHOP_NAME
        defaultShopShouldNotBeFound("shopName.equals=" + UPDATED_SHOP_NAME);
    }

    @Test
    @Transactional
    public void getAllShopsByShopNameIsInShouldWork() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where shopName in DEFAULT_SHOP_NAME or UPDATED_SHOP_NAME
        defaultShopShouldBeFound("shopName.in=" + DEFAULT_SHOP_NAME + "," + UPDATED_SHOP_NAME);

        // Get all the shopList where shopName equals to UPDATED_SHOP_NAME
        defaultShopShouldNotBeFound("shopName.in=" + UPDATED_SHOP_NAME);
    }

    @Test
    @Transactional
    public void getAllShopsByShopNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where shopName is not null
        defaultShopShouldBeFound("shopName.specified=true");

        // Get all the shopList where shopName is null
        defaultShopShouldNotBeFound("shopName.specified=false");
    }

    @Test
    @Transactional
    public void getAllShopsByShopAccountTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where shopAccountType equals to DEFAULT_SHOP_ACCOUNT_TYPE
        defaultShopShouldBeFound("shopAccountType.equals=" + DEFAULT_SHOP_ACCOUNT_TYPE);

        // Get all the shopList where shopAccountType equals to UPDATED_SHOP_ACCOUNT_TYPE
        defaultShopShouldNotBeFound("shopAccountType.equals=" + UPDATED_SHOP_ACCOUNT_TYPE);
    }

    @Test
    @Transactional
    public void getAllShopsByShopAccountTypeIsInShouldWork() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where shopAccountType in DEFAULT_SHOP_ACCOUNT_TYPE or UPDATED_SHOP_ACCOUNT_TYPE
        defaultShopShouldBeFound("shopAccountType.in=" + DEFAULT_SHOP_ACCOUNT_TYPE + "," + UPDATED_SHOP_ACCOUNT_TYPE);

        // Get all the shopList where shopAccountType equals to UPDATED_SHOP_ACCOUNT_TYPE
        defaultShopShouldNotBeFound("shopAccountType.in=" + UPDATED_SHOP_ACCOUNT_TYPE);
    }

    @Test
    @Transactional
    public void getAllShopsByShopAccountTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where shopAccountType is not null
        defaultShopShouldBeFound("shopAccountType.specified=true");

        // Get all the shopList where shopAccountType is null
        defaultShopShouldNotBeFound("shopAccountType.specified=false");
    }

    @Test
    @Transactional
    public void getAllShopsByApprovalDateIsEqualToSomething() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where approvalDate equals to DEFAULT_APPROVAL_DATE
        defaultShopShouldBeFound("approvalDate.equals=" + DEFAULT_APPROVAL_DATE);

        // Get all the shopList where approvalDate equals to UPDATED_APPROVAL_DATE
        defaultShopShouldNotBeFound("approvalDate.equals=" + UPDATED_APPROVAL_DATE);
    }

    @Test
    @Transactional
    public void getAllShopsByApprovalDateIsInShouldWork() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where approvalDate in DEFAULT_APPROVAL_DATE or UPDATED_APPROVAL_DATE
        defaultShopShouldBeFound("approvalDate.in=" + DEFAULT_APPROVAL_DATE + "," + UPDATED_APPROVAL_DATE);

        // Get all the shopList where approvalDate equals to UPDATED_APPROVAL_DATE
        defaultShopShouldNotBeFound("approvalDate.in=" + UPDATED_APPROVAL_DATE);
    }

    @Test
    @Transactional
    public void getAllShopsByApprovalDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where approvalDate is not null
        defaultShopShouldBeFound("approvalDate.specified=true");

        // Get all the shopList where approvalDate is null
        defaultShopShouldNotBeFound("approvalDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllShopsByApprovalDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where approvalDate greater than or equals to DEFAULT_APPROVAL_DATE
        defaultShopShouldBeFound("approvalDate.greaterOrEqualThan=" + DEFAULT_APPROVAL_DATE);

        // Get all the shopList where approvalDate greater than or equals to UPDATED_APPROVAL_DATE
        defaultShopShouldNotBeFound("approvalDate.greaterOrEqualThan=" + UPDATED_APPROVAL_DATE);
    }

    @Test
    @Transactional
    public void getAllShopsByApprovalDateIsLessThanSomething() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where approvalDate less than or equals to DEFAULT_APPROVAL_DATE
        defaultShopShouldNotBeFound("approvalDate.lessThan=" + DEFAULT_APPROVAL_DATE);

        // Get all the shopList where approvalDate less than or equals to UPDATED_APPROVAL_DATE
        defaultShopShouldBeFound("approvalDate.lessThan=" + UPDATED_APPROVAL_DATE);
    }


    @Test
    @Transactional
    public void getAllShopsByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where address equals to DEFAULT_ADDRESS
        defaultShopShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the shopList where address equals to UPDATED_ADDRESS
        defaultShopShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllShopsByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultShopShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the shopList where address equals to UPDATED_ADDRESS
        defaultShopShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllShopsByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where address is not null
        defaultShopShouldBeFound("address.specified=true");

        // Get all the shopList where address is null
        defaultShopShouldNotBeFound("address.specified=false");
    }

    @Test
    @Transactional
    public void getAllShopsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where email equals to DEFAULT_EMAIL
        defaultShopShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the shopList where email equals to UPDATED_EMAIL
        defaultShopShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllShopsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultShopShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the shopList where email equals to UPDATED_EMAIL
        defaultShopShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllShopsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where email is not null
        defaultShopShouldBeFound("email.specified=true");

        // Get all the shopList where email is null
        defaultShopShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    public void getAllShopsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where description equals to DEFAULT_DESCRIPTION
        defaultShopShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the shopList where description equals to UPDATED_DESCRIPTION
        defaultShopShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllShopsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultShopShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the shopList where description equals to UPDATED_DESCRIPTION
        defaultShopShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllShopsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where description is not null
        defaultShopShouldBeFound("description.specified=true");

        // Get all the shopList where description is null
        defaultShopShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllShopsByNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where note equals to DEFAULT_NOTE
        defaultShopShouldBeFound("note.equals=" + DEFAULT_NOTE);

        // Get all the shopList where note equals to UPDATED_NOTE
        defaultShopShouldNotBeFound("note.equals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllShopsByNoteIsInShouldWork() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where note in DEFAULT_NOTE or UPDATED_NOTE
        defaultShopShouldBeFound("note.in=" + DEFAULT_NOTE + "," + UPDATED_NOTE);

        // Get all the shopList where note equals to UPDATED_NOTE
        defaultShopShouldNotBeFound("note.in=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllShopsByNoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where note is not null
        defaultShopShouldBeFound("note.specified=true");

        // Get all the shopList where note is null
        defaultShopShouldNotBeFound("note.specified=false");
    }

    @Test
    @Transactional
    public void getAllShopsByLandlandIsEqualToSomething() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where landland equals to DEFAULT_LANDLAND
        defaultShopShouldBeFound("landland.equals=" + DEFAULT_LANDLAND);

        // Get all the shopList where landland equals to UPDATED_LANDLAND
        defaultShopShouldNotBeFound("landland.equals=" + UPDATED_LANDLAND);
    }

    @Test
    @Transactional
    public void getAllShopsByLandlandIsInShouldWork() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where landland in DEFAULT_LANDLAND or UPDATED_LANDLAND
        defaultShopShouldBeFound("landland.in=" + DEFAULT_LANDLAND + "," + UPDATED_LANDLAND);

        // Get all the shopList where landland equals to UPDATED_LANDLAND
        defaultShopShouldNotBeFound("landland.in=" + UPDATED_LANDLAND);
    }

    @Test
    @Transactional
    public void getAllShopsByLandlandIsNullOrNotNull() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where landland is not null
        defaultShopShouldBeFound("landland.specified=true");

        // Get all the shopList where landland is null
        defaultShopShouldNotBeFound("landland.specified=false");
    }

    @Test
    @Transactional
    public void getAllShopsByMobileIsEqualToSomething() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where mobile equals to DEFAULT_MOBILE
        defaultShopShouldBeFound("mobile.equals=" + DEFAULT_MOBILE);

        // Get all the shopList where mobile equals to UPDATED_MOBILE
        defaultShopShouldNotBeFound("mobile.equals=" + UPDATED_MOBILE);
    }

    @Test
    @Transactional
    public void getAllShopsByMobileIsInShouldWork() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where mobile in DEFAULT_MOBILE or UPDATED_MOBILE
        defaultShopShouldBeFound("mobile.in=" + DEFAULT_MOBILE + "," + UPDATED_MOBILE);

        // Get all the shopList where mobile equals to UPDATED_MOBILE
        defaultShopShouldNotBeFound("mobile.in=" + UPDATED_MOBILE);
    }

    @Test
    @Transactional
    public void getAllShopsByMobileIsNullOrNotNull() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where mobile is not null
        defaultShopShouldBeFound("mobile.specified=true");

        // Get all the shopList where mobile is null
        defaultShopShouldNotBeFound("mobile.specified=false");
    }

    @Test
    @Transactional
    public void getAllShopsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where createdDate equals to DEFAULT_CREATED_DATE
        defaultShopShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the shopList where createdDate equals to UPDATED_CREATED_DATE
        defaultShopShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllShopsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultShopShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the shopList where createdDate equals to UPDATED_CREATED_DATE
        defaultShopShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllShopsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where createdDate is not null
        defaultShopShouldBeFound("createdDate.specified=true");

        // Get all the shopList where createdDate is null
        defaultShopShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllShopsByCreatedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where createdDate greater than or equals to DEFAULT_CREATED_DATE
        defaultShopShouldBeFound("createdDate.greaterOrEqualThan=" + DEFAULT_CREATED_DATE);

        // Get all the shopList where createdDate greater than or equals to UPDATED_CREATED_DATE
        defaultShopShouldNotBeFound("createdDate.greaterOrEqualThan=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllShopsByCreatedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where createdDate less than or equals to DEFAULT_CREATED_DATE
        defaultShopShouldNotBeFound("createdDate.lessThan=" + DEFAULT_CREATED_DATE);

        // Get all the shopList where createdDate less than or equals to UPDATED_CREATED_DATE
        defaultShopShouldBeFound("createdDate.lessThan=" + UPDATED_CREATED_DATE);
    }


    @Test
    @Transactional
    public void getAllShopsByShopLogoUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where shopLogoUrl equals to DEFAULT_SHOP_LOGO_URL
        defaultShopShouldBeFound("shopLogoUrl.equals=" + DEFAULT_SHOP_LOGO_URL);

        // Get all the shopList where shopLogoUrl equals to UPDATED_SHOP_LOGO_URL
        defaultShopShouldNotBeFound("shopLogoUrl.equals=" + UPDATED_SHOP_LOGO_URL);
    }

    @Test
    @Transactional
    public void getAllShopsByShopLogoUrlIsInShouldWork() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where shopLogoUrl in DEFAULT_SHOP_LOGO_URL or UPDATED_SHOP_LOGO_URL
        defaultShopShouldBeFound("shopLogoUrl.in=" + DEFAULT_SHOP_LOGO_URL + "," + UPDATED_SHOP_LOGO_URL);

        // Get all the shopList where shopLogoUrl equals to UPDATED_SHOP_LOGO_URL
        defaultShopShouldNotBeFound("shopLogoUrl.in=" + UPDATED_SHOP_LOGO_URL);
    }

    @Test
    @Transactional
    public void getAllShopsByShopLogoUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where shopLogoUrl is not null
        defaultShopShouldBeFound("shopLogoUrl.specified=true");

        // Get all the shopList where shopLogoUrl is null
        defaultShopShouldNotBeFound("shopLogoUrl.specified=false");
    }

    @Test
    @Transactional
    public void getAllShopsByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where active equals to DEFAULT_ACTIVE
        defaultShopShouldBeFound("active.equals=" + DEFAULT_ACTIVE);

        // Get all the shopList where active equals to UPDATED_ACTIVE
        defaultShopShouldNotBeFound("active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllShopsByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where active in DEFAULT_ACTIVE or UPDATED_ACTIVE
        defaultShopShouldBeFound("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE);

        // Get all the shopList where active equals to UPDATED_ACTIVE
        defaultShopShouldNotBeFound("active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllShopsByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where active is not null
        defaultShopShouldBeFound("active.specified=true");

        // Get all the shopList where active is null
        defaultShopShouldNotBeFound("active.specified=false");
    }

    @Test
    @Transactional
    public void getAllShopsByCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where currency equals to DEFAULT_CURRENCY
        defaultShopShouldBeFound("currency.equals=" + DEFAULT_CURRENCY);

        // Get all the shopList where currency equals to UPDATED_CURRENCY
        defaultShopShouldNotBeFound("currency.equals=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    public void getAllShopsByCurrencyIsInShouldWork() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where currency in DEFAULT_CURRENCY or UPDATED_CURRENCY
        defaultShopShouldBeFound("currency.in=" + DEFAULT_CURRENCY + "," + UPDATED_CURRENCY);

        // Get all the shopList where currency equals to UPDATED_CURRENCY
        defaultShopShouldNotBeFound("currency.in=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    public void getAllShopsByCurrencyIsNullOrNotNull() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList where currency is not null
        defaultShopShouldBeFound("currency.specified=true");

        // Get all the shopList where currency is null
        defaultShopShouldNotBeFound("currency.specified=false");
    }

    @Test
    @Transactional
    public void getAllShopsByCompanyIsEqualToSomething() throws Exception {
        // Initialize the database
        Company company = CompanyResourceIntTest.createEntity(em);
        em.persist(company);
        em.flush();
        shop.setCompany(company);
        shopRepository.saveAndFlush(shop);
        Long companyId = company.getId();

        // Get all the shopList where company equals to companyId
        defaultShopShouldBeFound("companyId.equals=" + companyId);

        // Get all the shopList where company equals to companyId + 1
        defaultShopShouldNotBeFound("companyId.equals=" + (companyId + 1));
    }


    @Test
    @Transactional
    public void getAllShopsByApprovedByIsEqualToSomething() throws Exception {
        // Initialize the database
        Profile approvedBy = ProfileResourceIntTest.createEntity(em);
        em.persist(approvedBy);
        em.flush();
        shop.setApprovedBy(approvedBy);
        shopRepository.saveAndFlush(shop);
        Long approvedById = approvedBy.getId();

        // Get all the shopList where approvedBy equals to approvedById
        defaultShopShouldBeFound("approvedById.equals=" + approvedById);

        // Get all the shopList where approvedBy equals to approvedById + 1
        defaultShopShouldNotBeFound("approvedById.equals=" + (approvedById + 1));
    }


    @Test
    @Transactional
    public void getAllShopsByProfilesIsEqualToSomething() throws Exception {
        // Initialize the database
        Profile profiles = ProfileResourceIntTest.createEntity(em);
        em.persist(profiles);
        em.flush();
        shop.addProfiles(profiles);
        shopRepository.saveAndFlush(shop);
        Long profilesId = profiles.getId();

        // Get all the shopList where profiles equals to profilesId
        defaultShopShouldBeFound("profilesId.equals=" + profilesId);

        // Get all the shopList where profiles equals to profilesId + 1
        defaultShopShouldNotBeFound("profilesId.equals=" + (profilesId + 1));
    }


    @Test
    @Transactional
    public void getAllShopsByProductCategoriesIsEqualToSomething() throws Exception {
        // Initialize the database
        ProductCategory productCategories = ProductCategoryResourceIntTest.createEntity(em);
        em.persist(productCategories);
        em.flush();
        shop.addProductCategories(productCategories);
        shopRepository.saveAndFlush(shop);
        Long productCategoriesId = productCategories.getId();

        // Get all the shopList where productCategories equals to productCategoriesId
        defaultShopShouldBeFound("productCategoriesId.equals=" + productCategoriesId);

        // Get all the shopList where productCategories equals to productCategoriesId + 1
        defaultShopShouldNotBeFound("productCategoriesId.equals=" + (productCategoriesId + 1));
    }


    @Test
    @Transactional
    public void getAllShopsByProductTypesIsEqualToSomething() throws Exception {
        // Initialize the database
        ProductType productTypes = ProductTypeResourceIntTest.createEntity(em);
        em.persist(productTypes);
        em.flush();
        shop.addProductTypes(productTypes);
        shopRepository.saveAndFlush(shop);
        Long productTypesId = productTypes.getId();

        // Get all the shopList where productTypes equals to productTypesId
        defaultShopShouldBeFound("productTypesId.equals=" + productTypesId);

        // Get all the shopList where productTypes equals to productTypesId + 1
        defaultShopShouldNotBeFound("productTypesId.equals=" + (productTypesId + 1));
    }


    @Test
    @Transactional
    public void getAllShopsBySystemConfigsIsEqualToSomething() throws Exception {
        // Initialize the database
        SystemConfig systemConfigs = SystemConfigResourceIntTest.createEntity(em);
        em.persist(systemConfigs);
        em.flush();
        shop.addSystemConfigs(systemConfigs);
        shopRepository.saveAndFlush(shop);
        Long systemConfigsId = systemConfigs.getId();

        // Get all the shopList where systemConfigs equals to systemConfigsId
        defaultShopShouldBeFound("systemConfigsId.equals=" + systemConfigsId);

        // Get all the shopList where systemConfigs equals to systemConfigsId + 1
        defaultShopShouldNotBeFound("systemConfigsId.equals=" + (systemConfigsId + 1));
    }


    @Test
    @Transactional
    public void getAllShopsByDiscountsIsEqualToSomething() throws Exception {
        // Initialize the database
        Discount discounts = DiscountResourceIntTest.createEntity(em);
        em.persist(discounts);
        em.flush();
        shop.addDiscounts(discounts);
        shopRepository.saveAndFlush(shop);
        Long discountsId = discounts.getId();

        // Get all the shopList where discounts equals to discountsId
        defaultShopShouldBeFound("discountsId.equals=" + discountsId);

        // Get all the shopList where discounts equals to discountsId + 1
        defaultShopShouldNotBeFound("discountsId.equals=" + (discountsId + 1));
    }


    @Test
    @Transactional
    public void getAllShopsByTaxesIsEqualToSomething() throws Exception {
        // Initialize the database
        Tax taxes = TaxResourceIntTest.createEntity(em);
        em.persist(taxes);
        em.flush();
        shop.addTaxes(taxes);
        shopRepository.saveAndFlush(shop);
        Long taxesId = taxes.getId();

        // Get all the shopList where taxes equals to taxesId
        defaultShopShouldBeFound("taxesId.equals=" + taxesId);

        // Get all the shopList where taxes equals to taxesId + 1
        defaultShopShouldNotBeFound("taxesId.equals=" + (taxesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultShopShouldBeFound(String filter) throws Exception {
        restShopMockMvc.perform(get("/api/shops?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shop.getId().intValue())))
            .andExpect(jsonPath("$.[*].shopName").value(hasItem(DEFAULT_SHOP_NAME.toString())))
            .andExpect(jsonPath("$.[*].shopAccountType").value(hasItem(DEFAULT_SHOP_ACCOUNT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].approvalDate").value(hasItem(sameInstant(DEFAULT_APPROVAL_DATE))))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.toString())))
            .andExpect(jsonPath("$.[*].landland").value(hasItem(DEFAULT_LANDLAND.toString())))
            .andExpect(jsonPath("$.[*].mobile").value(hasItem(DEFAULT_MOBILE.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].shopLogoContentType").value(hasItem(DEFAULT_SHOP_LOGO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].shopLogo").value(hasItem(Base64Utils.encodeToString(DEFAULT_SHOP_LOGO))))
            .andExpect(jsonPath("$.[*].shopLogoUrl").value(hasItem(DEFAULT_SHOP_LOGO_URL.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY.toString())));

        // Check, that the count call also returns 1
        restShopMockMvc.perform(get("/api/shops/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultShopShouldNotBeFound(String filter) throws Exception {
        restShopMockMvc.perform(get("/api/shops?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restShopMockMvc.perform(get("/api/shops/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingShop() throws Exception {
        // Get the shop
        restShopMockMvc.perform(get("/api/shops/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateShop() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        int databaseSizeBeforeUpdate = shopRepository.findAll().size();

        // Update the shop
        Shop updatedShop = shopRepository.findById(shop.getId()).get();
        // Disconnect from session so that the updates on updatedShop are not directly saved in db
        em.detach(updatedShop);
        updatedShop
            .shopName(UPDATED_SHOP_NAME)
            .shopAccountType(UPDATED_SHOP_ACCOUNT_TYPE)
            .approvalDate(UPDATED_APPROVAL_DATE)
            .address(UPDATED_ADDRESS)
            .email(UPDATED_EMAIL)
            .description(UPDATED_DESCRIPTION)
            .note(UPDATED_NOTE)
            .landland(UPDATED_LANDLAND)
            .mobile(UPDATED_MOBILE)
            .createdDate(UPDATED_CREATED_DATE)
            .shopLogo(UPDATED_SHOP_LOGO)
            .shopLogoContentType(UPDATED_SHOP_LOGO_CONTENT_TYPE)
            .shopLogoUrl(UPDATED_SHOP_LOGO_URL)
            .active(UPDATED_ACTIVE)
            .currency(UPDATED_CURRENCY);
        ShopDTO shopDTO = shopMapper.toDto(updatedShop);

        restShopMockMvc.perform(put("/api/shops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shopDTO)))
            .andExpect(status().isOk());

        // Validate the Shop in the database
        List<Shop> shopList = shopRepository.findAll();
        assertThat(shopList).hasSize(databaseSizeBeforeUpdate);
        Shop testShop = shopList.get(shopList.size() - 1);
        assertThat(testShop.getShopName()).isEqualTo(UPDATED_SHOP_NAME);
        assertThat(testShop.getShopAccountType()).isEqualTo(UPDATED_SHOP_ACCOUNT_TYPE);
        assertThat(testShop.getApprovalDate()).isEqualTo(UPDATED_APPROVAL_DATE);
        assertThat(testShop.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testShop.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testShop.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testShop.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testShop.getLandland()).isEqualTo(UPDATED_LANDLAND);
        assertThat(testShop.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testShop.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testShop.getShopLogo()).isEqualTo(UPDATED_SHOP_LOGO);
        assertThat(testShop.getShopLogoContentType()).isEqualTo(UPDATED_SHOP_LOGO_CONTENT_TYPE);
        assertThat(testShop.getShopLogoUrl()).isEqualTo(UPDATED_SHOP_LOGO_URL);
        assertThat(testShop.isActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testShop.getCurrency()).isEqualTo(UPDATED_CURRENCY);

        // Validate the Shop in Elasticsearch
        verify(mockShopSearchRepository, times(1)).save(testShop);
    }

    @Test
    @Transactional
    public void updateNonExistingShop() throws Exception {
        int databaseSizeBeforeUpdate = shopRepository.findAll().size();

        // Create the Shop
        ShopDTO shopDTO = shopMapper.toDto(shop);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShopMockMvc.perform(put("/api/shops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shopDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Shop in the database
        List<Shop> shopList = shopRepository.findAll();
        assertThat(shopList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Shop in Elasticsearch
        verify(mockShopSearchRepository, times(0)).save(shop);
    }

    @Test
    @Transactional
    public void deleteShop() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        int databaseSizeBeforeDelete = shopRepository.findAll().size();

        // Get the shop
        restShopMockMvc.perform(delete("/api/shops/{id}", shop.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Shop> shopList = shopRepository.findAll();
        assertThat(shopList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Shop in Elasticsearch
        verify(mockShopSearchRepository, times(1)).deleteById(shop.getId());
    }

    @Test
    @Transactional
    public void searchShop() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);
        when(mockShopSearchRepository.search(queryStringQuery("id:" + shop.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(shop), PageRequest.of(0, 1), 1));
        // Search the shop
        restShopMockMvc.perform(get("/api/_search/shops?query=id:" + shop.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shop.getId().intValue())))
            .andExpect(jsonPath("$.[*].shopName").value(hasItem(DEFAULT_SHOP_NAME)))
            .andExpect(jsonPath("$.[*].shopAccountType").value(hasItem(DEFAULT_SHOP_ACCOUNT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].approvalDate").value(hasItem(sameInstant(DEFAULT_APPROVAL_DATE))))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].landland").value(hasItem(DEFAULT_LANDLAND)))
            .andExpect(jsonPath("$.[*].mobile").value(hasItem(DEFAULT_MOBILE)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].shopLogoContentType").value(hasItem(DEFAULT_SHOP_LOGO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].shopLogo").value(hasItem(Base64Utils.encodeToString(DEFAULT_SHOP_LOGO))))
            .andExpect(jsonPath("$.[*].shopLogoUrl").value(hasItem(DEFAULT_SHOP_LOGO_URL)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Shop.class);
        Shop shop1 = new Shop();
        shop1.setId(1L);
        Shop shop2 = new Shop();
        shop2.setId(shop1.getId());
        assertThat(shop1).isEqualTo(shop2);
        shop2.setId(2L);
        assertThat(shop1).isNotEqualTo(shop2);
        shop1.setId(null);
        assertThat(shop1).isNotEqualTo(shop2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShopDTO.class);
        ShopDTO shopDTO1 = new ShopDTO();
        shopDTO1.setId(1L);
        ShopDTO shopDTO2 = new ShopDTO();
        assertThat(shopDTO1).isNotEqualTo(shopDTO2);
        shopDTO2.setId(shopDTO1.getId());
        assertThat(shopDTO1).isEqualTo(shopDTO2);
        shopDTO2.setId(2L);
        assertThat(shopDTO1).isNotEqualTo(shopDTO2);
        shopDTO1.setId(null);
        assertThat(shopDTO1).isNotEqualTo(shopDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(shopMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(shopMapper.fromId(null)).isNull();
    }
}
