package com.jbd.chatgpt.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jbd.chatgpt.model.MarkdownModel;
import com.jbd.chatgpt.service.MarkDownToHtmlService;

import jakarta.validation.Valid;


@RestController
@RequestMapping(value = "/markdown", produces = MediaType.APPLICATION_JSON_VALUE)
public class MarkdownApiController {
	

	@Autowired
	private MarkDownToHtmlService markDownToHtmlService;

    @PostMapping("/convert-to-html")
    public MarkdownModel chatGptRequest(@ModelAttribute("markdown") @Valid final MarkdownModel markdownModel,
			final BindingResult bindingResult) {
    	
    	markdownModel.setHtmlResponse(markDownToHtmlService.markdownToHtml(markdownModel.getMarkdownCode()));
  
		return markdownModel;
    }

}
