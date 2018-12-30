package com.luulsolutions.luulpos.service.mapper;

import com.luulsolutions.luulpos.domain.*;
import com.luulsolutions.luulpos.service.dto.TaxDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Tax and its DTO TaxDTO.
 */
@Mapper(componentModel = "spring", uses = {ShopMapper.class})
public interface TaxMapper extends EntityMapper<TaxDTO, Tax> {

    @Mapping(source = "shop.id", target = "shopId")
    @Mapping(source = "shop.shopName", target = "shopShopName")
    TaxDTO toDto(Tax tax);

    @Mapping(source = "shopId", target = "shop")
    @Mapping(target = "products", ignore = true)
    Tax toEntity(TaxDTO taxDTO);

    default Tax fromId(Long id) {
        if (id == null) {
            return null;
        }
        Tax tax = new Tax();
        tax.setId(id);
        return tax;
    }
}
