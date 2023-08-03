package com.jbd.thymeleaf_start.controller;

import java.util.concurrent.ExecutionException;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.result.view.Rendering;
import org.springframework.web.server.WebSession;

import com.jbd.thymeleaf_start.model.ProductDTO;
import com.jbd.thymeleaf_start.service.ProductService;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;


@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(final ProductService productService) {
        this.productService = productService;
    }

    // to show products list page
    @GetMapping
    public Mono<Rendering> list(@PageableDefault(size = 10) final Pageable pageable,
            final Model  model, final WebSession session) {
    
        return setRedirectAttributes(model, session).thenReturn(Rendering.view("product/list") 
        		.modelAttribute("products", productService.findAll(pageable)).build());
        			
    }
    
    // to show a page to add a product
    @GetMapping("/add")
    public Mono<Rendering> add(@ModelAttribute("product") final ProductDTO productDTO) {
        return Mono.just(Rendering.view("product/add").build());
    }

    // To save a product in DB
    @PostMapping("/add")
    public Mono<Rendering> add(@ModelAttribute("product") @Valid final ProductDTO productDTO,
    			final BindingResult bindingResult, final WebSession session) {
    	
    	  return productService.nameExists(productDTO.getName()).toFuture().thenApplyAsync(bool -> {

	    	  if (!bindingResult.hasFieldErrors("name") && bool) {
	               bindingResult.addError(new FieldError("product", "name", productDTO.getName(), false, null, null, "this product name already exists!"));
	           }
	          if (bindingResult.hasErrors()) {
	               return Mono.just(Rendering.view("product/add").build());
	           } 
	    		
	           productService.create(productDTO).subscribe();
	           return addRedirectAttributes(session, "MSG_SUCCESS", String.format("Product %s added successfully!", productDTO.getName()))
	        		   .thenReturn(Rendering.redirectTo("/products").build());
	        		        	
          }).join();
    }

    // To show edit product page with product details
    @GetMapping("/edit/{id}")
    public Mono<Rendering> edit(@PathVariable final Long id, final Model model) { 
       
        return Mono.just(Rendering.view("product/edit").modelAttribute("product", productService.get(id)).build());
    }

    // For updating product to DB
    @PostMapping("/edit/{id}")
    public Mono<Rendering> edit(@PathVariable final Long id, @ModelAttribute("product") @Valid final ProductDTO productDTO,
            final BindingResult bindingResult, final WebSession session) {
    	
    	return productService.get(id).toFuture().thenApplyAsync(pdto -> {
    		
    		 return productService.nameExists(productDTO.getName()).toFuture().thenApplyAsync(bool -> {
    			if (!bindingResult.hasFieldErrors("name") && !productDTO.getName().equalsIgnoreCase(pdto.getName()) && bool) {
                	bindingResult.addError(new FieldError("product", "name", productDTO.getName(), false, null, null, "this product name already exists!"));
                }
    			if (bindingResult.hasErrors()) {
    		        	return Mono.just(Rendering.view("product/edit").build());
    		    }
    			  			
				this.productService.update(id, productDTO).subscribe();
				
				return addRedirectAttributes(session, "MSG_SUCCESS", String.format("Product %s updated successfully!", productDTO.getName()))
		        		   .thenReturn(Rendering.redirectTo("/products").build());
   		     
    		}).join();
   
    	}).join();
    	
    }

    // For deleting Product in DB
    @PostMapping("/delete/{id}")
    public Mono<Rendering> delete(@PathVariable final Long id, final WebSession session) throws InterruptedException, ExecutionException {
        productService.delete(id).subscribe();
        return addRedirectAttributes(session, "MSG_SUCCESS", String.format("Product %s deleted successfully!", id))
     		   .thenReturn(Rendering.redirectTo("/products").build());
    }
    
    
    private Mono<Void> setRedirectAttributes(final Model model, final WebSession session) {
        return Mono.fromRunnable(
            () -> {         	
            	if(session.getAttribute("MSG_SUCCESS") != null) {
            		 model.addAttribute("MSG_SUCCESS", session.getAttribute("MSG_SUCCESS"));
            		 session.getAttributes().remove("MSG_SUCCESS");
            	}
            	
            });
      }
    
    private Mono<Void> addRedirectAttributes(final WebSession session, final String key, final String value) {
        return Mono.fromRunnable(
            () -> {         	
            	if(session.getAttribute("MSG_SUCCESS") == null) {
            		 session.getAttributes().put(key, value);
            	}
            });
      }

}
