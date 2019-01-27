package com.luulsolutions.luulpos.service;

import com.luulsolutions.luulpos.domain.SystemConfig;
import com.luulsolutions.luulpos.repository.SystemConfigRepository;
import com.luulsolutions.luulpos.repository.search.SystemConfigSearchRepository;
import com.luulsolutions.luulpos.service.dto.SystemConfigDTO;
import com.luulsolutions.luulpos.service.mapper.SystemConfigMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing SystemConfig.
 */
@Service
@Transactional
public class SystemConfigService {

    private final Logger log = LoggerFactory.getLogger(SystemConfigService.class);

    private final SystemConfigRepository systemConfigRepository;

    private final SystemConfigMapper systemConfigMapper;

    private final SystemConfigSearchRepository systemConfigSearchRepository;

    public SystemConfigService(SystemConfigRepository systemConfigRepository, SystemConfigMapper systemConfigMapper, SystemConfigSearchRepository systemConfigSearchRepository) {
        this.systemConfigRepository = systemConfigRepository;
        this.systemConfigMapper = systemConfigMapper;
        this.systemConfigSearchRepository = systemConfigSearchRepository;
    }

    /**
     * Save a systemConfig.
     *
     * @param systemConfigDTO the entity to save
     * @return the persisted entity
     */
    public SystemConfigDTO save(SystemConfigDTO systemConfigDTO) {
        log.debug("Request to save SystemConfig : {}", systemConfigDTO);

        SystemConfig systemConfig = systemConfigMapper.toEntity(systemConfigDTO);
        systemConfig = systemConfigRepository.save(systemConfig);
        SystemConfigDTO result = systemConfigMapper.toDto(systemConfig);
        systemConfigSearchRepository.save(systemConfig);
        return result;
    }

    /**
     * Get all the systemConfigs.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SystemConfigDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SystemConfigs");
        return systemConfigRepository.findAll(pageable)
            .map(systemConfigMapper::toDto);
    }


    /**
     * Get one systemConfig by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<SystemConfigDTO> findOne(Long id) {
        log.debug("Request to get SystemConfig : {}", id);
        return systemConfigRepository.findById(id)
            .map(systemConfigMapper::toDto);
    }

    /**
     * Delete the systemConfig by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete SystemConfig : {}", id);
        systemConfigRepository.deleteById(id);
        systemConfigSearchRepository.deleteById(id);
    }

    /**
     * Search for the systemConfig corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SystemConfigDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SystemConfigs for query {}", query);
        return systemConfigSearchRepository.search(queryStringQuery(query), pageable)
            .map(systemConfigMapper::toDto);
    }
    
    @Transactional(readOnly = true)
    public List<SystemConfigDTO> findAllByShopId(long shopId) {
        log.debug("Request to get all SystemConfigs by shopId");
        List<SystemConfig> productList = systemConfigRepository.findAllByShopId(shopId);
        return systemConfigMapper.toDto(productList);
           
    }
}
