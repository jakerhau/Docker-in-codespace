package demo.example.controller;

import java.util.Optional;

import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import demo.common.dto.PersonDTO;
import demo.example.service.PersonService;

@ExtendWith(MockitoExtension.class)
public class PersonControllerTest {

    @Mock
    private PersonService personService;

    private PersonController personController;

    @BeforeEach
    void setUp() {
        personController = new PersonController();
        personController.setPersonService(personService);
    }

    @Test
    void getPersonById_ShouldReturn200_WhenPersonExists() {
        // Given
        String id = "test-id";
        PersonDTO person = new PersonDTO(id, 25, "John");
        when(personService.getPersonById(id))
                .thenReturn(Optional.of(person));

        // When
        Response response = personController.getPersonById(id);

        // Then
        assertEquals(200, response.getStatus());
        assertEquals(person, response.getEntity());
    }

    @Test
    void getPersonById_ShouldReturn404_WhenPersonDoesNotExist() {
        // Given
        String id = "non-existent-id";
        when(personService.getPersonById(id))
                .thenReturn(Optional.empty());

        // When
        Response response = personController.getPersonById(id);

        // Then
        assertEquals(404, response.getStatus());
    }
}