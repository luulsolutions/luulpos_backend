package com.luulsolutions.luulpos.domain;

import java.time.ZonedDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ShopDevice.class)
public abstract class ShopDevice_ {

	public static volatile SingularAttribute<ShopDevice, Shop> shop;
	public static volatile SingularAttribute<ShopDevice, ZonedDateTime> registeredDate;
	public static volatile SingularAttribute<ShopDevice, String> deviceModel;
	public static volatile SingularAttribute<ShopDevice, Long> id;
	public static volatile SingularAttribute<ShopDevice, String> deviceName;

}

