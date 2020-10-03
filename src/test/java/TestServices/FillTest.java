package TestServices;

import XPOJOS.Model.User;
import XPOJOS.Request.RegisterRequest;
import XPOJOS.Response.RegisterResponse;
import XPOJOS.Response.FillResponse;
import Services.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FillTest {

    private Register registerService;
    private Fill fillService;

    @BeforeEach
    public void setUp() {
        registerService = new Register();
        fillService = new Fill();
    }

    @AfterEach
    public void tearDown() {
        Clear clearService = new Clear();
        clearService.clearAll();
    }

    @Test
    public void FillOneGeneration() {

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

        FillResponse response1 = fillService.fill(bestUser.getUsername(), 0);

        assertEquals(response1.getMessage(), "Successfully added 1 persons and 1 events to the database.");
    }

    @Test
    public void FillDefaultGenerations() {

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

        FillResponse response1 = fillService.fill(bestUser.getUsername(), 101);

        assertEquals(response1.getMessage(), "Successfully added 31 persons and 91 events to the database.");
    }

    @Test
    public void Fill6Generations() {

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

        FillResponse response1 = fillService.fill(bestUser.getUsername(), 6);

        assertEquals(response1.getMessage(), "Successfully added 127 persons and 379 events to the database.");
    }

    @Test
    public void FillFail() {

        User bestUser = new User("sully11", "ladada", "wasup@gmail.com",
                "Mike", "Lee", "m", "bestP123");

        FillResponse response1 = fillService.fill(bestUser.getUsername(), 6);

        assertEquals(response1.getMessage(), "Error: Invalid userName or generations parameter");
    }
}
