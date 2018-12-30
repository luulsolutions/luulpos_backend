package com.luulsolutions.luulpos.repository;

import com.luulsolutions.luulpos.domain.OrdersLineExtra;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the OrdersLineExtra entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrdersLineExtraRepository extends JpaRepository<OrdersLineExtra, Long>, JpaSpecificationExecutor<OrdersLineExtra> {

}
