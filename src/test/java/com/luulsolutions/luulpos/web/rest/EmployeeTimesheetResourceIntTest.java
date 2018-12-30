package com.luulsolutions.luulpos.web.rest;

import com.luulsolutions.luulpos.LuulposApp;

import com.luulsolutions.luulpos.domain.EmployeeTimesheet;
import com.luulsolutions.luulpos.domain.Profile;
import com.luulsolutions.luulpos.domain.Shop;
import com.luulsolutions.luulpos.repository.EmployeeTimesheetRepository;
import com.luulsolutions.luulpos.repository.search.EmployeeTimesheetSearchRepository;
import com.luulsolutions.luulpos.service.EmployeeTimesheetService;
import com.luulsolutions.luulpos.service.dto.EmployeeTimesheetDTO;
import com.luulsolutions.luulpos.service.mapper.EmployeeTimesheetMapper;
import com.luulsolutions.luulpos.web.rest.errors.ExceptionTranslator;
import com.luulsolutions.luulpos.service.dto.EmployeeTimesheetCriteria;
import com.luulsolutions.luulpos.service.EmployeeTimesheetQueryService;

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
 * Test class for the EmployeeTimesheetResource REST controller.
 *
 * @see EmployeeTimesheetResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LuulposApp.class)
public class EmployeeTimesheetResourceIntTest {

    private static final ZonedDateTime DEFAULT_CHECKIN_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CHECKIN_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_CHECK_OUT_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CHECK_OUT_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_REGULAR_HOURS_WORKED = 1;
    private static final Integer UPDATED_REGULAR_HOURS_WORKED = 2;

    private static final Integer DEFAULT_OVER_TIME_HOURS_WORKED = 1;
    private static final Integer UPDATED_OVER_TIME_HOURS_WORKED = 2;

    private static final BigDecimal DEFAULT_PAY = new BigDecimal(1);
    private static final BigDecimal UPDATED_PAY = new BigDecimal(2);

    @Autowired
    private EmployeeTimesheetRepository employeeTimesheetRepository;

    @Autowired
    private EmployeeTimesheetMapper employeeTimesheetMapper;

    @Autowired
    private EmployeeTimesheetService employeeTimesheetService;

    /**
     * This repository is mocked in the com.luulsolutions.luulpos.repository.search test package.
     *
     * @see com.luulsolutions.luulpos.repository.search.EmployeeTimesheetSearchRepositoryMockConfiguration
     */
    @Autowired
    private EmployeeTimesheetSearchRepository mockEmployeeTimesheetSearchRepository;

    @Autowired
    private EmployeeTimesheetQueryService employeeTimesheetQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEmployeeTimesheetMockMvc;

    private EmployeeTimesheet employeeTimesheet;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EmployeeTimesheetResource employeeTimesheetResource = new EmployeeTimesheetResource(employeeTimesheetService, employeeTimesheetQueryService);
        this.restEmployeeTimesheetMockMvc = MockMvcBuilders.standaloneSetup(employeeTimesheetResource)
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
    public static EmployeeTimesheet createEntity(EntityManager em) {
        EmployeeTimesheet employeeTimesheet = new EmployeeTimesheet()
            .checkinTime(DEFAULT_CHECKIN_TIME)
            .checkOutTime(DEFAULT_CHECK_OUT_TIME)
            .regularHoursWorked(DEFAULT_REGULAR_HOURS_WORKED)
            .overTimeHoursWorked(DEFAULT_OVER_TIME_HOURS_WORKED)
            .pay(DEFAULT_PAY);
        return employeeTimesheet;
    }

    @Before
    public void initTest() {
        employeeTimesheet = createEntity(em);
    }

