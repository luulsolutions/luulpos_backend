package com.luulsolutions.luulpos.repository.search;

import com.luulsolutions.luulpos.domain.Shop;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Shop entity.
 */
public interface ShopSearchRepository extends ElasticsearchRepository<Shop, Long> {
}
