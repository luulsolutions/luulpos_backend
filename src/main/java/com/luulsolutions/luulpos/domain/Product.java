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
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "product")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 30)
    @Column(name = "product_name", length = 30, nullable = false)
    private String productName;

    @Size(max = 99)
    @Column(name = "product_description", length = 99)
    private String productDescription;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Lob
    @Column(name = "product_image_full")
    private byte[] productImageFull;

    @Column(name = "product_image_full_content_type")
    private String productImageFullContentType;

    @Column(name = "product_image_full_url")
    private String productImageFullUrl;

    @Lob
    @Column(name = "product_image_thumb")
    private byte[] productImageThumb;

    @Column(name = "product_image_thumb_content_type")
    private String productImageThumbContentType;

    @Column(name = "product_image_thumb_url")
    private String productImageThumbUrl;

    @Column(name = "date_created")
    private ZonedDateTime dateCreated;

    @Column(name = "barcode")
    private String barcode;

    @Column(name = "serial_code")
    private String serialCode;

    @Column(name = "priority_position")
    private Integer priorityPosition;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "is_variant_product")
    private Boolean isVariantProduct;

    @OneToMany(mappedBy = "product")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ProductVariant> variants = new HashSet<>();
    @OneToMany(mappedBy = "product")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ProductExtra> extras = new HashSet<>();
    @ManyToOne
    @JsonIgnoreProperties("")
    private ProductType productTypes;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Shop shop;

    @ManyToOne
    @JsonIgnoreProperties("products")
    private Discount discounts;

    @ManyToOne
    @JsonIgnoreProperties("products")
    private Tax taxes;

    @ManyToOne
    @JsonIgnoreProperties("products")
    private ProductCategory category;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public Product productName(String productName) {
        this.productName = productName;
        return this;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public Product productDescription(String productDescription) {
        this.productDescription = productDescription;
        return this;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Product price(BigDecimal price) {
        this.price = price;
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Product quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public byte[] getProductImageFull() {
        return productImageFull;
    }

    public Product productImageFull(byte[] productImageFull) {
        this.productImageFull = productImageFull;
        return this;
    }

    public void setProductImageFull(byte[] productImageFull) {
        this.productImageFull = productImageFull;
    }

    public String getProductImageFullContentType() {
        return productImageFullContentType;
    }

    public Product productImageFullContentType(String productImageFullContentType) {
        this.productImageFullContentType = productImageFullContentType;
        return this;
    }

    public void setProductImageFullContentType(String productImageFullContentType) {
        this.productImageFullContentType = productImageFullContentType;
    }

    public String getProductImageFullUrl() {
        return productImageFullUrl;
    }

    public Product productImageFullUrl(String productImageFullUrl) {
        this.productImageFullUrl = productImageFullUrl;
        return this;
    }

    public void setProductImageFullUrl(String productImageFullUrl) {
        this.productImageFullUrl = productImageFullUrl;
    }

    public byte[] getProductImageThumb() {
        return productImageThumb;
    }

    public Product productImageThumb(byte[] productImageThumb) {
        this.productImageThumb = productImageThumb;
        return this;
    }

    public void setProductImageThumb(byte[] productImageThumb) {
        this.productImageThumb = productImageThumb;
    }

    public String getProductImageThumbContentType() {
        return productImageThumbContentType;
    }

    public Product productImageThumbContentType(String productImageThumbContentType) {
        this.productImageThumbContentType = productImageThumbContentType;
        return this;
    }

    public void setProductImageThumbContentType(String productImageThumbContentType) {
        this.productImageThumbContentType = productImageThumbContentType;
    }

    public String getProductImageThumbUrl() {
        return productImageThumbUrl;
    }

    public Product productImageThumbUrl(String productImageThumbUrl) {
        this.productImageThumbUrl = productImageThumbUrl;
        return this;
    }

    public void setProductImageThumbUrl(String productImageThumbUrl) {
        this.productImageThumbUrl = productImageThumbUrl;
    }

    public ZonedDateTime getDateCreated() {
        return dateCreated;
    }

    public Product dateCreated(ZonedDateTime dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public void setDateCreated(ZonedDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getBarcode() {
        return barcode;
    }

    public Product barcode(String barcode) {
        this.barcode = barcode;
        return this;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getSerialCode() {
        return serialCode;
    }

    public Product serialCode(String serialCode) {
        this.serialCode = serialCode;
        return this;
    }

    public void setSerialCode(String serialCode) {
        this.serialCode = serialCode;
    }

    public Integer getPriorityPosition() {
        return priorityPosition;
    }

    public Product priorityPosition(Integer priorityPosition) {
        this.priorityPosition = priorityPosition;
        return this;
    }

    public void setPriorityPosition(Integer priorityPosition) {
        this.priorityPosition = priorityPosition;
    }

    public Boolean isActive() {
        return active;
    }

    public Product active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean isIsVariantProduct() {
        return isVariantProduct;
    }

    public Product isVariantProduct(Boolean isVariantProduct) {
        this.isVariantProduct = isVariantProduct;
        return this;
    }

    public void setIsVariantProduct(Boolean isVariantProduct) {
        this.isVariantProduct = isVariantProduct;
    }

    public Set<ProductVariant> getVariants() {
        return variants;
    }

    public Product variants(Set<ProductVariant> productVariants) {
        this.variants = productVariants;
        return this;
    }

    public Product addVariants(ProductVariant productVariant) {
        this.variants.add(productVariant);
        productVariant.setProduct(this);
        return this;
    }

    public Product removeVariants(ProductVariant productVariant) {
        this.variants.remove(productVariant);
        productVariant.setProduct(null);
        return this;
    }

    public void setVariants(Set<ProductVariant> productVariants) {
        this.variants = productVariants;
    }

    public Set<ProductExtra> getExtras() {
        return extras;
    }

    public Product extras(Set<ProductExtra> productExtras) {
        this.extras = productExtras;
        return this;
    }

    public Product addExtras(ProductExtra productExtra) {
        this.extras.add(productExtra);
        productExtra.setProduct(this);
        return this;
    }

    public Product removeExtras(ProductExtra productExtra) {
        this.extras.remove(productExtra);
        productExtra.setProduct(null);
        return this;
    }

    public void setExtras(Set<ProductExtra> productExtras) {
        this.extras = productExtras;
    }

    public ProductType getProductTypes() {
        return productTypes;
    }

    public Product productTypes(ProductType productType) {
        this.productTypes = productType;
        return this;
    }

    public void setProductTypes(ProductType productType) {
        this.productTypes = productType;
    }

    public Shop getShop() {
        return shop;
    }

    public Product shop(Shop shop) {
        this.shop = shop;
        return this;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public Discount getDiscounts() {
        return discounts;
    }

    public Product discounts(Discount discount) {
        this.discounts = discount;
        return this;
    }

    public void setDiscounts(Discount discount) {
        this.discounts = discount;
    }

    public Tax getTaxes() {
        return taxes;
    }

    public Product taxes(Tax tax) {
        this.taxes = tax;
        return this;
    }

    public void setTaxes(Tax tax) {
        this.taxes = tax;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public Product category(ProductCategory productCategory) {
        this.category = productCategory;
        return this;
    }

    public void setCategory(ProductCategory productCategory) {
        this.category = productCategory;
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
        Product product = (Product) o;
        if (product.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), product.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", productName='" + getProductName() + "'" +
            ", productDescription='" + getProductDescription() + "'" +
            ", price=" + getPrice() +
            ", quantity=" + getQuantity() +
            ", productImageFull='" + getProductImageFull() + "'" +
            ", productImageFullContentType='" + getProductImageFullContentType() + "'" +
            ", productImageFullUrl='" + getProductImageFullUrl() + "'" +
            ", productImageThumb='" + getProductImageThumb() + "'" +
            ", productImageThumbContentType='" + getProductImageThumbContentType() + "'" +
            ", productImageThumbUrl='" + getProductImageThumbUrl() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", barcode='" + getBarcode() + "'" +
            ", serialCode='" + getSerialCode() + "'" +
            ", priorityPosition=" + getPriorityPosition() +
            ", active='" + isActive() + "'" +
            ", isVariantProduct='" + isIsVariantProduct() + "'" +
            "}";
    }
}
