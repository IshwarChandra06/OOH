package com.eikona.tech.util;

import java.io.File;

import org.springframework.stereotype.Component;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Component
public class AWSUtil {
	
public void uploadFileToAWS(String[] imagePaths){
		
		String rootFilePath="eikona_ooh/";
		
		//access key
    	//secret key
    	AWSCredentials credentials = new BasicAWSCredentials(
    			"AKIA2P3WJZVTEOKZEH53", 
    			"B678RdSEWmSbjHo70e1YHzr2SyiboIa2VJ9JX66g"
    	);
    	
    	AmazonS3 s3client = AmazonS3ClientBuilder
    			  .standard()
    			  .withCredentials(new AWSStaticCredentialsProvider(credentials))
    			  .withRegion(Regions.AP_SOUTH_1)
    			  .build();
    	
    	for (String fileName : imagePaths) {
    		s3client.putObject(new PutObjectRequest(
    				"eikona-ooh", fileName,  new File(rootFilePath+ fileName)).withCannedAcl(CannedAccessControlList.PublicRead)
    		);
    	}
	}
	
	
//	public void uploadFileToAWS(String[] imagePaths){
//		
//		String bucketName = "eikona-ooh";
//        
//        String rootPath = "D:/workspace/Development/Visual Studio/eikona-ooh-main_old/static/";
//         
//        S3Client client = S3Client.builder().build();
//        
//        for (String fileName : imagePaths) {
//        	PutObjectRequest request = PutObjectRequest.builder()
//                    .bucket(bucketName).key(fileName)
//                    .acl("public-read").build();
// 
//        		client.putObject(request, RequestBody.fromFile(new File(rootPath+fileName)));
//        }
//         
//        
//	}
}
