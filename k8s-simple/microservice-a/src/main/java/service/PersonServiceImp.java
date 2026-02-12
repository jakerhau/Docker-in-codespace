package service;

import org.osgi.service.component.annotations.Component;

import demo.common.dto.PersonDTO;
import demo.common.events.PersonEvent;
import producer.PersonEventProducer;

@Component(service = PersonService.class)
public class PersonServiceImp implements PersonService {

    private PersonEventProducer producer;

    @Override
    public PersonDTO getPersonById(String id) {
        
        return null;
    }

    @Override
    public PersonDTO createPerson(PersonDTO personDTO) {

        PersonEvent event = new PersonEvent("CREATE", personDTO);

        producer.sendMessage(event);

        return null;
    }

    @Override
    public PersonDTO updatePerson(String id, PersonDTO personDTO) {

        PersonEvent event = new PersonEvent("UPDATE", personDTO);
        producer.sendMessage(event);

        return null;
    }

    @Override
    public void deletePerson(String id) {
        PersonEvent event = new PersonEvent("DELETE", new PersonDTO(id, 0, null));
        producer.sendMessage(event);
    }
}