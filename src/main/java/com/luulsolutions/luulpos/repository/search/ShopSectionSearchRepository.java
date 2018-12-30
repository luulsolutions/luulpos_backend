package com.luulsolutions.luulpos.repository.search;

import com.luulsolutions.luulpos.domain.ShopSection;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ShopSection entity.
 */
public interface ShopSectionSearchRepository extends ElasticsearchRepository<ShopSection, Long> {
}
