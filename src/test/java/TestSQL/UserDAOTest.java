package TestSQL;

import SQLObjects.*;
import XPOJOS.Model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTest {
    private Database db;
    private User bestUser;
    private User worstUser;
    Connection conn;
    UserDAO uDao;

    @BeforeEach
    public void setUp() throws DataAccessException {

        db = new Database();

        bestUser = new User("sully11", "laladadeedadadadum", "wasup@gmail.com",
                "Mike", "Lee", "m", "bestP123");

        worstUser = new User("sully1102", "laladadeedadadadum", "wasup@gmail.com",
                "Mike", "Lee", "m", "bestP120");

        conn = db.openConnection();

        db.reloadTables();

        uDao = new UserDAO(conn);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.closeConnection(true);
    }

    @Test
    public void insertPass() throws DataAccessException {

        uDao.insertUser(bestUser);

        User compareTest = uDao.selectUser(bestUser.getUsername());

        assertNotNull(compareTest);

        assertEquals(bestUser, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        uDao.insertUser(bestUser);
        assertThrows(DataAccessException.class, ()-> uDao.insertUser(bestUser));
    }

    @Test
    public void selectPass() throws DataAccessException {
        uDao.insertUser(bestUser);
        uDao.insertUser(worstUser);

        User compareTest1 = uDao.selectUser(bestUser.getUsername());
        User compareTest2 = uDao.selectUser(worstUser.getUsername());

        assertNotNull(compareTest1);

        assertEquals(bestUser, compareTest1);

        assertNotNull(compareTest2);

        assertEquals(worstUser, compareTest2);

        assertNotEquals(compareTest1, compareTest2);
    }

    @Test
    public void selectFail() throws DataAccessException {
        uDao.insertUser(bestUser);
        assertNull(uDao.selectUser(worstUser.getUsername()));
    }

    @Test
    public void deleteOnePass1() throws DataAccessException {
        uDao.insertUser(bestUser);
        uDao.insertUser(worstUser);

        assertEquals(bestUser.getLastName(), worstUser.getLastName());

        uDao.deleteOneUser(bestUser.getUsername());
        assertNull(uDao.selectUser(bestUser.getUsername()));
    }

    @Test
    public void deleteOnePass2() throws DataAccessException {
        uDao.insertUser(bestUser);
        uDao.insertUser(worstUser);

        assertEquals(bestUser.getLastName(), worstUser.getLastName());

        uDao.deleteOneUser(worstUser.getUsername());
        assertNull(uDao.selectUser(worstUser.getUsername()));
    }

    @Test
    public void reloadPass1() throws DataAccessException {
        uDao.insertUser(bestUser);
        uDao.insertUser(worstUser);

        assertEquals(bestUser.getLastName(), worstUser.getLastName());

        uDao.reloadUsersTable();

        uDao.insertUser(bestUser);
        uDao.insertUser(worstUser);

        assertEquals(bestUser.getLastName(), worstUser.getLastName());
    }

    @Test
    public void reloadPass2() throws DataAccessException {
        uDao.insertUser(bestUser);
        uDao.insertUser(worstUser);

        assertEquals(bestUser.getLastName(), worstUser.getLastName());

        uDao.reloadUsersTable();

        uDao.insertUser(bestUser);
        uDao.insertUser(worstUser);

        assertNotEquals(bestUser.getUsername(), worstUser.getUsername());
    }

    @Test
    public void printPass() throws DataAccessException {
        uDao.insertUser(bestUser);
        uDao.insertUser(worstUser);
        String compareString = uDao.UsersTableToString();

        assertEquals(bestUser.getLastName(), worstUser.getLastName());
        assertEquals(uDao.UsersTableToString(), compareString);
    }

    @Test
    public void printFail() throws DataAccessException {
        uDao.insertUser(bestUser);
        uDao.insertUser(worstUser);
        String compareString = uDao.UsersTableToString();

        assertEquals(bestUser.getLastName(), worstUser.getLastName());

        uDao.reloadUsersTable();

        assertNotEquals(uDao.UsersTableToString(), compareString);
    }

    @Test
    public void doesExistPass() throws DataAccessException {
        uDao.insertUser(bestUser);
        uDao.insertUser(worstUser);

        assertTrue(uDao.doesUsernameExist(bestUser.getUsername()));
        assertTrue(uDao.doesUsernameExist(worstUser.getUsername()));
    }

    @Test
    public void doesExistFail() throws DataAccessException {
        uDao.insertUser(bestUser);
        uDao.insertUser(worstUser);

        assertTrue(uDao.doesUsernameExist(bestUser.getUsername()));
        assertTrue(uDao.doesUsernameExist(worstUser.getUsername()));

        uDao.reloadUsersTable();

        assertFalse(uDao.doesUsernameExist(bestUser.getUsername()));
    }
}
