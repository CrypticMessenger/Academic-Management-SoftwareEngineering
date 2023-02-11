package studentmanagement.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class AcademicNorms {
    public static Map<String, Integer> grade_to_number = new HashMap<>();
    public static Float minSemCredits = 12.0f;

    public static void setup_grade_to_number() {
        grade_to_number.put("A", 10);
        grade_to_number.put("A-", 9);
        grade_to_number.put("B", 8);
        grade_to_number.put("B-", 7);
        grade_to_number.put("C", 6);
        grade_to_number.put("C-", 5);
        grade_to_number.put("D", 4);
        grade_to_number.put("F", 0);
    }

    // setter for minSemCredits
    public static void setMinSemCredits(Float minSemCredits) {
        AcademicNorms.minSemCredits = minSemCredits;
    }

    public static void displayCourseCatalog(Connection conn) {
        ResultSet resultSet = DatabaseUtils.getResultSet(conn, "select * from course_offerings ");

        try {
            while (resultSet.next()) {
                try {
                    String course_code = resultSet.getString(1);
                    String course_name = resultSet.getString(2);
                    Float cgpa_constraint = resultSet.getFloat(3);
                    System.out.println(course_code + " " + course_name + " " + cgpa_constraint);
                } catch (SQLException e) {
                    System.out.println("Error in displayCourseCatalog");
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            System.out.println("Error in displayCourseCatalog");
            e.printStackTrace();
        }
    }
}
