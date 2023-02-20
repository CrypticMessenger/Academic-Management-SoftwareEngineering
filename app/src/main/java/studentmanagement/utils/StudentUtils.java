package studentmanagement.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentUtils {
    public static String viewGrades(Connection conn, String student_email) {
        try {
            if (!StaffUtils.checkStudentExist(conn, student_email)) {
                System.out.println("Student does not exist");
                return "fail";
            }
            String table_name = "s" + student_email.substring(0, 11);

            ResultSet resultSet = DatabaseUtils.getResultSet(conn,
                    "select sem,ay,course,coalesce(grade,'Ongoing')  from " + table_name);
            System.out.println("Sem | Ay | Course | Grade");
            while (resultSet.next()) {
                System.out.println(
                        resultSet.getString(1) + " | " + resultSet.getString(2) + " | " + resultSet.getString(3)
                                + " | " + resultSet.getString(4));
            }
            return "pass";
        } catch (SQLException e) {
            System.out.println("Error in Student viewGrades");
            e.printStackTrace();
        }
        return "error";
    }

    public static String[][] get_prev_2_sem_ay(String ay, String sem) {
        String[] ay_sem = ay.split("-");
        Integer ay_int = Integer.parseInt(ay_sem[0]);
        Integer sem_int = Integer.parseInt(sem);
        String[] past_2_ay = new String[2];
        String[] past_2_sem = new String[2];
        String ay_temp;
        String sem_temp;
        Integer i = 0;
        while (i < 2) {
            if (sem_int == 1) {
                ay_int -= 1;
                sem_int = 2;
            } else {
                sem_int = 1;
            }
            ay_temp = ay_int.toString() + "-" + ((ay_int + 1) % 100);
            sem_temp = sem_int.toString();
            past_2_ay[i] = ay_temp;
            past_2_sem[i] = sem_temp;
            i++;
        }
        String[][] past_2_ay_sem = { past_2_ay, past_2_sem };
        return past_2_ay_sem;
    }

    public static Boolean viewCourseGrade(Connection conn, String email, String course_code, String ay, String sem) {
        // somewhat like table format
        String table_name = "s" + email.substring(0, 11);
        try {
            ResultSet resultSet = DatabaseUtils.getResultSet(conn,
                    "select * from " + table_name + " where course='" + course_code + "' and ay='" + ay
                            + "' and sem='" + sem + "' ");
            if (resultSet.next()) {
                System.out.println(email + " : " + resultSet.getString(4));
            }
            return true;

        } catch (SQLException e) {
            System.out.println("Error in viewCourseGrade");
            e.printStackTrace();
        }
        return false;
    }

    public static String viewCourseRecord(Connection conn, String course_code, String ay, String sem) {
        String result = "fail";
        Boolean res = true;
        try {
            ResultSet resultSet = DatabaseUtils.getResultSet(conn, "select id from user_auth where roles='s'");
            System.out.println(
                    "-----------Course Code: " + course_code + "---AY: " + ay + "--- SEM: " + sem + "----------------");
            System.out.println("Student email : Grade");
            while (resultSet.next()) {
                String student_email = resultSet.getString(1);
                res = res && viewCourseGrade(conn, student_email, course_code, ay, sem);
            }
            System.out.println("--------------------------------------------------------------------");
            if (res) {
                result = "pass";
            }
        } catch (SQLException e) {
            System.out.println("Error in viewCourseRecord");
            e.printStackTrace();
        }
        return result;
    }
}
