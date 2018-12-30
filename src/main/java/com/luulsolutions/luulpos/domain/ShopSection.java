package com.luulsolutions.luulpos.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A ShopSection.
 */
@Entity
@Table(name = "shop_section")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "shopsection")
public class ShopSection implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "section_name")
    private String sectionName;

    @Column(name = "description")
    private String description;

    @Column(name = "surcharge_percentage")
    private Integer surchargePercentage;

    @Column(name = "surcharge_flat_amount", precision = 10, scale = 2)
    private BigDecimal surchargeFlatAmount;

    @Column(name = "use_percentage")
    private Boolean usePercentage;

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

    public String getSectionName() {
        return sectionName;
    }

    public ShopSection sectionName(String sectionName) {
        this.sectionName = sectionName;
        return this;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getDescription() {
        return description;
    }

    public ShopSection description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSurchargePercentage() {
        return surchargePercentage;
    }

    public ShopSection surchargePercentage(Integer surchargePercentage) {
        this.surchargePercentage = surchargePercentage;
        return this;
    }

    public void setSurchargePercentage(Integer surchargePercentage) {
        this.surchargePercentage = surchargePercentage;
    }

    public BigDecimal getSurchargeFlatAmount() {
        return surchargeFlatAmount;
    }

    public ShopSection surchargeFlatAmount(BigDecimal surchargeFlatAmount) {
        this.surchargeFlatAmount = surchargeFlatAmount;
        return this;
    }

    public void setSurchargeFlatAmount(BigDecimal surchargeFlatAmount) {
        this.surchargeFlatAmount = surchargeFlatAmount;
    }

    public Boolean isUsePercentage() {
        return usePercentage;
    }

    public ShopSection usePercentage(Boolean usePercentage) {
        this.usePercentage = usePercentage;
        return this;
    }

    public void setUsePercentage(Boolean usePercentage) {
        this.usePercentage = usePercentage;
    }

    public Shop getShop() {
        return shop;
    }

    public ShopSection shop(Shop shop) {
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
        ShopSection shopSection = (ShopSection) o;
        if (shopSection.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), shopSection.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ShopSection{" +
            "id=" + getId() +
            ", sectionName='" + getSectionName() + "'" +
            ", description='" + getDescription() + "'" +
            ", surchargePercentage=" + getSurchargePercentage() +
            ", surchargeFlatAmount=" + getSurchargeFlatAmount() +
            ", usePercentage='" + isUsePercentage() + "'" +
            "}";
    }
}
