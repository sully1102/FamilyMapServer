package Services;

import java.util.*;
import SQLObjects.*;
import XPOJOS.Request.RegisterRequest;
import XPOJOS.Response.RegisterResponse;
import XPOJOS.Model.*;

public class Register {

    private Database db;

    public Register() { db = new Database(); }

    public RegisterResponse register(RegisterRequest registerRequest) {

        RegisterResponse response = new RegisterResponse();

        try {
            db.openConnection();

            boolean goodUsername = !registerRequest.getUsername().isEmpty();
            boolean goodPassword = !registerRequest.getPassword().isEmpty();
            boolean goodEmail = !registerRequest.getEmail().isEmpty();
            boolean goodFirst = !registerRequest.getFirstName().isEmpty();
            boolean goodLast = !registerRequest.getLastName().isEmpty();
            boolean goodGender = (registerRequest.getGender().equals("m") || registerRequest.getGender().equals("f"));


            if(goodUsername && goodPassword && goodEmail && goodFirst && goodLast && goodGender) {
                if(!db.getUserDao().doesUsernameExist(registerRequest.getUsername())) {

                    String newPersonID = UUID.randomUUID().toString();

                    User user = new User(registerRequest.getUsername(),
                                         registerRequest.getPassword(),
                                         registerRequest.getEmail(),
                                         registerRequest.getFirstName(),
                                         registerRequest.getLastName(),
                                         registerRequest.getGender(),
                                         newPersonID);

                    db.getUserDao().insertUser(user);
                    db.getPersonDao().generateRoot(user, newPersonID, 4, db.getEventDao());


                    String newAuthID = UUID.randomUUID().toString();
                    AuthToken authToken = new AuthToken(newAuthID, user.getUsername(), user.getPersonID());
                    db.getAuthDao().insertAuthToken(authToken);


                    response.setAuthToken(newAuthID);
                    response.setUsername(user.getUsername());
                    response.setPersonID(user.getPersonID());

                    response.setSuccess(true);
                    db.closeConnection(true);

                } else {
                    response.setSuccess(false);
                    response.setMessage("Error: Username already taken by another user");
                    db.closeConnection(false);
                }
            } else {
                response.setSuccess(false);
                response.setMessage("Error: Request property missing or has invalid value");
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
