package com.luulsolutions.luulpos.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A SystemEventsHistory.
 */
@Entity
@Table(name = "system_events_history")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "systemeventshistory")
public class SystemEventsHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "event_name", nullable = false)
    private String eventName;

    @Column(name = "event_date")
    private ZonedDateTime eventDate;

    @Column(name = "event_api")
    private String eventApi;

    @Column(name = "event_note")
    private String eventNote;

    @Column(name = "event_entity_name")
    private String eventEntityName;

    @Column(name = "event_entity_id")
    private Long eventEntityId;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Profile triggedBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public SystemEventsHistory eventName(String eventName) {
        this.eventName = eventName;
        return this;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public ZonedDateTime getEventDate() {
        return eventDate;
    }

    public SystemEventsHistory eventDate(ZonedDateTime eventDate) {
        this.eventDate = eventDate;
        return this;
    }

    public void setEventDate(ZonedDateTime eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventApi() {
        return eventApi;
    }

    public SystemEventsHistory eventApi(String eventApi) {
        this.eventApi = eventApi;
        return this;
    }

    public void setEventApi(String eventApi) {
        this.eventApi = eventApi;
    }

    public String getEventNote() {
        return eventNote;
    }

    public SystemEventsHistory eventNote(String eventNote) {
        this.eventNote = eventNote;
        return this;
    }

    public void setEventNote(String eventNote) {
        this.eventNote = eventNote;
    }

    public String getEventEntityName() {
        return eventEntityName;
    }

    public SystemEventsHistory eventEntityName(String eventEntityName) {
        this.eventEntityName = eventEntityName;
        return this;
    }

    public void setEventEntityName(String eventEntityName) {
        this.eventEntityName = eventEntityName;
    }

    public Long getEventEntityId() {
        return eventEntityId;
    }

    public SystemEventsHistory eventEntityId(Long eventEntityId) {
        this.eventEntityId = eventEntityId;
        return this;
    }

    public void setEventEntityId(Long eventEntityId) {
        this.eventEntityId = eventEntityId;
    }

    public Profile getTriggedBy() {
        return triggedBy;
    }

    public SystemEventsHistory triggedBy(Profile profile) {
        this.triggedBy = profile;
        return this;
    }

    public void setTriggedBy(Profile profile) {
        this.triggedBy = profile;
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
        SystemEventsHistory systemEventsHistory = (SystemEventsHistory) o;
        if (systemEventsHistory.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), systemEventsHistory.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SystemEventsHistory{" +
            "id=" + getId() +
            ", eventName='" + getEventName() + "'" +
            ", eventDate='" + getEventDate() + "'" +
            ", eventApi='" + getEventApi() + "'" +
            ", eventNote='" + getEventNote() + "'" +
            ", eventEntityName='" + getEventEntityName() + "'" +
            ", eventEntityId=" + getEventEntityId() +
            "}";
    }
}
