package com.luulsolutions.luulpos.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ProductCategory.class)
public abstract class ProductCategory_ {

	public static volatile SingularAttribute<ProductCategory, String> imageThumbUrl;
	public static volatile SingularAttribute<ProductCategory, String> imageThumbContentType;
	public static volatile SingularAttribute<ProductCategory, Shop> shop;
	public static volatile SingularAttribute<ProductCategory, byte[]> imageFull;
	public static volatile SingularAttribute<ProductCategory, String> description;
	public static volatile SingularAttribute<ProductCategory, String> imageFullUrl;
	public static volatile SingularAttribute<ProductCategory, Long> id;
	public static volatile SingularAttribute<ProductCategory, byte[]> imageThumb;
	public static volatile SingularAttribute<ProductCategory, String> category;
	public static volatile SingularAttribute<ProductCategory, String> imageFullContentType;
	public static volatile SetAttribute<ProductCategory, Product> products;

}

