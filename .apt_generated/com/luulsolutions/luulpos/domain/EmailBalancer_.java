package com.luulsolutions.luulpos.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(EmailBalancer.class)
public abstract class EmailBalancer_ {

	public static volatile SingularAttribute<EmailBalancer, String> relayId;
	public static volatile SingularAttribute<EmailBalancer, Integer> relayPort;
	public static volatile SingularAttribute<EmailBalancer, String> provider;
	public static volatile SingularAttribute<EmailBalancer, Integer> startingCount;
	public static volatile SingularAttribute<EmailBalancer, String> relayPassword;
	public static volatile SingularAttribute<EmailBalancer, Long> id;
	public static volatile SingularAttribute<EmailBalancer, Integer> endingCount;
	public static volatile SingularAttribute<EmailBalancer, Boolean> enabled;

}

