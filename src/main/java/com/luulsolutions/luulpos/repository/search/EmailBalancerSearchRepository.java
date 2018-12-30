package com.luulsolutions.luulpos.repository.search;

import com.luulsolutions.luulpos.domain.EmailBalancer;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the EmailBalancer entity.
 */
public interface EmailBalancerSearchRepository extends ElasticsearchRepository<EmailBalancer, Long> {
}
