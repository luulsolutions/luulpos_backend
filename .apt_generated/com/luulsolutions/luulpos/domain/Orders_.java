package com.luulsolutions.luulpos.domain;

import com.luulsolutions.luulpos.domain.enumeration.OrderStatus;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Orders.class)
public abstract class Orders_ {

	public static volatile SingularAttribute<Orders, String> note;
	public static volatile SingularAttribute<Orders, Integer> quantity;
	public static volatile SingularAttribute<Orders, Shop> shop;
	public static volatile SingularAttribute<Orders, BigDecimal> totalPrice;
	public static volatile SingularAttribute<Orders, Float> taxPercentage;
	public static volatile SingularAttribute<Orders, Profile> soldBy;
	public static volatile SingularAttribute<Orders, String> description;
	public static volatile SingularAttribute<Orders, BigDecimal> discountAmount;
	public static volatile SingularAttribute<Orders, OrderStatus> orderStatus;
	public static volatile SingularAttribute<Orders, SectionTable> sectionTable;
	public static volatile SingularAttribute<Orders, String> customerName;
	public static volatile SetAttribute<Orders, OrdersLine> ordersLines;
	public static volatile SingularAttribute<Orders, Float> discountPercentage;
	public static volatile SingularAttribute<Orders, Profile> preparedBy;
	public static volatile SingularAttribute<Orders, PaymentMethod> paymentMethod;
	public static volatile SingularAttribute<Orders, Long> id;
	public static volatile SingularAttribute<Orders, ShopDevice> shopDevice;
	public static volatile SingularAttribute<Orders, BigDecimal> taxAmount;
	public static volatile SingularAttribute<Orders, Boolean> isVariantOrder;
	public static volatile SingularAttribute<Orders, ZonedDateTime> orderDate;

}

