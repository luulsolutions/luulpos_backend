package com.luulsolutions.luulpos.repository;

import com.luulsolutions.luulpos.domain.ProductVariant;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ProductVariant entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long>, JpaSpecificationExecutor<ProductVariant> {
	List<ProductVariant> findAllByProductId(Long productId);

}
