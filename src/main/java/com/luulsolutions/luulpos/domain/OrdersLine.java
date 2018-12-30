package com.luulsolutions.luulpos.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A OrdersLine.
 */
@Entity
@Table(name = "orders_line")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "ordersline")
public class OrdersLine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "orders_line_name", nullable = false)
    private String ordersLineName;

    @NotNull
    @Column(name = "orders_line_value", nullable = false)
    private String ordersLineValue;

    @Column(name = "orders_line_price")
    private Float ordersLinePrice;

    @Column(name = "orders_line_description")
    private String ordersLineDescription;

    @Lob
    @Column(name = "thumbnail_photo")
    private byte[] thumbnailPhoto;

    @Column(name = "thumbnail_photo_content_type")
    private String thumbnailPhotoContentType;

    @Lob
    @Column(name = "full_photo")
    private byte[] fullPhoto;

    @Column(name = "full_photo_content_type")
    private String fullPhotoContentType;

    @Column(name = "full_photo_url")
    private String fullPhotoUrl;

    @Column(name = "thumbnail_photo_url")
    private String thumbnailPhotoUrl;

    @ManyToOne
    @JsonIgnoreProperties("ordersLines")
    private Orders orders;

    @OneToMany(mappedBy = "ordersLine")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<OrdersLineVariant> ordersLineVariants = new HashSet<>();
    @ManyToOne
    @JsonIgnoreProperties("")
    private Product product;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrdersLineName() {
        return ordersLineName;
    }

    public OrdersLine ordersLineName(String ordersLineName) {
        this.ordersLineName = ordersLineName;
        return this;
    }

    public void setOrdersLineName(String ordersLineName) {
        this.ordersLineName = ordersLineName;
    }

    public String getOrdersLineValue() {
        return ordersLineValue;
    }

    public OrdersLine ordersLineValue(String ordersLineValue) {
        this.ordersLineValue = ordersLineValue;
        return this;
    }

    public void setOrdersLineValue(String ordersLineValue) {
        this.ordersLineValue = ordersLineValue;
    }

    public Float getOrdersLinePrice() {
        return ordersLinePrice;
    }

    public OrdersLine ordersLinePrice(Float ordersLinePrice) {
        this.ordersLinePrice = ordersLinePrice;
        return this;
    }

    public void setOrdersLinePrice(Float ordersLinePrice) {
        this.ordersLinePrice = ordersLinePrice;
    }

    public String getOrdersLineDescription() {
        return ordersLineDescription;
    }

    public OrdersLine ordersLineDescription(String ordersLineDescription) {
        this.ordersLineDescription = ordersLineDescription;
        return this;
    }

    public void setOrdersLineDescription(String ordersLineDescription) {
        this.ordersLineDescription = ordersLineDescription;
    }

    public byte[] getThumbnailPhoto() {
        return thumbnailPhoto;
    }

    public OrdersLine thumbnailPhoto(byte[] thumbnailPhoto) {
        this.thumbnailPhoto = thumbnailPhoto;
        return this;
    }

    public void setThumbnailPhoto(byte[] thumbnailPhoto) {
        this.thumbnailPhoto = thumbnailPhoto;
    }

    public String getThumbnailPhotoContentType() {
        return thumbnailPhotoContentType;
    }

    public OrdersLine thumbnailPhotoContentType(String thumbnailPhotoContentType) {
        this.thumbnailPhotoContentType = thumbnailPhotoContentType;
        return this;
    }

    public void setThumbnailPhotoContentType(String thumbnailPhotoContentType) {
        this.thumbnailPhotoContentType = thumbnailPhotoContentType;
    }

    public byte[] getFullPhoto() {
        return fullPhoto;
    }

    public OrdersLine fullPhoto(byte[] fullPhoto) {
        this.fullPhoto = fullPhoto;
        return this;
    }

    public void setFullPhoto(byte[] fullPhoto) {
        this.fullPhoto = fullPhoto;
    }

    public String getFullPhotoContentType() {
        return fullPhotoContentType;
    }

    public OrdersLine fullPhotoContentType(String fullPhotoContentType) {
        this.fullPhotoContentType = fullPhotoContentType;
        return this;
    }

    public void setFullPhotoContentType(String fullPhotoContentType) {
        this.fullPhotoContentType = fullPhotoContentType;
    }

    public String getFullPhotoUrl() {
        return fullPhotoUrl;
    }

    public OrdersLine fullPhotoUrl(String fullPhotoUrl) {
        this.fullPhotoUrl = fullPhotoUrl;
        return this;
    }

    public void setFullPhotoUrl(String fullPhotoUrl) {
        this.fullPhotoUrl = fullPhotoUrl;
    }

    public String getThumbnailPhotoUrl() {
        return thumbnailPhotoUrl;
    }

    public OrdersLine thumbnailPhotoUrl(String thumbnailPhotoUrl) {
        this.thumbnailPhotoUrl = thumbnailPhotoUrl;
        return this;
    }

    public void setThumbnailPhotoUrl(String thumbnailPhotoUrl) {
        this.thumbnailPhotoUrl = thumbnailPhotoUrl;
    }

    public Orders getOrders() {
        return orders;
    }

    public OrdersLine orders(Orders orders) {
        this.orders = orders;
        return this;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    public Set<OrdersLineVariant> getOrdersLineVariants() {
        return ordersLineVariants;
    }

    public OrdersLine ordersLineVariants(Set<OrdersLineVariant> ordersLineVariants) {
        this.ordersLineVariants = ordersLineVariants;
        return this;
    }

    public OrdersLine addOrdersLineVariants(OrdersLineVariant ordersLineVariant) {
        this.ordersLineVariants.add(ordersLineVariant);
        ordersLineVariant.setOrdersLine(this);
        return this;
    }

    public OrdersLine removeOrdersLineVariants(OrdersLineVariant ordersLineVariant) {
        this.ordersLineVariants.remove(ordersLineVariant);
        ordersLineVariant.setOrdersLine(null);
        return this;
    }

    public void setOrdersLineVariants(Set<OrdersLineVariant> ordersLineVariants) {
        this.ordersLineVariants = ordersLineVariants;
    }

    public Product getProduct() {
        return product;
    }

    public OrdersLine product(Product product) {
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
        OrdersLine ordersLine = (OrdersLine) o;
        if (ordersLine.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ordersLine.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OrdersLine{" +
            "id=" + getId() +
            ", ordersLineName='" + getOrdersLineName() + "'" +
            ", ordersLineValue='" + getOrdersLineValue() + "'" +
            ", ordersLinePrice=" + getOrdersLinePrice() +
            ", ordersLineDescription='" + getOrdersLineDescription() + "'" +
            ", thumbnailPhoto='" + getThumbnailPhoto() + "'" +
            ", thumbnailPhotoContentType='" + getThumbnailPhotoContentType() + "'" +
            ", fullPhoto='" + getFullPhoto() + "'" +
            ", fullPhotoContentType='" + getFullPhotoContentType() + "'" +
            ", fullPhotoUrl='" + getFullPhotoUrl() + "'" +
            ", thumbnailPhotoUrl='" + getThumbnailPhotoUrl() + "'" +
            "}";
    }
}
