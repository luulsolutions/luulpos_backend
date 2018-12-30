package com.luulsolutions.luulpos.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.luulsolutions.luulpos.domain.enumeration.ShopAccountType;

/**
 * A Shop.
 */
@Entity
@Table(name = "shop")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "shop")
public class Shop implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "shop_name", nullable = false)
    private String shopName;

    @Enumerated(EnumType.STRING)
    @Column(name = "shop_account_type")
    private ShopAccountType shopAccountType;

    @Column(name = "approval_date")
    private ZonedDateTime approvalDate;

    @Column(name = "address")
    private String address;

    @Column(name = "email")
    private String email;

    @Column(name = "description")
    private String description;

    @Column(name = "note")
    private String note;

    @Column(name = "landland")
    private String landland;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Lob
    @Column(name = "shop_logo")
    private byte[] shopLogo;

    @Column(name = "shop_logo_content_type")
    private String shopLogoContentType;

    @Column(name = "shop_logo_url")
    private String shopLogoUrl;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "currency")
    private String currency;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Company company;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Profile approvedBy;

    @OneToMany(mappedBy = "shop")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Profile> profiles = new HashSet<>();
    @OneToMany(mappedBy = "shop")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ProductCategory> productCategories = new HashSet<>();
    @OneToMany(mappedBy = "shop")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ProductType> productTypes = new HashSet<>();
    @OneToMany(mappedBy = "shop")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<SystemConfig> systemConfigs = new HashSet<>();
    @OneToMany(mappedBy = "shop")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Discount> discounts = new HashSet<>();
    @OneToMany(mappedBy = "shop")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Tax> taxes = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShopName() {
        return shopName;
    }

    public Shop shopName(String shopName) {
        this.shopName = shopName;
        return this;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public ShopAccountType getShopAccountType() {
        return shopAccountType;
    }

    public Shop shopAccountType(ShopAccountType shopAccountType) {
        this.shopAccountType = shopAccountType;
        return this;
    }

    public void setShopAccountType(ShopAccountType shopAccountType) {
        this.shopAccountType = shopAccountType;
    }

    public ZonedDateTime getApprovalDate() {
        return approvalDate;
    }

    public Shop approvalDate(ZonedDateTime approvalDate) {
        this.approvalDate = approvalDate;
        return this;
    }

    public void setApprovalDate(ZonedDateTime approvalDate) {
        this.approvalDate = approvalDate;
    }

    public String getAddress() {
        return address;
    }

    public Shop address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public Shop email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public Shop description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNote() {
        return note;
    }

    public Shop note(String note) {
        this.note = note;
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getLandland() {
        return landland;
    }

    public Shop landland(String landland) {
        this.landland = landland;
        return this;
    }

    public void setLandland(String landland) {
        this.landland = landland;
    }

    public String getMobile() {
        return mobile;
    }

    public Shop mobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public Shop createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public byte[] getShopLogo() {
        return shopLogo;
    }

    public Shop shopLogo(byte[] shopLogo) {
        this.shopLogo = shopLogo;
        return this;
    }

    public void setShopLogo(byte[] shopLogo) {
        this.shopLogo = shopLogo;
    }

    public String getShopLogoContentType() {
        return shopLogoContentType;
    }

    public Shop shopLogoContentType(String shopLogoContentType) {
        this.shopLogoContentType = shopLogoContentType;
        return this;
    }

    public void setShopLogoContentType(String shopLogoContentType) {
        this.shopLogoContentType = shopLogoContentType;
    }

    public String getShopLogoUrl() {
        return shopLogoUrl;
    }

    public Shop shopLogoUrl(String shopLogoUrl) {
        this.shopLogoUrl = shopLogoUrl;
        return this;
    }

    public void setShopLogoUrl(String shopLogoUrl) {
        this.shopLogoUrl = shopLogoUrl;
    }

    public Boolean isActive() {
        return active;
    }

    public Shop active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getCurrency() {
        return currency;
    }

    public Shop currency(String currency) {
        this.currency = currency;
        return this;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Company getCompany() {
        return company;
    }

    public Shop company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Profile getApprovedBy() {
        return approvedBy;
    }

    public Shop approvedBy(Profile profile) {
        this.approvedBy = profile;
        return this;
    }

    public void setApprovedBy(Profile profile) {
        this.approvedBy = profile;
    }

    public Set<Profile> getProfiles() {
        return profiles;
    }

    public Shop profiles(Set<Profile> profiles) {
        this.profiles = profiles;
        return this;
    }

    public Shop addProfiles(Profile profile) {
        this.profiles.add(profile);
        profile.setShop(this);
        return this;
    }

    public Shop removeProfiles(Profile profile) {
        this.profiles.remove(profile);
        profile.setShop(null);
        return this;
    }

    public void setProfiles(Set<Profile> profiles) {
        this.profiles = profiles;
    }

    public Set<ProductCategory> getProductCategories() {
        return productCategories;
    }

    public Shop productCategories(Set<ProductCategory> productCategories) {
        this.productCategories = productCategories;
        return this;
    }

    public Shop addProductCategories(ProductCategory productCategory) {
        this.productCategories.add(productCategory);
        productCategory.setShop(this);
        return this;
    }

    public Shop removeProductCategories(ProductCategory productCategory) {
        this.productCategories.remove(productCategory);
        productCategory.setShop(null);
        return this;
    }

    public void setProductCategories(Set<ProductCategory> productCategories) {
        this.productCategories = productCategories;
    }

    public Set<ProductType> getProductTypes() {
        return productTypes;
    }

    public Shop productTypes(Set<ProductType> productTypes) {
        this.productTypes = productTypes;
        return this;
    }

    public Shop addProductTypes(ProductType productType) {
        this.productTypes.add(productType);
        productType.setShop(this);
        return this;
    }

    public Shop removeProductTypes(ProductType productType) {
        this.productTypes.remove(productType);
        productType.setShop(null);
        return this;
    }

    public void setProductTypes(Set<ProductType> productTypes) {
        this.productTypes = productTypes;
    }

    public Set<SystemConfig> getSystemConfigs() {
        return systemConfigs;
    }

    public Shop systemConfigs(Set<SystemConfig> systemConfigs) {
        this.systemConfigs = systemConfigs;
        return this;
    }

    public Shop addSystemConfigs(SystemConfig systemConfig) {
        this.systemConfigs.add(systemConfig);
        systemConfig.setShop(this);
        return this;
    }

    public Shop removeSystemConfigs(SystemConfig systemConfig) {
        this.systemConfigs.remove(systemConfig);
        systemConfig.setShop(null);
        return this;
    }

    public void setSystemConfigs(Set<SystemConfig> systemConfigs) {
        this.systemConfigs = systemConfigs;
    }

    public Set<Discount> getDiscounts() {
        return discounts;
    }

    public Shop discounts(Set<Discount> discounts) {
        this.discounts = discounts;
        return this;
    }

    public Shop addDiscounts(Discount discount) {
        this.discounts.add(discount);
        discount.setShop(this);
        return this;
    }

    public Shop removeDiscounts(Discount discount) {
        this.discounts.remove(discount);
        discount.setShop(null);
        return this;
    }

    public void setDiscounts(Set<Discount> discounts) {
        this.discounts = discounts;
    }

    public Set<Tax> getTaxes() {
        return taxes;
    }

    public Shop taxes(Set<Tax> taxes) {
        this.taxes = taxes;
        return this;
    }

    public Shop addTaxes(Tax tax) {
        this.taxes.add(tax);
        tax.setShop(this);
        return this;
    }

    public Shop removeTaxes(Tax tax) {
        this.taxes.remove(tax);
        tax.setShop(null);
        return this;
    }

    public void setTaxes(Set<Tax> taxes) {
        this.taxes = taxes;
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
        Shop shop = (Shop) o;
        if (shop.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), shop.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Shop{" +
            "id=" + getId() +
            ", shopName='" + getShopName() + "'" +
            ", shopAccountType='" + getShopAccountType() + "'" +
            ", approvalDate='" + getApprovalDate() + "'" +
            ", address='" + getAddress() + "'" +
            ", email='" + getEmail() + "'" +
            ", description='" + getDescription() + "'" +
            ", note='" + getNote() + "'" +
            ", landland='" + getLandland() + "'" +
            ", mobile='" + getMobile() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", shopLogo='" + getShopLogo() + "'" +
            ", shopLogoContentType='" + getShopLogoContentType() + "'" +
            ", shopLogoUrl='" + getShopLogoUrl() + "'" +
            ", active='" + isActive() + "'" +
            ", currency='" + getCurrency() + "'" +
            "}";
    }
}
