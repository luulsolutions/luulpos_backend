package com.luulsolutions.luulpos.domain;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Tax.class)
public abstract class Tax_ {

	public static volatile SingularAttribute<Tax, BigDecimal> amount;
	public static volatile SingularAttribute<Tax, Shop> shop;
	public static volatile SingularAttribute<Tax, Float> percentage;
	public static volatile SingularAttribute<Tax, String> description;
	public static volatile SingularAttribute<Tax, Boolean> active;
	public static volatile SingularAttribute<Tax, Long> id;
	public static volatile SetAttribute<Tax, Product> products;

}

