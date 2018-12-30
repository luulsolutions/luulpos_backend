package com.luulsolutions.luulpos.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the ProductCategory entity.
 */
public class ProductCategoryDTO implements Serializable {

    private Long id;

    private String category;

    private String description;

    @Lob
    private byte[] imageFull;
    private String imageFullContentType;

    private String imageFullUrl;

    @Lob
    private byte[] imageThumb;
    private String imageThumbContentType;

    private String imageThumbUrl;

    private Long shopId;

    private String shopShopName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getImageFull() {
        return imageFull;
    }

    public void setImageFull(byte[] imageFull) {
        this.imageFull = imageFull;
    }

    public String getImageFullContentType() {
        return imageFullContentType;
    }

    public void setImageFullContentType(String imageFullContentType) {
        this.imageFullContentType = imageFullContentType;
    }

    public String getImageFullUrl() {
        return imageFullUrl;
    }

    public void setImageFullUrl(String imageFullUrl) {
        this.imageFullUrl = imageFullUrl;
    }

    public byte[] getImageThumb() {
        return imageThumb;
    }

    public void setImageThumb(byte[] imageThumb) {
        this.imageThumb = imageThumb;
    }

    public String getImageThumbContentType() {
        return imageThumbContentType;
    }

    public void setImageThumbContentType(String imageThumbContentType) {
        this.imageThumbContentType = imageThumbContentType;
    }

    public String getImageThumbUrl() {
        return imageThumbUrl;
    }

    public void setImageThumbUrl(String imageThumbUrl) {
        this.imageThumbUrl = imageThumbUrl;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProductCategoryDTO productCategoryDTO = (ProductCategoryDTO) o;
        if (productCategoryDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), productCategoryDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProductCategoryDTO{" +
            "id=" + getId() +
            ", category='" + getCategory() + "'" +
            ", description='" + getDescription() + "'" +
            ", imageFull='" + getImageFull() + "'" +
            ", imageFullUrl='" + getImageFullUrl() + "'" +
            ", imageThumb='" + getImageThumb() + "'" +
            ", imageThumbUrl='" + getImageThumbUrl() + "'" +
            ", shop=" + getShopId() +
            ", shop='" + getShopShopName() + "'" +
            "}";
    }
}
