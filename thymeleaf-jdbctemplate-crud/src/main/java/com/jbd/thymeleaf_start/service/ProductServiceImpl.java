package com.jbd.thymeleaf_start.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jbd.thymeleaf_start.domain.Product;
import com.jbd.thymeleaf_start.model.ProductDTO;
import com.jbd.thymeleaf_start.repos.ProductJdbcTemplate;
import com.jbd.thymeleaf_start.util.NotFoundException;
import com.jbd.thymeleaf_start.util.ProductMappingUtils;


@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductJdbcTemplate productRepository;

    public ProductServiceImpl(final ProductJdbcTemplate productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductDTO> findAll() {
        return productRepository.findAll();
    
    }
    
    @Override
    public Page<ProductDTO> findAll(final Pageable pageable) {
        return productRepository.findAll(pageable);
    }
    
    @Override
    public List<ProductDTO> findAll(final Sort sort) {
       return productRepository.findAll(sort);
 
    }

    @Override
    public ProductDTO get(final Long id) {
        return productRepository.findById(id)
                .map(product -> ProductMappingUtils.mapToDTO(product, new ProductDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public Long create(final ProductDTO productDTO) {
        final Product product = ProductMappingUtils.mapToEntity(productDTO, new Product());
        int records = productRepository.save(product);
        
        return product.getId();
    }

    @Override
    public void update(final Long id, final ProductDTO productDTO) {
        final Product product = productRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        productRepository.update(ProductMappingUtils.mapToEntity(productDTO, product));
    }

    @Override
    public void delete(final Long id) {
        productRepository.delete(id);
    }


    @Override
    public boolean nameExists(final String name) {
        Optional<ProductDTO> product = productRepository.findByProductName(name);
        
        return !product.isEmpty();
    }

}
