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

import com.luulsolutions.luulpos.domain.ShopSection;
import com.luulsolutions.luulpos.domain.*; // for static metamodels
import com.luulsolutions.luulpos.repository.ShopSectionRepository;
import com.luulsolutions.luulpos.repository.search.ShopSectionSearchRepository;
import com.luulsolutions.luulpos.service.dto.ShopSectionCriteria;
import com.luulsolutions.luulpos.service.dto.ShopSectionDTO;
import com.luulsolutions.luulpos.service.mapper.ShopSectionMapper;

/**
 * Service for executing complex queries for ShopSection entities in the database.
 * The main input is a {@link ShopSectionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ShopSectionDTO} or a {@link Page} of {@link ShopSectionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ShopSectionQueryService extends QueryService<ShopSection> {

    private final Logger log = LoggerFactory.getLogger(ShopSectionQueryService.class);

    private final ShopSectionRepository shopSectionRepository;

    private final ShopSectionMapper shopSectionMapper;

    private final ShopSectionSearchRepository shopSectionSearchRepository;

    public ShopSectionQueryService(ShopSectionRepository shopSectionRepository, ShopSectionMapper shopSectionMapper, ShopSectionSearchRepository shopSectionSearchRepository) {
        this.shopSectionRepository = shopSectionRepository;
        this.shopSectionMapper = shopSectionMapper;
        this.shopSectionSearchRepository = shopSectionSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ShopSectionDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ShopSectionDTO> findByCriteria(ShopSectionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ShopSection> specification = createSpecification(criteria);
        return shopSectionMapper.toDto(shopSectionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ShopSectionDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ShopSectionDTO> findByCriteria(ShopSectionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ShopSection> specification = createSpecification(criteria);
        return shopSectionRepository.findAll(specification, page)
            .map(shopSectionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ShopSectionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ShopSection> specification = createSpecification(criteria);
        return shopSectionRepository.count(specification);
    }

    /**
     * Function to convert ShopSectionCriteria to a {@link Specification}
     */
    private Specification<ShopSection> createSpecification(ShopSectionCriteria criteria) {
        Specification<ShopSection> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ShopSection_.id));
            }
            if (criteria.getSectionName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSectionName(), ShopSection_.sectionName));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), ShopSection_.description));
            }
            if (criteria.getSurchargePercentage() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSurchargePercentage(), ShopSection_.surchargePercentage));
            }
            if (criteria.getSurchargeFlatAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSurchargeFlatAmount(), ShopSection_.surchargeFlatAmount));
            }
            if (criteria.getUsePercentage() != null) {
                specification = specification.and(buildSpecification(criteria.getUsePercentage(), ShopSection_.usePercentage));
            }
            if (criteria.getShopId() != null) {
                specification = specification.and(buildSpecification(criteria.getShopId(),
                    root -> root.join(ShopSection_.shop, JoinType.LEFT).get(Shop_.id)));
            }
        }
        return specification;
    }
}
