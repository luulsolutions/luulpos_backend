package com.luulsolutions.luulpos.domain;

import com.luulsolutions.luulpos.domain.enumeration.SuspensionType;
import java.time.ZonedDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(SuspensionHistory.class)
public abstract class SuspensionHistory_ {

	public static volatile SingularAttribute<SuspensionHistory, SuspensionType> suspensionType;
	public static volatile SingularAttribute<SuspensionHistory, ZonedDateTime> unsuspensionDate;
	public static volatile SingularAttribute<SuspensionHistory, Profile> profile;
	public static volatile SingularAttribute<SuspensionHistory, String> suspendedReason;
	public static volatile SingularAttribute<SuspensionHistory, Long> id;
	public static volatile SingularAttribute<SuspensionHistory, Profile> suspendedBy;
	public static volatile SingularAttribute<SuspensionHistory, ZonedDateTime> suspendedDate;
	public static volatile SingularAttribute<SuspensionHistory, String> resolutionNote;

}

