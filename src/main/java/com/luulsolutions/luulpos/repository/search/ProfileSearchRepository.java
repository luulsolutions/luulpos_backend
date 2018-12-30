package com.luulsolutions.luulpos.repository.search;

import com.luulsolutions.luulpos.domain.Profile;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Profile entity.
 */
public interface ProfileSearchRepository extends ElasticsearchRepository<Profile, Long> {
}
