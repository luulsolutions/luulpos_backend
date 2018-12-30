package com.luulsolutions.luulpos.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A EmployeeTimesheet.
 */
@Entity
@Table(name = "employee_timesheet")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "employeetimesheet")
public class EmployeeTimesheet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "checkin_time")
    private ZonedDateTime checkinTime;

    @Column(name = "check_out_time")
    private ZonedDateTime checkOutTime;

    @Column(name = "regular_hours_worked")
    private Integer regularHoursWorked;

    @Column(name = "over_time_hours_worked")
    private Integer overTimeHoursWorked;

    @Column(name = "pay", precision = 10, scale = 2)
    private BigDecimal pay;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Profile profile;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Shop shop;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCheckinTime() {
        return checkinTime;
    }

    public EmployeeTimesheet checkinTime(ZonedDateTime checkinTime) {
        this.checkinTime = checkinTime;
        return this;
    }

    public void setCheckinTime(ZonedDateTime checkinTime) {
        this.checkinTime = checkinTime;
    }

    public ZonedDateTime getCheckOutTime() {
        return checkOutTime;
    }

    public EmployeeTimesheet checkOutTime(ZonedDateTime checkOutTime) {
        this.checkOutTime = checkOutTime;
        return this;
    }

    public void setCheckOutTime(ZonedDateTime checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public Integer getRegularHoursWorked() {
        return regularHoursWorked;
    }

    public EmployeeTimesheet regularHoursWorked(Integer regularHoursWorked) {
        this.regularHoursWorked = regularHoursWorked;
        return this;
    }

    public void setRegularHoursWorked(Integer regularHoursWorked) {
        this.regularHoursWorked = regularHoursWorked;
    }

    public Integer getOverTimeHoursWorked() {
        return overTimeHoursWorked;
    }

    public EmployeeTimesheet overTimeHoursWorked(Integer overTimeHoursWorked) {
        this.overTimeHoursWorked = overTimeHoursWorked;
        return this;
    }

    public void setOverTimeHoursWorked(Integer overTimeHoursWorked) {
        this.overTimeHoursWorked = overTimeHoursWorked;
    }

    public BigDecimal getPay() {
        return pay;
    }

    public EmployeeTimesheet pay(BigDecimal pay) {
        this.pay = pay;
        return this;
    }

    public void setPay(BigDecimal pay) {
        this.pay = pay;
    }

    public Profile getProfile() {
        return profile;
    }

    public EmployeeTimesheet profile(Profile profile) {
        this.profile = profile;
        return this;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Shop getShop() {
        return shop;
    }

    public EmployeeTimesheet shop(Shop shop) {
        this.shop = shop;
        return this;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
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
        EmployeeTimesheet employeeTimesheet = (EmployeeTimesheet) o;
        if (employeeTimesheet.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), employeeTimesheet.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EmployeeTimesheet{" +
            "id=" + getId() +
            ", checkinTime='" + getCheckinTime() + "'" +
            ", checkOutTime='" + getCheckOutTime() + "'" +
            ", regularHoursWorked=" + getRegularHoursWorked() +
            ", overTimeHoursWorked=" + getOverTimeHoursWorked() +
            ", pay=" + getPay() +
            "}";
    }
}
