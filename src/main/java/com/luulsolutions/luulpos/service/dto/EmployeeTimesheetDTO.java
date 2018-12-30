package com.luulsolutions.luulpos.service.dto;

import java.time.ZonedDateTime;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the EmployeeTimesheet entity.
 */
public class EmployeeTimesheetDTO implements Serializable {

    private Long id;

    private ZonedDateTime checkinTime;

    private ZonedDateTime checkOutTime;

    private Integer regularHoursWorked;

    private Integer overTimeHoursWorked;

    private BigDecimal pay;

    private Long profileId;

    private String profileFirstName;

    private Long shopId;

    private String shopShopName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCheckinTime() {
        return checkinTime;
    }

    public void setCheckinTime(ZonedDateTime checkinTime) {
        this.checkinTime = checkinTime;
    }

    public ZonedDateTime getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(ZonedDateTime checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public Integer getRegularHoursWorked() {
        return regularHoursWorked;
    }

    public void setRegularHoursWorked(Integer regularHoursWorked) {
        this.regularHoursWorked = regularHoursWorked;
    }

    public Integer getOverTimeHoursWorked() {
        return overTimeHoursWorked;
    }

    public void setOverTimeHoursWorked(Integer overTimeHoursWorked) {
        this.overTimeHoursWorked = overTimeHoursWorked;
    }

    public BigDecimal getPay() {
        return pay;
    }

    public void setPay(BigDecimal pay) {
        this.pay = pay;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public String getProfileFirstName() {
        return profileFirstName;
    }

    public void setProfileFirstName(String profileFirstName) {
        this.profileFirstName = profileFirstName;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getShopShopName() {
        return shopShopName;
    }

    public void setShopShopName(String shopShopName) {
        this.shopShopName = shopShopName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EmployeeTimesheetDTO employeeTimesheetDTO = (EmployeeTimesheetDTO) o;
        if (employeeTimesheetDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), employeeTimesheetDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EmployeeTimesheetDTO{" +
            "id=" + getId() +
            ", checkinTime='" + getCheckinTime() + "'" +
            ", checkOutTime='" + getCheckOutTime() + "'" +
            ", regularHoursWorked=" + getRegularHoursWorked() +
            ", overTimeHoursWorked=" + getOverTimeHoursWorked() +
            ", pay=" + getPay() +
            ", profile=" + getProfileId() +
            ", profile='" + getProfileFirstName() + "'" +
            ", shop=" + getShopId() +
            ", shop='" + getShopShopName() + "'" +
            "}";
    }
}
