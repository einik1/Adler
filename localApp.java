package com.amazonaws.samples;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.dynamodbv2.xspec.NULL;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InstanceType;
import com.amazonaws.services.ec2.model.ResourceType;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.Tag;
import com.amazonaws.services.ec2.model.TagSpecification;

public class localApp {
	
	static List<TagSpecification> tagSpecifications = new ArrayList<TagSpecification>();
	static AWSCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(new ProfileCredentialsProvider().getCredentials());;
	static Boolean checkIfWeAlreadyCreatedTagSpecificationList = false; //not created
	static AmazonEC2 ec2 = AmazonEC2ClientBuilder.standard()
            .withCredentials(credentialsProvider)
            .withRegion("us-east-1")
            .build();
	static List<Instance> instances;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		

			if(!checkIfWeAlreadyCreatedTagSpecificationList) {
				
				createManagerAndAddItToTagSpecificationsList(tagSpecifications);
			}
			
//	        AmazonEC2 ec2 = AmazonEC2ClientBuilder.standard()
//	                .withCredentials(credentialsProvider)
//	                .withRegion("us-east-1")
//	                .build();
	        
	        if(therIsNoAMennager()) {
	        	RunInstancesRequest request = new RunInstancesRequest("ami-0080e4c5bc078760e", 1, 1);
	        	request.setInstanceType(InstanceType.T1Micro.toString());
	        	request.setTagSpecifications(tagSpecifications);
//            	List<Instance> instances = ec2.runInstances(request).getReservation().getInstances();
	        	instances = ec2.runInstances(request).getReservation().getInstances();
	        	System.out.println("Launch instances: " + instances);
	        }
	}
	
	
	public static void createManagerAndAddItToTagSpecificationsList(List<TagSpecification> tagSpecifications) {
		
		tagSpecifications.add(new TagSpecification().withTags(new Tag("Type","Manager")).withResourceType(ResourceType.Instance));
		checkIfWeAlreadyCreatedTagSpecificationList = true;
	}	
	
	public static boolean therIsNoAMennager() {
		return (instances == null || instances.isEmpty()) ;
	}

}








//public class EC2Launch {
//    public static void main(String[] args) throws Exception {
//        AWSCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(new ProfileCredentialsProvider().getCredentials());
//        AmazonEC2 ec2 = AmazonEC2ClientBuilder.standard()
//                .withCredentials(credentialsProvider)
//                .withRegion("us-east-1")
//                .build();
//
//        try {
//            // Basic 32-bit Amazon Linux AMI 1.0 (AMI Id: ami-08728661)
//            RunInstancesRequest request = new RunInstancesRequest("ami-0080e4c5bc078760e", 1, 1);
//            request.setInstanceType(InstanceType.T1Micro.toString());
//            List<Instance> instances = ec2.runInstances(request).getReservation().getInstances();
//            System.out.println("Launch instances: " + instances);
// 
//        } catch (AmazonServiceException ase) {
//            System.out.println("Caught Exception: " + ase.getMessage());
//            System.out.println("Reponse Status Code: " + ase.getStatusCode());
//            System.out.println("Error Code: " + ase.getErrorCode());
//            System.out.println("Request ID: " + ase.getRequestId());
//        }
//    }
//}