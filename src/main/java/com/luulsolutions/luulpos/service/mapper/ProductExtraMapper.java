package com.luulsolutions.luulpos.service.mapper;

import com.luulsolutions.luulpos.domain.*;
import com.luulsolutions.luulpos.service.dto.ProductExtraDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ProductExtra and its DTO ProductExtraDTO.
 */
@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface ProductExtraMapper extends EntityMapper<ProductExtraDTO, ProductExtra> {

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.productName", target = "productProductName")
    ProductExtraDTO toDto(ProductExtra productExtra);

    @Mapping(source = "productId", target = "product")
    ProductExtra toEntity(ProductExtraDTO productExtraDTO);

    default ProductExtra fromId(Long id) {
        if (id == null) {
            return null;
        }
        ProductExtra productExtra = new ProductExtra();
        productExtra.setId(id);
        return productExtra;
    }
}
