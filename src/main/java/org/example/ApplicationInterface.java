package org.example;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class ApplicationInterface {
    public static void main(String[] args) {
        login();
        showHomeScreen();
    }

    // the login method lets the user enter a username and password
    // if the user's input matches the username and password from isValidAmount, it would continue to home screen
    public static void login() {
        Scanner scanner = new Scanner(System.in);

        String username;
        String password;

        do {
            System.out.println("Enter your username:");
            username = scanner.nextLine();

            System.out.println("Enter your password:");
            password = scanner.nextLine();

            if (!isValidAccount(username, password)) {
                System.err.println("That is incorrect. Please try again.");
            }
        } while (!isValidAccount(username, password));

        System.out.println("Login successful.");
        // continue with the rest of the program
    }

    public static boolean isValidAccount(String username, String password) {
        // check if the username and password are valid
        // return true if they are, false otherwise
        // example:
        return username.equals("bestprogrammer") && password.equals("password");
    }

    //The home screen allows the user to select some options for the application
    public static void showHomeScreen() {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        //this will loop until user wants to exit the application

        while (!exit) {
            System.out.println("\n===== ACCOUNTING LEDGER =====\n" +
                    "\nPlease select an option:\n" +
                    "[D] Add Deposit\n" +
                    "[P] Make Payment (Debit)\n" +
                    "[L] Ledger\n" +
                    "[X] Exit");


            String option = scanner.nextLine();

            switch (option.toUpperCase()) {
                case "D" -> addDeposit();
                case "P" -> makePayment();
                case "L" -> showLedgerScreen();
                case "X" -> exit();
                default -> System.err.println("Invalid option. Please try again.");
            }
        }
    }
    /* this method asks for the users input information for a deposit made at any current
           time by the user. it writes this information onto transactions.csv
        */
    public static void addDeposit() {
        Scanner scanner = new Scanner(System.in);

        // this gets the current date and time and uses it in the format we want for our transaction.csv file
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String date = now.format(dateFormatter);
        String time = now.format(timeFormatter);

        try (FileWriter writer = new FileWriter("transactions.csv", true)) {
        //asks the user for the other parts of the transaction details
        System.out.print("Enter deposit description: ");
        String description = scanner.nextLine();
        System.out.print("Enter deposit source: ");
        String vendor = scanner.nextLine();

        System.out.print("Enter deposit amount: ");
        double amount = Double.parseDouble(scanner.nextLine());

        // this saves the users input into the transactions.csv file with a file writer

            writer.write(String.format("%s|%s|%s|%s|%.2f\n", date, time, description, vendor, amount));
        } catch (IOException e) {
            System.err.println("Error writing transaction to file: ");
        } catch (NumberFormatException e){
            System.err.println("\nPlease input a number for 'amount' ");
            addDeposit();
            return;
        }

        System.out.println("Deposit saved successfully.");
        showHomeScreen();
    }
    /* this method asks for the users input information for a payment made at any current
       time by the user. it writes this information onto transactions.csv
    */
    public static void makePayment() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter payment information:");
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String date = now.format(dateFormatter);
        String time = now.format(timeFormatter);

        System.out.print("Description: ");
        String description = scanner.next();
        System.out.print("Vendor: ");
        String vendor = scanner.next();
        System.out.print("Amount (negative value): ");
        double amount = scanner.nextDouble();

        try {FileWriter writer = new FileWriter("transactions.csv", true);
            writer.write(String.format("%s|%s|%s|%s|-%.2f\n", date, time, description, vendor, amount));
            writer.close();
            System.out.println("Payment added successfully.");
        } catch (IOException e) {
            System.err.println("An error occurred while saving payment information to file.");
            return;
        }
        showHomeScreen();
    }
    //this will display a new screen that allows the user to select options for the ledger
    public static void showLedgerScreen() {
        Scanner scanner = new Scanner(System.in);
        Ledger ledger = new Ledger();

        // This will loop until user chooses to go back to home screen
        while (true) {
            System.out.println(
                    "\n===== LEDGER SCREEN =====\n" +
                            "\nPlease select an option: \n"+
                    "[A] All\n" +
                    "[D] Deposits\n" +
                    "[P] Payments\n" +
                    "[R] Reports\n" +
                    "[H] Home\n" );


            String choice = scanner.nextLine().toUpperCase();
            switch (choice) {
                // will display all transactions
                case "A":

                    Ledger.printAllTransactions();
                    break;
                case "D":
                    //will display all deposits

                    Ledger.printDeposits();
                    break;
                case "P":
                    //will display all payments

                    Ledger.printPayments();
                    break;
                case "R":
                    // this will open the reports screen for more options
                    Ledger.showReport();
                    break;
                case "H":
                    // returns user to home screen
                    showHomeScreen();
                    return;
                default:
                    System.err.println("Invalid choice. Please try again.");
            }
        }
    }

    //only purpose is to exit the program
    public static void exit(){
        System.out.println("===== EXITING APPLICATION =====");
        System.exit(0);
    }
}
