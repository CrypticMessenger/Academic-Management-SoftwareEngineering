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

    public String getDepartment() {
        return department;
    }

    public String getName() {
        return this.name;
    }

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
            e.printStackTrace();
        }
        return list;

    }

    private Boolean checkPrereqCondition(String course_code) {
        ArrayList<String> pre_req = getPrerequisites(course_code);
        System.out.println(pre_req);
        ArrayList<String> courses_taken = new ArrayList<String>();
        System.out.println(courses_taken);
        try {
            ResultSet resultSet = DatabaseUtils.getResultSet(conn,
                    "select course from " + table_name + " where grade != 'F' and grade is not null");
            while (resultSet.next()) {
                courses_taken.add(resultSet.getString(1));
            }
            return courses_taken.containsAll(pre_req);
        } catch (SQLException e) {
            System.out.println("Error in checkPrereqCondition");
            e.printStackTrace();
            return false;
        }
    }

    public String registerCourse(String course_code, Scanner scan) {
        ResultSet resultSet;
        try {
            // fetch current config id, and allow only if config = 4
            Integer config_number = DatabaseUtils.getConfigNumber(conn);
            if (config_number != 4) {
                System.out.println("Registration is not open");
                return "fail:registration_not_open";
            }

            // check if course is in course offering
            resultSet = DatabaseUtils.getResultSet(conn,
                    "select * from course_offerings where course_code = '" + course_code + "'");
            if (!resultSet.next()) {
                System.out.println("Course not offered");
                return "fail:course_not_offered";
            }

            // check cgpa constraint
            Float cg_constraint = resultSet.getFloat(3);
            this.cgpa = getCGPA();
            if (this.cgpa < cg_constraint) {
                System.out.println("cgpa constraint not met");
                return "fail:cgpa_constraint_not_met";
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
                return "fail:credit_limit_exceeded";
            }

            // check if student is already registered for the course also checks if this
            // course is PC or not
            resultSet = DatabaseUtils.getResultSet(conn,
                    "select * from " + table_name + " where course = '" + course_code
                            + "' and (grade != 'F' or grade is null )");
            if (resultSet.next()) {
                System.out.println("Already registered or credited the course");
                return "fail:already_registered_or_credited";
            }

            // check if student has taken all the prerequisites with no grade as F or null
            if (!checkPrereqCondition(course_code)) {
                System.out.println("Prerequisite condition not met");
                return "fail:prerequisite_condition_not_met";
            }

            String query = "select * from course_catalog where course_code = '" + course_code + "' and ay = '"
                    + getAy() + "' and sem = " + getSem() + " and '" + getDepartment() + "'=any(pe_for)";
            resultSet = DatabaseUtils.getResultSet(conn, query);
            if (!resultSet.next()) {
                System.out.println("Course not offered for your branch");
                return "fail:course_not_offered_for_your_branch";
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
            return "success";

        } catch (SQLException e) {
            System.out.println("Error in Student registerCourse");
            e.printStackTrace();
        }
        return "error:error_in_register_course";
    }

    public void registerCourse(String course_code, String code) {
        ResultSet resultSet;
        try {

            // check if course is in course offering
            resultSet = DatabaseUtils.getResultSet(conn,
                    "select * from course_offerings where course_code = '" + course_code + "'");
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
                return;
            }

            // check if student is already registered for the course also checks if this
            // course is PC or not
            resultSet = DatabaseUtils.getResultSet(conn,
                    "select * from " + table_name + " where course = '" + course_code
                            + "' and (grade != 'F' or grade is null )");
            if (resultSet.next()) {
                System.out.println("Already registered or credited the course");
                return;
            }

            // check if student has taken all the prerequisites with no grade as F or null
            if (!checkPrereqCondition(course_code)) {
                System.out.println("Prerequisite condition not met");
                return;
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

        } catch (SQLException e) {
            System.out.println("Error in Student registerCourse");
            e.printStackTrace();
        }
    }

    public String deregisterCourse(String course_code) {
        ResultSet resultSet;
        // config number should be 4
        try {
            Integer config_number = DatabaseUtils.getConfigNumber(conn);
            if (config_number != 4) {
                System.out.println("Course drop is not allowed!");
                return "fail:not_allowed";
            }
            // student course_code should be in running state, hence grade should be null
            resultSet = DatabaseUtils.getResultSet(conn,
                    "select * from " + table_name + " where course = '" + course_code + "' and grade is null and ay = '"
                            + getAy() + "' and sem = '" + getSem() + "'");
            if (!resultSet.next()) {
                System.out.println("Course not registered or already credited!");
                return "fail:not_registered_or_credited";
            }
            // deregister courses
            String deregisterCourseQuery = "delete from " + table_name + " where course = '" + course_code
                    + "' and ay = '" + getAy() + "' and sem = '" + getSem() + "' and grade is null";
            Statement statement = conn.createStatement();
            statement.executeUpdate(deregisterCourseQuery);
            System.out.println("\n " + course_code + " deregistered successfully!!\n");
            return "Success";
        } catch (SQLException e) {
            System.out.println("Error in Student deregisterCourse");
            e.printStackTrace();
        }
        return "Error:error_in_deregister_course";

    }

    public void action(String inputLine, Scanner scan) {
        String course_code;
        switch (inputLine) {
            case "1":
                displayCourseCatalog(conn);
                System.out.print("Enter course code: ");
                course_code = scan.nextLine();
                registerCourse(course_code, scan);
                break;
            case "2":
                StudentUtils.viewGrades(conn, getEmail());
                System.out.print("Enter course code: ");
                course_code = scan.nextLine();
                deregisterCourse(course_code);
                break;
            case "3":
                StudentUtils.viewGrades(conn, getEmail());
                break;
            case "4":
                System.out.println("Your CGPA is: " + getCGPA());
                break;
            case "5":
                graduationCheck();
                System.out.println("Your CGPA is: " + getCGPA());
                break;
            case "6":
                finalize();
                break;
            default:
                System.out.println("Invalid input");
        }
    }

    // TODO: graduation check for admin too
    public void studentOptions(Scanner scan) {
        System.out.println("Welcome " + this.name + " !");
        while (true) {
            System.out.println("1: Register for course");
            System.out.println("2: De-register for course");
            System.out.println("3: View grades and courses");
            System.out.println("4: Get Current CGPA");
            System.out.println("5: Student  graduation check");
            System.out.println("6: Logout");
            System.out.print("Choose: ");
            String inputLine = scan.nextLine();
            action(inputLine, scan);

        }
    }

    private Boolean getPassStatus(String course_code) {
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

    private Boolean graduationCheck() {
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
            // TODO: download.csv is in appending mode

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
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
