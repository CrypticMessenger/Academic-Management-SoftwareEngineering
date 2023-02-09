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
            conn = DriverManager.getConnection(url, user, password);
            // Print a message to the console
            System.out.println("Connected to the PostgreSQL server successfully.");
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
                Connection conn = app.connect();
                try {
                    Statement statement = conn.createStatement();
                    ResultSet resultSet = statement
                            .executeQuery("SELECT login_check('" + email + "','" + password + "')");
                    resultSet.next();
                    String result = resultSet.getString(1);
                    switch (result) {
                        case "s":
                            System.out.println("welcome Student!");
                            login = true;
                            while (true) {
                                System.out.println("1: Register for course");
                                System.out.println("2: De-register for course");
                                System.out.println("2: View grades and courses");
                                System.out.println("4: Logout");
                                System.out.print("Choose: ");
                                inputLine = scan.nextLine();
                                break;
                            }
                            break;
                        case "p":
                            System.out.println("welcome Professor!");
                            login = true;
                            while (true) {
                                System.out.println("1: View grades in the courses");
                                System.out.println("2: Float a course");
                                System.out.println("3: Un-register a course");
                                System.out.println("4: Upload grades for course");
                                System.out.println("5: Logout");
                                System.out.print("Choose: ");
                                inputLine = scan.nextLine();
                                break;
                            }
                            break;
                        case "a":
                            System.out.println("welcome Admin!");
                            login = true;
                            while (true) {
                                System.out.println("1: Edit course Catalogue");
                                System.out.println("2: View student record");
                                System.out.println("3: Generate transcripts");
                                System.out.println("4: Logout");
                                System.out.print("Choose: ");
                                inputLine = scan.nextLine();
                                break;
                            }
                            break;
                        default:
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
