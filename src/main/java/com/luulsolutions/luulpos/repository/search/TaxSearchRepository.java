package com.luulsolutions.luulpos.repository.search;

import com.luulsolutions.luulpos.domain.Tax;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Tax entity.
 */
public interface TaxSearchRepository extends ElasticsearchRepository<Tax, Long> {
}
