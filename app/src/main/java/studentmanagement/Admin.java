package studentmanagement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Admin extends Person {
    private String name;
    private Connection conn;

    public Admin(String email, Connection conn, String ay, String sem) {
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
            System.out.println("Error in Admin constructor");
            e.printStackTrace();
        }

    }

    // getter for name
    public String getName() {
        return this.name;
    }

    // TODO: See how to use interfaces to remove repetition.
    public void adminOptions(Scanner scan) {
        System.out.println("Welcome " + getName() + " !");
        String inputLine;
        while (true) {
            System.out.println("1: Edit course Catalogue");
            System.out.println("2: View student record");
            System.out.println("3: Generate transcripts");
            System.out.println("4: Start grade submission");
            System.out.println("5: End grade submission");
            System.out.println("6: Validate grade submission");
            System.out.println("7: Start a new Academic session");
            System.out.println("8: Logout");
            System.out.print("Choose: ");
            inputLine = scan.nextLine();
            if (inputLine.equals("4")) {
                finalize();
                break;
            }
            break;
        }
    }

    // destroyer for admin
    public void finalize() {
        try {
            log_login_logout(conn, getEmail(), "logout");

            conn.close();
        } catch (SQLException e) {
            System.out.println("Error in Admin destructor");
            e.printStackTrace();
        }
    }
}
