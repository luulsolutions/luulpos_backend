package com.luulsolutions.luulpos.repository.search;

import com.luulsolutions.luulpos.domain.Discount;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Discount entity.
 */
public interface DiscountSearchRepository extends ElasticsearchRepository<Discount, Long> {
}
