package com.luulsolutions.luulpos.repository.search;

import com.luulsolutions.luulpos.domain.ProductType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ProductType entity.
 */
public interface ProductTypeSearchRepository extends ElasticsearchRepository<ProductType, Long> {
}
