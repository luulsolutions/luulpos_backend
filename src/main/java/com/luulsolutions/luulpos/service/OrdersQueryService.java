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

import com.luulsolutions.luulpos.domain.Orders;
import com.luulsolutions.luulpos.domain.*; // for static metamodels
import com.luulsolutions.luulpos.repository.OrdersRepository;
import com.luulsolutions.luulpos.repository.search.OrdersSearchRepository;
import com.luulsolutions.luulpos.service.dto.OrdersCriteria;
import com.luulsolutions.luulpos.service.dto.OrdersDTO;
import com.luulsolutions.luulpos.service.mapper.OrdersMapper;

/**
 * Service for executing complex queries for Orders entities in the database.
 * The main input is a {@link OrdersCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OrdersDTO} or a {@link Page} of {@link OrdersDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrdersQueryService extends QueryService<Orders> {

    private final Logger log = LoggerFactory.getLogger(OrdersQueryService.class);

    private final OrdersRepository ordersRepository;

    private final OrdersMapper ordersMapper;

    private final OrdersSearchRepository ordersSearchRepository;

    public OrdersQueryService(OrdersRepository ordersRepository, OrdersMapper ordersMapper, OrdersSearchRepository ordersSearchRepository) {
        this.ordersRepository = ordersRepository;
        this.ordersMapper = ordersMapper;
        this.ordersSearchRepository = ordersSearchRepository;
    }

    /**
     * Return a {@link List} of {@link OrdersDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OrdersDTO> findByCriteria(OrdersCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Orders> specification = createSpecification(criteria);
        return ordersMapper.toDto(ordersRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link OrdersDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrdersDTO> findByCriteria(OrdersCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Orders> specification = createSpecification(criteria);
        return ordersRepository.findAll(specification, page)
            .map(ordersMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrdersCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Orders> specification = createSpecification(criteria);
        return ordersRepository.count(specification);
    }

    /**
     * Function to convert OrdersCriteria to a {@link Specification}
     */
    private Specification<Orders> createSpecification(OrdersCriteria criteria) {
        Specification<Orders> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Orders_.id));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Orders_.description));
            }
            if (criteria.getCustomerName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCustomerName(), Orders_.customerName));
            }
            if (criteria.getTotalPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalPrice(), Orders_.totalPrice));
            }
            if (criteria.getQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantity(), Orders_.quantity));
            }
            if (criteria.getDiscountPercentage() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDiscountPercentage(), Orders_.discountPercentage));
            }
            if (criteria.getDiscountAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDiscountAmount(), Orders_.discountAmount));
            }
            if (criteria.getTaxPercentage() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTaxPercentage(), Orders_.taxPercentage));
            }
            if (criteria.getTaxAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTaxAmount(), Orders_.taxAmount));
            }
            if (criteria.getOrderStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getOrderStatus(), Orders_.orderStatus));
            }
            if (criteria.getNote() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNote(), Orders_.note));
            }
            if (criteria.getOrderDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrderDate(), Orders_.orderDate));
            }
            if (criteria.getIsVariantOrder() != null) {
                specification = specification.and(buildSpecification(criteria.getIsVariantOrder(), Orders_.isVariantOrder));
            }
            if (criteria.getOrdersLinesId() != null) {
                specification = specification.and(buildSpecification(criteria.getOrdersLinesId(),
                    root -> root.join(Orders_.ordersLines, JoinType.LEFT).get(OrdersLine_.id)));
            }
            if (criteria.getPaymentMethodId() != null) {
                specification = specification.and(buildSpecification(criteria.getPaymentMethodId(),
                    root -> root.join(Orders_.paymentMethod, JoinType.LEFT).get(PaymentMethod_.id)));
            }
            if (criteria.getSoldById() != null) {
                specification = specification.and(buildSpecification(criteria.getSoldById(),
                    root -> root.join(Orders_.soldBy, JoinType.LEFT).get(Profile_.id)));
            }
            if (criteria.getPreparedById() != null) {
                specification = specification.and(buildSpecification(criteria.getPreparedById(),
                    root -> root.join(Orders_.preparedBy, JoinType.LEFT).get(Profile_.id)));
            }
            if (criteria.getShopDeviceId() != null) {
                specification = specification.and(buildSpecification(criteria.getShopDeviceId(),
                    root -> root.join(Orders_.shopDevice, JoinType.LEFT).get(ShopDevice_.id)));
            }
            if (criteria.getSectionTableId() != null) {
                specification = specification.and(buildSpecification(criteria.getSectionTableId(),
                    root -> root.join(Orders_.sectionTable, JoinType.LEFT).get(SectionTable_.id)));
            }
            if (criteria.getShopId() != null) {
                specification = specification.and(buildSpecification(criteria.getShopId(),
                    root -> root.join(Orders_.shop, JoinType.LEFT).get(Shop_.id)));
            }
        }
        return specification;
    }
}
