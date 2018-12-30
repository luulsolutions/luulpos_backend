package com.luulsolutions.luulpos.service.mapper;

import com.luulsolutions.luulpos.domain.*;
import com.luulsolutions.luulpos.service.dto.PaymentMethodDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity PaymentMethod and its DTO PaymentMethodDTO.
 */
@Mapper(componentModel = "spring", uses = {ShopMapper.class})
public interface PaymentMethodMapper extends EntityMapper<PaymentMethodDTO, PaymentMethod> {

    @Mapping(source = "shop.id", target = "shopId")
    @Mapping(source = "shop.shopName", target = "shopShopName")
    PaymentMethodDTO toDto(PaymentMethod paymentMethod);

    @Mapping(source = "shopId", target = "shop")
    PaymentMethod toEntity(PaymentMethodDTO paymentMethodDTO);

    default PaymentMethod fromId(Long id) {
        if (id == null) {
            return null;
        }
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setId(id);
        return paymentMethod;
    }
}
