package demo.example.service;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import demo.common.dto.PersonDTO;
import demo.common.events.PersonEvent;
import demo.example.producer.PersonEventProducer;

@Component(service = PersonService.class)
public class PersonServiceImp implements PersonService {

    @Reference
    private PersonEventProducer producer;

    @Override
    public void getPersonById(String id) {

    }

    @Override
    public void createPerson(PersonDTO personDTO) {

        PersonEvent event = new PersonEvent("CREATE", personDTO);

        producer.sendMessage(event);

    }

    @Override
    public void updatePerson(String id, PersonDTO personDTO) {

        PersonEvent event = new PersonEvent("UPDATE", personDTO);
        producer.sendMessage(event);

    }

    @Override
    public void deletePerson(String id) {
        PersonEvent event = new PersonEvent("DELETE", new PersonDTO(id, 0, null));
        producer.sendMessage(event);
    }
}