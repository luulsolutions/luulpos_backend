package com.luulsolutions.luulpos.repository.search;

import com.luulsolutions.luulpos.domain.OrdersLineExtra;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the OrdersLineExtra entity.
 */
public interface OrdersLineExtraSearchRepository extends ElasticsearchRepository<OrdersLineExtra, Long> {
}
