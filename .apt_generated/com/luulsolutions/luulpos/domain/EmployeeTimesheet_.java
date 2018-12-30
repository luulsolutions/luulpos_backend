package com.luulsolutions.luulpos.domain;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(EmployeeTimesheet.class)
public abstract class EmployeeTimesheet_ {

	public static volatile SingularAttribute<EmployeeTimesheet, ZonedDateTime> checkinTime;
	public static volatile SingularAttribute<EmployeeTimesheet, Integer> regularHoursWorked;
	public static volatile SingularAttribute<EmployeeTimesheet, Shop> shop;
	public static volatile SingularAttribute<EmployeeTimesheet, ZonedDateTime> checkOutTime;
	public static volatile SingularAttribute<EmployeeTimesheet, Integer> overTimeHoursWorked;
	public static volatile SingularAttribute<EmployeeTimesheet, Profile> profile;
	public static volatile SingularAttribute<EmployeeTimesheet, BigDecimal> pay;
	public static volatile SingularAttribute<EmployeeTimesheet, Long> id;

}

