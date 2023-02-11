package studentmanagement.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUtils {

    public static ResultSet getResultSet(Connection conn, String query) {
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            return resultSet;
        } catch (SQLException e) {
            System.out.println("Error in getResultSet");
            e.printStackTrace();
            return null;
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
            e.printStackTrace();
            return -1;
        }
    }

}
