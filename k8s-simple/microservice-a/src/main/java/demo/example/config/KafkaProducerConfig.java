package demo.example.config;

import java.util.Properties;

import org.osgi.service.component.annotations.Component;

@Component(service = KafkaProducerConfig.class)
public class KafkaProducerConfig {
    public Properties getConfig() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        return props;
    }
}