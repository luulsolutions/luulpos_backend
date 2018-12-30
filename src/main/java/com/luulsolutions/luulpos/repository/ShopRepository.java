package com.luulsolutions.luulpos.repository;

import com.luulsolutions.luulpos.domain.Shop;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Shop entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShopRepository extends JpaRepository<Shop, Long>, JpaSpecificationExecutor<Shop> {

}
