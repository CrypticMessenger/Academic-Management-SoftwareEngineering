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

public class Option8Test {
    App app = null;
    Admin admin = null;
    Connection conn = null;

    @BeforeEach
    public void setUp() {
        app = new App();
        conn = app.connect();
        admin = new Admin("admin@iitrpr.ac.in", conn, "2020-21", "2");
        DatabaseUtils.executeUpdateQuery(conn, "delete from config_number");
        DatabaseUtils.executeUpdateQuery(conn, "insert into config_number values(6)");
        DatabaseUtils.executeUpdateQuery(conn,
                "insert into course_offerings(course_code, instructor_id) values('CS302','gunturi@iitrpr.ac.in')");
        DatabaseUtils.executeUpdateQuery(conn,
                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pc_for,pc_sem) values('CS302',3,1,2,null,'2020-21',2,'pc',Array ['ce'],1)");
        DatabaseUtils.executeUpdateQuery(conn, "insert into s2020csb1072 values(2,'2020-21','CS302')");

    }

    @ParameterizedTest
    @CsvSource({ "8,1", "8,2", "8,3" })
    public void testOption8(String choice, Integer expected) {
        String result;
        String input = choice + "\n13\n";
        // case statements in java

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
        DatabaseUtils.executeUpdateQuery(conn, "insert into config_number values(7)");

        if (expected == 2) {
            result = admin.adminOptions(scan);
            assertEquals("pass", result);
            scan.close();

            return;
        }
        DatabaseUtils.executeUpdateQuery(conn, "update s2020csb1072 set grade='A' where course='CS302'");

        if (expected == 3) {
            result = admin.adminOptions(scan);
            assertEquals("pass", result);
            scan.close();
            return;
        }

        scan.close();

    }

    @AfterEach
    public void tearDown() {
        conn = app.connect();
        DatabaseUtils.executeUpdateQuery(conn, "delete from config_number");
        DatabaseUtils.executeUpdateQuery(conn, "insert into config_number values(4)");
        DatabaseUtils.executeUpdateQuery(conn, "delete from course_offerings");
        DatabaseUtils.executeUpdateQuery(conn, "delete from course_catalog");
        DatabaseUtils.executeUpdateQuery(conn, "delete from s2020csb1072");
        conn = null;
        app = null;
    }
}
