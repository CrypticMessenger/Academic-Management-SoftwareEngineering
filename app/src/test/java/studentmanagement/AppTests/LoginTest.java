package studentmanagement.AppTests;

import org.junit.jupiter.api.Test;

import studentmanagement.App;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginTest {

    @Test
    public void testStudentLogin() {
        App auth = new App();
        String result = auth.login("2020csb1072@iitrpr.ac.in", "X123");
        assertEquals(result, "s", "Login should be successful.");
    }

    @Test
    public void testProfLogin() {
        App auth = new App();
        String result = auth.login("balwinder@iitrpr.ac.in", "X123");
        assertEquals(result, "p", "Login should be successful.");
    }

    @Test
    public void testAdminLogin() {
        App auth = new App();
        String result = auth.login("admin@iitrpr.ac.in", "X123");
        assertEquals(result, "a", "Login should be successful.");
    }

    @Test
    public void testFailedLogin() {
        App auth = new App();
        String result = auth.login("silly@iitrpr.ac.in", "donesntmatter");
        assertEquals(result, "f", "Login should not be successful.");
    }

}