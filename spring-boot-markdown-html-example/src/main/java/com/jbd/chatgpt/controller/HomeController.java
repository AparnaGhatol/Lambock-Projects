package com.jbd.chatgpt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.jbd.chatgpt.model.MarkdownModel;
import com.jbd.chatgpt.service.MarkDownToHtmlService;


@Controller
public class HomeController {

	@Autowired
	private MarkDownToHtmlService markDownToHtmlService;
	
    @GetMapping("/")
    public String index(@ModelAttribute("markdown") final MarkdownModel markdown, Model model) {
    	
    	final String markDownCode = """ 
# Heading1
## Heading2
### Heading3
#### Heading4
##### Heading5
###### Heading6 
				""";
    	
    	markdown.setMarkdownCode(markDownCode);
    	markdown.setHtmlResponse(markDownToHtmlService.markdownToHtml(markDownCode));
    	
    	model.addAttribute("markdown", markdown);
    	
        return "home/index";
    }
    
    @GetMapping("/simplemde")
    public String simplemde(@ModelAttribute("markdown") final MarkdownModel markdown, Model model) {
    	
    	
        return "home/simple-mde";
    }

}
