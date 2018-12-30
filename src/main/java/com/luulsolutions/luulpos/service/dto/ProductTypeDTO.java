package com.luulsolutions.luulpos.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the ProductType entity.
 */
public class ProductTypeDTO implements Serializable {

    private Long id;

    @NotNull
    private String productType;

    private String productTypeDescription;

    private Long shopId;

    private String shopShopName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductTypeDescription() {
        return productTypeDescription;
    }

    public void setProductTypeDescription(String productTypeDescription) {
        this.productTypeDescription = productTypeDescription;
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

        ProductTypeDTO productTypeDTO = (ProductTypeDTO) o;
        if (productTypeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), productTypeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProductTypeDTO{" +
            "id=" + getId() +
            ", productType='" + getProductType() + "'" +
            ", productTypeDescription='" + getProductTypeDescription() + "'" +
            ", shop=" + getShopId() +
            ", shop='" + getShopShopName() + "'" +
            "}";
    }
}
