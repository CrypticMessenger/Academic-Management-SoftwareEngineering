package studentmanagement;

import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

// TODO: make more object oriented, like making student, admin or teacher class
public class Client {
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

        Client app = new Client();
        String email = "";
        String password = "";
        Boolean login = false;
        Scanner scan = new Scanner(System.in);
        String inputLine;
        while (!login) {

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
                    conn.close();
                    conn = app.connect();
                    switch (result) {
                        case "s":
                            Student student = new Student(email, conn);
                            System.out.println("Welcome " + student.getName() + " !");
                            while (true) {
                                System.out.println("1: Register for course");
                                System.out.println("2: De-register for course");
                                System.out.println("2: View grades and courses");
                                System.out.println("4: Logout");
                                System.out.print("Choose: ");
                                inputLine = scan.nextLine();
                                if (inputLine.equals("4")) {
                                    student.finalize();
                                    login = false;
                                    break;
                                }
                                break;

                            }
                            break;
                        case "p":
                            Professor professor = new Professor(email, conn);
                            System.out.println("Welcome " + professor.getName() + " !");
                            while (true) {
                                System.out.println("1: View grades in the courses");
                                System.out.println("2: Float a course");
                                System.out.println("3: Un-register a course");
                                System.out.println("4: Upload grades for course");
                                System.out.println("5: Logout");
                                System.out.print("Choose: ");
                                inputLine = scan.nextLine();
                                if (inputLine.equals("5")) {
                                    professor.finalize();
                                    login = false;
                                    break;
                                }
                                break;
                            }
                            break;
                        case "a":
                            Admin admin = new Admin(email, conn);
                            System.out.println("Welcome " + admin.getName() + " !");
                            while (true) {
                                System.out.println("1: Edit course Catalogue");
                                System.out.println("2: View student record");
                                System.out.println("3: Generate transcripts");
                                System.out.println("4: Logout");
                                System.out.print("Choose: ");
                                inputLine = scan.nextLine();
                                if (inputLine.equals("4")) {
                                    admin.finalize();
                                    login = false;
                                    break;
                                }
                                break;
                            }
                            break;
                        default:
                            conn.close();
                            System.out.println("Login Failed! Try again.");
                            break;
                    }
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        scan.close();
    }
}
