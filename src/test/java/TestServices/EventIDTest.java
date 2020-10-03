package TestServices;

import XPOJOS.Request.RegisterRequest;
import XPOJOS.Response.*;
import XPOJOS.Model.*;
import Services.*;
import java.util.Random;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventIDTest {


    private EventID eventIDService;
    private EventAll eventAllService;
    private Fill fillService;
    private Register registerService;

    @BeforeEach
    public void setUp() {
        fillService = new Fill();
        eventIDService = new EventID();
        eventAllService = new EventAll();
        registerService = new Register();
    }

    @AfterEach
    public void tearDown() {
        Clear clearService = new Clear();
        clearService.clearAll();
    }

    @Test
    public void EventIDPass() {

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

        Random num = new Random();
        String randomID = events[num.nextInt(30)].getEventID();

        EventIDResponse responseID = eventIDService.getEvent(randomID, authID);

        assertEquals(responseID.getEventID(), randomID);
    }

    @Test
    public void EventIDFail() {
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

        EventAllResponse responseAll = eventAllService.getEvents(authID);
        Event[] events = responseAll.getData();

        String eventID = events[0].getEventID();

        EventIDResponse response3 = eventIDService.getEvent(eventID, authID);

        assertEquals(events.length, 1);
        assertEquals(response3.getEventType(), "birth");
    }
}
