package com.luulsolutions.luulpos.service;

import com.luulsolutions.luulpos.domain.ProductCategory;
import com.luulsolutions.luulpos.domain.Profile;
import com.luulsolutions.luulpos.repository.ProfileRepository;
import com.luulsolutions.luulpos.repository.search.ProfileSearchRepository;
import com.luulsolutions.luulpos.service.dto.PaymentMethodConfigDTO;
import com.luulsolutions.luulpos.service.dto.ProfileDTO;
import com.luulsolutions.luulpos.service.dto.ProfileDTOFull;
import com.luulsolutions.luulpos.service.dto.SectionTableDTO;
import com.luulsolutions.luulpos.service.dto.ShopChangeDTO;
import com.luulsolutions.luulpos.service.dto.ShopDTO;
import com.luulsolutions.luulpos.service.mapper.CompanyMapper;
import com.luulsolutions.luulpos.service.mapper.ProductCategoryMapper;
import com.luulsolutions.luulpos.service.mapper.ProfileMapper;
import com.luulsolutions.luulpos.service.mapper.ShopMapper;
import com.luulsolutions.luulpos.service.mapper.UserMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Profile.
 */
@Service
@Transactional
public class ProfileService {

    private final Logger log = LoggerFactory.getLogger(ProfileService.class);

    private final ProfileRepository profileRepository;

    private final ProfileMapper profileMapper;

    private final ProfileSearchRepository profileSearchRepository;
    
     @Autowired
     UserMapper userMapper;
    
     @Autowired
     ShopMapper shopMapper;
    
     @Autowired
     CompanyMapper companyMapper;
    
     @Autowired
     ProductCategoryMapper productCategoryMapper;
    
     @Autowired
     ShopSectionService shopSectionService;
     
     @Autowired
     SectionTableService sectionTableService;
    
     @Autowired
     SystemConfigService systemConfigService;
    
     @Autowired
     PaymentMethodService paymentMethodService;
    
     @Autowired
     PaymentMethodConfigService paymentMethodConfigService;
    
     @Autowired
     ShopChangeService shopChangeService;

    public ProfileService(ProfileRepository profileRepository, ProfileMapper profileMapper, ProfileSearchRepository profileSearchRepository) {
        this.profileRepository = profileRepository;
        this.profileMapper = profileMapper;
        this.profileSearchRepository = profileSearchRepository;
    }

    /**
     * Save a profile.
     *
     * @param profileDTO the entity to save
     * @return the persisted entity
     */
    public ProfileDTO save(ProfileDTO profileDTO) {
        log.debug("Request to save Profile : {}", profileDTO);

        Profile profile = profileMapper.toEntity(profileDTO);
        profile = profileRepository.save(profile);
        ProfileDTO result = profileMapper.toDto(profile);
        profileSearchRepository.save(profile);
        return result;
    }

    /**
     * Get all the profiles.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ProfileDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Profiles");
        return profileRepository.findAll(pageable)
            .map(profileMapper::toDto);
    }


    /**
     * Get one profile by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ProfileDTO> findOne(Long id) {
        log.debug("Request to get Profile : {}", id);
        return profileRepository.findById(id)
            .map(profileMapper::toDto);
    }

    /**
     * Delete the profile by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Profile : {}", id);
        profileRepository.deleteById(id);
        profileSearchRepository.deleteById(id);
    }

    /**
     * Search for the profile corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ProfileDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Profiles for query {}", query);
        return profileSearchRepository.search(queryStringQuery(query), pageable)
            .map(profileMapper::toDto);
    }
    
    public ProfileDTOFull findByEmail(String email) {
	     log.debug("Request to get full Profile : {}", email);
	        Profile profile = profileRepository.findByEmail(email);
	        ProfileDTO profileDTO =  profileMapper.toDto(profile);
	        if (profileDTO == null) {
	        	return null;
	        }
	        ProfileDTOFull profileDTOFull = new ProfileDTOFull();
	        profileDTOFull.setProfileDTO(profileDTO);
	        profileDTOFull.setId(profileDTO.getId());
	        profileDTOFull.setUserDTO(userMapper.userToUserDTO(profile.getUser()));
	        List<ShopDTO> shopDTOList = new ArrayList<ShopDTO>();
	        shopDTOList.add(shopMapper.toDto(profile.getShop()));
	        profileDTOFull.setShopDTOList(shopDTOList);
	        ArrayList <ProductCategory> productCategoryList = new ArrayList<ProductCategory>();
	        productCategoryList.addAll(profile.getShop().getProductCategories());
	        profileDTOFull.setProductCategoryDTOList(productCategoryMapper.toDto(productCategoryList));
	        profileDTOFull.setCompanyDTO(companyMapper.toDto(profile.getShop().getCompany()));
	        ShopChangeDTO shopChangeDTO = shopChangeService.findFirstByShopId(profile.getShop().getId());
	        if (shopChangeDTO != null) {
	        	profileDTO.setShopChangeId(shopChangeDTO.getId());
	        } else {
	        	profileDTO.setShopChangeId(0L);
	        }
	        profileDTOFull.setSystemConfigDTOList(systemConfigService.findAllByShopId(profile.getShop().getCompany().getId()));
	        profileDTOFull.setShopSectionDTOList(shopSectionService.findAllByShopId(profile.getShop().getId()));
	        ArrayList <SectionTableDTO> sectionTableDTOList = new ArrayList<SectionTableDTO>();
	        if (profileDTOFull.getShopSectionDTOList() != null && profileDTOFull.getShopSectionDTOList().size() > 0) {
	        profileDTOFull.getShopSectionDTOList().forEach((item) ->{   	
	        	sectionTableDTOList.addAll(sectionTableService.findAllByShopSectionsId(item.getId()));
	        });
	        }
	        profileDTOFull.setPaymentMethodsDTOList(paymentMethodService.findAllByShopId(profile.getShop().getId()));
	        List <PaymentMethodConfigDTO> paymentMethodConfigsDTOList = new ArrayList <PaymentMethodConfigDTO>();
	        if (profileDTOFull.getPaymentMethodsDTOList() != null && profileDTOFull.getPaymentMethodsDTOList().size() > 0) {
	        	profileDTOFull.getPaymentMethodsDTOList().forEach(paymentMethod -> {
	    	        paymentMethodConfigsDTOList.addAll(paymentMethodConfigService.findAllByPaymentMethodId(paymentMethod.getId()));
	        	});
	        }
	        profileDTOFull.setPaymentMethodConfigsDTOList(paymentMethodConfigsDTOList);
	        
           profileDTOFull.setSectionTableDTOList(sectionTableDTOList);
	        return profileDTOFull;  
	}
}
