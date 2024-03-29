package studentmanagement.ProfessorTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import studentmanagement.Professor;
import studentmanagement.utils.DatabaseUtils;

public class Option7Test {
    Professor st = null;
    Connection conn = null;

    @BeforeEach
    public void setUp() throws Exception {
        conn = DatabaseUtils.connect();
        st = new Professor("gunturi@iitrpr.ac.in", conn, "2020-21", "2");
    }

    @ParameterizedTest
    @CsvSource({ "7,999,1", "7,7014373887,2" })
    public void testOption7(String choice, String phoneNumber, Integer expected) {
        String result;
        String input = choice + "\n" + phoneNumber + "\n8\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        Scanner scan = new Scanner(System.in);
        if (expected == 1) {
            result = st.professorOptions(scan);
            assertEquals("fail", result);
            return;
        }
        if (expected == 2) {
            result = st.professorOptions(scan);
            assertEquals("pass", result);
            return;
        }

        scan.close();

    }

    @AfterEach
    void cleanUp() {
        conn = DatabaseUtils.connect();
        DatabaseUtils.executeUpdateQuery(conn,
                "update user_auth set phone = null where id = '" + "gunturi@iitrpr.ac.in" + "'");
        conn = null;
    }
}
