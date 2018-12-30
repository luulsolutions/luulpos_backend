package com.luulsolutions.luulpos.domain;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Discount.class)
public abstract class Discount_ {

	public static volatile SingularAttribute<Discount, BigDecimal> amount;
	public static volatile SingularAttribute<Discount, Shop> shop;
	public static volatile SingularAttribute<Discount, Float> percentage;
	public static volatile SingularAttribute<Discount, String> description;
	public static volatile SingularAttribute<Discount, Boolean> active;
	public static volatile SingularAttribute<Discount, Long> id;
	public static volatile SetAttribute<Discount, Product> products;

}

