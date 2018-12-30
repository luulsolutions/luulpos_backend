package com.luulsolutions.luulpos.web.rest;

import com.luulsolutions.luulpos.LuulposApp;

import com.luulsolutions.luulpos.domain.ShopDevice;
import com.luulsolutions.luulpos.domain.Shop;
import com.luulsolutions.luulpos.repository.ShopDeviceRepository;
import com.luulsolutions.luulpos.repository.search.ShopDeviceSearchRepository;
import com.luulsolutions.luulpos.service.ShopDeviceService;
import com.luulsolutions.luulpos.service.dto.ShopDeviceDTO;
import com.luulsolutions.luulpos.service.mapper.ShopDeviceMapper;
import com.luulsolutions.luulpos.web.rest.errors.ExceptionTranslator;
import com.luulsolutions.luulpos.service.dto.ShopDeviceCriteria;
import com.luulsolutions.luulpos.service.ShopDeviceQueryService;

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
 * Test class for the ShopDeviceResource REST controller.
 *
 * @see ShopDeviceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LuulposApp.class)
public class ShopDeviceResourceIntTest {

    private static final String DEFAULT_DEVICE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DEVICE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DEVICE_MODEL = "AAAAAAAAAA";
    private static final String UPDATED_DEVICE_MODEL = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_REGISTERED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_REGISTERED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private ShopDeviceRepository shopDeviceRepository;

    @Autowired
    private ShopDeviceMapper shopDeviceMapper;

    @Autowired
    private ShopDeviceService shopDeviceService;

    /**
     * This repository is mocked in the com.luulsolutions.luulpos.repository.search test package.
     *
     * @see com.luulsolutions.luulpos.repository.search.ShopDeviceSearchRepositoryMockConfiguration
     */
    @Autowired
    private ShopDeviceSearchRepository mockShopDeviceSearchRepository;

    @Autowired
    private ShopDeviceQueryService shopDeviceQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restShopDeviceMockMvc;

    private ShopDevice shopDevice;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ShopDeviceResource shopDeviceResource = new ShopDeviceResource(shopDeviceService, shopDeviceQueryService);
        this.restShopDeviceMockMvc = MockMvcBuilders.standaloneSetup(shopDeviceResource)
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
    public static ShopDevice createEntity(EntityManager em) {
        ShopDevice shopDevice = new ShopDevice()
            .deviceName(DEFAULT_DEVICE_NAME)
            .deviceModel(DEFAULT_DEVICE_MODEL)
            .registeredDate(DEFAULT_REGISTERED_DATE);
        return shopDevice;
    }

    @Before
    public void initTest() {
        shopDevice = createEntity(em);
    }

