package com.luulsolutions.luulpos.service.dto;

import java.time.ZonedDateTime;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import com.luulsolutions.luulpos.domain.enumeration.PaymentStatus;

/**
 * A DTO for the Payment entity.
 */
public class PaymentDTO implements Serializable {

    private Long id;

    private ZonedDateTime paymentDate;

    private String paymentProvider;

    private BigDecimal amount;

    private PaymentStatus paymentStatus;

    private String curency;

    private String customerName;

    private Long shopId;

    private String shopShopName;

    private Long processedById;

    private String processedByFirstName;

    private Long paymentMethodId;

    private String paymentMethodPaymentMethod;

    private Long orderId;

    private String orderDescription;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(ZonedDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentProvider() {
        return paymentProvider;
    }

    public void setPaymentProvider(String paymentProvider) {
        this.paymentProvider = paymentProvider;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getCurency() {
        return curency;
    }

    public void setCurency(String curency) {
        this.curency = curency;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public Long getProcessedById() {
        return processedById;
    }

    public void setProcessedById(Long profileId) {
        this.processedById = profileId;
    }

    public String getProcessedByFirstName() {
        return processedByFirstName;
    }

    public void setProcessedByFirstName(String profileFirstName) {
        this.processedByFirstName = profileFirstName;
    }

    public Long getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(Long paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public String getPaymentMethodPaymentMethod() {
        return paymentMethodPaymentMethod;
    }

    public void setPaymentMethodPaymentMethod(String paymentMethodPaymentMethod) {
        this.paymentMethodPaymentMethod = paymentMethodPaymentMethod;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long ordersId) {
        this.orderId = ordersId;
    }

    public String getOrderDescription() {
        return orderDescription;
    }

    public void setOrderDescription(String ordersDescription) {
        this.orderDescription = ordersDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PaymentDTO paymentDTO = (PaymentDTO) o;
        if (paymentDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), paymentDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PaymentDTO{" +
            "id=" + getId() +
            ", paymentDate='" + getPaymentDate() + "'" +
            ", paymentProvider='" + getPaymentProvider() + "'" +
            ", amount=" + getAmount() +
            ", paymentStatus='" + getPaymentStatus() + "'" +
            ", curency='" + getCurency() + "'" +
            ", customerName='" + getCustomerName() + "'" +
            ", shop=" + getShopId() +
            ", shop='" + getShopShopName() + "'" +
            ", processedBy=" + getProcessedById() +
            ", processedBy='" + getProcessedByFirstName() + "'" +
            ", paymentMethod=" + getPaymentMethodId() +
            ", paymentMethod='" + getPaymentMethodPaymentMethod() + "'" +
            ", order=" + getOrderId() +
            ", order='" + getOrderDescription() + "'" +
            "}";
    }
}
