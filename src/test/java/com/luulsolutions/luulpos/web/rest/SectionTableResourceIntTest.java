package com.luulsolutions.luulpos.web.rest;

import com.luulsolutions.luulpos.LuulposApp;

import com.luulsolutions.luulpos.domain.SectionTable;
import com.luulsolutions.luulpos.domain.ShopSection;
import com.luulsolutions.luulpos.repository.SectionTableRepository;
import com.luulsolutions.luulpos.repository.search.SectionTableSearchRepository;
import com.luulsolutions.luulpos.service.SectionTableService;
import com.luulsolutions.luulpos.service.dto.SectionTableDTO;
import com.luulsolutions.luulpos.service.mapper.SectionTableMapper;
import com.luulsolutions.luulpos.web.rest.errors.ExceptionTranslator;
import com.luulsolutions.luulpos.service.dto.SectionTableCriteria;
import com.luulsolutions.luulpos.service.SectionTableQueryService;

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
 * Test class for the SectionTableResource REST controller.
 *
 * @see SectionTableResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LuulposApp.class)
public class SectionTableResourceIntTest {

    private static final Integer DEFAULT_TABLE_NUMBER = 1;
    private static final Integer UPDATED_TABLE_NUMBER = 2;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private SectionTableRepository sectionTableRepository;

    @Autowired
    private SectionTableMapper sectionTableMapper;

    @Autowired
    private SectionTableService sectionTableService;

    /**
     * This repository is mocked in the com.luulsolutions.luulpos.repository.search test package.
     *
     * @see com.luulsolutions.luulpos.repository.search.SectionTableSearchRepositoryMockConfiguration
     */
    @Autowired
    private SectionTableSearchRepository mockSectionTableSearchRepository;

    @Autowired
    private SectionTableQueryService sectionTableQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSectionTableMockMvc;

    private SectionTable sectionTable;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SectionTableResource sectionTableResource = new SectionTableResource(sectionTableService, sectionTableQueryService);
        this.restSectionTableMockMvc = MockMvcBuilders.standaloneSetup(sectionTableResource)
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
    public static SectionTable createEntity(EntityManager em) {
        SectionTable sectionTable = new SectionTable()
            .tableNumber(DEFAULT_TABLE_NUMBER)
            .description(DEFAULT_DESCRIPTION);
        return sectionTable;
    }

    @Before
    public void initTest() {
        sectionTable = createEntity(em);
    }

