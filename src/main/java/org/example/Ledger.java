package org.example;

import java.io.*;
import java.util.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class Ledger {
    private static ArrayList<TransactionDetails> transactions = new ArrayList<>();
    public static void printAllTransactions() {
        showLedger();
    }


    public static void printDeposits() {

    }

    public static void printPayments(){

    }
    public static ArrayList<TransactionDetails> readTransactions() {
        try {
            FileReader fileReader = new FileReader("transactions.csv");
            BufferedReader bufReader = new BufferedReader(fileReader);

            String input;
            while ((input = bufReader.readLine()) != null) {
                if (!input.isEmpty()) { // skip empty lines
                    String[] parts = input.split("\\|");
                    LocalDate date = LocalDate.parse(parts[0]);
                    LocalTime time = LocalTime.parse(parts[1]);
                    String description = parts[2];
                    String vendor = parts[3];
                    double amount = Double.parseDouble(parts[4]);

                    TransactionDetails transaction = new TransactionDetails(date, time, description, vendor, amount);
                    transactions.add(transaction);
                }
            }
        } catch (IOException e) {
            System.out.println("File not found");
            System.exit(0);

        }
        return transactions;
    }

    public static void showLedger(){
        transactions = readTransactions();
            System.out.println("LEDGER\n");
            System.out.printf("%-15s %-15s %-25s %-15s %-10s\n", "DATE", "TIME", "DESCRIPTION", "VENDOR", "AMOUNT");
            System.out.println("------------------------------------------------------------------------------");

            for (TransactionDetails t : transactions) {
                System.out.printf("%-15s %-15s %-25s %-15s %-10.2f\n", t.getDate(), t.getTime(), t.getDescription(),
                        t.getVendor(), t.getAmount());
            }
        }



    public static void showReport(){
        ArrayList<TransactionDetails> transactions = new ArrayList<TransactionDetails>();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select a report:");
        System.out.println("1) Month To Date");
        System.out.println("2) Previous Month");
        System.out.println("3) Year To Date");
        System.out.println("4) Previous Year");
        System.out.println("5) Search by Vendor");
        System.out.println("6) Custom Search");
        System.out.println("0) Back");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1: // Month To Date report
                getMonthToDate();
                break;
            case 2:
                // Display Previous Month report
                break;
            case 3:
                // Display Year To Date report
                break;
            case 4:
                // Display Previous Year report
                break;
            case 5:
                // Prompt user for vendor name and display vendor transactions
                break;
            case 6:
                // Call customSearch method
                break;
            case 0:
                // Return to previous screen
                break;
            default:
                System.out.println("Invalid choice.");
                break;
        }
    }
    public static void getMonthToDate() {
        transactions = readTransactions();
        LocalDate today = LocalDate.now();

        // this includes transactions for the current month
        List<TransactionDetails> monthToDateTransactions = new ArrayList<>();
        for (TransactionDetails transaction : transactions) {
            if (transaction.getDate().getMonthValue() == today.getMonthValue() && transaction.getDate().getYear() == today.getYear()) {
                monthToDateTransactions.add(transaction);
            }
        }

        // Print the results
        System.out.printf("%-15s %-15s %-25s %-15s %-10s\n", "DATE", "TIME", "DESCRIPTION", "VENDOR", "AMOUNT");
        System.out.println("------------------------------------------------------------------------------");
        for (TransactionDetails t : monthToDateTransactions) {
            System.out.printf("%-15s %-15s %-25s %-15s %-10.2f\n", t.getDate(), t.getTime(), t.getDescription(),
                    t.getVendor(), t.getAmount());
        }
    }
}
