package com.luulsolutions.luulpos.service.dto;

import java.io.Serializable;
import java.util.Objects;

import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the ProductType entity. This class is used in ProductTypeResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /product-types?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProductTypeCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter productType;

    private StringFilter productTypeDescription;

    private LongFilter shopId;

    public ProductTypeCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getProductType() {
        return productType;
    }

    public void setProductType(StringFilter productType) {
        this.productType = productType;
    }

    public StringFilter getProductTypeDescription() {
        return productTypeDescription;
    }

    public void setProductTypeDescription(StringFilter productTypeDescription) {
        this.productTypeDescription = productTypeDescription;
    }

    public LongFilter getShopId() {
        return shopId;
    }

    public void setShopId(LongFilter shopId) {
        this.shopId = shopId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProductTypeCriteria that = (ProductTypeCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(productType, that.productType) &&
            Objects.equals(productTypeDescription, that.productTypeDescription) &&
            Objects.equals(shopId, that.shopId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        productType,
        productTypeDescription,
        shopId
        );
    }

    @Override
    public String toString() {
        return "ProductTypeCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (productType != null ? "productType=" + productType + ", " : "") +
                (productTypeDescription != null ? "productTypeDescription=" + productTypeDescription + ", " : "") +
                (shopId != null ? "shopId=" + shopId + ", " : "") +
            "}";
    }

}
