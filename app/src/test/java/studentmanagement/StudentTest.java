package studentmanagement;

import java.sql.Connection;

import org.junit.jupiter.api.Test;

public class StudentTest {

    @Test
    public void testGraduation() {
        App app = new App();
        Connection conn = app.connect();
        Student st = new Student("2020csb1072@iitrpr.ac.in", conn, "2020-21", "1");
    }

}
