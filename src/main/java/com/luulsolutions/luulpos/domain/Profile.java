package com.luulsolutions.luulpos.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

import com.luulsolutions.luulpos.domain.enumeration.ProfileType;

import com.luulsolutions.luulpos.domain.enumeration.Gender;

import com.luulsolutions.luulpos.domain.enumeration.ProfileStatus;

/**
 * A Profile.
 */
@Entity
@Table(name = "profile")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "profile")
public class Profile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false)
    private ProfileType userType;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "registeration_date")
    private ZonedDateTime registerationDate;

    @Column(name = "last_access")
    private ZonedDateTime lastAccess;

    @Enumerated(EnumType.STRING)
    @Column(name = "profile_status")
    private ProfileStatus profileStatus;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "hourly_pay_rate", precision = 10, scale = 2)
    private BigDecimal hourlyPayRate;

    @Lob
    @Column(name = "thumbnail_photo")
    private byte[] thumbnailPhoto;

    @Column(name = "thumbnail_photo_content_type")
    private String thumbnailPhotoContentType;

    @Column(name = "thumbnail_photo_url")
    private String thumbnailPhotoUrl;

    @Lob
    @Column(name = "full_photo")
    private byte[] fullPhoto;

    @Column(name = "full_photo_content_type")
    private String fullPhotoContentType;

    @Column(name = "full_photo_url")
    private String fullPhotoUrl;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "shop_change_id")
    private Long shopChangeId;

    @OneToOne    @JoinColumn(unique = true)
    private User user;

    @ManyToOne
    @JsonIgnoreProperties("profiles")
    private Shop shop;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public Profile firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Profile lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public Profile email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ProfileType getUserType() {
        return userType;
    }

    public Profile userType(ProfileType userType) {
        this.userType = userType;
        return this;
    }

    public void setUserType(ProfileType userType) {
        this.userType = userType;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public Profile dateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Gender getGender() {
        return gender;
    }

    public Profile gender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public ZonedDateTime getRegisterationDate() {
        return registerationDate;
    }

    public Profile registerationDate(ZonedDateTime registerationDate) {
        this.registerationDate = registerationDate;
        return this;
    }

    public void setRegisterationDate(ZonedDateTime registerationDate) {
        this.registerationDate = registerationDate;
    }

    public ZonedDateTime getLastAccess() {
        return lastAccess;
    }

    public Profile lastAccess(ZonedDateTime lastAccess) {
        this.lastAccess = lastAccess;
        return this;
    }

    public void setLastAccess(ZonedDateTime lastAccess) {
        this.lastAccess = lastAccess;
    }

    public ProfileStatus getProfileStatus() {
        return profileStatus;
    }

    public Profile profileStatus(ProfileStatus profileStatus) {
        this.profileStatus = profileStatus;
        return this;
    }

    public void setProfileStatus(ProfileStatus profileStatus) {
        this.profileStatus = profileStatus;
    }

    public String getTelephone() {
        return telephone;
    }

    public Profile telephone(String telephone) {
        this.telephone = telephone;
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getMobile() {
        return mobile;
    }

    public Profile mobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public BigDecimal getHourlyPayRate() {
        return hourlyPayRate;
    }

    public Profile hourlyPayRate(BigDecimal hourlyPayRate) {
        this.hourlyPayRate = hourlyPayRate;
        return this;
    }

    public void setHourlyPayRate(BigDecimal hourlyPayRate) {
        this.hourlyPayRate = hourlyPayRate;
    }

    public byte[] getThumbnailPhoto() {
        return thumbnailPhoto;
    }

    public Profile thumbnailPhoto(byte[] thumbnailPhoto) {
        this.thumbnailPhoto = thumbnailPhoto;
        return this;
    }

    public void setThumbnailPhoto(byte[] thumbnailPhoto) {
        this.thumbnailPhoto = thumbnailPhoto;
    }

    public String getThumbnailPhotoContentType() {
        return thumbnailPhotoContentType;
    }

    public Profile thumbnailPhotoContentType(String thumbnailPhotoContentType) {
        this.thumbnailPhotoContentType = thumbnailPhotoContentType;
        return this;
    }

    public void setThumbnailPhotoContentType(String thumbnailPhotoContentType) {
        this.thumbnailPhotoContentType = thumbnailPhotoContentType;
    }

    public String getThumbnailPhotoUrl() {
        return thumbnailPhotoUrl;
    }

    public Profile thumbnailPhotoUrl(String thumbnailPhotoUrl) {
        this.thumbnailPhotoUrl = thumbnailPhotoUrl;
        return this;
    }

    public void setThumbnailPhotoUrl(String thumbnailPhotoUrl) {
        this.thumbnailPhotoUrl = thumbnailPhotoUrl;
    }

    public byte[] getFullPhoto() {
        return fullPhoto;
    }

    public Profile fullPhoto(byte[] fullPhoto) {
        this.fullPhoto = fullPhoto;
        return this;
    }

    public void setFullPhoto(byte[] fullPhoto) {
        this.fullPhoto = fullPhoto;
    }

    public String getFullPhotoContentType() {
        return fullPhotoContentType;
    }

    public Profile fullPhotoContentType(String fullPhotoContentType) {
        this.fullPhotoContentType = fullPhotoContentType;
        return this;
    }

    public void setFullPhotoContentType(String fullPhotoContentType) {
        this.fullPhotoContentType = fullPhotoContentType;
    }

    public String getFullPhotoUrl() {
        return fullPhotoUrl;
    }

    public Profile fullPhotoUrl(String fullPhotoUrl) {
        this.fullPhotoUrl = fullPhotoUrl;
        return this;
    }

    public void setFullPhotoUrl(String fullPhotoUrl) {
        this.fullPhotoUrl = fullPhotoUrl;
    }

    public Boolean isActive() {
        return active;
    }

    public Profile active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Long getShopChangeId() {
        return shopChangeId;
    }

    public Profile shopChangeId(Long shopChangeId) {
        this.shopChangeId = shopChangeId;
        return this;
    }

    public void setShopChangeId(Long shopChangeId) {
        this.shopChangeId = shopChangeId;
    }

    public User getUser() {
        return user;
    }

    public Profile user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Shop getShop() {
        return shop;
    }

    public Profile shop(Shop shop) {
        this.shop = shop;
        return this;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Profile profile = (Profile) o;
        if (profile.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), profile.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Profile{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", email='" + getEmail() + "'" +
            ", userType='" + getUserType() + "'" +
            ", dateOfBirth='" + getDateOfBirth() + "'" +
            ", gender='" + getGender() + "'" +
            ", registerationDate='" + getRegisterationDate() + "'" +
            ", lastAccess='" + getLastAccess() + "'" +
            ", profileStatus='" + getProfileStatus() + "'" +
            ", telephone='" + getTelephone() + "'" +
            ", mobile='" + getMobile() + "'" +
            ", hourlyPayRate=" + getHourlyPayRate() +
            ", thumbnailPhoto='" + getThumbnailPhoto() + "'" +
            ", thumbnailPhotoContentType='" + getThumbnailPhotoContentType() + "'" +
            ", thumbnailPhotoUrl='" + getThumbnailPhotoUrl() + "'" +
            ", fullPhoto='" + getFullPhoto() + "'" +
            ", fullPhotoContentType='" + getFullPhotoContentType() + "'" +
            ", fullPhotoUrl='" + getFullPhotoUrl() + "'" +
            ", active='" + isActive() + "'" +
            ", shopChangeId=" + getShopChangeId() +
            "}";
    }
}
