package com.luulsolutions.luulpos.service;

import com.luulsolutions.luulpos.domain.Orders;
import com.luulsolutions.luulpos.repository.OrdersRepository;
import com.luulsolutions.luulpos.repository.search.OrdersSearchRepository;
import com.luulsolutions.luulpos.service.dto.OrdersDTO;
import com.luulsolutions.luulpos.service.mapper.OrdersMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Orders.
 */
@Service
@Transactional
public class OrdersService {

    private final Logger log = LoggerFactory.getLogger(OrdersService.class);

    private final OrdersRepository ordersRepository;

    private final OrdersMapper ordersMapper;

    private final OrdersSearchRepository ordersSearchRepository;

    public OrdersService(OrdersRepository ordersRepository, OrdersMapper ordersMapper, OrdersSearchRepository ordersSearchRepository) {
        this.ordersRepository = ordersRepository;
        this.ordersMapper = ordersMapper;
        this.ordersSearchRepository = ordersSearchRepository;
    }

    /**
     * Save a orders.
     *
     * @param ordersDTO the entity to save
     * @return the persisted entity
     */
    public OrdersDTO save(OrdersDTO ordersDTO) {
        log.debug("Request to save Orders : {}", ordersDTO);

        Orders orders = ordersMapper.toEntity(ordersDTO);
        orders = ordersRepository.save(orders);
        OrdersDTO result = ordersMapper.toDto(orders);
        ordersSearchRepository.save(orders);
        return result;
    }

    /**
     * Get all the orders.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<OrdersDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Orders");
        return ordersRepository.findAll(pageable)
            .map(ordersMapper::toDto);
    }


    /**
     * Get one orders by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<OrdersDTO> findOne(Long id) {
        log.debug("Request to get Orders : {}", id);
        return ordersRepository.findById(id)
            .map(ordersMapper::toDto);
    }

    /**
     * Delete the orders by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Orders : {}", id);
        ordersRepository.deleteById(id);
        ordersSearchRepository.deleteById(id);
    }

    /**
     * Search for the orders corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<OrdersDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Orders for query {}", query);
        return ordersSearchRepository.search(queryStringQuery(query), pageable)
            .map(ordersMapper::toDto);
    }
}
