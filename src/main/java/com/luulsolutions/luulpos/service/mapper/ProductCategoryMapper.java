package com.luulsolutions.luulpos.service.mapper;

import com.luulsolutions.luulpos.domain.*;
import com.luulsolutions.luulpos.service.dto.ProductCategoryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ProductCategory and its DTO ProductCategoryDTO.
 */
@Mapper(componentModel = "spring", uses = {ShopMapper.class})
public interface ProductCategoryMapper extends EntityMapper<ProductCategoryDTO, ProductCategory> {

    @Mapping(source = "shop.id", target = "shopId")
    @Mapping(source = "shop.shopName", target = "shopShopName")
    ProductCategoryDTO toDto(ProductCategory productCategory);

    @Mapping(source = "shopId", target = "shop")
    @Mapping(target = "products", ignore = true)
    ProductCategory toEntity(ProductCategoryDTO productCategoryDTO);

    default ProductCategory fromId(Long id) {
        if (id == null) {
            return null;
        }
        ProductCategory productCategory = new ProductCategory();
        productCategory.setId(id);
        return productCategory;
    }
}
