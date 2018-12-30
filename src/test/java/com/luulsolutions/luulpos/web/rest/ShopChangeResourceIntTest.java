package com.luulsolutions.luulpos.web.rest;

import com.luulsolutions.luulpos.LuulposApp;

import com.luulsolutions.luulpos.domain.ShopChange;
import com.luulsolutions.luulpos.domain.Shop;
import com.luulsolutions.luulpos.domain.Profile;
import com.luulsolutions.luulpos.repository.ShopChangeRepository;
import com.luulsolutions.luulpos.repository.search.ShopChangeSearchRepository;
import com.luulsolutions.luulpos.service.ShopChangeService;
import com.luulsolutions.luulpos.service.dto.ShopChangeDTO;
import com.luulsolutions.luulpos.service.mapper.ShopChangeMapper;
import com.luulsolutions.luulpos.web.rest.errors.ExceptionTranslator;
import com.luulsolutions.luulpos.service.dto.ShopChangeCriteria;
import com.luulsolutions.luulpos.service.ShopChangeQueryService;

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
 * Test class for the ShopChangeResource REST controller.
 *
 * @see ShopChangeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LuulposApp.class)
public class ShopChangeResourceIntTest {

    private static final String DEFAULT_CHANGE = "AAAAAAAAAA";
    private static final String UPDATED_CHANGE = "BBBBBBBBBB";

    private static final String DEFAULT_CHANGED_ENTITY = "AAAAAAAAAA";
    private static final String UPDATED_CHANGED_ENTITY = "BBBBBBBBBB";

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CHANGE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CHANGE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private ShopChangeRepository shopChangeRepository;

    @Autowired
    private ShopChangeMapper shopChangeMapper;

    @Autowired
    private ShopChangeService shopChangeService;

    /**
     * This repository is mocked in the com.luulsolutions.luulpos.repository.search test package.
     *
     * @see com.luulsolutions.luulpos.repository.search.ShopChangeSearchRepositoryMockConfiguration
     */
    @Autowired
    private ShopChangeSearchRepository mockShopChangeSearchRepository;

    @Autowired
    private ShopChangeQueryService shopChangeQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restShopChangeMockMvc;

    private ShopChange shopChange;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ShopChangeResource shopChangeResource = new ShopChangeResource(shopChangeService, shopChangeQueryService);
        this.restShopChangeMockMvc = MockMvcBuilders.standaloneSetup(shopChangeResource)
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
    public static ShopChange createEntity(EntityManager em) {
        ShopChange shopChange = new ShopChange()
            .change(DEFAULT_CHANGE)
            .changedEntity(DEFAULT_CHANGED_ENTITY)
            .note(DEFAULT_NOTE)
            .changeDate(DEFAULT_CHANGE_DATE);
        return shopChange;
    }

    @Before
    public void initTest() {
        shopChange = createEntity(em);
    }

