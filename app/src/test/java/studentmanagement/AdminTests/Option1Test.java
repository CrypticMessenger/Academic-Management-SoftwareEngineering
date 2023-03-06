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

public class Option1Test {
    Admin admin = null;
    Connection conn = null;

    @BeforeEach
    public void setUp() {
        conn = DatabaseUtils.connect();
        admin = new Admin("admin@iitrpr.ac.in", conn, "2020-21", "2");

        DatabaseUtils.executeUpdateQuery(conn,
                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pe_for,pe_minsem) values('CS302',3,1,2,null,'2020-21',2,'pe',Array ['cs'],0)");

    }

    @ParameterizedTest
    @CsvSource({ "1,1", "1,2", "1,3", "1,4", "1,5", "1,6", "1,7" })
    public void testOption1_1(String choice, Integer expected) {
        String result;
        String input = "";
        switch (expected) {
            case 1:
                input = choice + "\n13\n";
                break;
            case 2:
                input = choice + "\n1\nCompSci678\n3\n13\n";
                break;
            case 3:
                input = choice + "\n1\nCS302\n3\n13\n";
                break;
            case 4:
                input = choice + "\n1\nCS302\n3\n1\n2\nCS301\ndone\ncs\nmc\ndone\n\n3\n13\n";
                break;
            case 5:
                input = choice + "\n1\nCS302\n3\n1\n2\nCS301\nCS301\ndone\ncs\nmc\ndone\n\n3\n13\n";
                break;
            case 6:
                input = choice + "\n1\nCS302\n3\n1\n2\nCS301\nCS301\ndone\ncs\nmc\nphy\ndone\n0\n3\n13\n";
                break;
            case 7:
                input = choice + "\n1\nCS302\n3\n1\n2\nCS301\nCS301\ndone\ncs\nmc\ncs\nphy\ndone\n0\n3\n13\n";
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
        DatabaseUtils.executeUpdateQuery(conn, "insert into config_number values(1)");
        if (expected == 2) {
            result = admin.adminOptions(scan);
            assertEquals("fail", result);
            scan.close();
            return;
        }
        if (expected == 3) {
            result = admin.adminOptions(scan);
            assertEquals("fail", result);
            scan.close();
            return;
        }
        DatabaseUtils.executeUpdateQuery(conn, "delete from course_catalog");
        if (expected == 4 || expected == 5 || expected == 6 || expected == 7) {
            result = admin.adminOptions(scan);
            assertEquals("pass", result);
            scan.close();
            return;
        }
        scan.close();

    }

    @ParameterizedTest
    @CsvSource({ "1,1", "1,2", "1,3", "1,4", "1,5", "1,6" })
    public void testOption1_2(String choice, Integer expected) {
        String result;
        String input = "";
        switch (expected) {
            case 1:
                input = choice + "\n13\n";
                break;
            case 2:
                input = choice + "\n2\nCompSci678\n3\n13\n";
                break;
            case 3:
                input = choice + "\n2\nCS302\n3\n13\n";
                break;
            case 4:
                input = choice + "\n2\nCS302\n3\n13\n";
                break;
            case 5:
                input = choice + "\n4\n2\nCS302\n3\n13\n";
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
        DatabaseUtils.executeUpdateQuery(conn, "insert into config_number values(1)");
        if (expected == 2) {
            result = admin.adminOptions(scan);
            assertEquals("fail", result);
            scan.close();
            return;
        }
        if (expected == 3) {
            result = admin.adminOptions(scan);
            assertEquals("pass", result);
            scan.close();
            return;
        }
        DatabaseUtils.executeUpdateQuery(conn, "delete from course_catalog");
        if (expected == 4 || expected == 5) {
            result = admin.adminOptions(scan);
            assertEquals("pass", result);
            scan.close();
            return;
        }
        scan.close();

    }

    @AfterEach
    public void tearDown() {
        conn = DatabaseUtils.connect();
        DatabaseUtils.executeUpdateQuery(conn, "delete from config_number");
        DatabaseUtils.executeUpdateQuery(conn, "insert into config_number  values(4)");
        DatabaseUtils.executeUpdateQuery(conn, "delete from course_catalog");
        conn = null;
    }
}
