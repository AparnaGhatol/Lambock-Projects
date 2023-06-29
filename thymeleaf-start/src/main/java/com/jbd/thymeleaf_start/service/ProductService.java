package com.jbd.thymeleaf_start.service;

import com.jbd.thymeleaf_start.model.ProductDTO;
import java.util.List;


public interface ProductService {

    List<ProductDTO> findAll();

    ProductDTO get(Long id);

    Long create(ProductDTO productDTO);

    void update(Long id, ProductDTO productDTO);

    void delete(Long id);

    boolean nameExists(String name);

}
