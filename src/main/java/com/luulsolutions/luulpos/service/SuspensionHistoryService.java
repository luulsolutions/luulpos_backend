package com.luulsolutions.luulpos.service;

import com.luulsolutions.luulpos.domain.SuspensionHistory;
import com.luulsolutions.luulpos.repository.SuspensionHistoryRepository;
import com.luulsolutions.luulpos.repository.search.SuspensionHistorySearchRepository;
import com.luulsolutions.luulpos.service.dto.SuspensionHistoryDTO;
import com.luulsolutions.luulpos.service.mapper.SuspensionHistoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing SuspensionHistory.
 */
@Service
@Transactional
public class SuspensionHistoryService {

    private final Logger log = LoggerFactory.getLogger(SuspensionHistoryService.class);

    private final SuspensionHistoryRepository suspensionHistoryRepository;

    private final SuspensionHistoryMapper suspensionHistoryMapper;

    private final SuspensionHistorySearchRepository suspensionHistorySearchRepository;

    public SuspensionHistoryService(SuspensionHistoryRepository suspensionHistoryRepository, SuspensionHistoryMapper suspensionHistoryMapper, SuspensionHistorySearchRepository suspensionHistorySearchRepository) {
        this.suspensionHistoryRepository = suspensionHistoryRepository;
        this.suspensionHistoryMapper = suspensionHistoryMapper;
        this.suspensionHistorySearchRepository = suspensionHistorySearchRepository;
    }

    /**
     * Save a suspensionHistory.
     *
     * @param suspensionHistoryDTO the entity to save
     * @return the persisted entity
     */
    public SuspensionHistoryDTO save(SuspensionHistoryDTO suspensionHistoryDTO) {
        log.debug("Request to save SuspensionHistory : {}", suspensionHistoryDTO);

        SuspensionHistory suspensionHistory = suspensionHistoryMapper.toEntity(suspensionHistoryDTO);
        suspensionHistory = suspensionHistoryRepository.save(suspensionHistory);
        SuspensionHistoryDTO result = suspensionHistoryMapper.toDto(suspensionHistory);
        suspensionHistorySearchRepository.save(suspensionHistory);
        return result;
    }

    /**
     * Get all the suspensionHistories.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SuspensionHistoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SuspensionHistories");
        return suspensionHistoryRepository.findAll(pageable)
            .map(suspensionHistoryMapper::toDto);
    }


    /**
     * Get one suspensionHistory by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<SuspensionHistoryDTO> findOne(Long id) {
        log.debug("Request to get SuspensionHistory : {}", id);
        return suspensionHistoryRepository.findById(id)
            .map(suspensionHistoryMapper::toDto);
    }

    /**
     * Delete the suspensionHistory by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete SuspensionHistory : {}", id);
        suspensionHistoryRepository.deleteById(id);
        suspensionHistorySearchRepository.deleteById(id);
    }

    /**
     * Search for the suspensionHistory corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SuspensionHistoryDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SuspensionHistories for query {}", query);
        return suspensionHistorySearchRepository.search(queryStringQuery(query), pageable)
            .map(suspensionHistoryMapper::toDto);
    }
}
