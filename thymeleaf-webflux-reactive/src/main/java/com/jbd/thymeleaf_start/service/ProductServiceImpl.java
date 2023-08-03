package com.jbd.thymeleaf_start.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.reactive.TransactionalOperator;

import com.jbd.thymeleaf_start.domain.Product;
import com.jbd.thymeleaf_start.model.ProductDTO;
import com.jbd.thymeleaf_start.repos.ProductRepository;

import reactor.core.publisher.Mono;


@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    @Override
    public Mono<Page<ProductDTO>> findAll(final Pageable pageable) {
    	final int pageSize = pageable.getPageSize();
        final int currentPage = pageable.getPageNumber();
        
        return this.productRepository.findAllBy(pageable).flatMap(this::mapToDTO).collectList()
                              .zipWith(this.productRepository.count())
                              .map(i -> new PageImpl<>(i.getT1(), PageRequest.of(currentPage, pageSize), i.getT2()));
    }

    @Override
    public Mono<ProductDTO> get(final Long id) {
    	return productRepository.findById(id).flatMap(this::mapToDTO);
    }

    @Override
    public Mono<ProductDTO> create(final ProductDTO productDTO) {
    	return productRepository.save(mapToEntity(productDTO, new Product())).flatMap(this::mapToDTO);
    }

    @Override
    public Mono<ProductDTO> update(final Long id, final ProductDTO productDTO) {
    	
    	return this.productRepository.findById(id)
    	    .doOnNext(p -> this.mapToEntity(productDTO, p))
    	    .flatMap(this.productRepository::save)
    	    .flatMap(this::mapToDTO);
    }

    @Transactional
    @Override
    public Mono<Void> delete(final Long id) {
       return this.productRepository.deleteById(id);
    }

    private Mono<ProductDTO> mapToDTO(final Product product) {
    	final ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setPrice(product.getPrice());
        productDTO.setDiscount(product.getDiscount());
        productDTO.setStockStatus(product.getStockStatus());
        productDTO.setSku(product.getSku());
        return Mono.just(productDTO);
    }

    private Product mapToEntity(final ProductDTO productDTO, final Product product) {
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setDiscount(productDTO.getDiscount());
        product.setStockStatus(productDTO.getStockStatus());
        product.setSku(productDTO.getSku());
        return product;
    }

    @Override
    public Mono<Boolean> nameExists(final String name) {
        return productRepository.existsByNameIgnoreCase(name);
    }

}
