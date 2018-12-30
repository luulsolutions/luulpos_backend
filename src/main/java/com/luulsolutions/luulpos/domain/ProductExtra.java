package com.luulsolutions.luulpos.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ProductExtra.
 */
@Entity
@Table(name = "product_extra")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "productextra")
public class ProductExtra implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "extra_name")
    private String extraName;

    @Column(name = "description")
    private String description;

    @Column(name = "extra_value")
    private Float extraValue;

    @Lob
    @Column(name = "full_photo")
    private byte[] fullPhoto;

    @Column(name = "full_photo_content_type")
    private String fullPhotoContentType;

    @Column(name = "full_photo_url")
    private String fullPhotoUrl;

    @Lob
    @Column(name = "thumbnail_photo")
    private byte[] thumbnailPhoto;

    @Column(name = "thumbnail_photo_content_type")
    private String thumbnailPhotoContentType;

    @Column(name = "thumbnail_photo_url")
    private String thumbnailPhotoUrl;

    @ManyToOne
    @JsonIgnoreProperties("extras")
    private Product product;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExtraName() {
        return extraName;
    }

    public ProductExtra extraName(String extraName) {
        this.extraName = extraName;
        return this;
    }

    public void setExtraName(String extraName) {
        this.extraName = extraName;
    }

    public String getDescription() {
        return description;
    }

    public ProductExtra description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getExtraValue() {
        return extraValue;
    }

    public ProductExtra extraValue(Float extraValue) {
        this.extraValue = extraValue;
        return this;
    }

    public void setExtraValue(Float extraValue) {
        this.extraValue = extraValue;
    }

    public byte[] getFullPhoto() {
        return fullPhoto;
    }

    public ProductExtra fullPhoto(byte[] fullPhoto) {
        this.fullPhoto = fullPhoto;
        return this;
    }

    public void setFullPhoto(byte[] fullPhoto) {
        this.fullPhoto = fullPhoto;
    }

    public String getFullPhotoContentType() {
        return fullPhotoContentType;
    }

    public ProductExtra fullPhotoContentType(String fullPhotoContentType) {
        this.fullPhotoContentType = fullPhotoContentType;
        return this;
    }

    public void setFullPhotoContentType(String fullPhotoContentType) {
        this.fullPhotoContentType = fullPhotoContentType;
    }

    public String getFullPhotoUrl() {
        return fullPhotoUrl;
    }

    public ProductExtra fullPhotoUrl(String fullPhotoUrl) {
        this.fullPhotoUrl = fullPhotoUrl;
        return this;
    }

    public void setFullPhotoUrl(String fullPhotoUrl) {
        this.fullPhotoUrl = fullPhotoUrl;
    }

    public byte[] getThumbnailPhoto() {
        return thumbnailPhoto;
    }

    public ProductExtra thumbnailPhoto(byte[] thumbnailPhoto) {
        this.thumbnailPhoto = thumbnailPhoto;
        return this;
    }

    public void setThumbnailPhoto(byte[] thumbnailPhoto) {
        this.thumbnailPhoto = thumbnailPhoto;
    }

    public String getThumbnailPhotoContentType() {
        return thumbnailPhotoContentType;
    }

    public ProductExtra thumbnailPhotoContentType(String thumbnailPhotoContentType) {
        this.thumbnailPhotoContentType = thumbnailPhotoContentType;
        return this;
    }

    public void setThumbnailPhotoContentType(String thumbnailPhotoContentType) {
        this.thumbnailPhotoContentType = thumbnailPhotoContentType;
    }

    public String getThumbnailPhotoUrl() {
        return thumbnailPhotoUrl;
    }

    public ProductExtra thumbnailPhotoUrl(String thumbnailPhotoUrl) {
        this.thumbnailPhotoUrl = thumbnailPhotoUrl;
        return this;
    }

    public void setThumbnailPhotoUrl(String thumbnailPhotoUrl) {
        this.thumbnailPhotoUrl = thumbnailPhotoUrl;
    }

    public Product getProduct() {
        return product;
    }

    public ProductExtra product(Product product) {
        this.product = product;
        return this;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProductExtra productExtra = (ProductExtra) o;
        if (productExtra.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), productExtra.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProductExtra{" +
            "id=" + getId() +
            ", extraName='" + getExtraName() + "'" +
            ", description='" + getDescription() + "'" +
            ", extraValue=" + getExtraValue() +
            ", fullPhoto='" + getFullPhoto() + "'" +
            ", fullPhotoContentType='" + getFullPhotoContentType() + "'" +
            ", fullPhotoUrl='" + getFullPhotoUrl() + "'" +
            ", thumbnailPhoto='" + getThumbnailPhoto() + "'" +
            ", thumbnailPhotoContentType='" + getThumbnailPhotoContentType() + "'" +
            ", thumbnailPhotoUrl='" + getThumbnailPhotoUrl() + "'" +
            "}";
    }
}
