package studentmanagement;

import java.sql.*;
import java.util.Scanner;

public class Student extends Person {
    private String name;
    private String department;
    private Connection conn;

    public Student(String email, Connection conn) {
        super(email);
        this.conn = conn;
        String get_name_query = "SELECT name FROM user_auth WHERE id = ?";
        try {
            log_login_logout(conn, email, "login");
            PreparedStatement statement = conn.prepareStatement(get_name_query);
            statement.setString(1, email);
            ResultSet resultSet = statement
                    .executeQuery();
            resultSet.next();
            this.name = (resultSet.getString(1));

        } catch (SQLException e) {
            System.out.println("Error in Student constructor");
            e.printStackTrace();
        }
        this.department = email.substring(4, 5);

    }

    public String getDepartment() {
        return department;
    }

    public String getName() {
        return this.name;
    }

    public void registerCourse() {
        // TODO: register courses
    }

    public void deregisterCourse() {
        // TODO: deregister courses
    }

    public void viewGrades() {
        // TODO: view grades
    }

    public void studentOptions(Scanner scan) {
        System.out.println("Welcome " + this.getName() + " !");
        while (true) {
            System.out.println("1: Register for course");
            System.out.println("2: De-register for course");
            System.out.println("3: View grades and courses");
            System.out.println("4: Logout");
            System.out.print("Choose: ");
            String inputLine = scan.nextLine();
            if (inputLine.equals("4")) {
                finalize();
                break;
            } else if (inputLine.equals("3")) {
                viewGrades();
            } else if (inputLine.equals("2")) {
                deregisterCourse();
            } else if (inputLine.equals("1")) {
                registerCourse();
            } else {
                System.out.println("Invalid input");
            }
        }
    }

    // destructor for student
    public void finalize() {
        try {
            log_login_logout(conn, getEmail(), "logout");
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error in Student destructor");
            e.printStackTrace();
        }
    }

}
