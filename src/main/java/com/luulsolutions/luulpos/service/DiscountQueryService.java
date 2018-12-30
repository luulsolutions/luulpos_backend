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

import com.luulsolutions.luulpos.domain.Discount;
import com.luulsolutions.luulpos.domain.*; // for static metamodels
import com.luulsolutions.luulpos.repository.DiscountRepository;
import com.luulsolutions.luulpos.repository.search.DiscountSearchRepository;
import com.luulsolutions.luulpos.service.dto.DiscountCriteria;
import com.luulsolutions.luulpos.service.dto.DiscountDTO;
import com.luulsolutions.luulpos.service.mapper.DiscountMapper;

/**
 * Service for executing complex queries for Discount entities in the database.
 * The main input is a {@link DiscountCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DiscountDTO} or a {@link Page} of {@link DiscountDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DiscountQueryService extends QueryService<Discount> {

    private final Logger log = LoggerFactory.getLogger(DiscountQueryService.class);

    private final DiscountRepository discountRepository;

    private final DiscountMapper discountMapper;

    private final DiscountSearchRepository discountSearchRepository;

    public DiscountQueryService(DiscountRepository discountRepository, DiscountMapper discountMapper, DiscountSearchRepository discountSearchRepository) {
        this.discountRepository = discountRepository;
        this.discountMapper = discountMapper;
        this.discountSearchRepository = discountSearchRepository;
    }

    /**
     * Return a {@link List} of {@link DiscountDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DiscountDTO> findByCriteria(DiscountCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Discount> specification = createSpecification(criteria);
        return discountMapper.toDto(discountRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DiscountDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DiscountDTO> findByCriteria(DiscountCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Discount> specification = createSpecification(criteria);
        return discountRepository.findAll(specification, page)
            .map(discountMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DiscountCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Discount> specification = createSpecification(criteria);
        return discountRepository.count(specification);
    }

    /**
     * Function to convert DiscountCriteria to a {@link Specification}
     */
    private Specification<Discount> createSpecification(DiscountCriteria criteria) {
        Specification<Discount> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Discount_.id));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Discount_.description));
            }
            if (criteria.getPercentage() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPercentage(), Discount_.percentage));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), Discount_.amount));
            }
            if (criteria.getActive() != null) {
                specification = specification.and(buildSpecification(criteria.getActive(), Discount_.active));
            }
            if (criteria.getShopId() != null) {
                specification = specification.and(buildSpecification(criteria.getShopId(),
                    root -> root.join(Discount_.shop, JoinType.LEFT).get(Shop_.id)));
            }
            if (criteria.getProductId() != null) {
                specification = specification.and(buildSpecification(criteria.getProductId(),
                    root -> root.join(Discount_.products, JoinType.LEFT).get(Product_.id)));
            }
        }
        return specification;
    }
}