    @Test
    @Transactional
    public void createSectionTable() throws Exception {
        int databaseSizeBeforeCreate = sectionTableRepository.findAll().size();

        // Create the SectionTable
        SectionTableDTO sectionTableDTO = sectionTableMapper.toDto(sectionTable);
        restSectionTableMockMvc.perform(post("/api/section-tables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sectionTableDTO)))
            .andExpect(status().isCreated());

        // Validate the SectionTable in the database
        List<SectionTable> sectionTableList = sectionTableRepository.findAll();
        assertThat(sectionTableList).hasSize(databaseSizeBeforeCreate + 1);
        SectionTable testSectionTable = sectionTableList.get(sectionTableList.size() - 1);
        assertThat(testSectionTable.getTableNumber()).isEqualTo(DEFAULT_TABLE_NUMBER);
        assertThat(testSectionTable.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the SectionTable in Elasticsearch
        verify(mockSectionTableSearchRepository, times(1)).save(testSectionTable);
    }

    @Test
    @Transactional
    public void createSectionTableWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sectionTableRepository.findAll().size();

        // Create the SectionTable with an existing ID
        sectionTable.setId(1L);
        SectionTableDTO sectionTableDTO = sectionTableMapper.toDto(sectionTable);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSectionTableMockMvc.perform(post("/api/section-tables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sectionTableDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SectionTable in the database
        List<SectionTable> sectionTableList = sectionTableRepository.findAll();
        assertThat(sectionTableList).hasSize(databaseSizeBeforeCreate);

        // Validate the SectionTable in Elasticsearch
        verify(mockSectionTableSearchRepository, times(0)).save(sectionTable);
    }

    @Test
    @Transactional
    public void getAllSectionTables() throws Exception {
        // Initialize the database
        sectionTableRepository.saveAndFlush(sectionTable);

        // Get all the sectionTableList
        restSectionTableMockMvc.perform(get("/api/section-tables?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sectionTable.getId().intValue())))
            .andExpect(jsonPath("$.[*].tableNumber").value(hasItem(DEFAULT_TABLE_NUMBER)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
    
    @Test
    @Transactional
    public void getSectionTable() throws Exception {
        // Initialize the database
        sectionTableRepository.saveAndFlush(sectionTable);

        // Get the sectionTable
        restSectionTableMockMvc.perform(get("/api/section-tables/{id}", sectionTable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sectionTable.getId().intValue()))
            .andExpect(jsonPath("$.tableNumber").value(DEFAULT_TABLE_NUMBER))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getAllSectionTablesByTableNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        sectionTableRepository.saveAndFlush(sectionTable);

        // Get all the sectionTableList where tableNumber equals to DEFAULT_TABLE_NUMBER
        defaultSectionTableShouldBeFound("tableNumber.equals=" + DEFAULT_TABLE_NUMBER);

        // Get all the sectionTableList where tableNumber equals to UPDATED_TABLE_NUMBER
        defaultSectionTableShouldNotBeFound("tableNumber.equals=" + UPDATED_TABLE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllSectionTablesByTableNumberIsInShouldWork() throws Exception {
        // Initialize the database
        sectionTableRepository.saveAndFlush(sectionTable);

        // Get all the sectionTableList where tableNumber in DEFAULT_TABLE_NUMBER or UPDATED_TABLE_NUMBER
        defaultSectionTableShouldBeFound("tableNumber.in=" + DEFAULT_TABLE_NUMBER + "," + UPDATED_TABLE_NUMBER);

        // Get all the sectionTableList where tableNumber equals to UPDATED_TABLE_NUMBER
        defaultSectionTableShouldNotBeFound("tableNumber.in=" + UPDATED_TABLE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllSectionTablesByTableNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        sectionTableRepository.saveAndFlush(sectionTable);

        // Get all the sectionTableList where tableNumber is not null
        defaultSectionTableShouldBeFound("tableNumber.specified=true");

        // Get all the sectionTableList where tableNumber is null
        defaultSectionTableShouldNotBeFound("tableNumber.specified=false");
    }

    @Test
    @Transactional
    public void getAllSectionTablesByTableNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sectionTableRepository.saveAndFlush(sectionTable);

        // Get all the sectionTableList where tableNumber greater than or equals to DEFAULT_TABLE_NUMBER
        defaultSectionTableShouldBeFound("tableNumber.greaterOrEqualThan=" + DEFAULT_TABLE_NUMBER);

        // Get all the sectionTableList where tableNumber greater than or equals to UPDATED_TABLE_NUMBER
        defaultSectionTableShouldNotBeFound("tableNumber.greaterOrEqualThan=" + UPDATED_TABLE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllSectionTablesByTableNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        sectionTableRepository.saveAndFlush(sectionTable);

        // Get all the sectionTableList where tableNumber less than or equals to DEFAULT_TABLE_NUMBER
        defaultSectionTableShouldNotBeFound("tableNumber.lessThan=" + DEFAULT_TABLE_NUMBER);

        // Get all the sectionTableList where tableNumber less than or equals to UPDATED_TABLE_NUMBER
        defaultSectionTableShouldBeFound("tableNumber.lessThan=" + UPDATED_TABLE_NUMBER);
    }


    @Test
    @Transactional
    public void getAllSectionTablesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        sectionTableRepository.saveAndFlush(sectionTable);

        // Get all the sectionTableList where description equals to DEFAULT_DESCRIPTION
        defaultSectionTableShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the sectionTableList where description equals to UPDATED_DESCRIPTION
        defaultSectionTableShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllSectionTablesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        sectionTableRepository.saveAndFlush(sectionTable);

        // Get all the sectionTableList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultSectionTableShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the sectionTableList where description equals to UPDATED_DESCRIPTION
        defaultSectionTableShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllSectionTablesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        sectionTableRepository.saveAndFlush(sectionTable);

        // Get all the sectionTableList where description is not null
        defaultSectionTableShouldBeFound("description.specified=true");

        // Get all the sectionTableList where description is null
        defaultSectionTableShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllSectionTablesByShopSectionsIsEqualToSomething() throws Exception {
        // Initialize the database
        ShopSection shopSections = ShopSectionResourceIntTest.createEntity(em);
        em.persist(shopSections);
        em.flush();
        sectionTable.setShopSections(shopSections);
        sectionTableRepository.saveAndFlush(sectionTable);
        Long shopSectionsId = shopSections.getId();

        // Get all the sectionTableList where shopSections equals to shopSectionsId
        defaultSectionTableShouldBeFound("shopSectionsId.equals=" + shopSectionsId);

        // Get all the sectionTableList where shopSections equals to shopSectionsId + 1
        defaultSectionTableShouldNotBeFound("shopSectionsId.equals=" + (shopSectionsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultSectionTableShouldBeFound(String filter) throws Exception {
        restSectionTableMockMvc.perform(get("/api/section-tables?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sectionTable.getId().intValue())))
            .andExpect(jsonPath("$.[*].tableNumber").value(hasItem(DEFAULT_TABLE_NUMBER)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));

        // Check, that the count call also returns 1
        restSectionTableMockMvc.perform(get("/api/section-tables/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultSectionTableShouldNotBeFound(String filter) throws Exception {
        restSectionTableMockMvc.perform(get("/api/section-tables?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSectionTableMockMvc.perform(get("/api/section-tables/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingSectionTable() throws Exception {
        // Get the sectionTable
        restSectionTableMockMvc.perform(get("/api/section-tables/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSectionTable() throws Exception {
        // Initialize the database
        sectionTableRepository.saveAndFlush(sectionTable);

        int databaseSizeBeforeUpdate = sectionTableRepository.findAll().size();

        // Update the sectionTable
        SectionTable updatedSectionTable = sectionTableRepository.findById(sectionTable.getId()).get();
        // Disconnect from session so that the updates on updatedSectionTable are not directly saved in db
        em.detach(updatedSectionTable);
        updatedSectionTable
            .tableNumber(UPDATED_TABLE_NUMBER)
            .description(UPDATED_DESCRIPTION);
        SectionTableDTO sectionTableDTO = sectionTableMapper.toDto(updatedSectionTable);

        restSectionTableMockMvc.perform(put("/api/section-tables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sectionTableDTO)))
            .andExpect(status().isOk());

        // Validate the SectionTable in the database
        List<SectionTable> sectionTableList = sectionTableRepository.findAll();
        assertThat(sectionTableList).hasSize(databaseSizeBeforeUpdate);
        SectionTable testSectionTable = sectionTableList.get(sectionTableList.size() - 1);
        assertThat(testSectionTable.getTableNumber()).isEqualTo(UPDATED_TABLE_NUMBER);
        assertThat(testSectionTable.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the SectionTable in Elasticsearch
        verify(mockSectionTableSearchRepository, times(1)).save(testSectionTable);
    }

    @Test
    @Transactional
    public void updateNonExistingSectionTable() throws Exception {
        int databaseSizeBeforeUpdate = sectionTableRepository.findAll().size();

        // Create the SectionTable
        SectionTableDTO sectionTableDTO = sectionTableMapper.toDto(sectionTable);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSectionTableMockMvc.perform(put("/api/section-tables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sectionTableDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SectionTable in the database
        List<SectionTable> sectionTableList = sectionTableRepository.findAll();
        assertThat(sectionTableList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SectionTable in Elasticsearch
        verify(mockSectionTableSearchRepository, times(0)).save(sectionTable);
    }

    @Test
    @Transactional
    public void deleteSectionTable() throws Exception {
        // Initialize the database
        sectionTableRepository.saveAndFlush(sectionTable);

        int databaseSizeBeforeDelete = sectionTableRepository.findAll().size();

        // Get the sectionTable
        restSectionTableMockMvc.perform(delete("/api/section-tables/{id}", sectionTable.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SectionTable> sectionTableList = sectionTableRepository.findAll();
        assertThat(sectionTableList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the SectionTable in Elasticsearch
        verify(mockSectionTableSearchRepository, times(1)).deleteById(sectionTable.getId());
    }

    @Test
    @Transactional
    public void searchSectionTable() throws Exception {
        // Initialize the database
        sectionTableRepository.saveAndFlush(sectionTable);
        when(mockSectionTableSearchRepository.search(queryStringQuery("id:" + sectionTable.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(sectionTable), PageRequest.of(0, 1), 1));
        // Search the sectionTable
        restSectionTableMockMvc.perform(get("/api/_search/section-tables?query=id:" + sectionTable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sectionTable.getId().intValue())))
            .andExpect(jsonPath("$.[*].tableNumber").value(hasItem(DEFAULT_TABLE_NUMBER)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SectionTable.class);
        SectionTable sectionTable1 = new SectionTable();
        sectionTable1.setId(1L);
        SectionTable sectionTable2 = new SectionTable();
        sectionTable2.setId(sectionTable1.getId());
        assertThat(sectionTable1).isEqualTo(sectionTable2);
        sectionTable2.setId(2L);
        assertThat(sectionTable1).isNotEqualTo(sectionTable2);
        sectionTable1.setId(null);
        assertThat(sectionTable1).isNotEqualTo(sectionTable2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SectionTableDTO.class);
        SectionTableDTO sectionTableDTO1 = new SectionTableDTO();
        sectionTableDTO1.setId(1L);
        SectionTableDTO sectionTableDTO2 = new SectionTableDTO();
        assertThat(sectionTableDTO1).isNotEqualTo(sectionTableDTO2);
        sectionTableDTO2.setId(sectionTableDTO1.getId());
        assertThat(sectionTableDTO1).isEqualTo(sectionTableDTO2);
        sectionTableDTO2.setId(2L);
        assertThat(sectionTableDTO1).isNotEqualTo(sectionTableDTO2);
        sectionTableDTO1.setId(null);
        assertThat(sectionTableDTO1).isNotEqualTo(sectionTableDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(sectionTableMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(sectionTableMapper.fromId(null)).isNull();
    }
}
