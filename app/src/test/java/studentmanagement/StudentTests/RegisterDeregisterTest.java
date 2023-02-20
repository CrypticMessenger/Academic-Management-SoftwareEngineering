package studentmanagement.StudentTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Connection;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import studentmanagement.App;
import studentmanagement.Student;
import studentmanagement.utils.DatabaseUtils;

public class RegisterDeregisterTest {
        App app = null;
        Student st = null;
        Connection conn = null;

        @BeforeEach
        @Tag("register-deregister")
        public void setUpDeregister() throws Exception {
                app = new App();
                conn = app.connect();
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

                DatabaseUtils.executeUpdateQuery(conn, "update current_session set ay='2020-21' and sem=2 ");
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

        @Test
        @Tag("register-deregister")
        void testDeRegister() {

                String result = st.deregisterCourse("CS301");
                assertEquals("fail:not_allowed", result);
                DatabaseUtils.executeUpdateQuery(conn, "update config_number set id=4 ");
                result = st.deregisterCourse("CS301");
                assertEquals("fail:not_registered_or_credited", result);
                result = st.deregisterCourse("CS532");
                assertEquals("fail:not_registered_or_credited", result);
                result = st.deregisterCourse("CS539");
                assertEquals("Success", result);
        }

        @Test
        @Tag("register-deregister")
        void testRegister() {
                Scanner scan = new Scanner(System.in);
                String result = st.registerCourse("CS302", scan);
                assertEquals("fail:registration_not_open", result);

                DatabaseUtils.executeUpdateQuery(conn, "update config_number set id=4 ");

                result = st.registerCourse("CS302", scan);
                assertEquals("fail:course_not_offered", result);

                result = st.registerCourse("CS588", scan);
                assertEquals("fail:cgpa_constraint_not_met", result);

                result = st.registerCourse("CS202", scan);
                assertEquals("fail:credit_limit_exceeded", result);

                DatabaseUtils.executeUpdateQuery(conn, "delete from s2020csb1072 where course='CS544'");

                result = st.registerCourse("CS539", scan);
                assertEquals("fail:already_registered_or_credited", result);

                result = st.registerCourse("CS201", scan);
                assertEquals("fail:prerequisite_condition_not_met", result);

                result = st.registerCourse("CS544", scan);
                assertEquals("fail:course_not_offered_for_your_branch", result);

                result = st.registerCourse("CS202", scan);
                assertEquals("success", result);
        }

        @Test
        @Tag("name")
        void testGetName() {
                assertEquals("Ankit Sharma", st.getName());
        }

        @AfterEach
        @Tag("register-deregister")
        void cleanUpDeregister() {
                DatabaseUtils.executeUpdateQuery(conn, "delete from s2020csb1072");
                DatabaseUtils.executeUpdateQuery(conn, "delete from course_offerings");
                DatabaseUtils.executeUpdateQuery(conn, "delete from course_catalog");
                DatabaseUtils.executeUpdateQuery(conn, "update current_session set ay='2020-21' and sem=1 ");
                DatabaseUtils.executeUpdateQuery(conn, "update config_number set id=4 ");
                st.finalize();
        }

}