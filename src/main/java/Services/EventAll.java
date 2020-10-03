package Services;

import SQLObjects.*;
import XPOJOS.Response.*;

public class EventAll {

    private Database db;

    public EventAll() {
        db = new Database();
    }

    public EventAllResponse getEvents(String authID) {

        EventAllResponse response = new EventAllResponse();

        try {
            db.openConnection();

            if(db.getAuthDao().doesAuthorityTokenExist(authID)) {

                String userName = db.getAuthDao().selectAuthToken(authID).getUsername();
                response.setData(db.getEventDao().selectAllEvents(userName));

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
