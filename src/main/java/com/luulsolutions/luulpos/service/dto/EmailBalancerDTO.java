package com.luulsolutions.luulpos.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the EmailBalancer entity.
 */
public class EmailBalancerDTO implements Serializable {

    private Long id;

    private String relayId;

    private String relayPassword;

    private Integer startingCount;

    private Integer endingCount;

    private String provider;

    private Integer relayPort;

    private Boolean enabled;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRelayId() {
        return relayId;
    }

    public void setRelayId(String relayId) {
        this.relayId = relayId;
    }

    public String getRelayPassword() {
        return relayPassword;
    }

    public void setRelayPassword(String relayPassword) {
        this.relayPassword = relayPassword;
    }

    public Integer getStartingCount() {
        return startingCount;
    }

    public void setStartingCount(Integer startingCount) {
        this.startingCount = startingCount;
    }

    public Integer getEndingCount() {
        return endingCount;
    }

    public void setEndingCount(Integer endingCount) {
        this.endingCount = endingCount;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public Integer getRelayPort() {
        return relayPort;
    }

    public void setRelayPort(Integer relayPort) {
        this.relayPort = relayPort;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
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

        EmailBalancerDTO emailBalancerDTO = (EmailBalancerDTO) o;
        if (emailBalancerDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), emailBalancerDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EmailBalancerDTO{" +
            "id=" + getId() +
            ", relayId='" + getRelayId() + "'" +
            ", relayPassword='" + getRelayPassword() + "'" +
            ", startingCount=" + getStartingCount() +
            ", endingCount=" + getEndingCount() +
            ", provider='" + getProvider() + "'" +
            ", relayPort=" + getRelayPort() +
            ", enabled='" + isEnabled() + "'" +
            "}";
    }
}
