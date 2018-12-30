package com.luulsolutions.luulpos.service.mapper;

import com.luulsolutions.luulpos.domain.*;
import com.luulsolutions.luulpos.service.dto.PaymentMethodConfigDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity PaymentMethodConfig and its DTO PaymentMethodConfigDTO.
 */
@Mapper(componentModel = "spring", uses = {PaymentMethodMapper.class})
public interface PaymentMethodConfigMapper extends EntityMapper<PaymentMethodConfigDTO, PaymentMethodConfig> {

    @Mapping(source = "paymentMethod.id", target = "paymentMethodId")
    @Mapping(source = "paymentMethod.paymentMethod", target = "paymentMethodPaymentMethod")
    PaymentMethodConfigDTO toDto(PaymentMethodConfig paymentMethodConfig);

    @Mapping(source = "paymentMethodId", target = "paymentMethod")
    PaymentMethodConfig toEntity(PaymentMethodConfigDTO paymentMethodConfigDTO);

    default PaymentMethodConfig fromId(Long id) {
        if (id == null) {
            return null;
        }
        PaymentMethodConfig paymentMethodConfig = new PaymentMethodConfig();
        paymentMethodConfig.setId(id);
        return paymentMethodConfig;
    }
}
