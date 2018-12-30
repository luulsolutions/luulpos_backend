package com.luulsolutions.luulpos.repository;

import com.luulsolutions.luulpos.domain.ProductExtra;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ProductExtra entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductExtraRepository extends JpaRepository<ProductExtra, Long>, JpaSpecificationExecutor<ProductExtra> {

}
