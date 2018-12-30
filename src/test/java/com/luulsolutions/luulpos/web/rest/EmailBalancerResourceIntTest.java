package com.luulsolutions.luulpos.web.rest;

import com.luulsolutions.luulpos.LuulposApp;

import com.luulsolutions.luulpos.domain.EmailBalancer;
import com.luulsolutions.luulpos.repository.EmailBalancerRepository;
import com.luulsolutions.luulpos.repository.search.EmailBalancerSearchRepository;
import com.luulsolutions.luulpos.service.EmailBalancerService;
import com.luulsolutions.luulpos.service.dto.EmailBalancerDTO;
import com.luulsolutions.luulpos.service.mapper.EmailBalancerMapper;
import com.luulsolutions.luulpos.web.rest.errors.ExceptionTranslator;
import com.luulsolutions.luulpos.service.dto.EmailBalancerCriteria;
import com.luulsolutions.luulpos.service.EmailBalancerQueryService;

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
 * Test class for the EmailBalancerResource REST controller.
 *
 * @see EmailBalancerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LuulposApp.class)
public class EmailBalancerResourceIntTest {

    private static final String DEFAULT_RELAY_ID = "AAAAAAAAAA";
    private static final String UPDATED_RELAY_ID = "BBBBBBBBBB";

    private static final String DEFAULT_RELAY_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_RELAY_PASSWORD = "BBBBBBBBBB";

    private static final Integer DEFAULT_STARTING_COUNT = 1;
    private static final Integer UPDATED_STARTING_COUNT = 2;

    private static final Integer DEFAULT_ENDING_COUNT = 1;
    private static final Integer UPDATED_ENDING_COUNT = 2;

    private static final String DEFAULT_PROVIDER = "AAAAAAAAAA";
    private static final String UPDATED_PROVIDER = "BBBBBBBBBB";

    private static final Integer DEFAULT_RELAY_PORT = 1;
    private static final Integer UPDATED_RELAY_PORT = 2;

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    @Autowired
    private EmailBalancerRepository emailBalancerRepository;

    @Autowired
    private EmailBalancerMapper emailBalancerMapper;

    @Autowired
    private EmailBalancerService emailBalancerService;

    /**
     * This repository is mocked in the com.luulsolutions.luulpos.repository.search test package.
     *
     * @see com.luulsolutions.luulpos.repository.search.EmailBalancerSearchRepositoryMockConfiguration
     */
    @Autowired
    private EmailBalancerSearchRepository mockEmailBalancerSearchRepository;

    @Autowired
    private EmailBalancerQueryService emailBalancerQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEmailBalancerMockMvc;

    private EmailBalancer emailBalancer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EmailBalancerResource emailBalancerResource = new EmailBalancerResource(emailBalancerService, emailBalancerQueryService);
        this.restEmailBalancerMockMvc = MockMvcBuilders.standaloneSetup(emailBalancerResource)
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
    public static EmailBalancer createEntity(EntityManager em) {
        EmailBalancer emailBalancer = new EmailBalancer()
            .relayId(DEFAULT_RELAY_ID)
            .relayPassword(DEFAULT_RELAY_PASSWORD)
            .startingCount(DEFAULT_STARTING_COUNT)
            .endingCount(DEFAULT_ENDING_COUNT)
            .provider(DEFAULT_PROVIDER)
            .relayPort(DEFAULT_RELAY_PORT)
            .enabled(DEFAULT_ENABLED);
        return emailBalancer;
    }

    @Before
    public void initTest() {
        emailBalancer = createEntity(em);
    }

