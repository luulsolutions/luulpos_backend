package com.luulsolutions.luulpos.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

import com.luulsolutions.luulpos.domain.enumeration.ConfigType;

/**
 * A SystemConfig.
 */
@Entity
@Table(name = "system_config")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "systemconfig")
public class SystemConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jhi_key")
    private String key;

    @Column(name = "jhi_value")
    private String value;

    @Enumerated(EnumType.STRING)
    @Column(name = "configuration_type")
    private ConfigType configurationType;

    @Column(name = "note")
    private String note;

    @Column(name = "enabled")
    private Boolean enabled;

    @ManyToOne
    @JsonIgnoreProperties("systemConfigs")
    private Shop shop;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public SystemConfig key(String key) {
        this.key = key;
        return this;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public SystemConfig value(String value) {
        this.value = value;
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ConfigType getConfigurationType() {
        return configurationType;
    }

    public SystemConfig configurationType(ConfigType configurationType) {
        this.configurationType = configurationType;
        return this;
    }

    public void setConfigurationType(ConfigType configurationType) {
        this.configurationType = configurationType;
    }

    public String getNote() {
        return note;
    }

    public SystemConfig note(String note) {
        this.note = note;
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public SystemConfig enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Shop getShop() {
        return shop;
    }

    public SystemConfig shop(Shop shop) {
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
        SystemConfig systemConfig = (SystemConfig) o;
        if (systemConfig.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), systemConfig.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SystemConfig{" +
            "id=" + getId() +
            ", key='" + getKey() + "'" +
            ", value='" + getValue() + "'" +
            ", configurationType='" + getConfigurationType() + "'" +
            ", note='" + getNote() + "'" +
            ", enabled='" + isEnabled() + "'" +
            "}";
    }
}