    @Test
    @Transactional
    public void createEmployeeTimesheet() throws Exception {
        int databaseSizeBeforeCreate = employeeTimesheetRepository.findAll().size();

        // Create the EmployeeTimesheet
        EmployeeTimesheetDTO employeeTimesheetDTO = employeeTimesheetMapper.toDto(employeeTimesheet);
        restEmployeeTimesheetMockMvc.perform(post("/api/employee-timesheets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employeeTimesheetDTO)))
            .andExpect(status().isCreated());

        // Validate the EmployeeTimesheet in the database
        List<EmployeeTimesheet> employeeTimesheetList = employeeTimesheetRepository.findAll();
        assertThat(employeeTimesheetList).hasSize(databaseSizeBeforeCreate + 1);
        EmployeeTimesheet testEmployeeTimesheet = employeeTimesheetList.get(employeeTimesheetList.size() - 1);
        assertThat(testEmployeeTimesheet.getCheckinTime()).isEqualTo(DEFAULT_CHECKIN_TIME);
        assertThat(testEmployeeTimesheet.getCheckOutTime()).isEqualTo(DEFAULT_CHECK_OUT_TIME);
        assertThat(testEmployeeTimesheet.getRegularHoursWorked()).isEqualTo(DEFAULT_REGULAR_HOURS_WORKED);
        assertThat(testEmployeeTimesheet.getOverTimeHoursWorked()).isEqualTo(DEFAULT_OVER_TIME_HOURS_WORKED);
        assertThat(testEmployeeTimesheet.getPay()).isEqualTo(DEFAULT_PAY);

        // Validate the EmployeeTimesheet in Elasticsearch
        verify(mockEmployeeTimesheetSearchRepository, times(1)).save(testEmployeeTimesheet);
    }

    @Test
    @Transactional
    public void createEmployeeTimesheetWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = employeeTimesheetRepository.findAll().size();

        // Create the EmployeeTimesheet with an existing ID
        employeeTimesheet.setId(1L);
        EmployeeTimesheetDTO employeeTimesheetDTO = employeeTimesheetMapper.toDto(employeeTimesheet);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployeeTimesheetMockMvc.perform(post("/api/employee-timesheets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employeeTimesheetDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EmployeeTimesheet in the database
        List<EmployeeTimesheet> employeeTimesheetList = employeeTimesheetRepository.findAll();
        assertThat(employeeTimesheetList).hasSize(databaseSizeBeforeCreate);

        // Validate the EmployeeTimesheet in Elasticsearch
        verify(mockEmployeeTimesheetSearchRepository, times(0)).save(employeeTimesheet);
    }

    @Test
    @Transactional
    public void getAllEmployeeTimesheets() throws Exception {
        // Initialize the database
        employeeTimesheetRepository.saveAndFlush(employeeTimesheet);

        // Get all the employeeTimesheetList
        restEmployeeTimesheetMockMvc.perform(get("/api/employee-timesheets?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employeeTimesheet.getId().intValue())))
            .andExpect(jsonPath("$.[*].checkinTime").value(hasItem(sameInstant(DEFAULT_CHECKIN_TIME))))
            .andExpect(jsonPath("$.[*].checkOutTime").value(hasItem(sameInstant(DEFAULT_CHECK_OUT_TIME))))
            .andExpect(jsonPath("$.[*].regularHoursWorked").value(hasItem(DEFAULT_REGULAR_HOURS_WORKED)))
            .andExpect(jsonPath("$.[*].overTimeHoursWorked").value(hasItem(DEFAULT_OVER_TIME_HOURS_WORKED)))
            .andExpect(jsonPath("$.[*].pay").value(hasItem(DEFAULT_PAY.intValue())));
    }
    
    @Test
    @Transactional
    public void getEmployeeTimesheet() throws Exception {
        // Initialize the database
        employeeTimesheetRepository.saveAndFlush(employeeTimesheet);

        // Get the employeeTimesheet
        restEmployeeTimesheetMockMvc.perform(get("/api/employee-timesheets/{id}", employeeTimesheet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(employeeTimesheet.getId().intValue()))
            .andExpect(jsonPath("$.checkinTime").value(sameInstant(DEFAULT_CHECKIN_TIME)))
            .andExpect(jsonPath("$.checkOutTime").value(sameInstant(DEFAULT_CHECK_OUT_TIME)))
            .andExpect(jsonPath("$.regularHoursWorked").value(DEFAULT_REGULAR_HOURS_WORKED))
            .andExpect(jsonPath("$.overTimeHoursWorked").value(DEFAULT_OVER_TIME_HOURS_WORKED))
            .andExpect(jsonPath("$.pay").value(DEFAULT_PAY.intValue()));
    }

    @Test
    @Transactional
    public void getAllEmployeeTimesheetsByCheckinTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeTimesheetRepository.saveAndFlush(employeeTimesheet);

        // Get all the employeeTimesheetList where checkinTime equals to DEFAULT_CHECKIN_TIME
        defaultEmployeeTimesheetShouldBeFound("checkinTime.equals=" + DEFAULT_CHECKIN_TIME);

        // Get all the employeeTimesheetList where checkinTime equals to UPDATED_CHECKIN_TIME
        defaultEmployeeTimesheetShouldNotBeFound("checkinTime.equals=" + UPDATED_CHECKIN_TIME);
    }

    @Test
    @Transactional
    public void getAllEmployeeTimesheetsByCheckinTimeIsInShouldWork() throws Exception {
        // Initialize the database
        employeeTimesheetRepository.saveAndFlush(employeeTimesheet);

        // Get all the employeeTimesheetList where checkinTime in DEFAULT_CHECKIN_TIME or UPDATED_CHECKIN_TIME
        defaultEmployeeTimesheetShouldBeFound("checkinTime.in=" + DEFAULT_CHECKIN_TIME + "," + UPDATED_CHECKIN_TIME);

        // Get all the employeeTimesheetList where checkinTime equals to UPDATED_CHECKIN_TIME
        defaultEmployeeTimesheetShouldNotBeFound("checkinTime.in=" + UPDATED_CHECKIN_TIME);
    }

    @Test
    @Transactional
    public void getAllEmployeeTimesheetsByCheckinTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeTimesheetRepository.saveAndFlush(employeeTimesheet);

        // Get all the employeeTimesheetList where checkinTime is not null
        defaultEmployeeTimesheetShouldBeFound("checkinTime.specified=true");

        // Get all the employeeTimesheetList where checkinTime is null
        defaultEmployeeTimesheetShouldNotBeFound("checkinTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllEmployeeTimesheetsByCheckinTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeTimesheetRepository.saveAndFlush(employeeTimesheet);

        // Get all the employeeTimesheetList where checkinTime greater than or equals to DEFAULT_CHECKIN_TIME
        defaultEmployeeTimesheetShouldBeFound("checkinTime.greaterOrEqualThan=" + DEFAULT_CHECKIN_TIME);

        // Get all the employeeTimesheetList where checkinTime greater than or equals to UPDATED_CHECKIN_TIME
        defaultEmployeeTimesheetShouldNotBeFound("checkinTime.greaterOrEqualThan=" + UPDATED_CHECKIN_TIME);
    }

    @Test
    @Transactional
    public void getAllEmployeeTimesheetsByCheckinTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        employeeTimesheetRepository.saveAndFlush(employeeTimesheet);

        // Get all the employeeTimesheetList where checkinTime less than or equals to DEFAULT_CHECKIN_TIME
        defaultEmployeeTimesheetShouldNotBeFound("checkinTime.lessThan=" + DEFAULT_CHECKIN_TIME);

        // Get all the employeeTimesheetList where checkinTime less than or equals to UPDATED_CHECKIN_TIME
        defaultEmployeeTimesheetShouldBeFound("checkinTime.lessThan=" + UPDATED_CHECKIN_TIME);
    }


    @Test
    @Transactional
    public void getAllEmployeeTimesheetsByCheckOutTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeTimesheetRepository.saveAndFlush(employeeTimesheet);

        // Get all the employeeTimesheetList where checkOutTime equals to DEFAULT_CHECK_OUT_TIME
        defaultEmployeeTimesheetShouldBeFound("checkOutTime.equals=" + DEFAULT_CHECK_OUT_TIME);

        // Get all the employeeTimesheetList where checkOutTime equals to UPDATED_CHECK_OUT_TIME
        defaultEmployeeTimesheetShouldNotBeFound("checkOutTime.equals=" + UPDATED_CHECK_OUT_TIME);
    }

    @Test
    @Transactional
    public void getAllEmployeeTimesheetsByCheckOutTimeIsInShouldWork() throws Exception {
        // Initialize the database
        employeeTimesheetRepository.saveAndFlush(employeeTimesheet);

        // Get all the employeeTimesheetList where checkOutTime in DEFAULT_CHECK_OUT_TIME or UPDATED_CHECK_OUT_TIME
        defaultEmployeeTimesheetShouldBeFound("checkOutTime.in=" + DEFAULT_CHECK_OUT_TIME + "," + UPDATED_CHECK_OUT_TIME);

        // Get all the employeeTimesheetList where checkOutTime equals to UPDATED_CHECK_OUT_TIME
        defaultEmployeeTimesheetShouldNotBeFound("checkOutTime.in=" + UPDATED_CHECK_OUT_TIME);
    }

    @Test
    @Transactional
    public void getAllEmployeeTimesheetsByCheckOutTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeTimesheetRepository.saveAndFlush(employeeTimesheet);

        // Get all the employeeTimesheetList where checkOutTime is not null
        defaultEmployeeTimesheetShouldBeFound("checkOutTime.specified=true");

        // Get all the employeeTimesheetList where checkOutTime is null
        defaultEmployeeTimesheetShouldNotBeFound("checkOutTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllEmployeeTimesheetsByCheckOutTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeTimesheetRepository.saveAndFlush(employeeTimesheet);

        // Get all the employeeTimesheetList where checkOutTime greater than or equals to DEFAULT_CHECK_OUT_TIME
        defaultEmployeeTimesheetShouldBeFound("checkOutTime.greaterOrEqualThan=" + DEFAULT_CHECK_OUT_TIME);

        // Get all the employeeTimesheetList where checkOutTime greater than or equals to UPDATED_CHECK_OUT_TIME
        defaultEmployeeTimesheetShouldNotBeFound("checkOutTime.greaterOrEqualThan=" + UPDATED_CHECK_OUT_TIME);
    }

    @Test
    @Transactional
    public void getAllEmployeeTimesheetsByCheckOutTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        employeeTimesheetRepository.saveAndFlush(employeeTimesheet);

        // Get all the employeeTimesheetList where checkOutTime less than or equals to DEFAULT_CHECK_OUT_TIME
        defaultEmployeeTimesheetShouldNotBeFound("checkOutTime.lessThan=" + DEFAULT_CHECK_OUT_TIME);

        // Get all the employeeTimesheetList where checkOutTime less than or equals to UPDATED_CHECK_OUT_TIME
        defaultEmployeeTimesheetShouldBeFound("checkOutTime.lessThan=" + UPDATED_CHECK_OUT_TIME);
    }


    @Test
    @Transactional
    public void getAllEmployeeTimesheetsByRegularHoursWorkedIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeTimesheetRepository.saveAndFlush(employeeTimesheet);

        // Get all the employeeTimesheetList where regularHoursWorked equals to DEFAULT_REGULAR_HOURS_WORKED
        defaultEmployeeTimesheetShouldBeFound("regularHoursWorked.equals=" + DEFAULT_REGULAR_HOURS_WORKED);

        // Get all the employeeTimesheetList where regularHoursWorked equals to UPDATED_REGULAR_HOURS_WORKED
        defaultEmployeeTimesheetShouldNotBeFound("regularHoursWorked.equals=" + UPDATED_REGULAR_HOURS_WORKED);
    }

    @Test
    @Transactional
    public void getAllEmployeeTimesheetsByRegularHoursWorkedIsInShouldWork() throws Exception {
        // Initialize the database
        employeeTimesheetRepository.saveAndFlush(employeeTimesheet);

        // Get all the employeeTimesheetList where regularHoursWorked in DEFAULT_REGULAR_HOURS_WORKED or UPDATED_REGULAR_HOURS_WORKED
        defaultEmployeeTimesheetShouldBeFound("regularHoursWorked.in=" + DEFAULT_REGULAR_HOURS_WORKED + "," + UPDATED_REGULAR_HOURS_WORKED);

        // Get all the employeeTimesheetList where regularHoursWorked equals to UPDATED_REGULAR_HOURS_WORKED
        defaultEmployeeTimesheetShouldNotBeFound("regularHoursWorked.in=" + UPDATED_REGULAR_HOURS_WORKED);
    }

    @Test
    @Transactional
    public void getAllEmployeeTimesheetsByRegularHoursWorkedIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeTimesheetRepository.saveAndFlush(employeeTimesheet);

        // Get all the employeeTimesheetList where regularHoursWorked is not null
        defaultEmployeeTimesheetShouldBeFound("regularHoursWorked.specified=true");

        // Get all the employeeTimesheetList where regularHoursWorked is null
        defaultEmployeeTimesheetShouldNotBeFound("regularHoursWorked.specified=false");
    }

