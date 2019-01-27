package com.luulsolutions.luulpos.service;

import com.luulsolutions.luulpos.domain.PaymentMethodConfig;
import com.luulsolutions.luulpos.repository.PaymentMethodConfigRepository;
import com.luulsolutions.luulpos.repository.search.PaymentMethodConfigSearchRepository;
import com.luulsolutions.luulpos.service.dto.PaymentMethodConfigDTO;
import com.luulsolutions.luulpos.service.mapper.PaymentMethodConfigMapper;
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
 * Service Implementation for managing PaymentMethodConfig.
 */
@Service
@Transactional
public class PaymentMethodConfigService {

    private final Logger log = LoggerFactory.getLogger(PaymentMethodConfigService.class);

    private final PaymentMethodConfigRepository paymentMethodConfigRepository;

    private final PaymentMethodConfigMapper paymentMethodConfigMapper;

    private final PaymentMethodConfigSearchRepository paymentMethodConfigSearchRepository;

    public PaymentMethodConfigService(PaymentMethodConfigRepository paymentMethodConfigRepository, PaymentMethodConfigMapper paymentMethodConfigMapper, PaymentMethodConfigSearchRepository paymentMethodConfigSearchRepository) {
        this.paymentMethodConfigRepository = paymentMethodConfigRepository;
        this.paymentMethodConfigMapper = paymentMethodConfigMapper;
        this.paymentMethodConfigSearchRepository = paymentMethodConfigSearchRepository;
    }

    /**
     * Save a paymentMethodConfig.
     *
     * @param paymentMethodConfigDTO the entity to save
     * @return the persisted entity
     */
    public PaymentMethodConfigDTO save(PaymentMethodConfigDTO paymentMethodConfigDTO) {
        log.debug("Request to save PaymentMethodConfig : {}", paymentMethodConfigDTO);

        PaymentMethodConfig paymentMethodConfig = paymentMethodConfigMapper.toEntity(paymentMethodConfigDTO);
        paymentMethodConfig = paymentMethodConfigRepository.save(paymentMethodConfig);
        PaymentMethodConfigDTO result = paymentMethodConfigMapper.toDto(paymentMethodConfig);
        paymentMethodConfigSearchRepository.save(paymentMethodConfig);
        return result;
    }

    /**
     * Get all the paymentMethodConfigs.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PaymentMethodConfigDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PaymentMethodConfigs");
        return paymentMethodConfigRepository.findAll(pageable)
            .map(paymentMethodConfigMapper::toDto);
    }


    /**
     * Get one paymentMethodConfig by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<PaymentMethodConfigDTO> findOne(Long id) {
        log.debug("Request to get PaymentMethodConfig : {}", id);
        return paymentMethodConfigRepository.findById(id)
            .map(paymentMethodConfigMapper::toDto);
    }

    /**
     * Delete the paymentMethodConfig by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete PaymentMethodConfig : {}", id);
        paymentMethodConfigRepository.deleteById(id);
        paymentMethodConfigSearchRepository.deleteById(id);
    }

    /**
     * Search for the paymentMethodConfig corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PaymentMethodConfigDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PaymentMethodConfigs for query {}", query);
        return paymentMethodConfigSearchRepository.search(queryStringQuery(query), pageable)
            .map(paymentMethodConfigMapper::toDto);
    }
    
    @Transactional(readOnly = true)
 	public List<PaymentMethodConfigDTO> findAllByPaymentMethodId(Long paymentMethodId) {
 		  log.debug("Request to get all findAllByPaymentMethodId");
 	        return paymentMethodConfigMapper.toDto(paymentMethodConfigRepository.findAllByPaymentMethodId(paymentMethodId));
 	}
}
