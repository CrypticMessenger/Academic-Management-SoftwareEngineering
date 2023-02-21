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

    public void log_login_logout(Connection conn, String email, String status) {
        try {
            String enter_logout_log = "Insert into login_log(email,status) values (?,?)";
            PreparedStatement statement = conn.prepareStatement(enter_logout_log);
            statement.setString(1, email);
            statement.setString(2, status);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error in log_login_logout");
            // e.printStackTrace();
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

}
