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

import com.luulsolutions.luulpos.domain.SectionTable;
import com.luulsolutions.luulpos.domain.*; // for static metamodels
import com.luulsolutions.luulpos.repository.SectionTableRepository;
import com.luulsolutions.luulpos.repository.search.SectionTableSearchRepository;
import com.luulsolutions.luulpos.service.dto.SectionTableCriteria;
import com.luulsolutions.luulpos.service.dto.SectionTableDTO;
import com.luulsolutions.luulpos.service.mapper.SectionTableMapper;

/**
 * Service for executing complex queries for SectionTable entities in the database.
 * The main input is a {@link SectionTableCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SectionTableDTO} or a {@link Page} of {@link SectionTableDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SectionTableQueryService extends QueryService<SectionTable> {

    private final Logger log = LoggerFactory.getLogger(SectionTableQueryService.class);

    private final SectionTableRepository sectionTableRepository;

    private final SectionTableMapper sectionTableMapper;

    private final SectionTableSearchRepository sectionTableSearchRepository;

    public SectionTableQueryService(SectionTableRepository sectionTableRepository, SectionTableMapper sectionTableMapper, SectionTableSearchRepository sectionTableSearchRepository) {
        this.sectionTableRepository = sectionTableRepository;
        this.sectionTableMapper = sectionTableMapper;
        this.sectionTableSearchRepository = sectionTableSearchRepository;
    }

    /**
     * Return a {@link List} of {@link SectionTableDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SectionTableDTO> findByCriteria(SectionTableCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SectionTable> specification = createSpecification(criteria);
        return sectionTableMapper.toDto(sectionTableRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SectionTableDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SectionTableDTO> findByCriteria(SectionTableCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SectionTable> specification = createSpecification(criteria);
        return sectionTableRepository.findAll(specification, page)
            .map(sectionTableMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SectionTableCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SectionTable> specification = createSpecification(criteria);
        return sectionTableRepository.count(specification);
    }

    /**
     * Function to convert SectionTableCriteria to a {@link Specification}
     */
    private Specification<SectionTable> createSpecification(SectionTableCriteria criteria) {
        Specification<SectionTable> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), SectionTable_.id));
            }
            if (criteria.getTableNumber() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTableNumber(), SectionTable_.tableNumber));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), SectionTable_.description));
            }
            if (criteria.getShopSectionsId() != null) {
                specification = specification.and(buildSpecification(criteria.getShopSectionsId(),
                    root -> root.join(SectionTable_.shopSections, JoinType.LEFT).get(ShopSection_.id)));
            }
        }
        return specification;
    }
}
