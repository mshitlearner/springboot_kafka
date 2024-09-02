package in.mshitlearner.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaRetryTopic;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import in.mshitlearner.util.KafkaConfigConstants;

@Configuration
@EnableKafkaRetryTopic
public class KafkaConsumerConfig {

	@Bean
	public Map<String, Object> consumerConfig() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConfigConstants.bootStrapServerConfig);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
		props.put(JsonDeserializer.TRUSTED_PACKAGES, KafkaConfigConstants.trustedPackages);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		// props.put(JsonDeserializer.VALUE_DEFAULT_TYPE,
		// "in.mshitlearner.binding.Customer");
		return props;
	}

	@Bean
	public ConsumerFactory<String, Object> consumerFactory() {
		return new DefaultKafkaConsumerFactory<>(consumerConfig());
	}

	@Bean
	public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Object>> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		return factory;
	}

	@Bean
	public ThreadPoolTaskScheduler taskScheduler() {
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		scheduler.setPoolSize(10);
		scheduler.setThreadNamePrefix("KafkaRetryScheduler-");
		return scheduler;
	}

	/*
	 * @Bean public ProducerFactory<String, Object> producerFactory() { Map<String,
	 * Object> config = new HashMap<>();
	 * config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
	 * KafkaConfigConstants.bootStrapServerConfig); return new
	 * DefaultKafkaProducerFactory<>( config, new StringSerializer(), new
	 * JsonSerializer<>()); }
	 * 
	 * @Bean public KafkaTemplate<String, Object> retryableTopicKafkaTemplate() {
	 * return new KafkaTemplate<>(producerFactory()); }
	 */
}
