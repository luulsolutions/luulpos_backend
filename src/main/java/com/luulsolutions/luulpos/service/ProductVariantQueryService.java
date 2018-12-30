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

import com.luulsolutions.luulpos.domain.ProductVariant;
import com.luulsolutions.luulpos.domain.*; // for static metamodels
import com.luulsolutions.luulpos.repository.ProductVariantRepository;
import com.luulsolutions.luulpos.repository.search.ProductVariantSearchRepository;
import com.luulsolutions.luulpos.service.dto.ProductVariantCriteria;
import com.luulsolutions.luulpos.service.dto.ProductVariantDTO;
import com.luulsolutions.luulpos.service.mapper.ProductVariantMapper;

/**
 * Service for executing complex queries for ProductVariant entities in the database.
 * The main input is a {@link ProductVariantCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProductVariantDTO} or a {@link Page} of {@link ProductVariantDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductVariantQueryService extends QueryService<ProductVariant> {

    private final Logger log = LoggerFactory.getLogger(ProductVariantQueryService.class);

    private final ProductVariantRepository productVariantRepository;

    private final ProductVariantMapper productVariantMapper;

    private final ProductVariantSearchRepository productVariantSearchRepository;

    public ProductVariantQueryService(ProductVariantRepository productVariantRepository, ProductVariantMapper productVariantMapper, ProductVariantSearchRepository productVariantSearchRepository) {
        this.productVariantRepository = productVariantRepository;
        this.productVariantMapper = productVariantMapper;
        this.productVariantSearchRepository = productVariantSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ProductVariantDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProductVariantDTO> findByCriteria(ProductVariantCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ProductVariant> specification = createSpecification(criteria);
        return productVariantMapper.toDto(productVariantRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProductVariantDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductVariantDTO> findByCriteria(ProductVariantCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProductVariant> specification = createSpecification(criteria);
        return productVariantRepository.findAll(specification, page)
            .map(productVariantMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductVariantCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ProductVariant> specification = createSpecification(criteria);
        return productVariantRepository.count(specification);
    }

    /**
     * Function to convert ProductVariantCriteria to a {@link Specification}
     */
    private Specification<ProductVariant> createSpecification(ProductVariantCriteria criteria) {
        Specification<ProductVariant> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ProductVariant_.id));
            }
            if (criteria.getVariantName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVariantName(), ProductVariant_.variantName));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), ProductVariant_.description));
            }
            if (criteria.getPercentage() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPercentage(), ProductVariant_.percentage));
            }
            if (criteria.getFullPhotoUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFullPhotoUrl(), ProductVariant_.fullPhotoUrl));
            }
            if (criteria.getThumbnailPhotoUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getThumbnailPhotoUrl(), ProductVariant_.thumbnailPhotoUrl));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), ProductVariant_.price));
            }
            if (criteria.getProductId() != null) {
                specification = specification.and(buildSpecification(criteria.getProductId(),
                    root -> root.join(ProductVariant_.product, JoinType.LEFT).get(Product_.id)));
            }
        }
        return specification;
    }
}
