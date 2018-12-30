package com.luulsolutions.luulpos.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A OrdersLineVariant.
 */
@Entity
@Table(name = "orders_line_variant")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "orderslinevariant")
public class OrdersLineVariant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "variant_name")
    private String variantName;

    @Column(name = "variant_value")
    private String variantValue;

    @Column(name = "description")
    private String description;

    @Column(name = "percentage")
    private Float percentage;

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

    @NotNull
    @Column(name = "price", precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    @ManyToOne
    @JsonIgnoreProperties("ordersLineVariants")
    private OrdersLine ordersLine;

    @OneToMany(mappedBy = "ordersLineVariant")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<OrdersLineExtra> ordersLineExtras = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVariantName() {
        return variantName;
    }

    public OrdersLineVariant variantName(String variantName) {
        this.variantName = variantName;
        return this;
    }

    public void setVariantName(String variantName) {
        this.variantName = variantName;
    }

    public String getVariantValue() {
        return variantValue;
    }

    public OrdersLineVariant variantValue(String variantValue) {
        this.variantValue = variantValue;
        return this;
    }

    public void setVariantValue(String variantValue) {
        this.variantValue = variantValue;
    }

    public String getDescription() {
        return description;
    }

    public OrdersLineVariant description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getPercentage() {
        return percentage;
    }

    public OrdersLineVariant percentage(Float percentage) {
        this.percentage = percentage;
        return this;
    }

    public void setPercentage(Float percentage) {
        this.percentage = percentage;
    }

    public byte[] getFullPhoto() {
        return fullPhoto;
    }

    public OrdersLineVariant fullPhoto(byte[] fullPhoto) {
        this.fullPhoto = fullPhoto;
        return this;
    }

    public void setFullPhoto(byte[] fullPhoto) {
        this.fullPhoto = fullPhoto;
    }

    public String getFullPhotoContentType() {
        return fullPhotoContentType;
    }

    public OrdersLineVariant fullPhotoContentType(String fullPhotoContentType) {
        this.fullPhotoContentType = fullPhotoContentType;
        return this;
    }

    public void setFullPhotoContentType(String fullPhotoContentType) {
        this.fullPhotoContentType = fullPhotoContentType;
    }

    public String getFullPhotoUrl() {
        return fullPhotoUrl;
    }

    public OrdersLineVariant fullPhotoUrl(String fullPhotoUrl) {
        this.fullPhotoUrl = fullPhotoUrl;
        return this;
    }

    public void setFullPhotoUrl(String fullPhotoUrl) {
        this.fullPhotoUrl = fullPhotoUrl;
    }

    public byte[] getThumbnailPhoto() {
        return thumbnailPhoto;
    }

    public OrdersLineVariant thumbnailPhoto(byte[] thumbnailPhoto) {
        this.thumbnailPhoto = thumbnailPhoto;
        return this;
    }

    public void setThumbnailPhoto(byte[] thumbnailPhoto) {
        this.thumbnailPhoto = thumbnailPhoto;
    }

    public String getThumbnailPhotoContentType() {
        return thumbnailPhotoContentType;
    }

    public OrdersLineVariant thumbnailPhotoContentType(String thumbnailPhotoContentType) {
        this.thumbnailPhotoContentType = thumbnailPhotoContentType;
        return this;
    }

    public void setThumbnailPhotoContentType(String thumbnailPhotoContentType) {
        this.thumbnailPhotoContentType = thumbnailPhotoContentType;
    }

    public String getThumbnailPhotoUrl() {
        return thumbnailPhotoUrl;
    }

    public OrdersLineVariant thumbnailPhotoUrl(String thumbnailPhotoUrl) {
        this.thumbnailPhotoUrl = thumbnailPhotoUrl;
        return this;
    }

    public void setThumbnailPhotoUrl(String thumbnailPhotoUrl) {
        this.thumbnailPhotoUrl = thumbnailPhotoUrl;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public OrdersLineVariant price(BigDecimal price) {
        this.price = price;
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public OrdersLine getOrdersLine() {
        return ordersLine;
    }

    public OrdersLineVariant ordersLine(OrdersLine ordersLine) {
        this.ordersLine = ordersLine;
        return this;
    }

    public void setOrdersLine(OrdersLine ordersLine) {
        this.ordersLine = ordersLine;
    }

    public Set<OrdersLineExtra> getOrdersLineExtras() {
        return ordersLineExtras;
    }

    public OrdersLineVariant ordersLineExtras(Set<OrdersLineExtra> ordersLineExtras) {
        this.ordersLineExtras = ordersLineExtras;
        return this;
    }

    public OrdersLineVariant addOrdersLineExtras(OrdersLineExtra ordersLineExtra) {
        this.ordersLineExtras.add(ordersLineExtra);
        ordersLineExtra.setOrdersLineVariant(this);
        return this;
    }

    public OrdersLineVariant removeOrdersLineExtras(OrdersLineExtra ordersLineExtra) {
        this.ordersLineExtras.remove(ordersLineExtra);
        ordersLineExtra.setOrdersLineVariant(null);
        return this;
    }

    public void setOrdersLineExtras(Set<OrdersLineExtra> ordersLineExtras) {
        this.ordersLineExtras = ordersLineExtras;
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
        OrdersLineVariant ordersLineVariant = (OrdersLineVariant) o;
        if (ordersLineVariant.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ordersLineVariant.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OrdersLineVariant{" +
            "id=" + getId() +
            ", variantName='" + getVariantName() + "'" +
            ", variantValue='" + getVariantValue() + "'" +
            ", description='" + getDescription() + "'" +
            ", percentage=" + getPercentage() +
            ", fullPhoto='" + getFullPhoto() + "'" +
            ", fullPhotoContentType='" + getFullPhotoContentType() + "'" +
            ", fullPhotoUrl='" + getFullPhotoUrl() + "'" +
            ", thumbnailPhoto='" + getThumbnailPhoto() + "'" +
            ", thumbnailPhotoContentType='" + getThumbnailPhotoContentType() + "'" +
            ", thumbnailPhotoUrl='" + getThumbnailPhotoUrl() + "'" +
            ", price=" + getPrice() +
            "}";
    }
}
