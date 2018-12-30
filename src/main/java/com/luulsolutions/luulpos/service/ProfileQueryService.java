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

import com.luulsolutions.luulpos.domain.Profile;
import com.luulsolutions.luulpos.domain.*; // for static metamodels
import com.luulsolutions.luulpos.repository.ProfileRepository;
import com.luulsolutions.luulpos.repository.search.ProfileSearchRepository;
import com.luulsolutions.luulpos.service.dto.ProfileCriteria;
import com.luulsolutions.luulpos.service.dto.ProfileDTO;
import com.luulsolutions.luulpos.service.mapper.ProfileMapper;

/**
 * Service for executing complex queries for Profile entities in the database.
 * The main input is a {@link ProfileCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProfileDTO} or a {@link Page} of {@link ProfileDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProfileQueryService extends QueryService<Profile> {

    private final Logger log = LoggerFactory.getLogger(ProfileQueryService.class);

    private final ProfileRepository profileRepository;

    private final ProfileMapper profileMapper;

    private final ProfileSearchRepository profileSearchRepository;

    public ProfileQueryService(ProfileRepository profileRepository, ProfileMapper profileMapper, ProfileSearchRepository profileSearchRepository) {
        this.profileRepository = profileRepository;
        this.profileMapper = profileMapper;
        this.profileSearchRepository = profileSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ProfileDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProfileDTO> findByCriteria(ProfileCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Profile> specification = createSpecification(criteria);
        return profileMapper.toDto(profileRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProfileDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProfileDTO> findByCriteria(ProfileCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Profile> specification = createSpecification(criteria);
        return profileRepository.findAll(specification, page)
            .map(profileMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProfileCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Profile> specification = createSpecification(criteria);
        return profileRepository.count(specification);
    }

    /**
     * Function to convert ProfileCriteria to a {@link Specification}
     */
    private Specification<Profile> createSpecification(ProfileCriteria criteria) {
        Specification<Profile> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Profile_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), Profile_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), Profile_.lastName));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Profile_.email));
            }
            if (criteria.getUserType() != null) {
                specification = specification.and(buildSpecification(criteria.getUserType(), Profile_.userType));
            }
            if (criteria.getDateOfBirth() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateOfBirth(), Profile_.dateOfBirth));
            }
            if (criteria.getGender() != null) {
                specification = specification.and(buildSpecification(criteria.getGender(), Profile_.gender));
            }
            if (criteria.getRegisterationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRegisterationDate(), Profile_.registerationDate));
            }
            if (criteria.getLastAccess() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastAccess(), Profile_.lastAccess));
            }
            if (criteria.getProfileStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getProfileStatus(), Profile_.profileStatus));
            }
            if (criteria.getTelephone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTelephone(), Profile_.telephone));
            }
            if (criteria.getMobile() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMobile(), Profile_.mobile));
            }
            if (criteria.getHourlyPayRate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHourlyPayRate(), Profile_.hourlyPayRate));
            }
            if (criteria.getThumbnailPhotoUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getThumbnailPhotoUrl(), Profile_.thumbnailPhotoUrl));
            }
            if (criteria.getFullPhotoUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFullPhotoUrl(), Profile_.fullPhotoUrl));
            }
            if (criteria.getActive() != null) {
                specification = specification.and(buildSpecification(criteria.getActive(), Profile_.active));
            }
            if (criteria.getShopChangeId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getShopChangeId(), Profile_.shopChangeId));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(Profile_.user, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getShopId() != null) {
                specification = specification.and(buildSpecification(criteria.getShopId(),
                    root -> root.join(Profile_.shop, JoinType.LEFT).get(Shop_.id)));
            }
        }
        return specification;
    }
}
