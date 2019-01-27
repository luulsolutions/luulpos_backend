package com.luulsolutions.luulpos.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.BigDecimalFilter;
import io.github.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the EmployeeTimesheet entity. This class is used in EmployeeTimesheetResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /employee-timesheets?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EmployeeTimesheetCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ZonedDateTimeFilter checkinTime;

    private ZonedDateTimeFilter checkOutTime;

    private IntegerFilter regularHoursWorked;

    private IntegerFilter overTimeHoursWorked;

    private BigDecimalFilter pay;

    private LongFilter profileId;

    private LongFilter shopId;

    public EmployeeTimesheetCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public ZonedDateTimeFilter getCheckinTime() {
        return checkinTime;
    }

    public void setCheckinTime(ZonedDateTimeFilter checkinTime) {
        this.checkinTime = checkinTime;
    }

    public ZonedDateTimeFilter getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(ZonedDateTimeFilter checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public IntegerFilter getRegularHoursWorked() {
        return regularHoursWorked;
    }

    public void setRegularHoursWorked(IntegerFilter regularHoursWorked) {
        this.regularHoursWorked = regularHoursWorked;
    }

    public IntegerFilter getOverTimeHoursWorked() {
        return overTimeHoursWorked;
    }

    public void setOverTimeHoursWorked(IntegerFilter overTimeHoursWorked) {
        this.overTimeHoursWorked = overTimeHoursWorked;
    }

    public BigDecimalFilter getPay() {
        return pay;
    }

    public void setPay(BigDecimalFilter pay) {
        this.pay = pay;
    }

    public LongFilter getProfileId() {
        return profileId;
    }

    public void setProfileId(LongFilter profileId) {
        this.profileId = profileId;
    }

    public LongFilter getShopId() {
        return shopId;
    }

    public void setShopId(LongFilter shopId) {
        this.shopId = shopId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EmployeeTimesheetCriteria that = (EmployeeTimesheetCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(checkinTime, that.checkinTime) &&
            Objects.equals(checkOutTime, that.checkOutTime) &&
            Objects.equals(regularHoursWorked, that.regularHoursWorked) &&
            Objects.equals(overTimeHoursWorked, that.overTimeHoursWorked) &&
            Objects.equals(pay, that.pay) &&
            Objects.equals(profileId, that.profileId) &&
            Objects.equals(shopId, that.shopId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        checkinTime,
        checkOutTime,
        regularHoursWorked,
        overTimeHoursWorked,
        pay,
        profileId,
        shopId
        );
    }

    @Override
    public String toString() {
        return "EmployeeTimesheetCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (checkinTime != null ? "checkinTime=" + checkinTime + ", " : "") +
                (checkOutTime != null ? "checkOutTime=" + checkOutTime + ", " : "") +
                (regularHoursWorked != null ? "regularHoursWorked=" + regularHoursWorked + ", " : "") +
                (overTimeHoursWorked != null ? "overTimeHoursWorked=" + overTimeHoursWorked + ", " : "") +
                (pay != null ? "pay=" + pay + ", " : "") +
                (profileId != null ? "profileId=" + profileId + ", " : "") +
                (shopId != null ? "shopId=" + shopId + ", " : "") +
            "}";
    }

}
