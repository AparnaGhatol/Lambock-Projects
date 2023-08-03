package com.jbd.thymeleaf_start.service;

import java.util.concurrent.ExecutionException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jbd.thymeleaf_start.model.ProductDTO;

import reactor.core.publisher.Mono;


public interface ProductService {

    Mono<Page<ProductDTO>> findAll(final Pageable pageable);

    Mono<ProductDTO> get(Long id);

    Mono<ProductDTO> create(ProductDTO productDTO);

    Mono<ProductDTO> update(Long id, ProductDTO productDTO);

    Mono<Void> delete(Long id);

    Mono<Boolean> nameExists(String name);

}
