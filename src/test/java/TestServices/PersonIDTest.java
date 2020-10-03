package TestServices;

import XPOJOS.Request.RegisterRequest;
import XPOJOS.Response.*;
import XPOJOS.Model.*;
import Services.*;
import java.util.Random;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PersonIDTest {


    private PersonID personIDService;
    private PersonAll personAllService;
    private Fill fillService;
    private Register registerService;

    @BeforeEach
    public void setUp() {
        fillService = new Fill();
        personIDService = new PersonID();
        personAllService = new PersonAll();
        registerService = new Register();
    }

    @AfterEach
    public void tearDown() {
        Clear clearService = new Clear();
        clearService.clearAll();
    }

    @Test
    public void PersonAllPass() {

        User bestUser = new User("sully11", "ladada", "wasup@gmail.com",
                "Mike", "Lee", "m", "bestP123");

        RegisterRequest request = new RegisterRequest();
        request.setUsername(bestUser.getUsername());
        request.setPassword(bestUser.getPassword());
        request.setEmail(bestUser.getEmail());
        request.setFirstName(bestUser.getFirstName());
        request.setLastName(bestUser.getLastName());
        request.setGender(bestUser.getGender());

        RegisterResponse response = registerService.register(request);
        String authID = response.getAuthToken();

        FillResponse response1 = fillService.fill(bestUser.getUsername(), 4);

        PersonAllResponse responseAll = personAllService.getPeople(authID);

        Person[] persons = responseAll.getData();

        Random num = new Random();
        String randomID = persons[num.nextInt(30)].getPersonID();

        PersonIDResponse responseID = personIDService.getPerson(randomID, authID);

        assertEquals(responseID.getPersonID(), randomID);
    }

    @Test
    public void PersonAllFail() {
        User bestUser = new User("sully11", "ladada", "wasup@gmail.com",
                "Mike", "Lee", "m", "bestP123");

        RegisterRequest request = new RegisterRequest();
        request.setUsername(bestUser.getUsername());
        request.setPassword(bestUser.getPassword());
        request.setEmail(bestUser.getEmail());
        request.setFirstName(bestUser.getFirstName());
        request.setLastName(bestUser.getLastName());
        request.setGender(bestUser.getGender());

        RegisterResponse response = registerService.register(request);
        String authID = response.getAuthToken();

        FillResponse response1 = fillService.fill(bestUser.getUsername(), 0);

        PersonIDResponse responseID = personIDService.getPerson(bestUser.getPersonID(), authID);

        assertNull(responseID.getPersonID());
    }
}
