package com.jbd.thymeleaf_start.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Table("PRODUCT")
@Data // lombok
@AllArgsConstructor @NoArgsConstructor
public class Product {

	@Id
    private Long id;

    private String name;

    private Integer price;

    private Integer discount;

    @Column("stock_status")
    private String stockStatus;

    private String sku;

}
