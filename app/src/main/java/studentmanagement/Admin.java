package studentmanagement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Admin extends Person {
    private String name;
    private Connection conn;

    public Admin(String email, Connection conn) {
        super(email);
        this.conn = conn;
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement
                    .executeQuery("SELECT name FROM user_auth WHERE id = '" + this.getEmail() + "'");
            resultSet.next();
            this.name = (resultSet.getString(1));

        } catch (SQLException e) {
            System.out.println("Error in Admin constructor");
            e.printStackTrace();
        }

    }

    // getter for name
    public String getName() {
        return this.name;
    }

    // destroyer for admin
    public void finalize() {
        try {
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error in Admin destructor");
            e.printStackTrace();
        }
    }
}
