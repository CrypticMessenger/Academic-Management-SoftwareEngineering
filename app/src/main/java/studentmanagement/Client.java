package studentmanagement;

import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        // make an authetication system for users
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your username: ");
        String username = sc.nextLine();
        System.out.println("Enter your password: ");
        String password = sc.nextLine();

        System.out.println(username);
        System.out.println(password);
        sc.close();

    }
}
