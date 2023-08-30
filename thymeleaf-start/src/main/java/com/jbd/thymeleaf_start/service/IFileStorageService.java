package com.jbd.thymeleaf_start.service;

import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.jbd.thymeleaf_start.model.MediaDTO;

public interface IFileStorageService {
	
	public MediaDTO saveMedia(MultipartFile file) throws IOException;

	public Resource getMedia(String filePath) throws MalformedURLException;

	public boolean deleteMedia(String filePath) throws IOException;

}
