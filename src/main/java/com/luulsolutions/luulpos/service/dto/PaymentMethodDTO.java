package com.luulsolutions.luulpos.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the PaymentMethod entity.
 */
public class PaymentMethodDTO implements Serializable {

    private Long id;

    @NotNull
    private String paymentMethod;

    private String description;

    private Boolean active;

    private Long shopId;

    private String shopShopName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
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

        PaymentMethodDTO paymentMethodDTO = (PaymentMethodDTO) o;
        if (paymentMethodDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), paymentMethodDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PaymentMethodDTO{" +
            "id=" + getId() +
            ", paymentMethod='" + getPaymentMethod() + "'" +
            ", description='" + getDescription() + "'" +
            ", active='" + isActive() + "'" +
            ", shop=" + getShopId() +
            ", shop='" + getShopShopName() + "'" +
            "}";
    }
}
