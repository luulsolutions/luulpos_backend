package com.luulsolutions.luulpos.repository.search;

import com.luulsolutions.luulpos.domain.SystemConfig;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the SystemConfig entity.
 */
public interface SystemConfigSearchRepository extends ElasticsearchRepository<SystemConfig, Long> {
}
