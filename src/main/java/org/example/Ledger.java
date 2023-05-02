package org.example;
import java.io.*;
import java.util.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class Ledger {
    private static ArrayList<TransactionDetails> transactions = new ArrayList<>();

    public static void printAllTransactions() {
        transactions = readTransactions();
        /*use the collections class to sort the list in order by newest date.
        the comparator compares the dates of two objects from the transaction details.
        The most recent date which is (t1) and the earlier date (d2).
         */
        Collections.sort(transactions, new Comparator<TransactionDetails>() {
            public int compare(TransactionDetails t1, TransactionDetails t2) {
                return t2.getDate().compareTo(t1.getDate());
            }
        });

        System.out.println("LEDGER\n");
        System.out.printf("%-15s %-15s %-25s %-15s %-10s\n", "DATE", "TIME", "DESCRIPTION", "VENDOR", "AMOUNT");
        System.out.println("------------------------------------------------------------------------------");

        for (TransactionDetails t : transactions) {
            System.out.printf("%-15s %-15s %-25s %-15s %-20.2f\n", t.getDate(), t.getTime(), t.getDescription(),
                    t.getVendor(), t.getAmount());
        }
        ApplicationInterface.showLedgerScreen();
    }


    public static void printDeposits() {
        transactions = readTransactions();
        Collections.sort(transactions, new Comparator<TransactionDetails>() {
            public int compare(TransactionDetails t1, TransactionDetails t2) {
                return t2.getDate().compareTo(t1.getDate());
            }
        });
        System.out.println("DEPOSITS\n");
        System.out.printf("%-15s %-15s %-25s %-15s %-10s\n", "DATE", "TIME", "DESCRIPTION", "VENDOR", "AMOUNT");
        System.out.println("------------------------------------------------------------------------------");

        for (TransactionDetails t : transactions) {
            if (t.getAmount() > 0) {
                System.out.printf("%-15s %-15s %-25s %-15s %-10.2f\n", t.getDate(), t.getTime(), t.getDescription(),
                        t.getVendor(), t.getAmount());
            }
        }
        ApplicationInterface.showLedgerScreen();
    }

    public static void printPayments() {
        transactions = readTransactions();
        Collections.sort(transactions, new Comparator<TransactionDetails>() {
            public int compare(TransactionDetails t1, TransactionDetails t2) {
                return t2.getDate().compareTo(t1.getDate());
            }
        });
        System.out.println("DEPOSITS\n");
        System.out.printf("%-15s %-15s %-25s %-15s %-10s\n", "DATE", "TIME", "DESCRIPTION", "VENDOR", "AMOUNT");
        System.out.println("---------------------------------------------------------------------------------");

        for (TransactionDetails t : transactions) {
            if (t.getAmount() < 0) {
                System.out.printf("%-15s %-15s %-25s %-15s %-10.2f\n", t.getDate(), t.getTime(), t.getDescription(),
                        t.getVendor(), t.getAmount());
            }
        }
        ApplicationInterface.showLedgerScreen();
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


    public static void showReport() {
        ArrayList<TransactionDetails> transactions = new ArrayList<TransactionDetails>();
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n=====Select a report:=====\n");
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
                getPreviousMonth();

                break;
            case 3:
                // Display Year To Date report
                getYearToDate();
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
                ApplicationInterface.showLedgerScreen();
                break;
            default:
                System.out.println("Invalid choice.");
                break;
        }
    }

    public static void getMonthToDate() {
        transactions = readTransactions();
        Collections.sort(transactions, new Comparator<TransactionDetails>() {
            public int compare(TransactionDetails t1, TransactionDetails t2) {
                return t2.getDate().compareTo(t1.getDate());
            }
        });
        LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);

        // this includes transactions from the past 30 days
        List<TransactionDetails> monthToDateTransactions = new ArrayList<>();
        for (TransactionDetails transaction : transactions) {
            if (!transaction.getDate().isBefore(thirtyDaysAgo)) {
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
    public static void getPreviousMonth() {
        transactions = readTransactions();
        Collections.sort(transactions, new Comparator<TransactionDetails>() {
            public int compare(TransactionDetails t1, TransactionDetails t2) {
                return t2.getDate().compareTo(t1.getDate());
            }
        });
        LocalDate today = LocalDate.now();
        LocalDate prevMonth = today.minusMonths(1);

        List<TransactionDetails> prevMonthTransactions = new ArrayList<>();
        for (TransactionDetails transaction : transactions) {
            LocalDate transactionDate = transaction.getDate();
            if (transactionDate.isAfter(prevMonth.withDayOfMonth(1).minusDays(1))
                    && transactionDate.isBefore(today.withDayOfMonth(1))) {
                prevMonthTransactions.add(transaction);
            }
        }

        System.out.printf("%-15s %-15s %-25s %-15s %-10s\n", "DATE", "TIME", "DESCRIPTION", "VENDOR", "AMOUNT");
        System.out.println("------------------------------------------------------------------------------");
        for (TransactionDetails t : prevMonthTransactions) {
            System.out.printf("%-15s %-15s %-25s %-15s %-10.2f\n", t.getDate(), t.getTime(), t.getDescription(),
                    t.getVendor(), t.getAmount());
        }
    }
    public static void getYearToDate() {
        transactions = readTransactions();
        Collections.sort(transactions, new Comparator<TransactionDetails>() {
            public int compare(TransactionDetails t1, TransactionDetails t2) {
                return t2.getDate().compareTo(t1.getDate());
            }
        });
        LocalDate thirtyDaysAgo = LocalDate.now().minusDays(365);

        // this includes transactions from the past 30 days
        List<TransactionDetails> monthToDateTransactions = new ArrayList<>();
        for (TransactionDetails transaction : transactions) {
            if (!transaction.getDate().isBefore(thirtyDaysAgo)) {
                monthToDateTransactions.add(transaction);
            }
        }

        // Print the results
        System.out.println("\n================================Year to Date================================");
        System.out.printf("%-15s %-15s %-25s %-15s %-10s\n", "DATE", "TIME", "DESCRIPTION", "VENDOR", "AMOUNT");
        System.out.println("------------------------------------------------------------------------------");
        for (TransactionDetails t : monthToDateTransactions) {
            System.out.printf("%-15s %-15s %-25s %-15s %-10.2f\n", t.getDate(), t.getTime(), t.getDescription(),
                    t.getVendor(), t.getAmount());
        }
    }

}
