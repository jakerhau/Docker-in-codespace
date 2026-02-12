package producer;

import java.util.logging.Logger;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import config.KafkaProducerConfig;
import demo.common.events.PersonEvent;

public class PersonEventProducer {

    private static final Logger logger = Logger.getLogger(PersonEventProducer.class.getName());
    private final KafkaProducer<String, PersonEvent> kafkaProducer;
    private KafkaProducerConfig kafkaConfig;

    public PersonEventProducer() {
        this.kafkaProducer = new KafkaProducer<>(kafkaConfig.getConfig());
    }

    public void sendMessage(PersonEvent message) {
        logger.info(() -> "Sending message: " + message);
        kafkaProducer.send(new ProducerRecord<>("person-topic", message));
    }
}