package com.luulsolutions.luulpos.service.mapper;

import com.luulsolutions.luulpos.domain.*;
import com.luulsolutions.luulpos.service.dto.SectionTableDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity SectionTable and its DTO SectionTableDTO.
 */
@Mapper(componentModel = "spring", uses = {ShopSectionMapper.class})
public interface SectionTableMapper extends EntityMapper<SectionTableDTO, SectionTable> {

    @Mapping(source = "shopSections.id", target = "shopSectionsId")
    @Mapping(source = "shopSections.sectionName", target = "shopSectionsSectionName")
    SectionTableDTO toDto(SectionTable sectionTable);

    @Mapping(source = "shopSectionsId", target = "shopSections")
    SectionTable toEntity(SectionTableDTO sectionTableDTO);

    default SectionTable fromId(Long id) {
        if (id == null) {
            return null;
        }
        SectionTable sectionTable = new SectionTable();
        sectionTable.setId(id);
        return sectionTable;
    }
}
