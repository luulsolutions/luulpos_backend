package com.luulsolutions.luulpos.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the OrdersLineExtra entity.
 */
public class OrdersLineExtraDTO implements Serializable {

    private Long id;

    @NotNull
    private String ordersLineExtraName;

    @NotNull
    private String ordersLineExtraValue;

    private Float ordersLineExtraPrice;

    private String ordersOptionDescription;

    @Lob
    private byte[] fullPhoto;
    private String fullPhotoContentType;

    private String fullPhotoUrl;

    @Lob
    private byte[] thumbnailPhoto;
    private String thumbnailPhotoContentType;

    private String thumbnailPhotoUrl;

    private Long ordersLineVariantId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrdersLineExtraName() {
        return ordersLineExtraName;
    }

    public void setOrdersLineExtraName(String ordersLineExtraName) {
        this.ordersLineExtraName = ordersLineExtraName;
    }

    public String getOrdersLineExtraValue() {
        return ordersLineExtraValue;
    }

    public void setOrdersLineExtraValue(String ordersLineExtraValue) {
        this.ordersLineExtraValue = ordersLineExtraValue;
    }

    public Float getOrdersLineExtraPrice() {
        return ordersLineExtraPrice;
    }

    public void setOrdersLineExtraPrice(Float ordersLineExtraPrice) {
        this.ordersLineExtraPrice = ordersLineExtraPrice;
    }

    public String getOrdersOptionDescription() {
        return ordersOptionDescription;
    }

    public void setOrdersOptionDescription(String ordersOptionDescription) {
        this.ordersOptionDescription = ordersOptionDescription;
    }

    public byte[] getFullPhoto() {
        return fullPhoto;
    }

    public void setFullPhoto(byte[] fullPhoto) {
        this.fullPhoto = fullPhoto;
    }

    public String getFullPhotoContentType() {
        return fullPhotoContentType;
    }

    public void setFullPhotoContentType(String fullPhotoContentType) {
        this.fullPhotoContentType = fullPhotoContentType;
    }

    public String getFullPhotoUrl() {
        return fullPhotoUrl;
    }

    public void setFullPhotoUrl(String fullPhotoUrl) {
        this.fullPhotoUrl = fullPhotoUrl;
    }

    public byte[] getThumbnailPhoto() {
        return thumbnailPhoto;
    }

    public void setThumbnailPhoto(byte[] thumbnailPhoto) {
        this.thumbnailPhoto = thumbnailPhoto;
    }

    public String getThumbnailPhotoContentType() {
        return thumbnailPhotoContentType;
    }

    public void setThumbnailPhotoContentType(String thumbnailPhotoContentType) {
        this.thumbnailPhotoContentType = thumbnailPhotoContentType;
    }

    public String getThumbnailPhotoUrl() {
        return thumbnailPhotoUrl;
    }

    public void setThumbnailPhotoUrl(String thumbnailPhotoUrl) {
        this.thumbnailPhotoUrl = thumbnailPhotoUrl;
    }

    public Long getOrdersLineVariantId() {
        return ordersLineVariantId;
    }

    public void setOrdersLineVariantId(Long ordersLineVariantId) {
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

        OrdersLineExtraDTO ordersLineExtraDTO = (OrdersLineExtraDTO) o;
        if (ordersLineExtraDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ordersLineExtraDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OrdersLineExtraDTO{" +
            "id=" + getId() +
            ", ordersLineExtraName='" + getOrdersLineExtraName() + "'" +
            ", ordersLineExtraValue='" + getOrdersLineExtraValue() + "'" +
            ", ordersLineExtraPrice=" + getOrdersLineExtraPrice() +
            ", ordersOptionDescription='" + getOrdersOptionDescription() + "'" +
            ", fullPhoto='" + getFullPhoto() + "'" +
            ", fullPhotoUrl='" + getFullPhotoUrl() + "'" +
            ", thumbnailPhoto='" + getThumbnailPhoto() + "'" +
            ", thumbnailPhotoUrl='" + getThumbnailPhotoUrl() + "'" +
            ", ordersLineVariant=" + getOrdersLineVariantId() +
            "}";
    }
}
