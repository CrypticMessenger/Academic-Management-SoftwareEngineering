package studentmanagement;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import studentmanagement.utils.DatabaseUtils;

// TODO:TEACHER when floating course, course catalog should be printed.
// TODO:TEACHER when cancelling course, floating courses by the teacher should be printed.
//TODO:TEACHER maybe log whichever grades are uploaded by the teacher
//TODO: TEACHER, do you really need to ask for sem and ay when viewign student records?
//TODO: TEACHER when validating course, make sure you print, no student avaiable if it's empty
//TODO: refractor code

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

    public String login(String email, String password) {
        App app = new App();
        Connection conn = app.connect();
        String result = "";
        try {
            ResultSet resultSet = DatabaseUtils.getResultSet(conn,
                    "SELECT login_check('" + email + "','" + password + "')");
            if (resultSet.next()) {
                result = resultSet.getString(1);
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return result;
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
                    String result = app.login(email, password);
                    ResultSet resultSet;
                    resultSet = DatabaseUtils.getResultSet(conn, "select * from current_session");
                    resultSet.next();
                    String ay = resultSet.getString(1);
                    String sem = resultSet.getString(2);

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
