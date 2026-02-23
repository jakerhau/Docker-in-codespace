package demo.example.service;

import demo.common.dto.PersonDTO;

public interface PersonService {

    void handleCreatePerson(PersonDTO personDTO);

    void handleUpdatePerson(String id, PersonDTO personDTO);

    void handleDeletePerson(String id);
}