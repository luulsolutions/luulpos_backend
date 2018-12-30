package com.luulsolutions.luulpos.service;

import com.luulsolutions.luulpos.domain.EmailBalancer;
import com.luulsolutions.luulpos.repository.EmailBalancerRepository;
import com.luulsolutions.luulpos.repository.search.EmailBalancerSearchRepository;
import com.luulsolutions.luulpos.service.dto.EmailBalancerDTO;
import com.luulsolutions.luulpos.service.mapper.EmailBalancerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing EmailBalancer.
 */
@Service
@Transactional
public class EmailBalancerService {

    private final Logger log = LoggerFactory.getLogger(EmailBalancerService.class);

    private final EmailBalancerRepository emailBalancerRepository;

    private final EmailBalancerMapper emailBalancerMapper;

    private final EmailBalancerSearchRepository emailBalancerSearchRepository;

    public EmailBalancerService(EmailBalancerRepository emailBalancerRepository, EmailBalancerMapper emailBalancerMapper, EmailBalancerSearchRepository emailBalancerSearchRepository) {
        this.emailBalancerRepository = emailBalancerRepository;
        this.emailBalancerMapper = emailBalancerMapper;
        this.emailBalancerSearchRepository = emailBalancerSearchRepository;
    }

    /**
     * Save a emailBalancer.
     *
     * @param emailBalancerDTO the entity to save
     * @return the persisted entity
     */
    public EmailBalancerDTO save(EmailBalancerDTO emailBalancerDTO) {
        log.debug("Request to save EmailBalancer : {}", emailBalancerDTO);

        EmailBalancer emailBalancer = emailBalancerMapper.toEntity(emailBalancerDTO);
        emailBalancer = emailBalancerRepository.save(emailBalancer);
        EmailBalancerDTO result = emailBalancerMapper.toDto(emailBalancer);
        emailBalancerSearchRepository.save(emailBalancer);
        return result;
    }

    /**
     * Get all the emailBalancers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<EmailBalancerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EmailBalancers");
        return emailBalancerRepository.findAll(pageable)
            .map(emailBalancerMapper::toDto);
    }


    /**
     * Get one emailBalancer by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<EmailBalancerDTO> findOne(Long id) {
        log.debug("Request to get EmailBalancer : {}", id);
        return emailBalancerRepository.findById(id)
            .map(emailBalancerMapper::toDto);
    }

    /**
     * Delete the emailBalancer by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete EmailBalancer : {}", id);
        emailBalancerRepository.deleteById(id);
        emailBalancerSearchRepository.deleteById(id);
    }

    /**
     * Search for the emailBalancer corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<EmailBalancerDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of EmailBalancers for query {}", query);
        return emailBalancerSearchRepository.search(queryStringQuery(query), pageable)
            .map(emailBalancerMapper::toDto);
    }
}
