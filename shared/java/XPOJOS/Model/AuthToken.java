package XPOJOS.Model;


public class AuthToken {
    private String authID;
    private String associatedUsername;
    private String personID;

    public AuthToken(String authID, String userName, String personID) {
        this.authID = authID;
        this.associatedUsername = userName;
        this.personID = personID;
    }

    public String getAuthID() {
        return authID;
    }

    public void setAuthID(String authID) {
        this.authID = authID;
    }

    public String getUsername() {
        return associatedUsername;
    }

    public void setUsername(String userName) {
        this.associatedUsername = userName;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (o instanceof AuthToken) {
            AuthToken oAuthToken = (AuthToken) o;
            return oAuthToken.getAuthID().equals(getAuthID()) &&
                    oAuthToken.getUsername().equals(getUsername()) &&
                    oAuthToken.getPersonID().equals(getPersonID());
        } else {
            return false;
        }
    }
}
