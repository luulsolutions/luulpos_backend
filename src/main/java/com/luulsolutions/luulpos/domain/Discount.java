package com.luulsolutions.luulpos.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Discount.
 */
@Entity
@Table(name = "discount")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "discount")
public class Discount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "percentage", nullable = false)
    private Float percentage;

    @NotNull
    @Column(name = "amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "active")
    private Boolean active;

    @ManyToOne
    @JsonIgnoreProperties("discounts")
    private Shop shop;

    @OneToMany(mappedBy = "discounts")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Product> products = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public Discount description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getPercentage() {
        return percentage;
    }

    public Discount percentage(Float percentage) {
        this.percentage = percentage;
        return this;
    }

    public void setPercentage(Float percentage) {
        this.percentage = percentage;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Discount amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Boolean isActive() {
        return active;
    }

    public Discount active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Shop getShop() {
        return shop;
    }

    public Discount shop(Shop shop) {
        this.shop = shop;
        return this;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public Discount products(Set<Product> products) {
        this.products = products;
        return this;
    }

    public Discount addProduct(Product product) {
        this.products.add(product);
        product.setDiscounts(this);
        return this;
    }

    public Discount removeProduct(Product product) {
        this.products.remove(product);
        product.setDiscounts(null);
        return this;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
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
        Discount discount = (Discount) o;
        if (discount.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), discount.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Discount{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", percentage=" + getPercentage() +
            ", amount=" + getAmount() +
            ", active='" + isActive() + "'" +
            "}";
    }
}
