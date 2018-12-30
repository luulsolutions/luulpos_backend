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
import io.github.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the SystemEventsHistory entity. This class is used in SystemEventsHistoryResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /system-events-histories?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SystemEventsHistoryCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter eventName;

    private ZonedDateTimeFilter eventDate;

    private StringFilter eventApi;

    private StringFilter eventNote;

    private StringFilter eventEntityName;

    private LongFilter eventEntityId;

    private LongFilter triggedById;

    public SystemEventsHistoryCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getEventName() {
        return eventName;
    }

    public void setEventName(StringFilter eventName) {
        this.eventName = eventName;
    }

    public ZonedDateTimeFilter getEventDate() {
        return eventDate;
    }

    public void setEventDate(ZonedDateTimeFilter eventDate) {
        this.eventDate = eventDate;
    }

    public StringFilter getEventApi() {
        return eventApi;
    }

    public void setEventApi(StringFilter eventApi) {
        this.eventApi = eventApi;
    }

    public StringFilter getEventNote() {
        return eventNote;
    }

    public void setEventNote(StringFilter eventNote) {
        this.eventNote = eventNote;
    }

    public StringFilter getEventEntityName() {
        return eventEntityName;
    }

    public void setEventEntityName(StringFilter eventEntityName) {
        this.eventEntityName = eventEntityName;
    }

    public LongFilter getEventEntityId() {
        return eventEntityId;
    }

    public void setEventEntityId(LongFilter eventEntityId) {
        this.eventEntityId = eventEntityId;
    }

    public LongFilter getTriggedById() {
        return triggedById;
    }

    public void setTriggedById(LongFilter triggedById) {
        this.triggedById = triggedById;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SystemEventsHistoryCriteria that = (SystemEventsHistoryCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(eventName, that.eventName) &&
            Objects.equals(eventDate, that.eventDate) &&
            Objects.equals(eventApi, that.eventApi) &&
            Objects.equals(eventNote, that.eventNote) &&
            Objects.equals(eventEntityName, that.eventEntityName) &&
            Objects.equals(eventEntityId, that.eventEntityId) &&
            Objects.equals(triggedById, that.triggedById);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        eventName,
        eventDate,
        eventApi,
        eventNote,
        eventEntityName,
        eventEntityId,
        triggedById
        );
    }

    @Override
    public String toString() {
        return "SystemEventsHistoryCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (eventName != null ? "eventName=" + eventName + ", " : "") +
                (eventDate != null ? "eventDate=" + eventDate + ", " : "") +
                (eventApi != null ? "eventApi=" + eventApi + ", " : "") +
                (eventNote != null ? "eventNote=" + eventNote + ", " : "") +
                (eventEntityName != null ? "eventEntityName=" + eventEntityName + ", " : "") +
                (eventEntityId != null ? "eventEntityId=" + eventEntityId + ", " : "") +
                (triggedById != null ? "triggedById=" + triggedById + ", " : "") +
            "}";
    }

}
