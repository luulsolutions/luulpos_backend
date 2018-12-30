package com.luulsolutions.luulpos.repository.search;

import com.luulsolutions.luulpos.domain.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the User entity.
 */
public interface UserSearchRepository extends ElasticsearchRepository<User, Long> {
}
