package in.mshitlearner.service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import in.mshitlearner.binding.Customer;
import in.mshitlearner.util.KafkaConfigConstants;

@Service
public class CustomerService {
	
	@Autowired
	public KafkaTemplate<String, Object> kafkaTemple;
	
	
	public String publishCustomerObjectMessage(List<Customer> lstCustomer) {
		try {
			for (Customer cust : lstCustomer) {
				String uniqueKey = UUID.randomUUID().toString();
				CompletableFuture<SendResult<String, Object>> future = kafkaTemple.send(KafkaConfigConstants.topicName, uniqueKey, cust);
				
				// Explicitly specify the partition (e.g., partition 0)
		       // int partition = 0; 
				//CompletableFuture<SendResult<String, Object>> future = kafkaTemple.send(KafkaProducerConstants.topicName, partition, uniqueKey, cust);
				future.whenComplete((result, ex) -> {
	                if (ex == null) {
	                    System.out.println("Sent message=[" + cust.toString() +
	                            "] with offset=[" + result.getRecordMetadata().offset() + "] and partition=[" + result.getRecordMetadata().partition() + "]" );
	                } else {
	                    System.out.println("Unable to send message=[" +
	                    		cust.toString() + "] due to : " + ex.getMessage());
	                }
	            });
				Thread.sleep(3000);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "Details added To Kafka Successfully";
	}
}
