package com.luulsolutions.luulpos.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PaymentMethodConfig.class)
public abstract class PaymentMethodConfig_ {

	public static volatile SingularAttribute<PaymentMethodConfig, String> note;
	public static volatile SingularAttribute<PaymentMethodConfig, PaymentMethod> paymentMethod;
	public static volatile SingularAttribute<PaymentMethodConfig, Long> id;
	public static volatile SingularAttribute<PaymentMethodConfig, String> value;
	public static volatile SingularAttribute<PaymentMethodConfig, String> key;
	public static volatile SingularAttribute<PaymentMethodConfig, Boolean> enabled;

}

