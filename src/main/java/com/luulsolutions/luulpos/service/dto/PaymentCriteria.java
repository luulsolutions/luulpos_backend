package com.luulsolutions.luulpos.service.dto;

import java.io.Serializable;
import java.util.Objects;
import com.luulsolutions.luulpos.domain.enumeration.PaymentStatus;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.BigDecimalFilter;
import io.github.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the Payment entity. This class is used in PaymentResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /payments?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PaymentCriteria implements Serializable {
    /**
     * Class for filtering PaymentStatus
     */
    public static class PaymentStatusFilter extends Filter<PaymentStatus> {
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ZonedDateTimeFilter paymentDate;

    private StringFilter paymentProvider;

    private BigDecimalFilter amount;

    private PaymentStatusFilter paymentStatus;

    private StringFilter curency;

    private StringFilter customerName;

    private LongFilter shopId;

    private LongFilter processedById;

    private LongFilter paymentMethodId;

    private LongFilter orderId;

    public PaymentCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public ZonedDateTimeFilter getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(ZonedDateTimeFilter paymentDate) {
        this.paymentDate = paymentDate;
    }

    public StringFilter getPaymentProvider() {
        return paymentProvider;
    }

    public void setPaymentProvider(StringFilter paymentProvider) {
        this.paymentProvider = paymentProvider;
    }

    public BigDecimalFilter getAmount() {
        return amount;
    }

    public void setAmount(BigDecimalFilter amount) {
        this.amount = amount;
    }

    public PaymentStatusFilter getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatusFilter paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public StringFilter getCurency() {
        return curency;
    }

    public void setCurency(StringFilter curency) {
        this.curency = curency;
    }

    public StringFilter getCustomerName() {
        return customerName;
    }

    public void setCustomerName(StringFilter customerName) {
        this.customerName = customerName;
    }

    public LongFilter getShopId() {
        return shopId;
    }

    public void setShopId(LongFilter shopId) {
        this.shopId = shopId;
    }

    public LongFilter getProcessedById() {
        return processedById;
    }

    public void setProcessedById(LongFilter processedById) {
        this.processedById = processedById;
    }

    public LongFilter getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(LongFilter paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public LongFilter getOrderId() {
        return orderId;
    }

    public void setOrderId(LongFilter orderId) {
        this.orderId = orderId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PaymentCriteria that = (PaymentCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(paymentDate, that.paymentDate) &&
            Objects.equals(paymentProvider, that.paymentProvider) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(paymentStatus, that.paymentStatus) &&
            Objects.equals(curency, that.curency) &&
            Objects.equals(customerName, that.customerName) &&
            Objects.equals(shopId, that.shopId) &&
            Objects.equals(processedById, that.processedById) &&
            Objects.equals(paymentMethodId, that.paymentMethodId) &&
            Objects.equals(orderId, that.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        paymentDate,
        paymentProvider,
        amount,
        paymentStatus,
        curency,
        customerName,
        shopId,
        processedById,
        paymentMethodId,
        orderId
        );
    }

    @Override
    public String toString() {
        return "PaymentCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (paymentDate != null ? "paymentDate=" + paymentDate + ", " : "") +
                (paymentProvider != null ? "paymentProvider=" + paymentProvider + ", " : "") +
                (amount != null ? "amount=" + amount + ", " : "") +
                (paymentStatus != null ? "paymentStatus=" + paymentStatus + ", " : "") +
                (curency != null ? "curency=" + curency + ", " : "") +
                (customerName != null ? "customerName=" + customerName + ", " : "") +
                (shopId != null ? "shopId=" + shopId + ", " : "") +
                (processedById != null ? "processedById=" + processedById + ", " : "") +
                (paymentMethodId != null ? "paymentMethodId=" + paymentMethodId + ", " : "") +
                (orderId != null ? "orderId=" + orderId + ", " : "") +
            "}";
    }

}
