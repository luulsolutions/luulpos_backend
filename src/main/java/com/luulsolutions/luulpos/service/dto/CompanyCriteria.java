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
 * Criteria class for the Company entity. This class is used in CompanyResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /companies?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CompanyCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter companyName;

    private StringFilter description;

    private StringFilter note;

    private StringFilter companyLogoUrl;

    private BooleanFilter active;

    public CompanyCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getCompanyName() {
        return companyName;
    }

    public void setCompanyName(StringFilter companyName) {
        this.companyName = companyName;
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

    public StringFilter getCompanyLogoUrl() {
        return companyLogoUrl;
    }

    public void setCompanyLogoUrl(StringFilter companyLogoUrl) {
        this.companyLogoUrl = companyLogoUrl;
    }

    public BooleanFilter getActive() {
        return active;
    }

    public void setActive(BooleanFilter active) {
        this.active = active;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CompanyCriteria that = (CompanyCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(companyName, that.companyName) &&
            Objects.equals(description, that.description) &&
            Objects.equals(note, that.note) &&
            Objects.equals(companyLogoUrl, that.companyLogoUrl) &&
            Objects.equals(active, that.active);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        companyName,
        description,
        note,
        companyLogoUrl,
        active
        );
    }

    @Override
    public String toString() {
        return "CompanyCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (companyName != null ? "companyName=" + companyName + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (note != null ? "note=" + note + ", " : "") +
                (companyLogoUrl != null ? "companyLogoUrl=" + companyLogoUrl + ", " : "") +
                (active != null ? "active=" + active + ", " : "") +
            "}";
    }

}
