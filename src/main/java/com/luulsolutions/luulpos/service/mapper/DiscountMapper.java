package com.luulsolutions.luulpos.service.mapper;

import com.luulsolutions.luulpos.domain.*;
import com.luulsolutions.luulpos.service.dto.DiscountDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Discount and its DTO DiscountDTO.
 */
@Mapper(componentModel = "spring", uses = {ShopMapper.class})
public interface DiscountMapper extends EntityMapper<DiscountDTO, Discount> {

    @Mapping(source = "shop.id", target = "shopId")
    @Mapping(source = "shop.shopName", target = "shopShopName")
    DiscountDTO toDto(Discount discount);

    @Mapping(source = "shopId", target = "shop")
    @Mapping(target = "products", ignore = true)
    Discount toEntity(DiscountDTO discountDTO);

    default Discount fromId(Long id) {
        if (id == null) {
            return null;
        }
        Discount discount = new Discount();
        discount.setId(id);
        return discount;
    }
}
