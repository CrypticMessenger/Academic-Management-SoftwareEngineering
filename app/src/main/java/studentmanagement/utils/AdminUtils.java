package studentmanagement.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminUtils {
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

        }
        return result;

    }
}
