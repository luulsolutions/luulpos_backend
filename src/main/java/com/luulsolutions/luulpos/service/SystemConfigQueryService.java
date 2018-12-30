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

import com.luulsolutions.luulpos.domain.SystemConfig;
import com.luulsolutions.luulpos.domain.*; // for static metamodels
import com.luulsolutions.luulpos.repository.SystemConfigRepository;
import com.luulsolutions.luulpos.repository.search.SystemConfigSearchRepository;
import com.luulsolutions.luulpos.service.dto.SystemConfigCriteria;
import com.luulsolutions.luulpos.service.dto.SystemConfigDTO;
import com.luulsolutions.luulpos.service.mapper.SystemConfigMapper;

/**
 * Service for executing complex queries for SystemConfig entities in the database.
 * The main input is a {@link SystemConfigCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SystemConfigDTO} or a {@link Page} of {@link SystemConfigDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SystemConfigQueryService extends QueryService<SystemConfig> {

    private final Logger log = LoggerFactory.getLogger(SystemConfigQueryService.class);

    private final SystemConfigRepository systemConfigRepository;

    private final SystemConfigMapper systemConfigMapper;

    private final SystemConfigSearchRepository systemConfigSearchRepository;

    public SystemConfigQueryService(SystemConfigRepository systemConfigRepository, SystemConfigMapper systemConfigMapper, SystemConfigSearchRepository systemConfigSearchRepository) {
        this.systemConfigRepository = systemConfigRepository;
        this.systemConfigMapper = systemConfigMapper;
        this.systemConfigSearchRepository = systemConfigSearchRepository;
    }

    /**
     * Return a {@link List} of {@link SystemConfigDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SystemConfigDTO> findByCriteria(SystemConfigCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SystemConfig> specification = createSpecification(criteria);
        return systemConfigMapper.toDto(systemConfigRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SystemConfigDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SystemConfigDTO> findByCriteria(SystemConfigCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SystemConfig> specification = createSpecification(criteria);
        return systemConfigRepository.findAll(specification, page)
            .map(systemConfigMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SystemConfigCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SystemConfig> specification = createSpecification(criteria);
        return systemConfigRepository.count(specification);
    }

    /**
     * Function to convert SystemConfigCriteria to a {@link Specification}
     */
    private Specification<SystemConfig> createSpecification(SystemConfigCriteria criteria) {
        Specification<SystemConfig> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), SystemConfig_.id));
            }
            if (criteria.getKey() != null) {
                specification = specification.and(buildStringSpecification(criteria.getKey(), SystemConfig_.key));
            }
            if (criteria.getValue() != null) {
                specification = specification.and(buildStringSpecification(criteria.getValue(), SystemConfig_.value));
            }
            if (criteria.getConfigurationType() != null) {
                specification = specification.and(buildSpecification(criteria.getConfigurationType(), SystemConfig_.configurationType));
            }
            if (criteria.getNote() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNote(), SystemConfig_.note));
            }
            if (criteria.getEnabled() != null) {
                specification = specification.and(buildSpecification(criteria.getEnabled(), SystemConfig_.enabled));
            }
            if (criteria.getShopId() != null) {
                specification = specification.and(buildSpecification(criteria.getShopId(),
                    root -> root.join(SystemConfig_.shop, JoinType.LEFT).get(Shop_.id)));
            }
        }
        return specification;
    }
}
