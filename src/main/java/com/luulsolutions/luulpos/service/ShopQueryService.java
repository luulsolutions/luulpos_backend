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

import com.luulsolutions.luulpos.domain.Shop;
import com.luulsolutions.luulpos.domain.*; // for static metamodels
import com.luulsolutions.luulpos.repository.ShopRepository;
import com.luulsolutions.luulpos.repository.search.ShopSearchRepository;
import com.luulsolutions.luulpos.service.dto.ShopCriteria;
import com.luulsolutions.luulpos.service.dto.ShopDTO;
import com.luulsolutions.luulpos.service.mapper.ShopMapper;

/**
 * Service for executing complex queries for Shop entities in the database.
 * The main input is a {@link ShopCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ShopDTO} or a {@link Page} of {@link ShopDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ShopQueryService extends QueryService<Shop> {

    private final Logger log = LoggerFactory.getLogger(ShopQueryService.class);

    private final ShopRepository shopRepository;

    private final ShopMapper shopMapper;

    private final ShopSearchRepository shopSearchRepository;

    public ShopQueryService(ShopRepository shopRepository, ShopMapper shopMapper, ShopSearchRepository shopSearchRepository) {
        this.shopRepository = shopRepository;
        this.shopMapper = shopMapper;
        this.shopSearchRepository = shopSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ShopDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ShopDTO> findByCriteria(ShopCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Shop> specification = createSpecification(criteria);
        return shopMapper.toDto(shopRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ShopDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ShopDTO> findByCriteria(ShopCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Shop> specification = createSpecification(criteria);
        return shopRepository.findAll(specification, page)
            .map(shopMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ShopCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Shop> specification = createSpecification(criteria);
        return shopRepository.count(specification);
    }

    /**
     * Function to convert ShopCriteria to a {@link Specification}
     */
    private Specification<Shop> createSpecification(ShopCriteria criteria) {
        Specification<Shop> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Shop_.id));
            }
            if (criteria.getShopName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getShopName(), Shop_.shopName));
            }
            if (criteria.getShopAccountType() != null) {
                specification = specification.and(buildSpecification(criteria.getShopAccountType(), Shop_.shopAccountType));
            }
            if (criteria.getApprovalDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getApprovalDate(), Shop_.approvalDate));
            }
            if (criteria.getAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress(), Shop_.address));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Shop_.email));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Shop_.description));
            }
            if (criteria.getNote() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNote(), Shop_.note));
            }
            if (criteria.getLandland() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLandland(), Shop_.landland));
            }
            if (criteria.getMobile() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMobile(), Shop_.mobile));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Shop_.createdDate));
            }
            if (criteria.getShopLogoUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getShopLogoUrl(), Shop_.shopLogoUrl));
            }
            if (criteria.getActive() != null) {
                specification = specification.and(buildSpecification(criteria.getActive(), Shop_.active));
            }
            if (criteria.getCurrency() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCurrency(), Shop_.currency));
            }
            if (criteria.getCompanyId() != null) {
                specification = specification.and(buildSpecification(criteria.getCompanyId(),
                    root -> root.join(Shop_.company, JoinType.LEFT).get(Company_.id)));
            }
            if (criteria.getApprovedById() != null) {
                specification = specification.and(buildSpecification(criteria.getApprovedById(),
                    root -> root.join(Shop_.approvedBy, JoinType.LEFT).get(Profile_.id)));
            }
            if (criteria.getProfilesId() != null) {
                specification = specification.and(buildSpecification(criteria.getProfilesId(),
                    root -> root.join(Shop_.profiles, JoinType.LEFT).get(Profile_.id)));
            }
            if (criteria.getProductCategoriesId() != null) {
                specification = specification.and(buildSpecification(criteria.getProductCategoriesId(),
                    root -> root.join(Shop_.productCategories, JoinType.LEFT).get(ProductCategory_.id)));
            }
            if (criteria.getProductTypesId() != null) {
                specification = specification.and(buildSpecification(criteria.getProductTypesId(),
                    root -> root.join(Shop_.productTypes, JoinType.LEFT).get(ProductType_.id)));
            }
            if (criteria.getSystemConfigsId() != null) {
                specification = specification.and(buildSpecification(criteria.getSystemConfigsId(),
                    root -> root.join(Shop_.systemConfigs, JoinType.LEFT).get(SystemConfig_.id)));
            }
            if (criteria.getDiscountsId() != null) {
                specification = specification.and(buildSpecification(criteria.getDiscountsId(),
                    root -> root.join(Shop_.discounts, JoinType.LEFT).get(Discount_.id)));
            }
            if (criteria.getTaxesId() != null) {
                specification = specification.and(buildSpecification(criteria.getTaxesId(),
                    root -> root.join(Shop_.taxes, JoinType.LEFT).get(Tax_.id)));
            }
        }
        return specification;
    }
}
