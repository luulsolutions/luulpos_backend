package com.luulsolutions.luulpos.service.dto;

import java.io.Serializable;
import java.util.Objects;
import com.luulsolutions.luulpos.domain.enumeration.SuspensionType;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the SuspensionHistory entity. This class is used in SuspensionHistoryResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /suspension-histories?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SuspensionHistoryCriteria implements Serializable {
    /**
     * Class for filtering SuspensionType
     */
    public static class SuspensionTypeFilter extends Filter<SuspensionType> {
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ZonedDateTimeFilter suspendedDate;

    private SuspensionTypeFilter suspensionType;

    private StringFilter suspendedReason;

    private StringFilter resolutionNote;

    private ZonedDateTimeFilter unsuspensionDate;

    private LongFilter profileId;

    private LongFilter suspendedById;

    public SuspensionHistoryCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public ZonedDateTimeFilter getSuspendedDate() {
        return suspendedDate;
    }

    public void setSuspendedDate(ZonedDateTimeFilter suspendedDate) {
        this.suspendedDate = suspendedDate;
    }

    public SuspensionTypeFilter getSuspensionType() {
        return suspensionType;
    }

    public void setSuspensionType(SuspensionTypeFilter suspensionType) {
        this.suspensionType = suspensionType;
    }

    public StringFilter getSuspendedReason() {
        return suspendedReason;
    }

    public void setSuspendedReason(StringFilter suspendedReason) {
        this.suspendedReason = suspendedReason;
    }

    public StringFilter getResolutionNote() {
        return resolutionNote;
    }

    public void setResolutionNote(StringFilter resolutionNote) {
        this.resolutionNote = resolutionNote;
    }

    public ZonedDateTimeFilter getUnsuspensionDate() {
        return unsuspensionDate;
    }

    public void setUnsuspensionDate(ZonedDateTimeFilter unsuspensionDate) {
        this.unsuspensionDate = unsuspensionDate;
    }

    public LongFilter getProfileId() {
        return profileId;
    }

    public void setProfileId(LongFilter profileId) {
        this.profileId = profileId;
    }

    public LongFilter getSuspendedById() {
        return suspendedById;
    }

    public void setSuspendedById(LongFilter suspendedById) {
        this.suspendedById = suspendedById;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SuspensionHistoryCriteria that = (SuspensionHistoryCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(suspendedDate, that.suspendedDate) &&
            Objects.equals(suspensionType, that.suspensionType) &&
            Objects.equals(suspendedReason, that.suspendedReason) &&
            Objects.equals(resolutionNote, that.resolutionNote) &&
            Objects.equals(unsuspensionDate, that.unsuspensionDate) &&
            Objects.equals(profileId, that.profileId) &&
            Objects.equals(suspendedById, that.suspendedById);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        suspendedDate,
        suspensionType,
        suspendedReason,
        resolutionNote,
        unsuspensionDate,
        profileId,
        suspendedById
        );
    }

    @Override
    public String toString() {
        return "SuspensionHistoryCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (suspendedDate != null ? "suspendedDate=" + suspendedDate + ", " : "") +
                (suspensionType != null ? "suspensionType=" + suspensionType + ", " : "") +
                (suspendedReason != null ? "suspendedReason=" + suspendedReason + ", " : "") +
                (resolutionNote != null ? "resolutionNote=" + resolutionNote + ", " : "") +
                (unsuspensionDate != null ? "unsuspensionDate=" + unsuspensionDate + ", " : "") +
                (profileId != null ? "profileId=" + profileId + ", " : "") +
                (suspendedById != null ? "suspendedById=" + suspendedById + ", " : "") +
            "}";
    }

}
