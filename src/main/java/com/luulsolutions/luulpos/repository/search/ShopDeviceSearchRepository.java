package com.luulsolutions.luulpos.repository.search;

import com.luulsolutions.luulpos.domain.ShopDevice;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ShopDevice entity.
 */
public interface ShopDeviceSearchRepository extends ElasticsearchRepository<ShopDevice, Long> {
}
