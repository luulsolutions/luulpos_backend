package com.luulsolutions.luulpos.repository;

import com.luulsolutions.luulpos.domain.Payment;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Payment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>, JpaSpecificationExecutor<Payment> {
	List<Payment> findAllByOrderId(Long orderId);

	Page<Payment> findAllByOrderId(Pageable pageable, Long orderId);

	Page<Payment> findAllByShopId(Pageable pageable, Long shopId);
}
