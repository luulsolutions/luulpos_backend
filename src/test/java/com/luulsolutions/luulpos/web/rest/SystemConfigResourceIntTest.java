package com.luulsolutions.luulpos.web.rest;

import com.luulsolutions.luulpos.LuulposApp;

import com.luulsolutions.luulpos.domain.SystemConfig;
import com.luulsolutions.luulpos.domain.Shop;
import com.luulsolutions.luulpos.repository.SystemConfigRepository;
import com.luulsolutions.luulpos.repository.search.SystemConfigSearchRepository;
import com.luulsolutions.luulpos.service.SystemConfigService;
import com.luulsolutions.luulpos.service.dto.SystemConfigDTO;
import com.luulsolutions.luulpos.service.mapper.SystemConfigMapper;
import com.luulsolutions.luulpos.web.rest.errors.ExceptionTranslator;
import com.luulsolutions.luulpos.service.dto.SystemConfigCriteria;
import com.luulsolutions.luulpos.service.SystemConfigQueryService;

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

import com.luulsolutions.luulpos.domain.enumeration.ConfigType;
/**
 * Test class for the SystemConfigResource REST controller.
 *
 * @see SystemConfigResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LuulposApp.class)
public class SystemConfigResourceIntTest {

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final ConfigType DEFAULT_CONFIGURATION_TYPE = ConfigType.STRING;
    private static final ConfigType UPDATED_CONFIGURATION_TYPE = ConfigType.BOOLEAN;

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    @Autowired
    private SystemConfigRepository systemConfigRepository;

    @Autowired
    private SystemConfigMapper systemConfigMapper;

    @Autowired
    private SystemConfigService systemConfigService;

    /**
     * This repository is mocked in the com.luulsolutions.luulpos.repository.search test package.
     *
     * @see com.luulsolutions.luulpos.repository.search.SystemConfigSearchRepositoryMockConfiguration
     */
    @Autowired
    private SystemConfigSearchRepository mockSystemConfigSearchRepository;

    @Autowired
    private SystemConfigQueryService systemConfigQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSystemConfigMockMvc;

    private SystemConfig systemConfig;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SystemConfigResource systemConfigResource = new SystemConfigResource(systemConfigService, systemConfigQueryService);
        this.restSystemConfigMockMvc = MockMvcBuilders.standaloneSetup(systemConfigResource)
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
    public static SystemConfig createEntity(EntityManager em) {
        SystemConfig systemConfig = new SystemConfig()
            .key(DEFAULT_KEY)
            .value(DEFAULT_VALUE)
            .configurationType(DEFAULT_CONFIGURATION_TYPE)
            .note(DEFAULT_NOTE)
            .enabled(DEFAULT_ENABLED);
        return systemConfig;
    }

    @Before
    public void initTest() {
        systemConfig = createEntity(em);
    }

    @Test
    @Transactional
    public void createSystemConfig() throws Exception {
        int databaseSizeBeforeCreate = systemConfigRepository.findAll().size();

        // Create the SystemConfig
        SystemConfigDTO systemConfigDTO = systemConfigMapper.toDto(systemConfig);
        restSystemConfigMockMvc.perform(post("/api/system-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(systemConfigDTO)))
            .andExpect(status().isCreated());

        // Validate the SystemConfig in the database
        List<SystemConfig> systemConfigList = systemConfigRepository.findAll();
        assertThat(systemConfigList).hasSize(databaseSizeBeforeCreate + 1);
        SystemConfig testSystemConfig = systemConfigList.get(systemConfigList.size() - 1);
        assertThat(testSystemConfig.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testSystemConfig.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testSystemConfig.getConfigurationType()).isEqualTo(DEFAULT_CONFIGURATION_TYPE);
        assertThat(testSystemConfig.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testSystemConfig.isEnabled()).isEqualTo(DEFAULT_ENABLED);

        // Validate the SystemConfig in Elasticsearch
        verify(mockSystemConfigSearchRepository, times(1)).save(testSystemConfig);
    }

    @Test
    @Transactional
    public void createSystemConfigWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = systemConfigRepository.findAll().size();

        // Create the SystemConfig with an existing ID
        systemConfig.setId(1L);
        SystemConfigDTO systemConfigDTO = systemConfigMapper.toDto(systemConfig);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSystemConfigMockMvc.perform(post("/api/system-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(systemConfigDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SystemConfig in the database
        List<SystemConfig> systemConfigList = systemConfigRepository.findAll();
        assertThat(systemConfigList).hasSize(databaseSizeBeforeCreate);

        // Validate the SystemConfig in Elasticsearch
        verify(mockSystemConfigSearchRepository, times(0)).save(systemConfig);
    }

    @Test
    @Transactional
    public void getAllSystemConfigs() throws Exception {
        // Initialize the database
        systemConfigRepository.saveAndFlush(systemConfig);

        // Get all the systemConfigList
        restSystemConfigMockMvc.perform(get("/api/system-configs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(systemConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())))
            .andExpect(jsonPath("$.[*].configurationType").value(hasItem(DEFAULT_CONFIGURATION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.toString())))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getSystemConfig() throws Exception {
        // Initialize the database
        systemConfigRepository.saveAndFlush(systemConfig);

        // Get the systemConfig
        restSystemConfigMockMvc.perform(get("/api/system-configs/{id}", systemConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(systemConfig.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()))
            .andExpect(jsonPath("$.configurationType").value(DEFAULT_CONFIGURATION_TYPE.toString()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE.toString()))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllSystemConfigsByKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        systemConfigRepository.saveAndFlush(systemConfig);

        // Get all the systemConfigList where key equals to DEFAULT_KEY
        defaultSystemConfigShouldBeFound("key.equals=" + DEFAULT_KEY);

        // Get all the systemConfigList where key equals to UPDATED_KEY
        defaultSystemConfigShouldNotBeFound("key.equals=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    public void getAllSystemConfigsByKeyIsInShouldWork() throws Exception {
        // Initialize the database
        systemConfigRepository.saveAndFlush(systemConfig);

        // Get all the systemConfigList where key in DEFAULT_KEY or UPDATED_KEY
        defaultSystemConfigShouldBeFound("key.in=" + DEFAULT_KEY + "," + UPDATED_KEY);

        // Get all the systemConfigList where key equals to UPDATED_KEY
        defaultSystemConfigShouldNotBeFound("key.in=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    public void getAllSystemConfigsByKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        systemConfigRepository.saveAndFlush(systemConfig);

        // Get all the systemConfigList where key is not null
        defaultSystemConfigShouldBeFound("key.specified=true");

        // Get all the systemConfigList where key is null
        defaultSystemConfigShouldNotBeFound("key.specified=false");
    }

    @Test
    @Transactional
    public void getAllSystemConfigsByValueIsEqualToSomething() throws Exception {
        // Initialize the database
        systemConfigRepository.saveAndFlush(systemConfig);

        // Get all the systemConfigList where value equals to DEFAULT_VALUE
        defaultSystemConfigShouldBeFound("value.equals=" + DEFAULT_VALUE);

        // Get all the systemConfigList where value equals to UPDATED_VALUE
        defaultSystemConfigShouldNotBeFound("value.equals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllSystemConfigsByValueIsInShouldWork() throws Exception {
        // Initialize the database
        systemConfigRepository.saveAndFlush(systemConfig);

        // Get all the systemConfigList where value in DEFAULT_VALUE or UPDATED_VALUE
        defaultSystemConfigShouldBeFound("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE);

        // Get all the systemConfigList where value equals to UPDATED_VALUE
        defaultSystemConfigShouldNotBeFound("value.in=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllSystemConfigsByValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        systemConfigRepository.saveAndFlush(systemConfig);

        // Get all the systemConfigList where value is not null
        defaultSystemConfigShouldBeFound("value.specified=true");

        // Get all the systemConfigList where value is null
        defaultSystemConfigShouldNotBeFound("value.specified=false");
    }

    @Test
    @Transactional
    public void getAllSystemConfigsByConfigurationTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        systemConfigRepository.saveAndFlush(systemConfig);

        // Get all the systemConfigList where configurationType equals to DEFAULT_CONFIGURATION_TYPE
        defaultSystemConfigShouldBeFound("configurationType.equals=" + DEFAULT_CONFIGURATION_TYPE);

        // Get all the systemConfigList where configurationType equals to UPDATED_CONFIGURATION_TYPE
        defaultSystemConfigShouldNotBeFound("configurationType.equals=" + UPDATED_CONFIGURATION_TYPE);
    }

    @Test
    @Transactional
    public void getAllSystemConfigsByConfigurationTypeIsInShouldWork() throws Exception {
        // Initialize the database
        systemConfigRepository.saveAndFlush(systemConfig);

        // Get all the systemConfigList where configurationType in DEFAULT_CONFIGURATION_TYPE or UPDATED_CONFIGURATION_TYPE
        defaultSystemConfigShouldBeFound("configurationType.in=" + DEFAULT_CONFIGURATION_TYPE + "," + UPDATED_CONFIGURATION_TYPE);

        // Get all the systemConfigList where configurationType equals to UPDATED_CONFIGURATION_TYPE
        defaultSystemConfigShouldNotBeFound("configurationType.in=" + UPDATED_CONFIGURATION_TYPE);
    }

    @Test
    @Transactional
    public void getAllSystemConfigsByConfigurationTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        systemConfigRepository.saveAndFlush(systemConfig);

        // Get all the systemConfigList where configurationType is not null
        defaultSystemConfigShouldBeFound("configurationType.specified=true");

        // Get all the systemConfigList where configurationType is null
        defaultSystemConfigShouldNotBeFound("configurationType.specified=false");
    }

    @Test
    @Transactional
    public void getAllSystemConfigsByNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        systemConfigRepository.saveAndFlush(systemConfig);

        // Get all the systemConfigList where note equals to DEFAULT_NOTE
        defaultSystemConfigShouldBeFound("note.equals=" + DEFAULT_NOTE);

        // Get all the systemConfigList where note equals to UPDATED_NOTE
        defaultSystemConfigShouldNotBeFound("note.equals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllSystemConfigsByNoteIsInShouldWork() throws Exception {
        // Initialize the database
        systemConfigRepository.saveAndFlush(systemConfig);

        // Get all the systemConfigList where note in DEFAULT_NOTE or UPDATED_NOTE
        defaultSystemConfigShouldBeFound("note.in=" + DEFAULT_NOTE + "," + UPDATED_NOTE);

        // Get all the systemConfigList where note equals to UPDATED_NOTE
        defaultSystemConfigShouldNotBeFound("note.in=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllSystemConfigsByNoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        systemConfigRepository.saveAndFlush(systemConfig);

        // Get all the systemConfigList where note is not null
        defaultSystemConfigShouldBeFound("note.specified=true");

        // Get all the systemConfigList where note is null
        defaultSystemConfigShouldNotBeFound("note.specified=false");
    }

    @Test
    @Transactional
    public void getAllSystemConfigsByEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        systemConfigRepository.saveAndFlush(systemConfig);

        // Get all the systemConfigList where enabled equals to DEFAULT_ENABLED
        defaultSystemConfigShouldBeFound("enabled.equals=" + DEFAULT_ENABLED);

        // Get all the systemConfigList where enabled equals to UPDATED_ENABLED
        defaultSystemConfigShouldNotBeFound("enabled.equals=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void getAllSystemConfigsByEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        systemConfigRepository.saveAndFlush(systemConfig);

        // Get all the systemConfigList where enabled in DEFAULT_ENABLED or UPDATED_ENABLED
        defaultSystemConfigShouldBeFound("enabled.in=" + DEFAULT_ENABLED + "," + UPDATED_ENABLED);

        // Get all the systemConfigList where enabled equals to UPDATED_ENABLED
        defaultSystemConfigShouldNotBeFound("enabled.in=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void getAllSystemConfigsByEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        systemConfigRepository.saveAndFlush(systemConfig);

        // Get all the systemConfigList where enabled is not null
        defaultSystemConfigShouldBeFound("enabled.specified=true");

        // Get all the systemConfigList where enabled is null
        defaultSystemConfigShouldNotBeFound("enabled.specified=false");
    }

    @Test
    @Transactional
    public void getAllSystemConfigsByShopIsEqualToSomething() throws Exception {
        // Initialize the database
        Shop shop = ShopResourceIntTest.createEntity(em);
        em.persist(shop);
        em.flush();
        systemConfig.setShop(shop);
        systemConfigRepository.saveAndFlush(systemConfig);
        Long shopId = shop.getId();

        // Get all the systemConfigList where shop equals to shopId
        defaultSystemConfigShouldBeFound("shopId.equals=" + shopId);

        // Get all the systemConfigList where shop equals to shopId + 1
        defaultSystemConfigShouldNotBeFound("shopId.equals=" + (shopId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultSystemConfigShouldBeFound(String filter) throws Exception {
        restSystemConfigMockMvc.perform(get("/api/system-configs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(systemConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())))
            .andExpect(jsonPath("$.[*].configurationType").value(hasItem(DEFAULT_CONFIGURATION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.toString())))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())));

        // Check, that the count call also returns 1
        restSystemConfigMockMvc.perform(get("/api/system-configs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultSystemConfigShouldNotBeFound(String filter) throws Exception {
        restSystemConfigMockMvc.perform(get("/api/system-configs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSystemConfigMockMvc.perform(get("/api/system-configs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingSystemConfig() throws Exception {
        // Get the systemConfig
        restSystemConfigMockMvc.perform(get("/api/system-configs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSystemConfig() throws Exception {
        // Initialize the database
        systemConfigRepository.saveAndFlush(systemConfig);

        int databaseSizeBeforeUpdate = systemConfigRepository.findAll().size();

        // Update the systemConfig
        SystemConfig updatedSystemConfig = systemConfigRepository.findById(systemConfig.getId()).get();
        // Disconnect from session so that the updates on updatedSystemConfig are not directly saved in db
        em.detach(updatedSystemConfig);
        updatedSystemConfig
            .key(UPDATED_KEY)
            .value(UPDATED_VALUE)
            .configurationType(UPDATED_CONFIGURATION_TYPE)
            .note(UPDATED_NOTE)
            .enabled(UPDATED_ENABLED);
        SystemConfigDTO systemConfigDTO = systemConfigMapper.toDto(updatedSystemConfig);

        restSystemConfigMockMvc.perform(put("/api/system-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(systemConfigDTO)))
            .andExpect(status().isOk());

        // Validate the SystemConfig in the database
        List<SystemConfig> systemConfigList = systemConfigRepository.findAll();
        assertThat(systemConfigList).hasSize(databaseSizeBeforeUpdate);
        SystemConfig testSystemConfig = systemConfigList.get(systemConfigList.size() - 1);
        assertThat(testSystemConfig.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testSystemConfig.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testSystemConfig.getConfigurationType()).isEqualTo(UPDATED_CONFIGURATION_TYPE);
        assertThat(testSystemConfig.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testSystemConfig.isEnabled()).isEqualTo(UPDATED_ENABLED);

        // Validate the SystemConfig in Elasticsearch
        verify(mockSystemConfigSearchRepository, times(1)).save(testSystemConfig);
    }

    @Test
    @Transactional
    public void updateNonExistingSystemConfig() throws Exception {
        int databaseSizeBeforeUpdate = systemConfigRepository.findAll().size();

        // Create the SystemConfig
        SystemConfigDTO systemConfigDTO = systemConfigMapper.toDto(systemConfig);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSystemConfigMockMvc.perform(put("/api/system-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(systemConfigDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SystemConfig in the database
        List<SystemConfig> systemConfigList = systemConfigRepository.findAll();
        assertThat(systemConfigList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SystemConfig in Elasticsearch
        verify(mockSystemConfigSearchRepository, times(0)).save(systemConfig);
    }

    @Test
    @Transactional
    public void deleteSystemConfig() throws Exception {
        // Initialize the database
        systemConfigRepository.saveAndFlush(systemConfig);

        int databaseSizeBeforeDelete = systemConfigRepository.findAll().size();

        // Get the systemConfig
        restSystemConfigMockMvc.perform(delete("/api/system-configs/{id}", systemConfig.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SystemConfig> systemConfigList = systemConfigRepository.findAll();
        assertThat(systemConfigList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the SystemConfig in Elasticsearch
        verify(mockSystemConfigSearchRepository, times(1)).deleteById(systemConfig.getId());
    }

    @Test
    @Transactional
    public void searchSystemConfig() throws Exception {
        // Initialize the database
        systemConfigRepository.saveAndFlush(systemConfig);
        when(mockSystemConfigSearchRepository.search(queryStringQuery("id:" + systemConfig.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(systemConfig), PageRequest.of(0, 1), 1));
        // Search the systemConfig
        restSystemConfigMockMvc.perform(get("/api/_search/system-configs?query=id:" + systemConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(systemConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].configurationType").value(hasItem(DEFAULT_CONFIGURATION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SystemConfig.class);
        SystemConfig systemConfig1 = new SystemConfig();
        systemConfig1.setId(1L);
        SystemConfig systemConfig2 = new SystemConfig();
        systemConfig2.setId(systemConfig1.getId());
        assertThat(systemConfig1).isEqualTo(systemConfig2);
        systemConfig2.setId(2L);
        assertThat(systemConfig1).isNotEqualTo(systemConfig2);
        systemConfig1.setId(null);
        assertThat(systemConfig1).isNotEqualTo(systemConfig2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SystemConfigDTO.class);
        SystemConfigDTO systemConfigDTO1 = new SystemConfigDTO();
        systemConfigDTO1.setId(1L);
        SystemConfigDTO systemConfigDTO2 = new SystemConfigDTO();
        assertThat(systemConfigDTO1).isNotEqualTo(systemConfigDTO2);
        systemConfigDTO2.setId(systemConfigDTO1.getId());
        assertThat(systemConfigDTO1).isEqualTo(systemConfigDTO2);
        systemConfigDTO2.setId(2L);
        assertThat(systemConfigDTO1).isNotEqualTo(systemConfigDTO2);
        systemConfigDTO1.setId(null);
        assertThat(systemConfigDTO1).isNotEqualTo(systemConfigDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(systemConfigMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(systemConfigMapper.fromId(null)).isNull();
    }
}
