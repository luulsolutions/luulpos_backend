package com.luulsolutions.luulpos.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A ProductCategory.
 */
@Entity
@Table(name = "product_category")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "productcategory")
public class ProductCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category")
    private String category;

    @Column(name = "description")
    private String description;

    @Lob
    @Column(name = "image_full")
    private byte[] imageFull;

    @Column(name = "image_full_content_type")
    private String imageFullContentType;

    @Column(name = "image_full_url")
    private String imageFullUrl;

    @Lob
    @Column(name = "image_thumb")
    private byte[] imageThumb;

    @Column(name = "image_thumb_content_type")
    private String imageThumbContentType;

    @Column(name = "image_thumb_url")
    private String imageThumbUrl;

    @ManyToOne
    @JsonIgnoreProperties("productCategories")
    private Shop shop;

    @OneToMany(mappedBy = "category")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Product> products = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public ProductCategory category(String category) {
        this.category = category;
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public ProductCategory description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getImageFull() {
        return imageFull;
    }

    public ProductCategory imageFull(byte[] imageFull) {
        this.imageFull = imageFull;
        return this;
    }

    public void setImageFull(byte[] imageFull) {
        this.imageFull = imageFull;
    }

    public String getImageFullContentType() {
        return imageFullContentType;
    }

    public ProductCategory imageFullContentType(String imageFullContentType) {
        this.imageFullContentType = imageFullContentType;
        return this;
    }

    public void setImageFullContentType(String imageFullContentType) {
        this.imageFullContentType = imageFullContentType;
    }

    public String getImageFullUrl() {
        return imageFullUrl;
    }

    public ProductCategory imageFullUrl(String imageFullUrl) {
        this.imageFullUrl = imageFullUrl;
        return this;
    }

    public void setImageFullUrl(String imageFullUrl) {
        this.imageFullUrl = imageFullUrl;
    }

    public byte[] getImageThumb() {
        return imageThumb;
    }

    public ProductCategory imageThumb(byte[] imageThumb) {
        this.imageThumb = imageThumb;
        return this;
    }

    public void setImageThumb(byte[] imageThumb) {
        this.imageThumb = imageThumb;
    }

    public String getImageThumbContentType() {
        return imageThumbContentType;
    }

    public ProductCategory imageThumbContentType(String imageThumbContentType) {
        this.imageThumbContentType = imageThumbContentType;
        return this;
    }

    public void setImageThumbContentType(String imageThumbContentType) {
        this.imageThumbContentType = imageThumbContentType;
    }

    public String getImageThumbUrl() {
        return imageThumbUrl;
    }

    public ProductCategory imageThumbUrl(String imageThumbUrl) {
        this.imageThumbUrl = imageThumbUrl;
        return this;
    }

    public void setImageThumbUrl(String imageThumbUrl) {
        this.imageThumbUrl = imageThumbUrl;
    }

    public Shop getShop() {
        return shop;
    }

    public ProductCategory shop(Shop shop) {
        this.shop = shop;
        return this;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public ProductCategory products(Set<Product> products) {
        this.products = products;
        return this;
    }

    public ProductCategory addProducts(Product product) {
        this.products.add(product);
        product.setCategory(this);
        return this;
    }

    public ProductCategory removeProducts(Product product) {
        this.products.remove(product);
        product.setCategory(null);
        return this;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
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
        ProductCategory productCategory = (ProductCategory) o;
        if (productCategory.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), productCategory.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProductCategory{" +
            "id=" + getId() +
            ", category='" + getCategory() + "'" +
            ", description='" + getDescription() + "'" +
            ", imageFull='" + getImageFull() + "'" +
            ", imageFullContentType='" + getImageFullContentType() + "'" +
            ", imageFullUrl='" + getImageFullUrl() + "'" +
            ", imageThumb='" + getImageThumb() + "'" +
            ", imageThumbContentType='" + getImageThumbContentType() + "'" +
            ", imageThumbUrl='" + getImageThumbUrl() + "'" +
            "}";
    }
}
