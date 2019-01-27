package com.luulsolutions.luulpos.service;

import com.luulsolutions.luulpos.domain.ShopChange;
import com.luulsolutions.luulpos.repository.ShopChangeRepository;
import com.luulsolutions.luulpos.repository.search.ShopChangeSearchRepository;
import com.luulsolutions.luulpos.service.dto.ShopChangeDTO;
import com.luulsolutions.luulpos.service.mapper.ShopChangeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ShopChange.
 */
@Service
@Transactional
public class ShopChangeService {

    private final Logger log = LoggerFactory.getLogger(ShopChangeService.class);

    private final ShopChangeRepository shopChangeRepository;

    private final ShopChangeMapper shopChangeMapper;

    private final ShopChangeSearchRepository shopChangeSearchRepository;

    public ShopChangeService(ShopChangeRepository shopChangeRepository, ShopChangeMapper shopChangeMapper, ShopChangeSearchRepository shopChangeSearchRepository) {
        this.shopChangeRepository = shopChangeRepository;
        this.shopChangeMapper = shopChangeMapper;
        this.shopChangeSearchRepository = shopChangeSearchRepository;
    }

    /**
     * Save a shopChange.
     *
     * @param shopChangeDTO the entity to save
     * @return the persisted entity
     */
    public ShopChangeDTO save(ShopChangeDTO shopChangeDTO) {
        log.debug("Request to save ShopChange : {}", shopChangeDTO);

        ShopChange shopChange = shopChangeMapper.toEntity(shopChangeDTO);
        shopChange = shopChangeRepository.save(shopChange);
        ShopChangeDTO result = shopChangeMapper.toDto(shopChange);
        shopChangeSearchRepository.save(shopChange);
        return result;
    }

    /**
     * Get all the shopChanges.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ShopChangeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ShopChanges");
        return shopChangeRepository.findAll(pageable)
            .map(shopChangeMapper::toDto);
    }


    /**
     * Get one shopChange by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ShopChangeDTO> findOne(Long id) {
        log.debug("Request to get ShopChange : {}", id);
        return shopChangeRepository.findById(id)
            .map(shopChangeMapper::toDto);
    }

    /**
     * Delete the shopChange by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ShopChange : {}", id);
        shopChangeRepository.deleteById(id);
        shopChangeSearchRepository.deleteById(id);
    }

    /**
     * Search for the shopChange corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ShopChangeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ShopChanges for query {}", query);
        return shopChangeSearchRepository.search(queryStringQuery(query), pageable)
            .map(shopChangeMapper::toDto);
    }
    
	public ShopChangeDTO findFirstByShopId(Long shopId) {
		log.debug("Request to get all findAllByShopId");
		ShopChange shopChange = shopChangeRepository.findFirstByShopIdOrderByIdDesc(shopId);
        return shopChangeMapper.toDto(shopChange);
	}
}
