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

import com.luulsolutions.luulpos.domain.SystemEventsHistory;
import com.luulsolutions.luulpos.domain.*; // for static metamodels
import com.luulsolutions.luulpos.repository.SystemEventsHistoryRepository;
import com.luulsolutions.luulpos.repository.search.SystemEventsHistorySearchRepository;
import com.luulsolutions.luulpos.service.dto.SystemEventsHistoryCriteria;
import com.luulsolutions.luulpos.service.dto.SystemEventsHistoryDTO;
import com.luulsolutions.luulpos.service.mapper.SystemEventsHistoryMapper;

/**
 * Service for executing complex queries for SystemEventsHistory entities in the database.
 * The main input is a {@link SystemEventsHistoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SystemEventsHistoryDTO} or a {@link Page} of {@link SystemEventsHistoryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SystemEventsHistoryQueryService extends QueryService<SystemEventsHistory> {

    private final Logger log = LoggerFactory.getLogger(SystemEventsHistoryQueryService.class);

    private final SystemEventsHistoryRepository systemEventsHistoryRepository;

    private final SystemEventsHistoryMapper systemEventsHistoryMapper;

    private final SystemEventsHistorySearchRepository systemEventsHistorySearchRepository;

    public SystemEventsHistoryQueryService(SystemEventsHistoryRepository systemEventsHistoryRepository, SystemEventsHistoryMapper systemEventsHistoryMapper, SystemEventsHistorySearchRepository systemEventsHistorySearchRepository) {
        this.systemEventsHistoryRepository = systemEventsHistoryRepository;
        this.systemEventsHistoryMapper = systemEventsHistoryMapper;
        this.systemEventsHistorySearchRepository = systemEventsHistorySearchRepository;
    }

    /**
     * Return a {@link List} of {@link SystemEventsHistoryDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SystemEventsHistoryDTO> findByCriteria(SystemEventsHistoryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SystemEventsHistory> specification = createSpecification(criteria);
        return systemEventsHistoryMapper.toDto(systemEventsHistoryRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SystemEventsHistoryDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SystemEventsHistoryDTO> findByCriteria(SystemEventsHistoryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SystemEventsHistory> specification = createSpecification(criteria);
        return systemEventsHistoryRepository.findAll(specification, page)
            .map(systemEventsHistoryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SystemEventsHistoryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SystemEventsHistory> specification = createSpecification(criteria);
        return systemEventsHistoryRepository.count(specification);
    }

    /**
     * Function to convert SystemEventsHistoryCriteria to a {@link Specification}
     */
    private Specification<SystemEventsHistory> createSpecification(SystemEventsHistoryCriteria criteria) {
        Specification<SystemEventsHistory> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), SystemEventsHistory_.id));
            }
            if (criteria.getEventName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEventName(), SystemEventsHistory_.eventName));
            }
            if (criteria.getEventDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEventDate(), SystemEventsHistory_.eventDate));
            }
            if (criteria.getEventApi() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEventApi(), SystemEventsHistory_.eventApi));
            }
            if (criteria.getEventNote() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEventNote(), SystemEventsHistory_.eventNote));
            }
            if (criteria.getEventEntityName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEventEntityName(), SystemEventsHistory_.eventEntityName));
            }
            if (criteria.getEventEntityId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEventEntityId(), SystemEventsHistory_.eventEntityId));
            }
            if (criteria.getTriggedById() != null) {
                specification = specification.and(buildSpecification(criteria.getTriggedById(),
                    root -> root.join(SystemEventsHistory_.triggedBy, JoinType.LEFT).get(Profile_.id)));
            }
        }
        return specification;
    }
}
