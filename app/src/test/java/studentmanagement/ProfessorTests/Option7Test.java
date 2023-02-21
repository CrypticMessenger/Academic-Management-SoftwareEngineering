package studentmanagement.ProfessorTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import studentmanagement.App;
import studentmanagement.Professor;

public class Option7Test {
    App app = null;
    Professor prof = null;
    Connection conn = null;

    @BeforeEach
    public void setUp() {
        app = new App();
        conn = app.connect();
        prof = new Professor("gunturi@iitrpr.ac.in", conn, "2020-21", "2");

    }

    @ParameterizedTest
    @CsvSource({ "7,,1", "8,7,2" })
    public void testOption7(String choice, String choice2, Integer expected)
            throws Exception {
        String result;
        String input = choice + "\n" + choice2 + "\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        Scanner scan = new Scanner(System.in);
        if (expected == 1 || expected == 2) {
            result = prof.professorOptions(scan);
            assertEquals("pass", result);
            return;
        }

        scan.close();

    }

    @AfterEach
    public void tearDown() {

        conn = null;
        app = null;

    }
}
