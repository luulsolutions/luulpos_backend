package com.luulsolutions.luulpos.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of ProductTypeSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ProductTypeSearchRepositoryMockConfiguration {

    @MockBean
    private ProductTypeSearchRepository mockProductTypeSearchRepository;

}
