package com.luulsolutions.luulpos.repository;

import com.luulsolutions.luulpos.domain.Product;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Product entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

}
