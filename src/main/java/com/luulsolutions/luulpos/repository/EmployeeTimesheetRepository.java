package com.luulsolutions.luulpos.repository;

import com.luulsolutions.luulpos.domain.EmployeeTimesheet;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the EmployeeTimesheet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmployeeTimesheetRepository extends JpaRepository<EmployeeTimesheet, Long>, JpaSpecificationExecutor<EmployeeTimesheet> {

}
