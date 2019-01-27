package com.luulsolutions.luulpos.repository;

import com.luulsolutions.luulpos.domain.SystemConfig;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SystemConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SystemConfigRepository extends JpaRepository<SystemConfig, Long>, JpaSpecificationExecutor<SystemConfig> {
	List<SystemConfig> findAllByShopId(long shopId);

}
