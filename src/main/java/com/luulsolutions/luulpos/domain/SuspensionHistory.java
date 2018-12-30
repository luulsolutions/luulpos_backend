package com.luulsolutions.luulpos.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import com.luulsolutions.luulpos.domain.enumeration.SuspensionType;

/**
 * A SuspensionHistory.
 */
@Entity
@Table(name = "suspension_history")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "suspensionhistory")
public class SuspensionHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "suspended_date")
    private ZonedDateTime suspendedDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "suspension_type")
    private SuspensionType suspensionType;

    @Column(name = "suspended_reason")
    private String suspendedReason;

    @Column(name = "resolution_note")
    private String resolutionNote;

    @Column(name = "unsuspension_date")
    private ZonedDateTime unsuspensionDate;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Profile profile;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Profile suspendedBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getSuspendedDate() {
        return suspendedDate;
    }

    public SuspensionHistory suspendedDate(ZonedDateTime suspendedDate) {
        this.suspendedDate = suspendedDate;
        return this;
    }

    public void setSuspendedDate(ZonedDateTime suspendedDate) {
        this.suspendedDate = suspendedDate;
    }

    public SuspensionType getSuspensionType() {
        return suspensionType;
    }

    public SuspensionHistory suspensionType(SuspensionType suspensionType) {
        this.suspensionType = suspensionType;
        return this;
    }

    public void setSuspensionType(SuspensionType suspensionType) {
        this.suspensionType = suspensionType;
    }

    public String getSuspendedReason() {
        return suspendedReason;
    }

    public SuspensionHistory suspendedReason(String suspendedReason) {
        this.suspendedReason = suspendedReason;
        return this;
    }

    public void setSuspendedReason(String suspendedReason) {
        this.suspendedReason = suspendedReason;
    }

    public String getResolutionNote() {
        return resolutionNote;
    }

    public SuspensionHistory resolutionNote(String resolutionNote) {
        this.resolutionNote = resolutionNote;
        return this;
    }

    public void setResolutionNote(String resolutionNote) {
        this.resolutionNote = resolutionNote;
    }

    public ZonedDateTime getUnsuspensionDate() {
        return unsuspensionDate;
    }

    public SuspensionHistory unsuspensionDate(ZonedDateTime unsuspensionDate) {
        this.unsuspensionDate = unsuspensionDate;
        return this;
    }

    public void setUnsuspensionDate(ZonedDateTime unsuspensionDate) {
        this.unsuspensionDate = unsuspensionDate;
    }

    public Profile getProfile() {
        return profile;
    }

    public SuspensionHistory profile(Profile profile) {
        this.profile = profile;
        return this;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Profile getSuspendedBy() {
        return suspendedBy;
    }

    public SuspensionHistory suspendedBy(Profile profile) {
        this.suspendedBy = profile;
        return this;
    }

    public void setSuspendedBy(Profile profile) {
        this.suspendedBy = profile;
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
        SuspensionHistory suspensionHistory = (SuspensionHistory) o;
        if (suspensionHistory.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), suspensionHistory.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SuspensionHistory{" +
            "id=" + getId() +
            ", suspendedDate='" + getSuspendedDate() + "'" +
            ", suspensionType='" + getSuspensionType() + "'" +
            ", suspendedReason='" + getSuspendedReason() + "'" +
            ", resolutionNote='" + getResolutionNote() + "'" +
            ", unsuspensionDate='" + getUnsuspensionDate() + "'" +
            "}";
    }
}
