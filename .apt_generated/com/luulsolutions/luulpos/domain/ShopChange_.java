package com.luulsolutions.luulpos.domain;

import java.time.ZonedDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ShopChange.class)
public abstract class ShopChange_ {

	public static volatile SingularAttribute<ShopChange, String> note;
	public static volatile SingularAttribute<ShopChange, Shop> shop;
	public static volatile SingularAttribute<ShopChange, Profile> changedBy;
	public static volatile SingularAttribute<ShopChange, String> change;
	public static volatile SingularAttribute<ShopChange, ZonedDateTime> changeDate;
	public static volatile SingularAttribute<ShopChange, String> changedEntity;
	public static volatile SingularAttribute<ShopChange, Long> id;

}

