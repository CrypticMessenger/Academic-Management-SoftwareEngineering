package studentmanagement.ProfessorTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import studentmanagement.App;
import studentmanagement.Professor;
import studentmanagement.utils.DatabaseUtils;

public class Option4Test {
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
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course,grade) values (1,'2020-21','CS550','A-')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1070(sem,ay,course,grade) values (1,'2020-21','CS550','A')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1074(sem,ay,course,grade) values (2,'2020-21','CS550','B')");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1072(sem,ay,course,grade) values (2,'2020-21','CS551',null)");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1070(sem,ay,course,grade) values (2,'2020-21','CS551',null)");
                DatabaseUtils.executeUpdateQuery(conn,
                                "insert into s2020csb1074(sem,ay,course,grade) values (2,'2020-21','CS551',null)");
        }

        @ParameterizedTest
        @CsvSource({
                        "4,CS550,D:/vesthrax/Software engineering/StudentManagement/app/src/main/java/studentmanagement/grade_upload/CS551.csv,1",
                        "4,CS9999,D:/vesthrax/Software engineering/StudentManagement/app/src/main/java/studentmanagement/grade_upload/CS551.csv,2",
                        "4,CompSci551,D:/vesthrax/Software engineering/StudentManagement/app/src/main/java/studentmanagement/grade_upload/CS551.csv,3",
                        "4,CS550,D:/vesthrax/Software engineering/StudentManagement/app/src/main/java/studentmanagement/grade_upload/CS551.csv,4",
                        "4,CS551,D:/vesthrax/Software engineering/StudentManagement/app/src/main/java/studentmanagement/grade_upload/CS551.csv,5",
                        "4,CS551,D:/vesthrax/Software engineering/StudentManagement/app/src/main/java/studentmanagement/grade_upload/CS551.csv,6" })
        public void testOption4(String choice, String courseCode, String csv_path, Integer expected) throws Exception {
                String result;
                String input = choice + "\n" + courseCode + "\n" + csv_path + "\n7\n";
                ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
                System.setIn(inputStream);
                Scanner scan = new Scanner(System.in);
                if (expected == 1 || expected == 2 || expected == 3) {
                        result = prof.professorOptions(scan);
                        assertEquals("fail", result);
                        return;
                }
                DatabaseUtils.executeUpdateQuery(conn, "update config_number set id=6");
                if (expected == 4) {

                        result = prof.professorOptions(scan);
                        assertEquals("fail", result);
                        return;
                }

                String[] data = { "2020csb1070@iitrpr.ac.in,A", "2020csb1072@iitrpr.ac.in,A",
                                "2020csb1074@iitrpr.ac.in,B-",
                                "2020ceb1031@iitrpr.ac.in,B-", "2020meb1328@iitrpr.ac.in,B" };
                String fileName = csv_path;
                FileWriter writer = new FileWriter(fileName);
                // Write each data row
                for (String row : data) {
                        writer.write(row + "\n");
                }
                writer.close();
                File file = new File(csv_path);
                assertTrue(file.exists());

                if (expected == 6) {
                        result = prof.professorOptions(scan);
                        assertTrue(result == "pass");
                        file.delete();
                        return;
                }
                file.delete();
                if (expected == 5) {
                        result = prof.professorOptions(scan);
                        assertTrue(result == "fail");
                        return;
                }
                scan.close();

        }

        @AfterEach
        public void tearDown() {
                DatabaseUtils.executeUpdateQuery(conn, "delete from s2020csb1072");
                DatabaseUtils.executeUpdateQuery(conn, "delete from s2020csb1070");
                DatabaseUtils.executeUpdateQuery(conn, "delete from s2020csb1074");
                DatabaseUtils.executeUpdateQuery(conn, "delete from course_offerings");
                DatabaseUtils.executeUpdateQuery(conn, "delete from course_catalog");
                DatabaseUtils.executeUpdateQuery(conn, "delete from report_validator");
                DatabaseUtils.executeUpdateQuery(conn, "delete from current_session");
                DatabaseUtils.executeUpdateQuery(conn, "insert into current_session values('2020-21', 1)");
                DatabaseUtils.executeUpdateQuery(conn, "update config_number set id=4 ");
                prof.finalize();
                conn = null;
                app = null;

        }
}
