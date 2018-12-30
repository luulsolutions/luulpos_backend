package com.luulsolutions.luulpos.repository;

import com.luulsolutions.luulpos.domain.Payment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Payment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>, JpaSpecificationExecutor<Payment> {

}
