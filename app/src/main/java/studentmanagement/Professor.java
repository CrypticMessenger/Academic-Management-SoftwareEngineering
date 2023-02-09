package studentmanagement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Professor extends Person {
    private String name;
    private Connection conn;

    public Professor(String email, Connection conn) {
        super(email);
        this.conn = conn;
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement
                    .executeQuery("SELECT name FROM user_auth WHERE id = '" + this.getEmail() + "'");
            resultSet.next();
            this.name = (resultSet.getString(1));

        } catch (SQLException e) {
            System.out.println("Error in Professor constructor");
            e.printStackTrace();
        }
    }

    public String getName() {
        return this.name;
    }

    public void finalize() {
        try {
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error in Professor destructor");
            e.printStackTrace();
        }
    }
}
