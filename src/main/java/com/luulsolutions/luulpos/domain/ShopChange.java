package com.luulsolutions.luulpos.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A ShopChange.
 */
@Entity
@Table(name = "shop_change")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "shopchange")
public class ShopChange implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jhi_change")
    private String change;

    @Column(name = "changed_entity")
    private String changedEntity;

    @Column(name = "note")
    private String note;

    @Column(name = "change_date")
    private ZonedDateTime changeDate;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Shop shop;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Profile changedBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChange() {
        return change;
    }

    public ShopChange change(String change) {
        this.change = change;
        return this;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public String getChangedEntity() {
        return changedEntity;
    }

    public ShopChange changedEntity(String changedEntity) {
        this.changedEntity = changedEntity;
        return this;
    }

    public void setChangedEntity(String changedEntity) {
        this.changedEntity = changedEntity;
    }

    public String getNote() {
        return note;
    }

    public ShopChange note(String note) {
        this.note = note;
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public ZonedDateTime getChangeDate() {
        return changeDate;
    }

    public ShopChange changeDate(ZonedDateTime changeDate) {
        this.changeDate = changeDate;
        return this;
    }

    public void setChangeDate(ZonedDateTime changeDate) {
        this.changeDate = changeDate;
    }

    public Shop getShop() {
        return shop;
    }

    public ShopChange shop(Shop shop) {
        this.shop = shop;
        return this;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public Profile getChangedBy() {
        return changedBy;
    }

    public ShopChange changedBy(Profile profile) {
        this.changedBy = profile;
        return this;
    }

    public void setChangedBy(Profile profile) {
        this.changedBy = profile;
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
        ShopChange shopChange = (ShopChange) o;
        if (shopChange.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), shopChange.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ShopChange{" +
            "id=" + getId() +
            ", change='" + getChange() + "'" +
            ", changedEntity='" + getChangedEntity() + "'" +
            ", note='" + getNote() + "'" +
            ", changeDate='" + getChangeDate() + "'" +
            "}";
    }
}