    @Test
    @Transactional
    public void createEmailBalancer() throws Exception {
        int databaseSizeBeforeCreate = emailBalancerRepository.findAll().size();

        // Create the EmailBalancer
        EmailBalancerDTO emailBalancerDTO = emailBalancerMapper.toDto(emailBalancer);
        restEmailBalancerMockMvc.perform(post("/api/email-balancers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(emailBalancerDTO)))
            .andExpect(status().isCreated());

        // Validate the EmailBalancer in the database
        List<EmailBalancer> emailBalancerList = emailBalancerRepository.findAll();
        assertThat(emailBalancerList).hasSize(databaseSizeBeforeCreate + 1);
        EmailBalancer testEmailBalancer = emailBalancerList.get(emailBalancerList.size() - 1);
        assertThat(testEmailBalancer.getRelayId()).isEqualTo(DEFAULT_RELAY_ID);
        assertThat(testEmailBalancer.getRelayPassword()).isEqualTo(DEFAULT_RELAY_PASSWORD);
        assertThat(testEmailBalancer.getStartingCount()).isEqualTo(DEFAULT_STARTING_COUNT);
        assertThat(testEmailBalancer.getEndingCount()).isEqualTo(DEFAULT_ENDING_COUNT);
        assertThat(testEmailBalancer.getProvider()).isEqualTo(DEFAULT_PROVIDER);
        assertThat(testEmailBalancer.getRelayPort()).isEqualTo(DEFAULT_RELAY_PORT);
        assertThat(testEmailBalancer.isEnabled()).isEqualTo(DEFAULT_ENABLED);

        // Validate the EmailBalancer in Elasticsearch
        verify(mockEmailBalancerSearchRepository, times(1)).save(testEmailBalancer);
    }

    @Test
    @Transactional
    public void createEmailBalancerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = emailBalancerRepository.findAll().size();

        // Create the EmailBalancer with an existing ID
        emailBalancer.setId(1L);
        EmailBalancerDTO emailBalancerDTO = emailBalancerMapper.toDto(emailBalancer);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmailBalancerMockMvc.perform(post("/api/email-balancers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(emailBalancerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EmailBalancer in the database
        List<EmailBalancer> emailBalancerList = emailBalancerRepository.findAll();
        assertThat(emailBalancerList).hasSize(databaseSizeBeforeCreate);

        // Validate the EmailBalancer in Elasticsearch
        verify(mockEmailBalancerSearchRepository, times(0)).save(emailBalancer);
    }

    @Test
    @Transactional
    public void getAllEmailBalancers() throws Exception {
        // Initialize the database
        emailBalancerRepository.saveAndFlush(emailBalancer);

        // Get all the emailBalancerList
        restEmailBalancerMockMvc.perform(get("/api/email-balancers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(emailBalancer.getId().intValue())))
            .andExpect(jsonPath("$.[*].relayId").value(hasItem(DEFAULT_RELAY_ID.toString())))
            .andExpect(jsonPath("$.[*].relayPassword").value(hasItem(DEFAULT_RELAY_PASSWORD.toString())))
            .andExpect(jsonPath("$.[*].startingCount").value(hasItem(DEFAULT_STARTING_COUNT)))
            .andExpect(jsonPath("$.[*].endingCount").value(hasItem(DEFAULT_ENDING_COUNT)))
            .andExpect(jsonPath("$.[*].provider").value(hasItem(DEFAULT_PROVIDER.toString())))
            .andExpect(jsonPath("$.[*].relayPort").value(hasItem(DEFAULT_RELAY_PORT)))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getEmailBalancer() throws Exception {
        // Initialize the database
        emailBalancerRepository.saveAndFlush(emailBalancer);

        // Get the emailBalancer
        restEmailBalancerMockMvc.perform(get("/api/email-balancers/{id}", emailBalancer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(emailBalancer.getId().intValue()))
            .andExpect(jsonPath("$.relayId").value(DEFAULT_RELAY_ID.toString()))
            .andExpect(jsonPath("$.relayPassword").value(DEFAULT_RELAY_PASSWORD.toString()))
            .andExpect(jsonPath("$.startingCount").value(DEFAULT_STARTING_COUNT))
            .andExpect(jsonPath("$.endingCount").value(DEFAULT_ENDING_COUNT))
            .andExpect(jsonPath("$.provider").value(DEFAULT_PROVIDER.toString()))
            .andExpect(jsonPath("$.relayPort").value(DEFAULT_RELAY_PORT))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllEmailBalancersByRelayIdIsEqualToSomething() throws Exception {
        // Initialize the database
        emailBalancerRepository.saveAndFlush(emailBalancer);

        // Get all the emailBalancerList where relayId equals to DEFAULT_RELAY_ID
        defaultEmailBalancerShouldBeFound("relayId.equals=" + DEFAULT_RELAY_ID);

        // Get all the emailBalancerList where relayId equals to UPDATED_RELAY_ID
        defaultEmailBalancerShouldNotBeFound("relayId.equals=" + UPDATED_RELAY_ID);
    }

    @Test
    @Transactional
    public void getAllEmailBalancersByRelayIdIsInShouldWork() throws Exception {
        // Initialize the database
        emailBalancerRepository.saveAndFlush(emailBalancer);

        // Get all the emailBalancerList where relayId in DEFAULT_RELAY_ID or UPDATED_RELAY_ID
        defaultEmailBalancerShouldBeFound("relayId.in=" + DEFAULT_RELAY_ID + "," + UPDATED_RELAY_ID);

        // Get all the emailBalancerList where relayId equals to UPDATED_RELAY_ID
        defaultEmailBalancerShouldNotBeFound("relayId.in=" + UPDATED_RELAY_ID);
    }

    @Test
    @Transactional
    public void getAllEmailBalancersByRelayIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        emailBalancerRepository.saveAndFlush(emailBalancer);

        // Get all the emailBalancerList where relayId is not null
        defaultEmailBalancerShouldBeFound("relayId.specified=true");

        // Get all the emailBalancerList where relayId is null
        defaultEmailBalancerShouldNotBeFound("relayId.specified=false");
    }

    @Test
    @Transactional
    public void getAllEmailBalancersByRelayPasswordIsEqualToSomething() throws Exception {
        // Initialize the database
        emailBalancerRepository.saveAndFlush(emailBalancer);

        // Get all the emailBalancerList where relayPassword equals to DEFAULT_RELAY_PASSWORD
        defaultEmailBalancerShouldBeFound("relayPassword.equals=" + DEFAULT_RELAY_PASSWORD);

        // Get all the emailBalancerList where relayPassword equals to UPDATED_RELAY_PASSWORD
        defaultEmailBalancerShouldNotBeFound("relayPassword.equals=" + UPDATED_RELAY_PASSWORD);
    }

    @Test
    @Transactional
    public void getAllEmailBalancersByRelayPasswordIsInShouldWork() throws Exception {
        // Initialize the database
        emailBalancerRepository.saveAndFlush(emailBalancer);

        // Get all the emailBalancerList where relayPassword in DEFAULT_RELAY_PASSWORD or UPDATED_RELAY_PASSWORD
        defaultEmailBalancerShouldBeFound("relayPassword.in=" + DEFAULT_RELAY_PASSWORD + "," + UPDATED_RELAY_PASSWORD);

        // Get all the emailBalancerList where relayPassword equals to UPDATED_RELAY_PASSWORD
        defaultEmailBalancerShouldNotBeFound("relayPassword.in=" + UPDATED_RELAY_PASSWORD);
    }

    @Test
    @Transactional
    public void getAllEmailBalancersByRelayPasswordIsNullOrNotNull() throws Exception {
        // Initialize the database
        emailBalancerRepository.saveAndFlush(emailBalancer);

        // Get all the emailBalancerList where relayPassword is not null
        defaultEmailBalancerShouldBeFound("relayPassword.specified=true");

        // Get all the emailBalancerList where relayPassword is null
        defaultEmailBalancerShouldNotBeFound("relayPassword.specified=false");
    }

    @Test
    @Transactional
    public void getAllEmailBalancersByStartingCountIsEqualToSomething() throws Exception {
        // Initialize the database
        emailBalancerRepository.saveAndFlush(emailBalancer);

        // Get all the emailBalancerList where startingCount equals to DEFAULT_STARTING_COUNT
        defaultEmailBalancerShouldBeFound("startingCount.equals=" + DEFAULT_STARTING_COUNT);

        // Get all the emailBalancerList where startingCount equals to UPDATED_STARTING_COUNT
        defaultEmailBalancerShouldNotBeFound("startingCount.equals=" + UPDATED_STARTING_COUNT);
    }

    @Test
    @Transactional
    public void getAllEmailBalancersByStartingCountIsInShouldWork() throws Exception {
        // Initialize the database
        emailBalancerRepository.saveAndFlush(emailBalancer);

        // Get all the emailBalancerList where startingCount in DEFAULT_STARTING_COUNT or UPDATED_STARTING_COUNT
        defaultEmailBalancerShouldBeFound("startingCount.in=" + DEFAULT_STARTING_COUNT + "," + UPDATED_STARTING_COUNT);

        // Get all the emailBalancerList where startingCount equals to UPDATED_STARTING_COUNT
        defaultEmailBalancerShouldNotBeFound("startingCount.in=" + UPDATED_STARTING_COUNT);
    }

    @Test
    @Transactional
    public void getAllEmailBalancersByStartingCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        emailBalancerRepository.saveAndFlush(emailBalancer);

        // Get all the emailBalancerList where startingCount is not null
        defaultEmailBalancerShouldBeFound("startingCount.specified=true");

        // Get all the emailBalancerList where startingCount is null
        defaultEmailBalancerShouldNotBeFound("startingCount.specified=false");
    }

    @Test
    @Transactional
    public void getAllEmailBalancersByStartingCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        emailBalancerRepository.saveAndFlush(emailBalancer);

        // Get all the emailBalancerList where startingCount greater than or equals to DEFAULT_STARTING_COUNT
        defaultEmailBalancerShouldBeFound("startingCount.greaterOrEqualThan=" + DEFAULT_STARTING_COUNT);

        // Get all the emailBalancerList where startingCount greater than or equals to UPDATED_STARTING_COUNT
        defaultEmailBalancerShouldNotBeFound("startingCount.greaterOrEqualThan=" + UPDATED_STARTING_COUNT);
    }

    @Test
    @Transactional
    public void getAllEmailBalancersByStartingCountIsLessThanSomething() throws Exception {
        // Initialize the database
        emailBalancerRepository.saveAndFlush(emailBalancer);

        // Get all the emailBalancerList where startingCount less than or equals to DEFAULT_STARTING_COUNT
        defaultEmailBalancerShouldNotBeFound("startingCount.lessThan=" + DEFAULT_STARTING_COUNT);

        // Get all the emailBalancerList where startingCount less than or equals to UPDATED_STARTING_COUNT
        defaultEmailBalancerShouldBeFound("startingCount.lessThan=" + UPDATED_STARTING_COUNT);
    }


    @Test
    @Transactional
    public void getAllEmailBalancersByEndingCountIsEqualToSomething() throws Exception {
        // Initialize the database
        emailBalancerRepository.saveAndFlush(emailBalancer);

        // Get all the emailBalancerList where endingCount equals to DEFAULT_ENDING_COUNT
        defaultEmailBalancerShouldBeFound("endingCount.equals=" + DEFAULT_ENDING_COUNT);

        // Get all the emailBalancerList where endingCount equals to UPDATED_ENDING_COUNT
        defaultEmailBalancerShouldNotBeFound("endingCount.equals=" + UPDATED_ENDING_COUNT);
    }

    @Test
    @Transactional
    public void getAllEmailBalancersByEndingCountIsInShouldWork() throws Exception {
        // Initialize the database
        emailBalancerRepository.saveAndFlush(emailBalancer);

        // Get all the emailBalancerList where endingCount in DEFAULT_ENDING_COUNT or UPDATED_ENDING_COUNT
        defaultEmailBalancerShouldBeFound("endingCount.in=" + DEFAULT_ENDING_COUNT + "," + UPDATED_ENDING_COUNT);

        // Get all the emailBalancerList where endingCount equals to UPDATED_ENDING_COUNT
        defaultEmailBalancerShouldNotBeFound("endingCount.in=" + UPDATED_ENDING_COUNT);
    }

    @Test
    @Transactional
    public void getAllEmailBalancersByEndingCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        emailBalancerRepository.saveAndFlush(emailBalancer);

        // Get all the emailBalancerList where endingCount is not null
        defaultEmailBalancerShouldBeFound("endingCount.specified=true");

        // Get all the emailBalancerList where endingCount is null
        defaultEmailBalancerShouldNotBeFound("endingCount.specified=false");
    }

    @Test
    @Transactional
    public void getAllEmailBalancersByEndingCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        emailBalancerRepository.saveAndFlush(emailBalancer);

        // Get all the emailBalancerList where endingCount greater than or equals to DEFAULT_ENDING_COUNT
        defaultEmailBalancerShouldBeFound("endingCount.greaterOrEqualThan=" + DEFAULT_ENDING_COUNT);

        // Get all the emailBalancerList where endingCount greater than or equals to UPDATED_ENDING_COUNT
        defaultEmailBalancerShouldNotBeFound("endingCount.greaterOrEqualThan=" + UPDATED_ENDING_COUNT);
    }

    @Test
    @Transactional
    public void getAllEmailBalancersByEndingCountIsLessThanSomething() throws Exception {
        // Initialize the database
        emailBalancerRepository.saveAndFlush(emailBalancer);

        // Get all the emailBalancerList where endingCount less than or equals to DEFAULT_ENDING_COUNT
        defaultEmailBalancerShouldNotBeFound("endingCount.lessThan=" + DEFAULT_ENDING_COUNT);

        // Get all the emailBalancerList where endingCount less than or equals to UPDATED_ENDING_COUNT
        defaultEmailBalancerShouldBeFound("endingCount.lessThan=" + UPDATED_ENDING_COUNT);
    }


    @Test
    @Transactional
    public void getAllEmailBalancersByProviderIsEqualToSomething() throws Exception {
        // Initialize the database
        emailBalancerRepository.saveAndFlush(emailBalancer);

        // Get all the emailBalancerList where provider equals to DEFAULT_PROVIDER
        defaultEmailBalancerShouldBeFound("provider.equals=" + DEFAULT_PROVIDER);

        // Get all the emailBalancerList where provider equals to UPDATED_PROVIDER
        defaultEmailBalancerShouldNotBeFound("provider.equals=" + UPDATED_PROVIDER);
    }

    @Test
    @Transactional
    public void getAllEmailBalancersByProviderIsInShouldWork() throws Exception {
        // Initialize the database
        emailBalancerRepository.saveAndFlush(emailBalancer);

        // Get all the emailBalancerList where provider in DEFAULT_PROVIDER or UPDATED_PROVIDER
        defaultEmailBalancerShouldBeFound("provider.in=" + DEFAULT_PROVIDER + "," + UPDATED_PROVIDER);

        // Get all the emailBalancerList where provider equals to UPDATED_PROVIDER
        defaultEmailBalancerShouldNotBeFound("provider.in=" + UPDATED_PROVIDER);
    }

    @Test
    @Transactional
    public void getAllEmailBalancersByProviderIsNullOrNotNull() throws Exception {
        // Initialize the database
        emailBalancerRepository.saveAndFlush(emailBalancer);

        // Get all the emailBalancerList where provider is not null
        defaultEmailBalancerShouldBeFound("provider.specified=true");

        // Get all the emailBalancerList where provider is null
        defaultEmailBalancerShouldNotBeFound("provider.specified=false");
    }

    @Test
    @Transactional
    public void getAllEmailBalancersByRelayPortIsEqualToSomething() throws Exception {
        // Initialize the database
        emailBalancerRepository.saveAndFlush(emailBalancer);

        // Get all the emailBalancerList where relayPort equals to DEFAULT_RELAY_PORT
        defaultEmailBalancerShouldBeFound("relayPort.equals=" + DEFAULT_RELAY_PORT);

        // Get all the emailBalancerList where relayPort equals to UPDATED_RELAY_PORT
        defaultEmailBalancerShouldNotBeFound("relayPort.equals=" + UPDATED_RELAY_PORT);
    }

    @Test
    @Transactional
    public void getAllEmailBalancersByRelayPortIsInShouldWork() throws Exception {
        // Initialize the database
        emailBalancerRepository.saveAndFlush(emailBalancer);

        // Get all the emailBalancerList where relayPort in DEFAULT_RELAY_PORT or UPDATED_RELAY_PORT
        defaultEmailBalancerShouldBeFound("relayPort.in=" + DEFAULT_RELAY_PORT + "," + UPDATED_RELAY_PORT);

        // Get all the emailBalancerList where relayPort equals to UPDATED_RELAY_PORT
        defaultEmailBalancerShouldNotBeFound("relayPort.in=" + UPDATED_RELAY_PORT);
    }

    @Test
    @Transactional
    public void getAllEmailBalancersByRelayPortIsNullOrNotNull() throws Exception {
        // Initialize the database
        emailBalancerRepository.saveAndFlush(emailBalancer);

        // Get all the emailBalancerList where relayPort is not null
        defaultEmailBalancerShouldBeFound("relayPort.specified=true");

        // Get all the emailBalancerList where relayPort is null
        defaultEmailBalancerShouldNotBeFound("relayPort.specified=false");
    }

    @Test
    @Transactional
    public void getAllEmailBalancersByRelayPortIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        emailBalancerRepository.saveAndFlush(emailBalancer);

        // Get all the emailBalancerList where relayPort greater than or equals to DEFAULT_RELAY_PORT
        defaultEmailBalancerShouldBeFound("relayPort.greaterOrEqualThan=" + DEFAULT_RELAY_PORT);

        // Get all the emailBalancerList where relayPort greater than or equals to UPDATED_RELAY_PORT
        defaultEmailBalancerShouldNotBeFound("relayPort.greaterOrEqualThan=" + UPDATED_RELAY_PORT);
    }

    @Test
    @Transactional
    public void getAllEmailBalancersByRelayPortIsLessThanSomething() throws Exception {
        // Initialize the database
        emailBalancerRepository.saveAndFlush(emailBalancer);

        // Get all the emailBalancerList where relayPort less than or equals to DEFAULT_RELAY_PORT
        defaultEmailBalancerShouldNotBeFound("relayPort.lessThan=" + DEFAULT_RELAY_PORT);

        // Get all the emailBalancerList where relayPort less than or equals to UPDATED_RELAY_PORT
        defaultEmailBalancerShouldBeFound("relayPort.lessThan=" + UPDATED_RELAY_PORT);
    }


    @Test
    @Transactional
    public void getAllEmailBalancersByEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        emailBalancerRepository.saveAndFlush(emailBalancer);

        // Get all the emailBalancerList where enabled equals to DEFAULT_ENABLED
        defaultEmailBalancerShouldBeFound("enabled.equals=" + DEFAULT_ENABLED);

        // Get all the emailBalancerList where enabled equals to UPDATED_ENABLED
        defaultEmailBalancerShouldNotBeFound("enabled.equals=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void getAllEmailBalancersByEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        emailBalancerRepository.saveAndFlush(emailBalancer);

        // Get all the emailBalancerList where enabled in DEFAULT_ENABLED or UPDATED_ENABLED
        defaultEmailBalancerShouldBeFound("enabled.in=" + DEFAULT_ENABLED + "," + UPDATED_ENABLED);

        // Get all the emailBalancerList where enabled equals to UPDATED_ENABLED
        defaultEmailBalancerShouldNotBeFound("enabled.in=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void getAllEmailBalancersByEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        emailBalancerRepository.saveAndFlush(emailBalancer);

        // Get all the emailBalancerList where enabled is not null
        defaultEmailBalancerShouldBeFound("enabled.specified=true");

        // Get all the emailBalancerList where enabled is null
        defaultEmailBalancerShouldNotBeFound("enabled.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultEmailBalancerShouldBeFound(String filter) throws Exception {
        restEmailBalancerMockMvc.perform(get("/api/email-balancers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(emailBalancer.getId().intValue())))
            .andExpect(jsonPath("$.[*].relayId").value(hasItem(DEFAULT_RELAY_ID.toString())))
            .andExpect(jsonPath("$.[*].relayPassword").value(hasItem(DEFAULT_RELAY_PASSWORD.toString())))
            .andExpect(jsonPath("$.[*].startingCount").value(hasItem(DEFAULT_STARTING_COUNT)))
            .andExpect(jsonPath("$.[*].endingCount").value(hasItem(DEFAULT_ENDING_COUNT)))
            .andExpect(jsonPath("$.[*].provider").value(hasItem(DEFAULT_PROVIDER.toString())))
            .andExpect(jsonPath("$.[*].relayPort").value(hasItem(DEFAULT_RELAY_PORT)))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())));

        // Check, that the count call also returns 1
        restEmailBalancerMockMvc.perform(get("/api/email-balancers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultEmailBalancerShouldNotBeFound(String filter) throws Exception {
        restEmailBalancerMockMvc.perform(get("/api/email-balancers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEmailBalancerMockMvc.perform(get("/api/email-balancers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingEmailBalancer() throws Exception {
        // Get the emailBalancer
        restEmailBalancerMockMvc.perform(get("/api/email-balancers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEmailBalancer() throws Exception {
        // Initialize the database
        emailBalancerRepository.saveAndFlush(emailBalancer);

        int databaseSizeBeforeUpdate = emailBalancerRepository.findAll().size();

        // Update the emailBalancer
        EmailBalancer updatedEmailBalancer = emailBalancerRepository.findById(emailBalancer.getId()).get();
        // Disconnect from session so that the updates on updatedEmailBalancer are not directly saved in db
        em.detach(updatedEmailBalancer);
        updatedEmailBalancer
            .relayId(UPDATED_RELAY_ID)
            .relayPassword(UPDATED_RELAY_PASSWORD)
            .startingCount(UPDATED_STARTING_COUNT)
            .endingCount(UPDATED_ENDING_COUNT)
            .provider(UPDATED_PROVIDER)
            .relayPort(UPDATED_RELAY_PORT)
            .enabled(UPDATED_ENABLED);
        EmailBalancerDTO emailBalancerDTO = emailBalancerMapper.toDto(updatedEmailBalancer);

        restEmailBalancerMockMvc.perform(put("/api/email-balancers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(emailBalancerDTO)))
            .andExpect(status().isOk());

        // Validate the EmailBalancer in the database
        List<EmailBalancer> emailBalancerList = emailBalancerRepository.findAll();
        assertThat(emailBalancerList).hasSize(databaseSizeBeforeUpdate);
        EmailBalancer testEmailBalancer = emailBalancerList.get(emailBalancerList.size() - 1);
        assertThat(testEmailBalancer.getRelayId()).isEqualTo(UPDATED_RELAY_ID);
        assertThat(testEmailBalancer.getRelayPassword()).isEqualTo(UPDATED_RELAY_PASSWORD);
        assertThat(testEmailBalancer.getStartingCount()).isEqualTo(UPDATED_STARTING_COUNT);
        assertThat(testEmailBalancer.getEndingCount()).isEqualTo(UPDATED_ENDING_COUNT);
        assertThat(testEmailBalancer.getProvider()).isEqualTo(UPDATED_PROVIDER);
        assertThat(testEmailBalancer.getRelayPort()).isEqualTo(UPDATED_RELAY_PORT);
        assertThat(testEmailBalancer.isEnabled()).isEqualTo(UPDATED_ENABLED);

        // Validate the EmailBalancer in Elasticsearch
        verify(mockEmailBalancerSearchRepository, times(1)).save(testEmailBalancer);
    }

    @Test
    @Transactional
    public void updateNonExistingEmailBalancer() throws Exception {
        int databaseSizeBeforeUpdate = emailBalancerRepository.findAll().size();

        // Create the EmailBalancer
        EmailBalancerDTO emailBalancerDTO = emailBalancerMapper.toDto(emailBalancer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmailBalancerMockMvc.perform(put("/api/email-balancers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(emailBalancerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EmailBalancer in the database
        List<EmailBalancer> emailBalancerList = emailBalancerRepository.findAll();
        assertThat(emailBalancerList).hasSize(databaseSizeBeforeUpdate);

        // Validate the EmailBalancer in Elasticsearch
        verify(mockEmailBalancerSearchRepository, times(0)).save(emailBalancer);
    }

    @Test
    @Transactional
    public void deleteEmailBalancer() throws Exception {
        // Initialize the database
        emailBalancerRepository.saveAndFlush(emailBalancer);

        int databaseSizeBeforeDelete = emailBalancerRepository.findAll().size();

        // Get the emailBalancer
        restEmailBalancerMockMvc.perform(delete("/api/email-balancers/{id}", emailBalancer.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<EmailBalancer> emailBalancerList = emailBalancerRepository.findAll();
        assertThat(emailBalancerList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the EmailBalancer in Elasticsearch
        verify(mockEmailBalancerSearchRepository, times(1)).deleteById(emailBalancer.getId());
    }

    @Test
    @Transactional
    public void searchEmailBalancer() throws Exception {
        // Initialize the database
        emailBalancerRepository.saveAndFlush(emailBalancer);
        when(mockEmailBalancerSearchRepository.search(queryStringQuery("id:" + emailBalancer.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(emailBalancer), PageRequest.of(0, 1), 1));
        // Search the emailBalancer
        restEmailBalancerMockMvc.perform(get("/api/_search/email-balancers?query=id:" + emailBalancer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(emailBalancer.getId().intValue())))
            .andExpect(jsonPath("$.[*].relayId").value(hasItem(DEFAULT_RELAY_ID)))
            .andExpect(jsonPath("$.[*].relayPassword").value(hasItem(DEFAULT_RELAY_PASSWORD)))
            .andExpect(jsonPath("$.[*].startingCount").value(hasItem(DEFAULT_STARTING_COUNT)))
            .andExpect(jsonPath("$.[*].endingCount").value(hasItem(DEFAULT_ENDING_COUNT)))
            .andExpect(jsonPath("$.[*].provider").value(hasItem(DEFAULT_PROVIDER)))
            .andExpect(jsonPath("$.[*].relayPort").value(hasItem(DEFAULT_RELAY_PORT)))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmailBalancer.class);
        EmailBalancer emailBalancer1 = new EmailBalancer();
        emailBalancer1.setId(1L);
        EmailBalancer emailBalancer2 = new EmailBalancer();
        emailBalancer2.setId(emailBalancer1.getId());
        assertThat(emailBalancer1).isEqualTo(emailBalancer2);
        emailBalancer2.setId(2L);
        assertThat(emailBalancer1).isNotEqualTo(emailBalancer2);
        emailBalancer1.setId(null);
        assertThat(emailBalancer1).isNotEqualTo(emailBalancer2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmailBalancerDTO.class);
        EmailBalancerDTO emailBalancerDTO1 = new EmailBalancerDTO();
        emailBalancerDTO1.setId(1L);
        EmailBalancerDTO emailBalancerDTO2 = new EmailBalancerDTO();
        assertThat(emailBalancerDTO1).isNotEqualTo(emailBalancerDTO2);
        emailBalancerDTO2.setId(emailBalancerDTO1.getId());
        assertThat(emailBalancerDTO1).isEqualTo(emailBalancerDTO2);
        emailBalancerDTO2.setId(2L);
        assertThat(emailBalancerDTO1).isNotEqualTo(emailBalancerDTO2);
        emailBalancerDTO1.setId(null);
        assertThat(emailBalancerDTO1).isNotEqualTo(emailBalancerDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(emailBalancerMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(emailBalancerMapper.fromId(null)).isNull();
    }
}
