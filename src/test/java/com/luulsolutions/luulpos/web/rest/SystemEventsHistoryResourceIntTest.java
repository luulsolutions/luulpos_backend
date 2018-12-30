package com.luulsolutions.luulpos.web.rest;

import com.luulsolutions.luulpos.LuulposApp;

import com.luulsolutions.luulpos.domain.SystemEventsHistory;
import com.luulsolutions.luulpos.domain.Profile;
import com.luulsolutions.luulpos.repository.SystemEventsHistoryRepository;
import com.luulsolutions.luulpos.repository.search.SystemEventsHistorySearchRepository;
import com.luulsolutions.luulpos.service.SystemEventsHistoryService;
import com.luulsolutions.luulpos.service.dto.SystemEventsHistoryDTO;
import com.luulsolutions.luulpos.service.mapper.SystemEventsHistoryMapper;
import com.luulsolutions.luulpos.web.rest.errors.ExceptionTranslator;
import com.luulsolutions.luulpos.service.dto.SystemEventsHistoryCriteria;
import com.luulsolutions.luulpos.service.SystemEventsHistoryQueryService;

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
 * Test class for the SystemEventsHistoryResource REST controller.
 *
 * @see SystemEventsHistoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LuulposApp.class)
public class SystemEventsHistoryResourceIntTest {

    private static final String DEFAULT_EVENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_EVENT_NAME = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_EVENT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_EVENT_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_EVENT_API = "AAAAAAAAAA";
    private static final String UPDATED_EVENT_API = "BBBBBBBBBB";

    private static final String DEFAULT_EVENT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_EVENT_NOTE = "BBBBBBBBBB";

    private static final String DEFAULT_EVENT_ENTITY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_EVENT_ENTITY_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_EVENT_ENTITY_ID = 1L;
    private static final Long UPDATED_EVENT_ENTITY_ID = 2L;

    @Autowired
    private SystemEventsHistoryRepository systemEventsHistoryRepository;

    @Autowired
    private SystemEventsHistoryMapper systemEventsHistoryMapper;

    @Autowired
    private SystemEventsHistoryService systemEventsHistoryService;

    /**
     * This repository is mocked in the com.luulsolutions.luulpos.repository.search test package.
     *
     * @see com.luulsolutions.luulpos.repository.search.SystemEventsHistorySearchRepositoryMockConfiguration
     */
    @Autowired
    private SystemEventsHistorySearchRepository mockSystemEventsHistorySearchRepository;

    @Autowired
    private SystemEventsHistoryQueryService systemEventsHistoryQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSystemEventsHistoryMockMvc;

    private SystemEventsHistory systemEventsHistory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SystemEventsHistoryResource systemEventsHistoryResource = new SystemEventsHistoryResource(systemEventsHistoryService, systemEventsHistoryQueryService);
        this.restSystemEventsHistoryMockMvc = MockMvcBuilders.standaloneSetup(systemEventsHistoryResource)
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
    public static SystemEventsHistory createEntity(EntityManager em) {
        SystemEventsHistory systemEventsHistory = new SystemEventsHistory()
            .eventName(DEFAULT_EVENT_NAME)
            .eventDate(DEFAULT_EVENT_DATE)
            .eventApi(DEFAULT_EVENT_API)
            .eventNote(DEFAULT_EVENT_NOTE)
            .eventEntityName(DEFAULT_EVENT_ENTITY_NAME)
            .eventEntityId(DEFAULT_EVENT_ENTITY_ID);
        return systemEventsHistory;
    }

    @Before
    public void initTest() {
        systemEventsHistory = createEntity(em);
    }

