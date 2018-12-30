package com.luulsolutions.luulpos.repository;

import com.luulsolutions.luulpos.domain.ShopSection;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ShopSection entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShopSectionRepository extends JpaRepository<ShopSection, Long>, JpaSpecificationExecutor<ShopSection> {

}
