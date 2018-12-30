package com.luulsolutions.luulpos.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PaymentMethod.class)
public abstract class PaymentMethod_ {

	public static volatile SingularAttribute<PaymentMethod, Shop> shop;
	public static volatile SingularAttribute<PaymentMethod, String> paymentMethod;
	public static volatile SingularAttribute<PaymentMethod, String> description;
	public static volatile SingularAttribute<PaymentMethod, Boolean> active;
	public static volatile SingularAttribute<PaymentMethod, Long> id;

}

