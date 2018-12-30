package com.luulsolutions.luulpos.repository.timezone;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(DateTimeWrapper.class)
public abstract class DateTimeWrapper_ {

	public static volatile SingularAttribute<DateTimeWrapper, LocalDateTime> localDateTime;
	public static volatile SingularAttribute<DateTimeWrapper, LocalTime> localTime;
	public static volatile SingularAttribute<DateTimeWrapper, ZonedDateTime> zonedDateTime;
	public static volatile SingularAttribute<DateTimeWrapper, OffsetTime> offsetTime;
	public static volatile SingularAttribute<DateTimeWrapper, OffsetDateTime> offsetDateTime;
	public static volatile SingularAttribute<DateTimeWrapper, Long> id;
	public static volatile SingularAttribute<DateTimeWrapper, LocalDate> localDate;
	public static volatile SingularAttribute<DateTimeWrapper, Instant> instant;

}

