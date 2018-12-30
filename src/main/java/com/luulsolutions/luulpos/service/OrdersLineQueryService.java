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

import com.luulsolutions.luulpos.domain.OrdersLine;
import com.luulsolutions.luulpos.domain.*; // for static metamodels
import com.luulsolutions.luulpos.repository.OrdersLineRepository;
import com.luulsolutions.luulpos.repository.search.OrdersLineSearchRepository;
import com.luulsolutions.luulpos.service.dto.OrdersLineCriteria;
import com.luulsolutions.luulpos.service.dto.OrdersLineDTO;
import com.luulsolutions.luulpos.service.mapper.OrdersLineMapper;

/**
 * Service for executing complex queries for OrdersLine entities in the database.
 * The main input is a {@link OrdersLineCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OrdersLineDTO} or a {@link Page} of {@link OrdersLineDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrdersLineQueryService extends QueryService<OrdersLine> {

    private final Logger log = LoggerFactory.getLogger(OrdersLineQueryService.class);

    private final OrdersLineRepository ordersLineRepository;

    private final OrdersLineMapper ordersLineMapper;

    private final OrdersLineSearchRepository ordersLineSearchRepository;

    public OrdersLineQueryService(OrdersLineRepository ordersLineRepository, OrdersLineMapper ordersLineMapper, OrdersLineSearchRepository ordersLineSearchRepository) {
        this.ordersLineRepository = ordersLineRepository;
        this.ordersLineMapper = ordersLineMapper;
        this.ordersLineSearchRepository = ordersLineSearchRepository;
    }

    /**
     * Return a {@link List} of {@link OrdersLineDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OrdersLineDTO> findByCriteria(OrdersLineCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<OrdersLine> specification = createSpecification(criteria);
        return ordersLineMapper.toDto(ordersLineRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link OrdersLineDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrdersLineDTO> findByCriteria(OrdersLineCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OrdersLine> specification = createSpecification(criteria);
        return ordersLineRepository.findAll(specification, page)
            .map(ordersLineMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrdersLineCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<OrdersLine> specification = createSpecification(criteria);
        return ordersLineRepository.count(specification);
    }

    /**
     * Function to convert OrdersLineCriteria to a {@link Specification}
     */
    private Specification<OrdersLine> createSpecification(OrdersLineCriteria criteria) {
        Specification<OrdersLine> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), OrdersLine_.id));
            }
            if (criteria.getOrdersLineName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOrdersLineName(), OrdersLine_.ordersLineName));
            }
            if (criteria.getOrdersLineValue() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOrdersLineValue(), OrdersLine_.ordersLineValue));
            }
            if (criteria.getOrdersLinePrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrdersLinePrice(), OrdersLine_.ordersLinePrice));
            }
            if (criteria.getOrdersLineDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOrdersLineDescription(), OrdersLine_.ordersLineDescription));
            }
            if (criteria.getFullPhotoUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFullPhotoUrl(), OrdersLine_.fullPhotoUrl));
            }
            if (criteria.getThumbnailPhotoUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getThumbnailPhotoUrl(), OrdersLine_.thumbnailPhotoUrl));
            }
            if (criteria.getOrdersId() != null) {
                specification = specification.and(buildSpecification(criteria.getOrdersId(),
                    root -> root.join(OrdersLine_.orders, JoinType.LEFT).get(Orders_.id)));
            }
            if (criteria.getOrdersLineVariantsId() != null) {
                specification = specification.and(buildSpecification(criteria.getOrdersLineVariantsId(),
                    root -> root.join(OrdersLine_.ordersLineVariants, JoinType.LEFT).get(OrdersLineVariant_.id)));
            }
            if (criteria.getProductId() != null) {
                specification = specification.and(buildSpecification(criteria.getProductId(),
                    root -> root.join(OrdersLine_.product, JoinType.LEFT).get(Product_.id)));
            }
        }
        return specification;
    }
}
