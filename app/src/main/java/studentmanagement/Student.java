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
    private Map<String, Integer> grade_to_number = new HashMap<>();
    private Float total_earned_credits;
    private Float current_sem_credits;
    private Float past_2_credits;
    private Float cgpa;

    public Student(String email, Connection conn, String ay, String sem) {
        super(email, ay, sem);
        this.total_earned_credits = 0.0f;
        this.current_sem_credits = 0.0f;
        this.past_2_credits = 0.0f;
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
        setup_grade_to_number();

    }

    public void setup_grade_to_number() {
        grade_to_number.put("A", 10);
        grade_to_number.put("A-", 9);
        grade_to_number.put("B", 8);
        grade_to_number.put("B-", 7);
        grade_to_number.put("C", 6);
        grade_to_number.put("C-", 5);
        grade_to_number.put("D", 4);
        grade_to_number.put("F", 0);
    }

    public String getDepartment() {
        return department;
    }

    public String getName() {
        return this.name;
    }

    private Float getCGPA() {
        try {

            String get_cgpa_query = "select * from " + table_name + " ,course_catalog where " + table_name
                    + ".course = course_catalog.course_code and " + table_name + ".ay = course_catalog.ay and "
                    + table_name + ".sem = course_catalog.sem ";
            ResultSet resultSet = getResultSet(conn, get_cgpa_query);

            Float total_grade_points = 0.0f;

            Float cgpa_score = 0.0f;

            String[][] past_2_ay_sem = get_prev_2_sem_ay();
            String[] past_2_ay = past_2_ay_sem[0];
            String[] past_2_sem = past_2_ay_sem[1];

            while (resultSet.next()) {
                if (resultSet.getString(4) == null) {
                    this.current_sem_credits += resultSet.getFloat(9);
                } else if (!resultSet.getString(4).equals("F")) {
                    this.total_earned_credits += resultSet.getFloat(9);
                    total_grade_points += grade_to_number.get(resultSet.getString(4)) * resultSet.getFloat(9);
                    String sem_temp = resultSet.getString(1);
                    String ay_temp = resultSet.getString(2);

                    if ((ay_temp.equals(past_2_ay[0]) && sem_temp.equals(past_2_sem[0]))
                            || (ay_temp.equals(past_2_ay[1]) && sem_temp.equals(past_2_sem[1]))) {

                        this.past_2_credits += resultSet.getFloat(9);
                    }
                }

            }
            cgpa_score = total_grade_points / this.total_earned_credits;
            return cgpa_score;
        } catch (SQLException e) {
            System.out.println("Error in getCGPA");
            e.printStackTrace();
            return 0.0f;
        }
    }

    public void registerCourse(String course_code) {
        ResultSet resultSet;
        try {
            // fetch current config id, and allow only if config = 4
            Integer config_number = getConfigNumber(conn);
            if (config_number != 4) {
                System.out.println("Registration is not open");
                return;
            }

            // check if course is in course offering
            resultSet = getResultSet(conn, "select * from course_offerings where course_code = '" + course_code + "'");
            if (!resultSet.next()) {
                System.out.println("Course not offered");
                return;
            }

            // check cgpa constraint
            Float cg_constraint = resultSet.getFloat(3);
            this.cgpa = getCGPA();
            if (this.cgpa < cg_constraint) {
                System.out.println("cgpa constraint not met");
                return;
            }

            // check if credit limit allows for registration
            Float avg_past_credits = this.past_2_credits / 2;

            // get credit of requested course
            resultSet = getResultSet(conn,
                    "select * from course_catalog where course_code = '" + course_code + "' and ay = '"
                            + getAy() + "' and sem = '" + getSem() + "'");
            resultSet.next();
            Float requested_course_credits = resultSet.getFloat(5);
            // if avg past credits is less than 12, then credit limit is 12
            Float credit_limit = (1.25f * avg_past_credits) < 12 ? 12 : (1.25f * avg_past_credits);

            if (this.current_sem_credits + requested_course_credits > credit_limit) {
                System.out.println("Credit limit exceeded");
                return;
            }

            // check if student is already registered for the course
            resultSet = getResultSet(conn, "select * from " + table_name + " where course = '" + course_code
                    + "' and ay = '" + getAy() + "' and sem = '" + getSem() + "'");
            if (resultSet.next()) {
                System.out.println("Already registered for course");
                return;
            }

            // TODO: check if student has taken all the prerequisites with no grade as
            // F or null
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
            System.out.println("4: Get Current CGPA");
            System.out.println("5: Logout");
            System.out.print("Choose: ");
            String inputLine = scan.nextLine();
            if (inputLine.equals("5")) {
                finalize();
                break;
            } else if (inputLine.equals("4")) {
                System.out.println("Your CGPA is: " + getCGPA());
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
