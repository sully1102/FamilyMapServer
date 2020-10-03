package XPOJOS.Response;


public class RegisterResponse extends SuccessMessageResponse {

    private String authToken;
    private String userName;
    private String personID;

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUsername() {
        return userName;
    }

    public void setUsername(String userName) {
        this.userName = userName;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public RegisterResponse() {

    }
}
