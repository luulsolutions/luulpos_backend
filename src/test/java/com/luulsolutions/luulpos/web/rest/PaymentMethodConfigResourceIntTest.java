package com.luulsolutions.luulpos.web.rest;

import com.luulsolutions.luulpos.LuulposApp;

import com.luulsolutions.luulpos.domain.PaymentMethodConfig;
import com.luulsolutions.luulpos.domain.PaymentMethod;
import com.luulsolutions.luulpos.repository.PaymentMethodConfigRepository;
import com.luulsolutions.luulpos.repository.search.PaymentMethodConfigSearchRepository;
import com.luulsolutions.luulpos.service.PaymentMethodConfigService;
import com.luulsolutions.luulpos.service.dto.PaymentMethodConfigDTO;
import com.luulsolutions.luulpos.service.mapper.PaymentMethodConfigMapper;
import com.luulsolutions.luulpos.web.rest.errors.ExceptionTranslator;
import com.luulsolutions.luulpos.service.dto.PaymentMethodConfigCriteria;
import com.luulsolutions.luulpos.service.PaymentMethodConfigQueryService;

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
 * Test class for the PaymentMethodConfigResource REST controller.
 *
 * @see PaymentMethodConfigResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LuulposApp.class)
public class PaymentMethodConfigResourceIntTest {

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    @Autowired
    private PaymentMethodConfigRepository paymentMethodConfigRepository;

    @Autowired
    private PaymentMethodConfigMapper paymentMethodConfigMapper;

    @Autowired
    private PaymentMethodConfigService paymentMethodConfigService;

    /**
     * This repository is mocked in the com.luulsolutions.luulpos.repository.search test package.
     *
     * @see com.luulsolutions.luulpos.repository.search.PaymentMethodConfigSearchRepositoryMockConfiguration
     */
    @Autowired
    private PaymentMethodConfigSearchRepository mockPaymentMethodConfigSearchRepository;

    @Autowired
    private PaymentMethodConfigQueryService paymentMethodConfigQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPaymentMethodConfigMockMvc;

    private PaymentMethodConfig paymentMethodConfig;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PaymentMethodConfigResource paymentMethodConfigResource = new PaymentMethodConfigResource(paymentMethodConfigService, paymentMethodConfigQueryService);
        this.restPaymentMethodConfigMockMvc = MockMvcBuilders.standaloneSetup(paymentMethodConfigResource)
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
    public static PaymentMethodConfig createEntity(EntityManager em) {
        PaymentMethodConfig paymentMethodConfig = new PaymentMethodConfig()
            .key(DEFAULT_KEY)
            .value(DEFAULT_VALUE)
            .note(DEFAULT_NOTE)
            .enabled(DEFAULT_ENABLED);
        return paymentMethodConfig;
    }

    @Before
    public void initTest() {
        paymentMethodConfig = createEntity(em);
    }

