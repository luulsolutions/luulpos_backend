package com.luulsolutions.luulpos.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of DiscountSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class DiscountSearchRepositoryMockConfiguration {

    @MockBean
    private DiscountSearchRepository mockDiscountSearchRepository;

}
