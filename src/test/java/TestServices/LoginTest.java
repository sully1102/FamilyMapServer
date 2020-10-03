package TestServices;

import Services.*;
import XPOJOS.Request.LoginRequest;
import XPOJOS.Request.RegisterRequest;
import XPOJOS.Response.*;
import XPOJOS.Model.*;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {

    private Login loginService;
    private Register registerService;

    @BeforeEach
    public void setUp() {
        loginService = new Login();
        registerService = new Register();
    }

    @AfterEach
    public void tearDown() {
        Clear clearService = new Clear();
        clearService.clearAll();
    }

    @Test
    public void LoginPass() {

        User bestUser = new User("sully11", "ladada", "wasup@gmail.com",
                "Mike", "Lee", "m", "bestP123");

        RegisterRequest request = new RegisterRequest();
        request.setUsername(bestUser.getUsername());
        request.setPassword(bestUser.getPassword());
        request.setEmail(bestUser.getEmail());
        request.setFirstName(bestUser.getFirstName());
        request.setLastName(bestUser.getLastName());
        request.setGender(bestUser.getGender());

        LoginRequest request1 = new LoginRequest();
        request1.setUsername(bestUser.getUsername());
        request1.setPassword(bestUser.getPassword());

        RegisterResponse response = registerService.register(request);

        LoginResponse response1 = loginService.login(request1);

        assertEquals(response.getUsername(), response1.getUserName());

        assertTrue(response1.isSuccess());
    }

    @Test
    public void LoginFail() {
        User bestUser = new User("sully11", "ladada", "wasup@gmail.com",
                "Mike", "Lee", "m", "bestP123");

        LoginRequest request1 = new LoginRequest();
        request1.setUsername(bestUser.getUsername());
        request1.setPassword(bestUser.getPassword());

        LoginResponse response1 = loginService.login(request1);

        assertFalse(response1.isSuccess());
    }
}
