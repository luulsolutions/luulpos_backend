package com.luulsolutions.luulpos.repository.search;

import com.luulsolutions.luulpos.domain.ShopChange;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ShopChange entity.
 */
public interface ShopChangeSearchRepository extends ElasticsearchRepository<ShopChange, Long> {
}
