package SQLObjects;

import XPOJOS.Model.User;

import java.sql.*;

public class UserDAO {

    private Connection conn;
    private int numUsers;

    public UserDAO(Connection conn) {
        this.conn = conn;
        numUsers = 0;
    }

    public void insertUser(User user) throws DataAccessException {

        String sql = "INSERT INTO UsersTable (userName, password, email, " +
                "firstName, lastName, gender, personID) VALUES(?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getFirstName());
            stmt.setString(5, user.getLastName());
            stmt.setString(6, user.getGender());
            stmt.setString(7, user.getPersonID());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting a user into the database");
        }
        numUsers++;
    }

    public User selectUser(String userName) throws DataAccessException {

        User user;
        ResultSet rs = null;
        String sql = "SELECT * FROM UsersTable WHERE userName = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userName);
            rs = stmt.executeQuery();
            if (rs.next()) {
                user = new User(rs.getString("userName"),
                                rs.getString("password"),
                                rs.getString("email"),
                                rs.getString("firstName"),
                                rs.getString("lastName"),
                                rs.getString("gender"),
                                rs.getString("personID"));

                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding user");
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

    public void deleteOneUser(String userName) throws DataAccessException {

        String sql = "DELETE FROM UsersTable WHERE userName = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userName);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while deleting a user in the database");
        }
    }

    public void reloadUsersTable() throws DataAccessException {
        try (Statement stmt = conn.createStatement()){
            String sql = "DROP TABLE IF EXISTS UsersTable";
            stmt.executeUpdate(sql);
            sql = "CREATE TABLE UsersTable (" +
                    "userName TEXT NOT NULL UNIQUE, " +
                    "password	TEXT NOT NULL, " +
                    "email	TEXT NOT NULL, " +
                    "firstName	TEXT NOT NULL, " +
                    "lastName	TEXT NOT NULL, " +
                    "gender	TEXT NOT NULL, " +
                    "personID	TEXT NOT NULL, " +
                    "PRIMARY KEY(userName))";

            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while reloading UsersTable");
        }
    }

    public String UsersTableToString() throws DataAccessException {

        StringBuilder printString = new StringBuilder();
        printString.append("UsersTable\n");

        ResultSet rs = null;
        String sql = "SELECT * FROM UsersTable;";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            rs = stmt.executeQuery();

            while (rs.next()) {
                printString.append( rs.getString(1) + " " +
                                    rs.getString(2) + " " +
                                    rs.getString(3) + " " +
                                    rs.getString(4) + " " +
                                    rs.getString(5) + " " +
                                    rs.getString(6) + " " +
                                    rs.getString(7) + "\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while printing UsersTable");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return printString.toString();
        }
    }

    public boolean doesUsernameExist(String userName) throws DataAccessException {

        ResultSet rs = null;
        String sql = "SELECT * FROM UsersTable WHERE userName = ?";

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

    public int getNumUsers() { return numUsers; }
}
