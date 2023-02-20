package studentmanagement.ProfessorTests;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import studentmanagement.App;
import studentmanagement.Professor;
import studentmanagement.utils.DatabaseUtils;

public class Option3Test {
    App app = null;
    Professor prof = null;
    Connection conn = null;

    @BeforeEach
    public void setUp() {
        app = new App();
        conn = app.connect();
        prof = new Professor("gunturi@iitrpr.ac.in", conn, "2020-21", "2");
        DatabaseUtils.executeUpdateQuery(conn, "delete from current_session");
        DatabaseUtils.executeUpdateQuery(conn, "insert into current_session values('2020-21', 2)");
        DatabaseUtils.executeUpdateQuery(conn,
                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pe_for,pe_minsem) values('CS550',3,1,2,null,'2020-21',1,'pe',Array ['cs'],0)");
        DatabaseUtils.executeUpdateQuery(conn,
                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pe_for,pe_minsem) values('CS551',3,1,2,null,'2020-21',2,'pe',Array ['cs'],0)");
        DatabaseUtils.executeUpdateQuery(conn, "update config_number set id=4");
        DatabaseUtils.executeUpdateQuery(conn,
                "insert into course_offerings(course_code, instructor_id) values('CS551','gunturi@iitrpr.ac.in')");

    }

    @ParameterizedTest
    @CsvSource({ "3,CS550,1", "3,CS550,2", "3,CS551,3" })
    public void testOption3(String choice, String courseCode, Integer expected) {
        String result;
        String input = choice + "\n" + courseCode + "\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        Scanner scan = new Scanner(System.in);
        if (expected == 1) {
            result = prof.professorOptions(scan);
            assertFalse(result == "pass");
            return;
        }
        DatabaseUtils.executeUpdateQuery(conn, "update config_number set id=2");

        result = prof.professorOptions(scan);
        if (expected == 2) {
            assertFalse(result == "pass");
            return;
        } else if (expected == 3) {
            assertTrue(result == "pass");

        }

    }

    @AfterEach
    public void tearDown() {
        DatabaseUtils.executeUpdateQuery(conn, "delete from s2020csb1072");
        DatabaseUtils.executeUpdateQuery(conn, "delete from course_offerings");
        DatabaseUtils.executeUpdateQuery(conn, "delete from course_catalog");
        DatabaseUtils.executeUpdateQuery(conn, "delete from current_session");
        DatabaseUtils.executeUpdateQuery(conn, "insert into current_session values('2020-21', 1)");
        DatabaseUtils.executeUpdateQuery(conn, "update config_number set id=4 ");
        prof.finalize();
        conn = null;
        app = null;

    }
}
