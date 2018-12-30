package com.luulsolutions.luulpos.service;

import com.luulsolutions.luulpos.domain.Shop;
import com.luulsolutions.luulpos.repository.ShopRepository;
import com.luulsolutions.luulpos.repository.search.ShopSearchRepository;
import com.luulsolutions.luulpos.service.dto.ShopDTO;
import com.luulsolutions.luulpos.service.mapper.ShopMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Shop.
 */
@Service
@Transactional
public class ShopService {

    private final Logger log = LoggerFactory.getLogger(ShopService.class);

    private final ShopRepository shopRepository;

    private final ShopMapper shopMapper;

    private final ShopSearchRepository shopSearchRepository;

    public ShopService(ShopRepository shopRepository, ShopMapper shopMapper, ShopSearchRepository shopSearchRepository) {
        this.shopRepository = shopRepository;
        this.shopMapper = shopMapper;
        this.shopSearchRepository = shopSearchRepository;
    }

    /**
     * Save a shop.
     *
     * @param shopDTO the entity to save
     * @return the persisted entity
     */
    public ShopDTO save(ShopDTO shopDTO) {
        log.debug("Request to save Shop : {}", shopDTO);

        Shop shop = shopMapper.toEntity(shopDTO);
        shop = shopRepository.save(shop);
        ShopDTO result = shopMapper.toDto(shop);
        shopSearchRepository.save(shop);
        return result;
    }

    /**
     * Get all the shops.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ShopDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Shops");
        return shopRepository.findAll(pageable)
            .map(shopMapper::toDto);
    }


    /**
     * Get one shop by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ShopDTO> findOne(Long id) {
        log.debug("Request to get Shop : {}", id);
        return shopRepository.findById(id)
            .map(shopMapper::toDto);
    }

    /**
     * Delete the shop by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Shop : {}", id);
        shopRepository.deleteById(id);
        shopSearchRepository.deleteById(id);
    }

    /**
     * Search for the shop corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ShopDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Shops for query {}", query);
        return shopSearchRepository.search(queryStringQuery(query), pageable)
            .map(shopMapper::toDto);
    }
}
