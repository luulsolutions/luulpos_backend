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

import com.luulsolutions.luulpos.domain.Payment;
import com.luulsolutions.luulpos.domain.*; // for static metamodels
import com.luulsolutions.luulpos.repository.PaymentRepository;
import com.luulsolutions.luulpos.repository.search.PaymentSearchRepository;
import com.luulsolutions.luulpos.service.dto.PaymentCriteria;
import com.luulsolutions.luulpos.service.dto.PaymentDTO;
import com.luulsolutions.luulpos.service.mapper.PaymentMapper;

/**
 * Service for executing complex queries for Payment entities in the database.
 * The main input is a {@link PaymentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PaymentDTO} or a {@link Page} of {@link PaymentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PaymentQueryService extends QueryService<Payment> {

    private final Logger log = LoggerFactory.getLogger(PaymentQueryService.class);

    private final PaymentRepository paymentRepository;

    private final PaymentMapper paymentMapper;

    private final PaymentSearchRepository paymentSearchRepository;

    public PaymentQueryService(PaymentRepository paymentRepository, PaymentMapper paymentMapper, PaymentSearchRepository paymentSearchRepository) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
        this.paymentSearchRepository = paymentSearchRepository;
    }

    /**
     * Return a {@link List} of {@link PaymentDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PaymentDTO> findByCriteria(PaymentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Payment> specification = createSpecification(criteria);
        return paymentMapper.toDto(paymentRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PaymentDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PaymentDTO> findByCriteria(PaymentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Payment> specification = createSpecification(criteria);
        return paymentRepository.findAll(specification, page)
            .map(paymentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PaymentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Payment> specification = createSpecification(criteria);
        return paymentRepository.count(specification);
    }

    /**
     * Function to convert PaymentCriteria to a {@link Specification}
     */
    private Specification<Payment> createSpecification(PaymentCriteria criteria) {
        Specification<Payment> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Payment_.id));
            }
            if (criteria.getPaymentDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPaymentDate(), Payment_.paymentDate));
            }
            if (criteria.getPaymentProvider() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPaymentProvider(), Payment_.paymentProvider));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), Payment_.amount));
            }
            if (criteria.getPaymentStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getPaymentStatus(), Payment_.paymentStatus));
            }
            if (criteria.getCurency() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCurency(), Payment_.curency));
            }
            if (criteria.getCustomerName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCustomerName(), Payment_.customerName));
            }
            if (criteria.getShopId() != null) {
                specification = specification.and(buildSpecification(criteria.getShopId(),
                    root -> root.join(Payment_.shop, JoinType.LEFT).get(Shop_.id)));
            }
            if (criteria.getProcessedById() != null) {
                specification = specification.and(buildSpecification(criteria.getProcessedById(),
                    root -> root.join(Payment_.processedBy, JoinType.LEFT).get(Profile_.id)));
            }
            if (criteria.getPaymentMethodId() != null) {
                specification = specification.and(buildSpecification(criteria.getPaymentMethodId(),
                    root -> root.join(Payment_.paymentMethod, JoinType.LEFT).get(PaymentMethod_.id)));
            }
            if (criteria.getOrderId() != null) {
                specification = specification.and(buildSpecification(criteria.getOrderId(),
                    root -> root.join(Payment_.order, JoinType.LEFT).get(Orders_.id)));
            }
        }
        return specification;
    }
}
