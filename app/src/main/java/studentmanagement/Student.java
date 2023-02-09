package studentmanagement;

import java.sql.*;

public class Student extends Person {
    private String name;
    private String department;
    private Connection conn;

    public Student(String email, Connection conn) {
        super(email);
        this.conn = conn;
        String get_name_query = "SELECT name FROM user_auth WHERE id = ?";
        try {
            log_login_logout(conn, email, "login");
            PreparedStatement statement = conn.prepareStatement(get_name_query);
            statement.setString(1, email);
            ResultSet resultSet = statement
                    .executeQuery();
            resultSet.next();
            this.name = (resultSet.getString(1));

        } catch (SQLException e) {
            System.out.println("Error in Student constructor");
            e.printStackTrace();
        }
        this.department = email.substring(4, 5);

    }

    public String getDepartment() {
        return department;
    }

    public String getName() {
        return this.name;
    }

    public void registerCourse() {
        // TODO: register courses
    }

    public void deregisterCourse() {
        // TODO: deregister courses
    }

    public void viewGrades() {
        // TODO: view grades
    }

    public void studentOptions() {

    }

    // destructor for student
    public void finalize() {
        try {
            log_login_logout(conn, getEmail(), "logout");
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error in Student destructor");
            e.printStackTrace();
        }
    }

}
