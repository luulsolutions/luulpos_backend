package com.luulsolutions.luulpos.service.mapper;

import com.luulsolutions.luulpos.domain.*;
import com.luulsolutions.luulpos.service.dto.ProductDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Product and its DTO ProductDTO.
 */
@Mapper(componentModel = "spring", uses = {ProductTypeMapper.class, ShopMapper.class, DiscountMapper.class, TaxMapper.class, ProductCategoryMapper.class})
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {

    @Mapping(source = "productTypes.id", target = "productTypesId")
    @Mapping(source = "productTypes.productType", target = "productTypesProductType")
    @Mapping(source = "shop.id", target = "shopId")
    @Mapping(source = "shop.shopName", target = "shopShopName")
    @Mapping(source = "discounts.id", target = "discountsId")
    @Mapping(source = "discounts.description", target = "discountsDescription")
    @Mapping(source = "taxes.id", target = "taxesId")
    @Mapping(source = "taxes.description", target = "taxesDescription")
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.category", target = "categoryCategory")
    ProductDTO toDto(Product product);

    @Mapping(target = "variants", ignore = true)
    @Mapping(target = "extras", ignore = true)
    @Mapping(source = "productTypesId", target = "productTypes")
    @Mapping(source = "shopId", target = "shop")
    @Mapping(source = "discountsId", target = "discounts")
    @Mapping(source = "taxesId", target = "taxes")
    @Mapping(source = "categoryId", target = "category")
    Product toEntity(ProductDTO productDTO);

    default Product fromId(Long id) {
        if (id == null) {
            return null;
        }
        Product product = new Product();
        product.setId(id);
        return product;
    }
}
