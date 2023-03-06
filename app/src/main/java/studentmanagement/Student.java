package studentmanagement;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

import studentmanagement.utils.AcademicNorms;
import studentmanagement.utils.DatabaseUtils;
import studentmanagement.utils.StudentUtils;

public class Student extends Person {
    private String name;
    private String department;
    private Connection conn;
    private String table_name;
    private Float current_sem_credits;
    private Float past_2_credits;
    public Float cgpa;

    // Constructor
    public Student(String email, Connection conn, String ay, String sem) {
        super(email, ay, sem);
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
        this.department = email.substring(4, 6);

    }

    // getter for department
    public String getDepartment() {
        return department;
    }

    // getter for name
    public String getName() {
        return this.name;
    }

    // calculates and returns cgpa
    public Float getCGPA() {
        try {

            String get_cgpa_query = "select * from " + table_name + " ,course_catalog where " + table_name
                    + ".course = course_catalog.course_code and " + table_name + ".ay = course_catalog.ay and "
                    + table_name + ".sem = course_catalog.sem ";
            ResultSet resultSet = DatabaseUtils.getResultSet(conn, get_cgpa_query);

            Float total_grade_points = 0.0f;
            Float total_earned_credits = 0.0f;
            Float current_sem_credits = 0.0f;
            Float past_2_credits = 0.0f;

            Float cgpa_score = 0.0f;

            String[][] past_2_ay_sem = StudentUtils.get_prev_2_sem_ay(getAy(), getSem());
            String[] past_2_ay = past_2_ay_sem[0];
            String[] past_2_sem = past_2_ay_sem[1];

            while (resultSet.next()) {
                if (resultSet.getString(4) == null) {
                    current_sem_credits += resultSet.getFloat(9);
                } else if (!resultSet.getString(4).equals("F")) {
                    total_earned_credits += resultSet.getFloat(9);
                    total_grade_points += AcademicNorms.grade_to_number.get(resultSet.getString(4))
                            * resultSet.getFloat(9);
                    String sem_temp = resultSet.getString(1);
                    String ay_temp = resultSet.getString(2);

                    if ((ay_temp.equals(past_2_ay[0]) && sem_temp.equals(past_2_sem[0]))
                            || (ay_temp.equals(past_2_ay[1]) && sem_temp.equals(past_2_sem[1]))) {

                        past_2_credits += resultSet.getFloat(9);
                    } else {
                        continue;
                    }
                }

            }
            cgpa_score = total_grade_points / total_earned_credits;
            this.past_2_credits = past_2_credits;
            this.current_sem_credits = current_sem_credits;

            return cgpa_score;
        } catch (SQLException e) {
            System.out.println("Error in getCGPA");
            e.printStackTrace();
            return 0.0f;
        }
    }

