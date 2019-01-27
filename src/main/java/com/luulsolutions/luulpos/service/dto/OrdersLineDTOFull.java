package com.luulsolutions.luulpos.service.dto;


import java.io.Serializable;
import java.util.List;


/**
 * A DTO for the OrdersLine entity.
 */
public class OrdersLineDTOFull implements Serializable {
    
	private Long id;
	
    private OrdersLineDTO ordersLineDTO;
    
    private List <OrdersLineVariantDTOFull> ordersLineVariantsDTOFullList;

    
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public OrdersLineDTO getOrdersLineDTO() {
		return ordersLineDTO;
	}

	public void setOrdersLineDTO(OrdersLineDTO ordersLineDTO) {
		this.ordersLineDTO = ordersLineDTO;
	}

	public List <OrdersLineVariantDTOFull> getOrdersLineVariantsDTOFullList() {
		return ordersLineVariantsDTOFullList;
	}

	public void setOrdersLineVariantsDTOFullList(List <OrdersLineVariantDTOFull> ordersLineVariantsDTOFullList) {
		this.ordersLineVariantsDTOFullList = ordersLineVariantsDTOFullList;
	}

	
}
