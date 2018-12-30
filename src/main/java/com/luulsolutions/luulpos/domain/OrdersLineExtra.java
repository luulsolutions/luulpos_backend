package com.luulsolutions.luulpos.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A OrdersLineExtra.
 */
@Entity
@Table(name = "orders_line_extra")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "orderslineextra")
public class OrdersLineExtra implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "orders_line_extra_name", nullable = false)
    private String ordersLineExtraName;

    @NotNull
    @Column(name = "orders_line_extra_value", nullable = false)
    private String ordersLineExtraValue;

    @Column(name = "orders_line_extra_price")
    private Float ordersLineExtraPrice;

    @Column(name = "orders_option_description")
    private String ordersOptionDescription;

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
    @JsonIgnoreProperties("ordersLineExtras")
    private OrdersLineVariant ordersLineVariant;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrdersLineExtraName() {
        return ordersLineExtraName;
    }

    public OrdersLineExtra ordersLineExtraName(String ordersLineExtraName) {
        this.ordersLineExtraName = ordersLineExtraName;
        return this;
    }

    public void setOrdersLineExtraName(String ordersLineExtraName) {
        this.ordersLineExtraName = ordersLineExtraName;
    }

    public String getOrdersLineExtraValue() {
        return ordersLineExtraValue;
    }

    public OrdersLineExtra ordersLineExtraValue(String ordersLineExtraValue) {
        this.ordersLineExtraValue = ordersLineExtraValue;
        return this;
    }

    public void setOrdersLineExtraValue(String ordersLineExtraValue) {
        this.ordersLineExtraValue = ordersLineExtraValue;
    }

    public Float getOrdersLineExtraPrice() {
        return ordersLineExtraPrice;
    }

    public OrdersLineExtra ordersLineExtraPrice(Float ordersLineExtraPrice) {
        this.ordersLineExtraPrice = ordersLineExtraPrice;
        return this;
    }

    public void setOrdersLineExtraPrice(Float ordersLineExtraPrice) {
        this.ordersLineExtraPrice = ordersLineExtraPrice;
    }

    public String getOrdersOptionDescription() {
        return ordersOptionDescription;
    }

    public OrdersLineExtra ordersOptionDescription(String ordersOptionDescription) {
        this.ordersOptionDescription = ordersOptionDescription;
        return this;
    }

    public void setOrdersOptionDescription(String ordersOptionDescription) {
        this.ordersOptionDescription = ordersOptionDescription;
    }

    public byte[] getFullPhoto() {
        return fullPhoto;
    }

    public OrdersLineExtra fullPhoto(byte[] fullPhoto) {
        this.fullPhoto = fullPhoto;
        return this;
    }

    public void setFullPhoto(byte[] fullPhoto) {
        this.fullPhoto = fullPhoto;
    }

    public String getFullPhotoContentType() {
        return fullPhotoContentType;
    }

    public OrdersLineExtra fullPhotoContentType(String fullPhotoContentType) {
        this.fullPhotoContentType = fullPhotoContentType;
        return this;
    }

    public void setFullPhotoContentType(String fullPhotoContentType) {
        this.fullPhotoContentType = fullPhotoContentType;
    }

    public String getFullPhotoUrl() {
        return fullPhotoUrl;
    }

    public OrdersLineExtra fullPhotoUrl(String fullPhotoUrl) {
        this.fullPhotoUrl = fullPhotoUrl;
        return this;
    }

    public void setFullPhotoUrl(String fullPhotoUrl) {
        this.fullPhotoUrl = fullPhotoUrl;
    }

    public byte[] getThumbnailPhoto() {
        return thumbnailPhoto;
    }

    public OrdersLineExtra thumbnailPhoto(byte[] thumbnailPhoto) {
        this.thumbnailPhoto = thumbnailPhoto;
        return this;
    }

    public void setThumbnailPhoto(byte[] thumbnailPhoto) {
        this.thumbnailPhoto = thumbnailPhoto;
    }

    public String getThumbnailPhotoContentType() {
        return thumbnailPhotoContentType;
    }

    public OrdersLineExtra thumbnailPhotoContentType(String thumbnailPhotoContentType) {
        this.thumbnailPhotoContentType = thumbnailPhotoContentType;
        return this;
    }

    public void setThumbnailPhotoContentType(String thumbnailPhotoContentType) {
        this.thumbnailPhotoContentType = thumbnailPhotoContentType;
    }

    public String getThumbnailPhotoUrl() {
        return thumbnailPhotoUrl;
    }

    public OrdersLineExtra thumbnailPhotoUrl(String thumbnailPhotoUrl) {
        this.thumbnailPhotoUrl = thumbnailPhotoUrl;
        return this;
    }

    public void setThumbnailPhotoUrl(String thumbnailPhotoUrl) {
        this.thumbnailPhotoUrl = thumbnailPhotoUrl;
    }

    public OrdersLineVariant getOrdersLineVariant() {
        return ordersLineVariant;
    }

    public OrdersLineExtra ordersLineVariant(OrdersLineVariant ordersLineVariant) {
        this.ordersLineVariant = ordersLineVariant;
        return this;
    }

    public void setOrdersLineVariant(OrdersLineVariant ordersLineVariant) {
        this.ordersLineVariant = ordersLineVariant;
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
        OrdersLineExtra ordersLineExtra = (OrdersLineExtra) o;
        if (ordersLineExtra.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ordersLineExtra.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OrdersLineExtra{" +
            "id=" + getId() +
            ", ordersLineExtraName='" + getOrdersLineExtraName() + "'" +
            ", ordersLineExtraValue='" + getOrdersLineExtraValue() + "'" +
            ", ordersLineExtraPrice=" + getOrdersLineExtraPrice() +
            ", ordersOptionDescription='" + getOrdersOptionDescription() + "'" +
            ", fullPhoto='" + getFullPhoto() + "'" +
            ", fullPhotoContentType='" + getFullPhotoContentType() + "'" +
            ", fullPhotoUrl='" + getFullPhotoUrl() + "'" +
            ", thumbnailPhoto='" + getThumbnailPhoto() + "'" +
            ", thumbnailPhotoContentType='" + getThumbnailPhotoContentType() + "'" +
            ", thumbnailPhotoUrl='" + getThumbnailPhotoUrl() + "'" +
            "}";
    }
}
