package com.luulsolutions.luulpos.repository.search;

import com.luulsolutions.luulpos.domain.SystemEventsHistory;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the SystemEventsHistory entity.
 */
public interface SystemEventsHistorySearchRepository extends ElasticsearchRepository<SystemEventsHistory, Long> {
}
