package com.luulsolutions.luulpos.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.luulsolutions.luulpos.service.SectionTableService;
import com.luulsolutions.luulpos.service.ShopChangeService;
import com.luulsolutions.luulpos.service.ShopSectionService;
import com.luulsolutions.luulpos.web.rest.errors.BadRequestAlertException;
import com.luulsolutions.luulpos.web.rest.util.HeaderUtil;
import com.luulsolutions.luulpos.web.rest.util.PaginationUtil;
import com.luulsolutions.luulpos.service.dto.SectionTableDTO;
import com.luulsolutions.luulpos.service.dto.ShopSectionDTO;
import com.luulsolutions.luulpos.utils.CommonUtils;
import com.luulsolutions.luulpos.service.dto.SectionTableCriteria;
import com.luulsolutions.luulpos.domain.SectionTable;
import com.luulsolutions.luulpos.repository.SectionTableRepository;
import com.luulsolutions.luulpos.service.SectionTableQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing SectionTable.
 */
@RestController
@RequestMapping("/api")
public class SectionTableResource {

    private final Logger log = LoggerFactory.getLogger(SectionTableResource.class);

    private static final String ENTITY_NAME = "sectionTable";

    private final SectionTableService sectionTableService;

    private final SectionTableQueryService sectionTableQueryService;
    
    @Autowired
    ShopChangeService shopChangeService;
    
    @Autowired
    ShopSectionService shopSectionService;
    
    @Autowired
    SectionTableRepository sectionTableRepository;

    public SectionTableResource(SectionTableService sectionTableService, SectionTableQueryService sectionTableQueryService) {
        this.sectionTableService = sectionTableService;
        this.sectionTableQueryService = sectionTableQueryService;
    }

    /**
     * POST  /section-tables : Create a new sectionTable.
     *
     * @param sectionTableDTO the sectionTableDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sectionTableDTO, or with status 400 (Bad Request) if the sectionTable has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/section-tables")
    @Timed
    public ResponseEntity<SectionTableDTO> createSectionTable(@RequestBody SectionTableDTO sectionTableDTO) throws URISyntaxException {
        log.debug("REST request to save SectionTable : {}", sectionTableDTO);
        if (sectionTableDTO.getId() != null) {
            throw new BadRequestAlertException("A new sectionTable cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SectionTableDTO result = sectionTableService.save(sectionTableDTO);
        Optional <ShopSectionDTO> shopSection = shopSectionService.findOne(result.getShopSectionsId()); 
        CommonUtils.saveShopChange(shopChangeService, shopSection.get().getShopId(), "SectionTable", "New SectionTable created", shopSection.get().getShopShopName()); 
        return ResponseEntity.created(new URI("/api/section-tables/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /section-tables : Updates an existing sectionTable.
     *
     * @param sectionTableDTO the sectionTableDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sectionTableDTO,
     * or with status 400 (Bad Request) if the sectionTableDTO is not valid,
     * or with status 500 (Internal Server Error) if the sectionTableDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/section-tables")
    @Timed
    public ResponseEntity<SectionTableDTO> updateSectionTable(@RequestBody SectionTableDTO sectionTableDTO) throws URISyntaxException {
        log.debug("REST request to update SectionTable : {}", sectionTableDTO);
        if (sectionTableDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SectionTableDTO result = sectionTableService.save(sectionTableDTO);
        Optional <ShopSectionDTO> shopSection = shopSectionService.findOne(result.getShopSectionsId()); 
        CommonUtils.saveShopChange(shopChangeService, shopSection.get().getShopId(), "SectionTable", "Existing SectionTable updated", shopSection.get().getShopShopName());        
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, sectionTableDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /section-tables : get all the sectionTables.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of sectionTables in body
     */
    @GetMapping("/section-tables")
    @Timed
    public ResponseEntity<List<SectionTableDTO>> getAllSectionTables(Pageable pageable) {
        log.debug("REST request to get a page of SectionTables");
        Pageable pageable2 =  PageRequest.of(pageable.getPageNumber(),2000);
        Page<SectionTableDTO> page = sectionTableService.findAll(pageable2);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/section-tables");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /section-tables/:id : get the "id" sectionTable.
     *
     * @param id the id of the sectionTableDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sectionTableDTO, or with status 404 (Not Found)
     */
    @GetMapping("/section-tables/{id}")
    @Timed
    public ResponseEntity<SectionTableDTO> getSectionTable(@PathVariable Long id) {
        log.debug("REST request to get SectionTable : {}", id);
        Optional<SectionTableDTO> sectionTableDTO = sectionTableService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sectionTableDTO);
    }

    /**
     * DELETE  /section-tables/:id : delete the "id" sectionTable.
     *
     * @param id the id of the sectionTableDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/section-tables/{id}")
    @Timed
    public ResponseEntity<Void> deleteSectionTable(@PathVariable Long id) {
        log.debug("REST request to delete SectionTable : {}", id);
        SectionTable table = sectionTableRepository.getOne(id); 
        String shopName = table.getShopSections().getSectionName();
        long shopId = table.getShopSections().getId();
        sectionTableService.delete(id);
        CommonUtils.saveShopChange(shopChangeService, shopId, "SectionTable", "Existing SectionTable deleted", shopName);        
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/section-tables?query=:query : search for the sectionTable corresponding
     * to the query.
     *
     * @param query the query of the sectionTable search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/section-tables")
    @Timed
    public ResponseEntity<List<SectionTableDTO>> searchSectionTables(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of SectionTables for query {}", query);
        Page<SectionTableDTO> page = sectionTableService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/section-tables");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
    * GET  /section-tables/count : count all the sectionTables.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/section-tables/count")
    @Timed
    public ResponseEntity<Long> countSectionTables(SectionTableCriteria criteria) {
        log.debug("REST request to count SectionTables by criteria: {}", criteria);
        return ResponseEntity.ok().body(sectionTableQueryService.countByCriteria(criteria));
    }

 

}
