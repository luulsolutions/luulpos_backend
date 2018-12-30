package com.luulsolutions.luulpos.domain;

import java.time.ZonedDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(SystemEventsHistory.class)
public abstract class SystemEventsHistory_ {

	public static volatile SingularAttribute<SystemEventsHistory, String> eventNote;
	public static volatile SingularAttribute<SystemEventsHistory, String> eventApi;
	public static volatile SingularAttribute<SystemEventsHistory, String> eventEntityName;
	public static volatile SingularAttribute<SystemEventsHistory, Profile> triggedBy;
	public static volatile SingularAttribute<SystemEventsHistory, Long> eventEntityId;
	public static volatile SingularAttribute<SystemEventsHistory, String> eventName;
	public static volatile SingularAttribute<SystemEventsHistory, Long> id;
	public static volatile SingularAttribute<SystemEventsHistory, ZonedDateTime> eventDate;

}

