package com.luulsolutions.luulpos.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the OrdersLineVariant entity.
 */
public class OrdersLineVariantDTO implements Serializable {

    private Long id;

    private String variantName;

    private String variantValue;

    private String description;

    private Float percentage;

    @Lob
    private byte[] fullPhoto;
    private String fullPhotoContentType;

    private String fullPhotoUrl;

    @Lob
    private byte[] thumbnailPhoto;
    private String thumbnailPhotoContentType;

    private String thumbnailPhotoUrl;

    @NotNull
    private BigDecimal price;

    private Long ordersLineId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVariantName() {
        return variantName;
    }

    public void setVariantName(String variantName) {
        this.variantName = variantName;
    }

    public String getVariantValue() {
        return variantValue;
    }

    public void setVariantValue(String variantValue) {
        this.variantValue = variantValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getPercentage() {
        return percentage;
    }

    public void setPercentage(Float percentage) {
        this.percentage = percentage;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getOrdersLineId() {
        return ordersLineId;
    }

    public void setOrdersLineId(Long ordersLineId) {
        this.ordersLineId = ordersLineId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OrdersLineVariantDTO ordersLineVariantDTO = (OrdersLineVariantDTO) o;
        if (ordersLineVariantDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ordersLineVariantDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OrdersLineVariantDTO{" +
            "id=" + getId() +
            ", variantName='" + getVariantName() + "'" +
            ", variantValue='" + getVariantValue() + "'" +
            ", description='" + getDescription() + "'" +
            ", percentage=" + getPercentage() +
            ", fullPhoto='" + getFullPhoto() + "'" +
            ", fullPhotoUrl='" + getFullPhotoUrl() + "'" +
            ", thumbnailPhoto='" + getThumbnailPhoto() + "'" +
            ", thumbnailPhotoUrl='" + getThumbnailPhotoUrl() + "'" +
            ", price=" + getPrice() +
            ", ordersLine=" + getOrdersLineId() +
            "}";
    }
}
