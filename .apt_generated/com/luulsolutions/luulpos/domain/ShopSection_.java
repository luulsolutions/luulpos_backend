package com.luulsolutions.luulpos.domain;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ShopSection.class)
public abstract class ShopSection_ {

	public static volatile SingularAttribute<ShopSection, String> sectionName;
	public static volatile SingularAttribute<ShopSection, BigDecimal> surchargeFlatAmount;
	public static volatile SingularAttribute<ShopSection, Integer> surchargePercentage;
	public static volatile SingularAttribute<ShopSection, Shop> shop;
	public static volatile SingularAttribute<ShopSection, Boolean> usePercentage;
	public static volatile SingularAttribute<ShopSection, String> description;
	public static volatile SingularAttribute<ShopSection, Long> id;

}

