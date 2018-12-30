package com.luulsolutions.luulpos.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ProductExtra.class)
public abstract class ProductExtra_ {

	public static volatile SingularAttribute<ProductExtra, byte[]> fullPhoto;
	public static volatile SingularAttribute<ProductExtra, String> fullPhotoUrl;
	public static volatile SingularAttribute<ProductExtra, String> thumbnailPhotoUrl;
	public static volatile SingularAttribute<ProductExtra, Product> product;
	public static volatile SingularAttribute<ProductExtra, byte[]> thumbnailPhoto;
	public static volatile SingularAttribute<ProductExtra, String> extraName;
	public static volatile SingularAttribute<ProductExtra, String> description;
	public static volatile SingularAttribute<ProductExtra, String> thumbnailPhotoContentType;
	public static volatile SingularAttribute<ProductExtra, Long> id;
	public static volatile SingularAttribute<ProductExtra, Float> extraValue;
	public static volatile SingularAttribute<ProductExtra, String> fullPhotoContentType;

}

