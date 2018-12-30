package com.luulsolutions.luulpos.service;

import com.luulsolutions.luulpos.domain.Profile;
import com.luulsolutions.luulpos.repository.ProfileRepository;
import com.luulsolutions.luulpos.repository.search.ProfileSearchRepository;
import com.luulsolutions.luulpos.service.dto.ProfileDTO;
import com.luulsolutions.luulpos.service.mapper.ProfileMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
