package com.luulsolutions.luulpos.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(OrdersLineExtra.class)
public abstract class OrdersLineExtra_ {

	public static volatile SingularAttribute<OrdersLineExtra, byte[]> fullPhoto;
	public static volatile SingularAttribute<OrdersLineExtra, String> fullPhotoUrl;
	public static volatile SingularAttribute<OrdersLineExtra, String> thumbnailPhotoUrl;
	public static volatile SingularAttribute<OrdersLineExtra, byte[]> thumbnailPhoto;
	public static volatile SingularAttribute<OrdersLineExtra, String> ordersOptionDescription;
	public static volatile SingularAttribute<OrdersLineExtra, Float> ordersLineExtraPrice;
	public static volatile SingularAttribute<OrdersLineExtra, String> thumbnailPhotoContentType;
	public static volatile SingularAttribute<OrdersLineExtra, Long> id;
	public static volatile SingularAttribute<OrdersLineExtra, OrdersLineVariant> ordersLineVariant;
	public static volatile SingularAttribute<OrdersLineExtra, String> ordersLineExtraName;
	public static volatile SingularAttribute<OrdersLineExtra, String> ordersLineExtraValue;
	public static volatile SingularAttribute<OrdersLineExtra, String> fullPhotoContentType;

}

