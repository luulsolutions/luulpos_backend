package com.luulsolutions.luulpos.domain;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(OrdersLineVariant.class)
public abstract class OrdersLineVariant_ {

	public static volatile SingularAttribute<OrdersLineVariant, String> fullPhotoUrl;
	public static volatile SingularAttribute<OrdersLineVariant, OrdersLine> ordersLine;
	public static volatile SingularAttribute<OrdersLineVariant, String> variantValue;
	public static volatile SingularAttribute<OrdersLineVariant, String> description;
	public static volatile SingularAttribute<OrdersLineVariant, String> thumbnailPhotoContentType;
	public static volatile SingularAttribute<OrdersLineVariant, byte[]> fullPhoto;
	public static volatile SingularAttribute<OrdersLineVariant, String> thumbnailPhotoUrl;
	public static volatile SingularAttribute<OrdersLineVariant, byte[]> thumbnailPhoto;
	public static volatile SingularAttribute<OrdersLineVariant, BigDecimal> price;
	public static volatile SingularAttribute<OrdersLineVariant, Float> percentage;
	public static volatile SetAttribute<OrdersLineVariant, OrdersLineExtra> ordersLineExtras;
	public static volatile SingularAttribute<OrdersLineVariant, Long> id;
	public static volatile SingularAttribute<OrdersLineVariant, String> variantName;
	public static volatile SingularAttribute<OrdersLineVariant, String> fullPhotoContentType;

}

