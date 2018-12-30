package com.luulsolutions.luulpos.domain;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ProductVariant.class)
public abstract class ProductVariant_ {

	public static volatile SingularAttribute<ProductVariant, byte[]> fullPhoto;
	public static volatile SingularAttribute<ProductVariant, String> fullPhotoUrl;
	public static volatile SingularAttribute<ProductVariant, String> thumbnailPhotoUrl;
	public static volatile SingularAttribute<ProductVariant, Product> product;
	public static volatile SingularAttribute<ProductVariant, byte[]> thumbnailPhoto;
	public static volatile SingularAttribute<ProductVariant, BigDecimal> price;
	public static volatile SingularAttribute<ProductVariant, Float> percentage;
	public static volatile SingularAttribute<ProductVariant, String> description;
	public static volatile SingularAttribute<ProductVariant, String> thumbnailPhotoContentType;
	public static volatile SingularAttribute<ProductVariant, Long> id;
	public static volatile SingularAttribute<ProductVariant, String> variantName;
	public static volatile SingularAttribute<ProductVariant, String> fullPhotoContentType;

}

