package com.luulsolutions.luulpos.service.dto;

import java.io.Serializable;
import java.util.Objects;
import com.luulsolutions.luulpos.domain.enumeration.ConfigType;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the SystemConfig entity. This class is used in SystemConfigResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /system-configs?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SystemConfigCriteria implements Serializable {
    /**
     * Class for filtering ConfigType
     */
    public static class ConfigTypeFilter extends Filter<ConfigType> {
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter key;

    private StringFilter value;

    private ConfigTypeFilter configurationType;

    private StringFilter note;

    private BooleanFilter enabled;

    private LongFilter shopId;

    public SystemConfigCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getKey() {
        return key;
    }

    public void setKey(StringFilter key) {
        this.key = key;
    }

    public StringFilter getValue() {
        return value;
    }

    public void setValue(StringFilter value) {
        this.value = value;
    }

    public ConfigTypeFilter getConfigurationType() {
        return configurationType;
    }

    public void setConfigurationType(ConfigTypeFilter configurationType) {
        this.configurationType = configurationType;
    }

    public StringFilter getNote() {
        return note;
    }

    public void setNote(StringFilter note) {
        this.note = note;
    }

    public BooleanFilter getEnabled() {
        return enabled;
    }

    public void setEnabled(BooleanFilter enabled) {
        this.enabled = enabled;
    }

    public LongFilter getShopId() {
        return shopId;
    }

    public void setShopId(LongFilter shopId) {
        this.shopId = shopId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SystemConfigCriteria that = (SystemConfigCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(key, that.key) &&
            Objects.equals(value, that.value) &&
            Objects.equals(configurationType, that.configurationType) &&
            Objects.equals(note, that.note) &&
            Objects.equals(enabled, that.enabled) &&
            Objects.equals(shopId, that.shopId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        key,
        value,
        configurationType,
        note,
        enabled,
        shopId
        );
    }

    @Override
    public String toString() {
        return "SystemConfigCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (key != null ? "key=" + key + ", " : "") +
                (value != null ? "value=" + value + ", " : "") +
                (configurationType != null ? "configurationType=" + configurationType + ", " : "") +
                (note != null ? "note=" + note + ", " : "") +
                (enabled != null ? "enabled=" + enabled + ", " : "") +
                (shopId != null ? "shopId=" + shopId + ", " : "") +
            "}";
    }

}
