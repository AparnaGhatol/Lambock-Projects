package com.jbd.thymeleaf_start.controller;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jbd.thymeleaf_start.model.MediaDTO;
import com.jbd.thymeleaf_start.service.AWS_S3_StorageService;
import com.jbd.thymeleaf_start.service.DigitalOceanStorageService;
import com.jbd.thymeleaf_start.service.IMediaService;

@Controller
@RequestMapping("/s3storage")
public class AwsS3StorageController {

	@Autowired
	private AWS_S3_StorageService aws_S3_StorageService;
	
	@Autowired
	private IMediaService mediaservice;
	
	 @GetMapping
	 public String list(final Model model) {
	      model.addAttribute("mediaFiles", mediaservice.findAll());
	    return "s3storage/list";
	 }
	
	@GetMapping("/{fileName:.+}")
	public ResponseEntity<byte[]> getFile(@PathVariable String fileName) throws MalformedURLException {
		
		final MediaDTO media = mediaservice.findByName(fileName);
		ByteArrayOutputStream downloadInputStream = aws_S3_StorageService.downloadFile(media.getName()); // getMedia(media.getMediaLocation());

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(media.getMediaType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(downloadInputStream.toByteArray());
	  }
	
	@PostMapping("/upload")
	public String uploadFile(Model model, @RequestParam("mediafile") MultipartFile file, @RequestParam("fileName") String fileName) throws IOException {
		
	   final MediaDTO media = aws_S3_StorageService.uploadFile(fileName, file);
	   
	   final Long mediaId = mediaservice.create(media);
	   final List<MediaDTO> mediaList =  mediaservice.findAll();
	   
	   model.addAttribute("mediaFiles", mediaList);
			   
	   return "s3storage/list";
	  }
	
	 @PostMapping("/delete/{id}")
	    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) throws IOException {
		 
		    MediaDTO media = mediaservice.get(id);
		    aws_S3_StorageService.deleteFile(media.getName());
		    mediaservice.delete(id);
	        redirectAttributes.addFlashAttribute("MSG_INFO", "Media file deleted succesful!");
	        return "redirect:/s3storage";
	    }
}
