package com.jbd.thymeleaf_start.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageListModel {

	private List<PageModel> pages;
    private long start;
    private long end;
    private long totalItems;
    private long totalPages;
    private long size;
}
