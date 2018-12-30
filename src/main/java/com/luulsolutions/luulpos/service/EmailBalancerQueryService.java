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

import com.luulsolutions.luulpos.domain.EmailBalancer;
import com.luulsolutions.luulpos.domain.*; // for static metamodels
import com.luulsolutions.luulpos.repository.EmailBalancerRepository;
import com.luulsolutions.luulpos.repository.search.EmailBalancerSearchRepository;
import com.luulsolutions.luulpos.service.dto.EmailBalancerCriteria;
import com.luulsolutions.luulpos.service.dto.EmailBalancerDTO;
import com.luulsolutions.luulpos.service.mapper.EmailBalancerMapper;

/**
 * Service for executing complex queries for EmailBalancer entities in the database.
 * The main input is a {@link EmailBalancerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EmailBalancerDTO} or a {@link Page} of {@link EmailBalancerDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EmailBalancerQueryService extends QueryService<EmailBalancer> {

    private final Logger log = LoggerFactory.getLogger(EmailBalancerQueryService.class);

    private final EmailBalancerRepository emailBalancerRepository;

    private final EmailBalancerMapper emailBalancerMapper;

    private final EmailBalancerSearchRepository emailBalancerSearchRepository;

    public EmailBalancerQueryService(EmailBalancerRepository emailBalancerRepository, EmailBalancerMapper emailBalancerMapper, EmailBalancerSearchRepository emailBalancerSearchRepository) {
        this.emailBalancerRepository = emailBalancerRepository;
        this.emailBalancerMapper = emailBalancerMapper;
        this.emailBalancerSearchRepository = emailBalancerSearchRepository;
    }

    /**
     * Return a {@link List} of {@link EmailBalancerDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EmailBalancerDTO> findByCriteria(EmailBalancerCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EmailBalancer> specification = createSpecification(criteria);
        return emailBalancerMapper.toDto(emailBalancerRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EmailBalancerDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EmailBalancerDTO> findByCriteria(EmailBalancerCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EmailBalancer> specification = createSpecification(criteria);
        return emailBalancerRepository.findAll(specification, page)
            .map(emailBalancerMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EmailBalancerCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EmailBalancer> specification = createSpecification(criteria);
        return emailBalancerRepository.count(specification);
    }

    /**
     * Function to convert EmailBalancerCriteria to a {@link Specification}
     */
    private Specification<EmailBalancer> createSpecification(EmailBalancerCriteria criteria) {
        Specification<EmailBalancer> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), EmailBalancer_.id));
            }
            if (criteria.getRelayId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRelayId(), EmailBalancer_.relayId));
            }
            if (criteria.getRelayPassword() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRelayPassword(), EmailBalancer_.relayPassword));
            }
            if (criteria.getStartingCount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartingCount(), EmailBalancer_.startingCount));
            }
            if (criteria.getEndingCount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndingCount(), EmailBalancer_.endingCount));
            }
            if (criteria.getProvider() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProvider(), EmailBalancer_.provider));
            }
            if (criteria.getRelayPort() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRelayPort(), EmailBalancer_.relayPort));
            }
            if (criteria.getEnabled() != null) {
                specification = specification.and(buildSpecification(criteria.getEnabled(), EmailBalancer_.enabled));
            }
        }
        return specification;
    }
}
