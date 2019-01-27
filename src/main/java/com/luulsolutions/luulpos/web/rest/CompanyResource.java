package com.luulsolutions.luulpos.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.luulsolutions.luulpos.service.CompanyService;
import com.luulsolutions.luulpos.web.rest.errors.BadRequestAlertException;
import com.luulsolutions.luulpos.web.rest.util.HeaderUtil;
import com.luulsolutions.luulpos.web.rest.util.PaginationUtil;
import com.luulsolutions.luulpos.service.dto.CompanyDTO;
import com.luulsolutions.luulpos.service.s3.S3Service;
import com.luulsolutions.luulpos.utils.CommonUtils;
import com.luulsolutions.luulpos.utils.Constants;
import com.luulsolutions.luulpos.service.dto.CompanyCriteria;
import com.luulsolutions.luulpos.service.CompanyQueryService;
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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Company.
 */
@RestController
@RequestMapping("/api")
public class CompanyResource {

    private final Logger log = LoggerFactory.getLogger(CompanyResource.class);

    private static final String ENTITY_NAME = "company";

    private final CompanyService companyService;

    private final CompanyQueryService companyQueryService;
    
    @Autowired
    private S3Service s3Service;

    public CompanyResource(CompanyService companyService, CompanyQueryService companyQueryService) {
        this.companyService = companyService;
        this.companyQueryService = companyQueryService;
    }

  

    /**
    * GET  /companies/count : count all the companies.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/companies/count")
    @Timed
    public ResponseEntity<Long> countCompanies(CompanyCriteria criteria) {
        log.debug("REST request to count Companies by criteria: {}", criteria);
        return ResponseEntity.ok().body(companyQueryService.countByCriteria(criteria));
    }

    /**
     * POST  /companies : Create a new company.
     *
     * @param companyDTO the companyDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new companyDTO, or with status 400 (Bad Request) if the company has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/companies")
    @Timed
    public ResponseEntity<CompanyDTO> createCompany(@Valid @RequestBody CompanyDTO companyDTO) throws URISyntaxException {
        log.debug("REST request to save Company : {}", companyDTO);
        if (companyDTO.getId() != null) {
            throw new BadRequestAlertException("A new company cannot already have an ID", ENTITY_NAME, "idexists");
        } 
        
        CompanyDTO result = companyService.save(companyDTO);
        String fileName = "Company" + result.getId()  + ".png";
        String companyLogoUrl = "https://s3-eu-west-1.amazonaws.com/luulpos/" + fileName;
        result.setCompanyLogoUrl(companyLogoUrl);
        byte[] imageBytes = CommonUtils.resize(CommonUtils.createImageFromBytes(companyDTO.getCompanyLogo()),  Constants.FULL_IMAGE_HEIGHT,  Constants.FULL_IMAGE_WIDTH);
        CommonUtils.uploadToS3(imageBytes,fileName,s3Service.getAmazonS3() );
        CompanyDTO result2 = companyService.save(result);
        result2.setCompanyLogo(null);
        result2.setCompanyLogoContentType(null);
        companyService.save(result2);
        return ResponseEntity.created(new URI("/api/companies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result); 
    }

    /**
     * PUT  /companies : Updates an existing company.
     *
     * @param companyDTO the companyDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated companyDTO,
     * or with status 400 (Bad Request) if the companyDTO is not valid,
     * or with status 500 (Internal Server Error) if the companyDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/companies")
    @Timed
    public ResponseEntity<CompanyDTO> updateCompany(@Valid @RequestBody CompanyDTO companyDTO) throws URISyntaxException {
        log.debug("REST request to update Company : {}", companyDTO);
        if (companyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        
        CompanyDTO result = companyService.save(companyDTO);
        if (result.getCompanyLogo() != null) {
        String fileName = "Company" + result.getId()  + ".png";
        String companyLogoUrl = "https://s3-eu-west-1.amazonaws.com/luulpos/" + fileName;
        byte[] imageBytes = CommonUtils.resize(CommonUtils.createImageFromBytes(companyDTO.getCompanyLogo()),  Constants.FULL_IMAGE_HEIGHT,  Constants.FULL_IMAGE_WIDTH);
        result.setCompanyLogoUrl(companyLogoUrl);
        CommonUtils.uploadToS3(imageBytes,fileName,s3Service.getAmazonS3() );
        CompanyDTO result2 = companyService.save(result);
        result2.setCompanyLogo(null);
        result2.setCompanyLogoContentType(null);
        companyService.save(result2);
        }
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, companyDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /companies : get all the companies.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of companies in body
     */
    @GetMapping("/companies")
    @Timed
    public ResponseEntity<List<CompanyDTO>> getAllCompanies(Pageable pageable) {
        log.debug("REST request to get a page of Companies");
        Pageable pageable2 =  PageRequest.of(pageable.getPageNumber(),2000);
        Page<CompanyDTO> page = companyService.findAll(pageable2);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/companies");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /companies/:id : get the "id" company.
     *
     * @param id the id of the companyDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the companyDTO, or with status 404 (Not Found)
     */
    @GetMapping("/companies/{id}")
    @Timed
    public ResponseEntity<CompanyDTO> getCompany(@PathVariable Long id) {
        log.debug("REST request to get Company : {}", id);
        Optional<CompanyDTO> companyDTO = companyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(companyDTO);
    }

    /**
     * DELETE  /companies/:id : delete the "id" company.
     *
     * @param id the id of the companyDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/companies/{id}")
    @Timed
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        log.debug("REST request to delete Company : {}", id);
        companyService.delete(id);
        CommonUtils.deleteFromS3("Company" + id + ".png", s3Service.getAmazonS3());
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/companies?query=:query : search for the company corresponding
     * to the query.
     *
     * @param query the query of the company search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/companies")
    @Timed
    public ResponseEntity<List<CompanyDTO>> searchCompanies(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Companies for query {}", query);
        Page<CompanyDTO> page = companyService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/companies");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
}
