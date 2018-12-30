package com.luulsolutions.luulpos.service.dto;

import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.Objects;
import com.luulsolutions.luulpos.domain.enumeration.SuspensionType;

/**
 * A DTO for the SuspensionHistory entity.
 */
public class SuspensionHistoryDTO implements Serializable {

    private Long id;

    private ZonedDateTime suspendedDate;

    private SuspensionType suspensionType;

    private String suspendedReason;

    private String resolutionNote;

    private ZonedDateTime unsuspensionDate;

    private Long profileId;

    private String profileFirstName;

    private Long suspendedById;

    private String suspendedByFirstName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getSuspendedDate() {
        return suspendedDate;
    }

    public void setSuspendedDate(ZonedDateTime suspendedDate) {
        this.suspendedDate = suspendedDate;
    }

    public SuspensionType getSuspensionType() {
        return suspensionType;
    }

    public void setSuspensionType(SuspensionType suspensionType) {
        this.suspensionType = suspensionType;
    }

    public String getSuspendedReason() {
        return suspendedReason;
    }

    public void setSuspendedReason(String suspendedReason) {
        this.suspendedReason = suspendedReason;
    }

    public String getResolutionNote() {
        return resolutionNote;
    }

    public void setResolutionNote(String resolutionNote) {
        this.resolutionNote = resolutionNote;
    }

    public ZonedDateTime getUnsuspensionDate() {
        return unsuspensionDate;
    }

    public void setUnsuspensionDate(ZonedDateTime unsuspensionDate) {
        this.unsuspensionDate = unsuspensionDate;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public String getProfileFirstName() {
        return profileFirstName;
    }

    public void setProfileFirstName(String profileFirstName) {
        this.profileFirstName = profileFirstName;
    }

    public Long getSuspendedById() {
        return suspendedById;
    }

    public void setSuspendedById(Long profileId) {
        this.suspendedById = profileId;
    }

    public String getSuspendedByFirstName() {
        return suspendedByFirstName;
    }

    public void setSuspendedByFirstName(String profileFirstName) {
        this.suspendedByFirstName = profileFirstName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SuspensionHistoryDTO suspensionHistoryDTO = (SuspensionHistoryDTO) o;
        if (suspensionHistoryDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), suspensionHistoryDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SuspensionHistoryDTO{" +
            "id=" + getId() +
            ", suspendedDate='" + getSuspendedDate() + "'" +
            ", suspensionType='" + getSuspensionType() + "'" +
            ", suspendedReason='" + getSuspendedReason() + "'" +
            ", resolutionNote='" + getResolutionNote() + "'" +
            ", unsuspensionDate='" + getUnsuspensionDate() + "'" +
            ", profile=" + getProfileId() +
            ", profile='" + getProfileFirstName() + "'" +
            ", suspendedBy=" + getSuspendedById() +
            ", suspendedBy='" + getSuspendedByFirstName() + "'" +
            "}";
    }
}
