package demo.example.config;

import java.util.Collections;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.osgi.service.component.annotations.Component;

@Component(service = KafkaProducerConfig.class)
public class KafkaProducerConfig {

    private static final Logger logger = Logger.getLogger(KafkaProducerConfig.class.getName());

    public Properties getConfig() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        // props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 12000); // 12 giây
        props.put(ProducerConfig.RETRIES_CONFIG, 3);
        try (AdminClient adminClient = AdminClient.create(props)) {
            String topicName = "person-topic";
            int numPartitions = 3;
            short replicationFactor = 1;

            NewTopic newTopic = new NewTopic(topicName, numPartitions, replicationFactor);

            adminClient
                    .createTopics(Collections.singletonList(newTopic))
                    .all().get();

        } catch (Exception ex) {
            logger.severe(() -> "Error creating Kafka topic: " + ex.getMessage());
        }
        return props;
    }
}