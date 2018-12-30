package com.luulsolutions.luulpos.service;

import com.luulsolutions.luulpos.domain.ProductType;
import com.luulsolutions.luulpos.repository.ProductTypeRepository;
import com.luulsolutions.luulpos.repository.search.ProductTypeSearchRepository;
import com.luulsolutions.luulpos.service.dto.ProductTypeDTO;
import com.luulsolutions.luulpos.service.mapper.ProductTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ProductType.
 */
@Service
@Transactional
public class ProductTypeService {

    private final Logger log = LoggerFactory.getLogger(ProductTypeService.class);

    private final ProductTypeRepository productTypeRepository;

    private final ProductTypeMapper productTypeMapper;

    private final ProductTypeSearchRepository productTypeSearchRepository;

    public ProductTypeService(ProductTypeRepository productTypeRepository, ProductTypeMapper productTypeMapper, ProductTypeSearchRepository productTypeSearchRepository) {
        this.productTypeRepository = productTypeRepository;
        this.productTypeMapper = productTypeMapper;
        this.productTypeSearchRepository = productTypeSearchRepository;
    }

    /**
     * Save a productType.
     *
     * @param productTypeDTO the entity to save
     * @return the persisted entity
     */
    public ProductTypeDTO save(ProductTypeDTO productTypeDTO) {
        log.debug("Request to save ProductType : {}", productTypeDTO);

        ProductType productType = productTypeMapper.toEntity(productTypeDTO);
        productType = productTypeRepository.save(productType);
        ProductTypeDTO result = productTypeMapper.toDto(productType);
        productTypeSearchRepository.save(productType);
        return result;
    }

    /**
     * Get all the productTypes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ProductTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProductTypes");
        return productTypeRepository.findAll(pageable)
            .map(productTypeMapper::toDto);
    }


    /**
     * Get one productType by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ProductTypeDTO> findOne(Long id) {
        log.debug("Request to get ProductType : {}", id);
        return productTypeRepository.findById(id)
            .map(productTypeMapper::toDto);
    }

    /**
     * Delete the productType by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ProductType : {}", id);
        productTypeRepository.deleteById(id);
        productTypeSearchRepository.deleteById(id);
    }

    /**
     * Search for the productType corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ProductTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ProductTypes for query {}", query);
        return productTypeSearchRepository.search(queryStringQuery(query), pageable)
            .map(productTypeMapper::toDto);
    }
}
