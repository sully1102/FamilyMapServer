package SQLObjects;

import SerializeDeserialize.*;

import java.io.*;
import java.sql.*;
import java.util.*;
import XPOJOS.Model.*;

public class PersonDAO {

    private Connection conn;
    private int numPeople;
    private ArrayList<String> maleNameList;
    private ArrayList<String> femaleNameList;
    private ArrayList<String> lastNameList;

    public PersonDAO(Connection conn) {
        this.conn = conn;
        numPeople = 0;
        try {
            NamesArray female = Deserializer.deserializeNameList(new File("json/fnames.json"));
            NamesArray male = Deserializer.deserializeNameList(new File("json/mnames.json"));
            NamesArray last = Deserializer.deserializeNameList(new File("json/snames.json"));

            femaleNameList = new ArrayList<String>(Arrays.asList(female.getNamesList()));
            maleNameList = new ArrayList<String>(Arrays.asList(male.getNamesList()));
            lastNameList = new ArrayList<String>(Arrays.asList(last.getNamesList()));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void insertPerson(Person person) throws DataAccessException {

        String sql = "INSERT INTO PersonTable (personID, userName, firstName, lastName, " +
                "gender, father, mother, spouse) VALUES(?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, person.getPersonID());
            stmt.setString(2, person.getUsername());
            stmt.setString(3, person.getFirstName());
            stmt.setString(4, person.getLastName());
            stmt.setString(5, person.getGender());
            stmt.setString(6, person.getFatherID());
            stmt.setString(7, person.getMotherID());
            stmt.setString(8, person.getSpouseID());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting a person into the database");
        }
        numPeople++;
    }

    public Person selectPerson(String personID) throws DataAccessException {
        Person person;
        ResultSet rs = null;
        String sql = "SELECT * FROM PersonTable WHERE personID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, personID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                person = new Person(rs.getString("personID"),
                                    rs.getString("userName"),
                                    rs.getString("firstName"),
                                    rs.getString("lastName"),
                                    rs.getString("gender"),
                                    rs.getString("father"),
                                    rs.getString("mother"),
                                    rs.getString("spouse"));

                return person;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding person");
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

    public void deleteAllPersonsOfUser(String userName) throws DataAccessException {
        String sql = "DELETE FROM PersonTable WHERE userName = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userName);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while deleting all persons of a user in the database");
        }
    }

    public void reloadPersonTable() throws DataAccessException {
        try (Statement stmt = conn.createStatement()){
            String sql = "DROP TABLE IF EXISTS PersonTable";
            stmt.executeUpdate(sql);
            sql = "CREATE TABLE PersonTable (" +
                    "personID TEXT NOT NULL UNIQUE, " +
                    "userName	TEXT NOT NULL, " +
                    "firstName	TEXT NOT NULL, " +
                    "lastName	TEXT NOT NULL, " +
                    "gender	TEXT NOT NULL, " +
                    "father	TEXT, " +
                    "mother	TEXT, " +
                    "spouse	TEXT, " +
                    "PRIMARY KEY(personID))";

            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while reloading UsersTable");
        }
    }

    public String PersonTableToString() throws DataAccessException {
        StringBuilder printString = new StringBuilder();
        printString.append("PersonTable\n");

        ResultSet rs = null;
        String sql = "SELECT * FROM PersonTable;";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            rs = stmt.executeQuery();

            while (rs.next()) {
                printString.append( rs.getString(1) + " " +
                                    rs.getString(2) + " " +
                                    rs.getString(3) + " " +
                                    rs.getString(4) + " " +
                                    rs.getString(5) + " " +
                                    rs.getString(6) + " " +
                                    rs.getString(7) + " " +
                                    rs.getString(8) + "\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while printing PersonTable");
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

    public boolean doesPersonExist(String personID) throws DataAccessException {

        ResultSet rs = null;
        String sql = "SELECT * FROM PersonTable WHERE personID = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, personID);
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

    public Person[] selectAllPersons(String userName) throws DataAccessException {
        ArrayList<Person> personList = new ArrayList<Person>();

        ResultSet rs = null;
        String sql = "SELECT * FROM PersonTable WHERE userName = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userName);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Person person = new Person(rs.getString("personID"),
                                          rs.getString("userName"),
                                          rs.getString("firstName"),
                                          rs.getString("lastName"),
                                          rs.getString("gender"),
                                          rs.getString("father"),
                                          rs.getString("mother"),
                                          rs.getString("spouse"));

                personList.add(person);
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

        Person[] finalList = personList.toArray(new Person[personList.size()]);
        return finalList;
    }

    public void updateFather(String childID, String fatherID) throws DataAccessException {

        String sql = "UPDATE PersonTable SET father = ? WHERE personID = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, fatherID);
            stmt.setString(2, childID);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while updating person");
        }
    }

    public void updateMother(String childID, String motherID) throws DataAccessException {

        String sql = "UPDATE PersonTable SET mother = ? WHERE personID = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, motherID);
            stmt.setString(2, childID);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while updating person");
        }
    }

    public void generateRoot(User currentUser, String personID, int numGenerations, EventDAO eventDAO) throws DataAccessException {
        int currentYear = 2020;

        Person userPerson = new Person(personID, currentUser.getUsername(),
                                       currentUser.getFirstName(), currentUser.getLastName(),
                                        currentUser.getGender(), null, null, null);

        insertPerson(userPerson);
        eventDAO.generateBirthDay(currentUser.getUsername(), personID, (currentYear - 25));

        if(numGenerations > 0) {
            generateParents(currentUser.getUsername(), personID, (currentYear - 25), (numGenerations - 1), eventDAO, currentUser.getLastName());
        }
    }

    public void generateParents(String userName, String childID, int childBirthYear, int  numGenerations,
                                EventDAO eventDAO, String fatherLastName) throws DataAccessException{

        String fatherID = UUID.randomUUID().toString();
        String motherID = UUID.randomUUID().toString();

        String fatherName = maleNameList.get(new Random().nextInt(maleNameList.size()));
        String motherName = femaleNameList.get(new Random().nextInt(femaleNameList.size()));
        String motherLastName = lastNameList.get(new Random().nextInt(lastNameList.size()));

        Person father = new Person(fatherID, userName, fatherName, fatherLastName, "m",
                                    null, null, motherID);

        Person mother = new Person(motherID, userName, motherName, motherLastName, "f",
                                    null, null, fatherID);

        updateFather(childID, fatherID);
        updateMother(childID, motherID);
        insertPerson(father);
        insertPerson(mother);

        eventDAO.generateBirthDay(userName, fatherID, (childBirthYear - 25));
        eventDAO.generateBirthDay(userName, motherID, (childBirthYear - 20));
        eventDAO.generateMarriage(userName, fatherID, motherID, childBirthYear);
        eventDAO.generateDeath(userName, fatherID, (childBirthYear + 2));
        eventDAO.generateDeath(userName, motherID, (childBirthYear - 2));

        if(numGenerations > 0) {
            generateParents(userName, fatherID, (childBirthYear - 25), (numGenerations - 1), eventDAO, fatherLastName);
            generateParents(userName, motherID, (childBirthYear - 20), (numGenerations - 1), eventDAO, motherLastName);
        }
    }

    public int getNumPeople() {
        return numPeople;
    }
}
