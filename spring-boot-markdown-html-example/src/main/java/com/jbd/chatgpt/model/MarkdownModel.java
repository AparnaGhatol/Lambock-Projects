package com.jbd.chatgpt.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor @NoArgsConstructor
public class MarkdownModel{

    @NotNull
    private String markdownCode;
    
    //@NotNull
    private String htmlResponse;

}
