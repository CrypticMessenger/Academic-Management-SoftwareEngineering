package studentmanagement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import studentmanagement.utils.DatabaseUtils;

public class Admin extends Person {
    private String name;
    private Connection conn;

    public Admin(String email, Connection conn, String ay, String sem) {
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
            System.out.println("Error in Admin constructor");
            e.printStackTrace();
        }

    }

    // getter for name
    public String getName() {
        return this.name;
    }

    private void viewCourseGrade(String email, String course_code, String ay, String sem) {
        // somewhat like table format
        String table_name = "s" + email.substring(0, 11);
        try {
            ResultSet resultSet = DatabaseUtils.getResultSet(conn,
                    "select * from " + table_name + " where course='" + course_code + "' and ay='" + ay
                            + "' and sem='" + sem + "' and grade is not null");
            if (resultSet.next()) {
                System.out.println(email + " : " + resultSet.getString(4));
            }

        } catch (SQLException e) {
            System.out.println("Error in viewCourseGrade");
            e.printStackTrace();
        }
    }

    private void viewCourseRecord(String course_code, String ay, String sem) {

        try {
            ResultSet resultSet = DatabaseUtils.getResultSet(conn, "select id from user_auth where roles='s'");
            System.out.println(
                    "-----------Course Code: " + course_code + "---AY: " + ay + "--- SEM: " + sem + "----------------");
            System.out.println("Student email : Grade");
            while (resultSet.next()) {
                String student_email = resultSet.getString(1);
                viewCourseGrade(student_email, course_code, ay, sem);
            }
            System.out.println("--------------------------------------------------------------------");
        } catch (SQLException e) {
            System.out.println("Error in viewCourseRecord");
            e.printStackTrace();
        }
    }

    private void triggerEvent(String event, String state, Integer new_config, Scanner scanner) {
        Integer config = DatabaseUtils.getConfigNumber(conn);
        String question = state.equals("starting") ? "start" : "end";
        if (config == new_config) {
            System.out.println(event + " already " + question + "ed!!");
            return;
        }
        while (true) {
            System.out.println("Are you sure you want to " + question + " " + event + "? (y/n)");
            String response = scanner.nextLine();
            if (response.equals("y")) {
                DatabaseUtils.setConfigNumber(conn, new_config);
                System.out.println(event + " " + question + "ed!!");
                break;
            } else if (response.equals("n")) {
                System.out.println("OPERATION: " + state + "" + event + " aborted!!");
                break;
            } else {
                System.out.println("Invalid input");
            }

        }
    }

    private void generateValidationReport() {
        // generateValidationReport();
        try {
            ResultSet resultSet = DatabaseUtils.getResultSet(conn, "select id from user_auth where roles='s'");
            String ay = getAy();
            String sem = getSem();
            System.out.println(
                    "----------- Validation report---AY: " + ay + "--- SEM: " + sem + "----------------");
            System.out.println("Student email : Course code");
            while (resultSet.next()) {
                String student_email = resultSet.getString(1);
                validateStudentGrades(student_email, ay, sem);
            }
            System.out.println("--------------------------------------------------------------------");
        } catch (SQLException e) {
            System.out.println("Error in generateValidationReport");
            e.printStackTrace();
        }
    }

    private void validateStudentGrades(String email, String ay, String sem) {
        String table_name = "s" + email.substring(0, 11);
        String invalidEntryFilter = "select course from " + table_name + " where grade is null and ay='" + ay
                + "' and sem='" + sem + "'";
        try {
            ResultSet resultSet = DatabaseUtils.getResultSet(conn, invalidEntryFilter);
            while (resultSet.next()) {
                System.out.println(email + " : " + resultSet.getString(1));
                String insertIntoValidator = "Insert into report_validator(course_code,student_id) values('"
                        + resultSet.getString(1) + "','" + email + "')";
                DatabaseUtils.executeUpdateQuery(conn, insertIntoValidator);
            }
        } catch (SQLException e) {
            System.out.println("Error in validateStudentGrades");
            e.printStackTrace();
        }
    }

    // TODO: See how to use interfaces to remove repetition.
    public void adminOptions(Scanner scan) {
        System.out.println("Welcome " + getName() + " !");
        String inputLine;
        while (true) {
            Integer config = DatabaseUtils.getConfigNumber(conn);
            System.out.println("1: Edit course Catalogue");
            System.out.println("2: View student records");
            System.out.println("3: Generate transcripts");
            System.out.println("4: Start grade submission");
            System.out.println("5: End grade submission");
            System.out.println("6: Validate grade submission");
            System.out.println("7: Start a new Academic session");
            System.out.println("8: Allow course enrollment!");
            System.out.println("9: Dis-allow course enrollment!");
            System.out.println("10: allow course float!");
            System.out.println("11: dis-allow course float!");
            System.out.println("12: end current session!");
            System.out.println("13: Logout");
            System.out.print("Choose: ");
            inputLine = scan.nextLine();
            if (inputLine.equals("1")) {
                // TODO: for option 1, ask for actions like go back, edit, new... edit catalog
                // function

            } else if (inputLine.equals("2")) {
                while (true) {
                    System.out.println("1: View records of one course");
                    System.out.println("2: View records of one student");
                    System.out.println("3: go back to menu!");
                    System.out.print("Choose: ");
                    String response = scan.nextLine();
                    if (response.equals("1")) {
                        System.out.print("Enter course code: ");
                        String course_code = scan.nextLine();
                        System.out.print("Enter ay(eg: 2013-14): ");
                        String ay = scan.nextLine();
                        System.out.print("Enter sem(eg: 1 or 2): ");
                        String sem = scan.nextLine();
                        // view for one course record
                        viewCourseRecord(course_code, ay, sem);

                    } else if (response.equals("2")) {
                        // one student DONE
                        System.out.print("Enter student email: ");
                        String student_email = scan.nextLine();
                        viewGrades(conn, student_email);
                    } else if (response.equals("3")) {
                        break;
                    } else {
                        System.out.println("Invalid input");
                    }

                }

            } else if (inputLine.equals("3")) {
                // TODO: generate transcripts

            } else if (inputLine.equals("4")) {
                // start grade submission
                triggerEvent("grade submission", "starting", 6, scan);

            } else if (inputLine.equals("5")) {
                // end grade submission
                triggerEvent("grade submission", "ending", 7, scan);

            } else if (inputLine.equals("6")) {
                // validate grade submission
                if (config == 7) {
                    generateValidationReport();
                    DatabaseUtils.setConfigNumber(conn, 8);
                    System.out.println("Validation report generated!");
                } else {
                    System.out.println("Grade submission not ended yet!");
                }

            } else if (inputLine.equals("7")) {
                triggerEvent("new session", "starting", 1, scan);
                DatabaseUtils.executeUpdateQuery(conn, "Delete from course_offerings");
                System.out.println("Cleared all the course offerings!");
                DatabaseUtils.executeUpdateQuery(conn, "Delete from report_validator");
                System.out.println("Cleared all the invalid reports!");
                String ay = getAy();
                String sem = getSem();
                String[] temp_ay = ay.split("-");
                Integer ay_start = Integer.parseInt(temp_ay[0]);
                Integer ay_end = Integer.parseInt(temp_ay[1]);
                if (sem.equals("1")) {
                    sem = "2";
                } else {
                    sem = "1";
                    ay_start += 1;
                    ay_end += 1;
                }
                ay = ay_start.toString() + "-" + ay_end.toString();
                DatabaseUtils.executeUpdateQuery(conn,
                        "Update current_session set ay = '" + ay + "', sem = '" + sem + "'");
                setAy(ay);
                setSem(sem);

            } else if (inputLine.equals("8")) {
                // allow course enrollment
                triggerEvent("course enrollment", "starting", 4, scan);

            } else if (inputLine.equals("9")) {
                // disable course enrollment
                triggerEvent("course enrollment", "ending", 5, scan);

            } else if (inputLine.equals("10")) {
                // allow course float
                triggerEvent("course float", "starting", 2, scan);

            } else if (inputLine.equals("11")) {
                // dis-allow course float
                triggerEvent("course float", "ending", 3, scan);

            } else if (inputLine.equals("12")) {
                // end current session
                triggerEvent("current session", "ending", 9, scan);

            } else if (inputLine.equals("13")) {
                finalize();
                break;
            } else {
                System.out.println("Invalid input");
            }

        }
    }

    // destroyer for admin
    public void finalize() {
        try {
            log_login_logout(conn, getEmail(), "logout");

            conn.close();
        } catch (SQLException e) {
            System.out.println("Error in Admin destructor");
            e.printStackTrace();
        }
    }
}
