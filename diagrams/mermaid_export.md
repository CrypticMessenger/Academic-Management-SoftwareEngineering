classDiagram
direction BT
class AcademicNorms {
  + AcademicNorms() 
}
class Admin {
  + Admin(String, Connection, String, String) 
  - String name
  + adminOptions(Scanner) String
  - validateStudentGrades(String, String, String) void
  - generateAllTranscripts() String
  - generateValidationReport() String
  + finalize() void
  - generateTranscript(String) String
  - generateStudentTranscriptOptions(Scanner) String
  - triggerEvent(String, String, Integer, Scanner) String
  + viewStudentRecordsOptions(Connection, Scanner) String
  - editCourseCatalog(Scanner) String
  - startNewSession(Scanner) String
   String name
}
class AdminUtils {
  + AdminUtils() 
  + viewCourseRecord(Connection, String, String, String) String
  + getSemCompleted(Connection, String) Integer
  + viewCourseGrade(Connection, String, String, String, String) Boolean
  + get_next_n_sem_ay(Integer, String, String) String[][]
}
class App {
  + App() 
  + in_options(Scanner, Connection) String
  + login(String, String) String
  + main(String[]) void
}
class DatabaseUtils {
  + DatabaseUtils() 
  + connect() Connection
  + checkStudentExist(Connection, String) Boolean
  + getConfigNumber(Connection) Integer
  + executeUpdateQuery(Connection, String) void
  + setConfigNumber(Connection, Integer) void
  + getResultSet(Connection, String) ResultSet?
}
class Person {
  + Person(String, String, String) 
  - String email
  - String sem
  - String ay
  + log_login_logout(Connection, String, String) void
  + editPhoneNumber(Connection, Scanner) String
   String email
   String ay
   String sem
}
class Professor {
  + Professor(String, Connection, String, String) 
  - String name
  - cancelACourse(Scanner) String
  - uploadGradesForCourse(Scanner) String
  - displayFloatedCourses() void
  + professorOptions(Scanner) String
  - validateGradeSubmission() String
  - floatACourse(Scanner) String
  + finalize() void
   String name
}
class ProfessorUtils {
  + ProfessorUtils() 
  + displayCourseCatalog(Connection) void
  + saveCourseGrade(Connection, String, String, String, String, String) void
  + saveCourseRecord(Connection, String, String, String, String) String
  + viewStudentRecordsOptions(Connection, Scanner) String
}
class Student {
  + Student(String, Connection, String, String) 
  - String name
  - String department
  - deregisterCourse(Scanner) String
  + registerCourse(String, String) String
  + finalize() void
  - getPrerequisites(String) ArrayList~String~
  - checkPrereqCondition(String) Boolean
  - registerCourse(Scanner) String
  + studentOptions(Scanner) String
   String name
   Float CGPA
   String department
}
class StudentUtils {
  + StudentUtils() 
  + getPassStatus(String, String, Connection) Boolean
  + displayCourseOfferings(Connection) void
  + viewGrades(Connection, String) String
  + get_prev_2_sem_ay(String, String) String[][]
  + graduationCheck(String, Connection) Boolean
}

Admin  ..>  AdminUtils 
Admin  ..>  DatabaseUtils 
Admin  -->  Person 
Admin  ..>  Student : «create»
Admin  ..>  StudentUtils 
AdminUtils  ..>  DatabaseUtils 
App  ..>  Admin : «create»
App  ..>  DatabaseUtils 
App  ..>  Professor : «create»
App  ..>  Student : «create»
Person  ..>  DatabaseUtils 
Professor  ..>  DatabaseUtils 
Professor  -->  Person 
Professor  ..>  ProfessorUtils 
ProfessorUtils  ..>  AdminUtils 
ProfessorUtils  ..>  DatabaseUtils 
ProfessorUtils  ..>  StudentUtils 
Student  ..>  AcademicNorms 
Student  ..>  DatabaseUtils 
Student  -->  Person 
Student  ..>  StudentUtils 
StudentUtils  ..>  AcademicNorms 
StudentUtils  ..>  DatabaseUtils 
