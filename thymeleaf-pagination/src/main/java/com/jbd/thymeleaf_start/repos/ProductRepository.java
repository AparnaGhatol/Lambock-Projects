package com.jbd.thymeleaf_start.repos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.jbd.thymeleaf_start.domain.Product;


public interface ProductRepository extends CrudRepository<Product, Long> , PagingAndSortingRepository<Product, Long> {

    boolean existsByNameIgnoreCase(String name);

}
