package com.luulsolutions.luulpos.web.rest;

import com.luulsolutions.luulpos.LuulposApp;

import com.luulsolutions.luulpos.domain.Company;
import com.luulsolutions.luulpos.repository.CompanyRepository;
import com.luulsolutions.luulpos.repository.search.CompanySearchRepository;
import com.luulsolutions.luulpos.service.CompanyService;
import com.luulsolutions.luulpos.service.dto.CompanyDTO;
import com.luulsolutions.luulpos.service.mapper.CompanyMapper;
import com.luulsolutions.luulpos.web.rest.errors.ExceptionTranslator;
import com.luulsolutions.luulpos.service.dto.CompanyCriteria;
import com.luulsolutions.luulpos.service.CompanyQueryService;

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
 * Test class for the CompanyResource REST controller.
 *
 * @see CompanyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LuulposApp.class)
public class CompanyResourceIntTest {

    private static final String DEFAULT_COMPANY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final byte[] DEFAULT_COMPANY_LOGO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_COMPANY_LOGO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_COMPANY_LOGO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_COMPANY_LOGO_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_COMPANY_LOGO_URL = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_LOGO_URL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private CompanyService companyService;

    /**
     * This repository is mocked in the com.luulsolutions.luulpos.repository.search test package.
     *
     * @see com.luulsolutions.luulpos.repository.search.CompanySearchRepositoryMockConfiguration
     */
    @Autowired
    private CompanySearchRepository mockCompanySearchRepository;

    @Autowired
    private CompanyQueryService companyQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCompanyMockMvc;

    private Company company;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CompanyResource companyResource = new CompanyResource(companyService, companyQueryService);
        this.restCompanyMockMvc = MockMvcBuilders.standaloneSetup(companyResource)
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
    public static Company createEntity(EntityManager em) {
        Company company = new Company()
            .companyName(DEFAULT_COMPANY_NAME)
            .description(DEFAULT_DESCRIPTION)
            .note(DEFAULT_NOTE)
            .companyLogo(DEFAULT_COMPANY_LOGO)
            .companyLogoContentType(DEFAULT_COMPANY_LOGO_CONTENT_TYPE)
            .companyLogoUrl(DEFAULT_COMPANY_LOGO_URL)
            .active(DEFAULT_ACTIVE);
        return company;
    }

    @Before
    public void initTest() {
        company = createEntity(em);
    }

