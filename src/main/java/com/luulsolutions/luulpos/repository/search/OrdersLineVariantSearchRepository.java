package com.luulsolutions.luulpos.repository.search;

import com.luulsolutions.luulpos.domain.OrdersLineVariant;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the OrdersLineVariant entity.
 */
public interface OrdersLineVariantSearchRepository extends ElasticsearchRepository<OrdersLineVariant, Long> {
}
