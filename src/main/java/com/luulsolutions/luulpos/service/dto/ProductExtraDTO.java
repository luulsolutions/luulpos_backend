package com.luulsolutions.luulpos.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the ProductExtra entity.
 */
public class ProductExtraDTO implements Serializable {

    private Long id;

    private String extraName;

    private String description;

    private Float extraValue;

    @Lob
    private byte[] fullPhoto;
    private String fullPhotoContentType;

    private String fullPhotoUrl;

    @Lob
    private byte[] thumbnailPhoto;
    private String thumbnailPhotoContentType;

    private String thumbnailPhotoUrl;

    private Long productId;

    private String productProductName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExtraName() {
        return extraName;
    }

    public void setExtraName(String extraName) {
        this.extraName = extraName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getExtraValue() {
        return extraValue;
    }

    public void setExtraValue(Float extraValue) {
        this.extraValue = extraValue;
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

        ProductExtraDTO productExtraDTO = (ProductExtraDTO) o;
        if (productExtraDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), productExtraDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProductExtraDTO{" +
            "id=" + getId() +
            ", extraName='" + getExtraName() + "'" +
            ", description='" + getDescription() + "'" +
            ", extraValue=" + getExtraValue() +
            ", fullPhoto='" + getFullPhoto() + "'" +
            ", fullPhotoUrl='" + getFullPhotoUrl() + "'" +
            ", thumbnailPhoto='" + getThumbnailPhoto() + "'" +
            ", thumbnailPhotoUrl='" + getThumbnailPhotoUrl() + "'" +
            ", product=" + getProductId() +
            ", product='" + getProductProductName() + "'" +
            "}";
    }
}
