package studentmanagement.AdminTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import studentmanagement.Admin;
import studentmanagement.utils.DatabaseUtils;

public class Option13Test {
    Admin ad = null;
    Connection conn = null;

    @BeforeEach
    public void setUp() throws Exception {
        conn = DatabaseUtils.connect();
        ad = new Admin("admin@iitrpr.ac.in", conn, "2020-21", "2");
    }

    @ParameterizedTest
    @CsvSource({ "13,999,1", "13,7014373887,2" })
    public void testOption13(String choice, String phoneNumber, Integer expected) {
        String result;
        String input = choice + "\n" + phoneNumber + "\n15\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        Scanner scan = new Scanner(System.in);
        if (expected == 1) {
            result = ad.adminOptions(scan);
            assertEquals("fail", result);
            return;
        }
        if (expected == 2) {
            result = ad.adminOptions(scan);
            assertEquals("pass", result);
            return;
        }

        scan.close();

    }

    @AfterEach
    void cleanUp() {
        conn = DatabaseUtils.connect();
        DatabaseUtils.executeUpdateQuery(conn,
                "update user_auth set phone = null where id = '" + "admin@iitrpr.ac.in" + "'");
        conn = null;
    }
}
