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

import com.luulsolutions.luulpos.domain.EmployeeTimesheet;
import com.luulsolutions.luulpos.domain.*; // for static metamodels
import com.luulsolutions.luulpos.repository.EmployeeTimesheetRepository;
import com.luulsolutions.luulpos.repository.search.EmployeeTimesheetSearchRepository;
import com.luulsolutions.luulpos.service.dto.EmployeeTimesheetCriteria;
import com.luulsolutions.luulpos.service.dto.EmployeeTimesheetDTO;
import com.luulsolutions.luulpos.service.mapper.EmployeeTimesheetMapper;

/**
 * Service for executing complex queries for EmployeeTimesheet entities in the database.
 * The main input is a {@link EmployeeTimesheetCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EmployeeTimesheetDTO} or a {@link Page} of {@link EmployeeTimesheetDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EmployeeTimesheetQueryService extends QueryService<EmployeeTimesheet> {

    private final Logger log = LoggerFactory.getLogger(EmployeeTimesheetQueryService.class);

    private final EmployeeTimesheetRepository employeeTimesheetRepository;

    private final EmployeeTimesheetMapper employeeTimesheetMapper;

    private final EmployeeTimesheetSearchRepository employeeTimesheetSearchRepository;

    public EmployeeTimesheetQueryService(EmployeeTimesheetRepository employeeTimesheetRepository, EmployeeTimesheetMapper employeeTimesheetMapper, EmployeeTimesheetSearchRepository employeeTimesheetSearchRepository) {
        this.employeeTimesheetRepository = employeeTimesheetRepository;
        this.employeeTimesheetMapper = employeeTimesheetMapper;
        this.employeeTimesheetSearchRepository = employeeTimesheetSearchRepository;
    }

    /**
     * Return a {@link List} of {@link EmployeeTimesheetDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EmployeeTimesheetDTO> findByCriteria(EmployeeTimesheetCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EmployeeTimesheet> specification = createSpecification(criteria);
        return employeeTimesheetMapper.toDto(employeeTimesheetRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EmployeeTimesheetDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EmployeeTimesheetDTO> findByCriteria(EmployeeTimesheetCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EmployeeTimesheet> specification = createSpecification(criteria);
        return employeeTimesheetRepository.findAll(specification, page)
            .map(employeeTimesheetMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EmployeeTimesheetCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EmployeeTimesheet> specification = createSpecification(criteria);
        return employeeTimesheetRepository.count(specification);
    }

    /**
     * Function to convert EmployeeTimesheetCriteria to a {@link Specification}
     */
    private Specification<EmployeeTimesheet> createSpecification(EmployeeTimesheetCriteria criteria) {
        Specification<EmployeeTimesheet> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), EmployeeTimesheet_.id));
            }
            if (criteria.getCheckinTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCheckinTime(), EmployeeTimesheet_.checkinTime));
            }
            if (criteria.getCheckOutTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCheckOutTime(), EmployeeTimesheet_.checkOutTime));
            }
            if (criteria.getRegularHoursWorked() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRegularHoursWorked(), EmployeeTimesheet_.regularHoursWorked));
            }
            if (criteria.getOverTimeHoursWorked() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOverTimeHoursWorked(), EmployeeTimesheet_.overTimeHoursWorked));
            }
            if (criteria.getPay() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPay(), EmployeeTimesheet_.pay));
            }
            if (criteria.getProfileId() != null) {
                specification = specification.and(buildSpecification(criteria.getProfileId(),
                    root -> root.join(EmployeeTimesheet_.profile, JoinType.LEFT).get(Profile_.id)));
            }
            if (criteria.getShopId() != null) {
                specification = specification.and(buildSpecification(criteria.getShopId(),
                    root -> root.join(EmployeeTimesheet_.shop, JoinType.LEFT).get(Shop_.id)));
            }
        }
        return specification;
    }
}
