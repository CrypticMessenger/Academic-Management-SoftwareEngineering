package studentmanagement.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUtils {
    private static final String url = "jdbc:postgresql://localhost/academic_management";
    private static final String user = "postgres";
    private static final String password = "1421";

    public static Connection connect() {
        Connection conn = null;
        try {
            // Connect to the database
            System.out.println("Connecting...");
            conn = DriverManager.getConnection(url, user, password);
            // Print a message to the console
        } catch (SQLException e) {
            // Print a message to the console
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public static ResultSet getResultSet(Connection conn, String query) {
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            return resultSet;
        } catch (SQLException e) {
            System.out.println("Error in getResultSet");
            return null;
        }
    }

    public static void executeUpdateQuery(Connection conn, String query) {
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println("Error in executeUpdateQuery");
        }
    }

    public static Integer getConfigNumber(Connection conn) {
        try {

            ResultSet resultSet = DatabaseUtils.getResultSet(conn, "select * from config_number");
            resultSet.next();
            Integer config_number = resultSet.getInt(1);
            return config_number;
        } catch (SQLException e) {
            System.out.println("Error in getConfigNumber");
            return -1;
        }
    }

    public static void setConfigNumber(Connection conn, Integer new_config) {
        String setConfigNumberQuery = "update config_number set id = " + new_config;
        DatabaseUtils.executeUpdateQuery(conn, setConfigNumberQuery);

    }

    public static Boolean checkStudentExist(Connection conn, String email) {
        try {
            String checkStudentQuery = "select * from user_auth where id ='" + email + "' and roles='s'";
            ResultSet resultSetCheckStudent = DatabaseUtils.getResultSet(conn, checkStudentQuery);
            return resultSetCheckStudent.next();
        } catch (SQLException e) {
            System.out.println("Error in checkStudentExist");

            return false;
        }
    }

}