    @Test
    @Transactional
    public void createShopDevice() throws Exception {
        int databaseSizeBeforeCreate = shopDeviceRepository.findAll().size();

        // Create the ShopDevice
        ShopDeviceDTO shopDeviceDTO = shopDeviceMapper.toDto(shopDevice);
        restShopDeviceMockMvc.perform(post("/api/shop-devices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shopDeviceDTO)))
            .andExpect(status().isCreated());

        // Validate the ShopDevice in the database
        List<ShopDevice> shopDeviceList = shopDeviceRepository.findAll();
        assertThat(shopDeviceList).hasSize(databaseSizeBeforeCreate + 1);
        ShopDevice testShopDevice = shopDeviceList.get(shopDeviceList.size() - 1);
        assertThat(testShopDevice.getDeviceName()).isEqualTo(DEFAULT_DEVICE_NAME);
        assertThat(testShopDevice.getDeviceModel()).isEqualTo(DEFAULT_DEVICE_MODEL);
        assertThat(testShopDevice.getRegisteredDate()).isEqualTo(DEFAULT_REGISTERED_DATE);

        // Validate the ShopDevice in Elasticsearch
        verify(mockShopDeviceSearchRepository, times(1)).save(testShopDevice);
    }

    @Test
    @Transactional
    public void createShopDeviceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = shopDeviceRepository.findAll().size();

        // Create the ShopDevice with an existing ID
        shopDevice.setId(1L);
        ShopDeviceDTO shopDeviceDTO = shopDeviceMapper.toDto(shopDevice);

        // An entity with an existing ID cannot be created, so this API call must fail
        restShopDeviceMockMvc.perform(post("/api/shop-devices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shopDeviceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ShopDevice in the database
        List<ShopDevice> shopDeviceList = shopDeviceRepository.findAll();
        assertThat(shopDeviceList).hasSize(databaseSizeBeforeCreate);

        // Validate the ShopDevice in Elasticsearch
        verify(mockShopDeviceSearchRepository, times(0)).save(shopDevice);
    }

    @Test
    @Transactional
    public void getAllShopDevices() throws Exception {
        // Initialize the database
        shopDeviceRepository.saveAndFlush(shopDevice);

        // Get all the shopDeviceList
        restShopDeviceMockMvc.perform(get("/api/shop-devices?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shopDevice.getId().intValue())))
            .andExpect(jsonPath("$.[*].deviceName").value(hasItem(DEFAULT_DEVICE_NAME.toString())))
            .andExpect(jsonPath("$.[*].deviceModel").value(hasItem(DEFAULT_DEVICE_MODEL.toString())))
            .andExpect(jsonPath("$.[*].registeredDate").value(hasItem(sameInstant(DEFAULT_REGISTERED_DATE))));
    }
    
    @Test
    @Transactional
    public void getShopDevice() throws Exception {
        // Initialize the database
        shopDeviceRepository.saveAndFlush(shopDevice);

        // Get the shopDevice
        restShopDeviceMockMvc.perform(get("/api/shop-devices/{id}", shopDevice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(shopDevice.getId().intValue()))
            .andExpect(jsonPath("$.deviceName").value(DEFAULT_DEVICE_NAME.toString()))
            .andExpect(jsonPath("$.deviceModel").value(DEFAULT_DEVICE_MODEL.toString()))
            .andExpect(jsonPath("$.registeredDate").value(sameInstant(DEFAULT_REGISTERED_DATE)));
    }

    @Test
    @Transactional
    public void getAllShopDevicesByDeviceNameIsEqualToSomething() throws Exception {
        // Initialize the database
        shopDeviceRepository.saveAndFlush(shopDevice);

        // Get all the shopDeviceList where deviceName equals to DEFAULT_DEVICE_NAME
        defaultShopDeviceShouldBeFound("deviceName.equals=" + DEFAULT_DEVICE_NAME);

        // Get all the shopDeviceList where deviceName equals to UPDATED_DEVICE_NAME
        defaultShopDeviceShouldNotBeFound("deviceName.equals=" + UPDATED_DEVICE_NAME);
    }

    @Test
    @Transactional
    public void getAllShopDevicesByDeviceNameIsInShouldWork() throws Exception {
        // Initialize the database
        shopDeviceRepository.saveAndFlush(shopDevice);

        // Get all the shopDeviceList where deviceName in DEFAULT_DEVICE_NAME or UPDATED_DEVICE_NAME
        defaultShopDeviceShouldBeFound("deviceName.in=" + DEFAULT_DEVICE_NAME + "," + UPDATED_DEVICE_NAME);

        // Get all the shopDeviceList where deviceName equals to UPDATED_DEVICE_NAME
        defaultShopDeviceShouldNotBeFound("deviceName.in=" + UPDATED_DEVICE_NAME);
    }

    @Test
    @Transactional
    public void getAllShopDevicesByDeviceNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        shopDeviceRepository.saveAndFlush(shopDevice);

        // Get all the shopDeviceList where deviceName is not null
        defaultShopDeviceShouldBeFound("deviceName.specified=true");

        // Get all the shopDeviceList where deviceName is null
        defaultShopDeviceShouldNotBeFound("deviceName.specified=false");
    }

    @Test
    @Transactional
    public void getAllShopDevicesByDeviceModelIsEqualToSomething() throws Exception {
        // Initialize the database
        shopDeviceRepository.saveAndFlush(shopDevice);

        // Get all the shopDeviceList where deviceModel equals to DEFAULT_DEVICE_MODEL
        defaultShopDeviceShouldBeFound("deviceModel.equals=" + DEFAULT_DEVICE_MODEL);

        // Get all the shopDeviceList where deviceModel equals to UPDATED_DEVICE_MODEL
        defaultShopDeviceShouldNotBeFound("deviceModel.equals=" + UPDATED_DEVICE_MODEL);
    }

    @Test
    @Transactional
    public void getAllShopDevicesByDeviceModelIsInShouldWork() throws Exception {
        // Initialize the database
        shopDeviceRepository.saveAndFlush(shopDevice);

        // Get all the shopDeviceList where deviceModel in DEFAULT_DEVICE_MODEL or UPDATED_DEVICE_MODEL
        defaultShopDeviceShouldBeFound("deviceModel.in=" + DEFAULT_DEVICE_MODEL + "," + UPDATED_DEVICE_MODEL);

        // Get all the shopDeviceList where deviceModel equals to UPDATED_DEVICE_MODEL
        defaultShopDeviceShouldNotBeFound("deviceModel.in=" + UPDATED_DEVICE_MODEL);
    }

    @Test
    @Transactional
    public void getAllShopDevicesByDeviceModelIsNullOrNotNull() throws Exception {
        // Initialize the database
        shopDeviceRepository.saveAndFlush(shopDevice);

        // Get all the shopDeviceList where deviceModel is not null
        defaultShopDeviceShouldBeFound("deviceModel.specified=true");

        // Get all the shopDeviceList where deviceModel is null
        defaultShopDeviceShouldNotBeFound("deviceModel.specified=false");
    }

    @Test
    @Transactional
    public void getAllShopDevicesByRegisteredDateIsEqualToSomething() throws Exception {
        // Initialize the database
        shopDeviceRepository.saveAndFlush(shopDevice);

        // Get all the shopDeviceList where registeredDate equals to DEFAULT_REGISTERED_DATE
        defaultShopDeviceShouldBeFound("registeredDate.equals=" + DEFAULT_REGISTERED_DATE);

        // Get all the shopDeviceList where registeredDate equals to UPDATED_REGISTERED_DATE
        defaultShopDeviceShouldNotBeFound("registeredDate.equals=" + UPDATED_REGISTERED_DATE);
    }

    @Test
    @Transactional
    public void getAllShopDevicesByRegisteredDateIsInShouldWork() throws Exception {
        // Initialize the database
        shopDeviceRepository.saveAndFlush(shopDevice);

        // Get all the shopDeviceList where registeredDate in DEFAULT_REGISTERED_DATE or UPDATED_REGISTERED_DATE
        defaultShopDeviceShouldBeFound("registeredDate.in=" + DEFAULT_REGISTERED_DATE + "," + UPDATED_REGISTERED_DATE);

        // Get all the shopDeviceList where registeredDate equals to UPDATED_REGISTERED_DATE
        defaultShopDeviceShouldNotBeFound("registeredDate.in=" + UPDATED_REGISTERED_DATE);
    }

    @Test
    @Transactional
    public void getAllShopDevicesByRegisteredDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        shopDeviceRepository.saveAndFlush(shopDevice);

        // Get all the shopDeviceList where registeredDate is not null
        defaultShopDeviceShouldBeFound("registeredDate.specified=true");

        // Get all the shopDeviceList where registeredDate is null
        defaultShopDeviceShouldNotBeFound("registeredDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllShopDevicesByRegisteredDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        shopDeviceRepository.saveAndFlush(shopDevice);

        // Get all the shopDeviceList where registeredDate greater than or equals to DEFAULT_REGISTERED_DATE
        defaultShopDeviceShouldBeFound("registeredDate.greaterOrEqualThan=" + DEFAULT_REGISTERED_DATE);

        // Get all the shopDeviceList where registeredDate greater than or equals to UPDATED_REGISTERED_DATE
        defaultShopDeviceShouldNotBeFound("registeredDate.greaterOrEqualThan=" + UPDATED_REGISTERED_DATE);
    }

    @Test
    @Transactional
    public void getAllShopDevicesByRegisteredDateIsLessThanSomething() throws Exception {
        // Initialize the database
        shopDeviceRepository.saveAndFlush(shopDevice);

        // Get all the shopDeviceList where registeredDate less than or equals to DEFAULT_REGISTERED_DATE
        defaultShopDeviceShouldNotBeFound("registeredDate.lessThan=" + DEFAULT_REGISTERED_DATE);

        // Get all the shopDeviceList where registeredDate less than or equals to UPDATED_REGISTERED_DATE
        defaultShopDeviceShouldBeFound("registeredDate.lessThan=" + UPDATED_REGISTERED_DATE);
    }


    @Test
    @Transactional
    public void getAllShopDevicesByShopIsEqualToSomething() throws Exception {
        // Initialize the database
        Shop shop = ShopResourceIntTest.createEntity(em);
        em.persist(shop);
        em.flush();
        shopDevice.setShop(shop);
        shopDeviceRepository.saveAndFlush(shopDevice);
        Long shopId = shop.getId();

        // Get all the shopDeviceList where shop equals to shopId
        defaultShopDeviceShouldBeFound("shopId.equals=" + shopId);

        // Get all the shopDeviceList where shop equals to shopId + 1
        defaultShopDeviceShouldNotBeFound("shopId.equals=" + (shopId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultShopDeviceShouldBeFound(String filter) throws Exception {
        restShopDeviceMockMvc.perform(get("/api/shop-devices?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shopDevice.getId().intValue())))
            .andExpect(jsonPath("$.[*].deviceName").value(hasItem(DEFAULT_DEVICE_NAME.toString())))
            .andExpect(jsonPath("$.[*].deviceModel").value(hasItem(DEFAULT_DEVICE_MODEL.toString())))
            .andExpect(jsonPath("$.[*].registeredDate").value(hasItem(sameInstant(DEFAULT_REGISTERED_DATE))));

        // Check, that the count call also returns 1
        restShopDeviceMockMvc.perform(get("/api/shop-devices/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultShopDeviceShouldNotBeFound(String filter) throws Exception {
        restShopDeviceMockMvc.perform(get("/api/shop-devices?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restShopDeviceMockMvc.perform(get("/api/shop-devices/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingShopDevice() throws Exception {
        // Get the shopDevice
        restShopDeviceMockMvc.perform(get("/api/shop-devices/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateShopDevice() throws Exception {
        // Initialize the database
        shopDeviceRepository.saveAndFlush(shopDevice);

        int databaseSizeBeforeUpdate = shopDeviceRepository.findAll().size();

        // Update the shopDevice
        ShopDevice updatedShopDevice = shopDeviceRepository.findById(shopDevice.getId()).get();
        // Disconnect from session so that the updates on updatedShopDevice are not directly saved in db
        em.detach(updatedShopDevice);
        updatedShopDevice
            .deviceName(UPDATED_DEVICE_NAME)
            .deviceModel(UPDATED_DEVICE_MODEL)
            .registeredDate(UPDATED_REGISTERED_DATE);
        ShopDeviceDTO shopDeviceDTO = shopDeviceMapper.toDto(updatedShopDevice);

        restShopDeviceMockMvc.perform(put("/api/shop-devices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shopDeviceDTO)))
            .andExpect(status().isOk());

        // Validate the ShopDevice in the database
        List<ShopDevice> shopDeviceList = shopDeviceRepository.findAll();
        assertThat(shopDeviceList).hasSize(databaseSizeBeforeUpdate);
        ShopDevice testShopDevice = shopDeviceList.get(shopDeviceList.size() - 1);
        assertThat(testShopDevice.getDeviceName()).isEqualTo(UPDATED_DEVICE_NAME);
        assertThat(testShopDevice.getDeviceModel()).isEqualTo(UPDATED_DEVICE_MODEL);
        assertThat(testShopDevice.getRegisteredDate()).isEqualTo(UPDATED_REGISTERED_DATE);

        // Validate the ShopDevice in Elasticsearch
        verify(mockShopDeviceSearchRepository, times(1)).save(testShopDevice);
    }

    @Test
    @Transactional
    public void updateNonExistingShopDevice() throws Exception {
        int databaseSizeBeforeUpdate = shopDeviceRepository.findAll().size();

        // Create the ShopDevice
        ShopDeviceDTO shopDeviceDTO = shopDeviceMapper.toDto(shopDevice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShopDeviceMockMvc.perform(put("/api/shop-devices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shopDeviceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ShopDevice in the database
        List<ShopDevice> shopDeviceList = shopDeviceRepository.findAll();
        assertThat(shopDeviceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ShopDevice in Elasticsearch
        verify(mockShopDeviceSearchRepository, times(0)).save(shopDevice);
    }

    @Test
    @Transactional
    public void deleteShopDevice() throws Exception {
        // Initialize the database
        shopDeviceRepository.saveAndFlush(shopDevice);

        int databaseSizeBeforeDelete = shopDeviceRepository.findAll().size();

        // Get the shopDevice
        restShopDeviceMockMvc.perform(delete("/api/shop-devices/{id}", shopDevice.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ShopDevice> shopDeviceList = shopDeviceRepository.findAll();
        assertThat(shopDeviceList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ShopDevice in Elasticsearch
        verify(mockShopDeviceSearchRepository, times(1)).deleteById(shopDevice.getId());
    }

    @Test
    @Transactional
    public void searchShopDevice() throws Exception {
        // Initialize the database
        shopDeviceRepository.saveAndFlush(shopDevice);
        when(mockShopDeviceSearchRepository.search(queryStringQuery("id:" + shopDevice.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(shopDevice), PageRequest.of(0, 1), 1));
        // Search the shopDevice
        restShopDeviceMockMvc.perform(get("/api/_search/shop-devices?query=id:" + shopDevice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shopDevice.getId().intValue())))
            .andExpect(jsonPath("$.[*].deviceName").value(hasItem(DEFAULT_DEVICE_NAME)))
            .andExpect(jsonPath("$.[*].deviceModel").value(hasItem(DEFAULT_DEVICE_MODEL)))
            .andExpect(jsonPath("$.[*].registeredDate").value(hasItem(sameInstant(DEFAULT_REGISTERED_DATE))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShopDevice.class);
        ShopDevice shopDevice1 = new ShopDevice();
        shopDevice1.setId(1L);
        ShopDevice shopDevice2 = new ShopDevice();
        shopDevice2.setId(shopDevice1.getId());
        assertThat(shopDevice1).isEqualTo(shopDevice2);
        shopDevice2.setId(2L);
        assertThat(shopDevice1).isNotEqualTo(shopDevice2);
        shopDevice1.setId(null);
        assertThat(shopDevice1).isNotEqualTo(shopDevice2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShopDeviceDTO.class);
        ShopDeviceDTO shopDeviceDTO1 = new ShopDeviceDTO();
        shopDeviceDTO1.setId(1L);
        ShopDeviceDTO shopDeviceDTO2 = new ShopDeviceDTO();
        assertThat(shopDeviceDTO1).isNotEqualTo(shopDeviceDTO2);
        shopDeviceDTO2.setId(shopDeviceDTO1.getId());
        assertThat(shopDeviceDTO1).isEqualTo(shopDeviceDTO2);
        shopDeviceDTO2.setId(2L);
        assertThat(shopDeviceDTO1).isNotEqualTo(shopDeviceDTO2);
        shopDeviceDTO1.setId(null);
        assertThat(shopDeviceDTO1).isNotEqualTo(shopDeviceDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(shopDeviceMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(shopDeviceMapper.fromId(null)).isNull();
    }
}
