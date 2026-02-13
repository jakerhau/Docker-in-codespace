package demo.example.service;

import demo.common.dto.PersonDTO;

public interface PersonService {
    void getPersonById(String id);

    void createPerson(PersonDTO personDTO);

    void updatePerson(String id, PersonDTO personDTO);

    void deletePerson(String id);
}