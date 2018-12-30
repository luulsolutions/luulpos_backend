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

import com.luulsolutions.luulpos.domain.ShopChange;
import com.luulsolutions.luulpos.domain.*; // for static metamodels
import com.luulsolutions.luulpos.repository.ShopChangeRepository;
import com.luulsolutions.luulpos.repository.search.ShopChangeSearchRepository;
import com.luulsolutions.luulpos.service.dto.ShopChangeCriteria;
import com.luulsolutions.luulpos.service.dto.ShopChangeDTO;
import com.luulsolutions.luulpos.service.mapper.ShopChangeMapper;

/**
 * Service for executing complex queries for ShopChange entities in the database.
 * The main input is a {@link ShopChangeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ShopChangeDTO} or a {@link Page} of {@link ShopChangeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ShopChangeQueryService extends QueryService<ShopChange> {

    private final Logger log = LoggerFactory.getLogger(ShopChangeQueryService.class);

    private final ShopChangeRepository shopChangeRepository;

    private final ShopChangeMapper shopChangeMapper;

    private final ShopChangeSearchRepository shopChangeSearchRepository;

    public ShopChangeQueryService(ShopChangeRepository shopChangeRepository, ShopChangeMapper shopChangeMapper, ShopChangeSearchRepository shopChangeSearchRepository) {
        this.shopChangeRepository = shopChangeRepository;
        this.shopChangeMapper = shopChangeMapper;
        this.shopChangeSearchRepository = shopChangeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ShopChangeDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ShopChangeDTO> findByCriteria(ShopChangeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ShopChange> specification = createSpecification(criteria);
        return shopChangeMapper.toDto(shopChangeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ShopChangeDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ShopChangeDTO> findByCriteria(ShopChangeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ShopChange> specification = createSpecification(criteria);
        return shopChangeRepository.findAll(specification, page)
            .map(shopChangeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ShopChangeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ShopChange> specification = createSpecification(criteria);
        return shopChangeRepository.count(specification);
    }

    /**
     * Function to convert ShopChangeCriteria to a {@link Specification}
     */
    private Specification<ShopChange> createSpecification(ShopChangeCriteria criteria) {
        Specification<ShopChange> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ShopChange_.id));
            }
            if (criteria.getChange() != null) {
                specification = specification.and(buildStringSpecification(criteria.getChange(), ShopChange_.change));
            }
            if (criteria.getChangedEntity() != null) {
                specification = specification.and(buildStringSpecification(criteria.getChangedEntity(), ShopChange_.changedEntity));
            }
            if (criteria.getNote() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNote(), ShopChange_.note));
            }
            if (criteria.getChangeDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getChangeDate(), ShopChange_.changeDate));
            }
            if (criteria.getShopId() != null) {
                specification = specification.and(buildSpecification(criteria.getShopId(),
                    root -> root.join(ShopChange_.shop, JoinType.LEFT).get(Shop_.id)));
            }
            if (criteria.getChangedById() != null) {
                specification = specification.and(buildSpecification(criteria.getChangedById(),
                    root -> root.join(ShopChange_.changedBy, JoinType.LEFT).get(Profile_.id)));
            }
        }
        return specification;
    }
}
