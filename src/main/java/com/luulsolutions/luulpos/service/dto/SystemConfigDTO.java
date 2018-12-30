package com.luulsolutions.luulpos.service.dto;

import java.io.Serializable;
import java.util.Objects;
import com.luulsolutions.luulpos.domain.enumeration.ConfigType;

/**
 * A DTO for the SystemConfig entity.
 */
public class SystemConfigDTO implements Serializable {

    private Long id;

    private String key;

    private String value;

    private ConfigType configurationType;

    private String note;

    private Boolean enabled;

    private Long shopId;

    private String shopShopName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ConfigType getConfigurationType() {
        return configurationType;
    }

    public void setConfigurationType(ConfigType configurationType) {
        this.configurationType = configurationType;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
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

        SystemConfigDTO systemConfigDTO = (SystemConfigDTO) o;
        if (systemConfigDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), systemConfigDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SystemConfigDTO{" +
            "id=" + getId() +
            ", key='" + getKey() + "'" +
            ", value='" + getValue() + "'" +
            ", configurationType='" + getConfigurationType() + "'" +
            ", note='" + getNote() + "'" +
            ", enabled='" + isEnabled() + "'" +
            ", shop=" + getShopId() +
            ", shop='" + getShopShopName() + "'" +
            "}";
    }
}
