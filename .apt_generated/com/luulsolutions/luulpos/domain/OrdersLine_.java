package com.luulsolutions.luulpos.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(OrdersLine.class)
public abstract class OrdersLine_ {

	public static volatile SingularAttribute<OrdersLine, String> ordersLineName;
	public static volatile SingularAttribute<OrdersLine, Float> ordersLinePrice;
	public static volatile SingularAttribute<OrdersLine, String> fullPhotoUrl;
	public static volatile SingularAttribute<OrdersLine, Product> product;
	public static volatile SingularAttribute<OrdersLine, String> ordersLineValue;
	public static volatile SingularAttribute<OrdersLine, String> ordersLineDescription;
	public static volatile SingularAttribute<OrdersLine, String> thumbnailPhotoContentType;
	public static volatile SetAttribute<OrdersLine, OrdersLineVariant> ordersLineVariants;
	public static volatile SingularAttribute<OrdersLine, byte[]> fullPhoto;
	public static volatile SingularAttribute<OrdersLine, String> thumbnailPhotoUrl;
	public static volatile SingularAttribute<OrdersLine, byte[]> thumbnailPhoto;
	public static volatile SingularAttribute<OrdersLine, Orders> orders;
	public static volatile SingularAttribute<OrdersLine, Long> id;
	public static volatile SingularAttribute<OrdersLine, String> fullPhotoContentType;

}

