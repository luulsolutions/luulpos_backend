package com.luulsolutions.luulpos.service.dto;

import java.io.Serializable;
import java.util.Objects;
import com.luulsolutions.luulpos.domain.enumeration.ShopAccountType;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the Shop entity. This class is used in ShopResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /shops?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ShopCriteria implements Serializable {
    /**
     * Class for filtering ShopAccountType
     */
    public static class ShopAccountTypeFilter extends Filter<ShopAccountType> {
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter shopName;

    private ShopAccountTypeFilter shopAccountType;

    private ZonedDateTimeFilter approvalDate;

    private StringFilter address;

    private StringFilter email;

    private StringFilter description;

    private StringFilter note;

    private StringFilter landland;

    private StringFilter mobile;

    private ZonedDateTimeFilter createdDate;

    private StringFilter shopLogoUrl;

    private BooleanFilter active;

    private StringFilter currency;

    private LongFilter companyId;

    private LongFilter approvedById;

    private LongFilter profilesId;

    private LongFilter productCategoriesId;

    private LongFilter productTypesId;

    private LongFilter systemConfigsId;

    private LongFilter discountsId;

    private LongFilter taxesId;

    public ShopCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getShopName() {
        return shopName;
    }

    public void setShopName(StringFilter shopName) {
        this.shopName = shopName;
    }

    public ShopAccountTypeFilter getShopAccountType() {
        return shopAccountType;
    }

    public void setShopAccountType(ShopAccountTypeFilter shopAccountType) {
        this.shopAccountType = shopAccountType;
    }

    public ZonedDateTimeFilter getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(ZonedDateTimeFilter approvalDate) {
        this.approvalDate = approvalDate;
    }

    public StringFilter getAddress() {
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public StringFilter getEmail() {
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getNote() {
        return note;
    }

    public void setNote(StringFilter note) {
        this.note = note;
    }

    public StringFilter getLandland() {
        return landland;
    }

    public void setLandland(StringFilter landland) {
        this.landland = landland;
    }

    public StringFilter getMobile() {
        return mobile;
    }

    public void setMobile(StringFilter mobile) {
        this.mobile = mobile;
    }

    public ZonedDateTimeFilter getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTimeFilter createdDate) {
        this.createdDate = createdDate;
    }

    public StringFilter getShopLogoUrl() {
        return shopLogoUrl;
    }

    public void setShopLogoUrl(StringFilter shopLogoUrl) {
        this.shopLogoUrl = shopLogoUrl;
    }

    public BooleanFilter getActive() {
        return active;
    }

    public void setActive(BooleanFilter active) {
        this.active = active;
    }

    public StringFilter getCurrency() {
        return currency;
    }

    public void setCurrency(StringFilter currency) {
        this.currency = currency;
    }

    public LongFilter getCompanyId() {
        return companyId;
    }

    public void setCompanyId(LongFilter companyId) {
        this.companyId = companyId;
    }

    public LongFilter getApprovedById() {
        return approvedById;
    }

    public void setApprovedById(LongFilter approvedById) {
        this.approvedById = approvedById;
    }

    public LongFilter getProfilesId() {
        return profilesId;
    }

    public void setProfilesId(LongFilter profilesId) {
        this.profilesId = profilesId;
    }

    public LongFilter getProductCategoriesId() {
        return productCategoriesId;
    }

    public void setProductCategoriesId(LongFilter productCategoriesId) {
        this.productCategoriesId = productCategoriesId;
    }

    public LongFilter getProductTypesId() {
        return productTypesId;
    }

    public void setProductTypesId(LongFilter productTypesId) {
        this.productTypesId = productTypesId;
    }

    public LongFilter getSystemConfigsId() {
        return systemConfigsId;
    }

    public void setSystemConfigsId(LongFilter systemConfigsId) {
        this.systemConfigsId = systemConfigsId;
    }

    public LongFilter getDiscountsId() {
        return discountsId;
    }

    public void setDiscountsId(LongFilter discountsId) {
        this.discountsId = discountsId;
    }

    public LongFilter getTaxesId() {
        return taxesId;
    }

    public void setTaxesId(LongFilter taxesId) {
        this.taxesId = taxesId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ShopCriteria that = (ShopCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(shopName, that.shopName) &&
            Objects.equals(shopAccountType, that.shopAccountType) &&
            Objects.equals(approvalDate, that.approvalDate) &&
            Objects.equals(address, that.address) &&
            Objects.equals(email, that.email) &&
            Objects.equals(description, that.description) &&
            Objects.equals(note, that.note) &&
            Objects.equals(landland, that.landland) &&
            Objects.equals(mobile, that.mobile) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(shopLogoUrl, that.shopLogoUrl) &&
            Objects.equals(active, that.active) &&
            Objects.equals(currency, that.currency) &&
            Objects.equals(companyId, that.companyId) &&
            Objects.equals(approvedById, that.approvedById) &&
            Objects.equals(profilesId, that.profilesId) &&
            Objects.equals(productCategoriesId, that.productCategoriesId) &&
            Objects.equals(productTypesId, that.productTypesId) &&
            Objects.equals(systemConfigsId, that.systemConfigsId) &&
            Objects.equals(discountsId, that.discountsId) &&
            Objects.equals(taxesId, that.taxesId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        shopName,
        shopAccountType,
        approvalDate,
        address,
        email,
        description,
        note,
        landland,
        mobile,
        createdDate,
        shopLogoUrl,
        active,
        currency,
        companyId,
        approvedById,
        profilesId,
        productCategoriesId,
        productTypesId,
        systemConfigsId,
        discountsId,
        taxesId
        );
    }

    @Override
    public String toString() {
        return "ShopCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (shopName != null ? "shopName=" + shopName + ", " : "") +
                (shopAccountType != null ? "shopAccountType=" + shopAccountType + ", " : "") +
                (approvalDate != null ? "approvalDate=" + approvalDate + ", " : "") +
                (address != null ? "address=" + address + ", " : "") +
                (email != null ? "email=" + email + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (note != null ? "note=" + note + ", " : "") +
                (landland != null ? "landland=" + landland + ", " : "") +
                (mobile != null ? "mobile=" + mobile + ", " : "") +
                (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
                (shopLogoUrl != null ? "shopLogoUrl=" + shopLogoUrl + ", " : "") +
                (active != null ? "active=" + active + ", " : "") +
                (currency != null ? "currency=" + currency + ", " : "") +
                (companyId != null ? "companyId=" + companyId + ", " : "") +
                (approvedById != null ? "approvedById=" + approvedById + ", " : "") +
                (profilesId != null ? "profilesId=" + profilesId + ", " : "") +
                (productCategoriesId != null ? "productCategoriesId=" + productCategoriesId + ", " : "") +
                (productTypesId != null ? "productTypesId=" + productTypesId + ", " : "") +
                (systemConfigsId != null ? "systemConfigsId=" + systemConfigsId + ", " : "") +
                (discountsId != null ? "discountsId=" + discountsId + ", " : "") +
                (taxesId != null ? "taxesId=" + taxesId + ", " : "") +
            "}";
    }

}
