package com.luulsolutions.luulpos.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(SectionTable.class)
public abstract class SectionTable_ {

	public static volatile SingularAttribute<SectionTable, String> description;
	public static volatile SingularAttribute<SectionTable, Long> id;
	public static volatile SingularAttribute<SectionTable, ShopSection> shopSections;
	public static volatile SingularAttribute<SectionTable, Integer> tableNumber;

}

