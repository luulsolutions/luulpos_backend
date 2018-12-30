package com.luulsolutions.luulpos.service.dto;

import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the ShopDevice entity.
 */
public class ShopDeviceDTO implements Serializable {

    private Long id;

    private String deviceName;

    private String deviceModel;

    private ZonedDateTime registeredDate;

    private Long shopId;

    private String shopShopName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public ZonedDateTime getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(ZonedDateTime registeredDate) {
        this.registeredDate = registeredDate;
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

        ShopDeviceDTO shopDeviceDTO = (ShopDeviceDTO) o;
        if (shopDeviceDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), shopDeviceDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ShopDeviceDTO{" +
            "id=" + getId() +
            ", deviceName='" + getDeviceName() + "'" +
            ", deviceModel='" + getDeviceModel() + "'" +
            ", registeredDate='" + getRegisteredDate() + "'" +
            ", shop=" + getShopId() +
            ", shop='" + getShopShopName() + "'" +
            "}";
    }
}
