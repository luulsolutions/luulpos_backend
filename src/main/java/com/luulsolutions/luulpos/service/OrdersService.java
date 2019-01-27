package com.luulsolutions.luulpos.service;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.time.ZonedDateTime;
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

import com.luulsolutions.luulpos.domain.Orders;
import com.luulsolutions.luulpos.domain.OrdersLine;
import com.luulsolutions.luulpos.domain.OrdersLineExtra;
import com.luulsolutions.luulpos.domain.OrdersLineVariant;
import com.luulsolutions.luulpos.domain.Payment;
import com.luulsolutions.luulpos.repository.OrdersLineExtraRepository;
import com.luulsolutions.luulpos.repository.OrdersLineRepository;
import com.luulsolutions.luulpos.repository.OrdersLineVariantRepository;
import com.luulsolutions.luulpos.repository.OrdersRepository;
import com.luulsolutions.luulpos.repository.PaymentRepository;
import com.luulsolutions.luulpos.repository.search.OrdersLineExtraSearchRepository;
import com.luulsolutions.luulpos.repository.search.OrdersLineSearchRepository;
import com.luulsolutions.luulpos.repository.search.OrdersLineVariantSearchRepository;
import com.luulsolutions.luulpos.repository.search.OrdersSearchRepository;
import com.luulsolutions.luulpos.repository.search.PaymentSearchRepository;
import com.luulsolutions.luulpos.service.dto.OrdersDTO;
import com.luulsolutions.luulpos.service.dto.OrdersDTOFull;
import com.luulsolutions.luulpos.service.dto.OrdersLineDTO;
import com.luulsolutions.luulpos.service.dto.OrdersLineDTOFull;
import com.luulsolutions.luulpos.service.dto.OrdersLineExtraDTO;
import com.luulsolutions.luulpos.service.dto.OrdersLineVariantDTO;
import com.luulsolutions.luulpos.service.dto.OrdersLineVariantDTOFull;
import com.luulsolutions.luulpos.service.dto.PaymentDTO;
import com.luulsolutions.luulpos.service.mapper.OrdersLineExtraMapper;
import com.luulsolutions.luulpos.service.mapper.OrdersLineMapper;
import com.luulsolutions.luulpos.service.mapper.OrdersLineVariantMapper;
import com.luulsolutions.luulpos.service.mapper.OrdersMapper;
import com.luulsolutions.luulpos.service.mapper.PaymentMapper;

/**
 * Service Implementation for managing Orders.
 */
@Service
@Transactional
public class OrdersService {

    private final Logger log = LoggerFactory.getLogger(OrdersService.class);

    private final OrdersRepository ordersRepository;

    private final OrdersMapper ordersMapper;

    private final OrdersSearchRepository ordersSearchRepository;

    @Autowired
    OrdersLineRepository ordersLineRepository;
    
    @Autowired
    OrdersLineMapper ordersLineMapper;

    @Autowired
    OrdersLineSearchRepository ordersLineSearchRepository;
    
    @Autowired
    OrdersLineVariantRepository ordersLineVariantRepository;

    @Autowired
    OrdersLineVariantMapper ordersLineVariantMapper;

    @Autowired
    OrdersLineVariantSearchRepository ordersLineVariantSearchRepository;
    
    @Autowired
    OrdersLineExtraRepository ordersLineExtraRepository;
    
    @Autowired
    OrdersLineExtraMapper ordersLineExtraMapper;
    
    @Autowired
    OrdersLineExtraSearchRepository ordersLineExtraSearchRepository;
    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    PaymentMapper paymentMapper;
    
    @Autowired
    PaymentSearchRepository paymentSearchRepository;
    
    public OrdersService(OrdersRepository ordersRepository, OrdersMapper ordersMapper, OrdersSearchRepository ordersSearchRepository) {
        this.ordersRepository = ordersRepository;
        this.ordersMapper = ordersMapper;
        this.ordersSearchRepository = ordersSearchRepository;
    }
    