    @Test
    @Transactional
    public void createShopChange() throws Exception {
        int databaseSizeBeforeCreate = shopChangeRepository.findAll().size();

        // Create the ShopChange
        ShopChangeDTO shopChangeDTO = shopChangeMapper.toDto(shopChange);
        restShopChangeMockMvc.perform(post("/api/shop-changes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shopChangeDTO)))
            .andExpect(status().isCreated());

        // Validate the ShopChange in the database
        List<ShopChange> shopChangeList = shopChangeRepository.findAll();
        assertThat(shopChangeList).hasSize(databaseSizeBeforeCreate + 1);
        ShopChange testShopChange = shopChangeList.get(shopChangeList.size() - 1);
        assertThat(testShopChange.getChange()).isEqualTo(DEFAULT_CHANGE);
        assertThat(testShopChange.getChangedEntity()).isEqualTo(DEFAULT_CHANGED_ENTITY);
        assertThat(testShopChange.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testShopChange.getChangeDate()).isEqualTo(DEFAULT_CHANGE_DATE);

        // Validate the ShopChange in Elasticsearch
        verify(mockShopChangeSearchRepository, times(1)).save(testShopChange);
    }

    @Test
    @Transactional
    public void createShopChangeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = shopChangeRepository.findAll().size();

        // Create the ShopChange with an existing ID
        shopChange.setId(1L);
        ShopChangeDTO shopChangeDTO = shopChangeMapper.toDto(shopChange);

        // An entity with an existing ID cannot be created, so this API call must fail
        restShopChangeMockMvc.perform(post("/api/shop-changes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shopChangeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ShopChange in the database
        List<ShopChange> shopChangeList = shopChangeRepository.findAll();
        assertThat(shopChangeList).hasSize(databaseSizeBeforeCreate);

        // Validate the ShopChange in Elasticsearch
        verify(mockShopChangeSearchRepository, times(0)).save(shopChange);
    }

    @Test
    @Transactional
    public void getAllShopChanges() throws Exception {
        // Initialize the database
        shopChangeRepository.saveAndFlush(shopChange);

        // Get all the shopChangeList
        restShopChangeMockMvc.perform(get("/api/shop-changes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shopChange.getId().intValue())))
            .andExpect(jsonPath("$.[*].change").value(hasItem(DEFAULT_CHANGE.toString())))
            .andExpect(jsonPath("$.[*].changedEntity").value(hasItem(DEFAULT_CHANGED_ENTITY.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.toString())))
            .andExpect(jsonPath("$.[*].changeDate").value(hasItem(sameInstant(DEFAULT_CHANGE_DATE))));
    }
    
    @Test
    @Transactional
    public void getShopChange() throws Exception {
        // Initialize the database
        shopChangeRepository.saveAndFlush(shopChange);

        // Get the shopChange
        restShopChangeMockMvc.perform(get("/api/shop-changes/{id}", shopChange.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(shopChange.getId().intValue()))
            .andExpect(jsonPath("$.change").value(DEFAULT_CHANGE.toString()))
            .andExpect(jsonPath("$.changedEntity").value(DEFAULT_CHANGED_ENTITY.toString()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE.toString()))
            .andExpect(jsonPath("$.changeDate").value(sameInstant(DEFAULT_CHANGE_DATE)));
    }

    @Test
    @Transactional
    public void getAllShopChangesByChangeIsEqualToSomething() throws Exception {
        // Initialize the database
        shopChangeRepository.saveAndFlush(shopChange);

        // Get all the shopChangeList where change equals to DEFAULT_CHANGE
        defaultShopChangeShouldBeFound("change.equals=" + DEFAULT_CHANGE);

        // Get all the shopChangeList where change equals to UPDATED_CHANGE
        defaultShopChangeShouldNotBeFound("change.equals=" + UPDATED_CHANGE);
    }

    @Test
    @Transactional
    public void getAllShopChangesByChangeIsInShouldWork() throws Exception {
        // Initialize the database
        shopChangeRepository.saveAndFlush(shopChange);

        // Get all the shopChangeList where change in DEFAULT_CHANGE or UPDATED_CHANGE
        defaultShopChangeShouldBeFound("change.in=" + DEFAULT_CHANGE + "," + UPDATED_CHANGE);

        // Get all the shopChangeList where change equals to UPDATED_CHANGE
        defaultShopChangeShouldNotBeFound("change.in=" + UPDATED_CHANGE);
    }

    @Test
    @Transactional
    public void getAllShopChangesByChangeIsNullOrNotNull() throws Exception {
        // Initialize the database
        shopChangeRepository.saveAndFlush(shopChange);

        // Get all the shopChangeList where change is not null
        defaultShopChangeShouldBeFound("change.specified=true");

        // Get all the shopChangeList where change is null
        defaultShopChangeShouldNotBeFound("change.specified=false");
    }

    @Test
    @Transactional
    public void getAllShopChangesByChangedEntityIsEqualToSomething() throws Exception {
        // Initialize the database
        shopChangeRepository.saveAndFlush(shopChange);

        // Get all the shopChangeList where changedEntity equals to DEFAULT_CHANGED_ENTITY
        defaultShopChangeShouldBeFound("changedEntity.equals=" + DEFAULT_CHANGED_ENTITY);

        // Get all the shopChangeList where changedEntity equals to UPDATED_CHANGED_ENTITY
        defaultShopChangeShouldNotBeFound("changedEntity.equals=" + UPDATED_CHANGED_ENTITY);
    }

    @Test
    @Transactional
    public void getAllShopChangesByChangedEntityIsInShouldWork() throws Exception {
        // Initialize the database
        shopChangeRepository.saveAndFlush(shopChange);

        // Get all the shopChangeList where changedEntity in DEFAULT_CHANGED_ENTITY or UPDATED_CHANGED_ENTITY
        defaultShopChangeShouldBeFound("changedEntity.in=" + DEFAULT_CHANGED_ENTITY + "," + UPDATED_CHANGED_ENTITY);

        // Get all the shopChangeList where changedEntity equals to UPDATED_CHANGED_ENTITY
        defaultShopChangeShouldNotBeFound("changedEntity.in=" + UPDATED_CHANGED_ENTITY);
    }

    @Test
    @Transactional
    public void getAllShopChangesByChangedEntityIsNullOrNotNull() throws Exception {
        // Initialize the database
        shopChangeRepository.saveAndFlush(shopChange);

        // Get all the shopChangeList where changedEntity is not null
        defaultShopChangeShouldBeFound("changedEntity.specified=true");

        // Get all the shopChangeList where changedEntity is null
        defaultShopChangeShouldNotBeFound("changedEntity.specified=false");
    }

    @Test
    @Transactional
    public void getAllShopChangesByNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        shopChangeRepository.saveAndFlush(shopChange);

        // Get all the shopChangeList where note equals to DEFAULT_NOTE
        defaultShopChangeShouldBeFound("note.equals=" + DEFAULT_NOTE);

        // Get all the shopChangeList where note equals to UPDATED_NOTE
        defaultShopChangeShouldNotBeFound("note.equals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllShopChangesByNoteIsInShouldWork() throws Exception {
        // Initialize the database
        shopChangeRepository.saveAndFlush(shopChange);

        // Get all the shopChangeList where note in DEFAULT_NOTE or UPDATED_NOTE
        defaultShopChangeShouldBeFound("note.in=" + DEFAULT_NOTE + "," + UPDATED_NOTE);

        // Get all the shopChangeList where note equals to UPDATED_NOTE
        defaultShopChangeShouldNotBeFound("note.in=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllShopChangesByNoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        shopChangeRepository.saveAndFlush(shopChange);

        // Get all the shopChangeList where note is not null
        defaultShopChangeShouldBeFound("note.specified=true");

        // Get all the shopChangeList where note is null
        defaultShopChangeShouldNotBeFound("note.specified=false");
    }

    @Test
    @Transactional
    public void getAllShopChangesByChangeDateIsEqualToSomething() throws Exception {
        // Initialize the database
        shopChangeRepository.saveAndFlush(shopChange);

        // Get all the shopChangeList where changeDate equals to DEFAULT_CHANGE_DATE
        defaultShopChangeShouldBeFound("changeDate.equals=" + DEFAULT_CHANGE_DATE);

        // Get all the shopChangeList where changeDate equals to UPDATED_CHANGE_DATE
        defaultShopChangeShouldNotBeFound("changeDate.equals=" + UPDATED_CHANGE_DATE);
    }

    @Test
    @Transactional
    public void getAllShopChangesByChangeDateIsInShouldWork() throws Exception {
        // Initialize the database
        shopChangeRepository.saveAndFlush(shopChange);

        // Get all the shopChangeList where changeDate in DEFAULT_CHANGE_DATE or UPDATED_CHANGE_DATE
        defaultShopChangeShouldBeFound("changeDate.in=" + DEFAULT_CHANGE_DATE + "," + UPDATED_CHANGE_DATE);

        // Get all the shopChangeList where changeDate equals to UPDATED_CHANGE_DATE
        defaultShopChangeShouldNotBeFound("changeDate.in=" + UPDATED_CHANGE_DATE);
    }

    @Test
    @Transactional
    public void getAllShopChangesByChangeDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        shopChangeRepository.saveAndFlush(shopChange);

        // Get all the shopChangeList where changeDate is not null
        defaultShopChangeShouldBeFound("changeDate.specified=true");

        // Get all the shopChangeList where changeDate is null
        defaultShopChangeShouldNotBeFound("changeDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllShopChangesByChangeDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        shopChangeRepository.saveAndFlush(shopChange);

        // Get all the shopChangeList where changeDate greater than or equals to DEFAULT_CHANGE_DATE
        defaultShopChangeShouldBeFound("changeDate.greaterOrEqualThan=" + DEFAULT_CHANGE_DATE);

        // Get all the shopChangeList where changeDate greater than or equals to UPDATED_CHANGE_DATE
        defaultShopChangeShouldNotBeFound("changeDate.greaterOrEqualThan=" + UPDATED_CHANGE_DATE);
    }

    @Test
    @Transactional
    public void getAllShopChangesByChangeDateIsLessThanSomething() throws Exception {
        // Initialize the database
        shopChangeRepository.saveAndFlush(shopChange);

        // Get all the shopChangeList where changeDate less than or equals to DEFAULT_CHANGE_DATE
        defaultShopChangeShouldNotBeFound("changeDate.lessThan=" + DEFAULT_CHANGE_DATE);

        // Get all the shopChangeList where changeDate less than or equals to UPDATED_CHANGE_DATE
        defaultShopChangeShouldBeFound("changeDate.lessThan=" + UPDATED_CHANGE_DATE);
    }


    @Test
    @Transactional
    public void getAllShopChangesByShopIsEqualToSomething() throws Exception {
        // Initialize the database
        Shop shop = ShopResourceIntTest.createEntity(em);
        em.persist(shop);
        em.flush();
        shopChange.setShop(shop);
        shopChangeRepository.saveAndFlush(shopChange);
        Long shopId = shop.getId();

        // Get all the shopChangeList where shop equals to shopId
        defaultShopChangeShouldBeFound("shopId.equals=" + shopId);

        // Get all the shopChangeList where shop equals to shopId + 1
        defaultShopChangeShouldNotBeFound("shopId.equals=" + (shopId + 1));
    }


    @Test
    @Transactional
    public void getAllShopChangesByChangedByIsEqualToSomething() throws Exception {
        // Initialize the database
        Profile changedBy = ProfileResourceIntTest.createEntity(em);
        em.persist(changedBy);
        em.flush();
        shopChange.setChangedBy(changedBy);
        shopChangeRepository.saveAndFlush(shopChange);
        Long changedById = changedBy.getId();

        // Get all the shopChangeList where changedBy equals to changedById
        defaultShopChangeShouldBeFound("changedById.equals=" + changedById);

        // Get all the shopChangeList where changedBy equals to changedById + 1
        defaultShopChangeShouldNotBeFound("changedById.equals=" + (changedById + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultShopChangeShouldBeFound(String filter) throws Exception {
        restShopChangeMockMvc.perform(get("/api/shop-changes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shopChange.getId().intValue())))
            .andExpect(jsonPath("$.[*].change").value(hasItem(DEFAULT_CHANGE.toString())))
            .andExpect(jsonPath("$.[*].changedEntity").value(hasItem(DEFAULT_CHANGED_ENTITY.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.toString())))
            .andExpect(jsonPath("$.[*].changeDate").value(hasItem(sameInstant(DEFAULT_CHANGE_DATE))));

        // Check, that the count call also returns 1
        restShopChangeMockMvc.perform(get("/api/shop-changes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultShopChangeShouldNotBeFound(String filter) throws Exception {
        restShopChangeMockMvc.perform(get("/api/shop-changes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restShopChangeMockMvc.perform(get("/api/shop-changes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingShopChange() throws Exception {
        // Get the shopChange
        restShopChangeMockMvc.perform(get("/api/shop-changes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateShopChange() throws Exception {
        // Initialize the database
        shopChangeRepository.saveAndFlush(shopChange);

        int databaseSizeBeforeUpdate = shopChangeRepository.findAll().size();

        // Update the shopChange
        ShopChange updatedShopChange = shopChangeRepository.findById(shopChange.getId()).get();
        // Disconnect from session so that the updates on updatedShopChange are not directly saved in db
        em.detach(updatedShopChange);
        updatedShopChange
            .change(UPDATED_CHANGE)
            .changedEntity(UPDATED_CHANGED_ENTITY)
            .note(UPDATED_NOTE)
            .changeDate(UPDATED_CHANGE_DATE);
        ShopChangeDTO shopChangeDTO = shopChangeMapper.toDto(updatedShopChange);

        restShopChangeMockMvc.perform(put("/api/shop-changes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shopChangeDTO)))
            .andExpect(status().isOk());

        // Validate the ShopChange in the database
        List<ShopChange> shopChangeList = shopChangeRepository.findAll();
        assertThat(shopChangeList).hasSize(databaseSizeBeforeUpdate);
        ShopChange testShopChange = shopChangeList.get(shopChangeList.size() - 1);
        assertThat(testShopChange.getChange()).isEqualTo(UPDATED_CHANGE);
        assertThat(testShopChange.getChangedEntity()).isEqualTo(UPDATED_CHANGED_ENTITY);
        assertThat(testShopChange.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testShopChange.getChangeDate()).isEqualTo(UPDATED_CHANGE_DATE);

        // Validate the ShopChange in Elasticsearch
        verify(mockShopChangeSearchRepository, times(1)).save(testShopChange);
    }

    @Test
    @Transactional
    public void updateNonExistingShopChange() throws Exception {
        int databaseSizeBeforeUpdate = shopChangeRepository.findAll().size();

        // Create the ShopChange
        ShopChangeDTO shopChangeDTO = shopChangeMapper.toDto(shopChange);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShopChangeMockMvc.perform(put("/api/shop-changes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shopChangeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ShopChange in the database
        List<ShopChange> shopChangeList = shopChangeRepository.findAll();
        assertThat(shopChangeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ShopChange in Elasticsearch
        verify(mockShopChangeSearchRepository, times(0)).save(shopChange);
    }

    @Test
    @Transactional
    public void deleteShopChange() throws Exception {
        // Initialize the database
        shopChangeRepository.saveAndFlush(shopChange);

        int databaseSizeBeforeDelete = shopChangeRepository.findAll().size();

        // Get the shopChange
        restShopChangeMockMvc.perform(delete("/api/shop-changes/{id}", shopChange.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ShopChange> shopChangeList = shopChangeRepository.findAll();
        assertThat(shopChangeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ShopChange in Elasticsearch
        verify(mockShopChangeSearchRepository, times(1)).deleteById(shopChange.getId());
    }

    @Test
    @Transactional
    public void searchShopChange() throws Exception {
        // Initialize the database
        shopChangeRepository.saveAndFlush(shopChange);
        when(mockShopChangeSearchRepository.search(queryStringQuery("id:" + shopChange.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(shopChange), PageRequest.of(0, 1), 1));
        // Search the shopChange
        restShopChangeMockMvc.perform(get("/api/_search/shop-changes?query=id:" + shopChange.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shopChange.getId().intValue())))
            .andExpect(jsonPath("$.[*].change").value(hasItem(DEFAULT_CHANGE)))
            .andExpect(jsonPath("$.[*].changedEntity").value(hasItem(DEFAULT_CHANGED_ENTITY)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].changeDate").value(hasItem(sameInstant(DEFAULT_CHANGE_DATE))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShopChange.class);
        ShopChange shopChange1 = new ShopChange();
        shopChange1.setId(1L);
        ShopChange shopChange2 = new ShopChange();
        shopChange2.setId(shopChange1.getId());
        assertThat(shopChange1).isEqualTo(shopChange2);
        shopChange2.setId(2L);
        assertThat(shopChange1).isNotEqualTo(shopChange2);
        shopChange1.setId(null);
        assertThat(shopChange1).isNotEqualTo(shopChange2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShopChangeDTO.class);
        ShopChangeDTO shopChangeDTO1 = new ShopChangeDTO();
        shopChangeDTO1.setId(1L);
        ShopChangeDTO shopChangeDTO2 = new ShopChangeDTO();
        assertThat(shopChangeDTO1).isNotEqualTo(shopChangeDTO2);
        shopChangeDTO2.setId(shopChangeDTO1.getId());
        assertThat(shopChangeDTO1).isEqualTo(shopChangeDTO2);
        shopChangeDTO2.setId(2L);
        assertThat(shopChangeDTO1).isNotEqualTo(shopChangeDTO2);
        shopChangeDTO1.setId(null);
        assertThat(shopChangeDTO1).isNotEqualTo(shopChangeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(shopChangeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(shopChangeMapper.fromId(null)).isNull();
    }
}
