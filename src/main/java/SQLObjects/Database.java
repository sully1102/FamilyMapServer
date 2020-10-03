package SQLObjects;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private Connection conn;
    private AuthTokenDAO AuthDao;
    private EventDAO EventDao;
    private PersonDAO PersonDao;
    private UserDAO UserDao;

    public Database() {

    }

    public Connection openConnection() throws DataAccessException {
        try {

            final String CONNECTION_URL = "jdbc:sqlite:MyDatabase.db";

            conn = DriverManager.getConnection(CONNECTION_URL);

            AuthDao = new AuthTokenDAO(conn);
            EventDao = new EventDAO(conn);
            PersonDao = new PersonDAO(conn);
            UserDao = new UserDAO(conn);

            conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to open connection to database");
        }

        return conn;
    }

    public Connection getConnection() throws DataAccessException {
        if(conn == null) {
            return openConnection();
        } else {
            return conn;
        }
    }

    public void closeConnection(boolean commit) throws DataAccessException {
        try {
            if (commit) {
                conn.commit();
            } else {
                conn.rollback();
            }

            conn.close();
            conn = null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to close database connection");
        }
    }

    public void reloadTables() throws DataAccessException {
        AuthDao.reloadAuthorityTokenTable();
        EventDao.reloadEventsTable();
        PersonDao.reloadPersonTable();
        UserDao.reloadUsersTable();
    }

    public AuthTokenDAO getAuthDao() {
        return AuthDao;
    }

    public EventDAO getEventDao() {
        return EventDao;
    }

    public PersonDAO getPersonDao() {
        return PersonDao;
    }

    public UserDAO getUserDao() {
        return UserDao;
    }
}

