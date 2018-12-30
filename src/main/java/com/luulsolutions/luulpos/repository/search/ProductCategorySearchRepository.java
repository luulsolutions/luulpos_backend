package com.luulsolutions.luulpos.repository.search;

import com.luulsolutions.luulpos.domain.ProductCategory;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ProductCategory entity.
 */
public interface ProductCategorySearchRepository extends ElasticsearchRepository<ProductCategory, Long> {
}
