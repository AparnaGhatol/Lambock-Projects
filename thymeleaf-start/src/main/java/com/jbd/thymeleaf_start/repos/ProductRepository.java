package com.jbd.thymeleaf_start.repos;

import org.springframework.data.repository.CrudRepository;

import com.jbd.thymeleaf_start.domain.Product;


public interface ProductRepository extends CrudRepository<Product, Long> {

    boolean existsByNameIgnoreCase(String name);

}
