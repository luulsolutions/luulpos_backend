package com.luulsolutions.luulpos.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of OrdersLineVariantSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class OrdersLineVariantSearchRepositoryMockConfiguration {

    @MockBean
    private OrdersLineVariantSearchRepository mockOrdersLineVariantSearchRepository;

}
