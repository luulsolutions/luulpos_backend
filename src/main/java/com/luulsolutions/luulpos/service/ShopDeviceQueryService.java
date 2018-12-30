package com.luulsolutions.luulpos.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.luulsolutions.luulpos.domain.ShopDevice;
import com.luulsolutions.luulpos.domain.*; // for static metamodels
import com.luulsolutions.luulpos.repository.ShopDeviceRepository;
import com.luulsolutions.luulpos.repository.search.ShopDeviceSearchRepository;
import com.luulsolutions.luulpos.service.dto.ShopDeviceCriteria;
import com.luulsolutions.luulpos.service.dto.ShopDeviceDTO;
import com.luulsolutions.luulpos.service.mapper.ShopDeviceMapper;

/**
 * Service for executing complex queries for ShopDevice entities in the database.
 * The main input is a {@link ShopDeviceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ShopDeviceDTO} or a {@link Page} of {@link ShopDeviceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ShopDeviceQueryService extends QueryService<ShopDevice> {

    private final Logger log = LoggerFactory.getLogger(ShopDeviceQueryService.class);

    private final ShopDeviceRepository shopDeviceRepository;

    private final ShopDeviceMapper shopDeviceMapper;

    private final ShopDeviceSearchRepository shopDeviceSearchRepository;

    public ShopDeviceQueryService(ShopDeviceRepository shopDeviceRepository, ShopDeviceMapper shopDeviceMapper, ShopDeviceSearchRepository shopDeviceSearchRepository) {
        this.shopDeviceRepository = shopDeviceRepository;
        this.shopDeviceMapper = shopDeviceMapper;
        this.shopDeviceSearchRepository = shopDeviceSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ShopDeviceDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ShopDeviceDTO> findByCriteria(ShopDeviceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ShopDevice> specification = createSpecification(criteria);
        return shopDeviceMapper.toDto(shopDeviceRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ShopDeviceDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ShopDeviceDTO> findByCriteria(ShopDeviceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ShopDevice> specification = createSpecification(criteria);
        return shopDeviceRepository.findAll(specification, page)
            .map(shopDeviceMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ShopDeviceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ShopDevice> specification = createSpecification(criteria);
        return shopDeviceRepository.count(specification);
    }

    /**
     * Function to convert ShopDeviceCriteria to a {@link Specification}
     */
    private Specification<ShopDevice> createSpecification(ShopDeviceCriteria criteria) {
        Specification<ShopDevice> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ShopDevice_.id));
            }
            if (criteria.getDeviceName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDeviceName(), ShopDevice_.deviceName));
            }
            if (criteria.getDeviceModel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDeviceModel(), ShopDevice_.deviceModel));
            }
            if (criteria.getRegisteredDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRegisteredDate(), ShopDevice_.registeredDate));
            }
            if (criteria.getShopId() != null) {
                specification = specification.and(buildSpecification(criteria.getShopId(),
                    root -> root.join(ShopDevice_.shop, JoinType.LEFT).get(Shop_.id)));
            }
        }
        return specification;
    }
}
