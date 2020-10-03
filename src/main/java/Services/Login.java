package Services;

import SQLObjects.*;
import XPOJOS.Request.LoginRequest;
import XPOJOS.Response.LoginResponse;
import XPOJOS.Model.*;

import java.util.UUID;

public class Login {

    private Database db;

    public Login() {
        db = new Database();
    }

    public LoginResponse login(LoginRequest loginRequest) {

        LoginResponse response = new LoginResponse();

        try {
            db.openConnection();

            String userName = loginRequest.getUsername();
            String password = loginRequest.getPassword();


            if (db.getUserDao().doesUsernameExist(userName) &&
               password.equals(db.getUserDao().selectUser(userName).getPassword())) {

                String newAuthID = UUID.randomUUID().toString();
                String oldPersonID = db.getUserDao().selectUser(userName).getPersonID();


                if(db.getAuthDao().doesUserNameExist(userName)) {
                    db.getAuthDao().updateAuthToken(newAuthID, userName);
                } else {
                    AuthToken newAT = new AuthToken(newAuthID, userName, oldPersonID);
                    db.getAuthDao().insertAuthToken(newAT);
                }


                response.setAuthToken(newAuthID);
                response.setUserName(userName);
                response.setPersonID(oldPersonID);

                response.setSuccess(true);
                db.closeConnection(true);

            } else {
                response.setSuccess(false);
                response.setMessage("Error: Invalid userName or password");
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