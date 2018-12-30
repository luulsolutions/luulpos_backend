package com.luulsolutions.luulpos.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of SystemConfigSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class SystemConfigSearchRepositoryMockConfiguration {

    @MockBean
    private SystemConfigSearchRepository mockSystemConfigSearchRepository;

}
