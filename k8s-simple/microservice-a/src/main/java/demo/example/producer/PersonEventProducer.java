package demo.example.producer;

import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import demo.common.events.PersonEvent;
import demo.example.config.KafkaProducerConfig;

@Component(service = PersonEventProducer.class)
public class PersonEventProducer {

    private static final Logger logger = Logger.getLogger(PersonEventProducer.class.getName());
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private KafkaProducer<String, String> kafkaProducer;

    @Reference
    private KafkaProducerConfig kafkaConfig;

    @Activate
    public void active() {
        try {
            kafkaProducer = new KafkaProducer<>(kafkaConfig.getConfig());
        } catch (RuntimeException ex) {
            logger.severe(() -> "Kafka producer init failed: " + ex.getMessage());
            kafkaProducer = null;
        }
    }

    public void sendMessage(PersonEvent message) {
        if (kafkaProducer == null) {
            logger.warning("Kafka producer not available; skipping message send.");
            return;
        }
        try {
            String payload = objectMapper.writeValueAsString(message);
            logger.info(() -> "Sending message: " + payload);
            kafkaProducer.send(new ProducerRecord<>("person-topic", payload), (metadata, ex) -> {
                if (ex != null) {
                    logger.severe(() -> "Failed to send message: " + ex.getMessage());
                } else {
                    logger.info(() -> "Message sent successfully. Topic: person-topic, Payload: " + payload);
                }
            }).get();
        } catch (JsonProcessingException ex) {
            logger.severe(() -> "Failed to serialize PersonEvent: " + ex.getMessage());
        } catch (InterruptedException | ExecutionException ex) {
            logger.severe(() -> "Failed to send Kafka message: " + ex.getMessage());
        }
    }
}