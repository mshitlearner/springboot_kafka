package in.mshitlearner.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import in.mshitlearner.util.KafkaConfigConstants;

@Configuration
public class KafkaTopicConfig {
	
	//@Autowired
	//private AdminClient adminClient;
	

	
	@Bean
	public NewTopic createNewTopic() {

		try {
			/*ListTopicsResult listTopicsResult = adminClient.listTopics();
			if (listTopicsResult.names().get().contains(KafkaProducerConstants.topicName)) {
				DeleteTopicsResult deleteTopicsResult = adminClient
						.deleteTopics(Collections.singletonList(KafkaProducerConstants.topicName));
				System.out.println("Topic Deleted -- " + deleteTopicsResult.all().get());
			}*/
			return TopicBuilder.name(KafkaConfigConstants.topicName)
					.partitions(KafkaConfigConstants.topicPartitions)
					.replicas(KafkaConfigConstants.topicReplicas)
					.config("retention.ms", "86400000") // Retention period set to 1 day ie 24 hours (1*24*60*60*1000 ms)
					//.config("retention.bytes", "104857600") //104857600 bytes equals 100 MB. Adjust this value as needed.
					.build();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
