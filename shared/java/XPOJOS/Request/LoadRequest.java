package XPOJOS.Request;

import XPOJOS.Model.*;

public class LoadRequest {

    private User[] users;
    private Person[] persons;
    private Event[] events;

    public User[] getUsers() {
        return users;
    }

    public Person[] getPersons() {
        return persons;
    }

    public Event[] getEvents() {
        return events;
    }

    public void setUsers(User[] users) {
        this.users = users;
    }

    public void setPersons(Person[] persons) {
        this.persons = persons;
    }

    public void setEvents(Event[] events) {
        this.events = events;
    }

    public String toStr() {
        StringBuilder stringBuilder = new StringBuilder();

        for(int i = 0; i < users.length; i++) {
            stringBuilder.append(users[i].getUsername() + "\n");
            stringBuilder.append(users[i].getPassword() + "\n");
            stringBuilder.append(users[i].getEmail() + "\n");
            stringBuilder.append(users[i].getFirstName() + "\n");
            stringBuilder.append(users[i].getLastName() + "\n");
            stringBuilder.append(users[i].getGender() + "\n");
            stringBuilder.append(users[i].getPersonID() + "\n");
        }

        for(int i = 0; i < persons.length; i++) {
            stringBuilder.append(persons[i].getFirstName() + "\n");
            stringBuilder.append(persons[i].getLastName() + "\n");
            stringBuilder.append(persons[i].getGender() + "\n");
            stringBuilder.append(persons[i].getPersonID() + "\n");
            stringBuilder.append(persons[i].getSpouseID() + "\n");
            stringBuilder.append(persons[i].getFatherID() + "\n");
            stringBuilder.append(persons[i].getMotherID() + "\n");
            stringBuilder.append(persons[i].getUsername() + "\n");
        }

        for(int i = 0; i < events.length; i++) {
            stringBuilder.append(events[i].getUsername() + "\n");
            stringBuilder.append(events[i].getEventType() + "\n");
        }

        return stringBuilder.toString();
    }

    public LoadRequest() {

    }
}
