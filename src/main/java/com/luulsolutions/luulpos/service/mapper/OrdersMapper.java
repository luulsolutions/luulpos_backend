package com.luulsolutions.luulpos.service.mapper;

import com.luulsolutions.luulpos.domain.*;
import com.luulsolutions.luulpos.service.dto.OrdersDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Orders and its DTO OrdersDTO.
 */
@Mapper(componentModel = "spring", uses = {PaymentMethodMapper.class, ProfileMapper.class, ShopDeviceMapper.class, SectionTableMapper.class, ShopMapper.class})
public interface OrdersMapper extends EntityMapper<OrdersDTO, Orders> {

    @Mapping(source = "paymentMethod.id", target = "paymentMethodId")
    @Mapping(source = "paymentMethod.paymentMethod", target = "paymentMethodPaymentMethod")
    @Mapping(source = "soldBy.id", target = "soldById")
    @Mapping(source = "soldBy.firstName", target = "soldByFirstName")
    @Mapping(source = "preparedBy.id", target = "preparedById")
    @Mapping(source = "preparedBy.firstName", target = "preparedByFirstName")
    @Mapping(source = "shopDevice.id", target = "shopDeviceId")
    @Mapping(source = "shopDevice.deviceName", target = "shopDeviceDeviceName")
    @Mapping(source = "sectionTable.id", target = "sectionTableId")
    @Mapping(source = "sectionTable.tableNumber", target = "sectionTableTableNumber")
    @Mapping(source = "shop.id", target = "shopId")
    @Mapping(source = "shop.shopName", target = "shopShopName")
    OrdersDTO toDto(Orders orders);

    @Mapping(target = "ordersLines", ignore = true)
    @Mapping(source = "paymentMethodId", target = "paymentMethod")
    @Mapping(source = "soldById", target = "soldBy")
    @Mapping(source = "preparedById", target = "preparedBy")
    @Mapping(source = "shopDeviceId", target = "shopDevice")
    @Mapping(source = "sectionTableId", target = "sectionTable")
    @Mapping(source = "shopId", target = "shop")
    Orders toEntity(OrdersDTO ordersDTO);

    default Orders fromId(Long id) {
        if (id == null) {
            return null;
        }
        Orders orders = new Orders();
        orders.setId(id);
        return orders;
    }
}
