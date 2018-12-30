package com.luulsolutions.luulpos.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.luulsolutions.luulpos.domain.OrdersLineExtra;
import com.luulsolutions.luulpos.domain.*; // for static metamodels
import com.luulsolutions.luulpos.repository.OrdersLineExtraRepository;
import com.luulsolutions.luulpos.repository.search.OrdersLineExtraSearchRepository;
import com.luulsolutions.luulpos.service.dto.OrdersLineExtraCriteria;
import com.luulsolutions.luulpos.service.dto.OrdersLineExtraDTO;
import com.luulsolutions.luulpos.service.mapper.OrdersLineExtraMapper;

/**
 * Service for executing complex queries for OrdersLineExtra entities in the database.
 * The main input is a {@link OrdersLineExtraCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OrdersLineExtraDTO} or a {@link Page} of {@link OrdersLineExtraDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrdersLineExtraQueryService extends QueryService<OrdersLineExtra> {

    private final Logger log = LoggerFactory.getLogger(OrdersLineExtraQueryService.class);

    private final OrdersLineExtraRepository ordersLineExtraRepository;

    private final OrdersLineExtraMapper ordersLineExtraMapper;

    private final OrdersLineExtraSearchRepository ordersLineExtraSearchRepository;

    public OrdersLineExtraQueryService(OrdersLineExtraRepository ordersLineExtraRepository, OrdersLineExtraMapper ordersLineExtraMapper, OrdersLineExtraSearchRepository ordersLineExtraSearchRepository) {
        this.ordersLineExtraRepository = ordersLineExtraRepository;
        this.ordersLineExtraMapper = ordersLineExtraMapper;
        this.ordersLineExtraSearchRepository = ordersLineExtraSearchRepository;
    }

    /**
     * Return a {@link List} of {@link OrdersLineExtraDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OrdersLineExtraDTO> findByCriteria(OrdersLineExtraCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<OrdersLineExtra> specification = createSpecification(criteria);
        return ordersLineExtraMapper.toDto(ordersLineExtraRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link OrdersLineExtraDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrdersLineExtraDTO> findByCriteria(OrdersLineExtraCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OrdersLineExtra> specification = createSpecification(criteria);
        return ordersLineExtraRepository.findAll(specification, page)
            .map(ordersLineExtraMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrdersLineExtraCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<OrdersLineExtra> specification = createSpecification(criteria);
        return ordersLineExtraRepository.count(specification);
    }

    /**
     * Function to convert OrdersLineExtraCriteria to a {@link Specification}
     */
    private Specification<OrdersLineExtra> createSpecification(OrdersLineExtraCriteria criteria) {
        Specification<OrdersLineExtra> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), OrdersLineExtra_.id));
            }
            if (criteria.getOrdersLineExtraName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOrdersLineExtraName(), OrdersLineExtra_.ordersLineExtraName));
            }
            if (criteria.getOrdersLineExtraValue() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOrdersLineExtraValue(), OrdersLineExtra_.ordersLineExtraValue));
            }
            if (criteria.getOrdersLineExtraPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrdersLineExtraPrice(), OrdersLineExtra_.ordersLineExtraPrice));
            }
            if (criteria.getOrdersOptionDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOrdersOptionDescription(), OrdersLineExtra_.ordersOptionDescription));
            }
            if (criteria.getFullPhotoUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFullPhotoUrl(), OrdersLineExtra_.fullPhotoUrl));
            }
            if (criteria.getThumbnailPhotoUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getThumbnailPhotoUrl(), OrdersLineExtra_.thumbnailPhotoUrl));
            }
            if (criteria.getOrdersLineVariantId() != null) {
                specification = specification.and(buildSpecification(criteria.getOrdersLineVariantId(),
                    root -> root.join(OrdersLineExtra_.ordersLineVariant, JoinType.LEFT).get(OrdersLineVariant_.id)));
            }
        }
        return specification;
    }
}