    @Transactional
    public OrdersDTOFull saveOdersDTOFull(OrdersDTOFull ordersDTOFull) {
        log.debug("Request to save Orders full : {}", ordersDTOFull.getOrderDTO());
        OrdersDTO ordersDTO = ordersDTOFull.getOrderDTO(); 
        ordersDTO.setOrderDate(ZonedDateTime.now());
        Orders orders = ordersMapper.toEntity(ordersDTO);
        orders = ordersRepository.save(orders);
        ordersDTOFull.setId(orders.getId());
        ordersDTO.setId(orders.getId());
		List <OrdersLineDTOFull> ordersLineDTOFullList = new ArrayList<OrdersLineDTOFull> ();
		
		ordersDTOFull.getPaymentDTOList().forEach(paymentDTO -> {
			paymentDTO.setPaymentDate(ZonedDateTime.now());
			 paymentDTO.setOrderId(ordersDTOFull.getId());
		     Payment payment = paymentMapper.toEntity(paymentDTO);
		        payment = paymentRepository.save(payment);
		        PaymentDTO result9 = paymentMapper.toDto(payment);
		        paymentSearchRepository.save(payment);
		        paymentDTO.setId(result9.getId());
		});
		
        ordersDTOFull.getOrdersLineDTOFullList().forEach(ordersLineDTOFull -> {
        	
         			OrdersLineDTO ordersLineDTO = ordersLineDTOFull.getOrdersLineDTO();
         			OrdersLine ordersLine = ordersLineMapper.toEntity(ordersLineDTO);
         	        ordersLine = ordersLineRepository.save(ordersLine);
         	        OrdersLineDTO result = ordersLineMapper.toDto(ordersLine);
         	        ordersLineSearchRepository.save(ordersLine);
         			ordersLineDTOFull.setId(result.getId());
         			ordersLineDTOFull.setOrdersLineDTO(result);
         			ordersLineDTOFullList.add(ordersLineDTOFull);
        			List <OrdersLineVariantDTOFull> ordersLineVariantsDTOFullList = new ArrayList<OrdersLineVariantDTOFull> ();

         			ordersLineDTOFull.getOrdersLineVariantsDTOFullList().forEach(ordersLineVariantsDTOFull -> {
         				OrdersLineVariant ordersLineVariant = ordersLineVariantMapper.toEntity(ordersLineVariantsDTOFull.getOrdersLineVariantDTO());
         		        ordersLineVariant = ordersLineVariantRepository.save(ordersLineVariant);
         		        OrdersLineVariantDTO ordersLineVariantDTO = ordersLineVariantMapper.toDto(ordersLineVariant);
         		        ordersLineVariantSearchRepository.save(ordersLineVariant);
         		        ordersLineVariantsDTOFull.setId(ordersLineVariantDTO.getId());
         		        ordersLineVariantsDTOFull.setOrdersLineVariantDTO(ordersLineVariantDTO);
         		       ordersLineVariantsDTOFullList.add(ordersLineVariantsDTOFull);
        				List <OrdersLineExtraDTO> ordersLineExtraDTOList = new ArrayList<OrdersLineExtraDTO> ();
         				   if (ordersLineVariantsDTOFull.getOrdersLineExtraDTOList() != null && ordersLineVariantsDTOFull.getOrdersLineExtraDTOList().size() > 0) {
         					  ordersLineVariantsDTOFull.getOrdersLineExtraDTOList().forEach(ordersLineExtraDTO -> {
         						if (ordersLineExtraDTO != null) {
         							try {		
         								OrdersLineExtra ordersLineExtra = ordersLineExtraMapper.toEntity(ordersLineExtraDTO);
         								OrdersLineExtra ordersLineExtraEntity = ordersLineExtraRepository.save(ordersLineExtra);
         						        OrdersLineExtraDTO result3 = ordersLineExtraMapper.toDto(ordersLineExtraEntity);
         						        ordersLineExtraSearchRepository.save(ordersLineExtra);
         						       ordersLineExtraDTO.setId(result3.getId());
         						       ordersLineExtraDTOList.add(ordersLineExtraDTO);
         							} catch (Exception e) {
         								log.debug("error happened " + e.getMessage());
         							}
         						}
         					});
         				    }
         				  ordersLineVariantsDTOFull.setOrdersLineExtraDTOList(ordersLineExtraDTOList);;
         			});
         			ordersLineDTOFull.setOrdersLineVariantsDTOFullList(ordersLineVariantsDTOFullList);

         		});
               ordersDTOFull.setOrdersLineDTOFullList(ordersLineDTOFullList);
        orders = ordersRepository.save(orders);
        OrdersDTO result = ordersMapper.toDto(orders);
        ordersSearchRepository.save(orders);      
        return ordersDTOFull;
    }

    /**
     * Save a orders.
     *
     * @param ordersDTO the entity to save
     * @return the persisted entity
     */
    public OrdersDTO save(OrdersDTO ordersDTO) {
        log.debug("Request to save Orders : {}", ordersDTO);

        Orders orders = ordersMapper.toEntity(ordersDTO);
        orders = ordersRepository.save(orders);
        OrdersDTO result = ordersMapper.toDto(orders);
        ordersSearchRepository.save(orders);
        return result;
    }

