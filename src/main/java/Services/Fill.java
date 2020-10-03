package Services;

import SQLObjects.*;
import XPOJOS.Response.FillResponse;
import XPOJOS.Model.*;

public class Fill {

    private Database db;

    public Fill() {
        db = new Database();
    }

    public FillResponse fill(String userName, int numGenerations) {

        FillResponse response = new FillResponse();

        try {
            db.openConnection();

            if(db.getUserDao().doesUsernameExist(userName) && numGenerations >= 0) {

                User currentUser = db.getUserDao().selectUser(userName);

                db.getEventDao().deleteAllEventsOfUser(userName);
                db.getPersonDao().deleteAllPersonsOfUser(userName);

                if(numGenerations == 101) {
                    db.getPersonDao().generateRoot(currentUser, currentUser.getPersonID(),
                                                  4, db.getEventDao());
                } else {
                    db.getPersonDao().generateRoot(currentUser, currentUser.getPersonID(),
                                                   numGenerations, db.getEventDao());
                }

                int numPeople = db.getPersonDao().getNumPeople();
                int numEvents = db.getEventDao().getNumEvents();

                response.setMessage("Successfully added " + numPeople +
                                          " persons and " + numEvents + " events to the database.");

                response.setSuccess(true);
                db.closeConnection(true);
            } else {
                response.setSuccess(false);
                response.setMessage("Error: Invalid userName or generations parameter");
                db.closeConnection(false);
            }
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
