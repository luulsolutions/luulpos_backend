package com.luulsolutions.luulpos.service;

import com.luulsolutions.luulpos.domain.Payment;
import com.luulsolutions.luulpos.repository.PaymentRepository;
import com.luulsolutions.luulpos.repository.search.PaymentSearchRepository;
import com.luulsolutions.luulpos.service.dto.PaymentDTO;
import com.luulsolutions.luulpos.service.mapper.PaymentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Payment.
 */
@Service
@Transactional
public class PaymentService {

    private final Logger log = LoggerFactory.getLogger(PaymentService.class);

    private final PaymentRepository paymentRepository;

    private final PaymentMapper paymentMapper;

    private final PaymentSearchRepository paymentSearchRepository;

    public PaymentService(PaymentRepository paymentRepository, PaymentMapper paymentMapper, PaymentSearchRepository paymentSearchRepository) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
        this.paymentSearchRepository = paymentSearchRepository;
    }

    /**
     * Save a payment.
     *
     * @param paymentDTO the entity to save
     * @return the persisted entity
     */
    public PaymentDTO save(PaymentDTO paymentDTO) {
        log.debug("Request to save Payment : {}", paymentDTO);

        Payment payment = paymentMapper.toEntity(paymentDTO);
        payment = paymentRepository.save(payment);
        PaymentDTO result = paymentMapper.toDto(payment);
        paymentSearchRepository.save(payment);
        return result;
    }

    /**
     * Get all the payments.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PaymentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Payments");
        return paymentRepository.findAll(pageable)
            .map(paymentMapper::toDto);
    }


    /**
     * Get one payment by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<PaymentDTO> findOne(Long id) {
        log.debug("Request to get Payment : {}", id);
        return paymentRepository.findById(id)
            .map(paymentMapper::toDto);
    }

    /**
     * Delete the payment by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Payment : {}", id);
        paymentRepository.deleteById(id);
        paymentSearchRepository.deleteById(id);
    }

    /**
     * Search for the payment corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PaymentDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Payments for query {}", query);
        return paymentSearchRepository.search(queryStringQuery(query), pageable)
            .map(paymentMapper::toDto);
    }
}
