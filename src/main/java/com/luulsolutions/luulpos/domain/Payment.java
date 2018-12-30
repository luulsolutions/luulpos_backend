package com.luulsolutions.luulpos.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

import com.luulsolutions.luulpos.domain.enumeration.PaymentStatus;

/**
 * A Payment.
 */
@Entity
@Table(name = "payment")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "payment")
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_date")
    private ZonedDateTime paymentDate;

    @Column(name = "payment_provider")
    private String paymentProvider;

    @Column(name = "amount", precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;

    @Column(name = "curency")
    private String curency;

    @Column(name = "customer_name")
    private String customerName;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Shop shop;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Profile processedBy;

    @ManyToOne
    @JsonIgnoreProperties("")
    private PaymentMethod paymentMethod;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Orders order;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getPaymentDate() {
        return paymentDate;
    }

    public Payment paymentDate(ZonedDateTime paymentDate) {
        this.paymentDate = paymentDate;
        return this;
    }

    public void setPaymentDate(ZonedDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentProvider() {
        return paymentProvider;
    }

    public Payment paymentProvider(String paymentProvider) {
        this.paymentProvider = paymentProvider;
        return this;
    }

    public void setPaymentProvider(String paymentProvider) {
        this.paymentProvider = paymentProvider;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Payment amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public Payment paymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
        return this;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getCurency() {
        return curency;
    }

    public Payment curency(String curency) {
        this.curency = curency;
        return this;
    }

    public void setCurency(String curency) {
        this.curency = curency;
    }

    public String getCustomerName() {
        return customerName;
    }

    public Payment customerName(String customerName) {
        this.customerName = customerName;
        return this;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Shop getShop() {
        return shop;
    }

    public Payment shop(Shop shop) {
        this.shop = shop;
        return this;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public Profile getProcessedBy() {
        return processedBy;
    }

    public Payment processedBy(Profile profile) {
        this.processedBy = profile;
        return this;
    }

    public void setProcessedBy(Profile profile) {
        this.processedBy = profile;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public Payment paymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Orders getOrder() {
        return order;
    }

    public Payment order(Orders orders) {
        this.order = orders;
        return this;
    }

    public void setOrder(Orders orders) {
        this.order = orders;
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
        Payment payment = (Payment) o;
        if (payment.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), payment.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Payment{" +
            "id=" + getId() +
            ", paymentDate='" + getPaymentDate() + "'" +
            ", paymentProvider='" + getPaymentProvider() + "'" +
            ", amount=" + getAmount() +
            ", paymentStatus='" + getPaymentStatus() + "'" +
            ", curency='" + getCurency() + "'" +
            ", customerName='" + getCustomerName() + "'" +
            "}";
    }
}
