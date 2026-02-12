package service;

import demo.common.dto.PersonDTO;


public interface PersonService {
    PersonDTO getPersonById(String id);
    PersonDTO createPerson(PersonDTO personDTO);
    PersonDTO updatePerson(String id, PersonDTO personDTO);
    void deletePerson(String id);
}