package com.luulsolutions.luulpos.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.luulsolutions.luulpos.domain.enumeration.OrderStatus;

/**
 * A Orders.
 */
@Entity
@Table(name = "orders")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "orders")
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "customer_name")
    private String customerName;

    @NotNull
    @Column(name = "total_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalPrice;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "discount_percentage")
    private Float discountPercentage;

    @Column(name = "discount_amount", precision = 10, scale = 2)
    private BigDecimal discountAmount;

    @Column(name = "tax_percentage")
    private Float taxPercentage;

    @Column(name = "tax_amount", precision = 10, scale = 2)
    private BigDecimal taxAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;

    @Column(name = "note")
    private String note;

    @Column(name = "order_date")
    private ZonedDateTime orderDate;

    @Column(name = "is_variant_order")
    private Boolean isVariantOrder;

    @OneToMany(mappedBy = "orders")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<OrdersLine> ordersLines = new HashSet<>();
    @ManyToOne
    @JsonIgnoreProperties("")
    private PaymentMethod paymentMethod;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Profile soldBy;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Profile preparedBy;

    @ManyToOne
    @JsonIgnoreProperties("")
    private ShopDevice shopDevice;

    @ManyToOne
    @JsonIgnoreProperties("")
    private SectionTable sectionTable;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Shop shop;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public Orders description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCustomerName() {
        return customerName;
    }

    public Orders customerName(String customerName) {
        this.customerName = customerName;
        return this;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public Orders totalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Orders quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Float getDiscountPercentage() {
        return discountPercentage;
    }

    public Orders discountPercentage(Float discountPercentage) {
        this.discountPercentage = discountPercentage;
        return this;
    }

    public void setDiscountPercentage(Float discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public Orders discountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
        return this;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Float getTaxPercentage() {
        return taxPercentage;
    }

    public Orders taxPercentage(Float taxPercentage) {
        this.taxPercentage = taxPercentage;
        return this;
    }

    public void setTaxPercentage(Float taxPercentage) {
        this.taxPercentage = taxPercentage;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public Orders taxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
        return this;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public Orders orderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
        return this;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getNote() {
        return note;
    }

    public Orders note(String note) {
        this.note = note;
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public ZonedDateTime getOrderDate() {
        return orderDate;
    }

    public Orders orderDate(ZonedDateTime orderDate) {
        this.orderDate = orderDate;
        return this;
    }

    public void setOrderDate(ZonedDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public Boolean isIsVariantOrder() {
        return isVariantOrder;
    }

    public Orders isVariantOrder(Boolean isVariantOrder) {
        this.isVariantOrder = isVariantOrder;
        return this;
    }

    public void setIsVariantOrder(Boolean isVariantOrder) {
        this.isVariantOrder = isVariantOrder;
    }

    public Set<OrdersLine> getOrdersLines() {
        return ordersLines;
    }

    public Orders ordersLines(Set<OrdersLine> ordersLines) {
        this.ordersLines = ordersLines;
        return this;
    }

    public Orders addOrdersLines(OrdersLine ordersLine) {
        this.ordersLines.add(ordersLine);
        ordersLine.setOrders(this);
        return this;
    }

    public Orders removeOrdersLines(OrdersLine ordersLine) {
        this.ordersLines.remove(ordersLine);
        ordersLine.setOrders(null);
        return this;
    }

    public void setOrdersLines(Set<OrdersLine> ordersLines) {
        this.ordersLines = ordersLines;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public Orders paymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Profile getSoldBy() {
        return soldBy;
    }

    public Orders soldBy(Profile profile) {
        this.soldBy = profile;
        return this;
    }

    public void setSoldBy(Profile profile) {
        this.soldBy = profile;
    }

    public Profile getPreparedBy() {
        return preparedBy;
    }

    public Orders preparedBy(Profile profile) {
        this.preparedBy = profile;
        return this;
    }

    public void setPreparedBy(Profile profile) {
        this.preparedBy = profile;
    }

    public ShopDevice getShopDevice() {
        return shopDevice;
    }

    public Orders shopDevice(ShopDevice shopDevice) {
        this.shopDevice = shopDevice;
        return this;
    }

    public void setShopDevice(ShopDevice shopDevice) {
        this.shopDevice = shopDevice;
    }

    public SectionTable getSectionTable() {
        return sectionTable;
    }

    public Orders sectionTable(SectionTable sectionTable) {
        this.sectionTable = sectionTable;
        return this;
    }

    public void setSectionTable(SectionTable sectionTable) {
        this.sectionTable = sectionTable;
    }

    public Shop getShop() {
        return shop;
    }

    public Orders shop(Shop shop) {
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
        Orders orders = (Orders) o;
        if (orders.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), orders.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Orders{" +
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
            "}";
    }
}
