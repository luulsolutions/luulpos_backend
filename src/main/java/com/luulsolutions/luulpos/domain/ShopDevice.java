package com.luulsolutions.luulpos.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A ShopDevice.
 */
@Entity
@Table(name = "shop_device")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "shopdevice")
public class ShopDevice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "device_name")
    private String deviceName;

    @Column(name = "device_model")
    private String deviceModel;

    @Column(name = "registered_date")
    private ZonedDateTime registeredDate;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Shop shop;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public ShopDevice deviceName(String deviceName) {
        this.deviceName = deviceName;
        return this;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public ShopDevice deviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
        return this;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public ZonedDateTime getRegisteredDate() {
        return registeredDate;
    }

    public ShopDevice registeredDate(ZonedDateTime registeredDate) {
        this.registeredDate = registeredDate;
        return this;
    }

    public void setRegisteredDate(ZonedDateTime registeredDate) {
        this.registeredDate = registeredDate;
    }

    public Shop getShop() {
        return shop;
    }

    public ShopDevice shop(Shop shop) {
        this.shop = shop;
        return this;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ShopDevice shopDevice = (ShopDevice) o;
        if (shopDevice.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), shopDevice.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ShopDevice{" +
            "id=" + getId() +
            ", deviceName='" + getDeviceName() + "'" +
            ", deviceModel='" + getDeviceModel() + "'" +
            ", registeredDate='" + getRegisteredDate() + "'" +
            "}";
    }
}