    /**
     * Get all the orders.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<OrdersDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Orders");
        return ordersRepository.findAll(pageable)
            .map(ordersMapper::toDto);
    }


    /**
     * Get one orders by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<OrdersDTO> findOne(Long id) {
        log.debug("Request to get Orders : {}", id);
        return ordersRepository.findById(id)
            .map(ordersMapper::toDto);
    }

    /**
     * Delete the orders by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Orders : {}", id);
        ordersRepository.deleteById(id);
        ordersSearchRepository.deleteById(id);
    }

    /**
     * Search for the orders corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<OrdersDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Orders for query {}", query);
        return ordersSearchRepository.search(queryStringQuery(query), pageable)
            .map(ordersMapper::toDto);
    }
    
    public List<OrdersDTOFull> findAllOrdersFull(Pageable pageable, Long shopId, String orderStatus) {
	    log.debug("Request to findAllOrdersFull by shopId: {} and status {} ", shopId, orderStatus);
	   List <OrdersDTOFull> orderDTOFullList = new ArrayList<OrdersDTOFull>();
	   Page <Orders>  ordersList = ordersRepository.findAllByShopId(pageable, shopId);

	   ordersList.getContent().forEach(orders -> {
        	OrdersDTO ordersDTO = ordersMapper.toDto(orders);
        	OrdersDTOFull orderDTOFull = new OrdersDTOFull();
        	if (ordersDTO.getOrderStatus().toString().equalsIgnoreCase(orderStatus)) {
        		orderDTOFull.setOrderDTO(ordersDTO);
        		orderDTOFull.setId(ordersDTO.getId());
        		List <OrdersLineDTOFull> ordersLineDTOFullList = new ArrayList<OrdersLineDTOFull> ();
                
    			List <PaymentDTO> paymentDTOList = new ArrayList<PaymentDTO> ();
                List <Payment> paymentDTOs = paymentRepository.findAllByOrderId(orders.getId());
        		if (paymentDTOs != null && paymentDTOs.size() >0) {
        			paymentDTOs.forEach(payments ->{
        				
        				paymentDTOList.add(paymentMapper.toDto(payments));
        			});
        		}
        		orders.getOrdersLines().forEach(ordersLine -> {
        			List <OrdersLineVariantDTOFull> ordersLineVariantsDTOFullList = new ArrayList<OrdersLineVariantDTOFull> ();
        			OrdersLineDTOFull ordersLineDTOFull = new OrdersLineDTOFull();
        			OrdersLineDTO ordersLineDTO = ordersLineMapper.toDto(ordersLine);
        			ordersLineDTOFull.setId(ordersLineDTO.getId());
        			ordersLineDTOFull.setOrdersLineDTO(ordersLineDTO);
        			ordersLineDTOFullList.add(ordersLineDTOFull);
        			ordersLine.getOrdersLineVariants().forEach(ordersLineVariant -> {
        				OrdersLineVariantDTOFull ordersLineVariantDTOFull = new OrdersLineVariantDTOFull();
        				OrdersLineVariantDTO ordersLineVariantDTO = ordersLineVariantMapper.toDto(ordersLineVariant);
        				ordersLineVariantDTOFull.setId(ordersLineVariantDTO.getId());
        				ordersLineVariantDTOFull.setOrdersLineVariantDTO(ordersLineVariantDTO);
        				ordersLineVariantsDTOFullList.add(ordersLineVariantDTOFull);
        				List <OrdersLineExtraDTO> ordersLineExtraDTOList = new ArrayList<OrdersLineExtraDTO> ();
        				    if (ordersLineVariant.getOrdersLineExtras() != null && ordersLineVariant.getOrdersLineExtras().size() > 0) {
        					ordersLineVariant.getOrdersLineExtras().forEach(ordersLineExtra -> {
        						if (ordersLineExtra != null) {
        							try {
        								OrdersLineExtraDTO ordersLineExtraDTO = ordersLineExtraMapper.toDto(ordersLineExtra);
        								ordersLineExtraDTOList.add(ordersLineExtraDTO);
        							} catch (Exception e) {
        								log.debug("error happened " + e.getMessage());
        							}
        						}
        					});
        				    }
        					ordersLineVariantDTOFull.setOrdersLineExtraDTOList(ordersLineExtraDTOList);;
        			});
        			ordersLineDTOFull.setOrdersLineVariantsDTOFullList(ordersLineVariantsDTOFullList);

        		});
        		orderDTOFull.setOrdersLineDTOFullList(ordersLineDTOFullList);
        		orderDTOFull.setPaymentDTOList(paymentDTOList);
        		orderDTOFullList.add(orderDTOFull);
        	}
         });
	   return orderDTOFullList;
	}
    
}
