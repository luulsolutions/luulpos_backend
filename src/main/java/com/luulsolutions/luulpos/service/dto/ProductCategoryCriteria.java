package com.luulsolutions.luulpos.service.dto;

import java.io.Serializable;
import java.util.Objects;

import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the ProductCategory entity. This class is used in ProductCategoryResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /product-categories?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProductCategoryCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter category;

    private StringFilter description;

    private StringFilter imageFullUrl;

    private StringFilter imageThumbUrl;

    private LongFilter shopId;

    private LongFilter productsId;

    public ProductCategoryCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getCategory() {
        return category;
    }

    public void setCategory(StringFilter category) {
        this.category = category;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getImageFullUrl() {
        return imageFullUrl;
    }

    public void setImageFullUrl(StringFilter imageFullUrl) {
        this.imageFullUrl = imageFullUrl;
    }

    public StringFilter getImageThumbUrl() {
        return imageThumbUrl;
    }

    public void setImageThumbUrl(StringFilter imageThumbUrl) {
        this.imageThumbUrl = imageThumbUrl;
    }

    public LongFilter getShopId() {
        return shopId;
    }

    public void setShopId(LongFilter shopId) {
        this.shopId = shopId;
    }

    public LongFilter getProductsId() {
        return productsId;
    }

    public void setProductsId(LongFilter productsId) {
        this.productsId = productsId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProductCategoryCriteria that = (ProductCategoryCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(category, that.category) &&
            Objects.equals(description, that.description) &&
            Objects.equals(imageFullUrl, that.imageFullUrl) &&
            Objects.equals(imageThumbUrl, that.imageThumbUrl) &&
            Objects.equals(shopId, that.shopId) &&
            Objects.equals(productsId, that.productsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        category,
        description,
        imageFullUrl,
        imageThumbUrl,
        shopId,
        productsId
        );
    }

    @Override
    public String toString() {
        return "ProductCategoryCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (category != null ? "category=" + category + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (imageFullUrl != null ? "imageFullUrl=" + imageFullUrl + ", " : "") +
                (imageThumbUrl != null ? "imageThumbUrl=" + imageThumbUrl + ", " : "") +
                (shopId != null ? "shopId=" + shopId + ", " : "") +
                (productsId != null ? "productsId=" + productsId + ", " : "") +
            "}";
    }

}
