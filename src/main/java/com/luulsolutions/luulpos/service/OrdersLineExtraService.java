package com.luulsolutions.luulpos.service;

import com.luulsolutions.luulpos.domain.OrdersLineExtra;
import com.luulsolutions.luulpos.repository.OrdersLineExtraRepository;
import com.luulsolutions.luulpos.repository.search.OrdersLineExtraSearchRepository;
import com.luulsolutions.luulpos.service.dto.OrdersLineExtraDTO;
import com.luulsolutions.luulpos.service.mapper.OrdersLineExtraMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing OrdersLineExtra.
 */
@Service
@Transactional
public class OrdersLineExtraService {

    private final Logger log = LoggerFactory.getLogger(OrdersLineExtraService.class);

    private final OrdersLineExtraRepository ordersLineExtraRepository;

    private final OrdersLineExtraMapper ordersLineExtraMapper;

    private final OrdersLineExtraSearchRepository ordersLineExtraSearchRepository;

    public OrdersLineExtraService(OrdersLineExtraRepository ordersLineExtraRepository, OrdersLineExtraMapper ordersLineExtraMapper, OrdersLineExtraSearchRepository ordersLineExtraSearchRepository) {
        this.ordersLineExtraRepository = ordersLineExtraRepository;
        this.ordersLineExtraMapper = ordersLineExtraMapper;
        this.ordersLineExtraSearchRepository = ordersLineExtraSearchRepository;
    }

    /**
     * Save a ordersLineExtra.
     *
     * @param ordersLineExtraDTO the entity to save
     * @return the persisted entity
     */
    public OrdersLineExtraDTO save(OrdersLineExtraDTO ordersLineExtraDTO) {
        log.debug("Request to save OrdersLineExtra : {}", ordersLineExtraDTO);

        OrdersLineExtra ordersLineExtra = ordersLineExtraMapper.toEntity(ordersLineExtraDTO);
        ordersLineExtra = ordersLineExtraRepository.save(ordersLineExtra);
        OrdersLineExtraDTO result = ordersLineExtraMapper.toDto(ordersLineExtra);
        ordersLineExtraSearchRepository.save(ordersLineExtra);
        return result;
    }

    /**
     * Get all the ordersLineExtras.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<OrdersLineExtraDTO> findAll(Pageable pageable) {
        log.debug("Request to get all OrdersLineExtras");
        return ordersLineExtraRepository.findAll(pageable)
            .map(ordersLineExtraMapper::toDto);
    }


    /**
     * Get one ordersLineExtra by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<OrdersLineExtraDTO> findOne(Long id) {
        log.debug("Request to get OrdersLineExtra : {}", id);
        return ordersLineExtraRepository.findById(id)
            .map(ordersLineExtraMapper::toDto);
    }

    /**
     * Delete the ordersLineExtra by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete OrdersLineExtra : {}", id);
        ordersLineExtraRepository.deleteById(id);
        ordersLineExtraSearchRepository.deleteById(id);
    }

    /**
     * Search for the ordersLineExtra corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<OrdersLineExtraDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of OrdersLineExtras for query {}", query);
        return ordersLineExtraSearchRepository.search(queryStringQuery(query), pageable)
            .map(ordersLineExtraMapper::toDto);
    }
}
