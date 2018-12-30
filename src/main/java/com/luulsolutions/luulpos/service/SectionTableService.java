package com.luulsolutions.luulpos.service;

import com.luulsolutions.luulpos.domain.SectionTable;
import com.luulsolutions.luulpos.repository.SectionTableRepository;
import com.luulsolutions.luulpos.repository.search.SectionTableSearchRepository;
import com.luulsolutions.luulpos.service.dto.SectionTableDTO;
import com.luulsolutions.luulpos.service.mapper.SectionTableMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing SectionTable.
 */
@Service
@Transactional
public class SectionTableService {

    private final Logger log = LoggerFactory.getLogger(SectionTableService.class);

    private final SectionTableRepository sectionTableRepository;

    private final SectionTableMapper sectionTableMapper;

    private final SectionTableSearchRepository sectionTableSearchRepository;

    public SectionTableService(SectionTableRepository sectionTableRepository, SectionTableMapper sectionTableMapper, SectionTableSearchRepository sectionTableSearchRepository) {
        this.sectionTableRepository = sectionTableRepository;
        this.sectionTableMapper = sectionTableMapper;
        this.sectionTableSearchRepository = sectionTableSearchRepository;
    }

    /**
     * Save a sectionTable.
     *
     * @param sectionTableDTO the entity to save
     * @return the persisted entity
     */
    public SectionTableDTO save(SectionTableDTO sectionTableDTO) {
        log.debug("Request to save SectionTable : {}", sectionTableDTO);

        SectionTable sectionTable = sectionTableMapper.toEntity(sectionTableDTO);
        sectionTable = sectionTableRepository.save(sectionTable);
        SectionTableDTO result = sectionTableMapper.toDto(sectionTable);
        sectionTableSearchRepository.save(sectionTable);
        return result;
    }

    /**
     * Get all the sectionTables.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SectionTableDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SectionTables");
        return sectionTableRepository.findAll(pageable)
            .map(sectionTableMapper::toDto);
    }


    /**
     * Get one sectionTable by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<SectionTableDTO> findOne(Long id) {
        log.debug("Request to get SectionTable : {}", id);
        return sectionTableRepository.findById(id)
            .map(sectionTableMapper::toDto);
    }

    /**
     * Delete the sectionTable by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete SectionTable : {}", id);
        sectionTableRepository.deleteById(id);
        sectionTableSearchRepository.deleteById(id);
    }

    /**
     * Search for the sectionTable corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SectionTableDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SectionTables for query {}", query);
        return sectionTableSearchRepository.search(queryStringQuery(query), pageable)
            .map(sectionTableMapper::toDto);
    }
}
