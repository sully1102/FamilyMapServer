package TestSQL;

import SQLObjects.*;
import XPOJOS.Model.*;

import java.util.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class PersonDAOTest {
    private Database db;
    private Person bestPerson;
    private Person worstPerson;
    Connection conn;
    PersonDAO pDao;
    UserDAO uDao;
    EventDAO eDao;

    @BeforeEach
    public void setUp() throws DataAccessException {

        db = new Database();

        bestPerson = new Person("bestP123", "sully11", "Jerry",
                "Lee", "m", "bestP121", "bestP122", "bestP125");

        worstPerson = new Person("bestP120", "sully11", "Newman",
                "Lee", "m", "bestP121", "bestP122", "bestP125");

        conn = db.openConnection();

        db.reloadTables();

        pDao = new PersonDAO(conn);
        uDao = new UserDAO(conn);
        eDao = new EventDAO(conn);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.closeConnection(true);
    }

    @Test
    public void insertPass() throws DataAccessException {

        pDao.insertPerson(bestPerson);

        Person compareTest = pDao.selectPerson(bestPerson.getPersonID());

        assertNotNull(compareTest);

        assertEquals(bestPerson, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        pDao.insertPerson(bestPerson);
        assertThrows(DataAccessException.class, ()-> pDao.insertPerson(bestPerson));
    }

    @Test
    public void selectPass() throws DataAccessException {
        pDao.insertPerson(bestPerson);
        pDao.insertPerson(worstPerson);

        Person compareTest1 = pDao.selectPerson(bestPerson.getPersonID());
        Person compareTest2 = pDao.selectPerson(worstPerson.getPersonID());

        assertNotNull(compareTest1);

        assertEquals(bestPerson, compareTest1);

        assertNotNull(compareTest2);

        assertEquals(worstPerson, compareTest2);

        assertNotEquals(compareTest1, compareTest2);
    }

    @Test
    public void selectFail() throws DataAccessException {
        pDao.insertPerson(bestPerson);
        assertNull(pDao.selectPerson(worstPerson.getPersonID()));
    }

    @Test
    public void deleteOnePass1() throws DataAccessException {
        pDao.insertPerson(bestPerson);
        pDao.insertPerson(worstPerson);

        assertEquals(bestPerson.getLastName(), worstPerson.getLastName());

        pDao.deleteAllPersonsOfUser(bestPerson.getUsername());
        assertNull(pDao.selectPerson(bestPerson.getPersonID()));
    }

    @Test
    public void deleteOnePass2() throws DataAccessException {
        pDao.insertPerson(bestPerson);
        pDao.insertPerson(worstPerson);

        assertEquals(bestPerson.getLastName(), worstPerson.getLastName());

        pDao.deleteAllPersonsOfUser(worstPerson.getUsername());
        assertNull(pDao.selectPerson(worstPerson.getPersonID()));
    }

    @Test
    public void reloadPass1() throws DataAccessException {
        pDao.insertPerson(bestPerson);
        pDao.insertPerson(worstPerson);

        assertEquals(bestPerson.getLastName(), worstPerson.getLastName());

        pDao.reloadPersonTable();

        pDao.insertPerson(bestPerson);
        pDao.insertPerson(worstPerson);

        assertEquals(bestPerson.getLastName(), worstPerson.getLastName());
    }

    @Test
    public void reloadPass2() throws DataAccessException {
        pDao.insertPerson(bestPerson);
        pDao.insertPerson(worstPerson);

        assertEquals(bestPerson.getLastName(), worstPerson.getLastName());

        pDao.reloadPersonTable();

        pDao.insertPerson(bestPerson);
        pDao.insertPerson(worstPerson);

        assertNotEquals(bestPerson.getPersonID(), worstPerson.getPersonID());
    }

    @Test
    public void printPass() throws DataAccessException {
        pDao.insertPerson(bestPerson);
        pDao.insertPerson(worstPerson);
        String compareString = pDao.PersonTableToString();

        assertEquals(bestPerson.getLastName(), worstPerson.getLastName());
        assertEquals(pDao.PersonTableToString(), compareString);

        System.out.println(pDao.PersonTableToString());
    }

    @Test
    public void printFail() throws DataAccessException {
        pDao.insertPerson(bestPerson);
        pDao.insertPerson(worstPerson);
        String compareString = pDao.PersonTableToString();

        assertEquals(bestPerson.getLastName(), worstPerson.getLastName());

        pDao.reloadPersonTable();

        assertNotEquals(pDao.PersonTableToString(), compareString);
    }

    @Test
    public void doesExistPass() throws DataAccessException {
        pDao.insertPerson(bestPerson);
        pDao.insertPerson(worstPerson);

        assertTrue(pDao.doesPersonExist(bestPerson.getPersonID()));
        assertTrue(pDao.doesPersonExist(worstPerson.getPersonID()));
    }

    @Test
    public void doesExistFail() throws DataAccessException {
        pDao.insertPerson(bestPerson);
        pDao.insertPerson(worstPerson);

        assertTrue(pDao.doesPersonExist(bestPerson.getPersonID()));
        assertTrue(pDao.doesPersonExist(worstPerson.getPersonID()));

        pDao.reloadPersonTable();

        assertFalse(pDao.doesPersonExist(bestPerson.getPersonID()));
    }

    @Test
    public void selectAllPass() throws DataAccessException {
        pDao.insertPerson(bestPerson);
        pDao.insertPerson(worstPerson);

        Person[] compareTest1 = pDao.selectAllPersons(bestPerson.getUsername());

        assertNotNull(compareTest1);

        assertEquals(pDao.selectPerson(bestPerson.getPersonID()), compareTest1[0]);
        assertEquals(pDao.selectPerson(worstPerson.getPersonID()), compareTest1[1]);
    }

    @Test
    public void selectAllFail() throws DataAccessException {
        pDao.insertPerson(bestPerson);
        pDao.insertPerson(worstPerson);

        Person[] compareTest1 = pDao.selectAllPersons(bestPerson.getUsername());
        assertEquals(compareTest1.length, 2);

        pDao.reloadPersonTable();

        Person[] compareTest2 = pDao.selectAllPersons(bestPerson.getUsername());
        assertEquals(compareTest2.length, 0);
    }

    @Test
    public void updateFatherPass() throws DataAccessException {
        pDao.insertPerson(bestPerson);
        pDao.insertPerson(worstPerson);

        String childID = bestPerson.getPersonID();
        String fatherID = worstPerson.getFatherID();


        pDao.updateFather(childID, fatherID);

        assertEquals(bestPerson.getFatherID(), worstPerson.getFatherID());
    }

    @Test
    public void updateFatherFail() throws DataAccessException {
        pDao.insertPerson(bestPerson);
        pDao.insertPerson(worstPerson);

        String childID = bestPerson.getPersonID();
        String fatherID = "newFatherID";

        pDao.updateFather(childID, fatherID);

        assertNotEquals(bestPerson.getFatherID(), fatherID);
    }

    @Test
    public void updateMotherPass() throws DataAccessException {
        pDao.insertPerson(bestPerson);
        pDao.insertPerson(worstPerson);

        String childID = bestPerson.getPersonID();
        String motherID = worstPerson.getMotherID();


        pDao.updateFather(childID, motherID);

        assertEquals(bestPerson.getMotherID(), worstPerson.getMotherID());
    }

    @Test
    public void updateMotherFail() throws DataAccessException {
        pDao.insertPerson(bestPerson);
        pDao.insertPerson(worstPerson);

        String childID = bestPerson.getPersonID();
        String motherID = "newMotherID";

        pDao.updateFather(childID, motherID);

        assertNotEquals(bestPerson.getFatherID(), motherID);
    }

    @Test
    public void generateZeroGenerations() throws DataAccessException {
        String personID = UUID.randomUUID().toString();

        User user = new User("sully11", "trini", "choir@yahoo.com",
                "Mike", "Sullivan", "m", personID);

        uDao.insertUser(user);

        pDao.generateRoot(user, personID, 0, eDao);

        System.out.println(pDao.PersonTableToString());
        System.out.println("Number of people generated " + pDao.getNumPeople());
        System.out.println(eDao.EventsTableToString());
    }

    @Test
    public void generateOneGeneration() throws DataAccessException {
        String personID = UUID.randomUUID().toString();

        User user = new User("sully11", "trini", "choir@yahoo.com",
                "Mike", "Sullivan", "m", personID);

        uDao.insertUser(user);

        pDao.generateRoot(user, personID, 1, eDao);

        System.out.println(pDao.PersonTableToString());
        System.out.println("Number of people generated " + pDao.getNumPeople());
        System.out.println(eDao.EventsTableToString());
    }

    @Test
    public void generateTwoGenerations() throws DataAccessException {
        String personID = UUID.randomUUID().toString();

        User user = new User("sully11", "trini", "choir@yahoo.com",
                "Mike", "Sullivan", "m", personID);

        uDao.insertUser(user);

        pDao.generateRoot(user, personID, 2, eDao);

        System.out.println(pDao.PersonTableToString());
        System.out.println("Number of people generated " + pDao.getNumPeople());
        System.out.println(eDao.EventsTableToString());
        System.out.println("Number of events generated " + eDao.getNumEvents());
    }

}