    /*
     * This code gets the prerequisites for a given course code
     * It does this by getting the result set from the database and then
     * iterating through it to create an array of the prerequisites
     * The prerequisites are then added to the list and returned
     */
    private ArrayList<String> getPrerequisites(String course_code) {

        ArrayList<String> list = new ArrayList<String>();

        try {
            ResultSet resultSet = DatabaseUtils.getResultSet(conn,
                    "select pre_req from course_catalog where course_code = '"
                            + course_code + "' and ay = '" + getAy() + "' and sem = '" + getSem() + "'");
            if (resultSet.next()) {
                Array pre_req = resultSet.getArray(1);
                if (pre_req != null) {
                    Object[] pre_req_array = (Object[]) pre_req.getArray();
                    for (Object element : pre_req_array) {
                        list.add((String) element);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error in getPrerequisites");
        }
        return list;

    }

    /*
     * This code checks the prerequisite condition for a given course code
     * It does this by getting the result set from the database and then
     * iterating through it checking if he/she passed in all the courses
     * returns boolean
     */
    private Boolean checkPrereqCondition(String courseCode) {
        ArrayList<String> preReq = getPrerequisites(courseCode);
        System.out.println(preReq);
        ArrayList<String> coursesTaken = new ArrayList<String>();
        System.out.println(coursesTaken);
        try {
            ResultSet resultSet = DatabaseUtils.getResultSet(conn,
                    "select course from " + table_name + " where grade != 'F' and grade is not null");
            while (resultSet.next()) {
                coursesTaken.add(resultSet.getString(1));
            }
            return coursesTaken.containsAll(preReq);
        } catch (SQLException e) {
            System.out.println("Error in checkPrereqCondition");
            e.printStackTrace();
            return false;
        }
    }

    /*
     * registers course for student
     * checks if registration is open
     * inputs course code
     * checks if course is offered
     * checks if cgpa constraints are met
     * checks if credit limit constraints are met
     * checks if course is already registered
     * checks if prerequisite conditions are met
     * checks if course is offered for the branch
     * return string status pass or fail
     */
    private String registerCourse(Scanner scan) {
        ResultSet resultSet;
        try {
            // fetch current config id, and allow only if config = 4
            Integer config_number = DatabaseUtils.getConfigNumber(conn);
            if (config_number != 4) {
                System.out.println("Registration is not open");
                return "fail";
            }
            System.out.print("Enter course code: ");
            String course_code = scan.nextLine();
            if (!course_code.matches("^[A-Z]{2}\\d{3}$")) {
                System.out.println("Invalid course code");
                return "fail";
            }
            // check if course is in course offering
            resultSet = DatabaseUtils.getResultSet(conn,
                    "select * from course_offerings where course_code = '" + course_code + "'");
            if (!resultSet.next()) {
                System.out.println("Course not offered");
                return "fail";
            }

            // check cgpa constraint
            Float cg_constraint = resultSet.getFloat(3);
            this.cgpa = getCGPA();
            if (this.cgpa < cg_constraint) {
                System.out.println("cgpa constraint not met");
                return "fail";
            }

            // check if credit limit allows for registration
            Float avg_past_credits = this.past_2_credits / 2;

            // get credit of requested course
            resultSet = DatabaseUtils.getResultSet(conn,
                    "select * from course_catalog where course_code = '" + course_code + "' and ay = '"
                            + getAy() + "' and sem = '" + getSem() + "'");
            resultSet.next();
            Float requested_course_credits = resultSet.getFloat(5);
            // if avg past credits is less than 24, then credit limit is 24
            Float minSemCredits = AcademicNorms.minMaxSemCredits;
            Float credit_limit = (1.25f * avg_past_credits) < minSemCredits ? minSemCredits
                    : (1.25f * avg_past_credits);

            if (this.current_sem_credits + requested_course_credits > credit_limit) {
                System.out.println("Credit limit exceeded");
                return "fail";
            }

            // check if student is already registered for the course also checks if this
            // course is PC or not
            resultSet = DatabaseUtils.getResultSet(conn,
                    "select * from " + table_name + " where course = '" + course_code
                            + "' and (grade != 'F' or grade is null )");
            if (resultSet.next()) {
                System.out.println("Already registered or credited the course");
                return "fail";
            }

            // check if student has taken all the prerequisites with no grade as F or null
            if (!checkPrereqCondition(course_code)) {
                System.out.println("Prerequisite condition not met");
                return "fail";
            }

            String query = "select * from course_catalog where course_code = '" + course_code + "' and ay = '"
                    + getAy() + "' and sem = " + getSem() + " and '" + getDepartment() + "'=any(pe_for)";
            resultSet = DatabaseUtils.getResultSet(conn, query);
            if (!resultSet.next()) {
                System.out.println("Course not offered for your branch");
                return "fail";
            }

            // register courses
            String registerCourseQuery = "insert into " + table_name + "(sem,ay,course) values ('" + getSem() + "','"
                    + getAy() + "', '" + course_code + "')";
            Statement st = conn.createStatement();
            st.executeUpdate(registerCourseQuery);
            System.out.println("\n " + course_code + " registered successfully!!\n");
            this.current_sem_credits += requested_course_credits;
            System.out.println("Current credits: " + (this.current_sem_credits));
            System.out.println("Credit limit: " + credit_limit);
            System.out.println("cgpa: " + this.cgpa);
            return "pass";

        } catch (SQLException e) {
            System.out.println("Error in Student registerCourse");
            e.printStackTrace();
        }
        return "error";
    }

    public String registerCourse(String course_code, String code) {
        if (!course_code.matches("^[A-Z]{2}\\d{3}$")) {
            System.out.println("Invalid course code");
            return "fail";
        }
        ResultSet resultSet;
        try {

            // check if course is in course offering
            resultSet = DatabaseUtils.getResultSet(conn,
                    "select * from course_offerings where course_code = '" + course_code + "'");
            if (!resultSet.next()) {
                System.out.println("Course not offered");
                return "fail";
            }

            // check cgpa constraint
            Float cg_constraint = resultSet.getFloat(3);
            this.cgpa = getCGPA();
            if (this.cgpa < cg_constraint) {
                System.out.println("cgpa constraint not met");
                return "fail";
            }

            // check if credit limit allows for registration
            Float avg_past_credits = this.past_2_credits / 2;

            // get credit of requested course
            resultSet = DatabaseUtils.getResultSet(conn,
                    "select * from course_catalog where course_code = '" + course_code + "' and ay = '"
                            + getAy() + "' and sem = '" + getSem() + "'");
            resultSet.next();
            Float requested_course_credits = resultSet.getFloat(5);
            // if avg past credits is less than 12, then credit limit is 12
            Float minSemCredits = AcademicNorms.minMaxSemCredits;
            Float credit_limit = (1.25f * avg_past_credits) < minSemCredits ? minSemCredits
                    : (1.25f * avg_past_credits);

            if (this.current_sem_credits + requested_course_credits > credit_limit) {
                System.out.println("Credit limit exceeded");
                return "fail";
            }

            // check if student is already registered for the course also checks if this
            // course is PC or not
            resultSet = DatabaseUtils.getResultSet(conn,
                    "select * from " + table_name + " where course = '" + course_code
                            + "' and (grade != 'F' or grade is null )");
            if (resultSet.next()) {
                System.out.println("Already registered or credited the course");
                return "fail";
            }

            // check if student has taken all the prerequisites with no grade as F or null
            if (!checkPrereqCondition(course_code)) {
                System.out.println("Prerequisite condition not met");
                return "fail";
            }

            // register courses
            String registerCourseQuery = "insert into " + table_name + "(sem,ay,course) values ('" + getSem() + "','"
                    + getAy() + "', '" + course_code + "')";
            Statement st = conn.createStatement();
            st.executeUpdate(registerCourseQuery);
            System.out.println("\n " + course_code + " registered successfully!!\n");
            this.current_sem_credits += requested_course_credits;
            System.out.println("Current credits: " + (this.current_sem_credits));
            System.out.println("Credit limit: " + credit_limit);
            System.out.println("cgpa: " + this.cgpa);
            return "pass";
        } catch (SQLException e) {
            System.out.println("Error in Student registerCourse");
            e.printStackTrace();
        }
        return "fail";
    }

    /*
     * deregister course for student
     * checks if course drop is allowed
     * checks if student is registered for the course and hasn't already credited it
     * returns status of operation in string
     */
    private String deregisterCourse(Scanner scan) {
        ResultSet resultSet;
        // config number should be 4
        try {
            Integer config_number = DatabaseUtils.getConfigNumber(conn);
            if (config_number != 4) {
                System.out.println("Course drop is not allowed!");
                return "fail";
            }
            System.out.print("Enter course code: ");
            String course_code = scan.nextLine();
            if (!course_code.matches("^[A-Z]{2}\\d{3}$")) {
                System.out.println("Invalid course code");
                return "fail";
            }
            // student course_code should be in running state, hence grade should be null
            resultSet = DatabaseUtils.getResultSet(conn,
                    "select * from " + table_name + " where course = '" + course_code + "' and grade is null and ay = '"
                            + getAy() + "' and sem = '" + getSem() + "'");
            if (!resultSet.next()) {
                System.out.println("Course not registered or already credited!");
                return "fail";
            }
            // deregister courses
            String deregisterCourseQuery = "delete from " + table_name + " where course = '" + course_code
                    + "' and ay = '" + getAy() + "' and sem = '" + getSem() + "' and grade is null";
            Statement statement = conn.createStatement();
            statement.executeUpdate(deregisterCourseQuery);
            System.out.println("\n " + course_code + " deregistered successfully!!\n");
            return "pass";
        } catch (SQLException e) {
            System.out.println("Error in Student deregisterCourse");
            e.printStackTrace();
            return "Error";
        }

    }

    /*
     * option interface for student options
     * returns status of operation in string
     */
    public String studentOptions(Scanner scan) {
        System.out.println("Welcome " + this.name + " !");
        String result = "pass";
        while (true) {
            System.out.println("1: Register for course");
            System.out.println("2: De-register for course");
            System.out.println("3: View grades and courses");
            System.out.println("4: Get Current CGPA");
            System.out.println("5: Student  graduation check");
            System.out.println("6: Edit phone number");
            System.out.println("7: Logout");
            System.out.print("Choose: ");
            String inputLine = scan.nextLine();
            // System.out.println(inputLine);
            if (inputLine.equals("1")) {
                displayCourseOfferings(conn);

                result = registerCourse(scan);
            } else if (inputLine.equals("2")) {
                StudentUtils.viewGrades(conn, getEmail());

                result = deregisterCourse(scan);
            } else if (inputLine.equals("3")) {
                result = StudentUtils.viewGrades(conn, getEmail());
            } else if (inputLine.equals("4")) {
                result = getCGPA().toString();
                System.out.println("Your CGPA is: " + result);
            } else if (inputLine.equals("5")) {
                result = graduationCheck().toString();

                System.out.println("Your CGPA is: " + this.cgpa);
            } else if (inputLine.equals("6")) {
                result = editPhoneNumber(conn, scan);
            } else if (inputLine.equals("7")) {
                finalize();
                break;
            } else {
                System.out.println("Invalid input");
            }

        }
        return result;
    }

    /*
     * checks if student has passed the course everr.
     * accounts for cases like if it has failed first and then passed later
     * returns true if passed, false otherwise
     */
    public Boolean getPassStatus(String course_code) {
        String query = "select * from " + table_name + " where course = '" + course_code
                + "' and grade !='F' and grade is not null";
        ResultSet resultSet = DatabaseUtils.getResultSet(conn, query);
        try {
            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /*
     * checks if student is elgible for graduation or not
     * checks if student has passed all the courses
     * checks if student has completed the minimum credits required for graduation
     * checks if all the B.Tech capstones is done
     * returns true if elgible, false otherwise
     */
    public Boolean graduationCheck() {
        String query = "select s.sem, s.ay,c.c,s.course,s.grade from " + table_name
                + " as s,course_catalog as c where s.course = c.course_code and s.ay = c.ay and s.sem = c.sem";
        ResultSet resultSet = DatabaseUtils.getResultSet(conn, query);
        Integer total_credits = 0;
        try {
            while (resultSet.next()) {
                String course = resultSet.getString(4);
                Integer credits = resultSet.getInt(3);
                if (getPassStatus(course)) {
                    total_credits += credits;
                } else {
                    System.out.println("You have not passed the course: " + course);
                    return false;
                }

            }
            if (total_credits < AcademicNorms.minCreditReqGraduation) {
                System.out.println("You have not completed the minimum credits required for graduation");
                return false;
            }
            System.out.println("You have completed " + total_credits + " credits");
            if (getPassStatus("CP301") && getPassStatus("CP302") && getPassStatus("CP303")) {
                System.out.println("You have completed the BTP requirements");
                return true;
            }
            System.out.println("You have not completed the BTP requirements");
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /*
     * destructor for student.
     * while destructing, logs logout in database.
     */
    public void finalize() {
        try {
            log_login_logout(conn, getEmail(), "logout");
            conn.close();
        } catch (SQLException e) {

        }
    }

}
