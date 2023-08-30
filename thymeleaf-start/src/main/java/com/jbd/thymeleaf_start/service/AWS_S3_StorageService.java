package com.jbd.thymeleaf_start.service;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.jbd.thymeleaf_start.model.MediaDTO;

@Service
public class AWS_S3_StorageService {

	private Logger logger = LoggerFactory.getLogger(AWS_S3_StorageService.class);

    @Autowired
    @Qualifier("s3storage")
    private AmazonS3 amazonS3Client;

    @Value("${cloud.s3.bucket.name}")
    private String bucketName;
    
    @Value("${cloud.s3.cdn.endpoint}")
    private String cdn_endpoint;

    /**
     * Upload file into DigitalOcean Spaces
     *
     * @param keyName
     * @param file
     * @return String
     */
    public MediaDTO uploadFile(final String fileName, final MultipartFile file) {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(contentType(file));
            metadata.setHeader("x-amz-acl", "public-read");
            PutObjectResult result = amazonS3Client.putObject(bucketName, fileName, file.getInputStream(), metadata);
            //amazonS3Client.setObjectAcl(bucketName, fileName, CannedAccessControlList.PublicRead);
            
            MediaDTO media = new MediaDTO();
            media.setName(fileName);
            media.setMediaLocation(cdn_endpoint + fileName);
            media.setMediaType(contentType(file));
            media.setCreatedDate(new Date());
            media.setSize(file.getSize());
            
            System.out.println("Content - Length in KB : "+ result.getMetadata().getContentLength());
            
            return media;
        } catch (IOException ioe) {
            logger.error("IOException: " + ioe.getMessage());
        } catch (AmazonServiceException serviceException) {
            logger.info("AmazonServiceException: "+ serviceException.getMessage());
            throw serviceException;
        } catch (AmazonClientException clientException) {
            logger.info("AmazonClientException Message: " + clientException.getMessage());
            throw clientException;
        }
        return new MediaDTO();
    }
    
    /**
     * Deletes file from DigitalOcean Spaces
     *
     * @param fileName
     * @return
     */
    public String deleteFile(final String fileName) {
        amazonS3Client.deleteObject(bucketName, fileName);
        return "Deleted File: " + fileName;
    }


    /**
     * Downloads file from DigitalOcean Spaces
     *
     * @param keyName
     * @return ByteArrayOutputStream
     */
    public ByteArrayOutputStream downloadFile(String keyName) {
        try {
            S3Object s3object = amazonS3Client.getObject(new GetObjectRequest(bucketName, keyName));

            InputStream is = s3object.getObjectContent();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            int len;
            byte[] buffer = new byte[4096];
            while ((len = is.read(buffer, 0, buffer.length)) != -1) {
                outputStream.write(buffer, 0, len);
            }

            return outputStream;
        } catch (IOException ioException) {
            logger.error("IOException: " + ioException.getMessage());
        } catch (AmazonServiceException serviceException) {
            logger.info("AmazonServiceException Message:    " + serviceException.getMessage());
            throw serviceException;
        } catch (AmazonClientException clientException) {
            logger.info("AmazonClientException Message: " + clientException.getMessage());
            throw clientException;
        }

        return null;
    }

    /**
     * Get all files from DO Spaces
     *
     * @return
     */
    public List<String> listFiles() {

        ListObjectsRequest listObjectsRequest =
                new ListObjectsRequest().withBucketName(bucketName).withEncodingType(null);

        List<String> files = new ArrayList<>();
        ObjectListing objects = amazonS3Client.listObjects(listObjectsRequest);

        while (true) {
            List<S3ObjectSummary> objectSummaries = objects.getObjectSummaries();
            if (objectSummaries.size() < 1) {
                break;
            }

            for (S3ObjectSummary item : objectSummaries) {
                if (!item.getKey().endsWith("/"))
                	files.add(item.getKey());
            }

            objects = amazonS3Client.listNextBatchOfObjects(objects);
        }

        return files;
    }
    
    private String contentType(final MultipartFile file) {
    	
    	final String fileName = file.getOriginalFilename();
        return file.getContentType() == null ?  fileName.substring(fileName.lastIndexOf(".") + 1) : file.getContentType();
    }
}
