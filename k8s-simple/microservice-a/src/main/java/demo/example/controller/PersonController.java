package demo.example.controller;

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

        return Response.ok().build();
    }

    @GET
    @Path("/{id}")
    public Response getPersonById(@PathParam("id") String id) {
        logger.info(() -> "Getting person by ID: " + id + " .....");
        return null;
    }

    @POST
    public Response createPerson(PersonDTO personDTO) {
        logger.info("Creating a new person.....");
        personService.createPerson(personDTO);
        return null;
    }

    @PUT
    @Path("/{id}")
    public Response updatePerson(@PathParam("id") String id, PersonDTO personDTO) {
        logger.info(() -> "Updating person with ID: " + id + " .....");
        personService.updatePerson(id, personDTO);
        return null;
    }

    @DELETE
    @Path("/{id}")
    public Response deletePerson(@PathParam("id") String id) {
        logger.info(() -> "Deleting person with ID: " + id + " .....");
        personService.deletePerson(id);
        return null;
    }
}