package com.luulsolutions.luulpos.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the SectionTable entity.
 */
public class SectionTableDTO implements Serializable {

    private Long id;

    private Integer tableNumber;

    private String description;

    private Long shopSectionsId;

    private String shopSectionsSectionName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(Integer tableNumber) {
        this.tableNumber = tableNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getShopSectionsId() {
        return shopSectionsId;
    }

    public void setShopSectionsId(Long shopSectionId) {
        this.shopSectionsId = shopSectionId;
    }

    public String getShopSectionsSectionName() {
        return shopSectionsSectionName;
    }

    public void setShopSectionsSectionName(String shopSectionSectionName) {
        this.shopSectionsSectionName = shopSectionSectionName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SectionTableDTO sectionTableDTO = (SectionTableDTO) o;
        if (sectionTableDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sectionTableDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SectionTableDTO{" +
            "id=" + getId() +
            ", tableNumber=" + getTableNumber() +
            ", description='" + getDescription() + "'" +
            ", shopSections=" + getShopSectionsId() +
            ", shopSections='" + getShopSectionsSectionName() + "'" +
            "}";
    }
}
