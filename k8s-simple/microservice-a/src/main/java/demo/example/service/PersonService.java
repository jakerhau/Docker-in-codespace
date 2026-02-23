package demo.example.service;

import java.util.List;
import java.util.Optional;

import demo.common.dto.PersonDTO;

public interface PersonService {
    Optional<List<PersonDTO>> getAllPersons();
    
    Optional<PersonDTO> getPersonById(String id);

    void createPerson(PersonDTO personDTO);

    void updatePerson(String id, PersonDTO personDTO);

    void deletePerson(String id);
}