package studentmanagement;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import studentmanagement.utils.DatabaseUtils;
import studentmanagement.utils.ProfessorUtils;

public class Professor extends Person {
    private String name;
    private Connection conn;

    /*
     * Constructor for Professor class
     */
    public Professor(String email, Connection conn, String ay, String sem) {
        super(email, ay, sem);
        this.conn = conn;
        try {
            log_login_logout(conn, getEmail(), "login");
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement
                    .executeQuery("SELECT name FROM user_auth WHERE id = '" + this.getEmail() + "'");
            resultSet.next();
            this.name = (resultSet.getString(1));

        } catch (SQLException e) {
            System.out.println("Error in Professor constructor");
            e.printStackTrace();
        }
    }

    /*
     * name getter
     */
    public String getName() {
        return this.name;
    }

    /*
     * display all courses floated by the professor on console.
     * better accessiblity for the professor
     */
    private void displayFloatedCourses() {
        String extractAllCourses = "select course_code from course_offerings where instructor_id = '"
                + getEmail() + "'";
        ResultSet resultSet = DatabaseUtils.getResultSet(conn, extractAllCourses);
        System.out.println("Following courses are offered by you:");
        try {
            while (resultSet.next()) {
                System.out.println(resultSet.getString(1));
            }
        } catch (SQLException e) {
            System.out.println("Error in displayFloatedCourses");
        }
    }

    /*
     * Interface of professor options available
     */
    public String professorOptions(Scanner scan) {
        System.out.println("Welcome " + getName() + " !");
        String inputLine;
        String result = "pass";
        while (true) {
            System.out.println("1: View Student grades");
            System.out.println("2: Float a course");
            System.out.println("3: Cancel a course");
            System.out.println("4: Upload grades for course");
            System.out.println("5: Validate grade submission");
            System.out.println("6: get student list csv for currently offered course");
            System.out.println("7: Edit Phone number");
            System.out.println("8: Logout");
            System.out.print("Choose: ");
            inputLine = scan.nextLine();
            if (inputLine.equals("5")) {
                // validate grade submission
                result = validateGradeSubmission();

            } else if (inputLine.equals("1")) {
                // view grades in the courses
                result = ProfessorUtils.viewStudentRecordsOptions(conn, scan);

            } else if (inputLine.equals("2")) {

                result = floatACourse(scan);
            } else if (inputLine.equals("3")) {
                // cancel a course
                result = cancelACourse(scan);

            } else if (inputLine.equals("4")) {
                // upload grades for course
                result = uploadGradesForCourse(scan);

            } else if (inputLine.equals("6")) {
                System.out.println("Enter the course code: ");
                String courseCode = scan.nextLine();
                if (!courseCode.matches("^[A-Z]{2}\\d{3}$")) {
                    System.out.println("Invalid course code");
                    result = "fail";
                } else {
                    System.out.println("Enter  path of csv file to save: ");
                    String path = scan.nextLine();
                    System.out.println(path);
                    result = ProfessorUtils.saveCourseRecord(conn, courseCode, path, getAy(), getSem());
                }

            } else if (inputLine.equals("7")) {
                result = editPhoneNumber(conn, scan);
            } else if (inputLine.equals("8")) {
                finalize();
                break;
            } else
                System.out.println("Invalid input. Try again.");
        }
        return result;

    }

