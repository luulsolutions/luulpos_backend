package com.luulsolutions.luulpos.service.dto;

import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the Product entity.
 */
public class ProductDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 30)
    private String productName;

    @Size(max = 99)
    private String productDescription;

    private BigDecimal price;

    @NotNull
    private Integer quantity;

    @Lob
    private byte[] productImageFull;
    private String productImageFullContentType;

    private String productImageFullUrl;

    @Lob
    private byte[] productImageThumb;
    private String productImageThumbContentType;

    private String productImageThumbUrl;

    private ZonedDateTime dateCreated;

    private String barcode;

    private String serialCode;

    private Integer priorityPosition;

    private Boolean active;

    private Boolean isVariantProduct;

    private Long productTypesId;

    private String productTypesProductType;

    private Long shopId;

    private String shopShopName;

    private Long discountsId;

    private String discountsDescription;

    private Long taxesId;

    private String taxesDescription;

    private Long categoryId;

    private String categoryCategory;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public byte[] getProductImageFull() {
        return productImageFull;
    }

    public void setProductImageFull(byte[] productImageFull) {
        this.productImageFull = productImageFull;
    }

    public String getProductImageFullContentType() {
        return productImageFullContentType;
    }

    public void setProductImageFullContentType(String productImageFullContentType) {
        this.productImageFullContentType = productImageFullContentType;
    }

    public String getProductImageFullUrl() {
        return productImageFullUrl;
    }

    public void setProductImageFullUrl(String productImageFullUrl) {
        this.productImageFullUrl = productImageFullUrl;
    }

    public byte[] getProductImageThumb() {
        return productImageThumb;
    }

    public void setProductImageThumb(byte[] productImageThumb) {
        this.productImageThumb = productImageThumb;
    }

    public String getProductImageThumbContentType() {
        return productImageThumbContentType;
    }

    public void setProductImageThumbContentType(String productImageThumbContentType) {
        this.productImageThumbContentType = productImageThumbContentType;
    }

    public String getProductImageThumbUrl() {
        return productImageThumbUrl;
    }

    public void setProductImageThumbUrl(String productImageThumbUrl) {
        this.productImageThumbUrl = productImageThumbUrl;
    }

    public ZonedDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(ZonedDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getSerialCode() {
        return serialCode;
    }

    public void setSerialCode(String serialCode) {
        this.serialCode = serialCode;
    }

    public Integer getPriorityPosition() {
        return priorityPosition;
    }

    public void setPriorityPosition(Integer priorityPosition) {
        this.priorityPosition = priorityPosition;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean isIsVariantProduct() {
        return isVariantProduct;
    }

    public void setIsVariantProduct(Boolean isVariantProduct) {
        this.isVariantProduct = isVariantProduct;
    }

    public Long getProductTypesId() {
        return productTypesId;
    }

    public void setProductTypesId(Long productTypeId) {
        this.productTypesId = productTypeId;
    }

    public String getProductTypesProductType() {
        return productTypesProductType;
    }

    public void setProductTypesProductType(String productTypeProductType) {
        this.productTypesProductType = productTypeProductType;
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

    public Long getDiscountsId() {
        return discountsId;
    }

    public void setDiscountsId(Long discountId) {
        this.discountsId = discountId;
    }

    public String getDiscountsDescription() {
        return discountsDescription;
    }

    public void setDiscountsDescription(String discountDescription) {
        this.discountsDescription = discountDescription;
    }

    public Long getTaxesId() {
        return taxesId;
    }

    public void setTaxesId(Long taxId) {
        this.taxesId = taxId;
    }

    public String getTaxesDescription() {
        return taxesDescription;
    }

    public void setTaxesDescription(String taxDescription) {
        this.taxesDescription = taxDescription;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long productCategoryId) {
        this.categoryId = productCategoryId;
    }

    public String getCategoryCategory() {
        return categoryCategory;
    }

    public void setCategoryCategory(String productCategoryCategory) {
        this.categoryCategory = productCategoryCategory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProductDTO productDTO = (ProductDTO) o;
        if (productDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), productDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProductDTO{" +
            "id=" + getId() +
            ", productName='" + getProductName() + "'" +
            ", productDescription='" + getProductDescription() + "'" +
            ", price=" + getPrice() +
            ", quantity=" + getQuantity() +
            ", productImageFull='" + getProductImageFull() + "'" +
            ", productImageFullUrl='" + getProductImageFullUrl() + "'" +
            ", productImageThumb='" + getProductImageThumb() + "'" +
            ", productImageThumbUrl='" + getProductImageThumbUrl() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", barcode='" + getBarcode() + "'" +
            ", serialCode='" + getSerialCode() + "'" +
            ", priorityPosition=" + getPriorityPosition() +
            ", active='" + isActive() + "'" +
            ", isVariantProduct='" + isIsVariantProduct() + "'" +
            ", productTypes=" + getProductTypesId() +
            ", productTypes='" + getProductTypesProductType() + "'" +
            ", shop=" + getShopId() +
            ", shop='" + getShopShopName() + "'" +
            ", discounts=" + getDiscountsId() +
            ", discounts='" + getDiscountsDescription() + "'" +
            ", taxes=" + getTaxesId() +
            ", taxes='" + getTaxesDescription() + "'" +
            ", category=" + getCategoryId() +
            ", category='" + getCategoryCategory() + "'" +
            "}";
    }
}
