package com.jbd.thymeleaf_start.repos;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.jbd.thymeleaf_start.domain.Product;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface ProductRepository extends R2dbcRepository<Product, Long> {
	
   Flux<Product> findAllBy(Pageable pageable);

   Mono<Boolean> existsByNameIgnoreCase(final String name);
   
   Flux<Product> existsByStockStatus(final String stockStatus);

}
