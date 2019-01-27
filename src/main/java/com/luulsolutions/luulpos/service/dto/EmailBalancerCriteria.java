package com.luulsolutions.luulpos.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the EmailBalancer entity. This class is used in EmailBalancerResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /email-balancers?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EmailBalancerCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter relayId;

    private StringFilter relayPassword;

    private IntegerFilter startingCount;

    private IntegerFilter endingCount;

    private StringFilter provider;

    private IntegerFilter relayPort;

    private BooleanFilter enabled;

    public EmailBalancerCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getRelayId() {
        return relayId;
    }

    public void setRelayId(StringFilter relayId) {
        this.relayId = relayId;
    }

    public StringFilter getRelayPassword() {
        return relayPassword;
    }

    public void setRelayPassword(StringFilter relayPassword) {
        this.relayPassword = relayPassword;
    }

    public IntegerFilter getStartingCount() {
        return startingCount;
    }

    public void setStartingCount(IntegerFilter startingCount) {
        this.startingCount = startingCount;
    }

    public IntegerFilter getEndingCount() {
        return endingCount;
    }

    public void setEndingCount(IntegerFilter endingCount) {
        this.endingCount = endingCount;
    }

    public StringFilter getProvider() {
        return provider;
    }

    public void setProvider(StringFilter provider) {
        this.provider = provider;
    }

    public IntegerFilter getRelayPort() {
        return relayPort;
    }

    public void setRelayPort(IntegerFilter relayPort) {
        this.relayPort = relayPort;
    }

    public BooleanFilter getEnabled() {
        return enabled;
    }

    public void setEnabled(BooleanFilter enabled) {
        this.enabled = enabled;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EmailBalancerCriteria that = (EmailBalancerCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(relayId, that.relayId) &&
            Objects.equals(relayPassword, that.relayPassword) &&
            Objects.equals(startingCount, that.startingCount) &&
            Objects.equals(endingCount, that.endingCount) &&
            Objects.equals(provider, that.provider) &&
            Objects.equals(relayPort, that.relayPort) &&
            Objects.equals(enabled, that.enabled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        relayId,
        relayPassword,
        startingCount,
        endingCount,
        provider,
        relayPort,
        enabled
        );
    }

    @Override
    public String toString() {
        return "EmailBalancerCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (relayId != null ? "relayId=" + relayId + ", " : "") +
                (relayPassword != null ? "relayPassword=" + relayPassword + ", " : "") +
                (startingCount != null ? "startingCount=" + startingCount + ", " : "") +
                (endingCount != null ? "endingCount=" + endingCount + ", " : "") +
                (provider != null ? "provider=" + provider + ", " : "") +
                (relayPort != null ? "relayPort=" + relayPort + ", " : "") +
                (enabled != null ? "enabled=" + enabled + ", " : "") +
            "}";
    }

}
