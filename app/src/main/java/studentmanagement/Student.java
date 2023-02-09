package studentmanagement;

import java.sql.*;

public class Student extends Person {
    private String name;
    private String department;
    private Connection conn;

    public Student(String email, Connection conn) {
        super(email);
        this.conn = conn;
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement
                    .executeQuery("SELECT name FROM user_auth WHERE id = '" + this.getEmail() + "'");
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

    // destructor for student
    public void finalize() {
        try {
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error in Student destructor");
            e.printStackTrace();
        }
    }

}
