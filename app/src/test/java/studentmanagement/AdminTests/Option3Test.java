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

public class Option3Test {
        Admin admin = null;
        Connection conn = null;

        @BeforeEach
        public void setUp() {
                conn = DatabaseUtils.connect();
                admin = new Admin("admin@iitrpr.ac.in", conn, "2020-21", "2");
                DatabaseUtils.executeUpdateQuery(conn, "delete from config_number");
                DatabaseUtils.executeUpdateQuery(conn, "insert into config_number values(3)");
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
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pc_for,pc_sem) values('CS539',3,1,2,null,'2020-21',2,'pc',Array ['cs'],1)");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pc_for,pc_sem) values('CS540',3,1,2,null,'2020-21',2,'pc',Array ['cs'],1)");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pc_for,pc_sem) values('CS541',3,1,2,null,'2020-21',2,'pc',Array ['cs'],1)");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pc_for,pc_sem) values('CS542',3,1,2,null,'2020-21',2,'pc',Array ['cs'],1)");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pc_for,pc_sem) values('CS543',3,1,2,null,'2020-21',2,'pc',Array ['cs'],2)");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into course_catalog(course_code, L, T,P,pre_req,ay,sem,pc_or_pe,pc_for,pc_sem) values('CS544',3,1,2,null,'2020-21',2,'pc',Array ['ce'],1)");

        }

        @ParameterizedTest
        @CsvSource({ "3,1", "3,2", "3,3", "3,4", "3,5" })
        public void testOption3(String choice, Integer expected) {
                String result;
                String input = "";
                // case statements in java
                switch (expected) {
                        case 1:
                                input = choice + "\n13\n";
                                break;
                        case 2:
                                input = choice + "\ny\n13\n";
                                break;
                        case 3:
                                input = choice + "\nn\n13\n";
                                break;
                        case 4:
                                input = choice + "\nok\ny\n13\n";
                                break;
                        case 5:
                                input = choice + "\nok\nn\n13\n";
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
                DatabaseUtils.executeUpdateQuery(conn, "insert into config_number values(4)");
                if (expected == 2 || expected == 4) {
                        result = admin.adminOptions(scan);
                        assertEquals("pass", result);
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
                // TODO: delete better way
                DatabaseUtils.executeUpdateQuery(conn, "delete from s2020csb1072");
                DatabaseUtils.executeUpdateQuery(conn, "delete from s2020csb1070");
                DatabaseUtils.executeUpdateQuery(conn, "delete from s2020csb1074");
                DatabaseUtils.executeUpdateQuery(conn, "delete from s2020ceb1031");
                conn = null;
        }
}
