package producer;

import java.util.logging.Logger;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import config.KafkaProducerConfig;
import demo.common.events.PersonEvent;

@Component(service = PersonEventProducer.class)
public class PersonEventProducer {

    private static final Logger logger = Logger.getLogger(PersonEventProducer.class.getName());
    private KafkaProducer<String, PersonEvent> kafkaProducer;

    @Reference
    private KafkaProducerConfig kafkaConfig;

    @Activate
    public void active() {
        kafkaProducer = new KafkaProducer<>(kafkaConfig.getConfig());
    }

    public void sendMessage(PersonEvent message) {
        logger.info(() -> "Sending message: " + message);
        kafkaProducer.send(new ProducerRecord<>("person-topic", message));
    }
}