package studentmanagement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Professor extends Person {
    private String name;
    private Connection conn;

    public Professor(String email, Connection conn, String ay, String sem) {
        super(email, ay, sem);
        this.conn = conn;
        try {
            log_login_logout(conn, getEmail(), "login");
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

    // TODO: option 5 only available if validation report is available.
    public void professorOptions(Scanner scan) {
        System.out.println("Welcome " + getName() + " !");
        String inputLine;
        while (true) {
            System.out.println("1: View grades in the courses");
            System.out.println("2: Float a course");
            System.out.println("3: Cancel a course");
            System.out.println("4: Upload grades for course");
            System.out.println("5: Validate grade submission");
            System.out.println("6: Logout");
            System.out.print("Choose: ");
            inputLine = scan.nextLine();
            if (inputLine.equals("5")) {
                finalize();
                break;
            }
            break;
        }
    }

    // TODO: try to inherit from Person
    public void finalize() {
        try {
            log_login_logout(conn, getEmail(), "logout");
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error in Professor destructor");
            e.printStackTrace();
        }
    }
}
