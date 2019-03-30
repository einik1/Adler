package com.amazonaws.samples;

import java.util.List;
import java.util.UUID;
import java.util.Map.Entry; 
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.internal.SdkInternalList;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.DeleteQueueRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;

public class Mannager {
	
	public Mannager(String URL1, String URL2) {
		AWSCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(new ProfileCredentialsProvider().getCredentials());
        sqs = AmazonSQSClientBuilder.standard()
                .withCredentials(credentialsProvider)
                .withRegion("us-west-2")
                .build();
        LAQURL = URL1;
        WQURL = URL2;
	}	
	public void messegeInqueue(String messege) {
		sqs.sendMessage(new SendMessageRequest(WQURL, messege));
	}
	public void messegeDequeue() {
		ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(LAQURL);
		messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
	}
	
	public Boolean DeletMessege(Message m) {
		if()
		String messageRecieptHandle = m.getReceiptHandle();
        sqs.deleteMessage(new DeleteMessageRequest(LAQURL, messageRecieptHandle));
        return true;
	}
	private AmazonSQS sqs;
	private String LAQURL;
	private String WQURL;
	private List<Message> messages;
	

}
