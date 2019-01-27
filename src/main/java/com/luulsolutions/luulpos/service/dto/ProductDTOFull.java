package com.luulsolutions.luulpos.service.dto;

import java.io.Serializable;
import java.util.List;


/**
 * A DTO for the Product entity.
 */
public class ProductDTOFull implements Serializable {

    private  ProductDTO productDTO;
    private  List <ProductVariantDTO> productVariantsDTOList;
    private  List <ProductExtraDTO> productExtraDTOList;
   // private  List <ProductCategoryDTO> productCategoryDTOList;
  //  private ShopDTO shopDTO;
 //   private CompanyDTO companyDTO;
    private List<DiscountDTO> discountDTOList;
    private List<TaxDTO> taxDTOList;
 //   private List <PaymentMethodDTO> paymentMethodList;
 //   private List <PaymentMethodConfigDTO> paymentMethodConfigDTOList;
    
    
    
    
    
    
    
    public void setProductDTO (ProductDTO productDTO) {
    	this.productDTO = productDTO;
    }
    
    public ProductDTO getProductDTO () {
    	return productDTO;
    }

	public List <ProductVariantDTO> getProductVariantsDTOList() {
		return productVariantsDTOList;
	}

	public void setProductVariantsDTOList(List <ProductVariantDTO> productVariantsDTOList) {
		this.productVariantsDTOList = productVariantsDTOList;
	}

	public List <ProductExtraDTO> getProductExtraDTOList() {
		return productExtraDTOList;
	}

	public void setProductExtraDTOList(List <ProductExtraDTO> productExtraDTOList) {
		this.productExtraDTOList = productExtraDTOList;
	}

	/*public List <ProductCategoryDTO> getProductCategoryDTOList() {
		return productCategoryDTOList;
	}

	public void setProductCategoryDTOList(List <ProductCategoryDTO> productCategoryDTOList) {
		this.productCategoryDTOList = productCategoryDTOList;
	}

	public ShopDTO getShopDTO() {
		return shopDTO;
	}

	public void setShopDTO(ShopDTO shopDTO) {
		this.shopDTO = shopDTO;
	}

	public CompanyDTO getCompanyDTO() {
		return companyDTO;
	}

	public void setCompanyDTO(CompanyDTO companyDTO) {
		this.companyDTO = companyDTO;
	}*/

	public List<DiscountDTO> getDiscountDTOList() {
		return discountDTOList;
	}

	public void setDiscountDTOList(List<DiscountDTO> discountDTOList) {
		this.discountDTOList = discountDTOList;
	}

	public List<TaxDTO> getTaxDTOList() {
		return taxDTOList;
	}

	public void setTaxDTOList(List<TaxDTO> taxDTOList) {
		this.taxDTOList = taxDTOList;
	}

	/*public List <PaymentMethodDTO> getPaymentMethodList() {
		return paymentMethodList;
	}

	public void setPaymentMethodList(List <PaymentMethodDTO> paymentMethodList) {
		this.paymentMethodList = paymentMethodList;
	}

	public List <PaymentMethodConfigDTO> getPaymentMethodConfigDTOList() {
		return paymentMethodConfigDTOList;
	}

	public void setPaymentMethodConfigDTOList(List <PaymentMethodConfigDTO> paymentMethodConfigDTOList) {
		this.paymentMethodConfigDTOList = paymentMethodConfigDTOList;
	}*/
}
