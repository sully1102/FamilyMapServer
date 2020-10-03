package TestServices;

import XPOJOS.Request.RegisterRequest;
import XPOJOS.Response.*;
import XPOJOS.Model.*;
import Services.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EventAllTest {


    private EventAll eventAllService;
    private Fill fillService;
    private Register registerService;

    @BeforeEach
    public void setUp() {
        fillService = new Fill();
        eventAllService = new EventAll();
        registerService = new Register();
    }

    @AfterEach
    public void tearDown() {
        Clear clearService = new Clear();
        clearService.clearAll();
    }

    @Test
    public void EventAllPass() {

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

        EventAllResponse responseAll = eventAllService.getEvents(authID);

        Event[] events = responseAll.getData();

        assertEquals(events.length, 91);
    }

    @Test
    public void EventAllFail() {

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

        EventAllResponse responseAll = eventAllService.getEvents(authID);

        Event[] events = responseAll.getData();

        assertNotEquals(events.length, 92);
    }
}

