package com.luulsolutions.luulpos.repository.search;

import com.luulsolutions.luulpos.domain.PaymentMethodConfig;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the PaymentMethodConfig entity.
 */
public interface PaymentMethodConfigSearchRepository extends ElasticsearchRepository<PaymentMethodConfig, Long> {
}
