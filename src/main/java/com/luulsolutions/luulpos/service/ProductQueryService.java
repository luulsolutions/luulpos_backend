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

import com.luulsolutions.luulpos.domain.Product;
import com.luulsolutions.luulpos.domain.*; // for static metamodels
import com.luulsolutions.luulpos.repository.ProductRepository;
import com.luulsolutions.luulpos.repository.search.ProductSearchRepository;
import com.luulsolutions.luulpos.service.dto.ProductCriteria;
import com.luulsolutions.luulpos.service.dto.ProductDTO;
import com.luulsolutions.luulpos.service.mapper.ProductMapper;

/**
 * Service for executing complex queries for Product entities in the database.
 * The main input is a {@link ProductCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProductDTO} or a {@link Page} of {@link ProductDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductQueryService extends QueryService<Product> {

    private final Logger log = LoggerFactory.getLogger(ProductQueryService.class);

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    private final ProductSearchRepository productSearchRepository;

    public ProductQueryService(ProductRepository productRepository, ProductMapper productMapper, ProductSearchRepository productSearchRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.productSearchRepository = productSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ProductDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProductDTO> findByCriteria(ProductCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Product> specification = createSpecification(criteria);
        return productMapper.toDto(productRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProductDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductDTO> findByCriteria(ProductCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Product> specification = createSpecification(criteria);
        return productRepository.findAll(specification, page)
            .map(productMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Product> specification = createSpecification(criteria);
        return productRepository.count(specification);
    }

    /**
     * Function to convert ProductCriteria to a {@link Specification}
     */
    private Specification<Product> createSpecification(ProductCriteria criteria) {
        Specification<Product> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Product_.id));
            }
            if (criteria.getProductName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProductName(), Product_.productName));
            }
            if (criteria.getProductDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProductDescription(), Product_.productDescription));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), Product_.price));
            }
            if (criteria.getQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantity(), Product_.quantity));
            }
            if (criteria.getProductImageFullUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProductImageFullUrl(), Product_.productImageFullUrl));
            }
            if (criteria.getProductImageThumbUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProductImageThumbUrl(), Product_.productImageThumbUrl));
            }
            if (criteria.getDateCreated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateCreated(), Product_.dateCreated));
            }
            if (criteria.getBarcode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBarcode(), Product_.barcode));
            }
            if (criteria.getSerialCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSerialCode(), Product_.serialCode));
            }
            if (criteria.getPriorityPosition() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPriorityPosition(), Product_.priorityPosition));
            }
            if (criteria.getActive() != null) {
                specification = specification.and(buildSpecification(criteria.getActive(), Product_.active));
            }
            if (criteria.getIsVariantProduct() != null) {
                specification = specification.and(buildSpecification(criteria.getIsVariantProduct(), Product_.isVariantProduct));
            }
            if (criteria.getVariantsId() != null) {
                specification = specification.and(buildSpecification(criteria.getVariantsId(),
                    root -> root.join(Product_.variants, JoinType.LEFT).get(ProductVariant_.id)));
            }
            if (criteria.getExtrasId() != null) {
                specification = specification.and(buildSpecification(criteria.getExtrasId(),
                    root -> root.join(Product_.extras, JoinType.LEFT).get(ProductExtra_.id)));
            }
            if (criteria.getProductTypesId() != null) {
                specification = specification.and(buildSpecification(criteria.getProductTypesId(),
                    root -> root.join(Product_.productTypes, JoinType.LEFT).get(ProductType_.id)));
            }
            if (criteria.getShopId() != null) {
                specification = specification.and(buildSpecification(criteria.getShopId(),
                    root -> root.join(Product_.shop, JoinType.LEFT).get(Shop_.id)));
            }
            if (criteria.getDiscountsId() != null) {
                specification = specification.and(buildSpecification(criteria.getDiscountsId(),
                    root -> root.join(Product_.discounts, JoinType.LEFT).get(Discount_.id)));
            }
            if (criteria.getTaxesId() != null) {
                specification = specification.and(buildSpecification(criteria.getTaxesId(),
                    root -> root.join(Product_.taxes, JoinType.LEFT).get(Tax_.id)));
            }
            if (criteria.getCategoryId() != null) {
                specification = specification.and(buildSpecification(criteria.getCategoryId(),
                    root -> root.join(Product_.category, JoinType.LEFT).get(ProductCategory_.id)));
            }
        }
        return specification;
    }
}