    /*
     * upload grades for a course through csv file.
     * checks for if it's allowed to upload grades.
     */
    private String uploadGradesForCourse(Scanner scan) {
        System.out.println("Enter the course code: ");
        String courseCode = scan.nextLine();
        if (!courseCode.matches("^[A-Z]{2}\\d{3}$")) {
            System.out.println("Invalid course code");
            return "fail";
        }
        Integer config = DatabaseUtils.getConfigNumber(conn);
        if (config != 6) {
            System.out.println("You cannot upload grades now");
            return "fail";
        }

        try {

            System.out.print("Enter relative path of .csv file name that is in grade_upload folder: ");
            String csvPath = scan.nextLine();
            // read csv file
            BufferedReader br = null;
            String line = "";
            String cvsSplitBy = ",";

            String checkFloatingCondition = "select * from course_offerings where course_code = '"
                    + courseCode
                    + "' and instructor_id = '" + getEmail() + "'";
            ResultSet rs = DatabaseUtils.getResultSet(conn, checkFloatingCondition);

            if (!rs.next()) {
                System.out.println("You cannot upload grades for a course that you are not teaching!");
                return "fail";
            }
            try {
                br = new BufferedReader(new FileReader(csvPath));
                while ((line = br.readLine()) != null) {
                    // use comma as separator
                    String[] row = line.split(cvsSplitBy);
                    String studentEmail = row[0];

                    String gradeValue = row[1];
                    ResultSet rs_temp = DatabaseUtils.getResultSet(conn, "select * from user_auth where id = '"
                            + studentEmail + "'");
                    if (!rs_temp.next()) {
                        System.out.println("Student " + studentEmail + " does not exist");
                        continue;
                    }

                    String table_name = "s" + studentEmail.substring(0, 11);
                    String validateCourseEnrollment = "select * from " + table_name + " where course= '"
                            + courseCode + "' and ay = '" + getAy() + "' and sem = '" + getSem() + "'";
                    ResultSet rs2 = DatabaseUtils.getResultSet(conn, validateCourseEnrollment);
                    if (!rs2.next()) {
                        System.out.println(
                                "Student " + studentEmail + " is not enrolled in course " + courseCode);
                        continue;
                    }
                    String uploadGrade = "update " + table_name + " set grade = '" + gradeValue
                            + "' where course = '" + courseCode + "' and ay = '" + getAy() + "' and sem = '"
                            + getSem() + "'";
                    DatabaseUtils.executeUpdateQuery(conn, uploadGrade);
                    String updateReportValidator = "delete from report_validator where course_code = '"
                            + courseCode + "' and student_id = '" + studentEmail + "'";
                    DatabaseUtils.executeUpdateQuery(conn, updateReportValidator);
                }
                System.out.println("Grades uploaded successfully!");
                return "pass";
            } catch (FileNotFoundException e) {
                System.out.println("File not found");
                return "fail";
            } catch (IOException e) {
                System.out.println("Error reading file");
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "fail";
    }

    /*
     * Cancel a course
     * can only cancel if it's allowed
     * you have floated the course and want to cancel it
     */
    private String cancelACourse(Scanner scan) {
        Integer config = DatabaseUtils.getConfigNumber(conn);
        if (config != 2) {
            System.out.println("You cannot cancel a course now");
            return "fail";
        }
        displayFloatedCourses();
        System.out.print("Enter the course code: ");
        String courseCode = scan.nextLine();
        String checkFloatingCondition = "select * from course_offerings where course_code = '" + courseCode
                + "' and instructor_id = '" + getEmail() + "'";
        try {
            ResultSet rs = DatabaseUtils.getResultSet(conn, checkFloatingCondition);
            if (!rs.next()) {
                System.out.println("You cannot cancel a course that you are not teaching!");
                return "fail";
            } else {
                String cancelCourse = "delete from course_offerings where course_code = '" + courseCode
                        + "' and instructor_id = '" + getEmail() + "'";
                DatabaseUtils.executeUpdateQuery(conn, cancelCourse);
                System.out.println("Course cancelled successfully!");
                return "pass";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "fail";
    }

    /*
     * Float a course
     */
    private String floatACourse(Scanner scan) {
        Integer config = DatabaseUtils.getConfigNumber(conn);
        if (config != 2) {
            System.out.println("You cannot float a course now");
            return "error:float_not_allowed";
        }
        ProfessorUtils.displayCourseCatalog(conn);
        System.out.println("Enter the course code");
        String courseCode = scan.nextLine();

        String checkCourseCatalog = "select * from course_catalog where course_code = '" + courseCode
                + "' and sem = '" + getSem() + "' and ay = '" + getAy() + "'";
        try {
            ResultSet rs = DatabaseUtils.getResultSet(conn, checkCourseCatalog);
            if (!rs.next()) {
                System.out.println("Course does not exist in course catalog!");
                return "error:course_not_in_catalog";
            } else {
                System.out.println("Enter minimum cgpa:");
                double minCGPA = Double.parseDouble(scan.nextLine());
                String offerCourse = "insert into course_offerings(course_code,instructor_id,cg_constraint) values('"
                        + courseCode + "','" + getEmail() + "'," + minCGPA + ")";
                DatabaseUtils.executeUpdateQuery(conn, offerCourse);
                System.out.println("Course floated successfully!");
                return "success";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "fail";
    }

    /*
     * Validate grade submission
     * generates a validation report that says after grade submission, whose grades
     * are still not uploaded.
     */
    private String validateGradeSubmission() {
        Integer config = DatabaseUtils.getConfigNumber(conn);
        if (config != 8) {
            System.out.println("You cannot validate grade submission now");
            return "fail";
        }
        String extractAllCourses = "select course_code from course_offerings where instructor_id = '"
                + getEmail() + "'";
        try {
            ResultSet rs = DatabaseUtils.getResultSet(conn, extractAllCourses);
            System.out.println("Course Code | Student Email");
            while (rs.next()) {
                String courseCode = rs.getString(1);
                String extractAllInvalideGrades = "select * from report_validator where course_code = '"
                        + courseCode + "'";
                ResultSet rs2 = DatabaseUtils.getResultSet(conn, extractAllInvalideGrades);
                while (rs2.next()) {
                    System.out.println(rs2.getString(1) + " | " + rs2.getString(2));

                }
            }
            return "pass";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "fail";
    }

    public void finalize() {
        try {
            log_login_logout(conn, getEmail(), "logout");
            conn.close();
        } catch (SQLException e) {

        }
    }
}
