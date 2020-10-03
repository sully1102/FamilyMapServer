package TestServices;


import XPOJOS.Request.LoadRequest;
import XPOJOS.Response.LoadResponse;
import XPOJOS.Model.*;
import Services.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoadTest {

    private Load loadService;
    private Register registerService;

    @BeforeEach
    public void setUp() {
        loadService = new Load();
        registerService = new Register();
    }

    @AfterEach
    public void tearDown() {
        Clear clearService = new Clear();
        clearService.clearAll();
    }

    @Test
    public void LoadPass() {

        User bestUser = new User("sully11", "laladadeedadadadum", "wasup@gmail.com",
                "Mike", "Lee", "m", "bestP123");

        User worstUser = new User("sully1102", "laladadeedadadadum", "wasup@gmail.com",
                "Mike", "Lee", "m", "bestP120");

        Person bestPerson = new Person("bestP123", "sully11", "Jerry",
                "Lee", "m", "bestP121", "bestP122", "bestP125");

        Person worstPerson = new Person("bestP120", "sully11", "Newman",
                "Lee", "m", "bestP121", "bestP122", "bestP125");

        Event bestEvent = new Event("bestE123","sully11", "bestP123",
                10.5f, 20.5f, "USA", "Santa Rosa",
                "birth", 1995);

        Event worstEvent = new Event("bestE122","sully11", "bestP123",
                10.5f, 20.5f, "USA", "Santa Rosa",
                "baptism", 2004);

        User[] userParam = new User[2];
        Person[] personParam = new Person[2];
        Event[] eventParam = new Event[2];

        userParam[0] = bestUser;
        userParam[1] = worstUser;
        personParam[0] = bestPerson;
        personParam[1] = worstPerson;
        eventParam[0] = bestEvent;
        eventParam[1] = worstEvent;


        LoadRequest loadRequest = new LoadRequest();
        loadRequest.setUsers(userParam);
        loadRequest.setPersons(personParam);
        loadRequest.setEvents(eventParam);

        LoadResponse response = new LoadResponse();
        response = loadService.load(loadRequest);

        LoadResponse expectation = new LoadResponse();
        expectation.setSuccess(true);

        assertEquals(response.isSuccess(), expectation.isSuccess());
    }

    @Test
    public void LoadFail() {
        User bestUser = new User("sully11", "laladadeedadadadum", "wasup@gmail.com",
                "Mike", "Lee", "m", "bestP123");

        User worstUser = new User("sully1102", "laladadeedadadadum", "wasup@gmail.com",
                "Mike", "Lee", "m", "bestP120");

        Person bestPerson = new Person("bestP123", "sully11", "Jerry",
                "Lee", "m", "bestP121", "bestP122", "bestP125");

        Person worstPerson = new Person("bestP120", "sully11", "Newman",
                "Lee", "m", "bestP121", "bestP122", "bestP125");

        Event bestEvent = new Event("bestE123","sully11", "bestP123",
                10.5f, 20.5f, "USA", "Santa Rosa",
                "birth", 1995);

        Event worstEvent = new Event("bestE122","sully11", "bestP123",
                10.5f, 20.5f, "USA", "Santa Rosa",
                "baptism", 2004);

        User[] userParam = new User[2];
        Person[] personParam = new Person[2];
        Event[] eventParam = new Event[2];

        userParam[0] = bestUser;
        userParam[1] = worstUser;
        personParam[0] = bestPerson;
        personParam[1] = worstPerson;
        eventParam[0] = bestEvent;
        eventParam[1] = worstEvent;


        LoadRequest loadRequest = new LoadRequest();
        loadRequest.setUsers(userParam);
        loadRequest.setPersons(personParam);
        loadRequest.setEvents(eventParam);

        LoadResponse response = new LoadResponse();
        response = loadService.load(loadRequest);

        Event worstEvent1 = new Event("bestE122","sully11", "bestP123",
                10.5f, 20.5f, "Canada", "Santa Rosa",
                "baptism", 2004);
        eventParam[1] = worstEvent1;

        LoadResponse expectation = new LoadResponse();
        expectation.setSuccess(true);

        //need fixed numbers
    }
}
