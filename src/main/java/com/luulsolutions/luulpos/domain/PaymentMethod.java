package com.luulsolutions.luulpos.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A PaymentMethod.
 */
@Entity
@Table(name = "payment_method")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "paymentmethod")
public class PaymentMethod implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "payment_method", nullable = false)
    private String paymentMethod;

    @Column(name = "description")
    private String description;

    @Column(name = "active")
    private Boolean active;

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

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public PaymentMethod paymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getDescription() {
        return description;
    }

    public PaymentMethod description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isActive() {
        return active;
    }

    public PaymentMethod active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Shop getShop() {
        return shop;
    }

    public PaymentMethod shop(Shop shop) {
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
        PaymentMethod paymentMethod = (PaymentMethod) o;
        if (paymentMethod.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), paymentMethod.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PaymentMethod{" +
            "id=" + getId() +
            ", paymentMethod='" + getPaymentMethod() + "'" +
            ", description='" + getDescription() + "'" +
            ", active='" + isActive() + "'" +
            "}";
    }
}
