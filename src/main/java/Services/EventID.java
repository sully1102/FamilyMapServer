package Services;

import SQLObjects.*;
import XPOJOS.Response.*;
import XPOJOS.Model.*;

public class EventID {

    private Database db;

    public EventID() {
        db = new Database();
    }

    public EventIDResponse getEvent(String eventID, String authID) {

        EventIDResponse response = new EventIDResponse();

        try {
            db.openConnection();

            if(db.getAuthDao().doesAuthorityTokenExist(authID)) {

                String userName = db.getAuthDao().selectAuthToken(authID).getUsername();

                if(db.getEventDao().doesEventExist(eventID)) {

                    Event event = db.getEventDao().selectEvent(eventID);
                    String eventUsername = event.getUsername();

                    if(userName.equals(eventUsername)) {

                        response.setEventID(event.getEventID());
                        response.setUsername(event.getUsername());
                        response.setPersonID(event.getPersonID());
                        response.setLatitude(event.getLatitude());
                        response.setLongitude(event.getLongitude());
                        response.setCountry(event.getCountry());
                        response.setCity(event.getCity());
                        response.setEventType(event.getEventType());
                        response.setYear(event.getYear());

                        response.setSuccess(true);
                        db.closeConnection(true);
                    } else {
                        response.setSuccess(false);
                        response.setMessage("Error: Requested event does not belong to this user");
                        db.closeConnection(false);
                    }
                } else {
                    response.setSuccess(false);
                    response.setMessage("Error: Invalid eventID parameter");
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
