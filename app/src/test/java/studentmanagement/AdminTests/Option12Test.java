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

public class Option12Test {
    Admin admin = null;
    Connection conn = null;

    @BeforeEach
    public void setUp() {
        conn = DatabaseUtils.connect();
        admin = new Admin("admin@iitrpr.ac.in", conn, "2020-21", "2");
        DatabaseUtils.executeUpdateQuery(conn, "insert into s2020csb1072 values(1,'2020-21','CS301','A')");
        DatabaseUtils.executeUpdateQuery(conn,
                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pe_for,pe_minsem) values('CS301',3,1,2,null,'2020-21',1,'pe',Array ['cs'],0)");

    }

    @ParameterizedTest
    @CsvSource({ "12,1", "12,2", "12,3", "12,4", "12,5" })
    public void testOption12(String choice, Integer expected) {
        String result;
        String input = "";
        switch (expected) {
            case 1:
                input = choice + "\n1\n2020csb1072@iitrpr.ac.in\n3\n13\n";
                break;
            case 2: // fail
                input = choice + "\n1\n2020meb1072@iitrpr.ac.in\n3\n13\n";
                break;
            case 3: // fail
                input = choice + "\n1\n2020csbet1072@iitrpr.ac.in\n3\n13\n";
                break;
            case 4:
                input = choice + "\n2\n99\n3\n13\n";
                break;
            case 5:
                input = choice + "\n2\n99\n3\n99\n13\n";
                break;

        }
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        Scanner scan = new Scanner(System.in);
        if (expected == 1) {
            result = admin.adminOptions(scan);
            assertEquals("pass", result);
            scan.close();
            return;
        }

        if (expected == 2 || expected == 3) {
            result = admin.adminOptions(scan);
            assertEquals("fail", result);
            scan.close();
            return;
        }
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

        DatabaseUtils.executeUpdateQuery(conn, "delete from course_catalog");
        DatabaseUtils.executeUpdateQuery(conn, "delete from s2020csb1072");
        conn = null;
    }
}
