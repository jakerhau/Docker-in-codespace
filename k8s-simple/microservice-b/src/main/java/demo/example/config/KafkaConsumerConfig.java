package demo.example.config;

import java.util.Properties;
import java.util.logging.Logger;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.osgi.service.component.annotations.Component;

@Component(immediate = true, service = PersonKafkaConsumer.class)
public class KafkaConsumerConfig implements PersonKafkaConsumer {

    private static final Logger logger = Logger.getLogger(KafkaConsumerConfig.class.getName());

    @Override
    public Properties getConfig() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "microservice-b-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "100");
        logger.info(() -> "Kafka Consumer Config initialized: " + props);
        return props;
    }
}