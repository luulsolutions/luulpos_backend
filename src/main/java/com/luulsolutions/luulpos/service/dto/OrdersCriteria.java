package com.luulsolutions.luulpos.service.dto;

import java.io.Serializable;
import java.util.Objects;

import com.luulsolutions.luulpos.domain.enumeration.OrderStatus;

import io.github.jhipster.service.filter.BigDecimalFilter;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the Orders entity. This class is used in OrdersResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /orders?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class OrdersCriteria implements Serializable {
    /**
     * Class for filtering OrderStatus
     */
    public static class OrderStatusFilter extends Filter<OrderStatus> {
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter description;

    private StringFilter customerName;

    private BigDecimalFilter totalPrice;

    private IntegerFilter quantity;

    private FloatFilter discountPercentage;

    private BigDecimalFilter discountAmount;

    private FloatFilter taxPercentage;

    private BigDecimalFilter taxAmount;

    private OrderStatusFilter orderStatus;

    private StringFilter note;

    private ZonedDateTimeFilter orderDate;

    private BooleanFilter isVariantOrder;

    private LongFilter ordersLinesId;

    private LongFilter paymentMethodId;

    private LongFilter soldById;

    private LongFilter preparedById;

    private LongFilter shopDeviceId;

    private LongFilter sectionTableId;

    private LongFilter shopId;

    public OrdersCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getCustomerName() {
        return customerName;
    }

    public void setCustomerName(StringFilter customerName) {
        this.customerName = customerName;
    }

    public BigDecimalFilter getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimalFilter totalPrice) {
        this.totalPrice = totalPrice;
    }

    public IntegerFilter getQuantity() {
        return quantity;
    }

    public void setQuantity(IntegerFilter quantity) {
        this.quantity = quantity;
    }

    public FloatFilter getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(FloatFilter discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public BigDecimalFilter getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimalFilter discountAmount) {
        this.discountAmount = discountAmount;
    }

    public FloatFilter getTaxPercentage() {
        return taxPercentage;
    }

    public void setTaxPercentage(FloatFilter taxPercentage) {
        this.taxPercentage = taxPercentage;
    }

    public BigDecimalFilter getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimalFilter taxAmount) {
        this.taxAmount = taxAmount;
    }

    public OrderStatusFilter getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatusFilter orderStatus) {
        this.orderStatus = orderStatus;
    }

    public StringFilter getNote() {
        return note;
    }

    public void setNote(StringFilter note) {
        this.note = note;
    }

    public ZonedDateTimeFilter getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(ZonedDateTimeFilter orderDate) {
        this.orderDate = orderDate;
    }

    public BooleanFilter getIsVariantOrder() {
        return isVariantOrder;
    }

    public void setIsVariantOrder(BooleanFilter isVariantOrder) {
        this.isVariantOrder = isVariantOrder;
    }

    public LongFilter getOrdersLinesId() {
        return ordersLinesId;
    }

    public void setOrdersLinesId(LongFilter ordersLinesId) {
        this.ordersLinesId = ordersLinesId;
    }

    public LongFilter getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(LongFilter paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public LongFilter getSoldById() {
        return soldById;
    }

    public void setSoldById(LongFilter soldById) {
        this.soldById = soldById;
    }

    public LongFilter getPreparedById() {
        return preparedById;
    }

    public void setPreparedById(LongFilter preparedById) {
        this.preparedById = preparedById;
    }

    public LongFilter getShopDeviceId() {
        return shopDeviceId;
    }

    public void setShopDeviceId(LongFilter shopDeviceId) {
        this.shopDeviceId = shopDeviceId;
    }

    public LongFilter getSectionTableId() {
        return sectionTableId;
    }

    public void setSectionTableId(LongFilter sectionTableId) {
        this.sectionTableId = sectionTableId;
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
        final OrdersCriteria that = (OrdersCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(description, that.description) &&
            Objects.equals(customerName, that.customerName) &&
            Objects.equals(totalPrice, that.totalPrice) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(discountPercentage, that.discountPercentage) &&
            Objects.equals(discountAmount, that.discountAmount) &&
            Objects.equals(taxPercentage, that.taxPercentage) &&
            Objects.equals(taxAmount, that.taxAmount) &&
            Objects.equals(orderStatus, that.orderStatus) &&
            Objects.equals(note, that.note) &&
            Objects.equals(orderDate, that.orderDate) &&
            Objects.equals(isVariantOrder, that.isVariantOrder) &&
            Objects.equals(ordersLinesId, that.ordersLinesId) &&
            Objects.equals(paymentMethodId, that.paymentMethodId) &&
            Objects.equals(soldById, that.soldById) &&
            Objects.equals(preparedById, that.preparedById) &&
            Objects.equals(shopDeviceId, that.shopDeviceId) &&
            Objects.equals(sectionTableId, that.sectionTableId) &&
            Objects.equals(shopId, that.shopId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        description,
        customerName,
        totalPrice,
        quantity,
        discountPercentage,
        discountAmount,
        taxPercentage,
        taxAmount,
        orderStatus,
        note,
        orderDate,
        isVariantOrder,
        ordersLinesId,
        paymentMethodId,
        soldById,
        preparedById,
        shopDeviceId,
        sectionTableId,
        shopId
        );
    }

    @Override
    public String toString() {
        return "OrdersCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (customerName != null ? "customerName=" + customerName + ", " : "") +
                (totalPrice != null ? "totalPrice=" + totalPrice + ", " : "") +
                (quantity != null ? "quantity=" + quantity + ", " : "") +
                (discountPercentage != null ? "discountPercentage=" + discountPercentage + ", " : "") +
                (discountAmount != null ? "discountAmount=" + discountAmount + ", " : "") +
                (taxPercentage != null ? "taxPercentage=" + taxPercentage + ", " : "") +
                (taxAmount != null ? "taxAmount=" + taxAmount + ", " : "") +
                (orderStatus != null ? "orderStatus=" + orderStatus + ", " : "") +
                (note != null ? "note=" + note + ", " : "") +
                (orderDate != null ? "orderDate=" + orderDate + ", " : "") +
                (isVariantOrder != null ? "isVariantOrder=" + isVariantOrder + ", " : "") +
                (ordersLinesId != null ? "ordersLinesId=" + ordersLinesId + ", " : "") +
                (paymentMethodId != null ? "paymentMethodId=" + paymentMethodId + ", " : "") +
                (soldById != null ? "soldById=" + soldById + ", " : "") +
                (preparedById != null ? "preparedById=" + preparedById + ", " : "") +
                (shopDeviceId != null ? "shopDeviceId=" + shopDeviceId + ", " : "") +
                (sectionTableId != null ? "sectionTableId=" + sectionTableId + ", " : "") +
                (shopId != null ? "shopId=" + shopId + ", " : "") +
            "}";
    }

}
