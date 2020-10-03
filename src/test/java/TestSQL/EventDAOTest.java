package TestSQL;

import SQLObjects.*;
import XPOJOS.Model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class EventDAOTest {
    private Database db;
    private Event bestEvent;
    private Event worstEvent;
    Connection conn;
    EventDAO eDao;

    @BeforeEach
    public void setUp() throws DataAccessException {

        db = new Database();

        bestEvent = new Event("bestE123","sully11", "bestP123",
                10.5f, 20.5f, "USA", "Santa Rosa",
                "birth", 1995);

        worstEvent = new Event("bestE122","sully11", "bestP123",
                10.5f, 20.5f, "USA", "Santa Rosa",
                "baptism", 2004);

        conn = db.openConnection();

        db.reloadTables();

        eDao = new EventDAO(conn);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.closeConnection(true);
    }

    @Test
    public void insertPass() throws DataAccessException {

        eDao.insertEvent(bestEvent);

        Event compareTest = eDao.selectEvent(bestEvent.getEventID());

        assertNotNull(compareTest);

        assertEquals(bestEvent, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        eDao.insertEvent(bestEvent);
        assertThrows(DataAccessException.class, ()-> eDao.insertEvent(bestEvent));
    }

    @Test
    public void selectPass() throws DataAccessException {
        eDao.insertEvent(bestEvent);
        eDao.insertEvent(worstEvent);

        Event compareTest1 = eDao.selectEvent(bestEvent.getEventID());
        Event compareTest2 = eDao.selectEvent(worstEvent.getEventID());

        assertNotNull(compareTest1);

        assertEquals(bestEvent, compareTest1);

        assertNotNull(compareTest2);

        assertEquals(worstEvent, compareTest2);

        assertNotEquals(compareTest1, compareTest2);
    }

    @Test
    public void selectFail() throws DataAccessException {
        eDao.insertEvent(bestEvent);
        assertNull(eDao.selectEvent(worstEvent.getEventID()));
    }

    @Test
    public void deleteOnePass1() throws DataAccessException {
        eDao.insertEvent(bestEvent);
        eDao.insertEvent(worstEvent);

        assertEquals(bestEvent.getUsername(), worstEvent.getUsername());

        eDao.deleteAllEventsOfUser(bestEvent.getUsername());
        assertNull(eDao.selectEvent(bestEvent.getEventID()));
    }

    @Test
    public void deleteOnePass2() throws DataAccessException {
        eDao.insertEvent(bestEvent);
        eDao.insertEvent(worstEvent);

        assertEquals(bestEvent.getUsername(), worstEvent.getUsername());

        eDao.deleteAllEventsOfUser(worstEvent.getUsername());
        assertNull(eDao.selectEvent(worstEvent.getEventID()));
    }

    @Test
    public void reloadPass1() throws DataAccessException {
        eDao.insertEvent(bestEvent);
        eDao.insertEvent(worstEvent);

        assertEquals(bestEvent.getUsername(), worstEvent.getUsername());

        eDao.reloadEventsTable();

        eDao.insertEvent(bestEvent);
        eDao.insertEvent(worstEvent);

        assertEquals(bestEvent.getUsername(), worstEvent.getUsername());
    }

    @Test
    public void reloadPass2() throws DataAccessException {
        eDao.insertEvent(bestEvent);
        eDao.insertEvent(worstEvent);

        assertEquals(bestEvent.getUsername(), worstEvent.getUsername());

        eDao.reloadEventsTable();

        eDao.insertEvent(bestEvent);
        eDao.insertEvent(worstEvent);

        assertNotEquals(bestEvent.getEventID(), worstEvent.getEventID());
    }

    @Test
    public void printPass() throws DataAccessException {
        eDao.insertEvent(bestEvent);
        eDao.insertEvent(worstEvent);
        String compareString = eDao.EventsTableToString();

        assertEquals(bestEvent.getUsername(), worstEvent.getUsername());
        assertEquals(eDao.EventsTableToString(), compareString);
    }

    @Test
    public void printFail() throws DataAccessException {
        eDao.insertEvent(bestEvent);
        eDao.insertEvent(worstEvent);
        String compareString = eDao.EventsTableToString();

        assertEquals(bestEvent.getUsername(), worstEvent.getUsername());

        eDao.reloadEventsTable();

        assertNotEquals(eDao.EventsTableToString(), compareString);
    }

    @Test
    public void doesExistPass() throws DataAccessException {
        eDao.insertEvent(bestEvent);
        eDao.insertEvent(worstEvent);

        assertTrue(eDao.doesEventExist(bestEvent.getEventID()));
        assertTrue(eDao.doesEventExist(worstEvent.getEventID()));
    }

    @Test
    public void doesExistFail() throws DataAccessException {
        eDao.insertEvent(bestEvent);
        eDao.insertEvent(worstEvent);

        assertTrue(eDao.doesEventExist(bestEvent.getEventID()));
        assertTrue(eDao.doesEventExist(worstEvent.getEventID()));

        eDao.reloadEventsTable();

        assertFalse(eDao.doesEventExist(bestEvent.getEventID()));
    }

    @Test
    public void selectAllPass() throws DataAccessException {
        eDao.insertEvent(bestEvent);
        eDao.insertEvent(worstEvent);

        Event[] compareTest1 = eDao.selectAllEvents(bestEvent.getUsername());

        assertNotNull(compareTest1);

        assertEquals(eDao.selectEvent(bestEvent.getEventID()), compareTest1[0]);
        assertEquals(eDao.selectEvent(worstEvent.getEventID()), compareTest1[1]);
    }

    @Test
    public void selectAllFail() throws DataAccessException {
        eDao.insertEvent(bestEvent);
        eDao.insertEvent(worstEvent);

        Event[] compareTest1 = eDao.selectAllEvents(bestEvent.getUsername());
        assertEquals(compareTest1.length, 2);

        eDao.reloadEventsTable();

        Event[] compareTest2 = eDao.selectAllEvents(bestEvent.getUsername());
        assertEquals(compareTest2.length, 0);
    }
}
