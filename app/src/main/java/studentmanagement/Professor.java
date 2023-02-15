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

public class Professor extends Person {
    private String name;
    private Connection conn;

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

    // TODO: course catalog should be displayed when choosen Float a course
    public String getName() {
        return this.name;
    }

    public void professorOptions(Scanner scan) {
        System.out.println("Welcome " + getName() + " !");
        String inputLine;
        while (true) {
            System.out.println("1: View Student grades");
            System.out.println("2: Float a course");
            System.out.println("3: Cancel a course");
            System.out.println("4: Upload grades for course");
            System.out.println("5: Validate grade submission");
            System.out.println("6: get student list csv for currently offered course");
            System.out.println("7: Logout");
            System.out.print("Choose: ");
            inputLine = scan.nextLine();
            if (inputLine.equals("5")) {
                // validate grade submission
                validateGradeSubmission();

            } else if (inputLine.equals("1")) {
                // view grades in the courses
                viewStudentRecordsOptions(conn, scan);
            } else if (inputLine.equals("2")) {
                // float a course
                floatACourse(scan);
            } else if (inputLine.equals("3")) {
                // cancel a course
                cancelACourse(scan);

            } else if (inputLine.equals("4")) {
                System.out.println("Enter the course code: ");
                String courseCode = scan.nextLine();
                // upload grades for course
                uploadGradesForCourse(scan, courseCode);

            } else if (inputLine.equals("6")) {
                System.out.println("Enter the course code: ");
                String courseCode = scan.nextLine();
                System.out.println("Enter the path to save the csv file: ");
                String path = scan.nextLine();
                saveCourseRecord(conn, courseCode, path);
            } else if (inputLine.equals("7")) {
                finalize();
                break;
            } else
                System.out.println("Invalid input. Try again.");
        }

    }

    private void uploadGradesForCourse(Scanner scan, String course_Code) {
        Integer config = DatabaseUtils.getConfigNumber(conn);
        if (config != 6) {
            System.out.println("You cannot upload grades now");
            return;
        }

        try {

            System.out.print("Enter .csv file path: ");
            String csvPath = scan.nextLine();
            // read csv file
            BufferedReader br = null;
            String line = "";
            String cvsSplitBy = ",";
            try {
                br = new BufferedReader(new FileReader(csvPath));
                while ((line = br.readLine()) != null) {
                    // use comma as separator
                    String[] row = line.split(cvsSplitBy);
                    String studentEmail = row[0];
                    String courseCode = course_Code;
                    String checkFloatingCondition = "select * from course_offerings where course_code = '"
                            + courseCode
                            + "' and instructor_id = '" + getEmail() + "'";
                    ResultSet rs = DatabaseUtils.getResultSet(conn, checkFloatingCondition);

                    if (!rs.next()) {
                        System.out.println("You cannot upload grades for a course that you are not teaching!");
                        continue;
                    }

                    String gradeValue = row[1];
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
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
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
    }

    private void cancelACourse(Scanner scan) {
        Integer config = DatabaseUtils.getConfigNumber(conn);
        if (config != 2) {
            System.out.println("You cannot cancel a course now");
            return;
        }
        System.out.print("Enter the course code: ");
        String courseCode = scan.nextLine();
        String checkFloatingCondition = "select * from course_offerings where course_code = '" + courseCode
                + "' and instructor_id = '" + getEmail() + "'";
        try {
            ResultSet rs = DatabaseUtils.getResultSet(conn, checkFloatingCondition);
            if (!rs.next()) {
                System.out.println("You cannot cancel a course that you are not teaching!");
                return;
            } else {
                String cancelCourse = "delete from course_offerings where course_code = '" + courseCode
                        + "' and instructor_id = '" + getEmail() + "'";
                DatabaseUtils.executeUpdateQuery(conn, cancelCourse);
                System.out.println("Course cancelled successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void floatACourse(Scanner scan) {
        Integer config = DatabaseUtils.getConfigNumber(conn);
        if (config != 2) {
            System.out.println("You cannot float a course now");
            return;
        }
        System.out.println("Enter the course code");
        String courseCode = scan.nextLine();
        String checkCourseCatalog = "select * from course_catalog where course_code = '" + courseCode
                + "' and sem = '" + getSem() + "' and ay = '" + getAy() + "'";
        try {
            ResultSet rs = DatabaseUtils.getResultSet(conn, checkCourseCatalog);
            if (!rs.next()) {
                System.out.println("Course does not exist in course catalog!");
                return;
            } else {
                System.out.println("Enter minimum cgpa:");
                Integer minCGPA = Integer.parseInt(scan.nextLine());
                String offerCourse = "insert into course_offerings(course_code,instructor_id,cg_constraint) values('"
                        + courseCode + "','" + getEmail() + "'," + minCGPA + ")";
                DatabaseUtils.executeUpdateQuery(conn, offerCourse);
                System.out.println("Course floated successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void validateGradeSubmission() {
        Integer config = DatabaseUtils.getConfigNumber(conn);
        if (config != 8) {
            System.out.println("You cannot validate grade submission now");
            return;
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // TODO: check in teacher if only teacher floating the course can upload or
    // download grade or student sheet
    public void finalize() {
        try {
            log_login_logout(conn, getEmail(), "logout");
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error in Professor destructor");
            e.printStackTrace();
        }
    }
}
