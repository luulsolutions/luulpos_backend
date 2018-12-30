package com.luulsolutions.luulpos.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the SectionTable entity. This class is used in SectionTableResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /section-tables?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SectionTableCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter tableNumber;

    private StringFilter description;

    private LongFilter shopSectionsId;

    public SectionTableCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(IntegerFilter tableNumber) {
        this.tableNumber = tableNumber;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public LongFilter getShopSectionsId() {
        return shopSectionsId;
    }

    public void setShopSectionsId(LongFilter shopSectionsId) {
        this.shopSectionsId = shopSectionsId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SectionTableCriteria that = (SectionTableCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(tableNumber, that.tableNumber) &&
            Objects.equals(description, that.description) &&
            Objects.equals(shopSectionsId, that.shopSectionsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        tableNumber,
        description,
        shopSectionsId
        );
    }

    @Override
    public String toString() {
        return "SectionTableCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (tableNumber != null ? "tableNumber=" + tableNumber + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (shopSectionsId != null ? "shopSectionsId=" + shopSectionsId + ", " : "") +
            "}";
    }

}
