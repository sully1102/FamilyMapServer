package Services;

import SQLObjects.*;
import XPOJOS.Response.*;
import XPOJOS.Model.*;

public class PersonAll {

    private Database db;

    public PersonAll() {
        db = new Database();
    }

    public PersonAllResponse getPeople(String authID) {

        PersonAllResponse response = new PersonAllResponse();

        try {
            db.openConnection();

            if(db.getAuthDao().doesAuthorityTokenExist(authID)) {

                String userName = db.getAuthDao().selectAuthToken(authID).getUsername();
                response.setData(db.getPersonDao().selectAllPersons(userName));

                response.setSuccess(true);
                db.closeConnection(true);
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
