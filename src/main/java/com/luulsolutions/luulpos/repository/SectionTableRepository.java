package com.luulsolutions.luulpos.repository;

import com.luulsolutions.luulpos.domain.SectionTable;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SectionTable entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SectionTableRepository extends JpaRepository<SectionTable, Long>, JpaSpecificationExecutor<SectionTable> {
	List<SectionTable> findAllByShopSectionsId(Long shopSectionsId);

}
