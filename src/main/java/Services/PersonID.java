package Services;

import SQLObjects.*;
import XPOJOS.Response.*;
import XPOJOS.Model.*;

public class PersonID {

    private Database db;

    public PersonID() {
        db = new Database();
    }

    public PersonIDResponse getPerson(String personID, String authID) {

        PersonIDResponse response = new PersonIDResponse();

        try {
            db.openConnection();

            if(db.getAuthDao().doesAuthorityTokenExist(authID)) {

                String userName = db.getAuthDao().selectAuthToken(authID).getUsername();

                if(db.getPersonDao().doesPersonExist(personID)) {

                    Person person = db.getPersonDao().selectPerson(personID);
                    String personUsername = person.getUsername();

                    if(userName.equals(personUsername)) {

                        response.setUsername(person.getUsername());
                        response.setPersonID(person.getPersonID());
                        response.setFirstName(person.getFirstName());
                        response.setLastName(person.getLastName());
                        response.setGender(person.getGender());
                        if(person.getFatherID() != null) { response.setFatherID(person.getFatherID()); }
                        if(person.getMotherID() != null) { response.setMotherID(person.getMotherID()); }
                        if(person.getSpouseID() != null) { response.setSpouseID(person.getSpouseID()); }

                        response.setSuccess(true);
                        db.closeConnection(true);
                    } else {
                        response.setSuccess(false);
                        response.setMessage("Error: Requested person does not belong to this user");
                        db.closeConnection(false);
                    }
                } else {
                    response.setSuccess(false);
                    response.setMessage("Error: Invalid personID parameter");
                    db.closeConnection(false);
                }
            } else {
                response.setSuccess(false);
                response.setMessage("Error: Invalid auth token");
                db.closeConnection(false);
            }
        } catch(DataAccessException e) {
            response.setSuccess(false);
            response.setMessage("Internal server error");

            try {
                db.closeConnection(false);
            } catch (DataAccessException f) {
                f.printStackTrace();
            }
        }

        return response;
    }
}
