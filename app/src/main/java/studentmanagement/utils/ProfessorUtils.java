package studentmanagement.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ProfessorUtils {
    public static void displayCourseCatalog(Connection conn) {
        ResultSet rs = DatabaseUtils.getResultSet(conn, "select * from course_catalog");
        System.out.println(
                "course_code | l | t | p | c | ay | sem | pre_req | pc_or_pe | pc_for | pe_for | pc_sem | pe_minsem");

        try {
            while (rs.next()) {
                String course_code = rs.getString("course_code");
                int l = rs.getInt("l");
                int t = rs.getInt("t");
                int p = rs.getInt("p");
                int c = rs.getInt("c");
                String ay = rs.getString("ay");
                int sem = rs.getInt("sem");
                String pre_req = rs.getString("pre_req");
                String pc_or_pe = rs.getString("pc_or_pe");
                String pc_for = rs.getString("pc_for");
                String pe_for = rs.getString("pe_for");
                int pc_sem = rs.getInt("pc_sem");
                int pe_minsem = rs.getInt("pe_minsem");

                System.out.println(course_code + " | " + l + " | " + t + " | " + p + " | " + c + " | " + ay + " | "
                        + sem
                        + " | " + pre_req + " | " + pc_or_pe + " | " + pc_for + " | " + pe_for + " | " + pc_sem + " | "
                        + pe_minsem);
            }
        } catch (SQLException e) {

        }
    }

    public static void saveCourseGrade(Connection conn, String email, String course_code,
            String csv_file, String ay, String sem) {
        // somewhat like table format
        String table_name = "s" + email.substring(0, 11);
        try {
            ResultSet resultSet = DatabaseUtils.getResultSet(conn,
                    "select * from " + table_name + " where course='" + course_code + "' and ay='" + ay
                            + "' and sem='" + sem + "' ");
            if (resultSet.next()) {
                // create a FileWriter object to write to the csv file
                FileWriter writer = new FileWriter(csv_file, true); // append mode
                // write the email and grade to the csv file, separated by a comma
                writer.write(email + "," + resultSet.getString(4) + "\n");
                // close the writer
                writer.close();
                System.out.println("Saved email and grade to " + csv_file);
            }

        } catch (SQLException | IOException e) {
            System.out.println("Error in saveCourseGrade");

        }
    }

    public static String saveCourseRecord(Connection conn, String course_code, String filename, String ay, String sem) {
        try {
            String checkFloatingCondition = "select * from course_offerings where course_code = '"
                    + course_code
                    + "' ";
            ResultSet rs = DatabaseUtils.getResultSet(conn, checkFloatingCondition);

            if (!rs.next()) {
                System.out.println("You cannot upload/download grades for a course that is not offered.");
                return "fail";
            }
            ResultSet resultSet = DatabaseUtils.getResultSet(conn, "select id from user_auth where roles='s'");
            System.out.println(
                    "-----------Course Code: " + course_code + "---AY: " + ay + "--- SEM: " + sem
                            + "----------------");
            System.out.println("Student email : Grade");
            while (resultSet.next()) {
                String student_email = resultSet.getString(1);
                saveCourseGrade(conn, student_email, course_code, filename, ay, sem);
            }
            System.out.println("--------------------------------------------------------------------");
            return "pass";
        } catch (SQLException e) {
            System.out.println("Error in viewCourseRecord");
        }
        return "fail";
    }

    public static String viewStudentRecordsOptions(Connection conn, Scanner scan) {
        while (true) {
            System.out.println("1: View records of one course");
            System.out.println("2: View records of one student");
            System.out.println("3: go back to menu!");
            System.out.print("Choose: ");
            String response = scan.nextLine();
            if (response.equals("1")) {
                System.out.print("Enter course code: ");
                String course_code = scan.nextLine();
                System.out.print("Enter ay(eg: 2013-14): ");
                String ay = scan.nextLine();
                if (!ay.matches("^\\d{4}-\\d{2}$")) {
                    System.out.println("Invalid ay");
                    return "fail";
                }
                System.out.print("Enter sem(eg: 1 or 2): ");
                String sem = scan.nextLine();
                if (!sem.equals("1") && !sem.equals("2")) {
                    System.out.println("Invalid sem");
                    return "fail";
                }
                // view for one course record
                return AdminUtils.viewCourseRecord(conn, course_code, ay, sem);

            } else if (response.equals("2")) {
                // one student DONE
                System.out.print("Enter student email: ");
                String student_email = scan.nextLine();
                if (!student_email.matches("^\\d{4}[A-Za-z0-9]{3}\\d{4}@iitrpr\\.ac\\.in$")) {
                    System.out.println("Invalid email");
                    return "fail";
                }
                return StudentUtils.viewGrades(conn, student_email);
            } else if (response.equals("3")) {

                return "pass";
            } else {
                System.out.println("Invalid input");
            }

        }

    }
}
