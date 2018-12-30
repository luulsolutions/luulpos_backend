package com.luulsolutions.luulpos.repository.search;

import com.luulsolutions.luulpos.domain.OrdersLine;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the OrdersLine entity.
 */
public interface OrdersLineSearchRepository extends ElasticsearchRepository<OrdersLine, Long> {
}
