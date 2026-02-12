package demo.common.events;

import demo.common.dto.PersonDTO;

public class PersonEvent {
    private String eventType;
    private PersonDTO person;

    public PersonEvent(String eventType, PersonDTO person) {
        this.eventType = eventType;
        this.person = person;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public PersonDTO getPerson() {
        return person;
    }

    public void setPerson(PersonDTO person) {
        this.person = person;
    }

}