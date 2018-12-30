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

import com.luulsolutions.luulpos.domain.OrdersLineVariant;
import com.luulsolutions.luulpos.domain.*; // for static metamodels
import com.luulsolutions.luulpos.repository.OrdersLineVariantRepository;
import com.luulsolutions.luulpos.repository.search.OrdersLineVariantSearchRepository;
import com.luulsolutions.luulpos.service.dto.OrdersLineVariantCriteria;
import com.luulsolutions.luulpos.service.dto.OrdersLineVariantDTO;
import com.luulsolutions.luulpos.service.mapper.OrdersLineVariantMapper;

/**
 * Service for executing complex queries for OrdersLineVariant entities in the database.
 * The main input is a {@link OrdersLineVariantCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OrdersLineVariantDTO} or a {@link Page} of {@link OrdersLineVariantDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrdersLineVariantQueryService extends QueryService<OrdersLineVariant> {

    private final Logger log = LoggerFactory.getLogger(OrdersLineVariantQueryService.class);

    private final OrdersLineVariantRepository ordersLineVariantRepository;

    private final OrdersLineVariantMapper ordersLineVariantMapper;

    private final OrdersLineVariantSearchRepository ordersLineVariantSearchRepository;

    public OrdersLineVariantQueryService(OrdersLineVariantRepository ordersLineVariantRepository, OrdersLineVariantMapper ordersLineVariantMapper, OrdersLineVariantSearchRepository ordersLineVariantSearchRepository) {
        this.ordersLineVariantRepository = ordersLineVariantRepository;
        this.ordersLineVariantMapper = ordersLineVariantMapper;
        this.ordersLineVariantSearchRepository = ordersLineVariantSearchRepository;
    }

    /**
     * Return a {@link List} of {@link OrdersLineVariantDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OrdersLineVariantDTO> findByCriteria(OrdersLineVariantCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<OrdersLineVariant> specification = createSpecification(criteria);
        return ordersLineVariantMapper.toDto(ordersLineVariantRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link OrdersLineVariantDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrdersLineVariantDTO> findByCriteria(OrdersLineVariantCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OrdersLineVariant> specification = createSpecification(criteria);
        return ordersLineVariantRepository.findAll(specification, page)
            .map(ordersLineVariantMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrdersLineVariantCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<OrdersLineVariant> specification = createSpecification(criteria);
        return ordersLineVariantRepository.count(specification);
    }

    /**
     * Function to convert OrdersLineVariantCriteria to a {@link Specification}
     */
    private Specification<OrdersLineVariant> createSpecification(OrdersLineVariantCriteria criteria) {
        Specification<OrdersLineVariant> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), OrdersLineVariant_.id));
            }
            if (criteria.getVariantName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVariantName(), OrdersLineVariant_.variantName));
            }
            if (criteria.getVariantValue() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVariantValue(), OrdersLineVariant_.variantValue));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), OrdersLineVariant_.description));
            }
            if (criteria.getPercentage() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPercentage(), OrdersLineVariant_.percentage));
            }
            if (criteria.getFullPhotoUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFullPhotoUrl(), OrdersLineVariant_.fullPhotoUrl));
            }
            if (criteria.getThumbnailPhotoUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getThumbnailPhotoUrl(), OrdersLineVariant_.thumbnailPhotoUrl));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), OrdersLineVariant_.price));
            }
            if (criteria.getOrdersLineId() != null) {
                specification = specification.and(buildSpecification(criteria.getOrdersLineId(),
                    root -> root.join(OrdersLineVariant_.ordersLine, JoinType.LEFT).get(OrdersLine_.id)));
            }
            if (criteria.getOrdersLineExtrasId() != null) {
                specification = specification.and(buildSpecification(criteria.getOrdersLineExtrasId(),
                    root -> root.join(OrdersLineVariant_.ordersLineExtras, JoinType.LEFT).get(OrdersLineExtra_.id)));
            }
        }
        return specification;
    }
}
