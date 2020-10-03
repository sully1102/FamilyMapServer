package Services;

import SQLObjects.*;
import XPOJOS.Request.LoadRequest;
import XPOJOS.Response.LoadResponse;
import XPOJOS.Model.Person;


public class Load {

    private Database db;

    public Load() {
        db = new Database();
    }

    public LoadResponse load(LoadRequest loadRequest) {

        LoadResponse response = new LoadResponse();

        try {
            db.openConnection();
            db.reloadTables();

            for(int i = 0; i < loadRequest.getUsers().length; i++) {
                db.getUserDao().insertUser(loadRequest.getUsers()[i]);
            }
            for(int i = 0; i < loadRequest.getPersons().length; i++) {
                db.getPersonDao().insertPerson(loadRequest.getPersons()[i]);
            }
            for(int i = 0; i < loadRequest.getEvents().length; i++) {
                db.getEventDao().insertEvent(loadRequest.getEvents()[i]);
            }


            response.setMessage("Successfully added " + db.getUserDao().getNumUsers() +
                                           " users, " + db.getPersonDao().getNumPeople() +
                                     " persons, and " + db.getEventDao().getNumEvents() +
                                " events to the database");

            response.setSuccess(true);
            db.closeConnection(true);

        } catch(DataAccessException e) {
            response.setSuccess(false);
            response.setMessage("Internal server error");

            try {
                db.closeConnection(false);
            } catch(DataAccessException f) {
                f.printStackTrace();
            }
        }

        return response;
    }
}
