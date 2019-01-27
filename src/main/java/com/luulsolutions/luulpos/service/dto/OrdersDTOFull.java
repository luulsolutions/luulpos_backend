package com.luulsolutions.luulpos.service.dto;

import java.io.Serializable;
import java.util.List;

public class OrdersDTOFull implements Serializable  {
   
	private Long id;
	private OrdersDTO orderDTO;
	private List<OrdersLineDTOFull> ordersLineDTOFullList;
	private List<PaymentDTO> paymentDTOList;
	
	
	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public OrdersDTO getOrderDTO() {
		return orderDTO;
	}
	public void setOrderDTO(OrdersDTO orderDTO) {
		this.orderDTO = orderDTO;
	}
	public List<OrdersLineDTOFull> getOrdersLineDTOFullList() {
		return ordersLineDTOFullList;
	}
	public void setOrdersLineDTOFullList(List<OrdersLineDTOFull> ordersLineDTOFullList) {
		this.ordersLineDTOFullList = ordersLineDTOFullList;
	}
	public List<PaymentDTO> getPaymentDTOList() {
		return paymentDTOList;
	}
	public void setPaymentDTOList(List<PaymentDTO> paymentDTOList) {
		this.paymentDTOList = paymentDTOList;
	}
	
}
