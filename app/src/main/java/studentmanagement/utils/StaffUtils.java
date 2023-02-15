package studentmanagement.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class StaffUtils {
    public static Integer getSemCompleted(Connection conn, String student_email) {
        // return number of sem completed
        Integer result = -1;
        try {
            String table_name = "s" + student_email.substring(0, 11);
            String query = "select count(*) from (select distinct sem, ay from " + table_name
                    + " where grade is not null) as cnt";
            ResultSet resultSet = DatabaseUtils.getResultSet(conn, query);
            resultSet.next();
            result = resultSet.getInt(1);
        } catch (SQLException e) {
            System.out.println("Error in getSemCompleted");
            e.printStackTrace();
        }
        return result;

    }

    public static Boolean checkStudentExist(Connection conn, String email) {
        try {
            String checkStudentQuery = "select * from user_auth where id ='" + email + "' and roles='s'";
            ResultSet resultSetCheckStudent = DatabaseUtils.getResultSet(conn, checkStudentQuery);
            return resultSetCheckStudent.next();
        } catch (SQLException e) {
            System.out.println("Error in checkStudentExist");
            e.printStackTrace();
            return false;
        }
    }

    public static void viewStudentRecordsOptions(Connection conn, Scanner scan) {
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
                System.out.print("Enter sem(eg: 1 or 2): ");
                String sem = scan.nextLine();
                // view for one course record
                StudentUtils.viewCourseRecord(conn, course_code, ay, sem);

            } else if (response.equals("2")) {
                // one student DONE
                System.out.print("Enter student email: ");
                String student_email = scan.nextLine();
                StudentUtils.viewGrades(conn, student_email);
            } else if (response.equals("3")) {
                break;
            } else {
                System.out.println("Invalid input");
            }

        }
    }

    public static String[][] get_next_n_sem_ay(Integer n, String ay, String sem) {
        String[] ay_sem = ay.split("-");
        Integer ay_int = Integer.parseInt(ay_sem[0]);
        Integer sem_int = Integer.parseInt(sem);
        String[] next_n_ay = new String[n];
        String[] next_n_sem = new String[n];
        String ay_temp;
        String sem_temp;
        Integer i = 0;
        while (i < n) {
            if (sem_int == 1) {
                sem_int = 2;
            } else {
                sem_int = 1;
                ay_int += 1;
            }
            ay_temp = ay_int.toString() + "-" + ((ay_int + 1) % 100);
            sem_temp = sem_int.toString();
            next_n_ay[i] = ay_temp;
            next_n_sem[i] = sem_temp;
            i++;
        }
        String[][] next_n_ay_sem = { next_n_ay, next_n_sem };
        return next_n_ay_sem;
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

        } catch (SQLException e) {
            System.out.println("Error in saveCourseGrade");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error in writing to csv file");
            e.printStackTrace();
        }
    }

    public static void saveCourseRecord(Connection conn, String course_code, String filename, String ay, String sem) {

        try {
            String checkFloatingCondition = "select * from course_offerings where course_code = '"
                    + course_code
                    + "' ";
            ResultSet rs = DatabaseUtils.getResultSet(conn, checkFloatingCondition);

            if (!rs.next()) {
                System.out.println("You cannot upload/download grades for a course that is not offered.");
                return;
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
        } catch (SQLException e) {
            System.out.println("Error in viewCourseRecord");
            e.printStackTrace();
        }
    }

}
