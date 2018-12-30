package com.luulsolutions.luulpos.service.mapper;

import com.luulsolutions.luulpos.domain.*;
import com.luulsolutions.luulpos.service.dto.EmployeeTimesheetDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity EmployeeTimesheet and its DTO EmployeeTimesheetDTO.
 */
@Mapper(componentModel = "spring", uses = {ProfileMapper.class, ShopMapper.class})
public interface EmployeeTimesheetMapper extends EntityMapper<EmployeeTimesheetDTO, EmployeeTimesheet> {

    @Mapping(source = "profile.id", target = "profileId")
    @Mapping(source = "profile.firstName", target = "profileFirstName")
    @Mapping(source = "shop.id", target = "shopId")
    @Mapping(source = "shop.shopName", target = "shopShopName")
    EmployeeTimesheetDTO toDto(EmployeeTimesheet employeeTimesheet);

    @Mapping(source = "profileId", target = "profile")
    @Mapping(source = "shopId", target = "shop")
    EmployeeTimesheet toEntity(EmployeeTimesheetDTO employeeTimesheetDTO);

    default EmployeeTimesheet fromId(Long id) {
        if (id == null) {
            return null;
        }
        EmployeeTimesheet employeeTimesheet = new EmployeeTimesheet();
        employeeTimesheet.setId(id);
        return employeeTimesheet;
    }
}
