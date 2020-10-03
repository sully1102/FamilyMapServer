package SQLObjects;

import XPOJOS.Model.AuthToken;

import java.sql.*;

public class AuthTokenDAO {

    private final Connection conn;

    public AuthTokenDAO(Connection conn)
    {
        this.conn = conn;
    }

    public void insertAuthToken(AuthToken authToken) throws DataAccessException {

        String sql = "INSERT INTO AuthorityTokenTable (authID, userName, personID) " +
                "VALUES(?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, authToken.getAuthID());
            stmt.setString(2, authToken.getUsername());
            stmt.setString(3, authToken.getPersonID());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting an authority token into the database");
        }
    }

    public AuthToken selectAuthToken(String authID) throws DataAccessException {
        AuthToken authToken;
        ResultSet rs = null;
        String sql = "SELECT * FROM AuthorityTokenTable WHERE authID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, authID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                authToken = new AuthToken(rs.getString("authID"),
                                          rs.getString("userName"),
                                          rs.getString("personID"));

                return authToken;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding authID");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public void deleteAllAuthorityTokensOfUser(String userName) throws DataAccessException {
        String sql = "DELETE FROM AuthorityTokenTable WHERE userName = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userName);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while deleting all authority tokens of a user in the database");
        }
    }

    public void reloadAuthorityTokenTable() throws DataAccessException {
        try (Statement stmt = conn.createStatement()){
            String sql = "DROP TABLE IF EXISTS AuthorityTokenTable";
            stmt.executeUpdate(sql);
            sql = "CREATE TABLE AuthorityTokenTable (" +
                        "authID	TEXT NOT NULL UNIQUE, " +
                        "userName	TEXT NOT NULL, " +
                        "personID	TEXT NOT NULL, " +
                        "PRIMARY KEY(authID))";

            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while reloading UsersTable");
        }
    }

    public boolean doesAuthorityTokenExist(String authID) throws DataAccessException {

        ResultSet rs = null;
        String sql = "SELECT * FROM AuthorityTokenTable WHERE authID = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, authID);
            rs = stmt.executeQuery();

            if (!rs.next())
                return false;
            else
                return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean doesUserNameExist(String userName) throws DataAccessException {

        ResultSet rs = null;
        String sql = "SELECT * FROM AuthorityTokenTable WHERE userName = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userName);
            rs = stmt.executeQuery();

            if (!rs.next())
                return false;
            else
                return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void updateAuthToken(String newAuthID, String userName) throws DataAccessException {

        String sql = "UPDATE AuthorityTokenTable SET authID = ? WHERE userName = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newAuthID);
            stmt.setString(2, userName);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while updating Auth Token");
        }
    }
}
