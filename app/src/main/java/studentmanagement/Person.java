package studentmanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// an abstract class named Person with name and email
public abstract class Person {
    private String email;
    private String ay;
    private String sem;

    public Person(String email, String ay, String sem) {
        this.email = email;
        this.ay = ay;
        this.sem = sem;
    }

    public String getEmail() {
        return email;
    }

    // getter for ay
    public String getAy() {
        return ay;
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
            e.printStackTrace();
        }
    }

}
