package studentmanagement;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import studentmanagement.utils.DatabaseUtils;

//TODO: refractor ccode
//TODO: print the validation report
//

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
        Integer config = DatabaseUtils.getConfigNumber(conn);
        if (config == 7) {

            try {
                ResultSet resultSet = DatabaseUtils.getResultSet(conn, "select id from user_auth where roles='s'");
                String ay = getAy();
                String sem = getSem();
                System.out.println(
                        "----------- Validation report---AY: " + ay + "--- SEM: " + sem + "----------------");
                if (!resultSet.next()) {
                    System.out.println("Good to go to next semester!!");
                    return;
                } else {
                    System.out.println("Student email : Course code");
                    while (resultSet.next()) {
                        String student_email = resultSet.getString(1);
                        validateStudentGrades(student_email, ay, sem);
                    }
                }
                System.out.println("--------------------------------------------------------------------");
                DatabaseUtils.setConfigNumber(conn, 8);
                System.out.println("Validation report generated!");
            } catch (SQLException e) {
                System.out.println("Error in generateValidationReport");
                e.printStackTrace();
            }

        } else {
            System.out.println("Grade submission not ended yet!");
        }
    }

    // ToDO: refractor teacher part
    // TODO: check if registration is allowed for student, before asking for course
    // code
    // TODO: getAy() doesn't work if admin changes semester, so take care of it.
    // TODO: admin can edit UG curriculum when semester has ended that is config = 9
    // TODO: when admin starts the new semseter, whole UG currilum will
    // automatically insert into course_catalog, so admin will only float the
    // electives
    // TODO: whatif teacher removes course, student enrolled in that course should
    // also be removed
    // TODO: when student want to register a course, PE_for should contain his
    // department
    private void validateStudentGrades(String email, String ay, String sem) {
        String table_name = "s" + email.substring(0, 11);
        String invalidEntryFilter = "select course from " + table_name + " where grade is null and ay='" + ay
                + "' and sem='" + sem + "'";
        try {
            ResultSet resultSet = DatabaseUtils.getResultSet(conn, invalidEntryFilter);
            while (resultSet.next()) {
                System.out.println(email + " : " + resultSet.getString(1));
                String checkEntry = "select * from report_validator where course_code='" + resultSet.getString(1)
                        + "' and student_id='" + email + "'";
                ResultSet resultSet1 = DatabaseUtils.getResultSet(conn, checkEntry);
                if (resultSet1.next()) {
                    continue;
                }
                String insertIntoValidator = "Insert into report_validator(course_code,student_id) values('"
                        + resultSet.getString(1) + "','" + email + "')";
                DatabaseUtils.executeUpdateQuery(conn, insertIntoValidator);
            }
        } catch (SQLException e) {
            System.out.println("Error in validateStudentGrades");
            e.printStackTrace();
        }
    }

    private void generateTranscript(String email) {
        if (!checkStudentExist(conn, email)) {
            System.out.println("Student does not exist");
            return;
        }
        String table_name = "s" + email.substring(0, 11);
        String transcriptFilter = "select sem,ay,course,coalesce(grade,'Ongoing') from " + table_name;
        // print in .txt file
        try {
            ResultSet resultSet = DatabaseUtils.getResultSet(conn, transcriptFilter);
            String _path_dir = "app/src/main/java/studentmanagement/student_transcripts/";
            String filename = _path_dir + table_name + ".txt";
            File file = new File(filename);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("Student email : " + email);
            bw.newLine();
            bw.write("Current AY : " + getAy());
            bw.newLine();
            bw.write("Current SEM : " + getSem());
            bw.newLine();
            bw.write("Time of generation : " + new Date());
            bw.write("---------------------------------------");
            bw.newLine();
            bw.write("SEM | AY | Course code | Grade");
            bw.newLine();
            while (resultSet.next()) {
                String sem = resultSet.getString(1);
                String ay = resultSet.getString(2);
                String course_code = resultSet.getString(3);
                String grade = resultSet.getString(4);
                bw.write(sem + " | " + ay + " | " + course_code + " | " + grade);
                bw.newLine();
            }
            bw.close();
            System.out.println("\tTranscript generated for " + email + "");
        } catch (SQLException | IOException e) {
            System.out.println("Error in generate transcript");
            e.printStackTrace();
        }

    }

    private void generateAllTranscripts() {
        try {
            ResultSet resultSet = DatabaseUtils.getResultSet(conn, "select id from user_auth where roles='s'");
            while (resultSet.next()) {
                String email = resultSet.getString(1);
                generateTranscript(email);
            }
            System.out.println("\n\tAll transcripts generated \n\n");
        } catch (SQLException e) {
            System.out.println("Error in generateAllTranscripts");
            e.printStackTrace();
        }
    }

    private void generateStudentTranscriptOptions(Scanner scan) {
        while (true) {

            System.out.println("1: Generate transcript for one student");
            System.out.println("2: Generate transcript for all students");
            System.out.println("3: go back!");
            System.out.print("Choose: ");
            String response = scan.nextLine();
            if (response.equals("1")) {
                System.out.print("Enter student email: ");
                String student_email = scan.nextLine();
                generateTranscript(student_email);
                break;
            } else if (response.equals("2")) {
                generateAllTranscripts();
                break;
            } else if (response.equals("3")) {
                break;
            } else {
                System.out.println("Invalid input");
            }
        }
    }

    private void startNewSession(Scanner scan) {
        triggerEvent("new session", "starting", 1, scan);
        DatabaseUtils.executeUpdateQuery(conn, "Delete from course_offerings");
        System.out.println("Cleared all the course offerings!");
        DatabaseUtils.executeUpdateQuery(conn, "Delete from report_validator");
        System.out.println("Cleared all the invalid reports!");
        String[][] next_session = get_next_n_sem_ay(1);
        String ay = next_session[0][0];
        String sem = next_session[1][0];
        DatabaseUtils.executeUpdateQuery(conn,
                "Update current_session set ay = '" + ay + "', sem = '" + sem + "'");
        setAy(ay);
        setSem(sem);
        String setupCourseCatalog = "insert into course_catalog(course_code,l,t,p,ay,sem,pre_req,pc_or_pe,pc_for,pe_for,pc_sem,pe_minsem) select course_code,l,t,p,'"
                + getAy() + "'," + getSem() + ",pre_req,'pc',pc_for,pe_for,pc_sem,pe_minsem from ug_curriculum";
        DatabaseUtils.executeUpdateQuery(conn, setupCourseCatalog);
        System.out.println("New session started!");

    }

    private void editCourseCatalog(Scanner scan) {
        // TODO: remover recursive function in Student.java
        Integer config = DatabaseUtils.getConfigNumber(conn);
        if (config != 1) {
            System.out.println("Cannot edit course catalog now!");
            return;
        }
        while (true) {
            // TODO: add course edit option
            System.out.println("1: Add a new course");
            System.out.println("2: Remove a course");
            System.out.println("3: go back!");
            System.out.print("Choose: ");
            String response = scan.nextLine();
            if (response.equals("1")) {
                try {
                    System.out.print("Enter course code: ");
                    String course_code = scan.nextLine();
                    String checkCourseQuery = "select * from course_catalog where course_code = '" + course_code
                            + "' and ay= '" + getAy() + "' and sem = '" + getSem() + "'";
                    ResultSet resultSet = DatabaseUtils.getResultSet(conn, checkCourseQuery);
                    if (resultSet.next()) {
                        System.out.println("Course already exists!");
                        continue;
                    }
                    System.out.print("Enter L: ");
                    Integer l = Integer.parseInt(scan.nextLine());
                    System.out.print("Enter T: ");
                    Integer t = Integer.parseInt(scan.nextLine());
                    System.out.print("Enter P: ");
                    Float p = Float.parseFloat(scan.nextLine());

                    ArrayList<String> pre_req = new ArrayList<String>();
                    System.out.print("Enter course prerequisites: ");
                    while (true) {
                        System.out.println("Enter course code of prerequisite course or enter 'done' to finish");
                        String course_prereq = scan.nextLine();
                        if (course_prereq.equals("done")) {
                            break;
                        }
                        if (pre_req.contains(course_prereq)) {
                            System.out.println("Course already added as a prerequisite!");
                            continue;
                        } else {
                            pre_req.add(course_prereq);
                        }
                    }
                    String pre_req_string = "'" + String.join("','", pre_req) + "'";
                    ArrayList<String> pe_for = new ArrayList<String>();
                    System.out.println("Enter PE for: ");
                    while (true) {
                        System.out.println(
                                "Enter branch code(cs,mm,mc,ch,ce,me) of PE_for course or enter 'done' to finish");
                        String course_pe_for = scan.nextLine();
                        if (course_pe_for.equals("done")) {
                            break;
                        }
                        if (pe_for.contains(course_pe_for)) {
                            System.out.println("Course already added as a PE for!");
                            continue;
                        } else {
                            pe_for.add(course_pe_for);
                        }
                    }
                    String pe_for_string = "'" + String.join("','", pe_for) + "'";
                    Integer pe_minsem = 0;
                    System.out.println("Enter minimum semester for PE: ");
                    String temp = scan.nextLine();
                    if (temp.equals("")) {
                        pe_minsem = 0;
                    } else {
                        pe_minsem = Integer.parseInt(temp);
                    }

                    String query = "insert into course_catalog(course_code,l,t,p,ay,sem,pre_req,pc_or_pe,pe_for,pe_minsem) values('"
                            + course_code
                            + "'," + l
                            + "," + t + "," + p + ",'" + getAy() + "'," + getSem() + ",Array[" + pre_req_string
                            + "],'pe',Array[" + pe_for_string + "]," + pe_minsem + ")";
                    DatabaseUtils.executeUpdateQuery(conn, query);
                    System.out.println("Course added to catalog!");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else if (response.equals("2")) {
                System.out.print("Enter course code: ");
                String course_code = scan.nextLine();
                String query = "delete from course_catalog where course_code = '" + course_code + "' and ay = '"
                        + getAy()
                        + "' and sem = '" + getSem() + "'";
                DatabaseUtils.executeUpdateQuery(conn, query);
                System.out.println("Course removed from catalog!");
            } else if (response.equals("3")) {
                break;
            } else {
                System.out.println("Invalid input");
            }
        }

    }

    // TODO: See how to use interfaces to remove repetition.
    public void adminOptions(Scanner scan) {
        System.out.println("Welcome " + getName() + " !");
        String inputLine;
        while (true) {
            System.out.println("1: Edit course Catalogue");
            System.out.println("2: allow course float!");
            System.out.println("3: dis-allow course float!");
            System.out.println("4: Allow course enrollment!");
            System.out.println("5: Dis-allow course enrollment!");
            System.out.println("6: Start grade submission");
            System.out.println("7: End grade submission");
            System.out.println("8: Validate grade submission");
            System.out.println("9: end current session!");
            System.out.println("10: Start a new Academic session");
            System.out.println("11: View student records");
            System.out.println("12: Generate transcripts");
            System.out.println("13: Logout");
            System.out.print("Choose: ");
            inputLine = scan.nextLine();
            if (inputLine.equals("1")) {
                // edit catalog function
                editCourseCatalog(scan);

            } else if (inputLine.equals("11")) {
                // view student records
                viewStudentRecordsOptions(conn, scan);

            } else if (inputLine.equals("12")) {
                // generate transcripts
                generateStudentTranscriptOptions(scan);

            } else if (inputLine.equals("6")) {
                // start grade submission
                triggerEvent("grade submission", "starting", 6, scan);

            } else if (inputLine.equals("7")) {
                // end grade submission
                triggerEvent("grade submission", "ending", 7, scan);

            } else if (inputLine.equals("8")) {
                // validate grade submission
                generateValidationReport();

            } else if (inputLine.equals("10")) {
                // start new session
                startNewSession(scan);

            } else if (inputLine.equals("2")) {
                // allow course float
                triggerEvent("course float", "starting", 2, scan);

            } else if (inputLine.equals("3")) {
                // dis-allow course float
                triggerEvent("course float", "ending", 3, scan);
                String enrollPCStudents = "select * from course_catalog,user_auth where user_auth.dept = any(course_catalog.pc_for) and user_auth.roles = 's' and course_catalog.ay='"
                        + getAy() + "' and course_catalog.sem=" + getSem() + "";
                System.out.println(enrollPCStudents);

                ResultSet rs1 = DatabaseUtils.getResultSet(conn, enrollPCStudents);
                try {
                    while (rs1.next()) {
                        String course_code = rs1.getString(1);
                        String email = rs1.getString(15);
                        String table_name = "s" + email.substring(0, 11);
                        String pc_sem = rs1.getString(12);
                        if (Integer.parseInt(pc_sem) == getSemCompleted(conn, email) + 1) {
                            Student st = new Student(email, conn, getAy(), getSem());
                            System.out.println("Studentenrolling pc student: " + email + " in course: " + course_code);
                            st.forceRegisterCourse(course_code);
                            // String enrollPCStudent = "insert into " + table_name +
                            // "(sem,ay,course)values('" + getSem()
                            // + "','"
                            // + getAy() + "','" + course_code + "')";
                            // DatabaseUtils.executeUpdateQuery(conn, enrollPCStudent);
                        }
                        System.out.println("sem_comp:" + getSemCompleted(conn, email));
                        System.out.println("pc_sem: " + pc_sem);

                    }
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } else if (inputLine.equals("4")) {
                // allow course enrollment
                triggerEvent("course enrollment", "starting", 4, scan);

            } else if (inputLine.equals("5")) {
                // disable course enrollment
                triggerEvent("course enrollment", "ending", 5, scan);
            } else if (inputLine.equals("9")) {
                System.out.println("Consider checking the validation report before ending the session:");
                System.out.println("1: go back to menu");
                System.out.println("2: end session");

                while (true) {
                    String input = scan.nextLine();
                    if (input.equals("2")) {
                        triggerEvent("current session", "ending", 9, scan);
                        break;
                    } else if (input.equals("1")) {
                        // end current session
                        break;
                    } else {
                        System.out.println("Invalid input");
                    }
                }
                // end current session

            } else if (inputLine.equals("13")) {
                finalize();
                break;
            } else {
                System.out.println("Invalid input");
            }

        }
    }
    // TODO: try to inherit from Person

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
