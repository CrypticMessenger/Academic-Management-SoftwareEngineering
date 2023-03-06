package studentmanagement;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import studentmanagement.utils.DatabaseUtils;

public class App {

    // login method. returns s for student, p for professor, a for admin, and f for
    // failure
    public String login(String email, String password) {
        Connection conn = DatabaseUtils.connect();
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

    // display options: login, exit
    public String in_options(Scanner scan, Connection conn) {
        String res = "";
        try {

            String inputLine;
            String email = "";
            String password = "";
            while (true) {

                System.out.println("1: Login");
                System.out.println("2: Exit");
                System.out.print("Choose: ");
                inputLine = scan.nextLine();
                if (inputLine.equals("2")) {
                    System.out.println("Bye.");
                    res = "exit";
                    return res;
                } else if (inputLine.equals("1")) {

                    System.out.println("Login");
                    System.out.print("email: ");
                    email = scan.nextLine();
                    System.out.print("password: ");
                    password = scan.nextLine();

                    String result = login(email, password);
                    ResultSet resultSet;
                    resultSet = DatabaseUtils.getResultSet(conn, "select * from current_session");
                    resultSet.next();
                    String ay = resultSet.getString(1);
                    String sem = resultSet.getString(2);

                    switch (result) {

                        // case student

                        case "s":
                            Student student = new Student(email, conn, ay, sem);
                            res = student.studentOptions(scan);
                            break;

                        // case professor
                        case "p":
                            Professor professor = new Professor(email, conn, ay, sem);
                            res = professor.professorOptions(scan);
                            break;

                        // case admin
                        case "a":
                            Admin admin = new Admin(email, conn, ay, sem);
                            res = admin.adminOptions(scan);
                            break;

                        // case login failed
                        default:
                            System.out.println("Login Failed! Try again.");
                            break;
                    }

                } else {
                    continue;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return res;
    }

    public static void main(String[] args) throws IOException {

        App app = new App();
        Connection conn = DatabaseUtils.connect();
        Scanner scan = new Scanner(System.in);

        app.in_options(scan, conn);
        scan.close();
    }
}
