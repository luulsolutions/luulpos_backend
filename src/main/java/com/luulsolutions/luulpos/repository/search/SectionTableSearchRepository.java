package com.luulsolutions.luulpos.repository.search;

import com.luulsolutions.luulpos.domain.SectionTable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the SectionTable entity.
 */
public interface SectionTableSearchRepository extends ElasticsearchRepository<SectionTable, Long> {
}
