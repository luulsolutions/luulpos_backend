package com.luulsolutions.luulpos.domain;

import com.luulsolutions.luulpos.domain.enumeration.ConfigType;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(SystemConfig.class)
public abstract class SystemConfig_ {

	public static volatile SingularAttribute<SystemConfig, String> note;
	public static volatile SingularAttribute<SystemConfig, Shop> shop;
	public static volatile SingularAttribute<SystemConfig, Long> id;
	public static volatile SingularAttribute<SystemConfig, ConfigType> configurationType;
	public static volatile SingularAttribute<SystemConfig, String> value;
	public static volatile SingularAttribute<SystemConfig, String> key;
	public static volatile SingularAttribute<SystemConfig, Boolean> enabled;

}

