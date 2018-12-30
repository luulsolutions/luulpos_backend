package com.luulsolutions.luulpos.repository.search;

import com.luulsolutions.luulpos.domain.EmployeeTimesheet;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the EmployeeTimesheet entity.
 */
public interface EmployeeTimesheetSearchRepository extends ElasticsearchRepository<EmployeeTimesheet, Long> {
}
