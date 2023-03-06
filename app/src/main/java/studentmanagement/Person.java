package studentmanagement;

import java.sql.*;
import java.util.Scanner;

import studentmanagement.utils.DatabaseUtils;

// an abstract class named Person with email, ay and sem as attributes
public abstract class Person {
    private String email;
    private static String ay;
    private static String sem;

    // constructor
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

    // logs login and logout timestamps in database
    public void log_login_logout(Connection conn, String email, String status) {
        try {
            String enter_logout_log = "Insert into login_log(email,status) values (?,?)";
            PreparedStatement statement = conn.prepareStatement(enter_logout_log);
            statement.setString(1, email);
            statement.setString(2, status);
            statement.executeUpdate();
        } catch (SQLException e) {

        }
    }

    /*
     * Allows student to edit phone number
     * returns status of operation in string
     */
    public String editPhoneNumber(Connection conn, Scanner scan) {
        String result = "pass";
        System.out.print("Enter new phone number: ");
        String new_phone = scan.nextLine();
        if (!new_phone.matches("^\\d{10}$")) {
            System.out.println("Invalid phone number");
            result = "fail";
        } else {

            DatabaseUtils.executeUpdateQuery(conn, "update user_auth set phone = '" + new_phone + "' where id = '"
                    + getEmail() + "'");
            System.out.println("Phone number updated successfully");
        }
        return result;
    }

    // prints course offerings, avaiable for course enrollment for students, and
    // degistration for professors.
    public void displayCourseOfferings(Connection conn) {
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
