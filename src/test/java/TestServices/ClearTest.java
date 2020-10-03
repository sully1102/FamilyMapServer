package TestServices;

import SQLObjects.DataAccessException;
import XPOJOS.Model.User;
import XPOJOS.Request.RegisterRequest;
import XPOJOS.Response.RegisterResponse;
import XPOJOS.Response.ClearResponse;
import Services.Clear;
import Services.Register;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClearTest {

    private Register registerService;
    private Clear clearService;

    @BeforeEach
    public void setUp() throws DataAccessException {
        registerService = new Register();
    }

    @AfterEach
    public void tearDown() {
        clearService = new Clear();
        clearService.clearAll();
    }

    @Test
    public void ClearCanBeCalledTwice() {

        clearService = new Clear();
        ClearResponse response = clearService.clearAll();
        clearService = new Clear();
        ClearResponse response1 = clearService.clearAll();

        assertEquals(response.getMessage(), response1.getMessage());
    }

    @Test
    public void ClearUser() {

        User bestUser = new User("sully11", "laladadeedadadadum", "wasup@gmail.com",
                "Mike", "Lee", "m", "bestP123");

        RegisterRequest request = new RegisterRequest();
        request.setUsername(bestUser.getUsername());
        request.setPassword(bestUser.getPassword());
        request.setEmail(bestUser.getEmail());
        request.setFirstName(bestUser.getFirstName());
        request.setLastName(bestUser.getLastName());
        request.setGender(bestUser.getGender());

        RegisterResponse response = registerService.register(request);

        clearService = new Clear();
        ClearResponse response1 = clearService.clearAll();

        assertEquals("Clear succeeded", response1.getMessage());
    }
}
