package com.luulsolutions.luulpos.repository.search;

import com.luulsolutions.luulpos.domain.Payment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Payment entity.
 */
public interface PaymentSearchRepository extends ElasticsearchRepository<Payment, Long> {
}
