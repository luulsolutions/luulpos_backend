package com.luulsolutions.luulpos.service.dto;

import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the ShopChange entity.
 */
public class ShopChangeDTO implements Serializable {

    private Long id;

    private String change;

    private String changedEntity;

    private String note;

    private ZonedDateTime changeDate;

    private Long shopId;

    private String shopShopName;

    private Long changedById;

    private String changedByFirstName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public String getChangedEntity() {
        return changedEntity;
    }

    public void setChangedEntity(String changedEntity) {
        this.changedEntity = changedEntity;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public ZonedDateTime getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(ZonedDateTime changeDate) {
        this.changeDate = changeDate;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getShopShopName() {
        return shopShopName;
    }

    public void setShopShopName(String shopShopName) {
        this.shopShopName = shopShopName;
    }

    public Long getChangedById() {
        return changedById;
    }

    public void setChangedById(Long profileId) {
        this.changedById = profileId;
    }

    public String getChangedByFirstName() {
        return changedByFirstName;
    }

    public void setChangedByFirstName(String profileFirstName) {
        this.changedByFirstName = profileFirstName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ShopChangeDTO shopChangeDTO = (ShopChangeDTO) o;
        if (shopChangeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), shopChangeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ShopChangeDTO{" +
            "id=" + getId() +
            ", change='" + getChange() + "'" +
            ", changedEntity='" + getChangedEntity() + "'" +
            ", note='" + getNote() + "'" +
            ", changeDate='" + getChangeDate() + "'" +
            ", shop=" + getShopId() +
            ", shop='" + getShopShopName() + "'" +
            ", changedBy=" + getChangedById() +
            ", changedBy='" + getChangedByFirstName() + "'" +
            "}";
    }
}
