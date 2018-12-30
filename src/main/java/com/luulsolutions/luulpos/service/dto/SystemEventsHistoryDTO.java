package com.luulsolutions.luulpos.service.dto;

import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the SystemEventsHistory entity.
 */
public class SystemEventsHistoryDTO implements Serializable {

    private Long id;

    @NotNull
    private String eventName;

    private ZonedDateTime eventDate;

    private String eventApi;

    private String eventNote;

    private String eventEntityName;

    private Long eventEntityId;

    private Long triggedById;

    private String triggedByFirstName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public ZonedDateTime getEventDate() {
        return eventDate;
    }

    public void setEventDate(ZonedDateTime eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventApi() {
        return eventApi;
    }

    public void setEventApi(String eventApi) {
        this.eventApi = eventApi;
    }

    public String getEventNote() {
        return eventNote;
    }

    public void setEventNote(String eventNote) {
        this.eventNote = eventNote;
    }

    public String getEventEntityName() {
        return eventEntityName;
    }

    public void setEventEntityName(String eventEntityName) {
        this.eventEntityName = eventEntityName;
    }

    public Long getEventEntityId() {
        return eventEntityId;
    }

    public void setEventEntityId(Long eventEntityId) {
        this.eventEntityId = eventEntityId;
    }

    public Long getTriggedById() {
        return triggedById;
    }

    public void setTriggedById(Long profileId) {
        this.triggedById = profileId;
    }

    public String getTriggedByFirstName() {
        return triggedByFirstName;
    }

    public void setTriggedByFirstName(String profileFirstName) {
        this.triggedByFirstName = profileFirstName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SystemEventsHistoryDTO systemEventsHistoryDTO = (SystemEventsHistoryDTO) o;
        if (systemEventsHistoryDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), systemEventsHistoryDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SystemEventsHistoryDTO{" +
            "id=" + getId() +
            ", eventName='" + getEventName() + "'" +
            ", eventDate='" + getEventDate() + "'" +
            ", eventApi='" + getEventApi() + "'" +
            ", eventNote='" + getEventNote() + "'" +
            ", eventEntityName='" + getEventEntityName() + "'" +
            ", eventEntityId=" + getEventEntityId() +
            ", triggedBy=" + getTriggedById() +
            ", triggedBy='" + getTriggedByFirstName() + "'" +
            "}";
    }
}
