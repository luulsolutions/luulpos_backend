package com.luulsolutions.luulpos.repository.search;

import com.luulsolutions.luulpos.domain.ProductExtra;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ProductExtra entity.
 */
public interface ProductExtraSearchRepository extends ElasticsearchRepository<ProductExtra, Long> {
}
