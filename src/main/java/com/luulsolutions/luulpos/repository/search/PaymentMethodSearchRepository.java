package com.luulsolutions.luulpos.repository.search;

import com.luulsolutions.luulpos.domain.PaymentMethod;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the PaymentMethod entity.
 */
public interface PaymentMethodSearchRepository extends ElasticsearchRepository<PaymentMethod, Long> {
}