    @Test
    @Transactional
    public void getAllEmployeeTimesheetsByRegularHoursWorkedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeTimesheetRepository.saveAndFlush(employeeTimesheet);

        // Get all the employeeTimesheetList where regularHoursWorked greater than or equals to DEFAULT_REGULAR_HOURS_WORKED
        defaultEmployeeTimesheetShouldBeFound("regularHoursWorked.greaterOrEqualThan=" + DEFAULT_REGULAR_HOURS_WORKED);

        // Get all the employeeTimesheetList where regularHoursWorked greater than or equals to UPDATED_REGULAR_HOURS_WORKED
        defaultEmployeeTimesheetShouldNotBeFound("regularHoursWorked.greaterOrEqualThan=" + UPDATED_REGULAR_HOURS_WORKED);
    }

    @Test
    @Transactional
    public void getAllEmployeeTimesheetsByRegularHoursWorkedIsLessThanSomething() throws Exception {
        // Initialize the database
        employeeTimesheetRepository.saveAndFlush(employeeTimesheet);

        // Get all the employeeTimesheetList where regularHoursWorked less than or equals to DEFAULT_REGULAR_HOURS_WORKED
        defaultEmployeeTimesheetShouldNotBeFound("regularHoursWorked.lessThan=" + DEFAULT_REGULAR_HOURS_WORKED);

        // Get all the employeeTimesheetList where regularHoursWorked less than or equals to UPDATED_REGULAR_HOURS_WORKED
        defaultEmployeeTimesheetShouldBeFound("regularHoursWorked.lessThan=" + UPDATED_REGULAR_HOURS_WORKED);
    }


    @Test
    @Transactional
    public void getAllEmployeeTimesheetsByOverTimeHoursWorkedIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeTimesheetRepository.saveAndFlush(employeeTimesheet);

        // Get all the employeeTimesheetList where overTimeHoursWorked equals to DEFAULT_OVER_TIME_HOURS_WORKED
        defaultEmployeeTimesheetShouldBeFound("overTimeHoursWorked.equals=" + DEFAULT_OVER_TIME_HOURS_WORKED);

        // Get all the employeeTimesheetList where overTimeHoursWorked equals to UPDATED_OVER_TIME_HOURS_WORKED
        defaultEmployeeTimesheetShouldNotBeFound("overTimeHoursWorked.equals=" + UPDATED_OVER_TIME_HOURS_WORKED);
    }

    @Test
    @Transactional
    public void getAllEmployeeTimesheetsByOverTimeHoursWorkedIsInShouldWork() throws Exception {
        // Initialize the database
        employeeTimesheetRepository.saveAndFlush(employeeTimesheet);

        // Get all the employeeTimesheetList where overTimeHoursWorked in DEFAULT_OVER_TIME_HOURS_WORKED or UPDATED_OVER_TIME_HOURS_WORKED
        defaultEmployeeTimesheetShouldBeFound("overTimeHoursWorked.in=" + DEFAULT_OVER_TIME_HOURS_WORKED + "," + UPDATED_OVER_TIME_HOURS_WORKED);

        // Get all the employeeTimesheetList where overTimeHoursWorked equals to UPDATED_OVER_TIME_HOURS_WORKED
        defaultEmployeeTimesheetShouldNotBeFound("overTimeHoursWorked.in=" + UPDATED_OVER_TIME_HOURS_WORKED);
    }

    @Test
    @Transactional
    public void getAllEmployeeTimesheetsByOverTimeHoursWorkedIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeTimesheetRepository.saveAndFlush(employeeTimesheet);

        // Get all the employeeTimesheetList where overTimeHoursWorked is not null
        defaultEmployeeTimesheetShouldBeFound("overTimeHoursWorked.specified=true");

        // Get all the employeeTimesheetList where overTimeHoursWorked is null
        defaultEmployeeTimesheetShouldNotBeFound("overTimeHoursWorked.specified=false");
    }

    @Test
    @Transactional
    public void getAllEmployeeTimesheetsByOverTimeHoursWorkedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeTimesheetRepository.saveAndFlush(employeeTimesheet);

        // Get all the employeeTimesheetList where overTimeHoursWorked greater than or equals to DEFAULT_OVER_TIME_HOURS_WORKED
        defaultEmployeeTimesheetShouldBeFound("overTimeHoursWorked.greaterOrEqualThan=" + DEFAULT_OVER_TIME_HOURS_WORKED);

        // Get all the employeeTimesheetList where overTimeHoursWorked greater than or equals to UPDATED_OVER_TIME_HOURS_WORKED
        defaultEmployeeTimesheetShouldNotBeFound("overTimeHoursWorked.greaterOrEqualThan=" + UPDATED_OVER_TIME_HOURS_WORKED);
    }

    @Test
    @Transactional
    public void getAllEmployeeTimesheetsByOverTimeHoursWorkedIsLessThanSomething() throws Exception {
        // Initialize the database
        employeeTimesheetRepository.saveAndFlush(employeeTimesheet);

        // Get all the employeeTimesheetList where overTimeHoursWorked less than or equals to DEFAULT_OVER_TIME_HOURS_WORKED
        defaultEmployeeTimesheetShouldNotBeFound("overTimeHoursWorked.lessThan=" + DEFAULT_OVER_TIME_HOURS_WORKED);

        // Get all the employeeTimesheetList where overTimeHoursWorked less than or equals to UPDATED_OVER_TIME_HOURS_WORKED
        defaultEmployeeTimesheetShouldBeFound("overTimeHoursWorked.lessThan=" + UPDATED_OVER_TIME_HOURS_WORKED);
    }


    @Test
    @Transactional
    public void getAllEmployeeTimesheetsByPayIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeTimesheetRepository.saveAndFlush(employeeTimesheet);

        // Get all the employeeTimesheetList where pay equals to DEFAULT_PAY
        defaultEmployeeTimesheetShouldBeFound("pay.equals=" + DEFAULT_PAY);

        // Get all the employeeTimesheetList where pay equals to UPDATED_PAY
        defaultEmployeeTimesheetShouldNotBeFound("pay.equals=" + UPDATED_PAY);
    }

    @Test
    @Transactional
    public void getAllEmployeeTimesheetsByPayIsInShouldWork() throws Exception {
        // Initialize the database
        employeeTimesheetRepository.saveAndFlush(employeeTimesheet);

        // Get all the employeeTimesheetList where pay in DEFAULT_PAY or UPDATED_PAY
        defaultEmployeeTimesheetShouldBeFound("pay.in=" + DEFAULT_PAY + "," + UPDATED_PAY);

        // Get all the employeeTimesheetList where pay equals to UPDATED_PAY
        defaultEmployeeTimesheetShouldNotBeFound("pay.in=" + UPDATED_PAY);
    }

    @Test
    @Transactional
    public void getAllEmployeeTimesheetsByPayIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeTimesheetRepository.saveAndFlush(employeeTimesheet);

        // Get all the employeeTimesheetList where pay is not null
        defaultEmployeeTimesheetShouldBeFound("pay.specified=true");

        // Get all the employeeTimesheetList where pay is null
        defaultEmployeeTimesheetShouldNotBeFound("pay.specified=false");
    }

    @Test
    @Transactional
    public void getAllEmployeeTimesheetsByProfileIsEqualToSomething() throws Exception {
        // Initialize the database
        Profile profile = ProfileResourceIntTest.createEntity(em);
        em.persist(profile);
        em.flush();
        employeeTimesheet.setProfile(profile);
        employeeTimesheetRepository.saveAndFlush(employeeTimesheet);
        Long profileId = profile.getId();

        // Get all the employeeTimesheetList where profile equals to profileId
        defaultEmployeeTimesheetShouldBeFound("profileId.equals=" + profileId);

        // Get all the employeeTimesheetList where profile equals to profileId + 1
        defaultEmployeeTimesheetShouldNotBeFound("profileId.equals=" + (profileId + 1));
    }


    @Test
    @Transactional
    public void getAllEmployeeTimesheetsByShopIsEqualToSomething() throws Exception {
        // Initialize the database
        Shop shop = ShopResourceIntTest.createEntity(em);
        em.persist(shop);
        em.flush();
        employeeTimesheet.setShop(shop);
        employeeTimesheetRepository.saveAndFlush(employeeTimesheet);
        Long shopId = shop.getId();

        // Get all the employeeTimesheetList where shop equals to shopId
        defaultEmployeeTimesheetShouldBeFound("shopId.equals=" + shopId);

        // Get all the employeeTimesheetList where shop equals to shopId + 1
        defaultEmployeeTimesheetShouldNotBeFound("shopId.equals=" + (shopId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultEmployeeTimesheetShouldBeFound(String filter) throws Exception {
        restEmployeeTimesheetMockMvc.perform(get("/api/employee-timesheets?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employeeTimesheet.getId().intValue())))
            .andExpect(jsonPath("$.[*].checkinTime").value(hasItem(sameInstant(DEFAULT_CHECKIN_TIME))))
            .andExpect(jsonPath("$.[*].checkOutTime").value(hasItem(sameInstant(DEFAULT_CHECK_OUT_TIME))))
            .andExpect(jsonPath("$.[*].regularHoursWorked").value(hasItem(DEFAULT_REGULAR_HOURS_WORKED)))
            .andExpect(jsonPath("$.[*].overTimeHoursWorked").value(hasItem(DEFAULT_OVER_TIME_HOURS_WORKED)))
            .andExpect(jsonPath("$.[*].pay").value(hasItem(DEFAULT_PAY.intValue())));

        // Check, that the count call also returns 1
        restEmployeeTimesheetMockMvc.perform(get("/api/employee-timesheets/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultEmployeeTimesheetShouldNotBeFound(String filter) throws Exception {
        restEmployeeTimesheetMockMvc.perform(get("/api/employee-timesheets?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEmployeeTimesheetMockMvc.perform(get("/api/employee-timesheets/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingEmployeeTimesheet() throws Exception {
        // Get the employeeTimesheet
        restEmployeeTimesheetMockMvc.perform(get("/api/employee-timesheets/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEmployeeTimesheet() throws Exception {
        // Initialize the database
        employeeTimesheetRepository.saveAndFlush(employeeTimesheet);

        int databaseSizeBeforeUpdate = employeeTimesheetRepository.findAll().size();

        // Update the employeeTimesheet
        EmployeeTimesheet updatedEmployeeTimesheet = employeeTimesheetRepository.findById(employeeTimesheet.getId()).get();
        // Disconnect from session so that the updates on updatedEmployeeTimesheet are not directly saved in db
        em.detach(updatedEmployeeTimesheet);
        updatedEmployeeTimesheet
            .checkinTime(UPDATED_CHECKIN_TIME)
            .checkOutTime(UPDATED_CHECK_OUT_TIME)
            .regularHoursWorked(UPDATED_REGULAR_HOURS_WORKED)
            .overTimeHoursWorked(UPDATED_OVER_TIME_HOURS_WORKED)
            .pay(UPDATED_PAY);
        EmployeeTimesheetDTO employeeTimesheetDTO = employeeTimesheetMapper.toDto(updatedEmployeeTimesheet);

        restEmployeeTimesheetMockMvc.perform(put("/api/employee-timesheets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employeeTimesheetDTO)))
            .andExpect(status().isOk());

        // Validate the EmployeeTimesheet in the database
        List<EmployeeTimesheet> employeeTimesheetList = employeeTimesheetRepository.findAll();
        assertThat(employeeTimesheetList).hasSize(databaseSizeBeforeUpdate);
        EmployeeTimesheet testEmployeeTimesheet = employeeTimesheetList.get(employeeTimesheetList.size() - 1);
        assertThat(testEmployeeTimesheet.getCheckinTime()).isEqualTo(UPDATED_CHECKIN_TIME);
        assertThat(testEmployeeTimesheet.getCheckOutTime()).isEqualTo(UPDATED_CHECK_OUT_TIME);
        assertThat(testEmployeeTimesheet.getRegularHoursWorked()).isEqualTo(UPDATED_REGULAR_HOURS_WORKED);
        assertThat(testEmployeeTimesheet.getOverTimeHoursWorked()).isEqualTo(UPDATED_OVER_TIME_HOURS_WORKED);
        assertThat(testEmployeeTimesheet.getPay()).isEqualTo(UPDATED_PAY);

        // Validate the EmployeeTimesheet in Elasticsearch
        verify(mockEmployeeTimesheetSearchRepository, times(1)).save(testEmployeeTimesheet);
    }

    @Test
    @Transactional
    public void updateNonExistingEmployeeTimesheet() throws Exception {
        int databaseSizeBeforeUpdate = employeeTimesheetRepository.findAll().size();

        // Create the EmployeeTimesheet
        EmployeeTimesheetDTO employeeTimesheetDTO = employeeTimesheetMapper.toDto(employeeTimesheet);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeTimesheetMockMvc.perform(put("/api/employee-timesheets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employeeTimesheetDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EmployeeTimesheet in the database
        List<EmployeeTimesheet> employeeTimesheetList = employeeTimesheetRepository.findAll();
        assertThat(employeeTimesheetList).hasSize(databaseSizeBeforeUpdate);

        // Validate the EmployeeTimesheet in Elasticsearch
        verify(mockEmployeeTimesheetSearchRepository, times(0)).save(employeeTimesheet);
    }

    @Test
    @Transactional
    public void deleteEmployeeTimesheet() throws Exception {
        // Initialize the database
        employeeTimesheetRepository.saveAndFlush(employeeTimesheet);

        int databaseSizeBeforeDelete = employeeTimesheetRepository.findAll().size();

        // Get the employeeTimesheet
        restEmployeeTimesheetMockMvc.perform(delete("/api/employee-timesheets/{id}", employeeTimesheet.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<EmployeeTimesheet> employeeTimesheetList = employeeTimesheetRepository.findAll();
        assertThat(employeeTimesheetList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the EmployeeTimesheet in Elasticsearch
        verify(mockEmployeeTimesheetSearchRepository, times(1)).deleteById(employeeTimesheet.getId());
    }

    @Test
    @Transactional
    public void searchEmployeeTimesheet() throws Exception {
        // Initialize the database
        employeeTimesheetRepository.saveAndFlush(employeeTimesheet);
        when(mockEmployeeTimesheetSearchRepository.search(queryStringQuery("id:" + employeeTimesheet.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(employeeTimesheet), PageRequest.of(0, 1), 1));
        // Search the employeeTimesheet
        restEmployeeTimesheetMockMvc.perform(get("/api/_search/employee-timesheets?query=id:" + employeeTimesheet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employeeTimesheet.getId().intValue())))
            .andExpect(jsonPath("$.[*].checkinTime").value(hasItem(sameInstant(DEFAULT_CHECKIN_TIME))))
            .andExpect(jsonPath("$.[*].checkOutTime").value(hasItem(sameInstant(DEFAULT_CHECK_OUT_TIME))))
            .andExpect(jsonPath("$.[*].regularHoursWorked").value(hasItem(DEFAULT_REGULAR_HOURS_WORKED)))
            .andExpect(jsonPath("$.[*].overTimeHoursWorked").value(hasItem(DEFAULT_OVER_TIME_HOURS_WORKED)))
            .andExpect(jsonPath("$.[*].pay").value(hasItem(DEFAULT_PAY.intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployeeTimesheet.class);
        EmployeeTimesheet employeeTimesheet1 = new EmployeeTimesheet();
        employeeTimesheet1.setId(1L);
        EmployeeTimesheet employeeTimesheet2 = new EmployeeTimesheet();
        employeeTimesheet2.setId(employeeTimesheet1.getId());
        assertThat(employeeTimesheet1).isEqualTo(employeeTimesheet2);
        employeeTimesheet2.setId(2L);
        assertThat(employeeTimesheet1).isNotEqualTo(employeeTimesheet2);
        employeeTimesheet1.setId(null);
        assertThat(employeeTimesheet1).isNotEqualTo(employeeTimesheet2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployeeTimesheetDTO.class);
        EmployeeTimesheetDTO employeeTimesheetDTO1 = new EmployeeTimesheetDTO();
        employeeTimesheetDTO1.setId(1L);
        EmployeeTimesheetDTO employeeTimesheetDTO2 = new EmployeeTimesheetDTO();
        assertThat(employeeTimesheetDTO1).isNotEqualTo(employeeTimesheetDTO2);
        employeeTimesheetDTO2.setId(employeeTimesheetDTO1.getId());
        assertThat(employeeTimesheetDTO1).isEqualTo(employeeTimesheetDTO2);
        employeeTimesheetDTO2.setId(2L);
        assertThat(employeeTimesheetDTO1).isNotEqualTo(employeeTimesheetDTO2);
        employeeTimesheetDTO1.setId(null);
        assertThat(employeeTimesheetDTO1).isNotEqualTo(employeeTimesheetDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(employeeTimesheetMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(employeeTimesheetMapper.fromId(null)).isNull();
    }
}
