package com.luulsolutions.luulpos.service;

import com.luulsolutions.luulpos.domain.ProductExtra;
import com.luulsolutions.luulpos.repository.ProductExtraRepository;
import com.luulsolutions.luulpos.repository.search.ProductExtraSearchRepository;
import com.luulsolutions.luulpos.service.dto.ProductExtraDTO;
import com.luulsolutions.luulpos.service.mapper.ProductExtraMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ProductExtra.
 */
@Service
@Transactional
public class ProductExtraService {

    private final Logger log = LoggerFactory.getLogger(ProductExtraService.class);

    private final ProductExtraRepository productExtraRepository;

    private final ProductExtraMapper productExtraMapper;

    private final ProductExtraSearchRepository productExtraSearchRepository;

    public ProductExtraService(ProductExtraRepository productExtraRepository, ProductExtraMapper productExtraMapper, ProductExtraSearchRepository productExtraSearchRepository) {
        this.productExtraRepository = productExtraRepository;
        this.productExtraMapper = productExtraMapper;
        this.productExtraSearchRepository = productExtraSearchRepository;
    }

    /**
     * Save a productExtra.
     *
     * @param productExtraDTO the entity to save
     * @return the persisted entity
     */
    public ProductExtraDTO save(ProductExtraDTO productExtraDTO) {
        log.debug("Request to save ProductExtra : {}", productExtraDTO);

        ProductExtra productExtra = productExtraMapper.toEntity(productExtraDTO);
        productExtra = productExtraRepository.save(productExtra);
        ProductExtraDTO result = productExtraMapper.toDto(productExtra);
        productExtraSearchRepository.save(productExtra);
        return result;
    }

    /**
     * Get all the productExtras.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ProductExtraDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProductExtras");
        return productExtraRepository.findAll(pageable)
            .map(productExtraMapper::toDto);
    }


    /**
     * Get one productExtra by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ProductExtraDTO> findOne(Long id) {
        log.debug("Request to get ProductExtra : {}", id);
        return productExtraRepository.findById(id)
            .map(productExtraMapper::toDto);
    }

    /**
     * Delete the productExtra by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ProductExtra : {}", id);
        productExtraRepository.deleteById(id);
        productExtraSearchRepository.deleteById(id);
    }

    /**
     * Search for the productExtra corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ProductExtraDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ProductExtras for query {}", query);
        return productExtraSearchRepository.search(queryStringQuery(query), pageable)
            .map(productExtraMapper::toDto);
    }
    
    @Transactional(readOnly = true)
    public List<ProductExtraDTO> findAllByProductId(Long productId) {
        log.debug("Request to get all findAllByProductId");
         List <ProductExtra> productExtraList = productExtraRepository.findAllByProductId(productId);
         return productExtraMapper.toDto(productExtraList);
    }
}
