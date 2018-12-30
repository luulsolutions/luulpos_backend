package com.luulsolutions.luulpos.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the Company entity.
 */
public class CompanyDTO implements Serializable {

    private Long id;

    @NotNull
    private String companyName;

    private String description;

    private String note;

    @Lob
    private byte[] companyLogo;
    private String companyLogoContentType;

    private String companyLogoUrl;

    private Boolean active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public byte[] getCompanyLogo() {
        return companyLogo;
    }

    public void setCompanyLogo(byte[] companyLogo) {
        this.companyLogo = companyLogo;
    }

    public String getCompanyLogoContentType() {
        return companyLogoContentType;
    }

    public void setCompanyLogoContentType(String companyLogoContentType) {
        this.companyLogoContentType = companyLogoContentType;
    }

    public String getCompanyLogoUrl() {
        return companyLogoUrl;
    }

    public void setCompanyLogoUrl(String companyLogoUrl) {
        this.companyLogoUrl = companyLogoUrl;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CompanyDTO companyDTO = (CompanyDTO) o;
        if (companyDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), companyDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CompanyDTO{" +
            "id=" + getId() +
            ", companyName='" + getCompanyName() + "'" +
            ", description='" + getDescription() + "'" +
            ", note='" + getNote() + "'" +
            ", companyLogo='" + getCompanyLogo() + "'" +
            ", companyLogoUrl='" + getCompanyLogoUrl() + "'" +
            ", active='" + isActive() + "'" +
            "}";
    }
}
