package com.luulsolutions.luulpos.service;

import com.luulsolutions.luulpos.domain.OrdersLine;
import com.luulsolutions.luulpos.repository.OrdersLineRepository;
import com.luulsolutions.luulpos.repository.search.OrdersLineSearchRepository;
import com.luulsolutions.luulpos.service.dto.OrdersLineDTO;
import com.luulsolutions.luulpos.service.mapper.OrdersLineMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing OrdersLine.
 */
@Service
@Transactional
public class OrdersLineService {

    private final Logger log = LoggerFactory.getLogger(OrdersLineService.class);

    private final OrdersLineRepository ordersLineRepository;

    private final OrdersLineMapper ordersLineMapper;

    private final OrdersLineSearchRepository ordersLineSearchRepository;

    public OrdersLineService(OrdersLineRepository ordersLineRepository, OrdersLineMapper ordersLineMapper, OrdersLineSearchRepository ordersLineSearchRepository) {
        this.ordersLineRepository = ordersLineRepository;
        this.ordersLineMapper = ordersLineMapper;
        this.ordersLineSearchRepository = ordersLineSearchRepository;
    }

    /**
     * Save a ordersLine.
     *
     * @param ordersLineDTO the entity to save
     * @return the persisted entity
     */
    public OrdersLineDTO save(OrdersLineDTO ordersLineDTO) {
        log.debug("Request to save OrdersLine : {}", ordersLineDTO);

        OrdersLine ordersLine = ordersLineMapper.toEntity(ordersLineDTO);
        ordersLine = ordersLineRepository.save(ordersLine);
        OrdersLineDTO result = ordersLineMapper.toDto(ordersLine);
        ordersLineSearchRepository.save(ordersLine);
        return result;
    }

    /**
     * Get all the ordersLines.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<OrdersLineDTO> findAll(Pageable pageable) {
        log.debug("Request to get all OrdersLines");
        return ordersLineRepository.findAll(pageable)
            .map(ordersLineMapper::toDto);
    }


    /**
     * Get one ordersLine by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<OrdersLineDTO> findOne(Long id) {
        log.debug("Request to get OrdersLine : {}", id);
        return ordersLineRepository.findById(id)
            .map(ordersLineMapper::toDto);
    }

    /**
     * Delete the ordersLine by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete OrdersLine : {}", id);
        ordersLineRepository.deleteById(id);
        ordersLineSearchRepository.deleteById(id);
    }

    /**
     * Search for the ordersLine corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<OrdersLineDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of OrdersLines for query {}", query);
        return ordersLineSearchRepository.search(queryStringQuery(query), pageable)
            .map(ordersLineMapper::toDto);
    }
}