    @Test
    @Transactional
    public void createSystemEventsHistory() throws Exception {
        int databaseSizeBeforeCreate = systemEventsHistoryRepository.findAll().size();

        // Create the SystemEventsHistory
        SystemEventsHistoryDTO systemEventsHistoryDTO = systemEventsHistoryMapper.toDto(systemEventsHistory);
        restSystemEventsHistoryMockMvc.perform(post("/api/system-events-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(systemEventsHistoryDTO)))
            .andExpect(status().isCreated());

        // Validate the SystemEventsHistory in the database
        List<SystemEventsHistory> systemEventsHistoryList = systemEventsHistoryRepository.findAll();
        assertThat(systemEventsHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        SystemEventsHistory testSystemEventsHistory = systemEventsHistoryList.get(systemEventsHistoryList.size() - 1);
        assertThat(testSystemEventsHistory.getEventName()).isEqualTo(DEFAULT_EVENT_NAME);
        assertThat(testSystemEventsHistory.getEventDate()).isEqualTo(DEFAULT_EVENT_DATE);
        assertThat(testSystemEventsHistory.getEventApi()).isEqualTo(DEFAULT_EVENT_API);
        assertThat(testSystemEventsHistory.getEventNote()).isEqualTo(DEFAULT_EVENT_NOTE);
        assertThat(testSystemEventsHistory.getEventEntityName()).isEqualTo(DEFAULT_EVENT_ENTITY_NAME);
        assertThat(testSystemEventsHistory.getEventEntityId()).isEqualTo(DEFAULT_EVENT_ENTITY_ID);

        // Validate the SystemEventsHistory in Elasticsearch
        verify(mockSystemEventsHistorySearchRepository, times(1)).save(testSystemEventsHistory);
    }

    @Test
    @Transactional
    public void createSystemEventsHistoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = systemEventsHistoryRepository.findAll().size();

        // Create the SystemEventsHistory with an existing ID
        systemEventsHistory.setId(1L);
        SystemEventsHistoryDTO systemEventsHistoryDTO = systemEventsHistoryMapper.toDto(systemEventsHistory);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSystemEventsHistoryMockMvc.perform(post("/api/system-events-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(systemEventsHistoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SystemEventsHistory in the database
        List<SystemEventsHistory> systemEventsHistoryList = systemEventsHistoryRepository.findAll();
        assertThat(systemEventsHistoryList).hasSize(databaseSizeBeforeCreate);

        // Validate the SystemEventsHistory in Elasticsearch
        verify(mockSystemEventsHistorySearchRepository, times(0)).save(systemEventsHistory);
    }

    @Test
    @Transactional
    public void checkEventNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = systemEventsHistoryRepository.findAll().size();
        // set the field null
        systemEventsHistory.setEventName(null);

        // Create the SystemEventsHistory, which fails.
        SystemEventsHistoryDTO systemEventsHistoryDTO = systemEventsHistoryMapper.toDto(systemEventsHistory);

        restSystemEventsHistoryMockMvc.perform(post("/api/system-events-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(systemEventsHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<SystemEventsHistory> systemEventsHistoryList = systemEventsHistoryRepository.findAll();
        assertThat(systemEventsHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSystemEventsHistories() throws Exception {
        // Initialize the database
        systemEventsHistoryRepository.saveAndFlush(systemEventsHistory);

        // Get all the systemEventsHistoryList
        restSystemEventsHistoryMockMvc.perform(get("/api/system-events-histories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(systemEventsHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].eventName").value(hasItem(DEFAULT_EVENT_NAME.toString())))
            .andExpect(jsonPath("$.[*].eventDate").value(hasItem(sameInstant(DEFAULT_EVENT_DATE))))
            .andExpect(jsonPath("$.[*].eventApi").value(hasItem(DEFAULT_EVENT_API.toString())))
            .andExpect(jsonPath("$.[*].eventNote").value(hasItem(DEFAULT_EVENT_NOTE.toString())))
            .andExpect(jsonPath("$.[*].eventEntityName").value(hasItem(DEFAULT_EVENT_ENTITY_NAME.toString())))
            .andExpect(jsonPath("$.[*].eventEntityId").value(hasItem(DEFAULT_EVENT_ENTITY_ID.intValue())));
    }
    
    @Test
    @Transactional
    public void getSystemEventsHistory() throws Exception {
        // Initialize the database
        systemEventsHistoryRepository.saveAndFlush(systemEventsHistory);

        // Get the systemEventsHistory
        restSystemEventsHistoryMockMvc.perform(get("/api/system-events-histories/{id}", systemEventsHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(systemEventsHistory.getId().intValue()))
            .andExpect(jsonPath("$.eventName").value(DEFAULT_EVENT_NAME.toString()))
            .andExpect(jsonPath("$.eventDate").value(sameInstant(DEFAULT_EVENT_DATE)))
            .andExpect(jsonPath("$.eventApi").value(DEFAULT_EVENT_API.toString()))
            .andExpect(jsonPath("$.eventNote").value(DEFAULT_EVENT_NOTE.toString()))
            .andExpect(jsonPath("$.eventEntityName").value(DEFAULT_EVENT_ENTITY_NAME.toString()))
            .andExpect(jsonPath("$.eventEntityId").value(DEFAULT_EVENT_ENTITY_ID.intValue()));
    }

    @Test
    @Transactional
    public void getAllSystemEventsHistoriesByEventNameIsEqualToSomething() throws Exception {
        // Initialize the database
        systemEventsHistoryRepository.saveAndFlush(systemEventsHistory);

        // Get all the systemEventsHistoryList where eventName equals to DEFAULT_EVENT_NAME
        defaultSystemEventsHistoryShouldBeFound("eventName.equals=" + DEFAULT_EVENT_NAME);

        // Get all the systemEventsHistoryList where eventName equals to UPDATED_EVENT_NAME
        defaultSystemEventsHistoryShouldNotBeFound("eventName.equals=" + UPDATED_EVENT_NAME);
    }

    @Test
    @Transactional
    public void getAllSystemEventsHistoriesByEventNameIsInShouldWork() throws Exception {
        // Initialize the database
        systemEventsHistoryRepository.saveAndFlush(systemEventsHistory);

        // Get all the systemEventsHistoryList where eventName in DEFAULT_EVENT_NAME or UPDATED_EVENT_NAME
        defaultSystemEventsHistoryShouldBeFound("eventName.in=" + DEFAULT_EVENT_NAME + "," + UPDATED_EVENT_NAME);

        // Get all the systemEventsHistoryList where eventName equals to UPDATED_EVENT_NAME
        defaultSystemEventsHistoryShouldNotBeFound("eventName.in=" + UPDATED_EVENT_NAME);
    }

    @Test
    @Transactional
    public void getAllSystemEventsHistoriesByEventNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        systemEventsHistoryRepository.saveAndFlush(systemEventsHistory);

        // Get all the systemEventsHistoryList where eventName is not null
        defaultSystemEventsHistoryShouldBeFound("eventName.specified=true");

        // Get all the systemEventsHistoryList where eventName is null
        defaultSystemEventsHistoryShouldNotBeFound("eventName.specified=false");
    }

    @Test
    @Transactional
    public void getAllSystemEventsHistoriesByEventDateIsEqualToSomething() throws Exception {
        // Initialize the database
        systemEventsHistoryRepository.saveAndFlush(systemEventsHistory);

        // Get all the systemEventsHistoryList where eventDate equals to DEFAULT_EVENT_DATE
        defaultSystemEventsHistoryShouldBeFound("eventDate.equals=" + DEFAULT_EVENT_DATE);

        // Get all the systemEventsHistoryList where eventDate equals to UPDATED_EVENT_DATE
        defaultSystemEventsHistoryShouldNotBeFound("eventDate.equals=" + UPDATED_EVENT_DATE);
    }

    @Test
    @Transactional
    public void getAllSystemEventsHistoriesByEventDateIsInShouldWork() throws Exception {
        // Initialize the database
        systemEventsHistoryRepository.saveAndFlush(systemEventsHistory);

        // Get all the systemEventsHistoryList where eventDate in DEFAULT_EVENT_DATE or UPDATED_EVENT_DATE
        defaultSystemEventsHistoryShouldBeFound("eventDate.in=" + DEFAULT_EVENT_DATE + "," + UPDATED_EVENT_DATE);

        // Get all the systemEventsHistoryList where eventDate equals to UPDATED_EVENT_DATE
        defaultSystemEventsHistoryShouldNotBeFound("eventDate.in=" + UPDATED_EVENT_DATE);
    }

    @Test
    @Transactional
    public void getAllSystemEventsHistoriesByEventDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        systemEventsHistoryRepository.saveAndFlush(systemEventsHistory);

        // Get all the systemEventsHistoryList where eventDate is not null
        defaultSystemEventsHistoryShouldBeFound("eventDate.specified=true");

        // Get all the systemEventsHistoryList where eventDate is null
        defaultSystemEventsHistoryShouldNotBeFound("eventDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllSystemEventsHistoriesByEventDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        systemEventsHistoryRepository.saveAndFlush(systemEventsHistory);

        // Get all the systemEventsHistoryList where eventDate greater than or equals to DEFAULT_EVENT_DATE
        defaultSystemEventsHistoryShouldBeFound("eventDate.greaterOrEqualThan=" + DEFAULT_EVENT_DATE);

        // Get all the systemEventsHistoryList where eventDate greater than or equals to UPDATED_EVENT_DATE
        defaultSystemEventsHistoryShouldNotBeFound("eventDate.greaterOrEqualThan=" + UPDATED_EVENT_DATE);
    }

    @Test
    @Transactional
    public void getAllSystemEventsHistoriesByEventDateIsLessThanSomething() throws Exception {
        // Initialize the database
        systemEventsHistoryRepository.saveAndFlush(systemEventsHistory);

        // Get all the systemEventsHistoryList where eventDate less than or equals to DEFAULT_EVENT_DATE
        defaultSystemEventsHistoryShouldNotBeFound("eventDate.lessThan=" + DEFAULT_EVENT_DATE);

        // Get all the systemEventsHistoryList where eventDate less than or equals to UPDATED_EVENT_DATE
        defaultSystemEventsHistoryShouldBeFound("eventDate.lessThan=" + UPDATED_EVENT_DATE);
    }


    @Test
    @Transactional
    public void getAllSystemEventsHistoriesByEventApiIsEqualToSomething() throws Exception {
        // Initialize the database
        systemEventsHistoryRepository.saveAndFlush(systemEventsHistory);

        // Get all the systemEventsHistoryList where eventApi equals to DEFAULT_EVENT_API
        defaultSystemEventsHistoryShouldBeFound("eventApi.equals=" + DEFAULT_EVENT_API);

        // Get all the systemEventsHistoryList where eventApi equals to UPDATED_EVENT_API
        defaultSystemEventsHistoryShouldNotBeFound("eventApi.equals=" + UPDATED_EVENT_API);
    }

    @Test
    @Transactional
    public void getAllSystemEventsHistoriesByEventApiIsInShouldWork() throws Exception {
        // Initialize the database
        systemEventsHistoryRepository.saveAndFlush(systemEventsHistory);

        // Get all the systemEventsHistoryList where eventApi in DEFAULT_EVENT_API or UPDATED_EVENT_API
        defaultSystemEventsHistoryShouldBeFound("eventApi.in=" + DEFAULT_EVENT_API + "," + UPDATED_EVENT_API);

        // Get all the systemEventsHistoryList where eventApi equals to UPDATED_EVENT_API
        defaultSystemEventsHistoryShouldNotBeFound("eventApi.in=" + UPDATED_EVENT_API);
    }

    @Test
    @Transactional
    public void getAllSystemEventsHistoriesByEventApiIsNullOrNotNull() throws Exception {
        // Initialize the database
        systemEventsHistoryRepository.saveAndFlush(systemEventsHistory);

        // Get all the systemEventsHistoryList where eventApi is not null
        defaultSystemEventsHistoryShouldBeFound("eventApi.specified=true");

        // Get all the systemEventsHistoryList where eventApi is null
        defaultSystemEventsHistoryShouldNotBeFound("eventApi.specified=false");
    }

    @Test
    @Transactional
    public void getAllSystemEventsHistoriesByEventNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        systemEventsHistoryRepository.saveAndFlush(systemEventsHistory);

        // Get all the systemEventsHistoryList where eventNote equals to DEFAULT_EVENT_NOTE
        defaultSystemEventsHistoryShouldBeFound("eventNote.equals=" + DEFAULT_EVENT_NOTE);

        // Get all the systemEventsHistoryList where eventNote equals to UPDATED_EVENT_NOTE
        defaultSystemEventsHistoryShouldNotBeFound("eventNote.equals=" + UPDATED_EVENT_NOTE);
    }

    @Test
    @Transactional
    public void getAllSystemEventsHistoriesByEventNoteIsInShouldWork() throws Exception {
        // Initialize the database
        systemEventsHistoryRepository.saveAndFlush(systemEventsHistory);

        // Get all the systemEventsHistoryList where eventNote in DEFAULT_EVENT_NOTE or UPDATED_EVENT_NOTE
        defaultSystemEventsHistoryShouldBeFound("eventNote.in=" + DEFAULT_EVENT_NOTE + "," + UPDATED_EVENT_NOTE);

        // Get all the systemEventsHistoryList where eventNote equals to UPDATED_EVENT_NOTE
        defaultSystemEventsHistoryShouldNotBeFound("eventNote.in=" + UPDATED_EVENT_NOTE);
    }

    @Test
    @Transactional
    public void getAllSystemEventsHistoriesByEventNoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        systemEventsHistoryRepository.saveAndFlush(systemEventsHistory);

        // Get all the systemEventsHistoryList where eventNote is not null
        defaultSystemEventsHistoryShouldBeFound("eventNote.specified=true");

        // Get all the systemEventsHistoryList where eventNote is null
        defaultSystemEventsHistoryShouldNotBeFound("eventNote.specified=false");
    }

    @Test
    @Transactional
    public void getAllSystemEventsHistoriesByEventEntityNameIsEqualToSomething() throws Exception {
        // Initialize the database
        systemEventsHistoryRepository.saveAndFlush(systemEventsHistory);

        // Get all the systemEventsHistoryList where eventEntityName equals to DEFAULT_EVENT_ENTITY_NAME
        defaultSystemEventsHistoryShouldBeFound("eventEntityName.equals=" + DEFAULT_EVENT_ENTITY_NAME);

        // Get all the systemEventsHistoryList where eventEntityName equals to UPDATED_EVENT_ENTITY_NAME
        defaultSystemEventsHistoryShouldNotBeFound("eventEntityName.equals=" + UPDATED_EVENT_ENTITY_NAME);
    }

    @Test
    @Transactional
    public void getAllSystemEventsHistoriesByEventEntityNameIsInShouldWork() throws Exception {
        // Initialize the database
        systemEventsHistoryRepository.saveAndFlush(systemEventsHistory);

        // Get all the systemEventsHistoryList where eventEntityName in DEFAULT_EVENT_ENTITY_NAME or UPDATED_EVENT_ENTITY_NAME
        defaultSystemEventsHistoryShouldBeFound("eventEntityName.in=" + DEFAULT_EVENT_ENTITY_NAME + "," + UPDATED_EVENT_ENTITY_NAME);

        // Get all the systemEventsHistoryList where eventEntityName equals to UPDATED_EVENT_ENTITY_NAME
        defaultSystemEventsHistoryShouldNotBeFound("eventEntityName.in=" + UPDATED_EVENT_ENTITY_NAME);
    }

    @Test
    @Transactional
    public void getAllSystemEventsHistoriesByEventEntityNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        systemEventsHistoryRepository.saveAndFlush(systemEventsHistory);

        // Get all the systemEventsHistoryList where eventEntityName is not null
        defaultSystemEventsHistoryShouldBeFound("eventEntityName.specified=true");

        // Get all the systemEventsHistoryList where eventEntityName is null
        defaultSystemEventsHistoryShouldNotBeFound("eventEntityName.specified=false");
    }

    @Test
    @Transactional
    public void getAllSystemEventsHistoriesByEventEntityIdIsEqualToSomething() throws Exception {
        // Initialize the database
        systemEventsHistoryRepository.saveAndFlush(systemEventsHistory);

        // Get all the systemEventsHistoryList where eventEntityId equals to DEFAULT_EVENT_ENTITY_ID
        defaultSystemEventsHistoryShouldBeFound("eventEntityId.equals=" + DEFAULT_EVENT_ENTITY_ID);

        // Get all the systemEventsHistoryList where eventEntityId equals to UPDATED_EVENT_ENTITY_ID
        defaultSystemEventsHistoryShouldNotBeFound("eventEntityId.equals=" + UPDATED_EVENT_ENTITY_ID);
    }

    @Test
    @Transactional
    public void getAllSystemEventsHistoriesByEventEntityIdIsInShouldWork() throws Exception {
        // Initialize the database
        systemEventsHistoryRepository.saveAndFlush(systemEventsHistory);

        // Get all the systemEventsHistoryList where eventEntityId in DEFAULT_EVENT_ENTITY_ID or UPDATED_EVENT_ENTITY_ID
        defaultSystemEventsHistoryShouldBeFound("eventEntityId.in=" + DEFAULT_EVENT_ENTITY_ID + "," + UPDATED_EVENT_ENTITY_ID);

        // Get all the systemEventsHistoryList where eventEntityId equals to UPDATED_EVENT_ENTITY_ID
        defaultSystemEventsHistoryShouldNotBeFound("eventEntityId.in=" + UPDATED_EVENT_ENTITY_ID);
    }

    @Test
    @Transactional
    public void getAllSystemEventsHistoriesByEventEntityIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        systemEventsHistoryRepository.saveAndFlush(systemEventsHistory);

        // Get all the systemEventsHistoryList where eventEntityId is not null
        defaultSystemEventsHistoryShouldBeFound("eventEntityId.specified=true");

        // Get all the systemEventsHistoryList where eventEntityId is null
        defaultSystemEventsHistoryShouldNotBeFound("eventEntityId.specified=false");
    }

    @Test
    @Transactional
    public void getAllSystemEventsHistoriesByEventEntityIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        systemEventsHistoryRepository.saveAndFlush(systemEventsHistory);

        // Get all the systemEventsHistoryList where eventEntityId greater than or equals to DEFAULT_EVENT_ENTITY_ID
        defaultSystemEventsHistoryShouldBeFound("eventEntityId.greaterOrEqualThan=" + DEFAULT_EVENT_ENTITY_ID);

        // Get all the systemEventsHistoryList where eventEntityId greater than or equals to UPDATED_EVENT_ENTITY_ID
        defaultSystemEventsHistoryShouldNotBeFound("eventEntityId.greaterOrEqualThan=" + UPDATED_EVENT_ENTITY_ID);
    }

    @Test
    @Transactional
    public void getAllSystemEventsHistoriesByEventEntityIdIsLessThanSomething() throws Exception {
        // Initialize the database
        systemEventsHistoryRepository.saveAndFlush(systemEventsHistory);

        // Get all the systemEventsHistoryList where eventEntityId less than or equals to DEFAULT_EVENT_ENTITY_ID
        defaultSystemEventsHistoryShouldNotBeFound("eventEntityId.lessThan=" + DEFAULT_EVENT_ENTITY_ID);

        // Get all the systemEventsHistoryList where eventEntityId less than or equals to UPDATED_EVENT_ENTITY_ID
        defaultSystemEventsHistoryShouldBeFound("eventEntityId.lessThan=" + UPDATED_EVENT_ENTITY_ID);
    }


    @Test
    @Transactional
    public void getAllSystemEventsHistoriesByTriggedByIsEqualToSomething() throws Exception {
        // Initialize the database
        Profile triggedBy = ProfileResourceIntTest.createEntity(em);
        em.persist(triggedBy);
        em.flush();
        systemEventsHistory.setTriggedBy(triggedBy);
        systemEventsHistoryRepository.saveAndFlush(systemEventsHistory);
        Long triggedById = triggedBy.getId();

        // Get all the systemEventsHistoryList where triggedBy equals to triggedById
        defaultSystemEventsHistoryShouldBeFound("triggedById.equals=" + triggedById);

        // Get all the systemEventsHistoryList where triggedBy equals to triggedById + 1
        defaultSystemEventsHistoryShouldNotBeFound("triggedById.equals=" + (triggedById + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultSystemEventsHistoryShouldBeFound(String filter) throws Exception {
        restSystemEventsHistoryMockMvc.perform(get("/api/system-events-histories?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(systemEventsHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].eventName").value(hasItem(DEFAULT_EVENT_NAME.toString())))
            .andExpect(jsonPath("$.[*].eventDate").value(hasItem(sameInstant(DEFAULT_EVENT_DATE))))
            .andExpect(jsonPath("$.[*].eventApi").value(hasItem(DEFAULT_EVENT_API.toString())))
            .andExpect(jsonPath("$.[*].eventNote").value(hasItem(DEFAULT_EVENT_NOTE.toString())))
            .andExpect(jsonPath("$.[*].eventEntityName").value(hasItem(DEFAULT_EVENT_ENTITY_NAME.toString())))
            .andExpect(jsonPath("$.[*].eventEntityId").value(hasItem(DEFAULT_EVENT_ENTITY_ID.intValue())));

        // Check, that the count call also returns 1
        restSystemEventsHistoryMockMvc.perform(get("/api/system-events-histories/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultSystemEventsHistoryShouldNotBeFound(String filter) throws Exception {
        restSystemEventsHistoryMockMvc.perform(get("/api/system-events-histories?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSystemEventsHistoryMockMvc.perform(get("/api/system-events-histories/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingSystemEventsHistory() throws Exception {
        // Get the systemEventsHistory
        restSystemEventsHistoryMockMvc.perform(get("/api/system-events-histories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSystemEventsHistory() throws Exception {
        // Initialize the database
        systemEventsHistoryRepository.saveAndFlush(systemEventsHistory);

        int databaseSizeBeforeUpdate = systemEventsHistoryRepository.findAll().size();

        // Update the systemEventsHistory
        SystemEventsHistory updatedSystemEventsHistory = systemEventsHistoryRepository.findById(systemEventsHistory.getId()).get();
        // Disconnect from session so that the updates on updatedSystemEventsHistory are not directly saved in db
        em.detach(updatedSystemEventsHistory);
        updatedSystemEventsHistory
            .eventName(UPDATED_EVENT_NAME)
            .eventDate(UPDATED_EVENT_DATE)
            .eventApi(UPDATED_EVENT_API)
            .eventNote(UPDATED_EVENT_NOTE)
            .eventEntityName(UPDATED_EVENT_ENTITY_NAME)
            .eventEntityId(UPDATED_EVENT_ENTITY_ID);
        SystemEventsHistoryDTO systemEventsHistoryDTO = systemEventsHistoryMapper.toDto(updatedSystemEventsHistory);

        restSystemEventsHistoryMockMvc.perform(put("/api/system-events-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(systemEventsHistoryDTO)))
            .andExpect(status().isOk());

        // Validate the SystemEventsHistory in the database
        List<SystemEventsHistory> systemEventsHistoryList = systemEventsHistoryRepository.findAll();
        assertThat(systemEventsHistoryList).hasSize(databaseSizeBeforeUpdate);
        SystemEventsHistory testSystemEventsHistory = systemEventsHistoryList.get(systemEventsHistoryList.size() - 1);
        assertThat(testSystemEventsHistory.getEventName()).isEqualTo(UPDATED_EVENT_NAME);
        assertThat(testSystemEventsHistory.getEventDate()).isEqualTo(UPDATED_EVENT_DATE);
        assertThat(testSystemEventsHistory.getEventApi()).isEqualTo(UPDATED_EVENT_API);
        assertThat(testSystemEventsHistory.getEventNote()).isEqualTo(UPDATED_EVENT_NOTE);
        assertThat(testSystemEventsHistory.getEventEntityName()).isEqualTo(UPDATED_EVENT_ENTITY_NAME);
        assertThat(testSystemEventsHistory.getEventEntityId()).isEqualTo(UPDATED_EVENT_ENTITY_ID);

        // Validate the SystemEventsHistory in Elasticsearch
        verify(mockSystemEventsHistorySearchRepository, times(1)).save(testSystemEventsHistory);
    }

    @Test
    @Transactional
    public void updateNonExistingSystemEventsHistory() throws Exception {
        int databaseSizeBeforeUpdate = systemEventsHistoryRepository.findAll().size();

        // Create the SystemEventsHistory
        SystemEventsHistoryDTO systemEventsHistoryDTO = systemEventsHistoryMapper.toDto(systemEventsHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSystemEventsHistoryMockMvc.perform(put("/api/system-events-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(systemEventsHistoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SystemEventsHistory in the database
        List<SystemEventsHistory> systemEventsHistoryList = systemEventsHistoryRepository.findAll();
        assertThat(systemEventsHistoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SystemEventsHistory in Elasticsearch
        verify(mockSystemEventsHistorySearchRepository, times(0)).save(systemEventsHistory);
    }

    @Test
    @Transactional
    public void deleteSystemEventsHistory() throws Exception {
        // Initialize the database
        systemEventsHistoryRepository.saveAndFlush(systemEventsHistory);

        int databaseSizeBeforeDelete = systemEventsHistoryRepository.findAll().size();

        // Get the systemEventsHistory
        restSystemEventsHistoryMockMvc.perform(delete("/api/system-events-histories/{id}", systemEventsHistory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SystemEventsHistory> systemEventsHistoryList = systemEventsHistoryRepository.findAll();
        assertThat(systemEventsHistoryList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the SystemEventsHistory in Elasticsearch
        verify(mockSystemEventsHistorySearchRepository, times(1)).deleteById(systemEventsHistory.getId());
    }

    @Test
    @Transactional
    public void searchSystemEventsHistory() throws Exception {
        // Initialize the database
        systemEventsHistoryRepository.saveAndFlush(systemEventsHistory);
        when(mockSystemEventsHistorySearchRepository.search(queryStringQuery("id:" + systemEventsHistory.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(systemEventsHistory), PageRequest.of(0, 1), 1));
        // Search the systemEventsHistory
        restSystemEventsHistoryMockMvc.perform(get("/api/_search/system-events-histories?query=id:" + systemEventsHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(systemEventsHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].eventName").value(hasItem(DEFAULT_EVENT_NAME)))
            .andExpect(jsonPath("$.[*].eventDate").value(hasItem(sameInstant(DEFAULT_EVENT_DATE))))
            .andExpect(jsonPath("$.[*].eventApi").value(hasItem(DEFAULT_EVENT_API)))
            .andExpect(jsonPath("$.[*].eventNote").value(hasItem(DEFAULT_EVENT_NOTE)))
            .andExpect(jsonPath("$.[*].eventEntityName").value(hasItem(DEFAULT_EVENT_ENTITY_NAME)))
            .andExpect(jsonPath("$.[*].eventEntityId").value(hasItem(DEFAULT_EVENT_ENTITY_ID.intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SystemEventsHistory.class);
        SystemEventsHistory systemEventsHistory1 = new SystemEventsHistory();
        systemEventsHistory1.setId(1L);
        SystemEventsHistory systemEventsHistory2 = new SystemEventsHistory();
        systemEventsHistory2.setId(systemEventsHistory1.getId());
        assertThat(systemEventsHistory1).isEqualTo(systemEventsHistory2);
        systemEventsHistory2.setId(2L);
        assertThat(systemEventsHistory1).isNotEqualTo(systemEventsHistory2);
        systemEventsHistory1.setId(null);
        assertThat(systemEventsHistory1).isNotEqualTo(systemEventsHistory2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SystemEventsHistoryDTO.class);
        SystemEventsHistoryDTO systemEventsHistoryDTO1 = new SystemEventsHistoryDTO();
        systemEventsHistoryDTO1.setId(1L);
        SystemEventsHistoryDTO systemEventsHistoryDTO2 = new SystemEventsHistoryDTO();
        assertThat(systemEventsHistoryDTO1).isNotEqualTo(systemEventsHistoryDTO2);
        systemEventsHistoryDTO2.setId(systemEventsHistoryDTO1.getId());
        assertThat(systemEventsHistoryDTO1).isEqualTo(systemEventsHistoryDTO2);
        systemEventsHistoryDTO2.setId(2L);
        assertThat(systemEventsHistoryDTO1).isNotEqualTo(systemEventsHistoryDTO2);
        systemEventsHistoryDTO1.setId(null);
        assertThat(systemEventsHistoryDTO1).isNotEqualTo(systemEventsHistoryDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(systemEventsHistoryMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(systemEventsHistoryMapper.fromId(null)).isNull();
    }
}
