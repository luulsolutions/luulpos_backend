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

import com.luulsolutions.luulpos.domain.PaymentMethod;
import com.luulsolutions.luulpos.domain.*; // for static metamodels
import com.luulsolutions.luulpos.repository.PaymentMethodRepository;
import com.luulsolutions.luulpos.repository.search.PaymentMethodSearchRepository;
import com.luulsolutions.luulpos.service.dto.PaymentMethodCriteria;
import com.luulsolutions.luulpos.service.dto.PaymentMethodDTO;
import com.luulsolutions.luulpos.service.mapper.PaymentMethodMapper;

/**
 * Service for executing complex queries for PaymentMethod entities in the database.
 * The main input is a {@link PaymentMethodCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PaymentMethodDTO} or a {@link Page} of {@link PaymentMethodDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PaymentMethodQueryService extends QueryService<PaymentMethod> {

    private final Logger log = LoggerFactory.getLogger(PaymentMethodQueryService.class);

    private final PaymentMethodRepository paymentMethodRepository;

    private final PaymentMethodMapper paymentMethodMapper;

    private final PaymentMethodSearchRepository paymentMethodSearchRepository;

    public PaymentMethodQueryService(PaymentMethodRepository paymentMethodRepository, PaymentMethodMapper paymentMethodMapper, PaymentMethodSearchRepository paymentMethodSearchRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
        this.paymentMethodMapper = paymentMethodMapper;
        this.paymentMethodSearchRepository = paymentMethodSearchRepository;
    }

    /**
     * Return a {@link List} of {@link PaymentMethodDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PaymentMethodDTO> findByCriteria(PaymentMethodCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PaymentMethod> specification = createSpecification(criteria);
        return paymentMethodMapper.toDto(paymentMethodRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PaymentMethodDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PaymentMethodDTO> findByCriteria(PaymentMethodCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PaymentMethod> specification = createSpecification(criteria);
        return paymentMethodRepository.findAll(specification, page)
            .map(paymentMethodMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PaymentMethodCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PaymentMethod> specification = createSpecification(criteria);
        return paymentMethodRepository.count(specification);
    }

    /**
     * Function to convert PaymentMethodCriteria to a {@link Specification}
     */
    private Specification<PaymentMethod> createSpecification(PaymentMethodCriteria criteria) {
        Specification<PaymentMethod> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), PaymentMethod_.id));
            }
            if (criteria.getPaymentMethod() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPaymentMethod(), PaymentMethod_.paymentMethod));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), PaymentMethod_.description));
            }
            if (criteria.getActive() != null) {
                specification = specification.and(buildSpecification(criteria.getActive(), PaymentMethod_.active));
            }
            if (criteria.getShopId() != null) {
                specification = specification.and(buildSpecification(criteria.getShopId(),
                    root -> root.join(PaymentMethod_.shop, JoinType.LEFT).get(Shop_.id)));
            }
        }
        return specification;
    }
}
