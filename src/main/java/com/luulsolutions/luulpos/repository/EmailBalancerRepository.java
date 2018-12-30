package com.luulsolutions.luulpos.repository;

import com.luulsolutions.luulpos.domain.EmailBalancer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the EmailBalancer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmailBalancerRepository extends JpaRepository<EmailBalancer, Long>, JpaSpecificationExecutor<EmailBalancer> {

}
