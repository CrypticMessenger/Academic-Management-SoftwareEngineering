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

public class Option6Test {
    Admin admin = null;
    Connection conn = null;

    @BeforeEach
    public void setUp() {
        conn = DatabaseUtils.connect();
        admin = new Admin("admin@iitrpr.ac.in", conn, "2020-21", "2");
        DatabaseUtils.executeUpdateQuery(conn, "delete from config_number");
        DatabaseUtils.executeUpdateQuery(conn, "insert into config_number values(6)");

    }

    @ParameterizedTest
    @CsvSource({ "6,1", "6,2", "6,3", "6,4", "6,5" })
    public void testOption6(String choice, Integer expected) {
        String result;
        String input = "";
        // case statements in java
        switch (expected) {
            case 1:
                input = choice + "\n15\n";
                break;
            case 2:
                input = choice + "\ny\n15\n";
                break;
            case 3:
                input = choice + "\nn\n15\n";
                break;
            case 4:
                input = choice + "\nok\ny\n15\n";
                break;
            case 5:
                input = choice + "\nok\nn\n15\n";
                break;

        }
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        Scanner scan = new Scanner(System.in);
        if (expected == 1) {
            result = admin.adminOptions(scan);
            assertEquals("fail", result);
            scan.close();

            return;
        }
        DatabaseUtils.executeUpdateQuery(conn, "delete from config_number");
        DatabaseUtils.executeUpdateQuery(conn, "insert into config_number values(4)");
        if (expected == 2 || expected == 4) {
            result = admin.adminOptions(scan);
            assertEquals("pass", result);
            scan.close();

            return;
        }
        if (expected == 3 || expected == 5) {
            result = admin.adminOptions(scan);
            assertEquals("fail", result);
            scan.close();
            return;
        }

        scan.close();

    }

    @AfterEach
    public void tearDown() {
        conn = DatabaseUtils.connect();
        DatabaseUtils.executeUpdateQuery(conn, "delete from config_number");
        DatabaseUtils.executeUpdateQuery(conn, "insert into config_number values(4)");
        conn = null;
    }
}
