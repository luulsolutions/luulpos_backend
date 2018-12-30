package com.luulsolutions.luulpos.web.rest;

import com.luulsolutions.luulpos.LuulposApp;

import com.luulsolutions.luulpos.domain.Profile;
import com.luulsolutions.luulpos.domain.User;
import com.luulsolutions.luulpos.domain.Shop;
import com.luulsolutions.luulpos.repository.ProfileRepository;
import com.luulsolutions.luulpos.repository.search.ProfileSearchRepository;
import com.luulsolutions.luulpos.service.ProfileService;
import com.luulsolutions.luulpos.service.dto.ProfileDTO;
import com.luulsolutions.luulpos.service.mapper.ProfileMapper;
import com.luulsolutions.luulpos.web.rest.errors.ExceptionTranslator;
import com.luulsolutions.luulpos.service.dto.ProfileCriteria;
import com.luulsolutions.luulpos.service.ProfileQueryService;

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
import java.time.LocalDate;
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

import com.luulsolutions.luulpos.domain.enumeration.ProfileType;
import com.luulsolutions.luulpos.domain.enumeration.Gender;
import com.luulsolutions.luulpos.domain.enumeration.ProfileStatus;
/**
 * Test class for the ProfileResource REST controller.
 *
 * @see ProfileResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LuulposApp.class)
public class ProfileResourceIntTest {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final ProfileType DEFAULT_USER_TYPE = ProfileType.SYSTEM_MANAGER;
    private static final ProfileType UPDATED_USER_TYPE = ProfileType.COMPANY_MANAGER;

    private static final LocalDate DEFAULT_DATE_OF_BIRTH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_BIRTH = LocalDate.now(ZoneId.systemDefault());

    private static final Gender DEFAULT_GENDER = Gender.MALE;
    private static final Gender UPDATED_GENDER = Gender.FEMALE;

    private static final ZonedDateTime DEFAULT_REGISTERATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_REGISTERATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_LAST_ACCESS = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_ACCESS = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ProfileStatus DEFAULT_PROFILE_STATUS = ProfileStatus.ACTIVE;
    private static final ProfileStatus UPDATED_PROFILE_STATUS = ProfileStatus.SUSPENDED;

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    private static final String DEFAULT_MOBILE = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_HOURLY_PAY_RATE = new BigDecimal(1);
    private static final BigDecimal UPDATED_HOURLY_PAY_RATE = new BigDecimal(2);

    private static final byte[] DEFAULT_THUMBNAIL_PHOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_THUMBNAIL_PHOTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_THUMBNAIL_PHOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_THUMBNAIL_PHOTO_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_THUMBNAIL_PHOTO_URL = "AAAAAAAAAA";
    private static final String UPDATED_THUMBNAIL_PHOTO_URL = "BBBBBBBBBB";

    private static final byte[] DEFAULT_FULL_PHOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FULL_PHOTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FULL_PHOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FULL_PHOTO_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_FULL_PHOTO_URL = "AAAAAAAAAA";
    private static final String UPDATED_FULL_PHOTO_URL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final Long DEFAULT_SHOP_CHANGE_ID = 1L;
    private static final Long UPDATED_SHOP_CHANGE_ID = 2L;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ProfileMapper profileMapper;

    @Autowired
    private ProfileService profileService;

    /**
     * This repository is mocked in the com.luulsolutions.luulpos.repository.search test package.
     *
     * @see com.luulsolutions.luulpos.repository.search.ProfileSearchRepositoryMockConfiguration
     */
    @Autowired
    private ProfileSearchRepository mockProfileSearchRepository;

    @Autowired
    private ProfileQueryService profileQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProfileMockMvc;

    private Profile profile;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProfileResource profileResource = new ProfileResource(profileService, profileQueryService);
        this.restProfileMockMvc = MockMvcBuilders.standaloneSetup(profileResource)
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
    public static Profile createEntity(EntityManager em) {
        Profile profile = new Profile()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .userType(DEFAULT_USER_TYPE)
            .dateOfBirth(DEFAULT_DATE_OF_BIRTH)
            .gender(DEFAULT_GENDER)
            .registerationDate(DEFAULT_REGISTERATION_DATE)
            .lastAccess(DEFAULT_LAST_ACCESS)
            .profileStatus(DEFAULT_PROFILE_STATUS)
            .telephone(DEFAULT_TELEPHONE)
            .mobile(DEFAULT_MOBILE)
            .hourlyPayRate(DEFAULT_HOURLY_PAY_RATE)
            .thumbnailPhoto(DEFAULT_THUMBNAIL_PHOTO)
            .thumbnailPhotoContentType(DEFAULT_THUMBNAIL_PHOTO_CONTENT_TYPE)
            .thumbnailPhotoUrl(DEFAULT_THUMBNAIL_PHOTO_URL)
            .fullPhoto(DEFAULT_FULL_PHOTO)
            .fullPhotoContentType(DEFAULT_FULL_PHOTO_CONTENT_TYPE)
            .fullPhotoUrl(DEFAULT_FULL_PHOTO_URL)
            .active(DEFAULT_ACTIVE)
            .shopChangeId(DEFAULT_SHOP_CHANGE_ID);
        return profile;
    }

    @Before
    public void initTest() {
        profile = createEntity(em);
    }

    @Test
    @Transactional
    public void createProfile() throws Exception {
        int databaseSizeBeforeCreate = profileRepository.findAll().size();

        // Create the Profile
        ProfileDTO profileDTO = profileMapper.toDto(profile);
        restProfileMockMvc.perform(post("/api/profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profileDTO)))
            .andExpect(status().isCreated());

        // Validate the Profile in the database
        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeCreate + 1);
        Profile testProfile = profileList.get(profileList.size() - 1);
        assertThat(testProfile.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testProfile.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testProfile.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testProfile.getUserType()).isEqualTo(DEFAULT_USER_TYPE);
        assertThat(testProfile.getDateOfBirth()).isEqualTo(DEFAULT_DATE_OF_BIRTH);
        assertThat(testProfile.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testProfile.getRegisterationDate()).isEqualTo(DEFAULT_REGISTERATION_DATE);
        assertThat(testProfile.getLastAccess()).isEqualTo(DEFAULT_LAST_ACCESS);
        assertThat(testProfile.getProfileStatus()).isEqualTo(DEFAULT_PROFILE_STATUS);
        assertThat(testProfile.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testProfile.getMobile()).isEqualTo(DEFAULT_MOBILE);
        assertThat(testProfile.getHourlyPayRate()).isEqualTo(DEFAULT_HOURLY_PAY_RATE);
        assertThat(testProfile.getThumbnailPhoto()).isEqualTo(DEFAULT_THUMBNAIL_PHOTO);
        assertThat(testProfile.getThumbnailPhotoContentType()).isEqualTo(DEFAULT_THUMBNAIL_PHOTO_CONTENT_TYPE);
        assertThat(testProfile.getThumbnailPhotoUrl()).isEqualTo(DEFAULT_THUMBNAIL_PHOTO_URL);
        assertThat(testProfile.getFullPhoto()).isEqualTo(DEFAULT_FULL_PHOTO);
        assertThat(testProfile.getFullPhotoContentType()).isEqualTo(DEFAULT_FULL_PHOTO_CONTENT_TYPE);
        assertThat(testProfile.getFullPhotoUrl()).isEqualTo(DEFAULT_FULL_PHOTO_URL);
        assertThat(testProfile.isActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testProfile.getShopChangeId()).isEqualTo(DEFAULT_SHOP_CHANGE_ID);

        // Validate the Profile in Elasticsearch
        verify(mockProfileSearchRepository, times(1)).save(testProfile);
    }

    @Test
    @Transactional
    public void createProfileWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = profileRepository.findAll().size();

        // Create the Profile with an existing ID
        profile.setId(1L);
        ProfileDTO profileDTO = profileMapper.toDto(profile);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfileMockMvc.perform(post("/api/profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Profile in the database
        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeCreate);

        // Validate the Profile in Elasticsearch
        verify(mockProfileSearchRepository, times(0)).save(profile);
    }

    @Test
    @Transactional
    public void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = profileRepository.findAll().size();
        // set the field null
        profile.setFirstName(null);

        // Create the Profile, which fails.
        ProfileDTO profileDTO = profileMapper.toDto(profile);

        restProfileMockMvc.perform(post("/api/profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profileDTO)))
            .andExpect(status().isBadRequest());

        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = profileRepository.findAll().size();
        // set the field null
        profile.setLastName(null);

        // Create the Profile, which fails.
        ProfileDTO profileDTO = profileMapper.toDto(profile);

        restProfileMockMvc.perform(post("/api/profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profileDTO)))
            .andExpect(status().isBadRequest());

        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = profileRepository.findAll().size();
        // set the field null
        profile.setEmail(null);

        // Create the Profile, which fails.
        ProfileDTO profileDTO = profileMapper.toDto(profile);

        restProfileMockMvc.perform(post("/api/profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profileDTO)))
            .andExpect(status().isBadRequest());

        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUserTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = profileRepository.findAll().size();
        // set the field null
        profile.setUserType(null);

        // Create the Profile, which fails.
        ProfileDTO profileDTO = profileMapper.toDto(profile);

        restProfileMockMvc.perform(post("/api/profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profileDTO)))
            .andExpect(status().isBadRequest());

        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProfiles() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList
        restProfileMockMvc.perform(get("/api/profiles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profile.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].userType").value(hasItem(DEFAULT_USER_TYPE.toString())))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].registerationDate").value(hasItem(sameInstant(DEFAULT_REGISTERATION_DATE))))
            .andExpect(jsonPath("$.[*].lastAccess").value(hasItem(sameInstant(DEFAULT_LAST_ACCESS))))
            .andExpect(jsonPath("$.[*].profileStatus").value(hasItem(DEFAULT_PROFILE_STATUS.toString())))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE.toString())))
            .andExpect(jsonPath("$.[*].mobile").value(hasItem(DEFAULT_MOBILE.toString())))
            .andExpect(jsonPath("$.[*].hourlyPayRate").value(hasItem(DEFAULT_HOURLY_PAY_RATE.intValue())))
            .andExpect(jsonPath("$.[*].thumbnailPhotoContentType").value(hasItem(DEFAULT_THUMBNAIL_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].thumbnailPhoto").value(hasItem(Base64Utils.encodeToString(DEFAULT_THUMBNAIL_PHOTO))))
            .andExpect(jsonPath("$.[*].thumbnailPhotoUrl").value(hasItem(DEFAULT_THUMBNAIL_PHOTO_URL.toString())))
            .andExpect(jsonPath("$.[*].fullPhotoContentType").value(hasItem(DEFAULT_FULL_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fullPhoto").value(hasItem(Base64Utils.encodeToString(DEFAULT_FULL_PHOTO))))
            .andExpect(jsonPath("$.[*].fullPhotoUrl").value(hasItem(DEFAULT_FULL_PHOTO_URL.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].shopChangeId").value(hasItem(DEFAULT_SHOP_CHANGE_ID.intValue())));
    }
    
    @Test
    @Transactional
    public void getProfile() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get the profile
        restProfileMockMvc.perform(get("/api/profiles/{id}", profile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(profile.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.userType").value(DEFAULT_USER_TYPE.toString()))
            .andExpect(jsonPath("$.dateOfBirth").value(DEFAULT_DATE_OF_BIRTH.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.registerationDate").value(sameInstant(DEFAULT_REGISTERATION_DATE)))
            .andExpect(jsonPath("$.lastAccess").value(sameInstant(DEFAULT_LAST_ACCESS)))
            .andExpect(jsonPath("$.profileStatus").value(DEFAULT_PROFILE_STATUS.toString()))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE.toString()))
            .andExpect(jsonPath("$.mobile").value(DEFAULT_MOBILE.toString()))
            .andExpect(jsonPath("$.hourlyPayRate").value(DEFAULT_HOURLY_PAY_RATE.intValue()))
            .andExpect(jsonPath("$.thumbnailPhotoContentType").value(DEFAULT_THUMBNAIL_PHOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.thumbnailPhoto").value(Base64Utils.encodeToString(DEFAULT_THUMBNAIL_PHOTO)))
            .andExpect(jsonPath("$.thumbnailPhotoUrl").value(DEFAULT_THUMBNAIL_PHOTO_URL.toString()))
            .andExpect(jsonPath("$.fullPhotoContentType").value(DEFAULT_FULL_PHOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.fullPhoto").value(Base64Utils.encodeToString(DEFAULT_FULL_PHOTO)))
            .andExpect(jsonPath("$.fullPhotoUrl").value(DEFAULT_FULL_PHOTO_URL.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.shopChangeId").value(DEFAULT_SHOP_CHANGE_ID.intValue()));
    }

    @Test
    @Transactional
    public void getAllProfilesByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where firstName equals to DEFAULT_FIRST_NAME
        defaultProfileShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the profileList where firstName equals to UPDATED_FIRST_NAME
        defaultProfileShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllProfilesByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultProfileShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the profileList where firstName equals to UPDATED_FIRST_NAME
        defaultProfileShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllProfilesByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where firstName is not null
        defaultProfileShouldBeFound("firstName.specified=true");

        // Get all the profileList where firstName is null
        defaultProfileShouldNotBeFound("firstName.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where lastName equals to DEFAULT_LAST_NAME
        defaultProfileShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the profileList where lastName equals to UPDATED_LAST_NAME
        defaultProfileShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllProfilesByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultProfileShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the profileList where lastName equals to UPDATED_LAST_NAME
        defaultProfileShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllProfilesByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where lastName is not null
        defaultProfileShouldBeFound("lastName.specified=true");

        // Get all the profileList where lastName is null
        defaultProfileShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where email equals to DEFAULT_EMAIL
        defaultProfileShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the profileList where email equals to UPDATED_EMAIL
        defaultProfileShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllProfilesByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultProfileShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the profileList where email equals to UPDATED_EMAIL
        defaultProfileShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllProfilesByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where email is not null
        defaultProfileShouldBeFound("email.specified=true");

        // Get all the profileList where email is null
        defaultProfileShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByUserTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where userType equals to DEFAULT_USER_TYPE
        defaultProfileShouldBeFound("userType.equals=" + DEFAULT_USER_TYPE);

        // Get all the profileList where userType equals to UPDATED_USER_TYPE
        defaultProfileShouldNotBeFound("userType.equals=" + UPDATED_USER_TYPE);
    }

    @Test
    @Transactional
    public void getAllProfilesByUserTypeIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where userType in DEFAULT_USER_TYPE or UPDATED_USER_TYPE
        defaultProfileShouldBeFound("userType.in=" + DEFAULT_USER_TYPE + "," + UPDATED_USER_TYPE);

        // Get all the profileList where userType equals to UPDATED_USER_TYPE
        defaultProfileShouldNotBeFound("userType.in=" + UPDATED_USER_TYPE);
    }

    @Test
    @Transactional
    public void getAllProfilesByUserTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where userType is not null
        defaultProfileShouldBeFound("userType.specified=true");

        // Get all the profileList where userType is null
        defaultProfileShouldNotBeFound("userType.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByDateOfBirthIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where dateOfBirth equals to DEFAULT_DATE_OF_BIRTH
        defaultProfileShouldBeFound("dateOfBirth.equals=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the profileList where dateOfBirth equals to UPDATED_DATE_OF_BIRTH
        defaultProfileShouldNotBeFound("dateOfBirth.equals=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    public void getAllProfilesByDateOfBirthIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where dateOfBirth in DEFAULT_DATE_OF_BIRTH or UPDATED_DATE_OF_BIRTH
        defaultProfileShouldBeFound("dateOfBirth.in=" + DEFAULT_DATE_OF_BIRTH + "," + UPDATED_DATE_OF_BIRTH);

        // Get all the profileList where dateOfBirth equals to UPDATED_DATE_OF_BIRTH
        defaultProfileShouldNotBeFound("dateOfBirth.in=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    public void getAllProfilesByDateOfBirthIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where dateOfBirth is not null
        defaultProfileShouldBeFound("dateOfBirth.specified=true");

        // Get all the profileList where dateOfBirth is null
        defaultProfileShouldNotBeFound("dateOfBirth.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByDateOfBirthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where dateOfBirth greater than or equals to DEFAULT_DATE_OF_BIRTH
        defaultProfileShouldBeFound("dateOfBirth.greaterOrEqualThan=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the profileList where dateOfBirth greater than or equals to UPDATED_DATE_OF_BIRTH
        defaultProfileShouldNotBeFound("dateOfBirth.greaterOrEqualThan=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    public void getAllProfilesByDateOfBirthIsLessThanSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where dateOfBirth less than or equals to DEFAULT_DATE_OF_BIRTH
        defaultProfileShouldNotBeFound("dateOfBirth.lessThan=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the profileList where dateOfBirth less than or equals to UPDATED_DATE_OF_BIRTH
        defaultProfileShouldBeFound("dateOfBirth.lessThan=" + UPDATED_DATE_OF_BIRTH);
    }


    @Test
    @Transactional
    public void getAllProfilesByGenderIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where gender equals to DEFAULT_GENDER
        defaultProfileShouldBeFound("gender.equals=" + DEFAULT_GENDER);

        // Get all the profileList where gender equals to UPDATED_GENDER
        defaultProfileShouldNotBeFound("gender.equals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    public void getAllProfilesByGenderIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where gender in DEFAULT_GENDER or UPDATED_GENDER
        defaultProfileShouldBeFound("gender.in=" + DEFAULT_GENDER + "," + UPDATED_GENDER);

        // Get all the profileList where gender equals to UPDATED_GENDER
        defaultProfileShouldNotBeFound("gender.in=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    public void getAllProfilesByGenderIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where gender is not null
        defaultProfileShouldBeFound("gender.specified=true");

        // Get all the profileList where gender is null
        defaultProfileShouldNotBeFound("gender.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByRegisterationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where registerationDate equals to DEFAULT_REGISTERATION_DATE
        defaultProfileShouldBeFound("registerationDate.equals=" + DEFAULT_REGISTERATION_DATE);

        // Get all the profileList where registerationDate equals to UPDATED_REGISTERATION_DATE
        defaultProfileShouldNotBeFound("registerationDate.equals=" + UPDATED_REGISTERATION_DATE);
    }

    @Test
    @Transactional
    public void getAllProfilesByRegisterationDateIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where registerationDate in DEFAULT_REGISTERATION_DATE or UPDATED_REGISTERATION_DATE
        defaultProfileShouldBeFound("registerationDate.in=" + DEFAULT_REGISTERATION_DATE + "," + UPDATED_REGISTERATION_DATE);

        // Get all the profileList where registerationDate equals to UPDATED_REGISTERATION_DATE
        defaultProfileShouldNotBeFound("registerationDate.in=" + UPDATED_REGISTERATION_DATE);
    }

    @Test
    @Transactional
    public void getAllProfilesByRegisterationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where registerationDate is not null
        defaultProfileShouldBeFound("registerationDate.specified=true");

        // Get all the profileList where registerationDate is null
        defaultProfileShouldNotBeFound("registerationDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByRegisterationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where registerationDate greater than or equals to DEFAULT_REGISTERATION_DATE
        defaultProfileShouldBeFound("registerationDate.greaterOrEqualThan=" + DEFAULT_REGISTERATION_DATE);

        // Get all the profileList where registerationDate greater than or equals to UPDATED_REGISTERATION_DATE
        defaultProfileShouldNotBeFound("registerationDate.greaterOrEqualThan=" + UPDATED_REGISTERATION_DATE);
    }

    @Test
    @Transactional
    public void getAllProfilesByRegisterationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where registerationDate less than or equals to DEFAULT_REGISTERATION_DATE
        defaultProfileShouldNotBeFound("registerationDate.lessThan=" + DEFAULT_REGISTERATION_DATE);

        // Get all the profileList where registerationDate less than or equals to UPDATED_REGISTERATION_DATE
        defaultProfileShouldBeFound("registerationDate.lessThan=" + UPDATED_REGISTERATION_DATE);
    }


    @Test
    @Transactional
    public void getAllProfilesByLastAccessIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where lastAccess equals to DEFAULT_LAST_ACCESS
        defaultProfileShouldBeFound("lastAccess.equals=" + DEFAULT_LAST_ACCESS);

        // Get all the profileList where lastAccess equals to UPDATED_LAST_ACCESS
        defaultProfileShouldNotBeFound("lastAccess.equals=" + UPDATED_LAST_ACCESS);
    }

    @Test
    @Transactional
    public void getAllProfilesByLastAccessIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where lastAccess in DEFAULT_LAST_ACCESS or UPDATED_LAST_ACCESS
        defaultProfileShouldBeFound("lastAccess.in=" + DEFAULT_LAST_ACCESS + "," + UPDATED_LAST_ACCESS);

        // Get all the profileList where lastAccess equals to UPDATED_LAST_ACCESS
        defaultProfileShouldNotBeFound("lastAccess.in=" + UPDATED_LAST_ACCESS);
    }

    @Test
    @Transactional
    public void getAllProfilesByLastAccessIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where lastAccess is not null
        defaultProfileShouldBeFound("lastAccess.specified=true");

        // Get all the profileList where lastAccess is null
        defaultProfileShouldNotBeFound("lastAccess.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByLastAccessIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where lastAccess greater than or equals to DEFAULT_LAST_ACCESS
        defaultProfileShouldBeFound("lastAccess.greaterOrEqualThan=" + DEFAULT_LAST_ACCESS);

        // Get all the profileList where lastAccess greater than or equals to UPDATED_LAST_ACCESS
        defaultProfileShouldNotBeFound("lastAccess.greaterOrEqualThan=" + UPDATED_LAST_ACCESS);
    }

    @Test
    @Transactional
    public void getAllProfilesByLastAccessIsLessThanSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where lastAccess less than or equals to DEFAULT_LAST_ACCESS
        defaultProfileShouldNotBeFound("lastAccess.lessThan=" + DEFAULT_LAST_ACCESS);

        // Get all the profileList where lastAccess less than or equals to UPDATED_LAST_ACCESS
        defaultProfileShouldBeFound("lastAccess.lessThan=" + UPDATED_LAST_ACCESS);
    }


    @Test
    @Transactional
    public void getAllProfilesByProfileStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where profileStatus equals to DEFAULT_PROFILE_STATUS
        defaultProfileShouldBeFound("profileStatus.equals=" + DEFAULT_PROFILE_STATUS);

        // Get all the profileList where profileStatus equals to UPDATED_PROFILE_STATUS
        defaultProfileShouldNotBeFound("profileStatus.equals=" + UPDATED_PROFILE_STATUS);
    }

    @Test
    @Transactional
    public void getAllProfilesByProfileStatusIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where profileStatus in DEFAULT_PROFILE_STATUS or UPDATED_PROFILE_STATUS
        defaultProfileShouldBeFound("profileStatus.in=" + DEFAULT_PROFILE_STATUS + "," + UPDATED_PROFILE_STATUS);

        // Get all the profileList where profileStatus equals to UPDATED_PROFILE_STATUS
        defaultProfileShouldNotBeFound("profileStatus.in=" + UPDATED_PROFILE_STATUS);
    }

    @Test
    @Transactional
    public void getAllProfilesByProfileStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where profileStatus is not null
        defaultProfileShouldBeFound("profileStatus.specified=true");

        // Get all the profileList where profileStatus is null
        defaultProfileShouldNotBeFound("profileStatus.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByTelephoneIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where telephone equals to DEFAULT_TELEPHONE
        defaultProfileShouldBeFound("telephone.equals=" + DEFAULT_TELEPHONE);

        // Get all the profileList where telephone equals to UPDATED_TELEPHONE
        defaultProfileShouldNotBeFound("telephone.equals=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    public void getAllProfilesByTelephoneIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where telephone in DEFAULT_TELEPHONE or UPDATED_TELEPHONE
        defaultProfileShouldBeFound("telephone.in=" + DEFAULT_TELEPHONE + "," + UPDATED_TELEPHONE);

        // Get all the profileList where telephone equals to UPDATED_TELEPHONE
        defaultProfileShouldNotBeFound("telephone.in=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    public void getAllProfilesByTelephoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where telephone is not null
        defaultProfileShouldBeFound("telephone.specified=true");

        // Get all the profileList where telephone is null
        defaultProfileShouldNotBeFound("telephone.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByMobileIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where mobile equals to DEFAULT_MOBILE
        defaultProfileShouldBeFound("mobile.equals=" + DEFAULT_MOBILE);

        // Get all the profileList where mobile equals to UPDATED_MOBILE
        defaultProfileShouldNotBeFound("mobile.equals=" + UPDATED_MOBILE);
    }

    @Test
    @Transactional
    public void getAllProfilesByMobileIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where mobile in DEFAULT_MOBILE or UPDATED_MOBILE
        defaultProfileShouldBeFound("mobile.in=" + DEFAULT_MOBILE + "," + UPDATED_MOBILE);

        // Get all the profileList where mobile equals to UPDATED_MOBILE
        defaultProfileShouldNotBeFound("mobile.in=" + UPDATED_MOBILE);
    }

    @Test
    @Transactional
    public void getAllProfilesByMobileIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where mobile is not null
        defaultProfileShouldBeFound("mobile.specified=true");

        // Get all the profileList where mobile is null
        defaultProfileShouldNotBeFound("mobile.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByHourlyPayRateIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where hourlyPayRate equals to DEFAULT_HOURLY_PAY_RATE
        defaultProfileShouldBeFound("hourlyPayRate.equals=" + DEFAULT_HOURLY_PAY_RATE);

        // Get all the profileList where hourlyPayRate equals to UPDATED_HOURLY_PAY_RATE
        defaultProfileShouldNotBeFound("hourlyPayRate.equals=" + UPDATED_HOURLY_PAY_RATE);
    }

    @Test
    @Transactional
    public void getAllProfilesByHourlyPayRateIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where hourlyPayRate in DEFAULT_HOURLY_PAY_RATE or UPDATED_HOURLY_PAY_RATE
        defaultProfileShouldBeFound("hourlyPayRate.in=" + DEFAULT_HOURLY_PAY_RATE + "," + UPDATED_HOURLY_PAY_RATE);

        // Get all the profileList where hourlyPayRate equals to UPDATED_HOURLY_PAY_RATE
        defaultProfileShouldNotBeFound("hourlyPayRate.in=" + UPDATED_HOURLY_PAY_RATE);
    }

    @Test
    @Transactional
    public void getAllProfilesByHourlyPayRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where hourlyPayRate is not null
        defaultProfileShouldBeFound("hourlyPayRate.specified=true");

        // Get all the profileList where hourlyPayRate is null
        defaultProfileShouldNotBeFound("hourlyPayRate.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByThumbnailPhotoUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where thumbnailPhotoUrl equals to DEFAULT_THUMBNAIL_PHOTO_URL
        defaultProfileShouldBeFound("thumbnailPhotoUrl.equals=" + DEFAULT_THUMBNAIL_PHOTO_URL);

        // Get all the profileList where thumbnailPhotoUrl equals to UPDATED_THUMBNAIL_PHOTO_URL
        defaultProfileShouldNotBeFound("thumbnailPhotoUrl.equals=" + UPDATED_THUMBNAIL_PHOTO_URL);
    }

    @Test
    @Transactional
    public void getAllProfilesByThumbnailPhotoUrlIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where thumbnailPhotoUrl in DEFAULT_THUMBNAIL_PHOTO_URL or UPDATED_THUMBNAIL_PHOTO_URL
        defaultProfileShouldBeFound("thumbnailPhotoUrl.in=" + DEFAULT_THUMBNAIL_PHOTO_URL + "," + UPDATED_THUMBNAIL_PHOTO_URL);

        // Get all the profileList where thumbnailPhotoUrl equals to UPDATED_THUMBNAIL_PHOTO_URL
        defaultProfileShouldNotBeFound("thumbnailPhotoUrl.in=" + UPDATED_THUMBNAIL_PHOTO_URL);
    }

    @Test
    @Transactional
    public void getAllProfilesByThumbnailPhotoUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where thumbnailPhotoUrl is not null
        defaultProfileShouldBeFound("thumbnailPhotoUrl.specified=true");

        // Get all the profileList where thumbnailPhotoUrl is null
        defaultProfileShouldNotBeFound("thumbnailPhotoUrl.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByFullPhotoUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where fullPhotoUrl equals to DEFAULT_FULL_PHOTO_URL
        defaultProfileShouldBeFound("fullPhotoUrl.equals=" + DEFAULT_FULL_PHOTO_URL);

        // Get all the profileList where fullPhotoUrl equals to UPDATED_FULL_PHOTO_URL
        defaultProfileShouldNotBeFound("fullPhotoUrl.equals=" + UPDATED_FULL_PHOTO_URL);
    }

    @Test
    @Transactional
    public void getAllProfilesByFullPhotoUrlIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where fullPhotoUrl in DEFAULT_FULL_PHOTO_URL or UPDATED_FULL_PHOTO_URL
        defaultProfileShouldBeFound("fullPhotoUrl.in=" + DEFAULT_FULL_PHOTO_URL + "," + UPDATED_FULL_PHOTO_URL);

        // Get all the profileList where fullPhotoUrl equals to UPDATED_FULL_PHOTO_URL
        defaultProfileShouldNotBeFound("fullPhotoUrl.in=" + UPDATED_FULL_PHOTO_URL);
    }

    @Test
    @Transactional
    public void getAllProfilesByFullPhotoUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where fullPhotoUrl is not null
        defaultProfileShouldBeFound("fullPhotoUrl.specified=true");

        // Get all the profileList where fullPhotoUrl is null
        defaultProfileShouldNotBeFound("fullPhotoUrl.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where active equals to DEFAULT_ACTIVE
        defaultProfileShouldBeFound("active.equals=" + DEFAULT_ACTIVE);

        // Get all the profileList where active equals to UPDATED_ACTIVE
        defaultProfileShouldNotBeFound("active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllProfilesByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where active in DEFAULT_ACTIVE or UPDATED_ACTIVE
        defaultProfileShouldBeFound("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE);

        // Get all the profileList where active equals to UPDATED_ACTIVE
        defaultProfileShouldNotBeFound("active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllProfilesByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where active is not null
        defaultProfileShouldBeFound("active.specified=true");

        // Get all the profileList where active is null
        defaultProfileShouldNotBeFound("active.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByShopChangeIdIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where shopChangeId equals to DEFAULT_SHOP_CHANGE_ID
        defaultProfileShouldBeFound("shopChangeId.equals=" + DEFAULT_SHOP_CHANGE_ID);

        // Get all the profileList where shopChangeId equals to UPDATED_SHOP_CHANGE_ID
        defaultProfileShouldNotBeFound("shopChangeId.equals=" + UPDATED_SHOP_CHANGE_ID);
    }

    @Test
    @Transactional
    public void getAllProfilesByShopChangeIdIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where shopChangeId in DEFAULT_SHOP_CHANGE_ID or UPDATED_SHOP_CHANGE_ID
        defaultProfileShouldBeFound("shopChangeId.in=" + DEFAULT_SHOP_CHANGE_ID + "," + UPDATED_SHOP_CHANGE_ID);

        // Get all the profileList where shopChangeId equals to UPDATED_SHOP_CHANGE_ID
        defaultProfileShouldNotBeFound("shopChangeId.in=" + UPDATED_SHOP_CHANGE_ID);
    }

    @Test
    @Transactional
    public void getAllProfilesByShopChangeIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where shopChangeId is not null
        defaultProfileShouldBeFound("shopChangeId.specified=true");

        // Get all the profileList where shopChangeId is null
        defaultProfileShouldNotBeFound("shopChangeId.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByShopChangeIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where shopChangeId greater than or equals to DEFAULT_SHOP_CHANGE_ID
        defaultProfileShouldBeFound("shopChangeId.greaterOrEqualThan=" + DEFAULT_SHOP_CHANGE_ID);

        // Get all the profileList where shopChangeId greater than or equals to UPDATED_SHOP_CHANGE_ID
        defaultProfileShouldNotBeFound("shopChangeId.greaterOrEqualThan=" + UPDATED_SHOP_CHANGE_ID);
    }

    @Test
    @Transactional
    public void getAllProfilesByShopChangeIdIsLessThanSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where shopChangeId less than or equals to DEFAULT_SHOP_CHANGE_ID
        defaultProfileShouldNotBeFound("shopChangeId.lessThan=" + DEFAULT_SHOP_CHANGE_ID);

        // Get all the profileList where shopChangeId less than or equals to UPDATED_SHOP_CHANGE_ID
        defaultProfileShouldBeFound("shopChangeId.lessThan=" + UPDATED_SHOP_CHANGE_ID);
    }


    @Test
    @Transactional
    public void getAllProfilesByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        profile.setUser(user);
        profileRepository.saveAndFlush(profile);
        Long userId = user.getId();

        // Get all the profileList where user equals to userId
        defaultProfileShouldBeFound("userId.equals=" + userId);

        // Get all the profileList where user equals to userId + 1
        defaultProfileShouldNotBeFound("userId.equals=" + (userId + 1));
    }


    @Test
    @Transactional
    public void getAllProfilesByShopIsEqualToSomething() throws Exception {
        // Initialize the database
        Shop shop = ShopResourceIntTest.createEntity(em);
        em.persist(shop);
        em.flush();
        profile.setShop(shop);
        profileRepository.saveAndFlush(profile);
        Long shopId = shop.getId();

        // Get all the profileList where shop equals to shopId
        defaultProfileShouldBeFound("shopId.equals=" + shopId);

        // Get all the profileList where shop equals to shopId + 1
        defaultProfileShouldNotBeFound("shopId.equals=" + (shopId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultProfileShouldBeFound(String filter) throws Exception {
        restProfileMockMvc.perform(get("/api/profiles?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profile.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].userType").value(hasItem(DEFAULT_USER_TYPE.toString())))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].registerationDate").value(hasItem(sameInstant(DEFAULT_REGISTERATION_DATE))))
            .andExpect(jsonPath("$.[*].lastAccess").value(hasItem(sameInstant(DEFAULT_LAST_ACCESS))))
            .andExpect(jsonPath("$.[*].profileStatus").value(hasItem(DEFAULT_PROFILE_STATUS.toString())))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE.toString())))
            .andExpect(jsonPath("$.[*].mobile").value(hasItem(DEFAULT_MOBILE.toString())))
            .andExpect(jsonPath("$.[*].hourlyPayRate").value(hasItem(DEFAULT_HOURLY_PAY_RATE.intValue())))
            .andExpect(jsonPath("$.[*].thumbnailPhotoContentType").value(hasItem(DEFAULT_THUMBNAIL_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].thumbnailPhoto").value(hasItem(Base64Utils.encodeToString(DEFAULT_THUMBNAIL_PHOTO))))
            .andExpect(jsonPath("$.[*].thumbnailPhotoUrl").value(hasItem(DEFAULT_THUMBNAIL_PHOTO_URL.toString())))
            .andExpect(jsonPath("$.[*].fullPhotoContentType").value(hasItem(DEFAULT_FULL_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fullPhoto").value(hasItem(Base64Utils.encodeToString(DEFAULT_FULL_PHOTO))))
            .andExpect(jsonPath("$.[*].fullPhotoUrl").value(hasItem(DEFAULT_FULL_PHOTO_URL.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].shopChangeId").value(hasItem(DEFAULT_SHOP_CHANGE_ID.intValue())));

        // Check, that the count call also returns 1
        restProfileMockMvc.perform(get("/api/profiles/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultProfileShouldNotBeFound(String filter) throws Exception {
        restProfileMockMvc.perform(get("/api/profiles?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProfileMockMvc.perform(get("/api/profiles/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingProfile() throws Exception {
        // Get the profile
        restProfileMockMvc.perform(get("/api/profiles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProfile() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        int databaseSizeBeforeUpdate = profileRepository.findAll().size();

        // Update the profile
        Profile updatedProfile = profileRepository.findById(profile.getId()).get();
        // Disconnect from session so that the updates on updatedProfile are not directly saved in db
        em.detach(updatedProfile);
        updatedProfile
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .userType(UPDATED_USER_TYPE)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .gender(UPDATED_GENDER)
            .registerationDate(UPDATED_REGISTERATION_DATE)
            .lastAccess(UPDATED_LAST_ACCESS)
            .profileStatus(UPDATED_PROFILE_STATUS)
            .telephone(UPDATED_TELEPHONE)
            .mobile(UPDATED_MOBILE)
            .hourlyPayRate(UPDATED_HOURLY_PAY_RATE)
            .thumbnailPhoto(UPDATED_THUMBNAIL_PHOTO)
            .thumbnailPhotoContentType(UPDATED_THUMBNAIL_PHOTO_CONTENT_TYPE)
            .thumbnailPhotoUrl(UPDATED_THUMBNAIL_PHOTO_URL)
            .fullPhoto(UPDATED_FULL_PHOTO)
            .fullPhotoContentType(UPDATED_FULL_PHOTO_CONTENT_TYPE)
            .fullPhotoUrl(UPDATED_FULL_PHOTO_URL)
            .active(UPDATED_ACTIVE)
            .shopChangeId(UPDATED_SHOP_CHANGE_ID);
        ProfileDTO profileDTO = profileMapper.toDto(updatedProfile);

        restProfileMockMvc.perform(put("/api/profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profileDTO)))
            .andExpect(status().isOk());

        // Validate the Profile in the database
        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeUpdate);
        Profile testProfile = profileList.get(profileList.size() - 1);
        assertThat(testProfile.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testProfile.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testProfile.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testProfile.getUserType()).isEqualTo(UPDATED_USER_TYPE);
        assertThat(testProfile.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testProfile.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testProfile.getRegisterationDate()).isEqualTo(UPDATED_REGISTERATION_DATE);
        assertThat(testProfile.getLastAccess()).isEqualTo(UPDATED_LAST_ACCESS);
        assertThat(testProfile.getProfileStatus()).isEqualTo(UPDATED_PROFILE_STATUS);
        assertThat(testProfile.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testProfile.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testProfile.getHourlyPayRate()).isEqualTo(UPDATED_HOURLY_PAY_RATE);
        assertThat(testProfile.getThumbnailPhoto()).isEqualTo(UPDATED_THUMBNAIL_PHOTO);
        assertThat(testProfile.getThumbnailPhotoContentType()).isEqualTo(UPDATED_THUMBNAIL_PHOTO_CONTENT_TYPE);
        assertThat(testProfile.getThumbnailPhotoUrl()).isEqualTo(UPDATED_THUMBNAIL_PHOTO_URL);
        assertThat(testProfile.getFullPhoto()).isEqualTo(UPDATED_FULL_PHOTO);
        assertThat(testProfile.getFullPhotoContentType()).isEqualTo(UPDATED_FULL_PHOTO_CONTENT_TYPE);
        assertThat(testProfile.getFullPhotoUrl()).isEqualTo(UPDATED_FULL_PHOTO_URL);
        assertThat(testProfile.isActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testProfile.getShopChangeId()).isEqualTo(UPDATED_SHOP_CHANGE_ID);

        // Validate the Profile in Elasticsearch
        verify(mockProfileSearchRepository, times(1)).save(testProfile);
    }

    @Test
    @Transactional
    public void updateNonExistingProfile() throws Exception {
        int databaseSizeBeforeUpdate = profileRepository.findAll().size();

        // Create the Profile
        ProfileDTO profileDTO = profileMapper.toDto(profile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfileMockMvc.perform(put("/api/profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Profile in the database
        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Profile in Elasticsearch
        verify(mockProfileSearchRepository, times(0)).save(profile);
    }

    @Test
    @Transactional
    public void deleteProfile() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        int databaseSizeBeforeDelete = profileRepository.findAll().size();

        // Get the profile
        restProfileMockMvc.perform(delete("/api/profiles/{id}", profile.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Profile in Elasticsearch
        verify(mockProfileSearchRepository, times(1)).deleteById(profile.getId());
    }

    @Test
    @Transactional
    public void searchProfile() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);
        when(mockProfileSearchRepository.search(queryStringQuery("id:" + profile.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(profile), PageRequest.of(0, 1), 1));
        // Search the profile
        restProfileMockMvc.perform(get("/api/_search/profiles?query=id:" + profile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profile.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].userType").value(hasItem(DEFAULT_USER_TYPE.toString())))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].registerationDate").value(hasItem(sameInstant(DEFAULT_REGISTERATION_DATE))))
            .andExpect(jsonPath("$.[*].lastAccess").value(hasItem(sameInstant(DEFAULT_LAST_ACCESS))))
            .andExpect(jsonPath("$.[*].profileStatus").value(hasItem(DEFAULT_PROFILE_STATUS.toString())))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].mobile").value(hasItem(DEFAULT_MOBILE)))
            .andExpect(jsonPath("$.[*].hourlyPayRate").value(hasItem(DEFAULT_HOURLY_PAY_RATE.intValue())))
            .andExpect(jsonPath("$.[*].thumbnailPhotoContentType").value(hasItem(DEFAULT_THUMBNAIL_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].thumbnailPhoto").value(hasItem(Base64Utils.encodeToString(DEFAULT_THUMBNAIL_PHOTO))))
            .andExpect(jsonPath("$.[*].thumbnailPhotoUrl").value(hasItem(DEFAULT_THUMBNAIL_PHOTO_URL)))
            .andExpect(jsonPath("$.[*].fullPhotoContentType").value(hasItem(DEFAULT_FULL_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fullPhoto").value(hasItem(Base64Utils.encodeToString(DEFAULT_FULL_PHOTO))))
            .andExpect(jsonPath("$.[*].fullPhotoUrl").value(hasItem(DEFAULT_FULL_PHOTO_URL)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].shopChangeId").value(hasItem(DEFAULT_SHOP_CHANGE_ID.intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Profile.class);
        Profile profile1 = new Profile();
        profile1.setId(1L);
        Profile profile2 = new Profile();
        profile2.setId(profile1.getId());
        assertThat(profile1).isEqualTo(profile2);
        profile2.setId(2L);
        assertThat(profile1).isNotEqualTo(profile2);
        profile1.setId(null);
        assertThat(profile1).isNotEqualTo(profile2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfileDTO.class);
        ProfileDTO profileDTO1 = new ProfileDTO();
        profileDTO1.setId(1L);
        ProfileDTO profileDTO2 = new ProfileDTO();
        assertThat(profileDTO1).isNotEqualTo(profileDTO2);
        profileDTO2.setId(profileDTO1.getId());
        assertThat(profileDTO1).isEqualTo(profileDTO2);
        profileDTO2.setId(2L);
        assertThat(profileDTO1).isNotEqualTo(profileDTO2);
        profileDTO1.setId(null);
        assertThat(profileDTO1).isNotEqualTo(profileDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(profileMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(profileMapper.fromId(null)).isNull();
    }
}
