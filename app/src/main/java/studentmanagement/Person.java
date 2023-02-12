package studentmanagement;

import java.sql.*;
import java.util.Scanner;

import studentmanagement.utils.DatabaseUtils;

// an abstract class named Person with name and email
public abstract class Person {
    private String email;
    private static String ay;
    private static String sem;

    public Person(String email, String ay_in, String sem_in) {
        this.email = email;
        ay = ay_in;
        sem = sem_in;
    }

    public String getEmail() {
        return email;
    }

    // getter for ay
    public String getAy() {
        return ay;
    }

    // setter for ay
    public void setAy(String new_ay) {
        ay = new_ay;
    }

    // setter for sem
    public void setSem(String new_sem) {
        sem = new_sem;
    }

    // getter for sem
    public String getSem() {
        return sem;
    }

    public String[][] get_prev_2_sem_ay() {
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

    public String[][] get_next_n_sem_ay(Integer n) {
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

    public void log_login_logout(Connection conn, String email, String status) {
        try {
            String enter_logout_log = "Insert into login_log(email,status) values (?,?)";
            PreparedStatement statement = conn.prepareStatement(enter_logout_log);
            statement.setString(1, email);
            statement.setString(2, status);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error in log_login_logout");
            e.printStackTrace();
        }
    }

    public void displayCourseCatalog(Connection conn) {
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

    public void viewCourseGrade(Connection conn, String email, String course_code, String ay, String sem) {
        // somewhat like table format
        String table_name = "s" + email.substring(0, 11);
        try {
            ResultSet resultSet = DatabaseUtils.getResultSet(conn,
                    "select * from " + table_name + " where course='" + course_code + "' and ay='" + ay
                            + "' and sem='" + sem + "' ");
            if (resultSet.next()) {
                System.out.println(email + " : " + resultSet.getString(4));
            }

        } catch (SQLException e) {
            System.out.println("Error in viewCourseGrade");
            e.printStackTrace();
        }
    }

    public void viewCourseRecord(Connection conn, String course_code, String ay, String sem) {

        try {
            ResultSet resultSet = DatabaseUtils.getResultSet(conn, "select id from user_auth where roles='s'");
            System.out.println(
                    "-----------Course Code: " + course_code + "---AY: " + ay + "--- SEM: " + sem + "----------------");
            System.out.println("Student email : Grade");
            while (resultSet.next()) {
                String student_email = resultSet.getString(1);
                viewCourseGrade(conn, student_email, course_code, ay, sem);
            }
            System.out.println("--------------------------------------------------------------------");
        } catch (SQLException e) {
            System.out.println("Error in viewCourseRecord");
            e.printStackTrace();
        }
    }

    public Boolean checkStudentExist(Connection conn, String email) {
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

    public void viewGrades(Connection conn, String student_email) {
        try {
            if (!checkStudentExist(conn, student_email)) {
                System.out.println("Student does not exist");
                return;
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
        } catch (SQLException e) {
            System.out.println("Error in Student viewGrades");
            e.printStackTrace();
        }
    }

    public void viewStudentRecordsOptions(Connection conn, Scanner scan) {
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
                viewCourseRecord(conn, course_code, ay, sem);

            } else if (response.equals("2")) {
                // one student DONE
                System.out.print("Enter student email: ");
                String student_email = scan.nextLine();
                viewGrades(conn, student_email);
            } else if (response.equals("3")) {
                break;
            } else {
                System.out.println("Invalid input");
            }

        }
    }

}
