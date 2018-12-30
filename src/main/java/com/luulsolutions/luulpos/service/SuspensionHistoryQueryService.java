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

import com.luulsolutions.luulpos.domain.SuspensionHistory;
import com.luulsolutions.luulpos.domain.*; // for static metamodels
import com.luulsolutions.luulpos.repository.SuspensionHistoryRepository;
import com.luulsolutions.luulpos.repository.search.SuspensionHistorySearchRepository;
import com.luulsolutions.luulpos.service.dto.SuspensionHistoryCriteria;
import com.luulsolutions.luulpos.service.dto.SuspensionHistoryDTO;
import com.luulsolutions.luulpos.service.mapper.SuspensionHistoryMapper;

/**
 * Service for executing complex queries for SuspensionHistory entities in the database.
 * The main input is a {@link SuspensionHistoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SuspensionHistoryDTO} or a {@link Page} of {@link SuspensionHistoryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SuspensionHistoryQueryService extends QueryService<SuspensionHistory> {

    private final Logger log = LoggerFactory.getLogger(SuspensionHistoryQueryService.class);

    private final SuspensionHistoryRepository suspensionHistoryRepository;

    private final SuspensionHistoryMapper suspensionHistoryMapper;

    private final SuspensionHistorySearchRepository suspensionHistorySearchRepository;

    public SuspensionHistoryQueryService(SuspensionHistoryRepository suspensionHistoryRepository, SuspensionHistoryMapper suspensionHistoryMapper, SuspensionHistorySearchRepository suspensionHistorySearchRepository) {
        this.suspensionHistoryRepository = suspensionHistoryRepository;
        this.suspensionHistoryMapper = suspensionHistoryMapper;
        this.suspensionHistorySearchRepository = suspensionHistorySearchRepository;
    }

    /**
     * Return a {@link List} of {@link SuspensionHistoryDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SuspensionHistoryDTO> findByCriteria(SuspensionHistoryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SuspensionHistory> specification = createSpecification(criteria);
        return suspensionHistoryMapper.toDto(suspensionHistoryRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SuspensionHistoryDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SuspensionHistoryDTO> findByCriteria(SuspensionHistoryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SuspensionHistory> specification = createSpecification(criteria);
        return suspensionHistoryRepository.findAll(specification, page)
            .map(suspensionHistoryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SuspensionHistoryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SuspensionHistory> specification = createSpecification(criteria);
        return suspensionHistoryRepository.count(specification);
    }

    /**
     * Function to convert SuspensionHistoryCriteria to a {@link Specification}
     */
    private Specification<SuspensionHistory> createSpecification(SuspensionHistoryCriteria criteria) {
        Specification<SuspensionHistory> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), SuspensionHistory_.id));
            }
            if (criteria.getSuspendedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSuspendedDate(), SuspensionHistory_.suspendedDate));
            }
            if (criteria.getSuspensionType() != null) {
                specification = specification.and(buildSpecification(criteria.getSuspensionType(), SuspensionHistory_.suspensionType));
            }
            if (criteria.getSuspendedReason() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSuspendedReason(), SuspensionHistory_.suspendedReason));
            }
            if (criteria.getResolutionNote() != null) {
                specification = specification.and(buildStringSpecification(criteria.getResolutionNote(), SuspensionHistory_.resolutionNote));
            }
            if (criteria.getUnsuspensionDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUnsuspensionDate(), SuspensionHistory_.unsuspensionDate));
            }
            if (criteria.getProfileId() != null) {
                specification = specification.and(buildSpecification(criteria.getProfileId(),
                    root -> root.join(SuspensionHistory_.profile, JoinType.LEFT).get(Profile_.id)));
            }
            if (criteria.getSuspendedById() != null) {
                specification = specification.and(buildSpecification(criteria.getSuspendedById(),
                    root -> root.join(SuspensionHistory_.suspendedBy, JoinType.LEFT).get(Profile_.id)));
            }
        }
        return specification;
    }
}
