package com.luulsolutions.luulpos.service.dto;

import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import com.luulsolutions.luulpos.domain.enumeration.ShopAccountType;

/**
 * A DTO for the Shop entity.
 */
public class ShopDTO implements Serializable {

    private Long id;

    @NotNull
    private String shopName;

    private ShopAccountType shopAccountType;

    private ZonedDateTime approvalDate;

    private String address;

    private String email;

    private String description;

    private String note;

    private String landland;

    private String mobile;

    private ZonedDateTime createdDate;

    @Lob
    private byte[] shopLogo;
    private String shopLogoContentType;

    private String shopLogoUrl;

    private Boolean active;

    private String currency;

    private Long companyId;

    private String companyCompanyName;

    private Long approvedById;

    private String approvedByFirstName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public ShopAccountType getShopAccountType() {
        return shopAccountType;
    }

    public void setShopAccountType(ShopAccountType shopAccountType) {
        this.shopAccountType = shopAccountType;
    }

    public ZonedDateTime getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(ZonedDateTime approvalDate) {
        this.approvalDate = approvalDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getLandland() {
        return landland;
    }

    public void setLandland(String landland) {
        this.landland = landland;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public byte[] getShopLogo() {
        return shopLogo;
    }

    public void setShopLogo(byte[] shopLogo) {
        this.shopLogo = shopLogo;
    }

    public String getShopLogoContentType() {
        return shopLogoContentType;
    }

    public void setShopLogoContentType(String shopLogoContentType) {
        this.shopLogoContentType = shopLogoContentType;
    }

    public String getShopLogoUrl() {
        return shopLogoUrl;
    }

    public void setShopLogoUrl(String shopLogoUrl) {
        this.shopLogoUrl = shopLogoUrl;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getCompanyCompanyName() {
        return companyCompanyName;
    }

    public void setCompanyCompanyName(String companyCompanyName) {
        this.companyCompanyName = companyCompanyName;
    }

    public Long getApprovedById() {
        return approvedById;
    }

    public void setApprovedById(Long profileId) {
        this.approvedById = profileId;
    }

    public String getApprovedByFirstName() {
        return approvedByFirstName;
    }

    public void setApprovedByFirstName(String profileFirstName) {
        this.approvedByFirstName = profileFirstName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ShopDTO shopDTO = (ShopDTO) o;
        if (shopDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), shopDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ShopDTO{" +
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
            ", shopLogoUrl='" + getShopLogoUrl() + "'" +
            ", active='" + isActive() + "'" +
            ", currency='" + getCurrency() + "'" +
            ", company=" + getCompanyId() +
            ", company='" + getCompanyCompanyName() + "'" +
            ", approvedBy=" + getApprovedById() +
            ", approvedBy='" + getApprovedByFirstName() + "'" +
            "}";
    }
}
