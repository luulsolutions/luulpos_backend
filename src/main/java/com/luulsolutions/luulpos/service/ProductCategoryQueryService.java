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

import com.luulsolutions.luulpos.domain.ProductCategory;
import com.luulsolutions.luulpos.domain.*; // for static metamodels
import com.luulsolutions.luulpos.repository.ProductCategoryRepository;
import com.luulsolutions.luulpos.repository.search.ProductCategorySearchRepository;
import com.luulsolutions.luulpos.service.dto.ProductCategoryCriteria;
import com.luulsolutions.luulpos.service.dto.ProductCategoryDTO;
import com.luulsolutions.luulpos.service.mapper.ProductCategoryMapper;

/**
 * Service for executing complex queries for ProductCategory entities in the database.
 * The main input is a {@link ProductCategoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProductCategoryDTO} or a {@link Page} of {@link ProductCategoryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductCategoryQueryService extends QueryService<ProductCategory> {

    private final Logger log = LoggerFactory.getLogger(ProductCategoryQueryService.class);

    private final ProductCategoryRepository productCategoryRepository;

    private final ProductCategoryMapper productCategoryMapper;

    private final ProductCategorySearchRepository productCategorySearchRepository;

    public ProductCategoryQueryService(ProductCategoryRepository productCategoryRepository, ProductCategoryMapper productCategoryMapper, ProductCategorySearchRepository productCategorySearchRepository) {
        this.productCategoryRepository = productCategoryRepository;
        this.productCategoryMapper = productCategoryMapper;
        this.productCategorySearchRepository = productCategorySearchRepository;
    }

    /**
     * Return a {@link List} of {@link ProductCategoryDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProductCategoryDTO> findByCriteria(ProductCategoryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ProductCategory> specification = createSpecification(criteria);
        return productCategoryMapper.toDto(productCategoryRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProductCategoryDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductCategoryDTO> findByCriteria(ProductCategoryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProductCategory> specification = createSpecification(criteria);
        return productCategoryRepository.findAll(specification, page)
            .map(productCategoryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductCategoryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ProductCategory> specification = createSpecification(criteria);
        return productCategoryRepository.count(specification);
    }

    /**
     * Function to convert ProductCategoryCriteria to a {@link Specification}
     */
    private Specification<ProductCategory> createSpecification(ProductCategoryCriteria criteria) {
        Specification<ProductCategory> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ProductCategory_.id));
            }
            if (criteria.getCategory() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCategory(), ProductCategory_.category));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), ProductCategory_.description));
            }
            if (criteria.getImageFullUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImageFullUrl(), ProductCategory_.imageFullUrl));
            }
            if (criteria.getImageThumbUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImageThumbUrl(), ProductCategory_.imageThumbUrl));
            }
            if (criteria.getShopId() != null) {
                specification = specification.and(buildSpecification(criteria.getShopId(),
                    root -> root.join(ProductCategory_.shop, JoinType.LEFT).get(Shop_.id)));
            }
            if (criteria.getProductsId() != null) {
                specification = specification.and(buildSpecification(criteria.getProductsId(),
                    root -> root.join(ProductCategory_.products, JoinType.LEFT).get(Product_.id)));
            }
        }
        return specification;
    }
}
