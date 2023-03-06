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

public class Option11Test {
    Admin admin = null;
    Connection conn = null;

    @BeforeEach
    public void setUp() {
        conn = DatabaseUtils.connect();
        admin = new Admin("admin@iitrpr.ac.in", conn, "2020-21", "2");
        DatabaseUtils.executeUpdateQuery(conn, "delete from config_number");
        DatabaseUtils.executeUpdateQuery(conn, "insert into config_number values(9)");
        DatabaseUtils.executeUpdateQuery(conn,
                "insert into course_offerings(course_code, instructor_id) values('CS302','gunturi@iitrpr.ac.in')");
        DatabaseUtils.executeUpdateQuery(conn,
                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pc_for,pc_sem) values('CS302',3,1,2,null,'2020-21',2,'pc',Array ['ce'],1)");
        DatabaseUtils.executeUpdateQuery(conn, "insert into s2020csb1072 values(2,'2020-21','CS302')");

    }

    @ParameterizedTest
    @CsvSource({ "11,1", "11,2", "11,3", "11,4", "11,5" })
    public void testOption11(String choice, Integer expected) {
        String result;
        String input = "";
        // case statements in java
        switch (expected) {
            case 1:
                input = choice + "\n1\nCS302\n2020-21\n2\n13\n";
                break;
            case 2:
                input = choice + "\n2\n2020csb1072@iitrpr.ac.in\n13\n";
                break;
            case 3:
                input = choice + "\n2\n2020meb1328@iitrpr.ac.in\n13\n";
                break;
            case 4:
                input = choice + "\n3\n13\n";
                break;
            case 5:
                input = choice + "\n99\n3\n13\n";
                break;

        }
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        Scanner scan = new Scanner(System.in);
        if (expected == 1 || expected == 2) {
            result = admin.adminOptions(scan);
            assertEquals("pass", result);
            scan.close();

            return;
        }

        if (expected == 3 || expected == 4 || expected == 5) {
            result = admin.adminOptions(scan);
            assertEquals("fail", result);
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
        DatabaseUtils.executeUpdateQuery(conn, "insert into config_number  values(4)");
        DatabaseUtils.executeUpdateQuery(conn, "delete from course_offerings");
        DatabaseUtils.executeUpdateQuery(conn, "delete from course_catalog");
        DatabaseUtils.executeUpdateQuery(conn, "delete from s2020csb1072");
        conn = null;
    }
}
