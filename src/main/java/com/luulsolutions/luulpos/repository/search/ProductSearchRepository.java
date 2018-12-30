package com.luulsolutions.luulpos.repository.search;

import com.luulsolutions.luulpos.domain.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Product entity.
 */
public interface ProductSearchRepository extends ElasticsearchRepository<Product, Long> {
}
