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

import com.luulsolutions.luulpos.domain.PaymentMethodConfig;
import com.luulsolutions.luulpos.domain.*; // for static metamodels
import com.luulsolutions.luulpos.repository.PaymentMethodConfigRepository;
import com.luulsolutions.luulpos.repository.search.PaymentMethodConfigSearchRepository;
import com.luulsolutions.luulpos.service.dto.PaymentMethodConfigCriteria;
import com.luulsolutions.luulpos.service.dto.PaymentMethodConfigDTO;
import com.luulsolutions.luulpos.service.mapper.PaymentMethodConfigMapper;

/**
 * Service for executing complex queries for PaymentMethodConfig entities in the database.
 * The main input is a {@link PaymentMethodConfigCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PaymentMethodConfigDTO} or a {@link Page} of {@link PaymentMethodConfigDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PaymentMethodConfigQueryService extends QueryService<PaymentMethodConfig> {

    private final Logger log = LoggerFactory.getLogger(PaymentMethodConfigQueryService.class);

    private final PaymentMethodConfigRepository paymentMethodConfigRepository;

    private final PaymentMethodConfigMapper paymentMethodConfigMapper;

    private final PaymentMethodConfigSearchRepository paymentMethodConfigSearchRepository;

    public PaymentMethodConfigQueryService(PaymentMethodConfigRepository paymentMethodConfigRepository, PaymentMethodConfigMapper paymentMethodConfigMapper, PaymentMethodConfigSearchRepository paymentMethodConfigSearchRepository) {
        this.paymentMethodConfigRepository = paymentMethodConfigRepository;
        this.paymentMethodConfigMapper = paymentMethodConfigMapper;
        this.paymentMethodConfigSearchRepository = paymentMethodConfigSearchRepository;
    }

    /**
     * Return a {@link List} of {@link PaymentMethodConfigDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PaymentMethodConfigDTO> findByCriteria(PaymentMethodConfigCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PaymentMethodConfig> specification = createSpecification(criteria);
        return paymentMethodConfigMapper.toDto(paymentMethodConfigRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PaymentMethodConfigDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PaymentMethodConfigDTO> findByCriteria(PaymentMethodConfigCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PaymentMethodConfig> specification = createSpecification(criteria);
        return paymentMethodConfigRepository.findAll(specification, page)
            .map(paymentMethodConfigMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PaymentMethodConfigCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PaymentMethodConfig> specification = createSpecification(criteria);
        return paymentMethodConfigRepository.count(specification);
    }

    /**
     * Function to convert PaymentMethodConfigCriteria to a {@link Specification}
     */
    private Specification<PaymentMethodConfig> createSpecification(PaymentMethodConfigCriteria criteria) {
        Specification<PaymentMethodConfig> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), PaymentMethodConfig_.id));
            }
            if (criteria.getKey() != null) {
                specification = specification.and(buildStringSpecification(criteria.getKey(), PaymentMethodConfig_.key));
            }
            if (criteria.getValue() != null) {
                specification = specification.and(buildStringSpecification(criteria.getValue(), PaymentMethodConfig_.value));
            }
            if (criteria.getNote() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNote(), PaymentMethodConfig_.note));
            }
            if (criteria.getEnabled() != null) {
                specification = specification.and(buildSpecification(criteria.getEnabled(), PaymentMethodConfig_.enabled));
            }
            if (criteria.getPaymentMethodId() != null) {
                specification = specification.and(buildSpecification(criteria.getPaymentMethodId(),
                    root -> root.join(PaymentMethodConfig_.paymentMethod, JoinType.LEFT).get(PaymentMethod_.id)));
            }
        }
        return specification;
    }
}
