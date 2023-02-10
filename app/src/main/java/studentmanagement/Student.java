package studentmanagement;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
// TODO: refractor code and make it readable

public class Student extends Person {
    private String name;
    private String department;
    private Connection conn;
    private String table_name;

    public Student(String email, Connection conn, String ay, String sem) {
        super(email, ay, sem);
        this.table_name = "s" + email.substring(0, 11);
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

    public void registerCourse(String course_code) {
        Statement statement;
        ResultSet resultSet;
        // -- fetch current config id, and allow only if config = 4
        try {
            statement = conn.createStatement();
            resultSet = statement.executeQuery("select * from config_number");
            resultSet.next();
            Integer config_number = resultSet.getInt(1);
            if (config_number != 4) {
                System.out.println("Registration is not open");
                return;
            }
            // -- first check if course is in course offering
            statement = conn.createStatement();
            resultSet = statement
                    .executeQuery("select * from course_offerings where course_code = '" + course_code + "'");
            if (!resultSet.next()) {
                System.out.println("Course not offered");
                return;
            }
            // TODO: check cgpa constraint
            Float cg_constraint = resultSet.getFloat(3);
            Float cgpa = 0.0f;
            Float total_credits = 0.0f;
            String get_cgpa_query = "select * from " + table_name + " ,course_catalog where " + table_name
                    + ".course = course_catalog.course_code and " + table_name + ".ay = course_catalog.ay and "
                    + table_name + ".sem = course_catalog.sem and " + table_name + ".grade is not null and "
                    + table_name + ".grade != 'F'";
            System.out.println(get_cgpa_query);
            statement = conn.createStatement();
            resultSet = statement.executeQuery(get_cgpa_query);
            Map<String, Integer> grade_to_number = new HashMap<>();
            grade_to_number.put("A", 10);
            grade_to_number.put("A-", 9);
            grade_to_number.put("B", 8);
            grade_to_number.put("B-", 7);
            grade_to_number.put("C", 6);
            grade_to_number.put("C-", 5);
            grade_to_number.put("D", 4);
            grade_to_number.put("F", 0);

            while (resultSet.next()) {
                total_credits += resultSet.getFloat(9);
                cgpa += grade_to_number.get(resultSet.getString(4)) * resultSet.getFloat(9);
            }
            cgpa /= total_credits;
            System.out.println("cgpa: " + cgpa + " total_credits: " + total_credits);

            // TODO: secodn check if student is already registered for the course
            // TODO: third check if student has taken all the prerequisites with no grade as
            // F or null
            // TODO: fourth check if credit limit allows for registration
            // TODO: register courses
        } catch (SQLException e) {
            System.out.println("Error in Student registerCourse");
            e.printStackTrace();
        }
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
                // TODO: maybe display to offered courses
                System.out.println("Enter course code: ");
                String course_code = scan.nextLine();
                registerCourse(course_code);
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
