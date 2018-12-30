package com.luulsolutions.luulpos.service.mapper;

import com.luulsolutions.luulpos.domain.*;
import com.luulsolutions.luulpos.service.dto.ShopDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Shop and its DTO ShopDTO.
 */
@Mapper(componentModel = "spring", uses = {CompanyMapper.class, ProfileMapper.class})
public interface ShopMapper extends EntityMapper<ShopDTO, Shop> {

    @Mapping(source = "company.id", target = "companyId")
    @Mapping(source = "company.companyName", target = "companyCompanyName")
    @Mapping(source = "approvedBy.id", target = "approvedById")
    @Mapping(source = "approvedBy.firstName", target = "approvedByFirstName")
    ShopDTO toDto(Shop shop);

    @Mapping(source = "companyId", target = "company")
    @Mapping(source = "approvedById", target = "approvedBy")
    @Mapping(target = "profiles", ignore = true)
    @Mapping(target = "productCategories", ignore = true)
    @Mapping(target = "productTypes", ignore = true)
    @Mapping(target = "systemConfigs", ignore = true)
    @Mapping(target = "discounts", ignore = true)
    @Mapping(target = "taxes", ignore = true)
    Shop toEntity(ShopDTO shopDTO);

    default Shop fromId(Long id) {
        if (id == null) {
            return null;
        }
        Shop shop = new Shop();
        shop.setId(id);
        return shop;
    }
}
