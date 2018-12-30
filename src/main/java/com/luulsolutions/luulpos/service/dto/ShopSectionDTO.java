package com.luulsolutions.luulpos.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the ShopSection entity.
 */
public class ShopSectionDTO implements Serializable {

    private Long id;

    private String sectionName;

    private String description;

    private Integer surchargePercentage;

    private BigDecimal surchargeFlatAmount;

    private Boolean usePercentage;

    private Long shopId;

    private String shopShopName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSurchargePercentage() {
        return surchargePercentage;
    }

    public void setSurchargePercentage(Integer surchargePercentage) {
        this.surchargePercentage = surchargePercentage;
    }

    public BigDecimal getSurchargeFlatAmount() {
        return surchargeFlatAmount;
    }

    public void setSurchargeFlatAmount(BigDecimal surchargeFlatAmount) {
        this.surchargeFlatAmount = surchargeFlatAmount;
    }

    public Boolean isUsePercentage() {
        return usePercentage;
    }

    public void setUsePercentage(Boolean usePercentage) {
        this.usePercentage = usePercentage;
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

        ShopSectionDTO shopSectionDTO = (ShopSectionDTO) o;
        if (shopSectionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), shopSectionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ShopSectionDTO{" +
            "id=" + getId() +
            ", sectionName='" + getSectionName() + "'" +
            ", description='" + getDescription() + "'" +
            ", surchargePercentage=" + getSurchargePercentage() +
            ", surchargeFlatAmount=" + getSurchargeFlatAmount() +
            ", usePercentage='" + isUsePercentage() + "'" +
            ", shop=" + getShopId() +
            ", shop='" + getShopShopName() + "'" +
            "}";
    }
}
