package com.luulsolutions.luulpos.web.rest;

import com.luulsolutions.luulpos.LuulposApp;

import com.luulsolutions.luulpos.domain.SuspensionHistory;
import com.luulsolutions.luulpos.domain.Profile;
import com.luulsolutions.luulpos.domain.Profile;
import com.luulsolutions.luulpos.repository.SuspensionHistoryRepository;
import com.luulsolutions.luulpos.repository.search.SuspensionHistorySearchRepository;
import com.luulsolutions.luulpos.service.SuspensionHistoryService;
import com.luulsolutions.luulpos.service.dto.SuspensionHistoryDTO;
import com.luulsolutions.luulpos.service.mapper.SuspensionHistoryMapper;
import com.luulsolutions.luulpos.web.rest.errors.ExceptionTranslator;
import com.luulsolutions.luulpos.service.dto.SuspensionHistoryCriteria;
import com.luulsolutions.luulpos.service.SuspensionHistoryQueryService;

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

import com.luulsolutions.luulpos.domain.enumeration.SuspensionType;
/**
 * Test class for the SuspensionHistoryResource REST controller.
 *
 * @see SuspensionHistoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LuulposApp.class)
public class SuspensionHistoryResourceIntTest {

    private static final ZonedDateTime DEFAULT_SUSPENDED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_SUSPENDED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final SuspensionType DEFAULT_SUSPENSION_TYPE = SuspensionType.BANNED_FOR_LIFE;
    private static final SuspensionType UPDATED_SUSPENSION_TYPE = SuspensionType.BANNED_TEMPORARILY;

    private static final String DEFAULT_SUSPENDED_REASON = "AAAAAAAAAA";
    private static final String UPDATED_SUSPENDED_REASON = "BBBBBBBBBB";

    private static final String DEFAULT_RESOLUTION_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_RESOLUTION_NOTE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_UNSUSPENSION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UNSUSPENSION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private SuspensionHistoryRepository suspensionHistoryRepository;

    @Autowired
    private SuspensionHistoryMapper suspensionHistoryMapper;

    @Autowired
    private SuspensionHistoryService suspensionHistoryService;

    /**
     * This repository is mocked in the com.luulsolutions.luulpos.repository.search test package.
     *
     * @see com.luulsolutions.luulpos.repository.search.SuspensionHistorySearchRepositoryMockConfiguration
     */
    @Autowired
    private SuspensionHistorySearchRepository mockSuspensionHistorySearchRepository;

    @Autowired
    private SuspensionHistoryQueryService suspensionHistoryQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSuspensionHistoryMockMvc;

    private SuspensionHistory suspensionHistory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SuspensionHistoryResource suspensionHistoryResource = new SuspensionHistoryResource(suspensionHistoryService, suspensionHistoryQueryService);
        this.restSuspensionHistoryMockMvc = MockMvcBuilders.standaloneSetup(suspensionHistoryResource)
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
    public static SuspensionHistory createEntity(EntityManager em) {
        SuspensionHistory suspensionHistory = new SuspensionHistory()
            .suspendedDate(DEFAULT_SUSPENDED_DATE)
            .suspensionType(DEFAULT_SUSPENSION_TYPE)
            .suspendedReason(DEFAULT_SUSPENDED_REASON)
            .resolutionNote(DEFAULT_RESOLUTION_NOTE)
            .unsuspensionDate(DEFAULT_UNSUSPENSION_DATE);
        return suspensionHistory;
    }

    @Before
    public void initTest() {
        suspensionHistory = createEntity(em);
    }

    @Test
    @Transactional
    public void createSuspensionHistory() throws Exception {
        int databaseSizeBeforeCreate = suspensionHistoryRepository.findAll().size();

        // Create the SuspensionHistory
        SuspensionHistoryDTO suspensionHistoryDTO = suspensionHistoryMapper.toDto(suspensionHistory);
        restSuspensionHistoryMockMvc.perform(post("/api/suspension-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(suspensionHistoryDTO)))
            .andExpect(status().isCreated());

        // Validate the SuspensionHistory in the database
        List<SuspensionHistory> suspensionHistoryList = suspensionHistoryRepository.findAll();
        assertThat(suspensionHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        SuspensionHistory testSuspensionHistory = suspensionHistoryList.get(suspensionHistoryList.size() - 1);
        assertThat(testSuspensionHistory.getSuspendedDate()).isEqualTo(DEFAULT_SUSPENDED_DATE);
        assertThat(testSuspensionHistory.getSuspensionType()).isEqualTo(DEFAULT_SUSPENSION_TYPE);
        assertThat(testSuspensionHistory.getSuspendedReason()).isEqualTo(DEFAULT_SUSPENDED_REASON);
        assertThat(testSuspensionHistory.getResolutionNote()).isEqualTo(DEFAULT_RESOLUTION_NOTE);
        assertThat(testSuspensionHistory.getUnsuspensionDate()).isEqualTo(DEFAULT_UNSUSPENSION_DATE);

        // Validate the SuspensionHistory in Elasticsearch
        verify(mockSuspensionHistorySearchRepository, times(1)).save(testSuspensionHistory);
    }

    @Test
    @Transactional
    public void createSuspensionHistoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = suspensionHistoryRepository.findAll().size();

        // Create the SuspensionHistory with an existing ID
        suspensionHistory.setId(1L);
        SuspensionHistoryDTO suspensionHistoryDTO = suspensionHistoryMapper.toDto(suspensionHistory);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSuspensionHistoryMockMvc.perform(post("/api/suspension-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(suspensionHistoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SuspensionHistory in the database
        List<SuspensionHistory> suspensionHistoryList = suspensionHistoryRepository.findAll();
        assertThat(suspensionHistoryList).hasSize(databaseSizeBeforeCreate);

        // Validate the SuspensionHistory in Elasticsearch
        verify(mockSuspensionHistorySearchRepository, times(0)).save(suspensionHistory);
    }

    @Test
    @Transactional
    public void getAllSuspensionHistories() throws Exception {
        // Initialize the database
        suspensionHistoryRepository.saveAndFlush(suspensionHistory);

        // Get all the suspensionHistoryList
        restSuspensionHistoryMockMvc.perform(get("/api/suspension-histories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(suspensionHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].suspendedDate").value(hasItem(sameInstant(DEFAULT_SUSPENDED_DATE))))
            .andExpect(jsonPath("$.[*].suspensionType").value(hasItem(DEFAULT_SUSPENSION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].suspendedReason").value(hasItem(DEFAULT_SUSPENDED_REASON.toString())))
            .andExpect(jsonPath("$.[*].resolutionNote").value(hasItem(DEFAULT_RESOLUTION_NOTE.toString())))
            .andExpect(jsonPath("$.[*].unsuspensionDate").value(hasItem(sameInstant(DEFAULT_UNSUSPENSION_DATE))));
    }
    
    @Test
    @Transactional
    public void getSuspensionHistory() throws Exception {
        // Initialize the database
        suspensionHistoryRepository.saveAndFlush(suspensionHistory);

        // Get the suspensionHistory
        restSuspensionHistoryMockMvc.perform(get("/api/suspension-histories/{id}", suspensionHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(suspensionHistory.getId().intValue()))
            .andExpect(jsonPath("$.suspendedDate").value(sameInstant(DEFAULT_SUSPENDED_DATE)))
            .andExpect(jsonPath("$.suspensionType").value(DEFAULT_SUSPENSION_TYPE.toString()))
            .andExpect(jsonPath("$.suspendedReason").value(DEFAULT_SUSPENDED_REASON.toString()))
            .andExpect(jsonPath("$.resolutionNote").value(DEFAULT_RESOLUTION_NOTE.toString()))
            .andExpect(jsonPath("$.unsuspensionDate").value(sameInstant(DEFAULT_UNSUSPENSION_DATE)));
    }

    @Test
    @Transactional
    public void getAllSuspensionHistoriesBySuspendedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        suspensionHistoryRepository.saveAndFlush(suspensionHistory);

        // Get all the suspensionHistoryList where suspendedDate equals to DEFAULT_SUSPENDED_DATE
        defaultSuspensionHistoryShouldBeFound("suspendedDate.equals=" + DEFAULT_SUSPENDED_DATE);

        // Get all the suspensionHistoryList where suspendedDate equals to UPDATED_SUSPENDED_DATE
        defaultSuspensionHistoryShouldNotBeFound("suspendedDate.equals=" + UPDATED_SUSPENDED_DATE);
    }

    @Test
    @Transactional
    public void getAllSuspensionHistoriesBySuspendedDateIsInShouldWork() throws Exception {
        // Initialize the database
        suspensionHistoryRepository.saveAndFlush(suspensionHistory);

        // Get all the suspensionHistoryList where suspendedDate in DEFAULT_SUSPENDED_DATE or UPDATED_SUSPENDED_DATE
        defaultSuspensionHistoryShouldBeFound("suspendedDate.in=" + DEFAULT_SUSPENDED_DATE + "," + UPDATED_SUSPENDED_DATE);

        // Get all the suspensionHistoryList where suspendedDate equals to UPDATED_SUSPENDED_DATE
        defaultSuspensionHistoryShouldNotBeFound("suspendedDate.in=" + UPDATED_SUSPENDED_DATE);
    }

    @Test
    @Transactional
    public void getAllSuspensionHistoriesBySuspendedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        suspensionHistoryRepository.saveAndFlush(suspensionHistory);

        // Get all the suspensionHistoryList where suspendedDate is not null
        defaultSuspensionHistoryShouldBeFound("suspendedDate.specified=true");

        // Get all the suspensionHistoryList where suspendedDate is null
        defaultSuspensionHistoryShouldNotBeFound("suspendedDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllSuspensionHistoriesBySuspendedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        suspensionHistoryRepository.saveAndFlush(suspensionHistory);

        // Get all the suspensionHistoryList where suspendedDate greater than or equals to DEFAULT_SUSPENDED_DATE
        defaultSuspensionHistoryShouldBeFound("suspendedDate.greaterOrEqualThan=" + DEFAULT_SUSPENDED_DATE);

        // Get all the suspensionHistoryList where suspendedDate greater than or equals to UPDATED_SUSPENDED_DATE
        defaultSuspensionHistoryShouldNotBeFound("suspendedDate.greaterOrEqualThan=" + UPDATED_SUSPENDED_DATE);
    }

    @Test
    @Transactional
    public void getAllSuspensionHistoriesBySuspendedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        suspensionHistoryRepository.saveAndFlush(suspensionHistory);

        // Get all the suspensionHistoryList where suspendedDate less than or equals to DEFAULT_SUSPENDED_DATE
        defaultSuspensionHistoryShouldNotBeFound("suspendedDate.lessThan=" + DEFAULT_SUSPENDED_DATE);

        // Get all the suspensionHistoryList where suspendedDate less than or equals to UPDATED_SUSPENDED_DATE
        defaultSuspensionHistoryShouldBeFound("suspendedDate.lessThan=" + UPDATED_SUSPENDED_DATE);
    }


    @Test
    @Transactional
    public void getAllSuspensionHistoriesBySuspensionTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        suspensionHistoryRepository.saveAndFlush(suspensionHistory);

        // Get all the suspensionHistoryList where suspensionType equals to DEFAULT_SUSPENSION_TYPE
        defaultSuspensionHistoryShouldBeFound("suspensionType.equals=" + DEFAULT_SUSPENSION_TYPE);

        // Get all the suspensionHistoryList where suspensionType equals to UPDATED_SUSPENSION_TYPE
        defaultSuspensionHistoryShouldNotBeFound("suspensionType.equals=" + UPDATED_SUSPENSION_TYPE);
    }

    @Test
    @Transactional
    public void getAllSuspensionHistoriesBySuspensionTypeIsInShouldWork() throws Exception {
        // Initialize the database
        suspensionHistoryRepository.saveAndFlush(suspensionHistory);

        // Get all the suspensionHistoryList where suspensionType in DEFAULT_SUSPENSION_TYPE or UPDATED_SUSPENSION_TYPE
        defaultSuspensionHistoryShouldBeFound("suspensionType.in=" + DEFAULT_SUSPENSION_TYPE + "," + UPDATED_SUSPENSION_TYPE);

        // Get all the suspensionHistoryList where suspensionType equals to UPDATED_SUSPENSION_TYPE
        defaultSuspensionHistoryShouldNotBeFound("suspensionType.in=" + UPDATED_SUSPENSION_TYPE);
    }

    @Test
    @Transactional
    public void getAllSuspensionHistoriesBySuspensionTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        suspensionHistoryRepository.saveAndFlush(suspensionHistory);

        // Get all the suspensionHistoryList where suspensionType is not null
        defaultSuspensionHistoryShouldBeFound("suspensionType.specified=true");

        // Get all the suspensionHistoryList where suspensionType is null
        defaultSuspensionHistoryShouldNotBeFound("suspensionType.specified=false");
    }

    @Test
    @Transactional
    public void getAllSuspensionHistoriesBySuspendedReasonIsEqualToSomething() throws Exception {
        // Initialize the database
        suspensionHistoryRepository.saveAndFlush(suspensionHistory);

        // Get all the suspensionHistoryList where suspendedReason equals to DEFAULT_SUSPENDED_REASON
        defaultSuspensionHistoryShouldBeFound("suspendedReason.equals=" + DEFAULT_SUSPENDED_REASON);

        // Get all the suspensionHistoryList where suspendedReason equals to UPDATED_SUSPENDED_REASON
        defaultSuspensionHistoryShouldNotBeFound("suspendedReason.equals=" + UPDATED_SUSPENDED_REASON);
    }

    @Test
    @Transactional
    public void getAllSuspensionHistoriesBySuspendedReasonIsInShouldWork() throws Exception {
        // Initialize the database
        suspensionHistoryRepository.saveAndFlush(suspensionHistory);

        // Get all the suspensionHistoryList where suspendedReason in DEFAULT_SUSPENDED_REASON or UPDATED_SUSPENDED_REASON
        defaultSuspensionHistoryShouldBeFound("suspendedReason.in=" + DEFAULT_SUSPENDED_REASON + "," + UPDATED_SUSPENDED_REASON);

        // Get all the suspensionHistoryList where suspendedReason equals to UPDATED_SUSPENDED_REASON
        defaultSuspensionHistoryShouldNotBeFound("suspendedReason.in=" + UPDATED_SUSPENDED_REASON);
    }

    @Test
    @Transactional
    public void getAllSuspensionHistoriesBySuspendedReasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        suspensionHistoryRepository.saveAndFlush(suspensionHistory);

        // Get all the suspensionHistoryList where suspendedReason is not null
        defaultSuspensionHistoryShouldBeFound("suspendedReason.specified=true");

        // Get all the suspensionHistoryList where suspendedReason is null
        defaultSuspensionHistoryShouldNotBeFound("suspendedReason.specified=false");
    }

    @Test
    @Transactional
    public void getAllSuspensionHistoriesByResolutionNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        suspensionHistoryRepository.saveAndFlush(suspensionHistory);

        // Get all the suspensionHistoryList where resolutionNote equals to DEFAULT_RESOLUTION_NOTE
        defaultSuspensionHistoryShouldBeFound("resolutionNote.equals=" + DEFAULT_RESOLUTION_NOTE);

        // Get all the suspensionHistoryList where resolutionNote equals to UPDATED_RESOLUTION_NOTE
        defaultSuspensionHistoryShouldNotBeFound("resolutionNote.equals=" + UPDATED_RESOLUTION_NOTE);
    }

    @Test
    @Transactional
    public void getAllSuspensionHistoriesByResolutionNoteIsInShouldWork() throws Exception {
        // Initialize the database
        suspensionHistoryRepository.saveAndFlush(suspensionHistory);

        // Get all the suspensionHistoryList where resolutionNote in DEFAULT_RESOLUTION_NOTE or UPDATED_RESOLUTION_NOTE
        defaultSuspensionHistoryShouldBeFound("resolutionNote.in=" + DEFAULT_RESOLUTION_NOTE + "," + UPDATED_RESOLUTION_NOTE);

        // Get all the suspensionHistoryList where resolutionNote equals to UPDATED_RESOLUTION_NOTE
        defaultSuspensionHistoryShouldNotBeFound("resolutionNote.in=" + UPDATED_RESOLUTION_NOTE);
    }

    @Test
    @Transactional
    public void getAllSuspensionHistoriesByResolutionNoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        suspensionHistoryRepository.saveAndFlush(suspensionHistory);

        // Get all the suspensionHistoryList where resolutionNote is not null
        defaultSuspensionHistoryShouldBeFound("resolutionNote.specified=true");

        // Get all the suspensionHistoryList where resolutionNote is null
        defaultSuspensionHistoryShouldNotBeFound("resolutionNote.specified=false");
    }

    @Test
    @Transactional
    public void getAllSuspensionHistoriesByUnsuspensionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        suspensionHistoryRepository.saveAndFlush(suspensionHistory);

        // Get all the suspensionHistoryList where unsuspensionDate equals to DEFAULT_UNSUSPENSION_DATE
        defaultSuspensionHistoryShouldBeFound("unsuspensionDate.equals=" + DEFAULT_UNSUSPENSION_DATE);

        // Get all the suspensionHistoryList where unsuspensionDate equals to UPDATED_UNSUSPENSION_DATE
        defaultSuspensionHistoryShouldNotBeFound("unsuspensionDate.equals=" + UPDATED_UNSUSPENSION_DATE);
    }

    @Test
    @Transactional
    public void getAllSuspensionHistoriesByUnsuspensionDateIsInShouldWork() throws Exception {
        // Initialize the database
        suspensionHistoryRepository.saveAndFlush(suspensionHistory);

        // Get all the suspensionHistoryList where unsuspensionDate in DEFAULT_UNSUSPENSION_DATE or UPDATED_UNSUSPENSION_DATE
        defaultSuspensionHistoryShouldBeFound("unsuspensionDate.in=" + DEFAULT_UNSUSPENSION_DATE + "," + UPDATED_UNSUSPENSION_DATE);

        // Get all the suspensionHistoryList where unsuspensionDate equals to UPDATED_UNSUSPENSION_DATE
        defaultSuspensionHistoryShouldNotBeFound("unsuspensionDate.in=" + UPDATED_UNSUSPENSION_DATE);
    }

    @Test
    @Transactional
    public void getAllSuspensionHistoriesByUnsuspensionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        suspensionHistoryRepository.saveAndFlush(suspensionHistory);

        // Get all the suspensionHistoryList where unsuspensionDate is not null
        defaultSuspensionHistoryShouldBeFound("unsuspensionDate.specified=true");

        // Get all the suspensionHistoryList where unsuspensionDate is null
        defaultSuspensionHistoryShouldNotBeFound("unsuspensionDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllSuspensionHistoriesByUnsuspensionDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        suspensionHistoryRepository.saveAndFlush(suspensionHistory);

        // Get all the suspensionHistoryList where unsuspensionDate greater than or equals to DEFAULT_UNSUSPENSION_DATE
        defaultSuspensionHistoryShouldBeFound("unsuspensionDate.greaterOrEqualThan=" + DEFAULT_UNSUSPENSION_DATE);

        // Get all the suspensionHistoryList where unsuspensionDate greater than or equals to UPDATED_UNSUSPENSION_DATE
        defaultSuspensionHistoryShouldNotBeFound("unsuspensionDate.greaterOrEqualThan=" + UPDATED_UNSUSPENSION_DATE);
    }

    @Test
    @Transactional
    public void getAllSuspensionHistoriesByUnsuspensionDateIsLessThanSomething() throws Exception {
        // Initialize the database
        suspensionHistoryRepository.saveAndFlush(suspensionHistory);

        // Get all the suspensionHistoryList where unsuspensionDate less than or equals to DEFAULT_UNSUSPENSION_DATE
        defaultSuspensionHistoryShouldNotBeFound("unsuspensionDate.lessThan=" + DEFAULT_UNSUSPENSION_DATE);

        // Get all the suspensionHistoryList where unsuspensionDate less than or equals to UPDATED_UNSUSPENSION_DATE
        defaultSuspensionHistoryShouldBeFound("unsuspensionDate.lessThan=" + UPDATED_UNSUSPENSION_DATE);
    }


    @Test
    @Transactional
    public void getAllSuspensionHistoriesByProfileIsEqualToSomething() throws Exception {
        // Initialize the database
        Profile profile = ProfileResourceIntTest.createEntity(em);
        em.persist(profile);
        em.flush();
        suspensionHistory.setProfile(profile);
        suspensionHistoryRepository.saveAndFlush(suspensionHistory);
        Long profileId = profile.getId();

        // Get all the suspensionHistoryList where profile equals to profileId
        defaultSuspensionHistoryShouldBeFound("profileId.equals=" + profileId);

        // Get all the suspensionHistoryList where profile equals to profileId + 1
        defaultSuspensionHistoryShouldNotBeFound("profileId.equals=" + (profileId + 1));
    }


    @Test
    @Transactional
    public void getAllSuspensionHistoriesBySuspendedByIsEqualToSomething() throws Exception {
        // Initialize the database
        Profile suspendedBy = ProfileResourceIntTest.createEntity(em);
        em.persist(suspendedBy);
        em.flush();
        suspensionHistory.setSuspendedBy(suspendedBy);
        suspensionHistoryRepository.saveAndFlush(suspensionHistory);
        Long suspendedById = suspendedBy.getId();

        // Get all the suspensionHistoryList where suspendedBy equals to suspendedById
        defaultSuspensionHistoryShouldBeFound("suspendedById.equals=" + suspendedById);

        // Get all the suspensionHistoryList where suspendedBy equals to suspendedById + 1
        defaultSuspensionHistoryShouldNotBeFound("suspendedById.equals=" + (suspendedById + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultSuspensionHistoryShouldBeFound(String filter) throws Exception {
        restSuspensionHistoryMockMvc.perform(get("/api/suspension-histories?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(suspensionHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].suspendedDate").value(hasItem(sameInstant(DEFAULT_SUSPENDED_DATE))))
            .andExpect(jsonPath("$.[*].suspensionType").value(hasItem(DEFAULT_SUSPENSION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].suspendedReason").value(hasItem(DEFAULT_SUSPENDED_REASON.toString())))
            .andExpect(jsonPath("$.[*].resolutionNote").value(hasItem(DEFAULT_RESOLUTION_NOTE.toString())))
            .andExpect(jsonPath("$.[*].unsuspensionDate").value(hasItem(sameInstant(DEFAULT_UNSUSPENSION_DATE))));

        // Check, that the count call also returns 1
        restSuspensionHistoryMockMvc.perform(get("/api/suspension-histories/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultSuspensionHistoryShouldNotBeFound(String filter) throws Exception {
        restSuspensionHistoryMockMvc.perform(get("/api/suspension-histories?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSuspensionHistoryMockMvc.perform(get("/api/suspension-histories/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingSuspensionHistory() throws Exception {
        // Get the suspensionHistory
        restSuspensionHistoryMockMvc.perform(get("/api/suspension-histories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSuspensionHistory() throws Exception {
        // Initialize the database
        suspensionHistoryRepository.saveAndFlush(suspensionHistory);

        int databaseSizeBeforeUpdate = suspensionHistoryRepository.findAll().size();

        // Update the suspensionHistory
        SuspensionHistory updatedSuspensionHistory = suspensionHistoryRepository.findById(suspensionHistory.getId()).get();
        // Disconnect from session so that the updates on updatedSuspensionHistory are not directly saved in db
        em.detach(updatedSuspensionHistory);
        updatedSuspensionHistory
            .suspendedDate(UPDATED_SUSPENDED_DATE)
            .suspensionType(UPDATED_SUSPENSION_TYPE)
            .suspendedReason(UPDATED_SUSPENDED_REASON)
            .resolutionNote(UPDATED_RESOLUTION_NOTE)
            .unsuspensionDate(UPDATED_UNSUSPENSION_DATE);
        SuspensionHistoryDTO suspensionHistoryDTO = suspensionHistoryMapper.toDto(updatedSuspensionHistory);

        restSuspensionHistoryMockMvc.perform(put("/api/suspension-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(suspensionHistoryDTO)))
            .andExpect(status().isOk());

        // Validate the SuspensionHistory in the database
        List<SuspensionHistory> suspensionHistoryList = suspensionHistoryRepository.findAll();
        assertThat(suspensionHistoryList).hasSize(databaseSizeBeforeUpdate);
        SuspensionHistory testSuspensionHistory = suspensionHistoryList.get(suspensionHistoryList.size() - 1);
        assertThat(testSuspensionHistory.getSuspendedDate()).isEqualTo(UPDATED_SUSPENDED_DATE);
        assertThat(testSuspensionHistory.getSuspensionType()).isEqualTo(UPDATED_SUSPENSION_TYPE);
        assertThat(testSuspensionHistory.getSuspendedReason()).isEqualTo(UPDATED_SUSPENDED_REASON);
        assertThat(testSuspensionHistory.getResolutionNote()).isEqualTo(UPDATED_RESOLUTION_NOTE);
        assertThat(testSuspensionHistory.getUnsuspensionDate()).isEqualTo(UPDATED_UNSUSPENSION_DATE);

        // Validate the SuspensionHistory in Elasticsearch
        verify(mockSuspensionHistorySearchRepository, times(1)).save(testSuspensionHistory);
    }

    @Test
    @Transactional
    public void updateNonExistingSuspensionHistory() throws Exception {
        int databaseSizeBeforeUpdate = suspensionHistoryRepository.findAll().size();

        // Create the SuspensionHistory
        SuspensionHistoryDTO suspensionHistoryDTO = suspensionHistoryMapper.toDto(suspensionHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSuspensionHistoryMockMvc.perform(put("/api/suspension-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(suspensionHistoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SuspensionHistory in the database
        List<SuspensionHistory> suspensionHistoryList = suspensionHistoryRepository.findAll();
        assertThat(suspensionHistoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SuspensionHistory in Elasticsearch
        verify(mockSuspensionHistorySearchRepository, times(0)).save(suspensionHistory);
    }

    @Test
    @Transactional
    public void deleteSuspensionHistory() throws Exception {
        // Initialize the database
        suspensionHistoryRepository.saveAndFlush(suspensionHistory);

        int databaseSizeBeforeDelete = suspensionHistoryRepository.findAll().size();

        // Get the suspensionHistory
        restSuspensionHistoryMockMvc.perform(delete("/api/suspension-histories/{id}", suspensionHistory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SuspensionHistory> suspensionHistoryList = suspensionHistoryRepository.findAll();
        assertThat(suspensionHistoryList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the SuspensionHistory in Elasticsearch
        verify(mockSuspensionHistorySearchRepository, times(1)).deleteById(suspensionHistory.getId());
    }

    @Test
    @Transactional
    public void searchSuspensionHistory() throws Exception {
        // Initialize the database
        suspensionHistoryRepository.saveAndFlush(suspensionHistory);
        when(mockSuspensionHistorySearchRepository.search(queryStringQuery("id:" + suspensionHistory.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(suspensionHistory), PageRequest.of(0, 1), 1));
        // Search the suspensionHistory
        restSuspensionHistoryMockMvc.perform(get("/api/_search/suspension-histories?query=id:" + suspensionHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(suspensionHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].suspendedDate").value(hasItem(sameInstant(DEFAULT_SUSPENDED_DATE))))
            .andExpect(jsonPath("$.[*].suspensionType").value(hasItem(DEFAULT_SUSPENSION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].suspendedReason").value(hasItem(DEFAULT_SUSPENDED_REASON)))
            .andExpect(jsonPath("$.[*].resolutionNote").value(hasItem(DEFAULT_RESOLUTION_NOTE)))
            .andExpect(jsonPath("$.[*].unsuspensionDate").value(hasItem(sameInstant(DEFAULT_UNSUSPENSION_DATE))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SuspensionHistory.class);
        SuspensionHistory suspensionHistory1 = new SuspensionHistory();
        suspensionHistory1.setId(1L);
        SuspensionHistory suspensionHistory2 = new SuspensionHistory();
        suspensionHistory2.setId(suspensionHistory1.getId());
        assertThat(suspensionHistory1).isEqualTo(suspensionHistory2);
        suspensionHistory2.setId(2L);
        assertThat(suspensionHistory1).isNotEqualTo(suspensionHistory2);
        suspensionHistory1.setId(null);
        assertThat(suspensionHistory1).isNotEqualTo(suspensionHistory2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SuspensionHistoryDTO.class);
        SuspensionHistoryDTO suspensionHistoryDTO1 = new SuspensionHistoryDTO();
        suspensionHistoryDTO1.setId(1L);
        SuspensionHistoryDTO suspensionHistoryDTO2 = new SuspensionHistoryDTO();
        assertThat(suspensionHistoryDTO1).isNotEqualTo(suspensionHistoryDTO2);
        suspensionHistoryDTO2.setId(suspensionHistoryDTO1.getId());
        assertThat(suspensionHistoryDTO1).isEqualTo(suspensionHistoryDTO2);
        suspensionHistoryDTO2.setId(2L);
        assertThat(suspensionHistoryDTO1).isNotEqualTo(suspensionHistoryDTO2);
        suspensionHistoryDTO1.setId(null);
        assertThat(suspensionHistoryDTO1).isNotEqualTo(suspensionHistoryDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(suspensionHistoryMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(suspensionHistoryMapper.fromId(null)).isNull();
    }
}
