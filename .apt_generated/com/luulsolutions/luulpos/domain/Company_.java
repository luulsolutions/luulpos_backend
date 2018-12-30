package com.luulsolutions.luulpos.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Company.class)
public abstract class Company_ {

	public static volatile SingularAttribute<Company, String> note;
	public static volatile SingularAttribute<Company, String> companyLogoUrl;
	public static volatile SingularAttribute<Company, byte[]> companyLogo;
	public static volatile SingularAttribute<Company, String> companyName;
	public static volatile SingularAttribute<Company, String> description;
	public static volatile SingularAttribute<Company, Boolean> active;
	public static volatile SingularAttribute<Company, Long> id;
	public static volatile SingularAttribute<Company, String> companyLogoContentType;

}

