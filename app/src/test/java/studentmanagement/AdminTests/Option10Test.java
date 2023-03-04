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
import studentmanagement.App;
import studentmanagement.utils.DatabaseUtils;

public class Option10Test {
    App app = null;
    Admin admin = null;
    Connection conn = null;

    @BeforeEach
    public void setUp() {
        app = new App();
        conn = app.connect();
        admin = new Admin("admin@iitrpr.ac.in", conn, "2020-21", "2");
        DatabaseUtils.executeUpdateQuery(conn, "delete from config_number");
        DatabaseUtils.executeUpdateQuery(conn, "insert into config_number values(1)");

    }

    @ParameterizedTest
    @CsvSource({ "10,1", "10,2", "10,3", "10,4", "10,5" })
    public void testOption10(String choice, Integer expected) {
        String result;
        String input = "";
        // case statements in java
        switch (expected) {
            case 1:
                input = choice + "\n13\n";
                break;
            case 2:
                input = choice + "\ny\n13\n";
                break;
            case 3:
                input = choice + "\nn\n13\n";
                break;
            case 4:
                input = choice + "\nok\ny\n13\n";
                break;
            case 5:
                input = choice + "\nok\nn\n13\n";
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
        conn = app.connect();
        DatabaseUtils.executeUpdateQuery(conn, "delete from config_number");
        DatabaseUtils.executeUpdateQuery(conn, "insert into config_number  values(4)");

        conn = null;
        app = null;
    }
}
