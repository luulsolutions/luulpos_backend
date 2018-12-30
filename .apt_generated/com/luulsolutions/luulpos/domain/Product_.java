package com.luulsolutions.luulpos.domain;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Product.class)
public abstract class Product_ {

	public static volatile SingularAttribute<Product, Integer> quantity;
	public static volatile SingularAttribute<Product, Shop> shop;
	public static volatile SingularAttribute<Product, String> productImageThumbUrl;
	public static volatile SingularAttribute<Product, byte[]> productImageFull;
	public static volatile SingularAttribute<Product, String> productImageThumbContentType;
	public static volatile SingularAttribute<Product, Boolean> active;
	public static volatile SetAttribute<Product, ProductExtra> extras;
	public static volatile SingularAttribute<Product, Tax> taxes;
	public static volatile SingularAttribute<Product, ProductType> productTypes;
	public static volatile SingularAttribute<Product, String> serialCode;
	public static volatile SingularAttribute<Product, Boolean> isVariantProduct;
	public static volatile SetAttribute<Product, ProductVariant> variants;
	public static volatile SingularAttribute<Product, String> productName;
	public static volatile SingularAttribute<Product, Integer> priorityPosition;
	public static volatile SingularAttribute<Product, ZonedDateTime> dateCreated;
	public static volatile SingularAttribute<Product, Discount> discounts;
	public static volatile SingularAttribute<Product, BigDecimal> price;
	public static volatile SingularAttribute<Product, Long> id;
	public static volatile SingularAttribute<Product, ProductCategory> category;
	public static volatile SingularAttribute<Product, String> productImageFullUrl;
	public static volatile SingularAttribute<Product, String> barcode;
	public static volatile SingularAttribute<Product, String> productDescription;
	public static volatile SingularAttribute<Product, String> productImageFullContentType;
	public static volatile SingularAttribute<Product, byte[]> productImageThumb;

}

