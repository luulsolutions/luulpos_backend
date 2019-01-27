package com.luulsolutions.luulpos.service;

import com.luulsolutions.luulpos.domain.ProductCategory;
import com.luulsolutions.luulpos.repository.ProductCategoryRepository;
import com.luulsolutions.luulpos.repository.search.ProductCategorySearchRepository;
import com.luulsolutions.luulpos.service.dto.ProductCategoryDTO;
import com.luulsolutions.luulpos.service.mapper.ProductCategoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ProductCategory. 
 */
@Service
@Transactional
public class ProductCategoryService {

    private final Logger log = LoggerFactory.getLogger(ProductCategoryService.class);

    private final ProductCategoryRepository productCategoryRepository;

    private final ProductCategoryMapper productCategoryMapper;

    private final ProductCategorySearchRepository productCategorySearchRepository;

    public ProductCategoryService(ProductCategoryRepository productCategoryRepository, ProductCategoryMapper productCategoryMapper, ProductCategorySearchRepository productCategorySearchRepository) {
        this.productCategoryRepository = productCategoryRepository;
        this.productCategoryMapper = productCategoryMapper;
        this.productCategorySearchRepository = productCategorySearchRepository;
    }

    /**
     * Save a productCategory.
     *
     * @param productCategoryDTO the entity to save
     * @return the persisted entity
     */
    public ProductCategoryDTO save(ProductCategoryDTO productCategoryDTO) {
        log.debug("Request to save ProductCategory : {}", productCategoryDTO);

        ProductCategory productCategory = productCategoryMapper.toEntity(productCategoryDTO);
        productCategory = productCategoryRepository.save(productCategory);
        ProductCategoryDTO result = productCategoryMapper.toDto(productCategory);
        productCategorySearchRepository.save(productCategory);
        return result;
    }

    /**
     * Get all the productCategories.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ProductCategoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProductCategories");
        return productCategoryRepository.findAll(pageable)
            .map(productCategoryMapper::toDto);
    }


    /**
     * Get one productCategory by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ProductCategoryDTO> findOne(Long id) {
        log.debug("Request to get ProductCategory : {}", id);
        return productCategoryRepository.findById(id)
            .map(productCategoryMapper::toDto);
    }

    /**
     * Delete the productCategory by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ProductCategory : {}", id);
        productCategoryRepository.deleteById(id);
        productCategorySearchRepository.deleteById(id);
    }

    /**
     * Search for the productCategory corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ProductCategoryDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ProductCategories for query {}", query);
        return productCategorySearchRepository.search(queryStringQuery(query), pageable)
            .map(productCategoryMapper::toDto);
    }
    
    @Transactional(readOnly = true)
	public Page<ProductCategoryDTO> findAllProductCategoryByShopId(Pageable pageable, Long shopId) {
		log.debug("Request to findAllProductCategoryByShopId");
        return productCategoryRepository.findAllByShopId(pageable, shopId)
            .map(productCategoryMapper::toDto);
	}
}
