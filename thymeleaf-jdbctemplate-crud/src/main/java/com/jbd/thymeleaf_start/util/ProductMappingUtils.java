package com.jbd.thymeleaf_start.util;

import com.jbd.thymeleaf_start.domain.Product;
import com.jbd.thymeleaf_start.model.ProductDTO;

public class ProductMappingUtils {

    public static ProductDTO mapToDTO(final Product product, final ProductDTO productDTO) {
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setPrice(product.getPrice());
        productDTO.setDiscount(product.getDiscount());
        productDTO.setStockStatus(product.getStockStatus());
        productDTO.setSku(product.getSku());
        return productDTO;
    }

    public static Product mapToEntity(final ProductDTO productDTO, final Product product) {
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setDiscount(productDTO.getDiscount());
        product.setStockStatus(productDTO.getStockStatus());
        product.setSku(productDTO.getSku());
        return product;
    }
}
