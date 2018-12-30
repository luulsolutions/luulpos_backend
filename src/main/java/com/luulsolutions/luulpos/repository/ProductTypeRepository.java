package com.luulsolutions.luulpos.repository;

import com.luulsolutions.luulpos.domain.ProductType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ProductType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductTypeRepository extends JpaRepository<ProductType, Long>, JpaSpecificationExecutor<ProductType> {

}
