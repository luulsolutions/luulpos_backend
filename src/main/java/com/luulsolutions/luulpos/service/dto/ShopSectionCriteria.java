package com.luulsolutions.luulpos.service.dto;

import java.io.Serializable;
import java.util.Objects;

import io.github.jhipster.service.filter.BigDecimalFilter;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the ShopSection entity. This class is used in ShopSectionResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /shop-sections?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ShopSectionCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter sectionName;

    private StringFilter description;

    private IntegerFilter surchargePercentage;

    private BigDecimalFilter surchargeFlatAmount;

    private BooleanFilter usePercentage;

    private LongFilter shopId;

    public ShopSectionCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getSectionName() {
        return sectionName;
    }

    public void setSectionName(StringFilter sectionName) {
        this.sectionName = sectionName;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public IntegerFilter getSurchargePercentage() {
        return surchargePercentage;
    }

    public void setSurchargePercentage(IntegerFilter surchargePercentage) {
        this.surchargePercentage = surchargePercentage;
    }

    public BigDecimalFilter getSurchargeFlatAmount() {
        return surchargeFlatAmount;
    }

    public void setSurchargeFlatAmount(BigDecimalFilter surchargeFlatAmount) {
        this.surchargeFlatAmount = surchargeFlatAmount;
    }

    public BooleanFilter getUsePercentage() {
        return usePercentage;
    }

    public void setUsePercentage(BooleanFilter usePercentage) {
        this.usePercentage = usePercentage;
    }

    public LongFilter getShopId() {
        return shopId;
    }

    public void setShopId(LongFilter shopId) {
        this.shopId = shopId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ShopSectionCriteria that = (ShopSectionCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(sectionName, that.sectionName) &&
            Objects.equals(description, that.description) &&
            Objects.equals(surchargePercentage, that.surchargePercentage) &&
            Objects.equals(surchargeFlatAmount, that.surchargeFlatAmount) &&
            Objects.equals(usePercentage, that.usePercentage) &&
            Objects.equals(shopId, that.shopId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        sectionName,
        description,
        surchargePercentage,
        surchargeFlatAmount,
        usePercentage,
        shopId
        );
    }

    @Override
    public String toString() {
        return "ShopSectionCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (sectionName != null ? "sectionName=" + sectionName + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (surchargePercentage != null ? "surchargePercentage=" + surchargePercentage + ", " : "") +
                (surchargeFlatAmount != null ? "surchargeFlatAmount=" + surchargeFlatAmount + ", " : "") +
                (usePercentage != null ? "usePercentage=" + usePercentage + ", " : "") +
                (shopId != null ? "shopId=" + shopId + ", " : "") +
            "}";
    }

}