    @Test
    @Transactional
    public void createCompany() throws Exception {
        int databaseSizeBeforeCreate = companyRepository.findAll().size();

        // Create the Company
        CompanyDTO companyDTO = companyMapper.toDto(company);
        restCompanyMockMvc.perform(post("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyDTO)))
            .andExpect(status().isCreated());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeCreate + 1);
        Company testCompany = companyList.get(companyList.size() - 1);
        assertThat(testCompany.getCompanyName()).isEqualTo(DEFAULT_COMPANY_NAME);
        assertThat(testCompany.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCompany.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testCompany.getCompanyLogo()).isEqualTo(DEFAULT_COMPANY_LOGO);
        assertThat(testCompany.getCompanyLogoContentType()).isEqualTo(DEFAULT_COMPANY_LOGO_CONTENT_TYPE);
        assertThat(testCompany.getCompanyLogoUrl()).isEqualTo(DEFAULT_COMPANY_LOGO_URL);
        assertThat(testCompany.isActive()).isEqualTo(DEFAULT_ACTIVE);

        // Validate the Company in Elasticsearch
        verify(mockCompanySearchRepository, times(1)).save(testCompany);
    }

    @Test
    @Transactional
    public void createCompanyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = companyRepository.findAll().size();

        // Create the Company with an existing ID
        company.setId(1L);
        CompanyDTO companyDTO = companyMapper.toDto(company);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompanyMockMvc.perform(post("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeCreate);

        // Validate the Company in Elasticsearch
        verify(mockCompanySearchRepository, times(0)).save(company);
    }

    @Test
    @Transactional
    public void checkCompanyNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyRepository.findAll().size();
        // set the field null
        company.setCompanyName(null);

        // Create the Company, which fails.
        CompanyDTO companyDTO = companyMapper.toDto(company);

        restCompanyMockMvc.perform(post("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyDTO)))
            .andExpect(status().isBadRequest());

        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCompanies() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList
        restCompanyMockMvc.perform(get("/api/companies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(company.getId().intValue())))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.toString())))
            .andExpect(jsonPath("$.[*].companyLogoContentType").value(hasItem(DEFAULT_COMPANY_LOGO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].companyLogo").value(hasItem(Base64Utils.encodeToString(DEFAULT_COMPANY_LOGO))))
            .andExpect(jsonPath("$.[*].companyLogoUrl").value(hasItem(DEFAULT_COMPANY_LOGO_URL.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getCompany() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get the company
        restCompanyMockMvc.perform(get("/api/companies/{id}", company.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(company.getId().intValue()))
            .andExpect(jsonPath("$.companyName").value(DEFAULT_COMPANY_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE.toString()))
            .andExpect(jsonPath("$.companyLogoContentType").value(DEFAULT_COMPANY_LOGO_CONTENT_TYPE))
            .andExpect(jsonPath("$.companyLogo").value(Base64Utils.encodeToString(DEFAULT_COMPANY_LOGO)))
            .andExpect(jsonPath("$.companyLogoUrl").value(DEFAULT_COMPANY_LOGO_URL.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllCompaniesByCompanyNameIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where companyName equals to DEFAULT_COMPANY_NAME
        defaultCompanyShouldBeFound("companyName.equals=" + DEFAULT_COMPANY_NAME);

        // Get all the companyList where companyName equals to UPDATED_COMPANY_NAME
        defaultCompanyShouldNotBeFound("companyName.equals=" + UPDATED_COMPANY_NAME);
    }

    @Test
    @Transactional
    public void getAllCompaniesByCompanyNameIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where companyName in DEFAULT_COMPANY_NAME or UPDATED_COMPANY_NAME
        defaultCompanyShouldBeFound("companyName.in=" + DEFAULT_COMPANY_NAME + "," + UPDATED_COMPANY_NAME);

        // Get all the companyList where companyName equals to UPDATED_COMPANY_NAME
        defaultCompanyShouldNotBeFound("companyName.in=" + UPDATED_COMPANY_NAME);
    }

    @Test
    @Transactional
    public void getAllCompaniesByCompanyNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where companyName is not null
        defaultCompanyShouldBeFound("companyName.specified=true");

        // Get all the companyList where companyName is null
        defaultCompanyShouldNotBeFound("companyName.specified=false");
    }

    @Test
    @Transactional
    public void getAllCompaniesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where description equals to DEFAULT_DESCRIPTION
        defaultCompanyShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the companyList where description equals to UPDATED_DESCRIPTION
        defaultCompanyShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCompaniesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultCompanyShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the companyList where description equals to UPDATED_DESCRIPTION
        defaultCompanyShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCompaniesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where description is not null
        defaultCompanyShouldBeFound("description.specified=true");

        // Get all the companyList where description is null
        defaultCompanyShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllCompaniesByNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where note equals to DEFAULT_NOTE
        defaultCompanyShouldBeFound("note.equals=" + DEFAULT_NOTE);

        // Get all the companyList where note equals to UPDATED_NOTE
        defaultCompanyShouldNotBeFound("note.equals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllCompaniesByNoteIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where note in DEFAULT_NOTE or UPDATED_NOTE
        defaultCompanyShouldBeFound("note.in=" + DEFAULT_NOTE + "," + UPDATED_NOTE);

        // Get all the companyList where note equals to UPDATED_NOTE
        defaultCompanyShouldNotBeFound("note.in=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllCompaniesByNoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where note is not null
        defaultCompanyShouldBeFound("note.specified=true");

        // Get all the companyList where note is null
        defaultCompanyShouldNotBeFound("note.specified=false");
    }

    @Test
    @Transactional
    public void getAllCompaniesByCompanyLogoUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where companyLogoUrl equals to DEFAULT_COMPANY_LOGO_URL
        defaultCompanyShouldBeFound("companyLogoUrl.equals=" + DEFAULT_COMPANY_LOGO_URL);

        // Get all the companyList where companyLogoUrl equals to UPDATED_COMPANY_LOGO_URL
        defaultCompanyShouldNotBeFound("companyLogoUrl.equals=" + UPDATED_COMPANY_LOGO_URL);
    }

    @Test
    @Transactional
    public void getAllCompaniesByCompanyLogoUrlIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where companyLogoUrl in DEFAULT_COMPANY_LOGO_URL or UPDATED_COMPANY_LOGO_URL
        defaultCompanyShouldBeFound("companyLogoUrl.in=" + DEFAULT_COMPANY_LOGO_URL + "," + UPDATED_COMPANY_LOGO_URL);

        // Get all the companyList where companyLogoUrl equals to UPDATED_COMPANY_LOGO_URL
        defaultCompanyShouldNotBeFound("companyLogoUrl.in=" + UPDATED_COMPANY_LOGO_URL);
    }

    @Test
    @Transactional
    public void getAllCompaniesByCompanyLogoUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where companyLogoUrl is not null
        defaultCompanyShouldBeFound("companyLogoUrl.specified=true");

        // Get all the companyList where companyLogoUrl is null
        defaultCompanyShouldNotBeFound("companyLogoUrl.specified=false");
    }

    @Test
    @Transactional
    public void getAllCompaniesByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where active equals to DEFAULT_ACTIVE
        defaultCompanyShouldBeFound("active.equals=" + DEFAULT_ACTIVE);

        // Get all the companyList where active equals to UPDATED_ACTIVE
        defaultCompanyShouldNotBeFound("active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllCompaniesByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where active in DEFAULT_ACTIVE or UPDATED_ACTIVE
        defaultCompanyShouldBeFound("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE);

        // Get all the companyList where active equals to UPDATED_ACTIVE
        defaultCompanyShouldNotBeFound("active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllCompaniesByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where active is not null
        defaultCompanyShouldBeFound("active.specified=true");

        // Get all the companyList where active is null
        defaultCompanyShouldNotBeFound("active.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultCompanyShouldBeFound(String filter) throws Exception {
        restCompanyMockMvc.perform(get("/api/companies?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(company.getId().intValue())))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.toString())))
            .andExpect(jsonPath("$.[*].companyLogoContentType").value(hasItem(DEFAULT_COMPANY_LOGO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].companyLogo").value(hasItem(Base64Utils.encodeToString(DEFAULT_COMPANY_LOGO))))
            .andExpect(jsonPath("$.[*].companyLogoUrl").value(hasItem(DEFAULT_COMPANY_LOGO_URL.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restCompanyMockMvc.perform(get("/api/companies/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultCompanyShouldNotBeFound(String filter) throws Exception {
        restCompanyMockMvc.perform(get("/api/companies?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCompanyMockMvc.perform(get("/api/companies/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCompany() throws Exception {
        // Get the company
        restCompanyMockMvc.perform(get("/api/companies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCompany() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        int databaseSizeBeforeUpdate = companyRepository.findAll().size();

        // Update the company
        Company updatedCompany = companyRepository.findById(company.getId()).get();
        // Disconnect from session so that the updates on updatedCompany are not directly saved in db
        em.detach(updatedCompany);
        updatedCompany
            .companyName(UPDATED_COMPANY_NAME)
            .description(UPDATED_DESCRIPTION)
            .note(UPDATED_NOTE)
            .companyLogo(UPDATED_COMPANY_LOGO)
            .companyLogoContentType(UPDATED_COMPANY_LOGO_CONTENT_TYPE)
            .companyLogoUrl(UPDATED_COMPANY_LOGO_URL)
            .active(UPDATED_ACTIVE);
        CompanyDTO companyDTO = companyMapper.toDto(updatedCompany);

        restCompanyMockMvc.perform(put("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyDTO)))
            .andExpect(status().isOk());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
        Company testCompany = companyList.get(companyList.size() - 1);
        assertThat(testCompany.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testCompany.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCompany.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testCompany.getCompanyLogo()).isEqualTo(UPDATED_COMPANY_LOGO);
        assertThat(testCompany.getCompanyLogoContentType()).isEqualTo(UPDATED_COMPANY_LOGO_CONTENT_TYPE);
        assertThat(testCompany.getCompanyLogoUrl()).isEqualTo(UPDATED_COMPANY_LOGO_URL);
        assertThat(testCompany.isActive()).isEqualTo(UPDATED_ACTIVE);

        // Validate the Company in Elasticsearch
        verify(mockCompanySearchRepository, times(1)).save(testCompany);
    }

    @Test
    @Transactional
    public void updateNonExistingCompany() throws Exception {
        int databaseSizeBeforeUpdate = companyRepository.findAll().size();

        // Create the Company
        CompanyDTO companyDTO = companyMapper.toDto(company);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompanyMockMvc.perform(put("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Company in Elasticsearch
        verify(mockCompanySearchRepository, times(0)).save(company);
    }

    @Test
    @Transactional
    public void deleteCompany() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        int databaseSizeBeforeDelete = companyRepository.findAll().size();

        // Get the company
        restCompanyMockMvc.perform(delete("/api/companies/{id}", company.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Company in Elasticsearch
        verify(mockCompanySearchRepository, times(1)).deleteById(company.getId());
    }

    @Test
    @Transactional
    public void searchCompany() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);
        when(mockCompanySearchRepository.search(queryStringQuery("id:" + company.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(company), PageRequest.of(0, 1), 1));
        // Search the company
        restCompanyMockMvc.perform(get("/api/_search/companies?query=id:" + company.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(company.getId().intValue())))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].companyLogoContentType").value(hasItem(DEFAULT_COMPANY_LOGO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].companyLogo").value(hasItem(Base64Utils.encodeToString(DEFAULT_COMPANY_LOGO))))
            .andExpect(jsonPath("$.[*].companyLogoUrl").value(hasItem(DEFAULT_COMPANY_LOGO_URL)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Company.class);
        Company company1 = new Company();
        company1.setId(1L);
        Company company2 = new Company();
        company2.setId(company1.getId());
        assertThat(company1).isEqualTo(company2);
        company2.setId(2L);
        assertThat(company1).isNotEqualTo(company2);
        company1.setId(null);
        assertThat(company1).isNotEqualTo(company2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompanyDTO.class);
        CompanyDTO companyDTO1 = new CompanyDTO();
        companyDTO1.setId(1L);
        CompanyDTO companyDTO2 = new CompanyDTO();
        assertThat(companyDTO1).isNotEqualTo(companyDTO2);
        companyDTO2.setId(companyDTO1.getId());
        assertThat(companyDTO1).isEqualTo(companyDTO2);
        companyDTO2.setId(2L);
        assertThat(companyDTO1).isNotEqualTo(companyDTO2);
        companyDTO1.setId(null);
        assertThat(companyDTO1).isNotEqualTo(companyDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(companyMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(companyMapper.fromId(null)).isNull();
    }
}
