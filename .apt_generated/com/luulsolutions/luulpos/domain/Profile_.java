package com.luulsolutions.luulpos.domain;

import com.luulsolutions.luulpos.domain.enumeration.Gender;
import com.luulsolutions.luulpos.domain.enumeration.ProfileStatus;
import com.luulsolutions.luulpos.domain.enumeration.ProfileType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Profile.class)
public abstract class Profile_ {

	public static volatile SingularAttribute<Profile, String> lastName;
	public static volatile SingularAttribute<Profile, String> fullPhotoUrl;
	public static volatile SingularAttribute<Profile, Shop> shop;
	public static volatile SingularAttribute<Profile, Gender> gender;
	public static volatile SingularAttribute<Profile, BigDecimal> hourlyPayRate;
	public static volatile SingularAttribute<Profile, ProfileStatus> profileStatus;
	public static volatile SingularAttribute<Profile, String> mobile;
	public static volatile SingularAttribute<Profile, Boolean> active;
	public static volatile SingularAttribute<Profile, LocalDate> dateOfBirth;
	public static volatile SingularAttribute<Profile, ZonedDateTime> lastAccess;
	public static volatile SingularAttribute<Profile, String> telephone;
	public static volatile SingularAttribute<Profile, String> thumbnailPhotoContentType;
	public static volatile SingularAttribute<Profile, String> firstName;
	public static volatile SingularAttribute<Profile, byte[]> fullPhoto;
	public static volatile SingularAttribute<Profile, String> thumbnailPhotoUrl;
	public static volatile SingularAttribute<Profile, byte[]> thumbnailPhoto;
	public static volatile SingularAttribute<Profile, ZonedDateTime> registerationDate;
	public static volatile SingularAttribute<Profile, Long> shopChangeId;
	public static volatile SingularAttribute<Profile, Long> id;
	public static volatile SingularAttribute<Profile, ProfileType> userType;
	public static volatile SingularAttribute<Profile, User> user;
	public static volatile SingularAttribute<Profile, String> email;
	public static volatile SingularAttribute<Profile, String> fullPhotoContentType;

}

