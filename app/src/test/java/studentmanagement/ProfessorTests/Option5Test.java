package studentmanagement.ProfessorTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import studentmanagement.Professor;
import studentmanagement.utils.DatabaseUtils;

public class Option5Test {
        Professor prof = null;
        Connection conn = null;

        @BeforeEach
        public void setUp() {
                conn = DatabaseUtils.connect();
                prof = new Professor("gunturi@iitrpr.ac.in", conn, "2020-21", "1");
                DatabaseUtils.executeUpdateQuery(conn, "delete from current_session");
                DatabaseUtils.executeUpdateQuery(conn, "insert into current_session values('2020-21', 1)");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pe_for,pe_minsem) values('CS550',3,1,2,null,'2019-20',2,'pe',Array ['cs'],0)");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pe_for,pe_minsem) values('CS551',3,1,2,null,'2020-21',1,'pe',Array ['cs'],0)");
                DatabaseUtils.executeUpdateQuery(conn, "update config_number set id=4");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_offerings(course_code, instructor_id) values('CS551','gunturi@iitrpr.ac.in')");
        }

        @ParameterizedTest
        @CsvSource({ "5,1", "5,2" })
        public void testOption5(String choice, Integer expected) {
                String result;
                String input = choice + "\n8\n";
                ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
                System.setIn(inputStream);
                Scanner scan = new Scanner(System.in);
                if (expected == 1) {
                        result = prof.professorOptions(scan);
                        assertFalse(result == "pass");
                        return;
                } else if (expected == 2) {
                        DatabaseUtils.executeUpdateQuery(conn,
                                        "insert into s2020csb1072(sem,ay,course,grade) values (1,'2020-21','CS551',null)");
                        DatabaseUtils.executeUpdateQuery(conn,
                                        "insert into s2020csb1070(sem,ay,course,grade) values (1,'2020-21','CS551',null)");
                        DatabaseUtils.executeUpdateQuery(conn,
                                        "insert into s2020csb1074(sem,ay,course,grade) values (1,'2020-21','CS551',null)");
                        DatabaseUtils.executeUpdateQuery(conn,
                                        "insert into report_validator values('CS551','2020csb1072@iitrpr.ac.in')");
                        DatabaseUtils.executeUpdateQuery(conn,
                                        "insert into report_validator values('CS551','2020csb1070@iitrpr.ac.in')");
                        DatabaseUtils.executeUpdateQuery(conn,
                                        "insert into report_validator values('CS551','2020csb1074@iitrpr.ac.in')");
                        DatabaseUtils.executeUpdateQuery(conn, "update config_number set id=8");
                        result = prof.professorOptions(scan);
                        assertTrue(result == "pass");
                        return;
                }
                scan.close();

        }

        @Test
        void testName() {
                assertEquals("V. Gunturi", prof.getName());
        }

        @AfterEach
        public void tearDown() {
                conn = DatabaseUtils.connect();
                DatabaseUtils.executeUpdateQuery(conn, "delete from s2020csb1072");
                DatabaseUtils.executeUpdateQuery(conn, "delete from s2020csb1070");
                DatabaseUtils.executeUpdateQuery(conn, "delete from s2020csb1074");
                DatabaseUtils.executeUpdateQuery(conn, "delete from course_offerings");
                DatabaseUtils.executeUpdateQuery(conn, "delete from course_catalog");
                DatabaseUtils.executeUpdateQuery(conn, "delete from report_validator");
                DatabaseUtils.executeUpdateQuery(conn, "delete from current_session");
                DatabaseUtils.executeUpdateQuery(conn, "insert into current_session values('2020-21', 1)");
                DatabaseUtils.executeUpdateQuery(conn, "update config_number set id=4 ");
                conn = null;

        }
}
