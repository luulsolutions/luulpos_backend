package com.luulsolutions.luulpos.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of SectionTableSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class SectionTableSearchRepositoryMockConfiguration {

    @MockBean
    private SectionTableSearchRepository mockSectionTableSearchRepository;

}
