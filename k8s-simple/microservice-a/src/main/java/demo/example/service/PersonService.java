package demo.example.service;

import java.util.List;
import java.util.Optional;

import demo.common.dto.PersonDTO;

public interface PersonService {
    Optional<List<PersonDTO>> getAllPersons();
    
    Optional<PersonDTO> getPersonById(String id);

    PersonDTO createPerson(PersonDTO personDTO);

    PersonDTO updatePerson(String id, PersonDTO personDTO);

    void deletePerson(String id);
}