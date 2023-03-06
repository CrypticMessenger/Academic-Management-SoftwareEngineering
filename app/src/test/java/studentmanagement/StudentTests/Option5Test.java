package studentmanagement.StudentTests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import studentmanagement.Student;
import studentmanagement.utils.DatabaseUtils;
import studentmanagement.utils.StudentUtils;

public class Option5Test {
        Student st = null;
        Connection conn = null;

        @BeforeEach
        public void setUp() {
                conn = DatabaseUtils.connect();
                st = new Student("2020csb1072@iitrpr.ac.in", conn, "2020-21", "2");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course,grade) values (1,'2020-21','GE103','A')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course,grade) values (1,'2020-21','MA101','A-')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course,grade) values (1,'2020-21','HS103','B')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course,grade) values (1,'2020-21','PH101','A')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course,grade) values (1,'2020-21','CS101','F')");
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
                                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pe_for,pe_minsem) values('CS545',3,1,2,null,'2021-22',1,'pe',Array ['cs'],0)");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pe_for,pe_minsem) values('CS546',3,1,2,null,'2021-22',1,'pe',Array ['cs'],0)");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pe_for,pe_minsem) values('CS547',3,1,2,null,'2021-22',1,'pe',Array ['cs'],0)");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pe_for,pe_minsem) values('CS548',3,1,2,null,'2021-22',1,'pe',Array ['cs'],0)");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pe_for,pe_minsem) values('CS549',3,1,2,null,'2021-22',1,'pe',Array ['cs'],0)");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pe_for,pe_minsem) values('CS550',3,1,2,null,'2021-22',1,'pe',Array ['cs'],0)");

                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pe_for,pe_minsem) values('CS551',3,1,2,null,'2021-22',2,'pe',Array ['cs'],0)");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pe_for,pe_minsem) values('CS552',3,1,2,null,'2021-22',2,'pe',Array ['cs'],0)");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pe_for,pe_minsem) values('CS553',3,1,2,null,'2021-22',2,'pe',Array ['cs'],0)");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pe_for,pe_minsem) values('CS554',3,1,2,null,'2021-22',2,'pe',Array ['cs'],0)");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pe_for,pe_minsem) values('CS555',3,1,2,null,'2021-22',2,'pe',Array ['cs'],0)");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pe_for,pe_minsem) values('CS556',3,1,2,null,'2021-22',2,'pe',Array ['cs'],0)");

                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pe_for,pe_minsem) values('CS557',3,1,2,null,'2022-23',1,'pe',Array ['cs'],0)");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pe_for,pe_minsem) values('CS558',3,1,2,null,'2022-23',1,'pe',Array ['cs'],0)");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pe_for,pe_minsem) values('CS559',3,1,2,null,'2022-23',1,'pe',Array ['cs'],0)");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pe_for,pe_minsem) values('CS560',3,1,2,null,'2022-23',1,'pe',Array ['cs'],0)");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pe_for,pe_minsem) values('CS561',3,1,2,null,'2022-23',1,'pe',Array ['cs'],0)");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pe_for,pe_minsem) values('CS562',3,1,2,null,'2022-23',1,'pe',Array ['cs'],0)");

                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pe_for,pe_minsem) values('CS563',3,1,2,null,'2022-23',2,'pe',Array ['cs'],0)");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pe_for,pe_minsem) values('CS564',3,1,2,null,'2022-23',2,'pe',Array ['cs'],0)");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pe_for,pe_minsem) values('CS565',3,1,2,null,'2022-23',2,'pe',Array ['cs'],0)");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pe_for,pe_minsem) values('CS566',3,1,2,null,'2022-23',2,'pe',Array ['cs'],0)");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pe_for,pe_minsem) values('CS567',3,1,2,null,'2022-23',2,'pe',Array ['cs'],0)");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pe_for,pe_minsem) values('CS568',3,1,2,null,'2022-23',2,'pe',Array ['cs'],0)");

                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pe_for,pe_minsem) values('CS569',3,1,2,null,'2023-24',1,'pe',Array ['cs'],0)");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pe_for,pe_minsem) values('CS570',3,1,2,null,'2023-24',1,'pe',Array ['cs'],0)");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pe_for,pe_minsem) values('CS571',3,1,2,null,'2023-24',1,'pe',Array ['cs'],0)");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pe_for,pe_minsem) values('CS572',3,1,2,null,'2023-24',1,'pe',Array ['cs'],0)");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pe_for,pe_minsem) values('CS573',3,1,2,null,'2023-24',1,'pe',Array ['cs'],0)");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pe_for,pe_minsem) values('CS574',3,1,2,null,'2023-24',1,'pe',Array ['cs'],0)");

                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pe_for,pe_minsem) values('CS575',3,1,2,null,'2023-24',2,'pe',Array ['cs'],0)");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pe_for,pe_minsem) values('CS576',3,1,2,null,'2023-24',2,'pe',Array ['cs'],0)");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pe_for,pe_minsem) values('CS577',3,1,2,null,'2023-24',2,'pe',Array ['cs'],0)");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pe_for,pe_minsem) values('CS578',3,1,2,null,'2023-24',2,'pe',Array ['cs'],0)");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pe_for,pe_minsem) values('CS579',3,1,2,null,'2023-24',2,'pe',Array ['cs'],0)");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pe_for,pe_minsem) values('CS580',3,1,2,null,'2023-24',2,'pe',Array ['cs'],0)");

                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_offerings(course_code, instructor_id) values('CS575','gunturi@iitrpr.ac.in')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_offerings(course_code, instructor_id) values('CS576','gunturi@iitrpr.ac.in')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_offerings(course_code, instructor_id) values('CS577','gunturi@iitrpr.ac.in')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_offerings(course_code, instructor_id) values('CS578','gunturi@iitrpr.ac.in')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_offerings(course_code, instructor_id) values('CS579','gunturi@iitrpr.ac.in')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_offerings(course_code, instructor_id) values('CS580','gunturi@iitrpr.ac.in')");

                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course,grade) values (2,'2020-21','CS539','A-')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course,grade) values (2,'2020-21','CS540','B-')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course,grade) values (2,'2020-21','CS541','B')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course,grade) values (2,'2020-21','CS542','A')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course,grade) values (2,'2020-21','CS543','A')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course,grade) values (2,'2020-21','CS544','C')");

                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course,grade) values (1,'2021-22','CS545','A-')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course,grade) values (1,'2021-22','CS546','B-')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course,grade) values (1,'2021-22','CS547','B')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course,grade) values (1,'2021-22','CS548','A')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course,grade) values (1,'2021-22','CS549','A')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course,grade) values (1,'2021-22','CS550','C')");

                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course,grade) values (2,'2021-22','CS551','A-')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course,grade) values (2,'2021-22','CS552','B-')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course,grade) values (2,'2021-22','CS553','B')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course,grade) values (2,'2021-22','CS554','A')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course,grade) values (2,'2021-22','CS555','A')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course,grade) values (2,'2021-22','CS556','C')");

                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course,grade) values (1,'2022-23','CS557','A-')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course,grade) values (1,'2022-23','CS558','B-')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course,grade) values (1,'2022-23','CS559','B')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course,grade) values (1,'2022-23','CS560','A')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course,grade) values (1,'2022-23','CS561','A')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course,grade) values (1,'2022-23','CS562','C')");

                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course,grade) values (2,'2022-23','CS563','A-')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course,grade) values (2,'2022-23','CS564','B-')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course,grade) values (2,'2022-23','CS565','B')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course,grade) values (2,'2022-23','CS566','A')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course,grade) values (2,'2022-23','CS567','A')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course,grade) values (2,'2022-23','CS568','C')");

                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course,grade) values (1,'2023-24','CS569','A-')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course,grade) values (1,'2023-24','CS570','B-')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course,grade) values (1,'2023-24','CS571','B')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course,grade) values (1,'2023-24','CS572','A')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course,grade) values (1,'2023-24','CS573','A')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course,grade) values (1,'2023-24','CS574','C')");

                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course) values (2,'2023-24','CS575')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course) values (2,'2023-24','CS576')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course) values (2,'2023-24','CS577')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course) values (2,'2023-24','CP301')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course) values (2,'2023-24','CP302')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course) values (2,'2023-24','CP303')");

                DatabaseUtils.executeUpdateQuery(conn, "update config_number set id=1 ");
                DatabaseUtils.executeUpdateQuery(conn, "delete from current_session");
                DatabaseUtils.executeUpdateQuery(conn, "insert into current_session values('2023-24', 2)");
        }

        @ParameterizedTest
        @CsvSource({ "5,1", "5,2", "5,3", "5,4", "5,5", "5,6", "5,7", "5,8", "5,9" })
        public void testGraduation(String choice, Integer expected) {
                String result;
                String input = choice + "\n7\n";
                ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
                System.setIn(inputStream);
                Scanner scan = new Scanner(System.in);

                if (expected == 1) {
                        result = st.studentOptions(scan);
                        assertEquals("false", result);
                        scan.close();
                        return;

                }
                DatabaseUtils.executeUpdateQuery(conn, "update s2020csb1072 set grade='C-' where course='CS101'");
                if (expected == 2) {
                        result = st.studentOptions(scan);
                        assertEquals("false", result);
                        scan.close();
                        return;

                }
                DatabaseUtils.executeUpdateQuery(conn, "delete from s2020csb1072 where grade is null ");
                if (expected == 3) {
                        result = st.studentOptions(scan);
                        assertEquals("false", result);
                        scan.close();
                        return;

                }
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course,grade) values (2,'2023-24','CS575','B-')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course,grade) values (2,'2023-24','CS576','A')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course,grade) values (2,'2023-24','CS577','B')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course,grade) values (2,'2023-24','CP301',null)");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course,grade) values (2,'2023-24','CP302',null)");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course,grade) values (2,'2023-24','CP303',null)");

                if (expected == 4) {
                        result = st.studentOptions(scan);
                        assertEquals("false", result);
                        scan.close();
                        return;

                }
                DatabaseUtils.executeUpdateQuery(conn, "update s2020csb1072 set grade='B-' where course='CP301'");
                if (expected == 5) {
                        result = st.studentOptions(scan);
                        assertEquals("false", result);
                        scan.close();
                        return;

                }
                DatabaseUtils.executeUpdateQuery(conn, "update s2020csb1072 set grade='B-' where course='CP302'");
                if (expected == 6) {
                        result = st.studentOptions(scan);
                        assertEquals("false", result);
                        scan.close();
                        return;

                }
                DatabaseUtils.executeUpdateQuery(conn, "update s2020csb1072 set grade='B-' where course='CP303'");
                if (expected == 7) {
                        result = st.studentOptions(scan);
                        assertEquals("true", result);
                        scan.close();
                        return;

                }
                DatabaseUtils.executeUpdateQuery(conn, "delete from s2020csb1072 where ay='2023-24' or ay='2022-23'");
                if (expected == 8) {
                        result = st.studentOptions(scan);
                        assertEquals("false", result);
                        scan.close();
                        return;

                }
                result = StudentUtils.getPassStatus("", "2020csb1072@iitrpr.ac.in", conn).toString();
                assertEquals("false", result);
                scan.close();

        }

        @AfterEach
        public void cleanup() throws Exception {
                conn = DatabaseUtils.connect();
                DatabaseUtils.executeUpdateQuery(conn, "delete from s2020csb1072");
                DatabaseUtils.executeUpdateQuery(conn, "delete from course_offerings");
                DatabaseUtils.executeUpdateQuery(conn, "delete from course_catalog");
                DatabaseUtils.executeUpdateQuery(conn, "delete from current_session");
                DatabaseUtils.executeUpdateQuery(conn, "insert into current_session values('2020-21', 1)");
                DatabaseUtils.executeUpdateQuery(conn, "update config_number set id=4 ");
                conn = null;
        }
}
