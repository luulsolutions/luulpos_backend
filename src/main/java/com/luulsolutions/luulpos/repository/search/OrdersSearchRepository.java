package com.luulsolutions.luulpos.repository.search;

import com.luulsolutions.luulpos.domain.Orders;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Orders entity.
 */
public interface OrdersSearchRepository extends ElasticsearchRepository<Orders, Long> {
}
