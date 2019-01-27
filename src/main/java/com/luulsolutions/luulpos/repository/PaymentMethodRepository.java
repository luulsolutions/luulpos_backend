package com.luulsolutions.luulpos.repository;

import com.luulsolutions.luulpos.domain.PaymentMethod;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the PaymentMethod entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long>, JpaSpecificationExecutor<PaymentMethod> {
	List<PaymentMethod> findAllByShopId(Long shopId);

}
