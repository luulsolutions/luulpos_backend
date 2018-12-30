package com.luulsolutions.luulpos.service;

import com.luulsolutions.luulpos.domain.ProductVariant;
import com.luulsolutions.luulpos.repository.ProductVariantRepository;
import com.luulsolutions.luulpos.repository.search.ProductVariantSearchRepository;
import com.luulsolutions.luulpos.service.dto.ProductVariantDTO;
import com.luulsolutions.luulpos.service.mapper.ProductVariantMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ProductVariant.
 */
@Service
@Transactional
public class ProductVariantService {

    private final Logger log = LoggerFactory.getLogger(ProductVariantService.class);

    private final ProductVariantRepository productVariantRepository;

    private final ProductVariantMapper productVariantMapper;

    private final ProductVariantSearchRepository productVariantSearchRepository;

    public ProductVariantService(ProductVariantRepository productVariantRepository, ProductVariantMapper productVariantMapper, ProductVariantSearchRepository productVariantSearchRepository) {
        this.productVariantRepository = productVariantRepository;
        this.productVariantMapper = productVariantMapper;
        this.productVariantSearchRepository = productVariantSearchRepository;
    }

    /**
     * Save a productVariant.
     *
     * @param productVariantDTO the entity to save
     * @return the persisted entity
     */
    public ProductVariantDTO save(ProductVariantDTO productVariantDTO) {
        log.debug("Request to save ProductVariant : {}", productVariantDTO);

        ProductVariant productVariant = productVariantMapper.toEntity(productVariantDTO);
        productVariant = productVariantRepository.save(productVariant);
        ProductVariantDTO result = productVariantMapper.toDto(productVariant);
        productVariantSearchRepository.save(productVariant);
        return result;
    }

    /**
     * Get all the productVariants.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ProductVariantDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProductVariants");
        return productVariantRepository.findAll(pageable)
            .map(productVariantMapper::toDto);
    }


    /**
     * Get one productVariant by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ProductVariantDTO> findOne(Long id) {
        log.debug("Request to get ProductVariant : {}", id);
        return productVariantRepository.findById(id)
            .map(productVariantMapper::toDto);
    }

    /**
     * Delete the productVariant by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ProductVariant : {}", id);
        productVariantRepository.deleteById(id);
        productVariantSearchRepository.deleteById(id);
    }

    /**
     * Search for the productVariant corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ProductVariantDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ProductVariants for query {}", query);
        return productVariantSearchRepository.search(queryStringQuery(query), pageable)
            .map(productVariantMapper::toDto);
    }
}
