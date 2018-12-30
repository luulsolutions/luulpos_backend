package com.luulsolutions.luulpos.repository.search;

import com.luulsolutions.luulpos.domain.ProductVariant;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ProductVariant entity.
 */
public interface ProductVariantSearchRepository extends ElasticsearchRepository<ProductVariant, Long> {
}
