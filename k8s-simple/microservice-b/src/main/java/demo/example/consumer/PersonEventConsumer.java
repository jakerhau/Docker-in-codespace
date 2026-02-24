package demo.example.consumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.logging.Logger;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import demo.common.events.PersonEvent;
import demo.example.config.PersonKafkaConsumer;
import demo.example.service.PersonService;

@Component(immediate = true)
public class PersonEventConsumer {

    private static final Logger logger = Logger.getLogger(PersonEventConsumer.class.getName());
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private KafkaConsumer<String, String> kafkaConsumer;
    private volatile boolean running = false;
    private Thread consumerThread;

    @Reference
    private PersonKafkaConsumer kafkaConfig;

    @Reference
    private PersonService personService;

    @Activate
    public void active() {
        try {
            kafkaConsumer = new KafkaConsumer<>(kafkaConfig.getConfig());
            kafkaConsumer.subscribe(Arrays.asList("person-topic"));
            logger.info("Kafka Consumer initialized and subscribed to person-topic");
            
            // Start consumer in a background thread
            running = true;
            
            consumerThread = new Thread(this::consumeMessages);
            consumerThread.setName("PersonEventConsumer-Thread");
            consumerThread.setDaemon(true);
            consumerThread.start();
            logger.info("Consumer thread started");

        } catch (RuntimeException ex) {
            logger.severe(() -> "Kafka consumer init failed: " + ex.getMessage());
            kafkaConsumer = null;
        }
    }

    @Deactivate
    public void deactivate() {
        running = false;
        if (consumerThread != null) {
            try {
                consumerThread.join(5000); // Wait max 5 seconds for thread to finish
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                logger.warning(() -> "Interrupted while waiting for consumer thread: " + ex.getMessage());
            }
        }
        if (kafkaConsumer != null) {
            kafkaConsumer.close();
            logger.info("Kafka consumer closed");
        }
    }

    private void consumeMessages() {
        logger.info("Consumer polling started");
        while (running && kafkaConsumer != null) {
            try {
                ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofSeconds(5));
                if (!records.isEmpty()) {
                    records.forEach(record -> {
                        try {
                            logger.info(() -> "Received message from topic: " + record.topic() + ", partition: " + record.partition() + ", offset: " + record.offset());
                            PersonEvent event = objectMapper.readValue(record.value(), PersonEvent.class);
                            handlePersonEvent(event);
                        } catch (JsonProcessingException ex) {
                            logger.severe(() -> "Error processing message: " + ex.getMessage());
                        }
                    });
                }
            } catch (Exception ex) {
                logger.severe(() -> "Error polling messages: " + ex.getMessage());
            }
        }
        logger.info("Consumer polling stopped");
    }

    private void handlePersonEvent(PersonEvent event) {
        logger.info(() -> "Processing PersonEvent: eventType=" + event.getEventType() + ", person=" + event.getPerson());
        
        switch (event.getEventType()) {
            case "CREATE":
                logger.info(() -> "Creating person: " + event.getPerson().getName());
                personService.handleCreatePerson(event.getPerson());
                break;
            case "UPDATE":
                logger.info(() -> "Updating person: " + event.getPerson().getId());
                personService.handleUpdatePerson(event.getPerson().getId(), event.getPerson());
                break;
            case "DELETE":
                logger.info(() -> "Deleting person: " + event.getPerson().getId());
                personService.handleDeletePerson(event.getPerson().getId());
                break;
            default:
                logger.warning(() -> "Unknown event type: " + event.getEventType());
        }
    }
}