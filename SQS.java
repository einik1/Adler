package com.amazonaws.samples;
// * Copyright 2010-2012 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// *
// * Licensed under the Apache License, Version 2.0 (the "License").
// * You may not use this file except in compliance with the License.
// * A copy of the License is located at
// *
// *  http://aws.amazon.com/apache2.0
// *
// * or in the "license" file accompanying this file. This file is distributed
// * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
// * express or implied. See the License for the specific language governing
// * permissions and limitations under the License.
// */
import java.util.List;
 
import java.util.UUID;
import java.util.Map.Entry;
 
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.AmazonSQSException;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.DeleteQueueRequest;
import com.amazonaws.services.sqs.model.GetQueueAttributesRequest;
import com.amazonaws.services.sqs.model.GetQueueAttributesResult;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SetQueueAttributesRequest;
 
/**
 * This sample demonstrates how to make basic requests to Amazon SQS using the
 * AWS SDK for Java.
 * <p>
 * <b>Prerequisites:</b> You must have a valid Amazon Web
 * Services developer account, and be signed up to use Amazon SQS. For more
 * information on Amazon SQS, see http://aws.amazon.com/sqs.
 * <p>
 * <b>Important:</b> Be sure to fill in your AWS access credentials in the
 *                   AwsCredentials.properties file before you try to run this
 *                   sample.
 * http://aws.amazon.com/security-credentials
 */
public class SQS {
 
    public static void main(String[] args) throws Exception {
        /*
         * Important: Be sure to fill in your AWS access credentials in the
         *            AwsCredentials.properties file before you try to run this
         *            sample.
         * http://aws.amazon.com/security-credentials
         */
        AWSCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(new ProfileCredentialsProvider().getCredentials());
        AmazonSQS sqs = AmazonSQSClientBuilder.standard()
                .withCredentials(credentialsProvider)
                .withRegion("us-east-1")
                .build();
 
        System.out.println("===========================================");
        System.out.println("Getting Started with Amazon SQS");
        System.out.println("===========================================\n");
 
        try {
            // Create a queues
        	
        	//source queus
            System.out.println("Creating a new SQS queue called appToManagerQueue.\n");
            CreateQueueRequest appToManagerQueueRequest = new CreateQueueRequest("appToManagerQueue");
            String appToManagerQueueURL = sqs.createQueue(appToManagerQueueRequest).getQueueUrl();
            
            System.out.println("Creating a new SQS queue called mangerToWorkersQueue.\n");
            CreateQueueRequest mangerToWorkersQueueRequest = new CreateQueueRequest("mangerToWorkersQueue");
            String mangerToWorkersQueueURL = sqs.createQueue(mangerToWorkersQueueRequest).getQueueUrl();
            
            
            //DeadLetter queues
            System.out.println("Creating a new SQS queue called deadLetterQueueAppToManager.\n");
            CreateQueueRequest deadLetterQueueAppToManagerRequest = new CreateQueueRequest("deadLetterQueueAppToManager");
            String deadLetterQueueAppToManagerURL = sqs.createQueue(deadLetterQueueAppToManagerRequest).getQueueUrl();
            
            System.out.println("Creating a new SQS queue called deadLetterQueueManagerToWorkers.\n");
            CreateQueueRequest deadLetterQueueManagerToWorkersRequest = new CreateQueueRequest("deadLetterQueueManagerToWorkers");
            String deadLetterQueueManagerToWorkersURL = sqs.createQueue(deadLetterQueueManagerToWorkersRequest).getQueueUrl();
            
            GetQueueAttributesResult deadLetterQueueAppToManagerAttributes = sqs.getQueueAttributes(new GetQueueAttributesRequest("deadLetterQueueAppToManager").withAttributeNames("QueueArn"));
          //  GetQueueAttributesResult deadLetterQueueManagerToWorkersrAttributes = sqs.getQueueAttributes(new GetQueueAttributesRequest(deadLetterQueueManagerToWorkersRequest.getQueueName()));
            
            String deadLetterQueueAppToManagerArn = deadLetterQueueAppToManagerAttributes.getAttributes().get("QueueArn");       
        //    String deadLetterQueueManagerToWorkersArn = deadLetterQueueManagerToWorkersrAttributes.getAttributes().get(deadLetterQueueManagerToWorkersRequest.getQueueName());
            	               
            
            // Set dead letter queue with redrive policy on source queue.
            SetQueueAttributesRequest requestPolicyForAppToManagerQueue = new SetQueueAttributesRequest()
                    .withQueueUrl(appToManagerQueueURL)
                    .addAttributesEntry("RedrivePolicy",
                            "{\"maxReceiveCount\":\"5\", \"deadLetterTargetArn\":\""
                            + deadLetterQueueAppToManagerArn + "\"}");
            
//            SetQueueAttributesRequest requestPolicyForMangerToWorkersQueue = new SetQueueAttributesRequest()
//                    .withQueueUrl(mangerToWorkersQueueURL)
//                    .addAttributesEntry("RedrivePolicy",
//                            "{\"maxReceiveCount\":\"5\", \"deadLetterTargetArn\":\""
//                            + deadLetterQueueManagerToWorkersArn + "\"}");
//            
//            
            //sends the policy requests to the sqs
            sqs.setQueueAttributes(requestPolicyForAppToManagerQueue);
      //      sqs.setQueueAttributes(requestPolicyForMangerToWorkersQueue);
            
    
            // List queues
            System.out.println("Listing all queues in your account.\n");
            for (String queueUrl : sqs.listQueues().getQueueUrls()) {
                System.out.println("  QueueUrl: " + queueUrl);
            }
            System.out.println();
 
            // Send a message
            System.out.println("Sending a message to appToManagerQueue.\n");
            sqs.sendMessage(new SendMessageRequest(appToManagerQueueURL, "This is my message text."));
 
//            // Receive messages
//            System.out.println("Receiving messages from appToManagerQueue.\n");
//            ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(appToManagerQueueURL);
//            List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
//            for (Message message : messages) {
//                System.out.println("  Message");
//                System.out.println("    MessageId:     " + message.getMessageId());
//                System.out.println("    ReceiptHandle: " + message.getReceiptHandle());
//                System.out.println("    MD5OfBody:     " + message.getMD5OfBody());
//                System.out.println("    Body:          " + message.getBody());
//                for (Entry<String, String> entry : message.getAttributes().entrySet()) {
//                    System.out.println("  Attribute");
//                    System.out.println("    Name:  " + entry.getKey());
//                    System.out.println("    Value: " + entry.getValue());
//                }
//            }
//            System.out.println();
 
            // Delete a message
//            System.out.println("Deleting a message.\n");
//            String messageRecieptHandle = messages.get(0).getReceiptHandle();
//            sqs.deleteMessage(new DeleteMessageRequest(appToManagerQueueURL, messageRecieptHandle));
// 
//            // Delete a queue
//            System.out.println("Deleting the test queue.\n");
//            sqs.deleteQueue(new DeleteQueueRequest(appToManagerQueueURL));
        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which means your request made it " +
                    "to Amazon SQS, but was rejected with an error response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means the client encountered " +
                    "a serious internal problem while trying to communicate with SQS, such as not " +
                    "being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
    }
}
