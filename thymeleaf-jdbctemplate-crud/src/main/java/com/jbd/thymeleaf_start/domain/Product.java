package com.jbd.thymeleaf_start.domain;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Product {

    private Long id;

    private String name;

    private Integer price;

    private Integer discount;

    private String stockStatus;

    private String sku;

}
