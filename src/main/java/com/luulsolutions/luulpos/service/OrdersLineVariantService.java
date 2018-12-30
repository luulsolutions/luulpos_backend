package com.luulsolutions.luulpos.service;

import com.luulsolutions.luulpos.domain.OrdersLineVariant;
import com.luulsolutions.luulpos.repository.OrdersLineVariantRepository;
import com.luulsolutions.luulpos.repository.search.OrdersLineVariantSearchRepository;
import com.luulsolutions.luulpos.service.dto.OrdersLineVariantDTO;
import com.luulsolutions.luulpos.service.mapper.OrdersLineVariantMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing OrdersLineVariant.
 */
@Service
@Transactional
public class OrdersLineVariantService {

    private final Logger log = LoggerFactory.getLogger(OrdersLineVariantService.class);

    private final OrdersLineVariantRepository ordersLineVariantRepository;

    private final OrdersLineVariantMapper ordersLineVariantMapper;

    private final OrdersLineVariantSearchRepository ordersLineVariantSearchRepository;

    public OrdersLineVariantService(OrdersLineVariantRepository ordersLineVariantRepository, OrdersLineVariantMapper ordersLineVariantMapper, OrdersLineVariantSearchRepository ordersLineVariantSearchRepository) {
        this.ordersLineVariantRepository = ordersLineVariantRepository;
        this.ordersLineVariantMapper = ordersLineVariantMapper;
        this.ordersLineVariantSearchRepository = ordersLineVariantSearchRepository;
    }

    /**
     * Save a ordersLineVariant.
     *
     * @param ordersLineVariantDTO the entity to save
     * @return the persisted entity
     */
    public OrdersLineVariantDTO save(OrdersLineVariantDTO ordersLineVariantDTO) {
        log.debug("Request to save OrdersLineVariant : {}", ordersLineVariantDTO);

        OrdersLineVariant ordersLineVariant = ordersLineVariantMapper.toEntity(ordersLineVariantDTO);
        ordersLineVariant = ordersLineVariantRepository.save(ordersLineVariant);
        OrdersLineVariantDTO result = ordersLineVariantMapper.toDto(ordersLineVariant);
        ordersLineVariantSearchRepository.save(ordersLineVariant);
        return result;
    }

    /**
     * Get all the ordersLineVariants.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<OrdersLineVariantDTO> findAll(Pageable pageable) {
        log.debug("Request to get all OrdersLineVariants");
        return ordersLineVariantRepository.findAll(pageable)
            .map(ordersLineVariantMapper::toDto);
    }


    /**
     * Get one ordersLineVariant by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<OrdersLineVariantDTO> findOne(Long id) {
        log.debug("Request to get OrdersLineVariant : {}", id);
        return ordersLineVariantRepository.findById(id)
            .map(ordersLineVariantMapper::toDto);
    }

    /**
     * Delete the ordersLineVariant by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete OrdersLineVariant : {}", id);
        ordersLineVariantRepository.deleteById(id);
        ordersLineVariantSearchRepository.deleteById(id);
    }

    /**
     * Search for the ordersLineVariant corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<OrdersLineVariantDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of OrdersLineVariants for query {}", query);
        return ordersLineVariantSearchRepository.search(queryStringQuery(query), pageable)
            .map(ordersLineVariantMapper::toDto);
    }
}
