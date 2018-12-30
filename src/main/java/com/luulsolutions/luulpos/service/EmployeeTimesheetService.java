package com.luulsolutions.luulpos.service;

import com.luulsolutions.luulpos.domain.EmployeeTimesheet;
import com.luulsolutions.luulpos.repository.EmployeeTimesheetRepository;
import com.luulsolutions.luulpos.repository.search.EmployeeTimesheetSearchRepository;
import com.luulsolutions.luulpos.service.dto.EmployeeTimesheetDTO;
import com.luulsolutions.luulpos.service.mapper.EmployeeTimesheetMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing EmployeeTimesheet.
 */
@Service
@Transactional
public class EmployeeTimesheetService {

    private final Logger log = LoggerFactory.getLogger(EmployeeTimesheetService.class);

    private final EmployeeTimesheetRepository employeeTimesheetRepository;

    private final EmployeeTimesheetMapper employeeTimesheetMapper;

    private final EmployeeTimesheetSearchRepository employeeTimesheetSearchRepository;

    public EmployeeTimesheetService(EmployeeTimesheetRepository employeeTimesheetRepository, EmployeeTimesheetMapper employeeTimesheetMapper, EmployeeTimesheetSearchRepository employeeTimesheetSearchRepository) {
        this.employeeTimesheetRepository = employeeTimesheetRepository;
        this.employeeTimesheetMapper = employeeTimesheetMapper;
        this.employeeTimesheetSearchRepository = employeeTimesheetSearchRepository;
    }

    /**
     * Save a employeeTimesheet.
     *
     * @param employeeTimesheetDTO the entity to save
     * @return the persisted entity
     */
    public EmployeeTimesheetDTO save(EmployeeTimesheetDTO employeeTimesheetDTO) {
        log.debug("Request to save EmployeeTimesheet : {}", employeeTimesheetDTO);

        EmployeeTimesheet employeeTimesheet = employeeTimesheetMapper.toEntity(employeeTimesheetDTO);
        employeeTimesheet = employeeTimesheetRepository.save(employeeTimesheet);
        EmployeeTimesheetDTO result = employeeTimesheetMapper.toDto(employeeTimesheet);
        employeeTimesheetSearchRepository.save(employeeTimesheet);
        return result;
    }

    /**
     * Get all the employeeTimesheets.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<EmployeeTimesheetDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EmployeeTimesheets");
        return employeeTimesheetRepository.findAll(pageable)
            .map(employeeTimesheetMapper::toDto);
    }


    /**
     * Get one employeeTimesheet by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<EmployeeTimesheetDTO> findOne(Long id) {
        log.debug("Request to get EmployeeTimesheet : {}", id);
        return employeeTimesheetRepository.findById(id)
            .map(employeeTimesheetMapper::toDto);
    }

    /**
     * Delete the employeeTimesheet by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete EmployeeTimesheet : {}", id);
        employeeTimesheetRepository.deleteById(id);
        employeeTimesheetSearchRepository.deleteById(id);
    }

    /**
     * Search for the employeeTimesheet corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<EmployeeTimesheetDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of EmployeeTimesheets for query {}", query);
        return employeeTimesheetSearchRepository.search(queryStringQuery(query), pageable)
            .map(employeeTimesheetMapper::toDto);
    }
}
