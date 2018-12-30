package com.luulsolutions.luulpos.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the PaymentMethodConfig entity.
 */
public class PaymentMethodConfigDTO implements Serializable {

    private Long id;

    private String key;

    private String value;

    private String note;

    private Boolean enabled;

    private Long paymentMethodId;

    private String paymentMethodPaymentMethod;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Long getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(Long paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public String getPaymentMethodPaymentMethod() {
        return paymentMethodPaymentMethod;
    }

    public void setPaymentMethodPaymentMethod(String paymentMethodPaymentMethod) {
        this.paymentMethodPaymentMethod = paymentMethodPaymentMethod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PaymentMethodConfigDTO paymentMethodConfigDTO = (PaymentMethodConfigDTO) o;
        if (paymentMethodConfigDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), paymentMethodConfigDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PaymentMethodConfigDTO{" +
            "id=" + getId() +
            ", key='" + getKey() + "'" +
            ", value='" + getValue() + "'" +
            ", note='" + getNote() + "'" +
            ", enabled='" + isEnabled() + "'" +
            ", paymentMethod=" + getPaymentMethodId() +
            ", paymentMethod='" + getPaymentMethodPaymentMethod() + "'" +
            "}";
    }
}
