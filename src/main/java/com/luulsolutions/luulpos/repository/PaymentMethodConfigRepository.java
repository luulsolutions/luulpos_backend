package com.luulsolutions.luulpos.repository;

import com.luulsolutions.luulpos.domain.PaymentMethodConfig;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the PaymentMethodConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentMethodConfigRepository extends JpaRepository<PaymentMethodConfig, Long>, JpaSpecificationExecutor<PaymentMethodConfig> {
	List<PaymentMethodConfig> findAllByPaymentMethodId(Long paymentMethodId);

}
