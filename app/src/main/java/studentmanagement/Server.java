package studentmanagement;

import java.sql.*;
import java.io.*;
import java.net.*;

public class Server {
    private final String url = "jdbc:postgresql://localhost/academic_management";
    private final String user = "postgres";
    private final String password = "1421";

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

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(4444);
            System.out.println("Server started!");
        } catch (IOException e) {
            System.out.println("Could not listen on port: 4444");
            System.exit(-1);
        }

        Socket clientSocket = null;
        try {
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            System.out.println("Accept failed: 4444");
            System.exit(-1);
        }

        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        clientSocket.getInputStream()));
        String inputLine;
        Server server = new Server();

        while ((inputLine = in.readLine()) != null) {

            String[] request = inputLine.split(" ");
            // login
            if (request[0].equals("login:")) {
                Connection conn = server.connect();
                String email = request[1];
                String password = request[2];
                try {
                    Statement statement = conn.createStatement();
                    ResultSet resultSet = statement
                            .executeQuery("SELECT login_check('" + email + "','" + password + "')");
                    resultSet.next();
                    String result = resultSet.getString(1);
                    out.println(result);

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            // exit
            else if (request[0].equals("exit:")) {
                break;
            }
        }
        out.close();
        in.close();
        clientSocket.close();
        serverSocket.close();
    }

}
