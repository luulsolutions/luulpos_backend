package com.luulsolutions.luulpos.repository;

import com.luulsolutions.luulpos.domain.SuspensionHistory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SuspensionHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SuspensionHistoryRepository extends JpaRepository<SuspensionHistory, Long>, JpaSpecificationExecutor<SuspensionHistory> {

}
