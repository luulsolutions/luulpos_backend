package com.luulsolutions.luulpos.service.dto;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Lob;
import com.luulsolutions.luulpos.domain.enumeration.ProfileType;
import com.luulsolutions.luulpos.domain.enumeration.Gender;
import com.luulsolutions.luulpos.domain.enumeration.ProfileStatus;

/**
 * A DTO for the Profile entity.
 */
public class ProfileDTO implements Serializable {

    private Long id;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String email;

    @NotNull
    private ProfileType userType;

    private LocalDate dateOfBirth;

    private Gender gender;

    private ZonedDateTime registerationDate;

    private ZonedDateTime lastAccess;

    private ProfileStatus profileStatus;

    private String telephone;

    private String mobile;

    private BigDecimal hourlyPayRate;

    @Lob
    private byte[] thumbnailPhoto;
    private String thumbnailPhotoContentType;

    private String thumbnailPhotoUrl;

    @Lob
    private byte[] fullPhoto;
    private String fullPhotoContentType;

    private String fullPhotoUrl;

    private Boolean active;

    private Long shopChangeId;

    private Long userId;

    private String userFirstName;

    private Long shopId;

    private String shopShopName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ProfileType getUserType() {
        return userType;
    }

    public void setUserType(ProfileType userType) {
        this.userType = userType;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public ZonedDateTime getRegisterationDate() {
        return registerationDate;
    }

    public void setRegisterationDate(ZonedDateTime registerationDate) {
        this.registerationDate = registerationDate;
    }

    public ZonedDateTime getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(ZonedDateTime lastAccess) {
        this.lastAccess = lastAccess;
    }

    public ProfileStatus getProfileStatus() {
        return profileStatus;
    }

    public void setProfileStatus(ProfileStatus profileStatus) {
        this.profileStatus = profileStatus;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public BigDecimal getHourlyPayRate() {
        return hourlyPayRate;
    }

    public void setHourlyPayRate(BigDecimal hourlyPayRate) {
        this.hourlyPayRate = hourlyPayRate;
    }

    public byte[] getThumbnailPhoto() {
        return thumbnailPhoto;
    }

    public void setThumbnailPhoto(byte[] thumbnailPhoto) {
        this.thumbnailPhoto = thumbnailPhoto;
    }

    public String getThumbnailPhotoContentType() {
        return thumbnailPhotoContentType;
    }

    public void setThumbnailPhotoContentType(String thumbnailPhotoContentType) {
        this.thumbnailPhotoContentType = thumbnailPhotoContentType;
    }

    public String getThumbnailPhotoUrl() {
        return thumbnailPhotoUrl;
    }

    public void setThumbnailPhotoUrl(String thumbnailPhotoUrl) {
        this.thumbnailPhotoUrl = thumbnailPhotoUrl;
    }

    public byte[] getFullPhoto() {
        return fullPhoto;
    }

    public void setFullPhoto(byte[] fullPhoto) {
        this.fullPhoto = fullPhoto;
    }

    public String getFullPhotoContentType() {
        return fullPhotoContentType;
    }

    public void setFullPhotoContentType(String fullPhotoContentType) {
        this.fullPhotoContentType = fullPhotoContentType;
    }

    public String getFullPhotoUrl() {
        return fullPhotoUrl;
    }

    public void setFullPhotoUrl(String fullPhotoUrl) {
        this.fullPhotoUrl = fullPhotoUrl;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Long getShopChangeId() {
        return shopChangeId;
    }

    public void setShopChangeId(Long shopChangeId) {
        this.shopChangeId = shopChangeId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getShopShopName() {
        return shopShopName;
    }

    public void setShopShopName(String shopShopName) {
        this.shopShopName = shopShopName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProfileDTO profileDTO = (ProfileDTO) o;
        if (profileDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), profileDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProfileDTO{" +
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
            ", thumbnailPhotoUrl='" + getThumbnailPhotoUrl() + "'" +
            ", fullPhoto='" + getFullPhoto() + "'" +
            ", fullPhotoUrl='" + getFullPhotoUrl() + "'" +
            ", active='" + isActive() + "'" +
            ", shopChangeId=" + getShopChangeId() +
            ", user=" + getUserId() +
            ", user='" + getUserFirstName() + "'" +
            ", shop=" + getShopId() +
            ", shop='" + getShopShopName() + "'" +
            "}";
    }
}
