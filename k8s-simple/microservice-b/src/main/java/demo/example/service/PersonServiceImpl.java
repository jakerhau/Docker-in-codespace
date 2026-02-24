package demo.example.service;

import java.util.logging.Logger;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.fasterxml.jackson.databind.ObjectMapper;

import demo.common.dto.PersonDTO;
import demo.example.config.RedisConfig;
import redis.clients.jedis.Jedis;

@Component(service = PersonService.class)
public class PersonServiceImpl implements PersonService {

    private static final Logger logger = Logger.getLogger(PersonServiceImpl.class.getName());
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Reference
    private RedisConfig redisConfig;

    @Override
    public void handleCreatePerson(PersonDTO personDTO) {
        try (Jedis jedis = redisConfig.getResource()) {
            String key = "person:" + personDTO.getId();
            String json = objectMapper.writeValueAsString(personDTO);
            jedis.set(key, json);
            logger.info(() -> "Person created: " + personDTO.toString());
        } catch (Exception ex) {
            logger.severe(() -> "Error saving person to Redis: " + ex.getMessage());
        }
    }

    @Override
    public void handleUpdatePerson(String id, PersonDTO personDTO) {
        try (Jedis jedis = redisConfig.getResource()) {
            String key = "person:" + id;
            if (jedis.exists(key)) {
                String json = objectMapper.writeValueAsString(personDTO);
                jedis.set(key, json);
                logger.info(() -> "Person updated: " + personDTO.toString());
            } else {
                logger.warning(() -> "Person with ID " + id + " not found for update");
            }
        } catch (Exception ex) {
            logger.severe(() -> "Error updating person in Redis: " + ex.getMessage());
        }
    }

    @Override
    public void handleDeletePerson(String id) {
        try (Jedis jedis = redisConfig.getResource()) {
            String key = "person:" + id;
            if (jedis.exists(key)) {
                jedis.del(key);
                logger.info(() -> "Person deleted with ID: " + id);
            } else {
                logger.warning(() -> "Person with ID " + id + " not found for deletion");
            }
        } catch (Exception ex) {
            logger.severe(() -> "Error deleting person from Redis: " + ex.getMessage());
        }
    }
}