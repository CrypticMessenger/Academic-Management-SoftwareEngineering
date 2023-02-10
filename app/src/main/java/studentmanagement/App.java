package studentmanagement;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

// TODO: make studnt options different
//- make more object oriented, like making student, admin or teacher class
public class App {
    private final String url = "jdbc:postgresql://localhost/academic_management";
    private final String user = "postgres";
    private final String password = "1421";

    public Connection connect() {
        Connection conn = null;
        try {
            // Connect to the database
            System.out.println("Connecting...");
            conn = DriverManager.getConnection(url, user, password);
            // Print a message to the console
        } catch (SQLException e) {
            // Print a message to the console
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public static void main(String[] args) throws IOException {

        App app = new App();
        String email = "";
        String password = "";
        Scanner scan = new Scanner(System.in);
        String inputLine;
        while (true) {

            System.out.println("1: Login");
            System.out.println("2: Exit");
            System.out.print("Choose: ");
            inputLine = scan.nextLine();
            if (inputLine.equals("2")) {
                System.out.println("Bye.");
                System.exit(0);
            } else if (inputLine.equals("1")) {

                System.out.println("Login");
                System.out.print("email: ");
                email = scan.nextLine();
                System.out.print("password: ");
                password = scan.nextLine();
                try {
                    Connection conn = app.connect();
                    Statement statement = conn.createStatement();
                    ResultSet resultSet = statement
                            .executeQuery("SELECT login_check('" + email + "','" + password + "')");
                    resultSet.next();
                    String result = resultSet.getString(1);
                    statement = conn.createStatement();
                    resultSet = statement.executeQuery("select * from current_session");
                    resultSet.next();
                    String ay = resultSet.getString(1);
                    String sem = resultSet.getString(2);
                    // ! remove close(())
                    conn.close();
                    conn = app.connect();
                    switch (result) {

                        // case student
                        case "s":
                            Student student = new Student(email, conn, ay, sem);
                            student.studentOptions(scan);
                            break;

                        // case professor
                        case "p":
                            Professor professor = new Professor(email, conn, ay, sem);
                            professor.professorOptions(scan);
                            break;

                        // case admin
                        case "a":
                            Admin admin = new Admin(email, conn, ay, sem);
                            admin.adminOptions(scan);
                            break;

                        // case login failed
                        default:
                            conn.close();
                            System.out.println("Login Failed! Try again.");
                            break;
                    }
                } catch (SQLException e) {
                    System.out.println("error in login!");
                    e.printStackTrace();
                    break;
                }
            }
        }

        scan.close();
    }
}