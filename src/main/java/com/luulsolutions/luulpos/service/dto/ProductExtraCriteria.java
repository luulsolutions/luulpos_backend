package com.luulsolutions.luulpos.service.dto;

import java.io.Serializable;
import java.util.Objects;

import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the ProductExtra entity. This class is used in ProductExtraResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /product-extras?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProductExtraCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter extraName;

    private StringFilter description;

    private FloatFilter extraValue;

    private StringFilter fullPhotoUrl;

    private StringFilter thumbnailPhotoUrl;

    private LongFilter productId;

    public ProductExtraCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getExtraName() {
        return extraName;
    }

    public void setExtraName(StringFilter extraName) {
        this.extraName = extraName;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public FloatFilter getExtraValue() {
        return extraValue;
    }

    public void setExtraValue(FloatFilter extraValue) {
        this.extraValue = extraValue;
    }

    public StringFilter getFullPhotoUrl() {
        return fullPhotoUrl;
    }

    public void setFullPhotoUrl(StringFilter fullPhotoUrl) {
        this.fullPhotoUrl = fullPhotoUrl;
    }

    public StringFilter getThumbnailPhotoUrl() {
        return thumbnailPhotoUrl;
    }

    public void setThumbnailPhotoUrl(StringFilter thumbnailPhotoUrl) {
        this.thumbnailPhotoUrl = thumbnailPhotoUrl;
    }

    public LongFilter getProductId() {
        return productId;
    }

    public void setProductId(LongFilter productId) {
        this.productId = productId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProductExtraCriteria that = (ProductExtraCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(extraName, that.extraName) &&
            Objects.equals(description, that.description) &&
            Objects.equals(extraValue, that.extraValue) &&
            Objects.equals(fullPhotoUrl, that.fullPhotoUrl) &&
            Objects.equals(thumbnailPhotoUrl, that.thumbnailPhotoUrl) &&
            Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        extraName,
        description,
        extraValue,
        fullPhotoUrl,
        thumbnailPhotoUrl,
        productId
        );
    }

    @Override
    public String toString() {
        return "ProductExtraCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (extraName != null ? "extraName=" + extraName + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (extraValue != null ? "extraValue=" + extraValue + ", " : "") +
                (fullPhotoUrl != null ? "fullPhotoUrl=" + fullPhotoUrl + ", " : "") +
                (thumbnailPhotoUrl != null ? "thumbnailPhotoUrl=" + thumbnailPhotoUrl + ", " : "") +
                (productId != null ? "productId=" + productId + ", " : "") +
            "}";
    }

}
