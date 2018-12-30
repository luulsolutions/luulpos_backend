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

import com.luulsolutions.luulpos.domain.ProductType;
import com.luulsolutions.luulpos.domain.*; // for static metamodels
import com.luulsolutions.luulpos.repository.ProductTypeRepository;
import com.luulsolutions.luulpos.repository.search.ProductTypeSearchRepository;
import com.luulsolutions.luulpos.service.dto.ProductTypeCriteria;
import com.luulsolutions.luulpos.service.dto.ProductTypeDTO;
import com.luulsolutions.luulpos.service.mapper.ProductTypeMapper;

/**
 * Service for executing complex queries for ProductType entities in the database.
 * The main input is a {@link ProductTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProductTypeDTO} or a {@link Page} of {@link ProductTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductTypeQueryService extends QueryService<ProductType> {

    private final Logger log = LoggerFactory.getLogger(ProductTypeQueryService.class);

    private final ProductTypeRepository productTypeRepository;

    private final ProductTypeMapper productTypeMapper;

    private final ProductTypeSearchRepository productTypeSearchRepository;

    public ProductTypeQueryService(ProductTypeRepository productTypeRepository, ProductTypeMapper productTypeMapper, ProductTypeSearchRepository productTypeSearchRepository) {
        this.productTypeRepository = productTypeRepository;
        this.productTypeMapper = productTypeMapper;
        this.productTypeSearchRepository = productTypeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ProductTypeDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProductTypeDTO> findByCriteria(ProductTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ProductType> specification = createSpecification(criteria);
        return productTypeMapper.toDto(productTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProductTypeDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductTypeDTO> findByCriteria(ProductTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProductType> specification = createSpecification(criteria);
        return productTypeRepository.findAll(specification, page)
            .map(productTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ProductType> specification = createSpecification(criteria);
        return productTypeRepository.count(specification);
    }

    /**
     * Function to convert ProductTypeCriteria to a {@link Specification}
     */
    private Specification<ProductType> createSpecification(ProductTypeCriteria criteria) {
        Specification<ProductType> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ProductType_.id));
            }
            if (criteria.getProductType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProductType(), ProductType_.productType));
            }
            if (criteria.getProductTypeDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProductTypeDescription(), ProductType_.productTypeDescription));
            }
            if (criteria.getShopId() != null) {
                specification = specification.and(buildSpecification(criteria.getShopId(),
                    root -> root.join(ProductType_.shop, JoinType.LEFT).get(Shop_.id)));
            }
        }
        return specification;
    }
}
