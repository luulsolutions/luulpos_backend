package com.luulsolutions.luulpos.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A PaymentMethodConfig.
 */
@Entity
@Table(name = "payment_method_config")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "paymentmethodconfig")
public class PaymentMethodConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jhi_key")
    private String key;

    @Column(name = "jhi_value")
    private String value;

    @Column(name = "note")
    private String note;

    @Column(name = "enabled")
    private Boolean enabled;

    @ManyToOne
    @JsonIgnoreProperties("")
    private PaymentMethod paymentMethod;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public PaymentMethodConfig key(String key) {
        this.key = key;
        return this;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public PaymentMethodConfig value(String value) {
        this.value = value;
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getNote() {
        return note;
    }

    public PaymentMethodConfig note(String note) {
        this.note = note;
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public PaymentMethodConfig enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public PaymentMethodConfig paymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
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
        PaymentMethodConfig paymentMethodConfig = (PaymentMethodConfig) o;
        if (paymentMethodConfig.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), paymentMethodConfig.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PaymentMethodConfig{" +
            "id=" + getId() +
            ", key='" + getKey() + "'" +
            ", value='" + getValue() + "'" +
            ", note='" + getNote() + "'" +
            ", enabled='" + isEnabled() + "'" +
            "}";
    }
}
