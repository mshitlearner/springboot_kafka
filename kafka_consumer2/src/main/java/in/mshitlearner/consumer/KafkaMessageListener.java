package in.mshitlearner.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import in.mshitlearner.binding.Customer;
import in.mshitlearner.service.CustomerService;
import in.mshitlearner.util.KafkaConfigConstants;

@Service
public class KafkaMessageListener {
	Logger log = LoggerFactory.getLogger(KafkaMessageListener.class);
	/*
	private CustomerService customerService;
	
	public KafkaMessageListener(CustomerService customerService) {
		this.customerService = customerService;
	}
	*/
	@KafkaListener(topics = KafkaConfigConstants.topicName, groupId = KafkaConfigConstants.consumerGroupName)
	@RetryableTopic(attempts = KafkaConfigConstants.retryableTopicAttempts , autoCreateTopics="false" ) 
	public void consumeEvents(Customer customer, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
			@Header(KafkaHeaders.OFFSET) long offset, @Header(KafkaHeaders.RECEIVED_PARTITION) String partition) {
		try {
			log.info("Consumer-2 Received: {} from {} offset {} partition {}", new ObjectMapper().writeValueAsString(customer), topic, offset, partition);
			//log.info("Consumer-2 consumes {} ", customer.toString());
			//customerService.validateAgeOfCustomer(customer);
			  if(customer.getAge() < 18) {
		        	 throw new RuntimeException("Under 18 Years age will not consider for the sales !");
		        }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@DltHandler
	public void listenDLT(Customer customer, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
			@Header(KafkaHeaders.OFFSET) long offset, @Header(KafkaHeaders.RECEIVED_PARTITION) String partition,
			Acknowledgment acknowledgment) {
		log.info("DLT Received : {} , from {} , offset {} partition {}", customer.getCustName(), topic, offset, partition);
	}

}
