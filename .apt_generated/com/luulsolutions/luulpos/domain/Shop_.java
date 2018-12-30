package com.luulsolutions.luulpos.domain;

import com.luulsolutions.luulpos.domain.enumeration.ShopAccountType;
import java.time.ZonedDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Shop.class)
public abstract class Shop_ {

	public static volatile SingularAttribute<Shop, String> note;
	public static volatile SetAttribute<Shop, SystemConfig> systemConfigs;
	public static volatile SingularAttribute<Shop, ZonedDateTime> approvalDate;
	public static volatile SingularAttribute<Shop, String> address;
	public static volatile SingularAttribute<Shop, byte[]> shopLogo;
	public static volatile SingularAttribute<Shop, String> mobile;
	public static volatile SingularAttribute<Shop, Profile> approvedBy;
	public static volatile SetAttribute<Shop, Profile> profiles;
	public static volatile SingularAttribute<Shop, String> shopName;
	public static volatile SingularAttribute<Shop, String> description;
	public static volatile SingularAttribute<Shop, Boolean> active;
	public static volatile SetAttribute<Shop, Tax> taxes;
	public static volatile SingularAttribute<Shop, String> shopLogoUrl;
	public static volatile SetAttribute<Shop, ProductType> productTypes;
	public static volatile SingularAttribute<Shop, ShopAccountType> shopAccountType;
	public static volatile SetAttribute<Shop, ProductCategory> productCategories;
	public static volatile SingularAttribute<Shop, String> landland;
	public static volatile SingularAttribute<Shop, ZonedDateTime> createdDate;
	public static volatile SetAttribute<Shop, Discount> discounts;
	public static volatile SingularAttribute<Shop, String> shopLogoContentType;
	public static volatile SingularAttribute<Shop, String> currency;
	public static volatile SingularAttribute<Shop, Company> company;
	public static volatile SingularAttribute<Shop, Long> id;
	public static volatile SingularAttribute<Shop, String> email;

}

