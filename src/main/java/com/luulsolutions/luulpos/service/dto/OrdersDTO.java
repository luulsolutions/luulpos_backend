package com.luulsolutions.luulpos.service.dto;

import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import com.luulsolutions.luulpos.domain.enumeration.OrderStatus;

/**
 * A DTO for the Orders entity.
 */
public class OrdersDTO implements Serializable {

    private Long id;

    private String description;

    private String customerName;

    @NotNull
    private BigDecimal totalPrice;

    @NotNull
    private Integer quantity;

    private Float discountPercentage;

    private BigDecimal discountAmount;

    private Float taxPercentage;

    private BigDecimal taxAmount;

    private OrderStatus orderStatus;

    private String note;

    private ZonedDateTime orderDate;

    private Boolean isVariantOrder;

    private Long paymentMethodId;

    private String paymentMethodPaymentMethod;

    private Long soldById;

    private String soldByFirstName;

    private Long preparedById;

    private String preparedByFirstName;

    private Long shopDeviceId;

    private String shopDeviceDeviceName;

    private Long sectionTableId;

    private String sectionTableTableNumber;

    private Long shopId;

    private String shopShopName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Float getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(Float discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Float getTaxPercentage() {
        return taxPercentage;
    }

    public void setTaxPercentage(Float taxPercentage) {
        this.taxPercentage = taxPercentage;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public ZonedDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(ZonedDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public Boolean isIsVariantOrder() {
        return isVariantOrder;
    }

    public void setIsVariantOrder(Boolean isVariantOrder) {
        this.isVariantOrder = isVariantOrder;
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

    public Long getSoldById() {
        return soldById;
    }

    public void setSoldById(Long profileId) {
        this.soldById = profileId;
    }

    public String getSoldByFirstName() {
        return soldByFirstName;
    }

    public void setSoldByFirstName(String profileFirstName) {
        this.soldByFirstName = profileFirstName;
    }

    public Long getPreparedById() {
        return preparedById;
    }

    public void setPreparedById(Long profileId) {
        this.preparedById = profileId;
    }

    public String getPreparedByFirstName() {
        return preparedByFirstName;
    }

    public void setPreparedByFirstName(String profileFirstName) {
        this.preparedByFirstName = profileFirstName;
    }

    public Long getShopDeviceId() {
        return shopDeviceId;
    }

    public void setShopDeviceId(Long shopDeviceId) {
        this.shopDeviceId = shopDeviceId;
    }

    public String getShopDeviceDeviceName() {
        return shopDeviceDeviceName;
    }

    public void setShopDeviceDeviceName(String shopDeviceDeviceName) {
        this.shopDeviceDeviceName = shopDeviceDeviceName;
    }

    public Long getSectionTableId() {
        return sectionTableId;
    }

    public void setSectionTableId(Long sectionTableId) {
        this.sectionTableId = sectionTableId;
    }

    public String getSectionTableTableNumber() {
        return sectionTableTableNumber;
    }

    public void setSectionTableTableNumber(String sectionTableTableNumber) {
        this.sectionTableTableNumber = sectionTableTableNumber;
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

        OrdersDTO ordersDTO = (OrdersDTO) o;
        if (ordersDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ordersDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OrdersDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", customerName='" + getCustomerName() + "'" +
            ", totalPrice=" + getTotalPrice() +
            ", quantity=" + getQuantity() +
            ", discountPercentage=" + getDiscountPercentage() +
            ", discountAmount=" + getDiscountAmount() +
            ", taxPercentage=" + getTaxPercentage() +
            ", taxAmount=" + getTaxAmount() +
            ", orderStatus='" + getOrderStatus() + "'" +
            ", note='" + getNote() + "'" +
            ", orderDate='" + getOrderDate() + "'" +
            ", isVariantOrder='" + isIsVariantOrder() + "'" +
            ", paymentMethod=" + getPaymentMethodId() +
            ", paymentMethod='" + getPaymentMethodPaymentMethod() + "'" +
            ", soldBy=" + getSoldById() +
            ", soldBy='" + getSoldByFirstName() + "'" +
            ", preparedBy=" + getPreparedById() +
            ", preparedBy='" + getPreparedByFirstName() + "'" +
            ", shopDevice=" + getShopDeviceId() +
            ", shopDevice='" + getShopDeviceDeviceName() + "'" +
            ", sectionTable=" + getSectionTableId() +
            ", sectionTable='" + getSectionTableTableNumber() + "'" +
            ", shop=" + getShopId() +
            ", shop='" + getShopShopName() + "'" +
            "}";
    }
}
