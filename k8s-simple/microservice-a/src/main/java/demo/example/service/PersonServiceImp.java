package demo.example.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import demo.common.dto.PersonDTO;
import demo.common.events.PersonEvent;
import demo.example.config.RedisConfig;
import demo.example.producer.PersonEventProducer;
import redis.clients.jedis.Jedis;

@Component(service = PersonService.class)
public class PersonServiceImp implements PersonService {

    private static final Logger logger = Logger.getLogger(PersonServiceImp.class.getName());
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Reference
    private PersonEventProducer producer;

    @Reference
    private RedisConfig redisConfig;

    @Override
    public Optional<List<PersonDTO>> getAllPersons() {
        try (Jedis jedis = redisConfig.getResource()) {
            String pattern = "person:*";
            List<PersonDTO> persons;
            persons = jedis.keys(pattern).stream()
                    .map(key -> {
                        try {
                            String json = jedis.get(key);
                            return objectMapper.readValue(json, PersonDTO.class);
                        } catch (JsonProcessingException ex) {
                            logger.severe(() -> "Error parsing person data from Redis: " + ex.getMessage());
                            return null;
                        }
                    })
                    .filter(person -> person != null)
                    .toList();
            return Optional.of(persons);
        } catch (Exception ex) {
            logger.severe(() -> "Error accessing Redis: " + ex.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<PersonDTO> getPersonById(String id) {
        try (Jedis jedis = redisConfig.getResource()) {
            String key = "person:" + id;
            String json = jedis.get(key);

            if (json != null) {
                try {
                    return Optional.of(objectMapper.readValue(json, PersonDTO.class));
                } catch (JsonProcessingException ex) {
                    logger.severe(() -> "Error parsing person data from Redis: " + ex.getMessage());
                }
            } else {
                logger.info(() -> "Person not found in cache for id: " + id);
            }
        } catch (Exception ex) {
            logger.severe(() -> "Error accessing Redis: " + ex.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public void createPerson(PersonDTO personDTO) {
        String id = UUID.randomUUID().toString();
        personDTO.setId(id);
        PersonEvent event = new PersonEvent("CREATE", personDTO);
        producer.sendMessage(event);
    }

    @Override
    public void updatePerson(String id, PersonDTO personDTO) {
        personDTO.setId(id);
        PersonEvent event = new PersonEvent("UPDATE", personDTO);
        producer.sendMessage(event);

    }

    @Override
    public void deletePerson(String id) {
        PersonEvent event = new PersonEvent("DELETE", new PersonDTO(id, 0, null));
        producer.sendMessage(event);
    }

}