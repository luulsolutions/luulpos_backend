package com.luulsolutions.luulpos.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the OrdersLine entity.
 */
public class OrdersLineDTO implements Serializable {

    private Long id;

    @NotNull
    private String ordersLineName;

    @NotNull
    private String ordersLineValue;

    private Float ordersLinePrice;

    private String ordersLineDescription;

    @Lob
    private byte[] thumbnailPhoto;
    private String thumbnailPhotoContentType;

    @Lob
    private byte[] fullPhoto;
    private String fullPhotoContentType;

    private String fullPhotoUrl;

    private String thumbnailPhotoUrl;

    private Long ordersId;

    private Long productId;

    private String productProductName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrdersLineName() {
        return ordersLineName;
    }

    public void setOrdersLineName(String ordersLineName) {
        this.ordersLineName = ordersLineName;
    }

    public String getOrdersLineValue() {
        return ordersLineValue;
    }

    public void setOrdersLineValue(String ordersLineValue) {
        this.ordersLineValue = ordersLineValue;
    }

    public Float getOrdersLinePrice() {
        return ordersLinePrice;
    }

    public void setOrdersLinePrice(Float ordersLinePrice) {
        this.ordersLinePrice = ordersLinePrice;
    }

    public String getOrdersLineDescription() {
        return ordersLineDescription;
    }

    public void setOrdersLineDescription(String ordersLineDescription) {
        this.ordersLineDescription = ordersLineDescription;
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

    public String getThumbnailPhotoUrl() {
        return thumbnailPhotoUrl;
    }

    public void setThumbnailPhotoUrl(String thumbnailPhotoUrl) {
        this.thumbnailPhotoUrl = thumbnailPhotoUrl;
    }

    public Long getOrdersId() {
        return ordersId;
    }

    public void setOrdersId(Long ordersId) {
        this.ordersId = ordersId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductProductName() {
        return productProductName;
    }

    public void setProductProductName(String productProductName) {
        this.productProductName = productProductName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OrdersLineDTO ordersLineDTO = (OrdersLineDTO) o;
        if (ordersLineDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ordersLineDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OrdersLineDTO{" +
            "id=" + getId() +
            ", ordersLineName='" + getOrdersLineName() + "'" +
            ", ordersLineValue='" + getOrdersLineValue() + "'" +
            ", ordersLinePrice=" + getOrdersLinePrice() +
            ", ordersLineDescription='" + getOrdersLineDescription() + "'" +
            ", thumbnailPhoto='" + getThumbnailPhoto() + "'" +
            ", fullPhoto='" + getFullPhoto() + "'" +
            ", fullPhotoUrl='" + getFullPhotoUrl() + "'" +
            ", thumbnailPhotoUrl='" + getThumbnailPhotoUrl() + "'" +
            ", orders=" + getOrdersId() +
            ", product=" + getProductId() +
            ", product='" + getProductProductName() + "'" +
            "}";
    }
}
