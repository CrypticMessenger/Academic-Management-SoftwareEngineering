package studentmanagement;

import java.sql.*;

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

}
