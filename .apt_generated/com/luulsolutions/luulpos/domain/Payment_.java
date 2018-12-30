package com.luulsolutions.luulpos.domain;

import com.luulsolutions.luulpos.domain.enumeration.PaymentStatus;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Payment.class)
public abstract class Payment_ {

	public static volatile SingularAttribute<Payment, BigDecimal> amount;
	public static volatile SingularAttribute<Payment, Shop> shop;
	public static volatile SingularAttribute<Payment, String> paymentProvider;
	public static volatile SingularAttribute<Payment, PaymentMethod> paymentMethod;
	public static volatile SingularAttribute<Payment, String> curency;
	public static volatile SingularAttribute<Payment, Long> id;
	public static volatile SingularAttribute<Payment, ZonedDateTime> paymentDate;
	public static volatile SingularAttribute<Payment, Profile> processedBy;
	public static volatile SingularAttribute<Payment, PaymentStatus> paymentStatus;
	public static volatile SingularAttribute<Payment, String> customerName;
	public static volatile SingularAttribute<Payment, Orders> order;

}

