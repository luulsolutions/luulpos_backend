package com.luulsolutions.luulpos.service;

import com.luulsolutions.luulpos.domain.PaymentMethod;
import com.luulsolutions.luulpos.repository.PaymentMethodRepository;
import com.luulsolutions.luulpos.repository.search.PaymentMethodSearchRepository;
import com.luulsolutions.luulpos.service.dto.PaymentMethodDTO;
import com.luulsolutions.luulpos.service.mapper.PaymentMethodMapper;
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
 * Service Implementation for managing PaymentMethod.
 */
@Service
@Transactional
public class PaymentMethodService {

    private final Logger log = LoggerFactory.getLogger(PaymentMethodService.class);

    private final PaymentMethodRepository paymentMethodRepository;

    private final PaymentMethodMapper paymentMethodMapper;

    private final PaymentMethodSearchRepository paymentMethodSearchRepository;

    public PaymentMethodService(PaymentMethodRepository paymentMethodRepository, PaymentMethodMapper paymentMethodMapper, PaymentMethodSearchRepository paymentMethodSearchRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
        this.paymentMethodMapper = paymentMethodMapper;
        this.paymentMethodSearchRepository = paymentMethodSearchRepository;
    }

    /**
     * Save a paymentMethod.
     *
     * @param paymentMethodDTO the entity to save
     * @return the persisted entity
     */
    public PaymentMethodDTO save(PaymentMethodDTO paymentMethodDTO) {
        log.debug("Request to save PaymentMethod : {}", paymentMethodDTO);

        PaymentMethod paymentMethod = paymentMethodMapper.toEntity(paymentMethodDTO);
        paymentMethod = paymentMethodRepository.save(paymentMethod);
        PaymentMethodDTO result = paymentMethodMapper.toDto(paymentMethod);
        paymentMethodSearchRepository.save(paymentMethod);
        return result;
    }

    /**
     * Get all the paymentMethods.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PaymentMethodDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PaymentMethods");
        return paymentMethodRepository.findAll(pageable)
            .map(paymentMethodMapper::toDto);
    }


    /**
     * Get one paymentMethod by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<PaymentMethodDTO> findOne(Long id) {
        log.debug("Request to get PaymentMethod : {}", id);
        return paymentMethodRepository.findById(id)
            .map(paymentMethodMapper::toDto);
    }

    /**
     * Delete the paymentMethod by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete PaymentMethod : {}", id);
        paymentMethodRepository.deleteById(id);
        paymentMethodSearchRepository.deleteById(id);
    }

    /**
     * Search for the paymentMethod corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PaymentMethodDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PaymentMethods for query {}", query);
        return paymentMethodSearchRepository.search(queryStringQuery(query), pageable)
            .map(paymentMethodMapper::toDto);
    }
    
    @Transactional(readOnly = true)
    public List<PaymentMethodDTO> findAllByShopId(Long shopId) {
        log.debug("Request to get all PaymentMethods");
        List<PaymentMethod> paymentMethodList = paymentMethodRepository.findAllByShopId(shopId);
           return paymentMethodMapper.toDto(paymentMethodList);
            
            
    }
}
