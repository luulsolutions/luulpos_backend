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

import com.luulsolutions.luulpos.domain.ProductExtra;
import com.luulsolutions.luulpos.domain.*; // for static metamodels
import com.luulsolutions.luulpos.repository.ProductExtraRepository;
import com.luulsolutions.luulpos.repository.search.ProductExtraSearchRepository;
import com.luulsolutions.luulpos.service.dto.ProductExtraCriteria;
import com.luulsolutions.luulpos.service.dto.ProductExtraDTO;
import com.luulsolutions.luulpos.service.mapper.ProductExtraMapper;

/**
 * Service for executing complex queries for ProductExtra entities in the database.
 * The main input is a {@link ProductExtraCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProductExtraDTO} or a {@link Page} of {@link ProductExtraDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductExtraQueryService extends QueryService<ProductExtra> {

    private final Logger log = LoggerFactory.getLogger(ProductExtraQueryService.class);

    private final ProductExtraRepository productExtraRepository;

    private final ProductExtraMapper productExtraMapper;

    private final ProductExtraSearchRepository productExtraSearchRepository;

    public ProductExtraQueryService(ProductExtraRepository productExtraRepository, ProductExtraMapper productExtraMapper, ProductExtraSearchRepository productExtraSearchRepository) {
        this.productExtraRepository = productExtraRepository;
        this.productExtraMapper = productExtraMapper;
        this.productExtraSearchRepository = productExtraSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ProductExtraDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProductExtraDTO> findByCriteria(ProductExtraCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ProductExtra> specification = createSpecification(criteria);
        return productExtraMapper.toDto(productExtraRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProductExtraDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductExtraDTO> findByCriteria(ProductExtraCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProductExtra> specification = createSpecification(criteria);
        return productExtraRepository.findAll(specification, page)
            .map(productExtraMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductExtraCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ProductExtra> specification = createSpecification(criteria);
        return productExtraRepository.count(specification);
    }

    /**
     * Function to convert ProductExtraCriteria to a {@link Specification}
     */
    private Specification<ProductExtra> createSpecification(ProductExtraCriteria criteria) {
        Specification<ProductExtra> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ProductExtra_.id));
            }
            if (criteria.getExtraName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtraName(), ProductExtra_.extraName));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), ProductExtra_.description));
            }
            if (criteria.getExtraValue() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getExtraValue(), ProductExtra_.extraValue));
            }
            if (criteria.getFullPhotoUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFullPhotoUrl(), ProductExtra_.fullPhotoUrl));
            }
            if (criteria.getThumbnailPhotoUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getThumbnailPhotoUrl(), ProductExtra_.thumbnailPhotoUrl));
            }
            if (criteria.getProductId() != null) {
                specification = specification.and(buildSpecification(criteria.getProductId(),
                    root -> root.join(ProductExtra_.product, JoinType.LEFT).get(Product_.id)));
            }
        }
        return specification;
    }
}
