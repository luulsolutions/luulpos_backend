package com.luulsolutions.luulpos.service.dto;


import java.io.Serializable;
import java.util.List;


/**
 * A DTO for the OrdersLineVariant entity.
 */
public class OrdersLineVariantDTOFull implements Serializable {

    private Long id;
    
    private OrdersLineVariantDTO ordersLineVariantDTO;
    
    private List <OrdersLineExtraDTO> ordersLineExtraDTOList;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public OrdersLineVariantDTO getOrdersLineVariantDTO() {
		return ordersLineVariantDTO;
	}

	public void setOrdersLineVariantDTO(OrdersLineVariantDTO ordersLineVariantDTO) {
		this.ordersLineVariantDTO = ordersLineVariantDTO;
	}

	public List <OrdersLineExtraDTO> getOrdersLineExtraDTOList() {
		return ordersLineExtraDTOList;
	}

	public void setOrdersLineExtraDTOList(List <OrdersLineExtraDTO> ordersLineExtraDTOList) {
		this.ordersLineExtraDTOList = ordersLineExtraDTOList;
	}

	
	
  
    

    

   
}
