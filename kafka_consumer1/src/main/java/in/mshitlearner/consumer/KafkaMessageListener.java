package in.mshitlearner.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import in.mshitlearner.binding.Customer;
import in.mshitlearner.service.CustomerService;
import in.mshitlearner.util.KafkaConfigConstants;

@Service
public class KafkaMessageListener {
	Logger log = LoggerFactory.getLogger(KafkaMessageListener.class);
	
	@Autowired
	private CustomerService customerService;
	
	// Here the Consumer-1 will access the data from the Partition 0 (Zero) of
	// specified topic.
	// PartitionOffset Class: Optionally, you can set an initial offset using
	// PartitionOffset. If you don't specify an initial offset, the listener will
	// start
	// consuming from the latest offset by default.

	// ****Dis Adv - When you manually assign partitions using @KafkaListener,
	// Spring Kafka does not rebalance these partitions automatically
	// if you add or remove consumers.
//backoff = @Backoff(delay = 100, multiplier = 2.0), // Delay between retries
    //autoCreateTopics = "true", dltTopicSuffix = "-dlt",
    		
	@KafkaListener(topics = KafkaConfigConstants.topicName, groupId = KafkaConfigConstants.consumerGroupName)
	@RetryableTopic(attempts = "4", autoCreateTopics = "true", autoStartDltHandler = "true")  // Automatically create retry topics if they don't exist) 
	public void consumeEvents(Customer customer, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
			@Header(KafkaHeaders.OFFSET) long offset, @Header(KafkaHeaders.RECEIVED_PARTITION) String partition) {
		try {
			log.info("Consumer-1 Received: {} from {} offset {} partition {}", new ObjectMapper().writeValueAsString(customer), topic, offset, partition);
			//log.info("Consumer-1 consumes {} ", customer.toString());
			customerService.validateAgeOfCustomer(customer);
			  //ack.acknowledge();
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
