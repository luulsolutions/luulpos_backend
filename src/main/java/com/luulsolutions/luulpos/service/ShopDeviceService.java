package com.luulsolutions.luulpos.service;

import com.luulsolutions.luulpos.domain.ShopDevice;
import com.luulsolutions.luulpos.repository.ShopDeviceRepository;
import com.luulsolutions.luulpos.repository.search.ShopDeviceSearchRepository;
import com.luulsolutions.luulpos.service.dto.ShopDeviceDTO;
import com.luulsolutions.luulpos.service.mapper.ShopDeviceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ShopDevice.
 */
@Service
@Transactional
public class ShopDeviceService {

    private final Logger log = LoggerFactory.getLogger(ShopDeviceService.class);

    private final ShopDeviceRepository shopDeviceRepository;

    private final ShopDeviceMapper shopDeviceMapper;

    private final ShopDeviceSearchRepository shopDeviceSearchRepository;

    public ShopDeviceService(ShopDeviceRepository shopDeviceRepository, ShopDeviceMapper shopDeviceMapper, ShopDeviceSearchRepository shopDeviceSearchRepository) {
        this.shopDeviceRepository = shopDeviceRepository;
        this.shopDeviceMapper = shopDeviceMapper;
        this.shopDeviceSearchRepository = shopDeviceSearchRepository;
    }

    /**
     * Save a shopDevice.
     *
     * @param shopDeviceDTO the entity to save
     * @return the persisted entity
     */
    public ShopDeviceDTO save(ShopDeviceDTO shopDeviceDTO) {
        log.debug("Request to save ShopDevice : {}", shopDeviceDTO);

        ShopDevice shopDevice = shopDeviceMapper.toEntity(shopDeviceDTO);
        shopDevice = shopDeviceRepository.save(shopDevice);
        ShopDeviceDTO result = shopDeviceMapper.toDto(shopDevice);
        shopDeviceSearchRepository.save(shopDevice);
        return result;
    }

    /**
     * Get all the shopDevices.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ShopDeviceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ShopDevices");
        return shopDeviceRepository.findAll(pageable)
            .map(shopDeviceMapper::toDto);
    }


    /**
     * Get one shopDevice by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ShopDeviceDTO> findOne(Long id) {
        log.debug("Request to get ShopDevice : {}", id);
        return shopDeviceRepository.findById(id)
            .map(shopDeviceMapper::toDto);
    }

    /**
     * Delete the shopDevice by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ShopDevice : {}", id);
        shopDeviceRepository.deleteById(id);
        shopDeviceSearchRepository.deleteById(id);
    }

    /**
     * Search for the shopDevice corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ShopDeviceDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ShopDevices for query {}", query);
        return shopDeviceSearchRepository.search(queryStringQuery(query), pageable)
            .map(shopDeviceMapper::toDto);
    }
}