    @Test
    @Transactional
    public void createPaymentMethodConfig() throws Exception {
        int databaseSizeBeforeCreate = paymentMethodConfigRepository.findAll().size();

        // Create the PaymentMethodConfig
        PaymentMethodConfigDTO paymentMethodConfigDTO = paymentMethodConfigMapper.toDto(paymentMethodConfig);
        restPaymentMethodConfigMockMvc.perform(post("/api/payment-method-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentMethodConfigDTO)))
            .andExpect(status().isCreated());

        // Validate the PaymentMethodConfig in the database
        List<PaymentMethodConfig> paymentMethodConfigList = paymentMethodConfigRepository.findAll();
        assertThat(paymentMethodConfigList).hasSize(databaseSizeBeforeCreate + 1);
        PaymentMethodConfig testPaymentMethodConfig = paymentMethodConfigList.get(paymentMethodConfigList.size() - 1);
        assertThat(testPaymentMethodConfig.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testPaymentMethodConfig.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testPaymentMethodConfig.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testPaymentMethodConfig.isEnabled()).isEqualTo(DEFAULT_ENABLED);

        // Validate the PaymentMethodConfig in Elasticsearch
        verify(mockPaymentMethodConfigSearchRepository, times(1)).save(testPaymentMethodConfig);
    }

    @Test
    @Transactional
    public void createPaymentMethodConfigWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = paymentMethodConfigRepository.findAll().size();

        // Create the PaymentMethodConfig with an existing ID
        paymentMethodConfig.setId(1L);
        PaymentMethodConfigDTO paymentMethodConfigDTO = paymentMethodConfigMapper.toDto(paymentMethodConfig);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentMethodConfigMockMvc.perform(post("/api/payment-method-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentMethodConfigDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PaymentMethodConfig in the database
        List<PaymentMethodConfig> paymentMethodConfigList = paymentMethodConfigRepository.findAll();
        assertThat(paymentMethodConfigList).hasSize(databaseSizeBeforeCreate);

        // Validate the PaymentMethodConfig in Elasticsearch
        verify(mockPaymentMethodConfigSearchRepository, times(0)).save(paymentMethodConfig);
    }

    @Test
    @Transactional
    public void getAllPaymentMethodConfigs() throws Exception {
        // Initialize the database
        paymentMethodConfigRepository.saveAndFlush(paymentMethodConfig);

        // Get all the paymentMethodConfigList
        restPaymentMethodConfigMockMvc.perform(get("/api/payment-method-configs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentMethodConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.toString())))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getPaymentMethodConfig() throws Exception {
        // Initialize the database
        paymentMethodConfigRepository.saveAndFlush(paymentMethodConfig);

        // Get the paymentMethodConfig
        restPaymentMethodConfigMockMvc.perform(get("/api/payment-method-configs/{id}", paymentMethodConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(paymentMethodConfig.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE.toString()))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllPaymentMethodConfigsByKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentMethodConfigRepository.saveAndFlush(paymentMethodConfig);

        // Get all the paymentMethodConfigList where key equals to DEFAULT_KEY
        defaultPaymentMethodConfigShouldBeFound("key.equals=" + DEFAULT_KEY);

        // Get all the paymentMethodConfigList where key equals to UPDATED_KEY
        defaultPaymentMethodConfigShouldNotBeFound("key.equals=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    public void getAllPaymentMethodConfigsByKeyIsInShouldWork() throws Exception {
        // Initialize the database
        paymentMethodConfigRepository.saveAndFlush(paymentMethodConfig);

        // Get all the paymentMethodConfigList where key in DEFAULT_KEY or UPDATED_KEY
        defaultPaymentMethodConfigShouldBeFound("key.in=" + DEFAULT_KEY + "," + UPDATED_KEY);

        // Get all the paymentMethodConfigList where key equals to UPDATED_KEY
        defaultPaymentMethodConfigShouldNotBeFound("key.in=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    public void getAllPaymentMethodConfigsByKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentMethodConfigRepository.saveAndFlush(paymentMethodConfig);

        // Get all the paymentMethodConfigList where key is not null
        defaultPaymentMethodConfigShouldBeFound("key.specified=true");

        // Get all the paymentMethodConfigList where key is null
        defaultPaymentMethodConfigShouldNotBeFound("key.specified=false");
    }

    @Test
    @Transactional
    public void getAllPaymentMethodConfigsByValueIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentMethodConfigRepository.saveAndFlush(paymentMethodConfig);

        // Get all the paymentMethodConfigList where value equals to DEFAULT_VALUE
        defaultPaymentMethodConfigShouldBeFound("value.equals=" + DEFAULT_VALUE);

        // Get all the paymentMethodConfigList where value equals to UPDATED_VALUE
        defaultPaymentMethodConfigShouldNotBeFound("value.equals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllPaymentMethodConfigsByValueIsInShouldWork() throws Exception {
        // Initialize the database
        paymentMethodConfigRepository.saveAndFlush(paymentMethodConfig);

        // Get all the paymentMethodConfigList where value in DEFAULT_VALUE or UPDATED_VALUE
        defaultPaymentMethodConfigShouldBeFound("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE);

        // Get all the paymentMethodConfigList where value equals to UPDATED_VALUE
        defaultPaymentMethodConfigShouldNotBeFound("value.in=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllPaymentMethodConfigsByValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentMethodConfigRepository.saveAndFlush(paymentMethodConfig);

        // Get all the paymentMethodConfigList where value is not null
        defaultPaymentMethodConfigShouldBeFound("value.specified=true");

        // Get all the paymentMethodConfigList where value is null
        defaultPaymentMethodConfigShouldNotBeFound("value.specified=false");
    }

    @Test
    @Transactional
    public void getAllPaymentMethodConfigsByNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentMethodConfigRepository.saveAndFlush(paymentMethodConfig);

        // Get all the paymentMethodConfigList where note equals to DEFAULT_NOTE
        defaultPaymentMethodConfigShouldBeFound("note.equals=" + DEFAULT_NOTE);

        // Get all the paymentMethodConfigList where note equals to UPDATED_NOTE
        defaultPaymentMethodConfigShouldNotBeFound("note.equals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllPaymentMethodConfigsByNoteIsInShouldWork() throws Exception {
        // Initialize the database
        paymentMethodConfigRepository.saveAndFlush(paymentMethodConfig);

        // Get all the paymentMethodConfigList where note in DEFAULT_NOTE or UPDATED_NOTE
        defaultPaymentMethodConfigShouldBeFound("note.in=" + DEFAULT_NOTE + "," + UPDATED_NOTE);

        // Get all the paymentMethodConfigList where note equals to UPDATED_NOTE
        defaultPaymentMethodConfigShouldNotBeFound("note.in=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllPaymentMethodConfigsByNoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentMethodConfigRepository.saveAndFlush(paymentMethodConfig);

        // Get all the paymentMethodConfigList where note is not null
        defaultPaymentMethodConfigShouldBeFound("note.specified=true");

        // Get all the paymentMethodConfigList where note is null
        defaultPaymentMethodConfigShouldNotBeFound("note.specified=false");
    }

    @Test
    @Transactional
    public void getAllPaymentMethodConfigsByEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentMethodConfigRepository.saveAndFlush(paymentMethodConfig);

        // Get all the paymentMethodConfigList where enabled equals to DEFAULT_ENABLED
        defaultPaymentMethodConfigShouldBeFound("enabled.equals=" + DEFAULT_ENABLED);

        // Get all the paymentMethodConfigList where enabled equals to UPDATED_ENABLED
        defaultPaymentMethodConfigShouldNotBeFound("enabled.equals=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void getAllPaymentMethodConfigsByEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        paymentMethodConfigRepository.saveAndFlush(paymentMethodConfig);

        // Get all the paymentMethodConfigList where enabled in DEFAULT_ENABLED or UPDATED_ENABLED
        defaultPaymentMethodConfigShouldBeFound("enabled.in=" + DEFAULT_ENABLED + "," + UPDATED_ENABLED);

        // Get all the paymentMethodConfigList where enabled equals to UPDATED_ENABLED
        defaultPaymentMethodConfigShouldNotBeFound("enabled.in=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void getAllPaymentMethodConfigsByEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentMethodConfigRepository.saveAndFlush(paymentMethodConfig);

        // Get all the paymentMethodConfigList where enabled is not null
        defaultPaymentMethodConfigShouldBeFound("enabled.specified=true");

        // Get all the paymentMethodConfigList where enabled is null
        defaultPaymentMethodConfigShouldNotBeFound("enabled.specified=false");
    }

    @Test
    @Transactional
    public void getAllPaymentMethodConfigsByPaymentMethodIsEqualToSomething() throws Exception {
        // Initialize the database
        PaymentMethod paymentMethod = PaymentMethodResourceIntTest.createEntity(em);
        em.persist(paymentMethod);
        em.flush();
        paymentMethodConfig.setPaymentMethod(paymentMethod);
        paymentMethodConfigRepository.saveAndFlush(paymentMethodConfig);
        Long paymentMethodId = paymentMethod.getId();

        // Get all the paymentMethodConfigList where paymentMethod equals to paymentMethodId
        defaultPaymentMethodConfigShouldBeFound("paymentMethodId.equals=" + paymentMethodId);

        // Get all the paymentMethodConfigList where paymentMethod equals to paymentMethodId + 1
        defaultPaymentMethodConfigShouldNotBeFound("paymentMethodId.equals=" + (paymentMethodId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultPaymentMethodConfigShouldBeFound(String filter) throws Exception {
        restPaymentMethodConfigMockMvc.perform(get("/api/payment-method-configs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentMethodConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.toString())))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())));

        // Check, that the count call also returns 1
        restPaymentMethodConfigMockMvc.perform(get("/api/payment-method-configs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultPaymentMethodConfigShouldNotBeFound(String filter) throws Exception {
        restPaymentMethodConfigMockMvc.perform(get("/api/payment-method-configs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPaymentMethodConfigMockMvc.perform(get("/api/payment-method-configs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPaymentMethodConfig() throws Exception {
        // Get the paymentMethodConfig
        restPaymentMethodConfigMockMvc.perform(get("/api/payment-method-configs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePaymentMethodConfig() throws Exception {
        // Initialize the database
        paymentMethodConfigRepository.saveAndFlush(paymentMethodConfig);

        int databaseSizeBeforeUpdate = paymentMethodConfigRepository.findAll().size();

        // Update the paymentMethodConfig
        PaymentMethodConfig updatedPaymentMethodConfig = paymentMethodConfigRepository.findById(paymentMethodConfig.getId()).get();
        // Disconnect from session so that the updates on updatedPaymentMethodConfig are not directly saved in db
        em.detach(updatedPaymentMethodConfig);
        updatedPaymentMethodConfig
            .key(UPDATED_KEY)
            .value(UPDATED_VALUE)
            .note(UPDATED_NOTE)
            .enabled(UPDATED_ENABLED);
        PaymentMethodConfigDTO paymentMethodConfigDTO = paymentMethodConfigMapper.toDto(updatedPaymentMethodConfig);

        restPaymentMethodConfigMockMvc.perform(put("/api/payment-method-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentMethodConfigDTO)))
            .andExpect(status().isOk());

        // Validate the PaymentMethodConfig in the database
        List<PaymentMethodConfig> paymentMethodConfigList = paymentMethodConfigRepository.findAll();
        assertThat(paymentMethodConfigList).hasSize(databaseSizeBeforeUpdate);
        PaymentMethodConfig testPaymentMethodConfig = paymentMethodConfigList.get(paymentMethodConfigList.size() - 1);
        assertThat(testPaymentMethodConfig.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testPaymentMethodConfig.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testPaymentMethodConfig.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testPaymentMethodConfig.isEnabled()).isEqualTo(UPDATED_ENABLED);

        // Validate the PaymentMethodConfig in Elasticsearch
        verify(mockPaymentMethodConfigSearchRepository, times(1)).save(testPaymentMethodConfig);
    }

    @Test
    @Transactional
    public void updateNonExistingPaymentMethodConfig() throws Exception {
        int databaseSizeBeforeUpdate = paymentMethodConfigRepository.findAll().size();

        // Create the PaymentMethodConfig
        PaymentMethodConfigDTO paymentMethodConfigDTO = paymentMethodConfigMapper.toDto(paymentMethodConfig);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentMethodConfigMockMvc.perform(put("/api/payment-method-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentMethodConfigDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PaymentMethodConfig in the database
        List<PaymentMethodConfig> paymentMethodConfigList = paymentMethodConfigRepository.findAll();
        assertThat(paymentMethodConfigList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PaymentMethodConfig in Elasticsearch
        verify(mockPaymentMethodConfigSearchRepository, times(0)).save(paymentMethodConfig);
    }

    @Test
    @Transactional
    public void deletePaymentMethodConfig() throws Exception {
        // Initialize the database
        paymentMethodConfigRepository.saveAndFlush(paymentMethodConfig);

        int databaseSizeBeforeDelete = paymentMethodConfigRepository.findAll().size();

        // Get the paymentMethodConfig
        restPaymentMethodConfigMockMvc.perform(delete("/api/payment-method-configs/{id}", paymentMethodConfig.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PaymentMethodConfig> paymentMethodConfigList = paymentMethodConfigRepository.findAll();
        assertThat(paymentMethodConfigList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the PaymentMethodConfig in Elasticsearch
        verify(mockPaymentMethodConfigSearchRepository, times(1)).deleteById(paymentMethodConfig.getId());
    }

    @Test
    @Transactional
    public void searchPaymentMethodConfig() throws Exception {
        // Initialize the database
        paymentMethodConfigRepository.saveAndFlush(paymentMethodConfig);
        when(mockPaymentMethodConfigSearchRepository.search(queryStringQuery("id:" + paymentMethodConfig.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(paymentMethodConfig), PageRequest.of(0, 1), 1));
        // Search the paymentMethodConfig
        restPaymentMethodConfigMockMvc.perform(get("/api/_search/payment-method-configs?query=id:" + paymentMethodConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentMethodConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentMethodConfig.class);
        PaymentMethodConfig paymentMethodConfig1 = new PaymentMethodConfig();
        paymentMethodConfig1.setId(1L);
        PaymentMethodConfig paymentMethodConfig2 = new PaymentMethodConfig();
        paymentMethodConfig2.setId(paymentMethodConfig1.getId());
        assertThat(paymentMethodConfig1).isEqualTo(paymentMethodConfig2);
        paymentMethodConfig2.setId(2L);
        assertThat(paymentMethodConfig1).isNotEqualTo(paymentMethodConfig2);
        paymentMethodConfig1.setId(null);
        assertThat(paymentMethodConfig1).isNotEqualTo(paymentMethodConfig2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentMethodConfigDTO.class);
        PaymentMethodConfigDTO paymentMethodConfigDTO1 = new PaymentMethodConfigDTO();
        paymentMethodConfigDTO1.setId(1L);
        PaymentMethodConfigDTO paymentMethodConfigDTO2 = new PaymentMethodConfigDTO();
        assertThat(paymentMethodConfigDTO1).isNotEqualTo(paymentMethodConfigDTO2);
        paymentMethodConfigDTO2.setId(paymentMethodConfigDTO1.getId());
        assertThat(paymentMethodConfigDTO1).isEqualTo(paymentMethodConfigDTO2);
        paymentMethodConfigDTO2.setId(2L);
        assertThat(paymentMethodConfigDTO1).isNotEqualTo(paymentMethodConfigDTO2);
        paymentMethodConfigDTO1.setId(null);
        assertThat(paymentMethodConfigDTO1).isNotEqualTo(paymentMethodConfigDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(paymentMethodConfigMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(paymentMethodConfigMapper.fromId(null)).isNull();
    }
}
