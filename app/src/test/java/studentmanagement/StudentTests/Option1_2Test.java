package studentmanagement.StudentTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import studentmanagement.Student;
import studentmanagement.utils.DatabaseUtils;

public class Option1_2Test {
        Student st = null;
        Connection conn = null;

        @BeforeEach
        public void setUp() throws Exception {
                conn = DatabaseUtils.connect();
                st = new Student("2020csb1072@iitrpr.ac.in", conn, "2020-21", "2");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course,grade) values (1,'2020-21','CS101','F')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course,grade) values (1,'2020-21','GE103','A')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course,grade) values (1,'2020-21','MA101','A-')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course,grade) values (1,'2020-21','HS103','B')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course,grade) values (1,'2020-21','PH101','A')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course,grade) values (1,'2020-21','GE105','A')");

                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pe_for,pe_minsem) values('CS101',3,1,2,null,'2020-21',1,'pe',Array ['cs'],0)");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pe_for,pe_minsem) values('GE103',3,1,2,null,'2020-21',1,'pe',Array ['cs'],0)");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pe_for,pe_minsem) values('MA101',3,1,2,null,'2020-21',1,'pe',Array ['cs'],0)");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pe_for,pe_minsem) values('HS103',3,1,2,null,'2020-21',1,'pe',Array ['cs'],0)");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pe_for,pe_minsem) values('PH101',3,1,2,null,'2020-21',1,'pe',Array ['cs'],0)");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pe_for,pe_minsem) values('GE105',3,1,2,null,'2020-21',1,'pe',Array ['cs'],0)");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pe_for,pe_minsem) values('CS201',3,1,2,Array ['CS101'],'2020-21',2,'pe',Array ['cs'],0)");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pe_for,pe_minsem) values('CS202',3,1,2,null,'2020-21',2,'pe',Array ['cs'],0)");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pe_for,pe_minsem) values('CS301',3,1,2,Array ['CS201'],'2020-21',2,'pe',Array ['cs'],0)");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pe_for,pe_minsem) values('CS302',3,1,2,Array ['CS201'],'2020-21',2,'pe',Array ['cs'],0)");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pe_for,pe_minsem) values('CS539',3,1,2,null,'2020-21',2,'pe',Array ['cs'],0)");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pe_for,pe_minsem) values('CS540',3,1,2,null,'2020-21',2,'pe',Array ['cs'],0)");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pe_for,pe_minsem) values('CS541',3,1,2,null,'2020-21',2,'pe',Array ['cs'],0)");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pe_for,pe_minsem) values('CS542',3,1,2,null,'2020-21',2,'pe',Array ['cs'],0)");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pe_for,pe_minsem) values('CS543',3,1,2,null,'2020-21',2,'pe',Array ['cs'],0)");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pe_for,pe_minsem) values('CS588',3,1,2,null,'2020-21',2,'pe',Array ['cs'],0)");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pe_for,pe_minsem) values('CS544',3,1,2,null,'2020-21',2,'pe',Array ['mc'],0)");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_offerings(course_code, instructor_id) values('CS202','apurva@iitrpr.ac.in')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_offerings(course_code, instructor_id) values('CS201','apurva@iitrpr.ac.in')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_offerings(course_code, instructor_id) values('CS301','balwinder@iitrpr.ac.in')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_offerings(course_code, instructor_id,cg_constraint) values('CS588','balwinder@iitrpr.ac.in',10)");

                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_offerings(course_code, instructor_id) values('CS539','gunturi@iitrpr.ac.in')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_offerings(course_code, instructor_id) values('CS540','gunturi@iitrpr.ac.in')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_offerings(course_code, instructor_id) values('CS541','gunturi@iitrpr.ac.in')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_offerings(course_code, instructor_id) values('CS542','gunturi@iitrpr.ac.in')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_offerings(course_code, instructor_id) values('CS543','gunturi@iitrpr.ac.in')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_offerings(course_code, instructor_id) values('CS544','gunturi@iitrpr.ac.in')");

                DatabaseUtils.executeUpdateQuery(conn, "delete from current_session");
                DatabaseUtils.executeUpdateQuery(conn, "insert into current_session(ay,sem) values('2020-21',2)");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course) values (2,'2020-21','CS539')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course) values (2,'2020-21','CS540')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course) values (2,'2020-21','CS541')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course) values (2,'2020-21','CS542')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course) values (2,'2020-21','CS543')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course) values (2,'2020-21','CS544')");
                DatabaseUtils.executeUpdateQuery(conn, "update config_number set id=1 ");

        }

        @ParameterizedTest
        @CsvSource({ "2,CS301,1", "2,CS301,2", "2,CS532,3", "2,CS539,4", "2,CompSci582,5", "2,CS5845,6" })
        public void testOption2(String choice, String courseCode, Integer expected) {
                String result;
                String input = choice + "\n" + courseCode + "\n7\n";
                ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
                System.setIn(inputStream);
                Scanner scan = new Scanner(System.in);
                if (expected == 1) {
                        result = st.studentOptions(scan);
                        assertEquals("fail", result);
                        return;
                }
                DatabaseUtils.executeUpdateQuery(conn, "update config_number set id=4 ");
                if (expected == 2 || expected == 3 || expected == 5 || expected == 6) {
                        result = st.studentOptions(scan);
                        assertEquals("fail", result);
                        return;
                }

                if (expected == 4) {
                        result = st.studentOptions(scan);
                        assertEquals("pass", result);
                        return;
                }

                scan.close();

        }

        @ParameterizedTest
        @CsvSource({ "1,CS302,1", "1,CS302,2", "1,CS588,3", "1,CS202,4", "1,CS539,5", "1,CS201,6", "1,CS544,7",
                        "1,CS202,8", "1,CompSci582,9", "1,CS5845,10" })
        public void testOption1(String choice, String courseCode, Integer expected) {
                String result;
                String input = "";
                if (expected != 1) {
                        input = choice + "\n" + courseCode + "\n7\n";
                } else {
                        input = choice + "\n7\n";
                }
                ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
                System.setIn(inputStream);
                Scanner scan = new Scanner(System.in);
                if (expected == 1) {
                        result = st.studentOptions(scan);
                        assertEquals("fail", result);
                        scan.close();
                        return;
                }
                DatabaseUtils.executeUpdateQuery(conn, "update config_number set id=4 ");
                if (expected == 2 || expected == 3 || expected == 4 || expected == 9 || expected == 10) {
                        result = st.studentOptions(scan);
                        assertEquals("fail", result);
                        scan.close();
                        return;
                }

                DatabaseUtils.executeUpdateQuery(conn, "delete from s2020csb1072 where course='CS544'");
                if (expected == 5 || expected == 6 || expected == 7) {
                        result = st.studentOptions(scan);
                        assertEquals("fail", result);
                        scan.close();
                        return;
                }
                if (expected == 8) {
                        result = st.studentOptions(scan);
                        assertEquals("pass", result);
                        scan.close();
                        return;
                }
                scan.close();

        }

        @ParameterizedTest
        @CsvSource({ "CS302,1", "CS302,2", "CS588,3", "CS202,4", "CS539,5", "CS201,6",
                        "CS202,8", "CompSci582,9", "CS5845,10" })
        public void testOption_1force(String courseCode, Integer expected) {
                String result;

                if (expected == 1) {
                        result = st.registerCourse(courseCode, "force");
                        assertEquals("fail", result);
                        return;
                }
                DatabaseUtils.executeUpdateQuery(conn, "update config_number set id=4 ");
                if (expected == 2 || expected == 3 || expected == 4 || expected == 9 || expected == 10) {
                        result = st.registerCourse(courseCode, "force");
                        assertEquals("fail", result);
                        return;
                }

                DatabaseUtils.executeUpdateQuery(conn, "delete from s2020csb1072 where course='CS544'");
                if (expected == 5 || expected == 6 || expected == 7) {
                        result = st.registerCourse(courseCode, "force");
                        assertEquals("fail", result);
                        return;
                }
                if (expected == 8) {
                        result = st.registerCourse(courseCode, "force");
                        assertEquals("pass", result);
                        return;
                }

        }

        @Test
        void testGetName() {
                assertEquals("Ankit Sharma", st.getName());
        }

        @AfterEach
        void cleanUpDeregister() {

                conn = DatabaseUtils.connect();
                DatabaseUtils.executeUpdateQuery(conn, "delete from s2020csb1072");
                DatabaseUtils.executeUpdateQuery(conn, "delete from course_offerings");
                DatabaseUtils.executeUpdateQuery(conn, "delete from course_catalog");
                DatabaseUtils.executeUpdateQuery(conn, "delete from current_session");
                DatabaseUtils.executeUpdateQuery(conn, "insert into current_session(ay,sem) values('2020-21',1)");
                DatabaseUtils.executeUpdateQuery(conn, "update config_number set id=4 ");
                conn = null;
        }
}
