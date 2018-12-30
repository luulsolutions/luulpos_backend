package com.luulsolutions.luulpos.web.rest;

import com.luulsolutions.luulpos.LuulposApp;

import com.luulsolutions.luulpos.domain.Product;
import com.luulsolutions.luulpos.domain.ProductVariant;
import com.luulsolutions.luulpos.domain.ProductExtra;
import com.luulsolutions.luulpos.domain.ProductType;
import com.luulsolutions.luulpos.domain.Shop;
import com.luulsolutions.luulpos.domain.Discount;
import com.luulsolutions.luulpos.domain.Tax;
import com.luulsolutions.luulpos.domain.ProductCategory;
import com.luulsolutions.luulpos.repository.ProductRepository;
import com.luulsolutions.luulpos.repository.search.ProductSearchRepository;
import com.luulsolutions.luulpos.service.ProductService;
import com.luulsolutions.luulpos.service.dto.ProductDTO;
import com.luulsolutions.luulpos.service.mapper.ProductMapper;
import com.luulsolutions.luulpos.web.rest.errors.ExceptionTranslator;
import com.luulsolutions.luulpos.service.dto.ProductCriteria;
import com.luulsolutions.luulpos.service.ProductQueryService;

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

/**
 * Test class for the ProductResource REST controller.
 *
 * @see ProductResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LuulposApp.class)
public class ProductResourceIntTest {

    private static final String DEFAULT_PRODUCT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final byte[] DEFAULT_PRODUCT_IMAGE_FULL = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PRODUCT_IMAGE_FULL = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PRODUCT_IMAGE_FULL_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PRODUCT_IMAGE_FULL_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_PRODUCT_IMAGE_FULL_URL = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_IMAGE_FULL_URL = "BBBBBBBBBB";

    private static final byte[] DEFAULT_PRODUCT_IMAGE_THUMB = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PRODUCT_IMAGE_THUMB = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PRODUCT_IMAGE_THUMB_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PRODUCT_IMAGE_THUMB_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_PRODUCT_IMAGE_THUMB_URL = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_IMAGE_THUMB_URL = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_BARCODE = "AAAAAAAAAA";
    private static final String UPDATED_BARCODE = "BBBBBBBBBB";

    private static final String DEFAULT_SERIAL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SERIAL_CODE = "BBBBBBBBBB";

    private static final Integer DEFAULT_PRIORITY_POSITION = 1;
    private static final Integer UPDATED_PRIORITY_POSITION = 2;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final Boolean DEFAULT_IS_VARIANT_PRODUCT = false;
    private static final Boolean UPDATED_IS_VARIANT_PRODUCT = true;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductService productService;

    /**
     * This repository is mocked in the com.luulsolutions.luulpos.repository.search test package.
     *
     * @see com.luulsolutions.luulpos.repository.search.ProductSearchRepositoryMockConfiguration
     */
    @Autowired
    private ProductSearchRepository mockProductSearchRepository;

    @Autowired
    private ProductQueryService productQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProductMockMvc;

    private Product product;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProductResource productResource = new ProductResource(productService, productQueryService);
        this.restProductMockMvc = MockMvcBuilders.standaloneSetup(productResource)
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
    public static Product createEntity(EntityManager em) {
        Product product = new Product()
            .productName(DEFAULT_PRODUCT_NAME)
            .productDescription(DEFAULT_PRODUCT_DESCRIPTION)
            .price(DEFAULT_PRICE)
            .quantity(DEFAULT_QUANTITY)
            .productImageFull(DEFAULT_PRODUCT_IMAGE_FULL)
            .productImageFullContentType(DEFAULT_PRODUCT_IMAGE_FULL_CONTENT_TYPE)
            .productImageFullUrl(DEFAULT_PRODUCT_IMAGE_FULL_URL)
            .productImageThumb(DEFAULT_PRODUCT_IMAGE_THUMB)
            .productImageThumbContentType(DEFAULT_PRODUCT_IMAGE_THUMB_CONTENT_TYPE)
            .productImageThumbUrl(DEFAULT_PRODUCT_IMAGE_THUMB_URL)
            .dateCreated(DEFAULT_DATE_CREATED)
            .barcode(DEFAULT_BARCODE)
            .serialCode(DEFAULT_SERIAL_CODE)
            .priorityPosition(DEFAULT_PRIORITY_POSITION)
            .active(DEFAULT_ACTIVE)
            .isVariantProduct(DEFAULT_IS_VARIANT_PRODUCT);
        return product;
    }

    @Before
    public void initTest() {
        product = createEntity(em);
    }

    @Test
    @Transactional
    public void createProduct() throws Exception {
        int databaseSizeBeforeCreate = productRepository.findAll().size();

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);
        restProductMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isCreated());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeCreate + 1);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getProductName()).isEqualTo(DEFAULT_PRODUCT_NAME);
        assertThat(testProduct.getProductDescription()).isEqualTo(DEFAULT_PRODUCT_DESCRIPTION);
        assertThat(testProduct.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testProduct.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testProduct.getProductImageFull()).isEqualTo(DEFAULT_PRODUCT_IMAGE_FULL);
        assertThat(testProduct.getProductImageFullContentType()).isEqualTo(DEFAULT_PRODUCT_IMAGE_FULL_CONTENT_TYPE);
        assertThat(testProduct.getProductImageFullUrl()).isEqualTo(DEFAULT_PRODUCT_IMAGE_FULL_URL);
        assertThat(testProduct.getProductImageThumb()).isEqualTo(DEFAULT_PRODUCT_IMAGE_THUMB);
        assertThat(testProduct.getProductImageThumbContentType()).isEqualTo(DEFAULT_PRODUCT_IMAGE_THUMB_CONTENT_TYPE);
        assertThat(testProduct.getProductImageThumbUrl()).isEqualTo(DEFAULT_PRODUCT_IMAGE_THUMB_URL);
        assertThat(testProduct.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testProduct.getBarcode()).isEqualTo(DEFAULT_BARCODE);
        assertThat(testProduct.getSerialCode()).isEqualTo(DEFAULT_SERIAL_CODE);
        assertThat(testProduct.getPriorityPosition()).isEqualTo(DEFAULT_PRIORITY_POSITION);
        assertThat(testProduct.isActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testProduct.isIsVariantProduct()).isEqualTo(DEFAULT_IS_VARIANT_PRODUCT);

        // Validate the Product in Elasticsearch
        verify(mockProductSearchRepository, times(1)).save(testProduct);
    }

    @Test
    @Transactional
    public void createProductWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = productRepository.findAll().size();

        // Create the Product with an existing ID
        product.setId(1L);
        ProductDTO productDTO = productMapper.toDto(product);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeCreate);

        // Validate the Product in Elasticsearch
        verify(mockProductSearchRepository, times(0)).save(product);
    }

    @Test
    @Transactional
    public void checkProductNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setProductName(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setQuantity(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProducts() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList
        restProductMockMvc.perform(get("/api/products?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].productName").value(hasItem(DEFAULT_PRODUCT_NAME.toString())))
            .andExpect(jsonPath("$.[*].productDescription").value(hasItem(DEFAULT_PRODUCT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].productImageFullContentType").value(hasItem(DEFAULT_PRODUCT_IMAGE_FULL_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].productImageFull").value(hasItem(Base64Utils.encodeToString(DEFAULT_PRODUCT_IMAGE_FULL))))
            .andExpect(jsonPath("$.[*].productImageFullUrl").value(hasItem(DEFAULT_PRODUCT_IMAGE_FULL_URL.toString())))
            .andExpect(jsonPath("$.[*].productImageThumbContentType").value(hasItem(DEFAULT_PRODUCT_IMAGE_THUMB_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].productImageThumb").value(hasItem(Base64Utils.encodeToString(DEFAULT_PRODUCT_IMAGE_THUMB))))
            .andExpect(jsonPath("$.[*].productImageThumbUrl").value(hasItem(DEFAULT_PRODUCT_IMAGE_THUMB_URL.toString())))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(sameInstant(DEFAULT_DATE_CREATED))))
            .andExpect(jsonPath("$.[*].barcode").value(hasItem(DEFAULT_BARCODE.toString())))
            .andExpect(jsonPath("$.[*].serialCode").value(hasItem(DEFAULT_SERIAL_CODE.toString())))
            .andExpect(jsonPath("$.[*].priorityPosition").value(hasItem(DEFAULT_PRIORITY_POSITION)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].isVariantProduct").value(hasItem(DEFAULT_IS_VARIANT_PRODUCT.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get the product
        restProductMockMvc.perform(get("/api/products/{id}", product.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(product.getId().intValue()))
            .andExpect(jsonPath("$.productName").value(DEFAULT_PRODUCT_NAME.toString()))
            .andExpect(jsonPath("$.productDescription").value(DEFAULT_PRODUCT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.productImageFullContentType").value(DEFAULT_PRODUCT_IMAGE_FULL_CONTENT_TYPE))
            .andExpect(jsonPath("$.productImageFull").value(Base64Utils.encodeToString(DEFAULT_PRODUCT_IMAGE_FULL)))
            .andExpect(jsonPath("$.productImageFullUrl").value(DEFAULT_PRODUCT_IMAGE_FULL_URL.toString()))
            .andExpect(jsonPath("$.productImageThumbContentType").value(DEFAULT_PRODUCT_IMAGE_THUMB_CONTENT_TYPE))
            .andExpect(jsonPath("$.productImageThumb").value(Base64Utils.encodeToString(DEFAULT_PRODUCT_IMAGE_THUMB)))
            .andExpect(jsonPath("$.productImageThumbUrl").value(DEFAULT_PRODUCT_IMAGE_THUMB_URL.toString()))
            .andExpect(jsonPath("$.dateCreated").value(sameInstant(DEFAULT_DATE_CREATED)))
            .andExpect(jsonPath("$.barcode").value(DEFAULT_BARCODE.toString()))
            .andExpect(jsonPath("$.serialCode").value(DEFAULT_SERIAL_CODE.toString()))
            .andExpect(jsonPath("$.priorityPosition").value(DEFAULT_PRIORITY_POSITION))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.isVariantProduct").value(DEFAULT_IS_VARIANT_PRODUCT.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllProductsByProductNameIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productName equals to DEFAULT_PRODUCT_NAME
        defaultProductShouldBeFound("productName.equals=" + DEFAULT_PRODUCT_NAME);

        // Get all the productList where productName equals to UPDATED_PRODUCT_NAME
        defaultProductShouldNotBeFound("productName.equals=" + UPDATED_PRODUCT_NAME);
    }

    @Test
    @Transactional
    public void getAllProductsByProductNameIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productName in DEFAULT_PRODUCT_NAME or UPDATED_PRODUCT_NAME
        defaultProductShouldBeFound("productName.in=" + DEFAULT_PRODUCT_NAME + "," + UPDATED_PRODUCT_NAME);

        // Get all the productList where productName equals to UPDATED_PRODUCT_NAME
        defaultProductShouldNotBeFound("productName.in=" + UPDATED_PRODUCT_NAME);
    }

    @Test
    @Transactional
    public void getAllProductsByProductNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productName is not null
        defaultProductShouldBeFound("productName.specified=true");

        // Get all the productList where productName is null
        defaultProductShouldNotBeFound("productName.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsByProductDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productDescription equals to DEFAULT_PRODUCT_DESCRIPTION
        defaultProductShouldBeFound("productDescription.equals=" + DEFAULT_PRODUCT_DESCRIPTION);

        // Get all the productList where productDescription equals to UPDATED_PRODUCT_DESCRIPTION
        defaultProductShouldNotBeFound("productDescription.equals=" + UPDATED_PRODUCT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllProductsByProductDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productDescription in DEFAULT_PRODUCT_DESCRIPTION or UPDATED_PRODUCT_DESCRIPTION
        defaultProductShouldBeFound("productDescription.in=" + DEFAULT_PRODUCT_DESCRIPTION + "," + UPDATED_PRODUCT_DESCRIPTION);

        // Get all the productList where productDescription equals to UPDATED_PRODUCT_DESCRIPTION
        defaultProductShouldNotBeFound("productDescription.in=" + UPDATED_PRODUCT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllProductsByProductDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productDescription is not null
        defaultProductShouldBeFound("productDescription.specified=true");

        // Get all the productList where productDescription is null
        defaultProductShouldNotBeFound("productDescription.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where price equals to DEFAULT_PRICE
        defaultProductShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the productList where price equals to UPDATED_PRICE
        defaultProductShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllProductsByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultProductShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the productList where price equals to UPDATED_PRICE
        defaultProductShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllProductsByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where price is not null
        defaultProductShouldBeFound("price.specified=true");

        // Get all the productList where price is null
        defaultProductShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where quantity equals to DEFAULT_QUANTITY
        defaultProductShouldBeFound("quantity.equals=" + DEFAULT_QUANTITY);

        // Get all the productList where quantity equals to UPDATED_QUANTITY
        defaultProductShouldNotBeFound("quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllProductsByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where quantity in DEFAULT_QUANTITY or UPDATED_QUANTITY
        defaultProductShouldBeFound("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY);

        // Get all the productList where quantity equals to UPDATED_QUANTITY
        defaultProductShouldNotBeFound("quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllProductsByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where quantity is not null
        defaultProductShouldBeFound("quantity.specified=true");

        // Get all the productList where quantity is null
        defaultProductShouldNotBeFound("quantity.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where quantity greater than or equals to DEFAULT_QUANTITY
        defaultProductShouldBeFound("quantity.greaterOrEqualThan=" + DEFAULT_QUANTITY);

        // Get all the productList where quantity greater than or equals to UPDATED_QUANTITY
        defaultProductShouldNotBeFound("quantity.greaterOrEqualThan=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllProductsByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where quantity less than or equals to DEFAULT_QUANTITY
        defaultProductShouldNotBeFound("quantity.lessThan=" + DEFAULT_QUANTITY);

        // Get all the productList where quantity less than or equals to UPDATED_QUANTITY
        defaultProductShouldBeFound("quantity.lessThan=" + UPDATED_QUANTITY);
    }


    @Test
    @Transactional
    public void getAllProductsByProductImageFullUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productImageFullUrl equals to DEFAULT_PRODUCT_IMAGE_FULL_URL
        defaultProductShouldBeFound("productImageFullUrl.equals=" + DEFAULT_PRODUCT_IMAGE_FULL_URL);

        // Get all the productList where productImageFullUrl equals to UPDATED_PRODUCT_IMAGE_FULL_URL
        defaultProductShouldNotBeFound("productImageFullUrl.equals=" + UPDATED_PRODUCT_IMAGE_FULL_URL);
    }

    @Test
    @Transactional
    public void getAllProductsByProductImageFullUrlIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productImageFullUrl in DEFAULT_PRODUCT_IMAGE_FULL_URL or UPDATED_PRODUCT_IMAGE_FULL_URL
        defaultProductShouldBeFound("productImageFullUrl.in=" + DEFAULT_PRODUCT_IMAGE_FULL_URL + "," + UPDATED_PRODUCT_IMAGE_FULL_URL);

        // Get all the productList where productImageFullUrl equals to UPDATED_PRODUCT_IMAGE_FULL_URL
        defaultProductShouldNotBeFound("productImageFullUrl.in=" + UPDATED_PRODUCT_IMAGE_FULL_URL);
    }

    @Test
    @Transactional
    public void getAllProductsByProductImageFullUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productImageFullUrl is not null
        defaultProductShouldBeFound("productImageFullUrl.specified=true");

        // Get all the productList where productImageFullUrl is null
        defaultProductShouldNotBeFound("productImageFullUrl.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsByProductImageThumbUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productImageThumbUrl equals to DEFAULT_PRODUCT_IMAGE_THUMB_URL
        defaultProductShouldBeFound("productImageThumbUrl.equals=" + DEFAULT_PRODUCT_IMAGE_THUMB_URL);

        // Get all the productList where productImageThumbUrl equals to UPDATED_PRODUCT_IMAGE_THUMB_URL
        defaultProductShouldNotBeFound("productImageThumbUrl.equals=" + UPDATED_PRODUCT_IMAGE_THUMB_URL);
    }

    @Test
    @Transactional
    public void getAllProductsByProductImageThumbUrlIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productImageThumbUrl in DEFAULT_PRODUCT_IMAGE_THUMB_URL or UPDATED_PRODUCT_IMAGE_THUMB_URL
        defaultProductShouldBeFound("productImageThumbUrl.in=" + DEFAULT_PRODUCT_IMAGE_THUMB_URL + "," + UPDATED_PRODUCT_IMAGE_THUMB_URL);

        // Get all the productList where productImageThumbUrl equals to UPDATED_PRODUCT_IMAGE_THUMB_URL
        defaultProductShouldNotBeFound("productImageThumbUrl.in=" + UPDATED_PRODUCT_IMAGE_THUMB_URL);
    }

    @Test
    @Transactional
    public void getAllProductsByProductImageThumbUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productImageThumbUrl is not null
        defaultProductShouldBeFound("productImageThumbUrl.specified=true");

        // Get all the productList where productImageThumbUrl is null
        defaultProductShouldNotBeFound("productImageThumbUrl.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsByDateCreatedIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where dateCreated equals to DEFAULT_DATE_CREATED
        defaultProductShouldBeFound("dateCreated.equals=" + DEFAULT_DATE_CREATED);

        // Get all the productList where dateCreated equals to UPDATED_DATE_CREATED
        defaultProductShouldNotBeFound("dateCreated.equals=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    public void getAllProductsByDateCreatedIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where dateCreated in DEFAULT_DATE_CREATED or UPDATED_DATE_CREATED
        defaultProductShouldBeFound("dateCreated.in=" + DEFAULT_DATE_CREATED + "," + UPDATED_DATE_CREATED);

        // Get all the productList where dateCreated equals to UPDATED_DATE_CREATED
        defaultProductShouldNotBeFound("dateCreated.in=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    public void getAllProductsByDateCreatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where dateCreated is not null
        defaultProductShouldBeFound("dateCreated.specified=true");

        // Get all the productList where dateCreated is null
        defaultProductShouldNotBeFound("dateCreated.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsByDateCreatedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where dateCreated greater than or equals to DEFAULT_DATE_CREATED
        defaultProductShouldBeFound("dateCreated.greaterOrEqualThan=" + DEFAULT_DATE_CREATED);

        // Get all the productList where dateCreated greater than or equals to UPDATED_DATE_CREATED
        defaultProductShouldNotBeFound("dateCreated.greaterOrEqualThan=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    public void getAllProductsByDateCreatedIsLessThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where dateCreated less than or equals to DEFAULT_DATE_CREATED
        defaultProductShouldNotBeFound("dateCreated.lessThan=" + DEFAULT_DATE_CREATED);

        // Get all the productList where dateCreated less than or equals to UPDATED_DATE_CREATED
        defaultProductShouldBeFound("dateCreated.lessThan=" + UPDATED_DATE_CREATED);
    }


    @Test
    @Transactional
    public void getAllProductsByBarcodeIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where barcode equals to DEFAULT_BARCODE
        defaultProductShouldBeFound("barcode.equals=" + DEFAULT_BARCODE);

        // Get all the productList where barcode equals to UPDATED_BARCODE
        defaultProductShouldNotBeFound("barcode.equals=" + UPDATED_BARCODE);
    }

    @Test
    @Transactional
    public void getAllProductsByBarcodeIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where barcode in DEFAULT_BARCODE or UPDATED_BARCODE
        defaultProductShouldBeFound("barcode.in=" + DEFAULT_BARCODE + "," + UPDATED_BARCODE);

        // Get all the productList where barcode equals to UPDATED_BARCODE
        defaultProductShouldNotBeFound("barcode.in=" + UPDATED_BARCODE);
    }

    @Test
    @Transactional
    public void getAllProductsByBarcodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where barcode is not null
        defaultProductShouldBeFound("barcode.specified=true");

        // Get all the productList where barcode is null
        defaultProductShouldNotBeFound("barcode.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsBySerialCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where serialCode equals to DEFAULT_SERIAL_CODE
        defaultProductShouldBeFound("serialCode.equals=" + DEFAULT_SERIAL_CODE);

        // Get all the productList where serialCode equals to UPDATED_SERIAL_CODE
        defaultProductShouldNotBeFound("serialCode.equals=" + UPDATED_SERIAL_CODE);
    }

    @Test
    @Transactional
    public void getAllProductsBySerialCodeIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where serialCode in DEFAULT_SERIAL_CODE or UPDATED_SERIAL_CODE
        defaultProductShouldBeFound("serialCode.in=" + DEFAULT_SERIAL_CODE + "," + UPDATED_SERIAL_CODE);

        // Get all the productList where serialCode equals to UPDATED_SERIAL_CODE
        defaultProductShouldNotBeFound("serialCode.in=" + UPDATED_SERIAL_CODE);
    }

    @Test
    @Transactional
    public void getAllProductsBySerialCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where serialCode is not null
        defaultProductShouldBeFound("serialCode.specified=true");

        // Get all the productList where serialCode is null
        defaultProductShouldNotBeFound("serialCode.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsByPriorityPositionIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where priorityPosition equals to DEFAULT_PRIORITY_POSITION
        defaultProductShouldBeFound("priorityPosition.equals=" + DEFAULT_PRIORITY_POSITION);

        // Get all the productList where priorityPosition equals to UPDATED_PRIORITY_POSITION
        defaultProductShouldNotBeFound("priorityPosition.equals=" + UPDATED_PRIORITY_POSITION);
    }

    @Test
    @Transactional
    public void getAllProductsByPriorityPositionIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where priorityPosition in DEFAULT_PRIORITY_POSITION or UPDATED_PRIORITY_POSITION
        defaultProductShouldBeFound("priorityPosition.in=" + DEFAULT_PRIORITY_POSITION + "," + UPDATED_PRIORITY_POSITION);

        // Get all the productList where priorityPosition equals to UPDATED_PRIORITY_POSITION
        defaultProductShouldNotBeFound("priorityPosition.in=" + UPDATED_PRIORITY_POSITION);
    }

    @Test
    @Transactional
    public void getAllProductsByPriorityPositionIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where priorityPosition is not null
        defaultProductShouldBeFound("priorityPosition.specified=true");

        // Get all the productList where priorityPosition is null
        defaultProductShouldNotBeFound("priorityPosition.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsByPriorityPositionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where priorityPosition greater than or equals to DEFAULT_PRIORITY_POSITION
        defaultProductShouldBeFound("priorityPosition.greaterOrEqualThan=" + DEFAULT_PRIORITY_POSITION);

        // Get all the productList where priorityPosition greater than or equals to UPDATED_PRIORITY_POSITION
        defaultProductShouldNotBeFound("priorityPosition.greaterOrEqualThan=" + UPDATED_PRIORITY_POSITION);
    }

    @Test
    @Transactional
    public void getAllProductsByPriorityPositionIsLessThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where priorityPosition less than or equals to DEFAULT_PRIORITY_POSITION
        defaultProductShouldNotBeFound("priorityPosition.lessThan=" + DEFAULT_PRIORITY_POSITION);

        // Get all the productList where priorityPosition less than or equals to UPDATED_PRIORITY_POSITION
        defaultProductShouldBeFound("priorityPosition.lessThan=" + UPDATED_PRIORITY_POSITION);
    }


    @Test
    @Transactional
    public void getAllProductsByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where active equals to DEFAULT_ACTIVE
        defaultProductShouldBeFound("active.equals=" + DEFAULT_ACTIVE);

        // Get all the productList where active equals to UPDATED_ACTIVE
        defaultProductShouldNotBeFound("active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllProductsByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where active in DEFAULT_ACTIVE or UPDATED_ACTIVE
        defaultProductShouldBeFound("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE);

        // Get all the productList where active equals to UPDATED_ACTIVE
        defaultProductShouldNotBeFound("active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllProductsByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where active is not null
        defaultProductShouldBeFound("active.specified=true");

        // Get all the productList where active is null
        defaultProductShouldNotBeFound("active.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsByIsVariantProductIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where isVariantProduct equals to DEFAULT_IS_VARIANT_PRODUCT
        defaultProductShouldBeFound("isVariantProduct.equals=" + DEFAULT_IS_VARIANT_PRODUCT);

        // Get all the productList where isVariantProduct equals to UPDATED_IS_VARIANT_PRODUCT
        defaultProductShouldNotBeFound("isVariantProduct.equals=" + UPDATED_IS_VARIANT_PRODUCT);
    }

    @Test
    @Transactional
    public void getAllProductsByIsVariantProductIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where isVariantProduct in DEFAULT_IS_VARIANT_PRODUCT or UPDATED_IS_VARIANT_PRODUCT
        defaultProductShouldBeFound("isVariantProduct.in=" + DEFAULT_IS_VARIANT_PRODUCT + "," + UPDATED_IS_VARIANT_PRODUCT);

        // Get all the productList where isVariantProduct equals to UPDATED_IS_VARIANT_PRODUCT
        defaultProductShouldNotBeFound("isVariantProduct.in=" + UPDATED_IS_VARIANT_PRODUCT);
    }

    @Test
    @Transactional
    public void getAllProductsByIsVariantProductIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where isVariantProduct is not null
        defaultProductShouldBeFound("isVariantProduct.specified=true");

        // Get all the productList where isVariantProduct is null
        defaultProductShouldNotBeFound("isVariantProduct.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsByVariantsIsEqualToSomething() throws Exception {
        // Initialize the database
        ProductVariant variants = ProductVariantResourceIntTest.createEntity(em);
        em.persist(variants);
        em.flush();
        product.addVariants(variants);
        productRepository.saveAndFlush(product);
        Long variantsId = variants.getId();

        // Get all the productList where variants equals to variantsId
        defaultProductShouldBeFound("variantsId.equals=" + variantsId);

        // Get all the productList where variants equals to variantsId + 1
        defaultProductShouldNotBeFound("variantsId.equals=" + (variantsId + 1));
    }


    @Test
    @Transactional
    public void getAllProductsByExtrasIsEqualToSomething() throws Exception {
        // Initialize the database
        ProductExtra extras = ProductExtraResourceIntTest.createEntity(em);
        em.persist(extras);
        em.flush();
        product.addExtras(extras);
        productRepository.saveAndFlush(product);
        Long extrasId = extras.getId();

        // Get all the productList where extras equals to extrasId
        defaultProductShouldBeFound("extrasId.equals=" + extrasId);

        // Get all the productList where extras equals to extrasId + 1
        defaultProductShouldNotBeFound("extrasId.equals=" + (extrasId + 1));
    }


    @Test
    @Transactional
    public void getAllProductsByProductTypesIsEqualToSomething() throws Exception {
        // Initialize the database
        ProductType productTypes = ProductTypeResourceIntTest.createEntity(em);
        em.persist(productTypes);
        em.flush();
        product.setProductTypes(productTypes);
        productRepository.saveAndFlush(product);
        Long productTypesId = productTypes.getId();

        // Get all the productList where productTypes equals to productTypesId
        defaultProductShouldBeFound("productTypesId.equals=" + productTypesId);

        // Get all the productList where productTypes equals to productTypesId + 1
        defaultProductShouldNotBeFound("productTypesId.equals=" + (productTypesId + 1));
    }


    @Test
    @Transactional
    public void getAllProductsByShopIsEqualToSomething() throws Exception {
        // Initialize the database
        Shop shop = ShopResourceIntTest.createEntity(em);
        em.persist(shop);
        em.flush();
        product.setShop(shop);
        productRepository.saveAndFlush(product);
        Long shopId = shop.getId();

        // Get all the productList where shop equals to shopId
        defaultProductShouldBeFound("shopId.equals=" + shopId);

        // Get all the productList where shop equals to shopId + 1
        defaultProductShouldNotBeFound("shopId.equals=" + (shopId + 1));
    }


    @Test
    @Transactional
    public void getAllProductsByDiscountsIsEqualToSomething() throws Exception {
        // Initialize the database
        Discount discounts = DiscountResourceIntTest.createEntity(em);
        em.persist(discounts);
        em.flush();
        product.setDiscounts(discounts);
        productRepository.saveAndFlush(product);
        Long discountsId = discounts.getId();

        // Get all the productList where discounts equals to discountsId
        defaultProductShouldBeFound("discountsId.equals=" + discountsId);

        // Get all the productList where discounts equals to discountsId + 1
        defaultProductShouldNotBeFound("discountsId.equals=" + (discountsId + 1));
    }


    @Test
    @Transactional
    public void getAllProductsByTaxesIsEqualToSomething() throws Exception {
        // Initialize the database
        Tax taxes = TaxResourceIntTest.createEntity(em);
        em.persist(taxes);
        em.flush();
        product.setTaxes(taxes);
        productRepository.saveAndFlush(product);
        Long taxesId = taxes.getId();

        // Get all the productList where taxes equals to taxesId
        defaultProductShouldBeFound("taxesId.equals=" + taxesId);

        // Get all the productList where taxes equals to taxesId + 1
        defaultProductShouldNotBeFound("taxesId.equals=" + (taxesId + 1));
    }


    @Test
    @Transactional
    public void getAllProductsByCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        ProductCategory category = ProductCategoryResourceIntTest.createEntity(em);
        em.persist(category);
        em.flush();
        product.setCategory(category);
        productRepository.saveAndFlush(product);
        Long categoryId = category.getId();

        // Get all the productList where category equals to categoryId
        defaultProductShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the productList where category equals to categoryId + 1
        defaultProductShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultProductShouldBeFound(String filter) throws Exception {
        restProductMockMvc.perform(get("/api/products?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].productName").value(hasItem(DEFAULT_PRODUCT_NAME.toString())))
            .andExpect(jsonPath("$.[*].productDescription").value(hasItem(DEFAULT_PRODUCT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].productImageFullContentType").value(hasItem(DEFAULT_PRODUCT_IMAGE_FULL_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].productImageFull").value(hasItem(Base64Utils.encodeToString(DEFAULT_PRODUCT_IMAGE_FULL))))
            .andExpect(jsonPath("$.[*].productImageFullUrl").value(hasItem(DEFAULT_PRODUCT_IMAGE_FULL_URL.toString())))
            .andExpect(jsonPath("$.[*].productImageThumbContentType").value(hasItem(DEFAULT_PRODUCT_IMAGE_THUMB_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].productImageThumb").value(hasItem(Base64Utils.encodeToString(DEFAULT_PRODUCT_IMAGE_THUMB))))
            .andExpect(jsonPath("$.[*].productImageThumbUrl").value(hasItem(DEFAULT_PRODUCT_IMAGE_THUMB_URL.toString())))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(sameInstant(DEFAULT_DATE_CREATED))))
            .andExpect(jsonPath("$.[*].barcode").value(hasItem(DEFAULT_BARCODE.toString())))
            .andExpect(jsonPath("$.[*].serialCode").value(hasItem(DEFAULT_SERIAL_CODE.toString())))
            .andExpect(jsonPath("$.[*].priorityPosition").value(hasItem(DEFAULT_PRIORITY_POSITION)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].isVariantProduct").value(hasItem(DEFAULT_IS_VARIANT_PRODUCT.booleanValue())));

        // Check, that the count call also returns 1
        restProductMockMvc.perform(get("/api/products/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultProductShouldNotBeFound(String filter) throws Exception {
        restProductMockMvc.perform(get("/api/products?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductMockMvc.perform(get("/api/products/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingProduct() throws Exception {
        // Get the product
        restProductMockMvc.perform(get("/api/products/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Update the product
        Product updatedProduct = productRepository.findById(product.getId()).get();
        // Disconnect from session so that the updates on updatedProduct are not directly saved in db
        em.detach(updatedProduct);
        updatedProduct
            .productName(UPDATED_PRODUCT_NAME)
            .productDescription(UPDATED_PRODUCT_DESCRIPTION)
            .price(UPDATED_PRICE)
            .quantity(UPDATED_QUANTITY)
            .productImageFull(UPDATED_PRODUCT_IMAGE_FULL)
            .productImageFullContentType(UPDATED_PRODUCT_IMAGE_FULL_CONTENT_TYPE)
            .productImageFullUrl(UPDATED_PRODUCT_IMAGE_FULL_URL)
            .productImageThumb(UPDATED_PRODUCT_IMAGE_THUMB)
            .productImageThumbContentType(UPDATED_PRODUCT_IMAGE_THUMB_CONTENT_TYPE)
            .productImageThumbUrl(UPDATED_PRODUCT_IMAGE_THUMB_URL)
            .dateCreated(UPDATED_DATE_CREATED)
            .barcode(UPDATED_BARCODE)
            .serialCode(UPDATED_SERIAL_CODE)
            .priorityPosition(UPDATED_PRIORITY_POSITION)
            .active(UPDATED_ACTIVE)
            .isVariantProduct(UPDATED_IS_VARIANT_PRODUCT);
        ProductDTO productDTO = productMapper.toDto(updatedProduct);

        restProductMockMvc.perform(put("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isOk());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getProductName()).isEqualTo(UPDATED_PRODUCT_NAME);
        assertThat(testProduct.getProductDescription()).isEqualTo(UPDATED_PRODUCT_DESCRIPTION);
        assertThat(testProduct.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testProduct.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testProduct.getProductImageFull()).isEqualTo(UPDATED_PRODUCT_IMAGE_FULL);
        assertThat(testProduct.getProductImageFullContentType()).isEqualTo(UPDATED_PRODUCT_IMAGE_FULL_CONTENT_TYPE);
        assertThat(testProduct.getProductImageFullUrl()).isEqualTo(UPDATED_PRODUCT_IMAGE_FULL_URL);
        assertThat(testProduct.getProductImageThumb()).isEqualTo(UPDATED_PRODUCT_IMAGE_THUMB);
        assertThat(testProduct.getProductImageThumbContentType()).isEqualTo(UPDATED_PRODUCT_IMAGE_THUMB_CONTENT_TYPE);
        assertThat(testProduct.getProductImageThumbUrl()).isEqualTo(UPDATED_PRODUCT_IMAGE_THUMB_URL);
        assertThat(testProduct.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testProduct.getBarcode()).isEqualTo(UPDATED_BARCODE);
        assertThat(testProduct.getSerialCode()).isEqualTo(UPDATED_SERIAL_CODE);
        assertThat(testProduct.getPriorityPosition()).isEqualTo(UPDATED_PRIORITY_POSITION);
        assertThat(testProduct.isActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testProduct.isIsVariantProduct()).isEqualTo(UPDATED_IS_VARIANT_PRODUCT);

        // Validate the Product in Elasticsearch
        verify(mockProductSearchRepository, times(1)).save(testProduct);
    }

    @Test
    @Transactional
    public void updateNonExistingProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductMockMvc.perform(put("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Product in Elasticsearch
        verify(mockProductSearchRepository, times(0)).save(product);
    }

    @Test
    @Transactional
    public void deleteProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        int databaseSizeBeforeDelete = productRepository.findAll().size();

        // Get the product
        restProductMockMvc.perform(delete("/api/products/{id}", product.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Product in Elasticsearch
        verify(mockProductSearchRepository, times(1)).deleteById(product.getId());
    }

    @Test
    @Transactional
    public void searchProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);
        when(mockProductSearchRepository.search(queryStringQuery("id:" + product.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(product), PageRequest.of(0, 1), 1));
        // Search the product
        restProductMockMvc.perform(get("/api/_search/products?query=id:" + product.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].productName").value(hasItem(DEFAULT_PRODUCT_NAME)))
            .andExpect(jsonPath("$.[*].productDescription").value(hasItem(DEFAULT_PRODUCT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].productImageFullContentType").value(hasItem(DEFAULT_PRODUCT_IMAGE_FULL_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].productImageFull").value(hasItem(Base64Utils.encodeToString(DEFAULT_PRODUCT_IMAGE_FULL))))
            .andExpect(jsonPath("$.[*].productImageFullUrl").value(hasItem(DEFAULT_PRODUCT_IMAGE_FULL_URL)))
            .andExpect(jsonPath("$.[*].productImageThumbContentType").value(hasItem(DEFAULT_PRODUCT_IMAGE_THUMB_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].productImageThumb").value(hasItem(Base64Utils.encodeToString(DEFAULT_PRODUCT_IMAGE_THUMB))))
            .andExpect(jsonPath("$.[*].productImageThumbUrl").value(hasItem(DEFAULT_PRODUCT_IMAGE_THUMB_URL)))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(sameInstant(DEFAULT_DATE_CREATED))))
            .andExpect(jsonPath("$.[*].barcode").value(hasItem(DEFAULT_BARCODE)))
            .andExpect(jsonPath("$.[*].serialCode").value(hasItem(DEFAULT_SERIAL_CODE)))
            .andExpect(jsonPath("$.[*].priorityPosition").value(hasItem(DEFAULT_PRIORITY_POSITION)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].isVariantProduct").value(hasItem(DEFAULT_IS_VARIANT_PRODUCT.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Product.class);
        Product product1 = new Product();
        product1.setId(1L);
        Product product2 = new Product();
        product2.setId(product1.getId());
        assertThat(product1).isEqualTo(product2);
        product2.setId(2L);
        assertThat(product1).isNotEqualTo(product2);
        product1.setId(null);
        assertThat(product1).isNotEqualTo(product2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductDTO.class);
        ProductDTO productDTO1 = new ProductDTO();
        productDTO1.setId(1L);
        ProductDTO productDTO2 = new ProductDTO();
        assertThat(productDTO1).isNotEqualTo(productDTO2);
        productDTO2.setId(productDTO1.getId());
        assertThat(productDTO1).isEqualTo(productDTO2);
        productDTO2.setId(2L);
        assertThat(productDTO1).isNotEqualTo(productDTO2);
        productDTO1.setId(null);
        assertThat(productDTO1).isNotEqualTo(productDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(productMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(productMapper.fromId(null)).isNull();
    }
}
