package SQLObjects;

import XPOJOS.Model.Event;
import SerializeDeserialize.*;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class EventDAO {

    private final Connection conn;
    private int numEvents;
    private LocationsArray locations;

    public EventDAO(Connection conn) {
        this.conn = conn;
        numEvents = 0;
        try {
            locations = Deserializer.deserializeLocations(new File("json/locations.json"));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void insertEvent(Event event) throws DataAccessException {

        String sql = "INSERT INTO EventsTable (eventID, userName, personID, latitude, longitude, " +
                "country, city, eventType, eventYear) VALUES(?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, event.getEventID());
            stmt.setString(2, event.getUsername());
            stmt.setString(3, event.getPersonID());
            stmt.setFloat(4, event.getLatitude());
            stmt.setFloat(5, event.getLongitude());
            stmt.setString(6, event.getCountry());
            stmt.setString(7, event.getCity());
            stmt.setString(8, event.getEventType());
            stmt.setInt(9, event.getYear());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting an event into the database");
        }
        numEvents++;
    }

    public Event selectEvent(String eventID) throws DataAccessException {
        Event event;
        ResultSet rs = null;
        String sql = "SELECT * FROM EventsTable WHERE eventID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, eventID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                event = new Event(rs.getString("eventID"),
                                  rs.getString("userName"),
                                  rs.getString("personID"),
                                  rs.getFloat("latitude"),
                                  rs.getFloat("longitude"),
                                  rs.getString("country"),
                                  rs.getString("city"),
                                  rs.getString("eventType"),
                                  rs.getInt("eventYear"));

                return event;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding event");
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

    public void deleteAllEventsOfUser(String userName) throws DataAccessException {
        String sql = "DELETE FROM EventsTable WHERE userName = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userName);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while deleting all events of a user in the database");
        }
    }

    public void reloadEventsTable() throws DataAccessException {
        try (Statement stmt = conn.createStatement()){
            String sql = "DROP TABLE IF EXISTS EventsTable";
            stmt.executeUpdate(sql);
            sql = "CREATE TABLE EventsTable (" +
                    "eventID	TEXT NOT NULL UNIQUE, " +
                    "userName	TEXT NOT NULL, " +
                    "personID	INTEGER NOT NULL, " +
                    "latitude	REAL NOT NULL, " +
                    "longitude	REAL NOT NULL, " +
                    "country	TEXT NOT NULL, " +
                    "city	TEXT NOT NULL, " +
                    "eventType	TEXT NOT NULL, " +
                    "eventYear	INTEGER NOT NULL, " +
                    "PRIMARY KEY(eventID))";

            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while reloading UsersTable");
        }
    }

    public String EventsTableToString() throws DataAccessException {
        StringBuilder printString = new StringBuilder();
        printString.append("EventsTable\n");

        ResultSet rs = null;
        String sql = "SELECT * FROM EventsTable;";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            rs = stmt.executeQuery();

            while (rs.next()) {
                printString.append( rs.getString(1) + " " +
                                    rs.getString(2) + " " +
                                    rs.getString(3) + " " +
                                    rs.getFloat(4)  + " " +
                                    rs.getFloat(5)  + " " +
                                    rs.getString(6) + " " +
                                    rs.getString(7) + " " +
                                    rs.getString(8) + " " +
                                    rs.getInt(9)    + "\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while printing EventsTable");
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

    public boolean doesEventExist(String eventID) throws DataAccessException {

        ResultSet rs = null;
        String sql = "SELECT * FROM EventsTable WHERE eventID = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, eventID);
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

    public Event[] selectAllEvents(String userName) throws DataAccessException {
        ArrayList<Event> eventList = new ArrayList<Event>();

        ResultSet rs = null;
        String sql = "SELECT * FROM EventsTable WHERE userName = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userName);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Event event = new Event(rs.getString("eventID"),
                                        rs.getString("userName"),
                                        rs.getString("personID"),
                                        rs.getFloat("latitude"),
                                        rs.getFloat("longitude"),
                                        rs.getString("country"),
                                        rs.getString("city"),
                                        rs.getString("eventType"),
                                        rs.getInt("eventYear"));

                eventList.add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding event");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        Event[] finalList = eventList.toArray(new Event[eventList.size()]);
        return finalList;
    }

    public void generateBirthDay(String userName, String personID, int childBirthYear) throws DataAccessException{

        Location birthLocation = locations.getLocationList()[new Random().nextInt(977)];

        Event birth = new Event(UUID.randomUUID().toString(), userName, personID,
                                birthLocation.getLatitude(), birthLocation.getLongitude(),
                                birthLocation.getCountry(), birthLocation.getCity(),
                                "birth", childBirthYear);

        insertEvent(birth);
    }

    public void generateMarriage(String userName, String fatherID, String motherID, int childBirthYear) throws DataAccessException {

        Location marriageLocation = locations.getLocationList()[new Random().nextInt(977)];

        Event fathersMarriage = new Event(UUID.randomUUID().toString(), userName, fatherID,
                                          marriageLocation.getLatitude(), marriageLocation.getLongitude(),
                                          marriageLocation.getCountry(), marriageLocation.getCity(),
                                          "marriage", (childBirthYear-2));
        Event mothersMarriage = new Event(UUID.randomUUID().toString(), userName, motherID,
                                          marriageLocation.getLatitude(), marriageLocation.getLongitude(),
                                          marriageLocation.getCountry(), marriageLocation.getCity(),
                                          "marriage", (childBirthYear-2));

        insertEvent(fathersMarriage);
        insertEvent(mothersMarriage);
    }

    public void generateDeath(String userName, String personID, int childBirthYear) throws DataAccessException {

        Location deathLocation = locations.getLocationList()[new Random().nextInt(977)];

        Event death = new Event(UUID.randomUUID().toString(), userName, personID,
                                deathLocation.getLatitude(), deathLocation.getLongitude(),
                                deathLocation.getCountry(), deathLocation.getCity(),
                                "death", (childBirthYear+50));

        insertEvent(death);
    }

    public LocationsArray getLocations() {
        return locations;
    }

    public int getNumEvents() { return numEvents; }
}
