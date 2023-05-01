package org.example;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class Ledger {
    public static void printAllTransactions(){

    }

    public static void printDeposits() {
        
    }

    public static void printPayments(){
        
    }
    public static List<TransactionDetails> readTransactionsFromFile() {
        List<TransactionDetails> transactions = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File("transactions.csv"))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split("\\|");
                LocalDate date = LocalDate.parse(parts[0]);
                LocalTime time = LocalTime.parse(parts[1]);
                String description = parts[2];
                String vendor = parts[3];
                double amount = Double.parseDouble(parts[4]);
                TransactionDetails transaction = new TransactionDetails(date, time, description, vendor, amount);
                transactions.add(transaction);
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: ");
        } catch (DateTimeParseException e) {
            System.err.println("Invalid date/time format in file: " );
        } catch (NumberFormatException e) {
            System.err.println("Invalid amount format in file: ");
        }
        return transactions;
    }

    public static void showLedger(ArrayList<TransactionDetails> monthToDate) {
        ArrayList<TransactionDetails> transactions = (ArrayList<TransactionDetails>) readTransactionsFromFile();

        // Sort transactions by date in descending order
        Collections.sort(transactions, Collections.reverseOrder());

        System.out.println("LEDGER\n");
        System.out.printf("%-15s %-15s %-25s %-15s %-10s\n", "DATE", "TIME", "DESCRIPTION", "VENDOR", "AMOUNT");
        System.out.println("------------------------------------------------------------------------------");

        for (TransactionDetails t : transactions) {
            System.out.printf("%-15s %-15s %-25s %-15s %-10.2f\n", t.getDate(), t.getTime(), t.getDescription(),
                    t.getVendor(), t.getAmount());
        }
    }



    public static void showReport(ArrayList<TransactionDetails> transactions) {
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
                LocalDate currentDate = LocalDate.now();
                LocalDate startDate = LocalDate.of(currentDate.getYear(), currentDate.getMonthValue(), 1);

                ArrayList<TransactionDetails> monthToDate = new ArrayList<>();
                for (TransactionDetails t : transactions) {
                    LocalDate transactionDate = t.getDate();
                    if (!transactionDate.isBefore(startDate)) {
                        monthToDate.add(t);
                    }
                }

                showLedger(monthToDate);
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



}
