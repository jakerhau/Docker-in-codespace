package demo.example.service;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import demo.common.dto.PersonDTO;
import demo.example.config.RedisConfig;

@Component(service = PersonService.class)
public class PersonServiceImpl implements PersonService {

    @Reference
    private RedisConfig redisConfig;

    @Override
    public void handleCreatePerson(PersonDTO personDTO) {
        // Handle create person logic here
    }

    @Override
    public void handleUpdatePerson(String id, PersonDTO personDTO) {
        // Handle update person logic here
    }

    @Override
    public void handleDeletePerson(String id) {
        // Handle delete person logic here
    }
}