package com.luulsolutions.luulpos.service.mapper;

import com.luulsolutions.luulpos.domain.*;
import com.luulsolutions.luulpos.service.dto.ProductVariantDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ProductVariant and its DTO ProductVariantDTO.
 */
@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface ProductVariantMapper extends EntityMapper<ProductVariantDTO, ProductVariant> {

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.productName", target = "productProductName")
    ProductVariantDTO toDto(ProductVariant productVariant);

    @Mapping(source = "productId", target = "product")
    ProductVariant toEntity(ProductVariantDTO productVariantDTO);

    default ProductVariant fromId(Long id) {
        if (id == null) {
            return null;
        }
        ProductVariant productVariant = new ProductVariant();
        productVariant.setId(id);
        return productVariant;
    }
}
