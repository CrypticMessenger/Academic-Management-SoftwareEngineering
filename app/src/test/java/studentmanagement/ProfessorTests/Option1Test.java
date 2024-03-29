package studentmanagement.ProfessorTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

public class Option1Test {
    Professor prof = null;
    Connection conn = null;

    @BeforeEach
    public void setUp() {
        conn = DatabaseUtils.connect();
        prof = new Professor("gunturi@iitrpr.ac.in", conn, "2020-21", "2");
        DatabaseUtils.executeUpdateQuery(conn, "delete from current_session");
        DatabaseUtils.executeUpdateQuery(conn, "insert into current_session values('2020-21', 2)");
        DatabaseUtils.executeUpdateQuery(conn,
                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pe_for,pe_minsem) values('CS550',3,1,2,null,'2020-21',1,'pe',Array ['cs'],0)");
        DatabaseUtils.executeUpdateQuery(conn,
                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pe_for,pe_minsem) values('CS551',3,1,2,null,'2020-21',2,'pe',Array ['cs'],0)");
        DatabaseUtils.executeUpdateQuery(conn, "update config_number set id=4");
        DatabaseUtils.executeUpdateQuery(conn,
                "insert into s2020csb1072(sem,ay,course,grade) values (1,'2020-21','CS550','A-')");
        DatabaseUtils.executeUpdateQuery(conn,
                "insert into s2020csb1070(sem,ay,course,grade) values (1,'2020-21','CS550','A')");
        DatabaseUtils.executeUpdateQuery(conn,
                "insert into s2020csb1074(sem,ay,course,grade) values (1,'2020-21','CS550','B')");

    }

    @ParameterizedTest
    @CsvSource({ "1,1,CS550,2020-21,1,8,1", "1,1,CS550,2020-212,1,8,2", "1,1,CS550,,1,8,3", "1,1,CS550,2020-21,8,8,4",
            "1,1,CS550,2020-21,,8,5" })
    public void testOption1_oneCourse(String choice, String choice2, String courseCode, String ay, String sem,
            String exit,
            Integer expected) {
        String result;
        if (expected == 1) {
            String input = choice + "\n" + choice2 + "\n" + courseCode + "\n" + ay + "\n" + sem + "\n" + exit + "\n";

            ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
            System.setIn(inputStream);
            Scanner scan = new Scanner(System.in);
            result = prof.professorOptions(scan);
            assertEquals("pass", result);
            scan.close();
        } else if (expected == 2 || expected == 3) {
            String input = choice + "\n" + choice2 + "\n" + courseCode + "\n" + ay + "\n" + exit + "\n";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
            System.setIn(inputStream);
            Scanner scan = new Scanner(System.in);
            result = prof.professorOptions(scan);
            assertEquals("fail", result);
            scan.close();

        } else {
            String input = choice + "\n" + choice2 + "\n" + courseCode + "\n" + ay + "\n" + sem + "\n" + exit + "\n";

            ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
            System.setIn(inputStream);
            Scanner scan = new Scanner(System.in);
            result = prof.professorOptions(scan);
            assertEquals("fail", result);
            scan.close();
        }

    }

    @ParameterizedTest
    @CsvSource({ "1,2,2020csb1072@iitrpr.ac.in,8,1", "1,2,2020csb1072@iisc.in,8,2", "1,2,gunturi@iitrpr.ac.in,8,3",
            "1,2,2020csb1072@iitrprac.in,8,4", "1,2,2020meb1328@iitrpr.ac.in,8,5" })
    public void testOption1_oneStudent(String choice, String choice2, String student_id, String exit,
            Integer expected) {
        String result;
        String input = choice + "\n" + choice2 + "\n" + student_id + "\n" + exit + "\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        Scanner scan = new Scanner(System.in);
        if (expected == 1) {
            result = prof.professorOptions(scan);
            assertEquals("pass", result);
        } else {
            result = prof.professorOptions(scan);
            assertEquals("fail", result);
        }

        scan.close();

    }

    @ParameterizedTest
    @CsvSource({ "1,3,8,1" })
    public void testOption1_back(String choice, String choice2, String exit, Integer expected) {
        String result;
        String input = choice + "\n" + choice2 + "\n" + exit + "\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        Scanner scan = new Scanner(System.in);
        if (expected == 1) {
            result = prof.professorOptions(scan);
            assertEquals("pass", result);
        } else {
            result = prof.professorOptions(scan);
            assertEquals("fail", result);
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
