package TestSQL;

import SQLObjects.*;
import XPOJOS.Model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class AuthTokenDAOTest {
    private Database db;
    private AuthToken bestToken;
    private AuthToken worstToken;
    private AuthToken worstToken1;
    Connection conn;
    AuthTokenDAO aDao;

    @BeforeEach
    public void setUp() throws DataAccessException {

        db = new Database();

        bestToken = new AuthToken("bestT124","sully11","tripi15");

        worstToken = new AuthToken("bestT123","sully11","tripi14");

        worstToken1 = new AuthToken("bestT125","sully1102","tripi14");

        conn = db.openConnection();

        aDao = new AuthTokenDAO(conn);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.reloadTables();
        db.closeConnection(true);
    }

    @Test
    public void insertPass() throws DataAccessException {

        aDao.insertAuthToken(bestToken);

        AuthToken compareTest = aDao.selectAuthToken(bestToken.getAuthID());

        assertNotNull(compareTest);

        assertEquals(bestToken, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        aDao.insertAuthToken(bestToken);
        assertThrows(DataAccessException.class, ()-> aDao.insertAuthToken(bestToken));
    }

    @Test
    public void selectPass() throws DataAccessException {
        aDao.insertAuthToken(bestToken);
        aDao.insertAuthToken(worstToken);

        AuthToken compareTest1 = aDao.selectAuthToken(bestToken.getAuthID());
        AuthToken compareTest2 = aDao.selectAuthToken(worstToken.getAuthID());

        assertNotNull(compareTest1);

        assertEquals(bestToken, compareTest1);

        assertNotNull(compareTest2);

        assertEquals(worstToken, compareTest2);

        assertNotEquals(compareTest1, compareTest2);
    }

    @Test
    public void selectFail() throws DataAccessException {
        aDao.insertAuthToken(bestToken);
        assertNull(aDao.selectAuthToken(worstToken.getAuthID()));
    }

    @Test
    public void deleteOnePass1() throws DataAccessException {
        aDao.insertAuthToken(bestToken);
        aDao.insertAuthToken(worstToken);

        assertEquals(bestToken.getUsername(), worstToken.getUsername());

        aDao.deleteAllAuthorityTokensOfUser(bestToken.getUsername());
        assertNull(aDao.selectAuthToken(bestToken.getAuthID()));
    }

    @Test
    public void deleteOnePass2() throws DataAccessException {
        aDao.insertAuthToken(bestToken);
        aDao.insertAuthToken(worstToken);

        assertEquals(bestToken.getUsername(), worstToken.getUsername());

        aDao.deleteAllAuthorityTokensOfUser(worstToken.getUsername());
        assertNull(aDao.selectAuthToken(worstToken.getAuthID()));
    }

    @Test
    public void reloadPass1() throws DataAccessException {
        aDao.insertAuthToken(bestToken);
        aDao.insertAuthToken(worstToken);

        assertEquals(bestToken.getUsername(), worstToken.getUsername());

        aDao.reloadAuthorityTokenTable();

        aDao.insertAuthToken(bestToken);
        aDao.insertAuthToken(worstToken);

        assertEquals(bestToken.getUsername(), worstToken.getUsername());
    }

    @Test
    public void reloadPass2() throws DataAccessException {
        aDao.insertAuthToken(bestToken);
        aDao.insertAuthToken(worstToken);

        assertEquals(bestToken.getUsername(), worstToken.getUsername());

        aDao.reloadAuthorityTokenTable();

        aDao.insertAuthToken(bestToken);
        aDao.insertAuthToken(worstToken);

        assertNotEquals(bestToken.getAuthID(), worstToken.getAuthID());
    }

    @Test
    public void doesATExistPass() throws DataAccessException {
        aDao.insertAuthToken(bestToken);
        aDao.insertAuthToken(worstToken);

        assertTrue(aDao.doesAuthorityTokenExist(bestToken.getAuthID()));
        assertTrue(aDao.doesAuthorityTokenExist(worstToken.getAuthID()));
    }

    @Test
    public void doesATExistFail() throws DataAccessException {
        aDao.insertAuthToken(bestToken);
        aDao.insertAuthToken(worstToken);

        assertTrue(aDao.doesAuthorityTokenExist(bestToken.getAuthID()));
        assertTrue(aDao.doesAuthorityTokenExist(worstToken.getAuthID()));

        aDao.reloadAuthorityTokenTable();

        assertFalse(aDao.doesAuthorityTokenExist(bestToken.getAuthID()));
    }

    @Test
    public void doesUsernameExistPass() throws DataAccessException {
        aDao.insertAuthToken(bestToken);
        aDao.insertAuthToken(worstToken);

        assertTrue(aDao.doesUserNameExist(bestToken.getUsername()));
        assertTrue(aDao.doesUserNameExist(worstToken.getUsername()));
    }

    @Test
    public void doesUsernameExistFail() throws DataAccessException {
        aDao.insertAuthToken(bestToken);
        aDao.insertAuthToken(worstToken);

        assertTrue(aDao.doesUserNameExist(bestToken.getUsername()));
        assertTrue(aDao.doesUserNameExist(worstToken.getUsername()));

        aDao.reloadAuthorityTokenTable();

        assertFalse(aDao.doesUserNameExist(bestToken.getUsername()));
    }

    @Test
    public void updateAuthTokenTest() throws DataAccessException {
        aDao.insertAuthToken(worstToken1);

        assertEquals("bestT125", worstToken1.getAuthID());

        aDao.updateAuthToken("bestT122", worstToken1.getUsername());

        AuthToken newAT = aDao.selectAuthToken("bestT122");

        assertEquals("bestT122", newAT.getAuthID());
    }
}
