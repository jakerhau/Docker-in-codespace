package demo.example.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import demo.common.dto.PersonDTO;
import demo.common.events.PersonEvent;
import demo.example.config.RedisConfig;
import demo.example.producer.PersonEventProducer;
import redis.clients.jedis.Jedis;

@ExtendWith(MockitoExtension.class)
public class PersonServiceImpTest {

    @Mock
    private PersonEventProducer producer;

    @Mock
    private RedisConfig redisConfig;

    @Mock
    private Jedis jedis;

    @InjectMocks
    private PersonServiceImp personService;

    // private PersonDTO samplePerson;

    // @BeforeEach
    // void setUp() {
    // samplePerson = new PersonDTO("test-id", 25, "John");
    // }

    @Test
    void testCreatePerson_shouldSendCreateEventWithGeneratedId() {
        PersonDTO input = new PersonDTO(null, 25, "John");

        personService.createPerson(input);

        verify(producer).sendMessage(argThat(event -> event.getEventType().equals("CREATE") &&
                event.getPerson().getId() != null &&
                event.getPerson().getName().equals("John")));
    }

    @Test
    void testCreatePerson_withNullName_shouldSendCreateEventWithGeneratedId() {
        PersonDTO input = new PersonDTO(null, 25, null);

        personService.createPerson(input);

        verify(producer).sendMessage(argThat(event -> event.getEventType().equals("CREATE") &&
                event.getPerson().getId() != null &&
                event.getPerson().getName() == null));
    }

    @Test
    void testGetPersonById_invalidJson_shouldReturnEmpty() {

        when(redisConfig.getResource()).thenReturn(jedis);
        when(jedis.get("person:test-id")).thenReturn("invalid-json");

        Optional<PersonDTO> result = personService.getPersonById("test-id");

        assertTrue(result.isEmpty());
    }


    @Test
    void testGetAllPersons() throws Exception {
        when(redisConfig.getResource()).thenReturn(jedis);

        when(jedis.keys("person:*"))
                .thenReturn(Set.of("person:test-id"));

        when(jedis.get("person:test-id"))
                .thenReturn("{\"id\":\"test-id\",\"age\":25,\"name\":\"John\"}");

        Optional<List<PersonDTO>> result = personService.getAllPersons();

        assertTrue(result.isPresent());
        assertEquals(1, result.get().size());
        assertEquals("John", result.get().get(0).getName());

        verify(redisConfig).getResource();
        verify(jedis).keys("person:*");
    }

    @Test
    void testGetPersonById() {
        when(redisConfig.getResource()).thenReturn(jedis);
        when(jedis.get("person:test-id"))
                .thenReturn("{\"id\":\"test-id\",\"age\":25,\"name\":\"John\"}");

        Optional<PersonDTO> result = personService.getPersonById("test-id");

        assertTrue(result.isPresent());
        assertEquals("John", result.get().getName());

        verify(redisConfig).getResource();
        verify(jedis).get("person:test-id");
    }

    @Test
    void testCreatePerson() {
        PersonDTO input = new PersonDTO(null, 25, "John");

        doNothing().when(producer).sendMessage(any(PersonEvent.class));

        PersonDTO result = personService.createPerson(input);

        assertNotNull(result.getId());
        assertEquals("John", result.getName());

        verify(producer, times(1))
                .sendMessage(any(PersonEvent.class));
    }

    @Test
    void testUpdatePerson() {
        doNothing().when(producer).sendMessage(any(PersonEvent.class));

        PersonDTO updated = new PersonDTO(null, 30, "Jane");

        PersonDTO result = personService.updatePerson("test-id", updated);

        assertEquals("test-id", result.getId());
        assertEquals("Jane", result.getName());

        verify(producer).sendMessage(any(PersonEvent.class));
    }

    @Test
    void testDeletePerson() {
        doNothing().when(producer).sendMessage(any(PersonEvent.class));

        personService.deletePerson("test-id");

        verify(producer, times(1))
                .sendMessage(any(PersonEvent.class));
    }
}