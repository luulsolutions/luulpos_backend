package com.luulsolutions.luulpos.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A EmailBalancer.
 */
@Entity
@Table(name = "email_balancer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "emailbalancer")
public class EmailBalancer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "relay_id")
    private String relayId;

    @Column(name = "relay_password")
    private String relayPassword;

    @Column(name = "starting_count")
    private Integer startingCount;

    @Column(name = "ending_count")
    private Integer endingCount;

    @Column(name = "provider")
    private String provider;

    @Column(name = "relay_port")
    private Integer relayPort;

    @Column(name = "enabled")
    private Boolean enabled;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRelayId() {
        return relayId;
    }

    public EmailBalancer relayId(String relayId) {
        this.relayId = relayId;
        return this;
    }

    public void setRelayId(String relayId) {
        this.relayId = relayId;
    }

    public String getRelayPassword() {
        return relayPassword;
    }

    public EmailBalancer relayPassword(String relayPassword) {
        this.relayPassword = relayPassword;
        return this;
    }

    public void setRelayPassword(String relayPassword) {
        this.relayPassword = relayPassword;
    }

    public Integer getStartingCount() {
        return startingCount;
    }

    public EmailBalancer startingCount(Integer startingCount) {
        this.startingCount = startingCount;
        return this;
    }

    public void setStartingCount(Integer startingCount) {
        this.startingCount = startingCount;
    }

    public Integer getEndingCount() {
        return endingCount;
    }

    public EmailBalancer endingCount(Integer endingCount) {
        this.endingCount = endingCount;
        return this;
    }

    public void setEndingCount(Integer endingCount) {
        this.endingCount = endingCount;
    }

    public String getProvider() {
        return provider;
    }

    public EmailBalancer provider(String provider) {
        this.provider = provider;
        return this;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public Integer getRelayPort() {
        return relayPort;
    }

    public EmailBalancer relayPort(Integer relayPort) {
        this.relayPort = relayPort;
        return this;
    }

    public void setRelayPort(Integer relayPort) {
        this.relayPort = relayPort;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public EmailBalancer enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
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
        EmailBalancer emailBalancer = (EmailBalancer) o;
        if (emailBalancer.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), emailBalancer.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EmailBalancer{" +
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
