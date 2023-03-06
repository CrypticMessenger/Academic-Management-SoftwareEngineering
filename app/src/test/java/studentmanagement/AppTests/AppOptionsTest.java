package studentmanagement.AppTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.util.Scanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import studentmanagement.App;
import studentmanagement.utils.DatabaseUtils;

public class AppOptionsTest {
    App app = null;
    Connection conn = null;

    @BeforeEach
    public void setUp() {
        app = new App();
        conn = DatabaseUtils.connect();
    }

    @ParameterizedTest
    @CsvSource({ "1,2020csb1072@iitrpr.ac.in,X123,7,1", "1,gunturi@iitrpr.ac.in,X123,8,2",
            "1,admin@iitrpr.ac.in,X123,15,3", "1,god@lpu.ac.in,X123,4,4", "1,god@lpu.ac.in,X123,99,5" })
    public void testOptions(String choice, String email, String password, String logout, Integer expected) {
        String result;
        String input = "";
        if (expected != 4) {
            input = choice + "\n" + email + "\n" + password + "\n" + logout + "\n2\n";

        } else {
            input = choice + "\n" + email + "\n" + password + "\n2\n";
        }
        if (expected == 5) {
            input = logout + "\n" + choice + "\n" + email + "\n" + password + "\n2\n";
        }
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        Scanner scan = new Scanner(System.in);

        result = app.in_options(scan, conn);
        assertEquals("exit", result);

        scan.close();

    }
}
