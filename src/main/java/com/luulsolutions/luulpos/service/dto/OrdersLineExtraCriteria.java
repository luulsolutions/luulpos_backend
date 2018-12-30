package com.luulsolutions.luulpos.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the OrdersLineExtra entity. This class is used in OrdersLineExtraResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /orders-line-extras?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class OrdersLineExtraCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter ordersLineExtraName;

    private StringFilter ordersLineExtraValue;

    private FloatFilter ordersLineExtraPrice;

    private StringFilter ordersOptionDescription;

    private StringFilter fullPhotoUrl;

    private StringFilter thumbnailPhotoUrl;

    private LongFilter ordersLineVariantId;

    public OrdersLineExtraCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getOrdersLineExtraName() {
        return ordersLineExtraName;
    }

    public void setOrdersLineExtraName(StringFilter ordersLineExtraName) {
        this.ordersLineExtraName = ordersLineExtraName;
    }

    public StringFilter getOrdersLineExtraValue() {
        return ordersLineExtraValue;
    }

    public void setOrdersLineExtraValue(StringFilter ordersLineExtraValue) {
        this.ordersLineExtraValue = ordersLineExtraValue;
    }

    public FloatFilter getOrdersLineExtraPrice() {
        return ordersLineExtraPrice;
    }

    public void setOrdersLineExtraPrice(FloatFilter ordersLineExtraPrice) {
        this.ordersLineExtraPrice = ordersLineExtraPrice;
    }

    public StringFilter getOrdersOptionDescription() {
        return ordersOptionDescription;
    }

    public void setOrdersOptionDescription(StringFilter ordersOptionDescription) {
        this.ordersOptionDescription = ordersOptionDescription;
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

    public LongFilter getOrdersLineVariantId() {
        return ordersLineVariantId;
    }

    public void setOrdersLineVariantId(LongFilter ordersLineVariantId) {
        this.ordersLineVariantId = ordersLineVariantId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OrdersLineExtraCriteria that = (OrdersLineExtraCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(ordersLineExtraName, that.ordersLineExtraName) &&
            Objects.equals(ordersLineExtraValue, that.ordersLineExtraValue) &&
            Objects.equals(ordersLineExtraPrice, that.ordersLineExtraPrice) &&
            Objects.equals(ordersOptionDescription, that.ordersOptionDescription) &&
            Objects.equals(fullPhotoUrl, that.fullPhotoUrl) &&
            Objects.equals(thumbnailPhotoUrl, that.thumbnailPhotoUrl) &&
            Objects.equals(ordersLineVariantId, that.ordersLineVariantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        ordersLineExtraName,
        ordersLineExtraValue,
        ordersLineExtraPrice,
        ordersOptionDescription,
        fullPhotoUrl,
        thumbnailPhotoUrl,
        ordersLineVariantId
        );
    }

    @Override
    public String toString() {
        return "OrdersLineExtraCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (ordersLineExtraName != null ? "ordersLineExtraName=" + ordersLineExtraName + ", " : "") +
                (ordersLineExtraValue != null ? "ordersLineExtraValue=" + ordersLineExtraValue + ", " : "") +
                (ordersLineExtraPrice != null ? "ordersLineExtraPrice=" + ordersLineExtraPrice + ", " : "") +
                (ordersOptionDescription != null ? "ordersOptionDescription=" + ordersOptionDescription + ", " : "") +
                (fullPhotoUrl != null ? "fullPhotoUrl=" + fullPhotoUrl + ", " : "") +
                (thumbnailPhotoUrl != null ? "thumbnailPhotoUrl=" + thumbnailPhotoUrl + ", " : "") +
                (ordersLineVariantId != null ? "ordersLineVariantId=" + ordersLineVariantId + ", " : "") +
            "}";
    }

}
