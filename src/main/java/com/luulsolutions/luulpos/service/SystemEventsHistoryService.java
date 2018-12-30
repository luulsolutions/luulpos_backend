package com.luulsolutions.luulpos.service;

import com.luulsolutions.luulpos.domain.SystemEventsHistory;
import com.luulsolutions.luulpos.repository.SystemEventsHistoryRepository;
import com.luulsolutions.luulpos.repository.search.SystemEventsHistorySearchRepository;
import com.luulsolutions.luulpos.service.dto.SystemEventsHistoryDTO;
import com.luulsolutions.luulpos.service.mapper.SystemEventsHistoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing SystemEventsHistory.
 */
@Service
@Transactional
public class SystemEventsHistoryService {

    private final Logger log = LoggerFactory.getLogger(SystemEventsHistoryService.class);

    private final SystemEventsHistoryRepository systemEventsHistoryRepository;

    private final SystemEventsHistoryMapper systemEventsHistoryMapper;

    private final SystemEventsHistorySearchRepository systemEventsHistorySearchRepository;

    public SystemEventsHistoryService(SystemEventsHistoryRepository systemEventsHistoryRepository, SystemEventsHistoryMapper systemEventsHistoryMapper, SystemEventsHistorySearchRepository systemEventsHistorySearchRepository) {
        this.systemEventsHistoryRepository = systemEventsHistoryRepository;
        this.systemEventsHistoryMapper = systemEventsHistoryMapper;
        this.systemEventsHistorySearchRepository = systemEventsHistorySearchRepository;
    }

    /**
     * Save a systemEventsHistory.
     *
     * @param systemEventsHistoryDTO the entity to save
     * @return the persisted entity
     */
    public SystemEventsHistoryDTO save(SystemEventsHistoryDTO systemEventsHistoryDTO) {
        log.debug("Request to save SystemEventsHistory : {}", systemEventsHistoryDTO);

        SystemEventsHistory systemEventsHistory = systemEventsHistoryMapper.toEntity(systemEventsHistoryDTO);
        systemEventsHistory = systemEventsHistoryRepository.save(systemEventsHistory);
        SystemEventsHistoryDTO result = systemEventsHistoryMapper.toDto(systemEventsHistory);
        systemEventsHistorySearchRepository.save(systemEventsHistory);
        return result;
    }

    /**
     * Get all the systemEventsHistories.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SystemEventsHistoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SystemEventsHistories");
        return systemEventsHistoryRepository.findAll(pageable)
            .map(systemEventsHistoryMapper::toDto);
    }


    /**
     * Get one systemEventsHistory by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<SystemEventsHistoryDTO> findOne(Long id) {
        log.debug("Request to get SystemEventsHistory : {}", id);
        return systemEventsHistoryRepository.findById(id)
            .map(systemEventsHistoryMapper::toDto);
    }

    /**
     * Delete the systemEventsHistory by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete SystemEventsHistory : {}", id);
        systemEventsHistoryRepository.deleteById(id);
        systemEventsHistorySearchRepository.deleteById(id);
    }

    /**
     * Search for the systemEventsHistory corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SystemEventsHistoryDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SystemEventsHistories for query {}", query);
        return systemEventsHistorySearchRepository.search(queryStringQuery(query), pageable)
            .map(systemEventsHistoryMapper::toDto);
    }
}
