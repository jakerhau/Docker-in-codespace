package demo.example.producer;

import java.lang.reflect.Field;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.RecordMetadata;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import demo.common.dto.PersonDTO;
import demo.common.events.PersonEvent;

@ExtendWith(MockitoExtension.class)
public class PersonEventProducerTest {

    @Mock
    private KafkaProducer<String, String> kafkaProducer;

    private PersonEventProducer personEventProducer;

    @BeforeEach
    void setUp() throws Exception {
        personEventProducer = new PersonEventProducer();

        // inject mock kafkaProducer vào field private
        Field field = PersonEventProducer.class
                .getDeclaredField("kafkaProducer");
        field.setAccessible(true);
        field.set(personEventProducer, kafkaProducer);
    }

    @Test
    void testSendMessage_success() throws Exception {

        PersonEvent event = new PersonEvent("CREATE",
                new PersonDTO("1", 25, "John"));

        Future<RecordMetadata> future = CompletableFuture.completedFuture(null);

        when(kafkaProducer.send(any(), any()))
                .thenReturn(future);

        personEventProducer.sendMessage(event);

        verify(kafkaProducer, times(1))
                .send(any(), any());
    }

    @Test
    void testSendMessage_whenProducerNull_shouldSkip() {

        PersonEventProducer newProducer = new PersonEventProducer();

        // Không inject kafkaProducer để null

        PersonEvent event = new PersonEvent("CREATE",
                new PersonDTO("1", 25, "John"));

        newProducer.sendMessage(event);

        // Không throw exception
        assertTrue(true);
    }

}