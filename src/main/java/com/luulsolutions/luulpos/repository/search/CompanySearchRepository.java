package com.luulsolutions.luulpos.repository.search;

import com.luulsolutions.luulpos.domain.Company;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Company entity.
 */
public interface CompanySearchRepository extends ElasticsearchRepository<Company, Long> {
}
