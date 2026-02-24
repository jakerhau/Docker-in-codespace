package demo.example.controller;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import demo.common.dto.PersonDTO;
import demo.example.service.PersonService;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PersonController {

    private static final Logger logger = Logger.getLogger(PersonController.class.getName());
    private PersonService personService;

    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }

    @GET
    public Response getAllPersons() {
        logger.info("Getting all persons.....");
        Optional<List<PersonDTO>> persons = personService.getAllPersons();
        if (persons.isPresent()) {
            return Response.status(Response.Status.OK)
                    .entity(persons.get())
                    .build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getPersonById(@PathParam("id") String id) {
        logger.info(() -> "Getting person by ID: " + id + " .....");
        Optional<PersonDTO> person = personService.getPersonById(id);
        if (person.isPresent()) {
            return Response.status(Response.Status.OK)
                    .entity(person.get())
                    .build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    public Response createPerson(PersonDTO personDTO) {
        logger.info("Creating a new person.....");
        PersonDTO created = personService.createPerson(personDTO);
        return Response.status(Response.Status.CREATED)
                .entity(created)
                .build();
    }

    @PUT
    @Path("/{id}")
    public Response updatePerson(@PathParam("id") String id, PersonDTO personDTO) {
        logger.info(() -> "Updating person with ID: " + id + " .....");
        personService.updatePerson(id, personDTO);
        return Response.status(Response.Status.ACCEPTED)
                .build();
    }

    @DELETE
    @Path("/{id}")
    public Response deletePerson(@PathParam("id") String id) {
        logger.info(() -> "Deleting person with ID: " + id + " .....");
        personService.deletePerson(id);
        return Response.status(Response.Status.NO_CONTENT)
                .build();
    }
}