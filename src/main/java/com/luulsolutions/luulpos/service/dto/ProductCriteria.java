package com.luulsolutions.luulpos.service.dto;

import java.io.Serializable;
import java.util.Objects;

import io.github.jhipster.service.filter.BigDecimalFilter;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the Product entity. This class is used in ProductResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /products?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProductCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter productName;

    private StringFilter productDescription;

    private BigDecimalFilter price;

    private IntegerFilter quantity;

    private StringFilter productImageFullUrl;

    private StringFilter productImageThumbUrl;

    private ZonedDateTimeFilter dateCreated;

    private StringFilter barcode;

    private StringFilter serialCode;

    private IntegerFilter priorityPosition;

    private BooleanFilter active;

    private BooleanFilter isVariantProduct;

    private LongFilter variantsId;

    private LongFilter extrasId;

    private LongFilter productTypesId;

    private LongFilter shopId;

    private LongFilter discountsId;

    private LongFilter taxesId;

    private LongFilter categoryId;

    public ProductCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getProductName() {
        return productName;
    }

    public void setProductName(StringFilter productName) {
        this.productName = productName;
    }

    public StringFilter getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(StringFilter productDescription) {
        this.productDescription = productDescription;
    }

    public BigDecimalFilter getPrice() {
        return price;
    }

    public void setPrice(BigDecimalFilter price) {
        this.price = price;
    }

    public IntegerFilter getQuantity() {
        return quantity;
    }

    public void setQuantity(IntegerFilter quantity) {
        this.quantity = quantity;
    }

    public StringFilter getProductImageFullUrl() {
        return productImageFullUrl;
    }

    public void setProductImageFullUrl(StringFilter productImageFullUrl) {
        this.productImageFullUrl = productImageFullUrl;
    }

    public StringFilter getProductImageThumbUrl() {
        return productImageThumbUrl;
    }

    public void setProductImageThumbUrl(StringFilter productImageThumbUrl) {
        this.productImageThumbUrl = productImageThumbUrl;
    }

    public ZonedDateTimeFilter getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(ZonedDateTimeFilter dateCreated) {
        this.dateCreated = dateCreated;
    }

    public StringFilter getBarcode() {
        return barcode;
    }

    public void setBarcode(StringFilter barcode) {
        this.barcode = barcode;
    }

    public StringFilter getSerialCode() {
        return serialCode;
    }

    public void setSerialCode(StringFilter serialCode) {
        this.serialCode = serialCode;
    }

    public IntegerFilter getPriorityPosition() {
        return priorityPosition;
    }

    public void setPriorityPosition(IntegerFilter priorityPosition) {
        this.priorityPosition = priorityPosition;
    }

    public BooleanFilter getActive() {
        return active;
    }

    public void setActive(BooleanFilter active) {
        this.active = active;
    }

    public BooleanFilter getIsVariantProduct() {
        return isVariantProduct;
    }

    public void setIsVariantProduct(BooleanFilter isVariantProduct) {
        this.isVariantProduct = isVariantProduct;
    }

    public LongFilter getVariantsId() {
        return variantsId;
    }

    public void setVariantsId(LongFilter variantsId) {
        this.variantsId = variantsId;
    }

    public LongFilter getExtrasId() {
        return extrasId;
    }

    public void setExtrasId(LongFilter extrasId) {
        this.extrasId = extrasId;
    }

    public LongFilter getProductTypesId() {
        return productTypesId;
    }

    public void setProductTypesId(LongFilter productTypesId) {
        this.productTypesId = productTypesId;
    }

    public LongFilter getShopId() {
        return shopId;
    }

    public void setShopId(LongFilter shopId) {
        this.shopId = shopId;
    }

    public LongFilter getDiscountsId() {
        return discountsId;
    }

    public void setDiscountsId(LongFilter discountsId) {
        this.discountsId = discountsId;
    }

    public LongFilter getTaxesId() {
        return taxesId;
    }

    public void setTaxesId(LongFilter taxesId) {
        this.taxesId = taxesId;
    }

    public LongFilter getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(LongFilter categoryId) {
        this.categoryId = categoryId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProductCriteria that = (ProductCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(productName, that.productName) &&
            Objects.equals(productDescription, that.productDescription) &&
            Objects.equals(price, that.price) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(productImageFullUrl, that.productImageFullUrl) &&
            Objects.equals(productImageThumbUrl, that.productImageThumbUrl) &&
            Objects.equals(dateCreated, that.dateCreated) &&
            Objects.equals(barcode, that.barcode) &&
            Objects.equals(serialCode, that.serialCode) &&
            Objects.equals(priorityPosition, that.priorityPosition) &&
            Objects.equals(active, that.active) &&
            Objects.equals(isVariantProduct, that.isVariantProduct) &&
            Objects.equals(variantsId, that.variantsId) &&
            Objects.equals(extrasId, that.extrasId) &&
            Objects.equals(productTypesId, that.productTypesId) &&
            Objects.equals(shopId, that.shopId) &&
            Objects.equals(discountsId, that.discountsId) &&
            Objects.equals(taxesId, that.taxesId) &&
            Objects.equals(categoryId, that.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        productName,
        productDescription,
        price,
        quantity,
        productImageFullUrl,
        productImageThumbUrl,
        dateCreated,
        barcode,
        serialCode,
        priorityPosition,
        active,
        isVariantProduct,
        variantsId,
        extrasId,
        productTypesId,
        shopId,
        discountsId,
        taxesId,
        categoryId
        );
    }

    @Override
    public String toString() {
        return "ProductCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (productName != null ? "productName=" + productName + ", " : "") +
                (productDescription != null ? "productDescription=" + productDescription + ", " : "") +
                (price != null ? "price=" + price + ", " : "") +
                (quantity != null ? "quantity=" + quantity + ", " : "") +
                (productImageFullUrl != null ? "productImageFullUrl=" + productImageFullUrl + ", " : "") +
                (productImageThumbUrl != null ? "productImageThumbUrl=" + productImageThumbUrl + ", " : "") +
                (dateCreated != null ? "dateCreated=" + dateCreated + ", " : "") +
                (barcode != null ? "barcode=" + barcode + ", " : "") +
                (serialCode != null ? "serialCode=" + serialCode + ", " : "") +
                (priorityPosition != null ? "priorityPosition=" + priorityPosition + ", " : "") +
                (active != null ? "active=" + active + ", " : "") +
                (isVariantProduct != null ? "isVariantProduct=" + isVariantProduct + ", " : "") +
                (variantsId != null ? "variantsId=" + variantsId + ", " : "") +
                (extrasId != null ? "extrasId=" + extrasId + ", " : "") +
                (productTypesId != null ? "productTypesId=" + productTypesId + ", " : "") +
                (shopId != null ? "shopId=" + shopId + ", " : "") +
                (discountsId != null ? "discountsId=" + discountsId + ", " : "") +
                (taxesId != null ? "taxesId=" + taxesId + ", " : "") +
                (categoryId != null ? "categoryId=" + categoryId + ", " : "") +
            "}";
    }

}
