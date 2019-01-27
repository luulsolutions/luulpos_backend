package com.luulsolutions.luulpos.service;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luulsolutions.luulpos.domain.Discount;
import com.luulsolutions.luulpos.domain.Product;
import com.luulsolutions.luulpos.domain.Tax;
import com.luulsolutions.luulpos.repository.ProductRepository;
import com.luulsolutions.luulpos.repository.search.ProductSearchRepository;
import com.luulsolutions.luulpos.repository.search.ProfileSearchRepository;
import com.luulsolutions.luulpos.service.dto.ProductDTO;
import com.luulsolutions.luulpos.service.dto.ProductDTOFull;
import com.luulsolutions.luulpos.service.mapper.CompanyMapper;
import com.luulsolutions.luulpos.service.mapper.DiscountMapper;
import com.luulsolutions.luulpos.service.mapper.ProductCategoryMapper;
import com.luulsolutions.luulpos.service.mapper.ProductMapper;
import com.luulsolutions.luulpos.service.mapper.ProfileMapper;
import com.luulsolutions.luulpos.service.mapper.ShopMapper;
import com.luulsolutions.luulpos.service.mapper.TaxMapper;
import com.luulsolutions.luulpos.service.mapper.UserMapper;

/**
 * Service Implementation for managing Product.
 */
@Service
@Transactional
public class ProductService {

    private final Logger log = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    private final ProductSearchRepository productSearchRepository;
    
    @Autowired
    ProfileMapper profileMapper;
    
    @Autowired
    UserMapper userMapper;
    
    @Autowired
    ShopMapper shopMapper;
    
    @Autowired
    CompanyMapper companyMapper;
    
    @Autowired
    ProductCategoryMapper productCategoryMapper;

    @Autowired
    ProfileSearchRepository profileSearchRepository;
    
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
    DiscountMapper  discountMapper;
    
    @Autowired
    TaxMapper  taxMapper;
    
    @Autowired
    ProductVariantService productVariantService;
    
    @Autowired
    ProductExtraService productExtraService;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper, ProductSearchRepository productSearchRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.productSearchRepository = productSearchRepository;
    }
    
    

	public Page<ProductDTO> findAllByProductCategoryId( Pageable pageable,Long categoryId) {
		log.debug("Request to get all Products by Product Category ID");
            return productRepository.findAllByCategoryId(pageable,categoryId)
                    .map(productMapper::toDto);
	}

	public List<ProductDTOFull> findAllByShopId(Long shopId) {
		log.debug("Request to get all Products by Product findAllByShopId ");
		List<ProductDTOFull> productDTOFullList = new ArrayList<ProductDTOFull>();
        List<Product> productList =  productRepository.findAllByShopId(shopId);
        productList.forEach(product -> {
        	ProductDTOFull profileDTOFull = new ProductDTOFull();
        	profileDTOFull.setProductDTO(productMapper.toDto(product));
        	//profileDTOFull.setShopDTO(shopMapper.toDto(product.getShop()));
        	//profileDTOFull.setCompanyDTO(companyMapper.toDto(product.getShop().getCompany()));
        	List <Discount> discountList = new ArrayList<Discount>();
        	discountList.addAll(product.getShop().getDiscounts());
        	profileDTOFull.setDiscountDTOList(discountMapper.toDto(discountList));
     
        	List <Tax> taxList = new ArrayList<Tax>();
        	taxList.addAll(product.getShop().getTaxes());
        	profileDTOFull.setTaxDTOList(taxMapper.toDto(taxList));
        	//profileDTOFull.setPaymentMethodList(paymentMethodService.findAllByShopId(product.getShop().getId()));
        	//List <PaymentMethodConfigDTO> paymentMethodConfigList = new ArrayList<PaymentMethodConfigDTO>();
        	//profileDTOFull.getPaymentMethodList().forEach(paymentMethod -> {
        	//	paymentMethodConfigList.addAll(paymentMethodConfigService.findAllByPaymentMethodId(paymentMethod.getId()));
        		
        	//});
        	
        	//profileDTOFull.setPaymentMethodConfigDTOList(paymentMethodConfigList);
        	
        	//List <ProductCategoryDTO> productCategoryDTOList = new ArrayList<ProductCategoryDTO>();
        	
        	//productCategoryDTOList.add(productCategoryMapper.toDto(product.getCategory()));
        	//profileDTOFull.setProductCategoryDTOList(productCategoryDTOList);
        	
        	profileDTOFull.setProductVariantsDTOList(productVariantService.findAllByProductId(product.getId()));
        	
        	profileDTOFull.setProductExtraDTOList(productExtraService.findAllByProductId(product.getId()));
        	
        	productDTOFullList.add(profileDTOFull);
       
        });
        
        
      
                
        return productDTOFullList;
	}
	

    /**
     * Save a product.
     *
     * @param productDTO the entity to save
     * @return the persisted entity
     */
    public ProductDTO save(ProductDTO productDTO) {
        log.debug("Request to save Product : {}", productDTO);

        Product product = productMapper.toEntity(productDTO);
        product = productRepository.save(product);
        ProductDTO result = productMapper.toDto(product);
        productSearchRepository.save(product);
        return result;
    }

    /**
     * Get all the products.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Products");
        return productRepository.findAll(pageable)
            .map(productMapper::toDto);
    }


    /**
     * Get one product by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ProductDTO> findOne(Long id) {
        log.debug("Request to get Product : {}", id);
        return productRepository.findById(id)
            .map(productMapper::toDto);
    }

    /**
     * Delete the product by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Product : {}", id);
        productRepository.deleteById(id);
        productSearchRepository.deleteById(id);
    }

    /**
     * Search for the product corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ProductDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Products for query {}", query);
        return productSearchRepository.search(queryStringQuery(query), pageable)
            .map(productMapper::toDto);
    }
}
