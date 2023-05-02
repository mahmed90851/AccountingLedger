package org.example;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.time.LocalDate;
import java.time.LocalTime;
public class ApplicationInterface {
    public static void main(String[] args) {
        showHomeScreen();
    }
    //The home screen allows the user tos
    public static void showHomeScreen() {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("\n===== ACCOUNTING LEDGER =====\n");
            System.out.println("Please select an option:");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");

            String option = scanner.nextLine();

            switch (option.toUpperCase()) {
                case "D" -> addDeposit();
                case "P" -> makePayment();
                case "L" -> showLedgerScreen();
                case "X" -> exit();
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    public static void addDeposit() {
        Scanner scanner = new Scanner(System.in);
        // get the current date and time
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String date = now.format(dateFormatter);
        String time = now.format(timeFormatter);

        //ask the user for the other parts of the transaction details
        System.out.print("Enter deposit description: ");
        String description = scanner.nextLine();
        System.out.print("Enter deposit source: ");
        String vendor = scanner.nextLine();

        System.out.print("Enter deposit amount: ");
        double amount = Double.parseDouble(scanner.nextLine());

        // this saves the users input into the transactions.csv file.
        try (FileWriter writer = new FileWriter("transactions.csv", true)) {
            writer.write(String.format("%s|%s|%s|%s|+%.2f\n", date, time, description, vendor, amount));
        } catch (IOException e) {
            System.out.println("Error writing transaction to file: " + e.getMessage());
        }

        System.out.println("Deposit saved successfully.");
        showHomeScreen();
    }


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

        // Append payment information to transactions file
        try {
            FileWriter writer = new FileWriter("transactions.csv", true);
            writer.write(String.format("%s|%s|%s|%s|-%.2f\n", date, time, description, vendor, amount));
            writer.close();
            System.out.println("Payment added successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while saving payment information to file.");
            e.printStackTrace();
        }
        showHomeScreen();
    }

    public static void showLedgerScreen() {
        Scanner scanner = new Scanner(System.in);
        Ledger ledger = new Ledger();

        // Loop until user chooses to go back to home screen
        while (true) {
            System.out.println("\n=== LEDGER SCREEN ===\n");
            System.out.println("A) All");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().toUpperCase();
            switch (choice) {
                case "A":
                    System.out.println("\n=== ALL TRANSACTIONS ===\n");
                    Ledger.printAllTransactions();
                    break;
                case "D":
                    System.out.println("=== DEPOSITS ===");
                    Ledger.printDeposits();
                    break;
                case "P":
                    System.out.println("=== PAYMENTS ===");
                    Ledger.printPayments();
                    break;
                case "R":
                    Ledger.showReport();
                    break;
                case "H":
                    showHomeScreen();
                    return; // go back to home screen
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    public static void exit(){
        System.out.println("====Exiting Application====");
        System.exit(0);
    }
}
