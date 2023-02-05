package studentmanagement;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket clientSocket = null;
        try {
            clientSocket = new Socket("localhost", 4444);
        } catch (UnknownHostException e) {
            System.err.println("Unknown host: localhost");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: localhost");
            System.exit(1);
        }

        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        clientSocket.getInputStream()));
        Scanner scan = new Scanner(System.in);
        String inputLine;
        String email = "";
        String password = "";
        Boolean login = false;
        while (!login) {

            System.out.println("1: Login");
            System.out.println("2: Exit");
            System.out.print("Choose: ");
            inputLine = scan.nextLine();
            if (inputLine.equals("2")) {
                System.out.println("Bye.");
                out.println("exit: " + inputLine);
            } else if (inputLine.equals("1")) {

                System.out.println("Login");
                System.out.print("email: ");
                email = scan.nextLine();
                System.out.print("password: ");
                password = scan.nextLine();
                inputLine = "login: " + email + " " + password;
                out.println(inputLine);
                switch (in.readLine()) {
                    case "s":
                        System.out.println("welcome Student!");
                        login = true;
                        break;
                    case "p":
                        System.out.println("welcome Professor!");
                        login = true;
                        break;
                    case "a":
                        System.out.println("welcome Admin!");
                        login = true;
                        break;
                    default:
                        System.out.println("Login Failed!");
                        break;
                }
            }
        }

        out.close();
        in.close();
        clientSocket.close();
        scan.close();
    }
}
