package studentmanagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Client {
    private final String url = "jdbc:postgresql://localhost/academic_management";
    private final String user = "postgres";
    private final String password = "1421";
    private String current_user = "";

    public Connection connect() {
        Connection conn = null;
        try {
            // Connect to the database
            conn = DriverManager.getConnection(url, user, password);
            // Print a message to the console
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            // Print a message to the console
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public Boolean getResultSet(Connection conn, String query) {
        Boolean flag = false;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            for (int i = 1; i <= columnsNumber; i++) {
                if (i > 1)
                    System.out.print("  ");
                System.out.print(rsmd.getColumnName(i));
            }
            System.out.println("");
            while (rs.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1)
                        System.out.print("  ");
                    String columnValue = rs.getString(i);
                    System.out.print(columnValue + " ");
                }
                System.out.println("");
                if (!flag) {
                    flag = true;
                }
            }
            return flag;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return flag;
        }

    }

    public static void main(String[] args) {
        Client app = new Client();
        Connection conn = app.connect();
        // make an authetication system for users
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your email id: ");
        String id = sc.nextLine();
        System.out.println("Enter your password: ");
        String pwd = sc.nextLine();

        // TODO: deal with SQL injection attacks
        // TODO: refactor this code
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT login_check('" + id + "','" + pwd + "')");
            resultSet.next();
            String result = resultSet.getString(1);
            switch (result) {
                case "s":
                    System.out.println("Welcome student!");
                case "p":
                    System.out.println("Welcome professor!");
                case "a":
                    System.out.println("Welcome admin!");
                case "f":
                    System.out.println("Login failed!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println(id);
        System.out.println(pwd);
        sc.close();

    }
}
