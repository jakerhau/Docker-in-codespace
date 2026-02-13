package demo.example.config;

import java.util.Properties;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.osgi.service.component.annotations.Component;


@Component(service = KafkaProducerConfig.class)
public class KafkaProducerConfig {
    public Properties getConfig() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        // props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 12000); // 12 giây
        props.put(ProducerConfig.RETRIES_CONFIG, 3);
        return props;
    }
}