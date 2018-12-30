package com.luulsolutions.luulpos.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of SuspensionHistorySearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class SuspensionHistorySearchRepositoryMockConfiguration {

    @MockBean
    private SuspensionHistorySearchRepository mockSuspensionHistorySearchRepository;

}
