package com.jbd.thymeleaf_start.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.jbd.thymeleaf_start.model.ProductDTO;


public interface ProductService {

    List<ProductDTO> findAll();

    ProductDTO get(Long id);

    Long create(ProductDTO productDTO);

    void update(Long id, ProductDTO productDTO);

    void delete(Long id);

    boolean nameExists(String name);

	Page<ProductDTO> findAll(Pageable pageable);

	List<ProductDTO> findAll(Sort sort);

}
