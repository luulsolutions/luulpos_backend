package com.luulsolutions.luulpos.service.dto;


import java.io.Serializable;

import java.util.List;





/**
 * A DTO for the Profile entity.
 */
public class ProfileDTOFull implements Serializable {

    private Long id;

    private ProfileDTO profileDTO;
    
    private List<ShopDTO> shopDTOList;

    private List<ProductCategoryDTO> productCategoryDTOList;
    
    private UserDTO userDTO;
    
    private List<SystemConfigDTO> systemConfigDTOList;

    private List<ShopSectionDTO> shopSectionDTOList;
  
    private List<SectionTableDTO> sectionTableDTOList;
    
    private CompanyDTO companyDTO;
    
    private List<PaymentMethodDTO> paymentMethodsDTOList;
    
    private List<PaymentMethodConfigDTO> paymentMethodConfigsDTOList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

  

    public void setShopDTOList(List<ShopDTO> shopDTOList) {
        this.shopDTOList = shopDTOList;
    }
    
    public List<ShopDTO> getShopDTOList() {
    	return shopDTOList;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

  
	public CompanyDTO getCompanyDTO() {
		return companyDTO;
	}

	public void setCompanyDTO(CompanyDTO companyDTO) {
		this.companyDTO = companyDTO;
	}

	public ProfileDTO getProfileDTO() {
		return profileDTO;
	}

	public void setProfileDTO(ProfileDTO profileDTO) {
		this.profileDTO = profileDTO;
	}

	public List<ProductCategoryDTO> getProductCategoryDTOList() {
		return productCategoryDTOList;
	}

	public void setProductCategoryDTOList(List<ProductCategoryDTO> productCategoryDTOList) {
		this.productCategoryDTOList = productCategoryDTOList;
	}

	public List<SystemConfigDTO> getSystemConfigDTOList() {
		return systemConfigDTOList;
	}

	public void setSystemConfigDTOList(List<SystemConfigDTO> systemConfigDTOList) {
		this.systemConfigDTOList = systemConfigDTOList;
	}

	public List<ShopSectionDTO> getShopSectionDTOList() {
		return shopSectionDTOList;
	}

	public void setShopSectionDTOList(List<ShopSectionDTO> shopSectionDTOList) {
		this.shopSectionDTOList = shopSectionDTOList;
	}

	public List<SectionTableDTO> getSectionTableDTOList() {
		return sectionTableDTOList;
	}

	public void setSectionTableDTOList(List<SectionTableDTO> sectionTableDTOList) {
		this.sectionTableDTOList = sectionTableDTOList;
	}

	public List<PaymentMethodDTO> getPaymentMethodsDTOList() {
		return paymentMethodsDTOList;
	}

	public void setPaymentMethodsDTOList(List<PaymentMethodDTO> paymentMethodsDTOList) {
		this.paymentMethodsDTOList = paymentMethodsDTOList;
	}

	public List<PaymentMethodConfigDTO> getPaymentMethodConfigsDTOList() {
		return paymentMethodConfigsDTOList;
	}

	public void setPaymentMethodConfigsDTOList(List<PaymentMethodConfigDTO> paymentMethodConfigsDTOList) {
		this.paymentMethodConfigsDTOList = paymentMethodConfigsDTOList;
	}









}

