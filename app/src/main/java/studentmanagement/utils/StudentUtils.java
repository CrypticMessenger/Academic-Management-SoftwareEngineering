package studentmanagement.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentUtils {
    public static String viewGrades(Connection conn, String student_email) {
        try {
            if (!DatabaseUtils.checkStudentExist(conn, student_email)) {
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
        if (DatabaseUtils.checkStudentExist(conn, email) == false) {
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

    // prints course offerings, avaiable for course enrollment for students
    public static void displayCourseOfferings(Connection conn) {
        ResultSet resultSet = DatabaseUtils.getResultSet(conn, "select * from course_offerings ");

        try {
            while (resultSet.next()) {
                String course_code = resultSet.getString(1);
                String course_name = resultSet.getString(2);
                Float cgpa_constraint = resultSet.getFloat(3);
                System.out.println(course_code + " " + course_name + " " + cgpa_constraint);

            }
        } catch (SQLException e) {

        }
    }
}
