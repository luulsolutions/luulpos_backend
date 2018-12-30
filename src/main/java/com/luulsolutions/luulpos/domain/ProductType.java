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
 * A ProductType.
 */
@Entity
@Table(name = "product_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "producttype")
public class ProductType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "product_type", nullable = false)
    private String productType;

    @Column(name = "product_type_description")
    private String productTypeDescription;

    @ManyToOne
    @JsonIgnoreProperties("productTypes")
    private Shop shop;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductType() {
        return productType;
    }

    public ProductType productType(String productType) {
        this.productType = productType;
        return this;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductTypeDescription() {
        return productTypeDescription;
    }

    public ProductType productTypeDescription(String productTypeDescription) {
        this.productTypeDescription = productTypeDescription;
        return this;
    }

    public void setProductTypeDescription(String productTypeDescription) {
        this.productTypeDescription = productTypeDescription;
    }

    public Shop getShop() {
        return shop;
    }

    public ProductType shop(Shop shop) {
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
        ProductType productType = (ProductType) o;
        if (productType.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), productType.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProductType{" +
            "id=" + getId() +
            ", productType='" + getProductType() + "'" +
            ", productTypeDescription='" + getProductTypeDescription() + "'" +
            "}";
    }
}
