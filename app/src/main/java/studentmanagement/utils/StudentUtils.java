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

    /*
     * checks if student has passed the course everr.
     * accounts for cases like if it has failed first and then passed later
     * returns true if passed, false otherwise
     */
    public static Boolean getPassStatus(String course_code, String email, Connection conn) {
        String table_name = "s" + email.substring(0, 11);

        String query = "select * from " + table_name + " where course = '" + course_code
                + "' and grade !='F' and grade is not null";
        ResultSet resultSet = DatabaseUtils.getResultSet(conn, query);
        try {
            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /*
     * checks if student is elgible for graduation or not
     * checks if student has passed all the courses
     * checks if student has completed the minimum credits required for graduation
     * checks if all the B.Tech capstones is done
     * returns true if elgible, false otherwise
     */
    public static Boolean graduationCheck(String email, Connection conn) {
        if (StaffUtils.checkStudentExist(conn, email) == false) {
            System.out.println("Student does not exist");
            return false;
        }
        String table_name = "s" + email.substring(0, 11);
        String query = "select s.sem, s.ay,c.c,s.course,s.grade from " + table_name
                + " as s,course_catalog as c where s.course = c.course_code and s.ay = c.ay and s.sem = c.sem";
        ResultSet resultSet = DatabaseUtils.getResultSet(conn, query);
        Integer total_credits = 0;
        try {
            while (resultSet.next()) {
                String course = resultSet.getString(4);
                Integer credits = resultSet.getInt(3);
                if (getPassStatus(course, email, conn)) {
                    total_credits += credits;
                } else {
                    System.out.println("You have not passed the course: " + course);
                    return false;
                }

            }
            if (total_credits < AcademicNorms.minCreditReqGraduation) {
                System.out.println("You have not completed the minimum credits required for graduation");
                return false;
            }
            System.out.println("You have completed " + total_credits + " credits");
            if (getPassStatus("CP301", email, conn) && getPassStatus("CP302", email, conn)
                    && getPassStatus("CP303", email, conn)) {
                System.out.println("You have completed the BTP requirements");
                return true;
            }
            System.out.println("You have not completed the BTP requirements");
            return false;

        } catch (SQLException e) {
            System.out.println("student doesn't exist!");
        }
        return false;
    }
}
