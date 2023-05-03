package org.example;
import java.io.*;
import java.util.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class Ledger {
    public static Scanner scanner = new Scanner(System.in);

    private static ArrayList<TransactionDetails> transactions = readTransactions();

    public static void printHeader() {
        System.out.printf("%-15s %-15s %-25s %-15s %-10s\n", "DATE", "TIME", "DESCRIPTION", "VENDOR", "AMOUNT");
        System.out.println("-------------------------------------------------------------------------------");
    }

    public static ArrayList<TransactionDetails> readTransactions() {

        ArrayList<TransactionDetails> transactions = new ArrayList<>();
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
        /*use the collections class to sort the list in order by newest date.
         */
        Collections.sort(transactions, Comparator.comparing(TransactionDetails::getDate)
                .thenComparing(TransactionDetails::getTime)
                .reversed());
        return transactions;
    }

    public static void printAllTransactions() {
        System.out.println("============================== ALL TRANSACTIONS =============================\n");
        printHeader();

        for (TransactionDetails t : transactions) {
            System.out.printf("%-15s %-15s %-25s %-15s %-20.2f\n", t.getDate(), t.getTime(), t.getDescription(),
                    t.getVendor(), t.getAmount());
        }
        ApplicationInterface.showLedgerScreen();
    }

    public static void printDeposits() {
        System.out.println("============================== ALL DEPOSITS ============================== \n");
        printHeader();

        for (TransactionDetails t : transactions) {
            if (t.getAmount() > 0) {
                System.out.printf("%-15s %-15s %-25s %-15s %-10.2f\n", t.getDate(), t.getTime(), t.getDescription(),
                        t.getVendor(), t.getAmount());
            }
        }
        ApplicationInterface.showLedgerScreen();
    }

    public static void printPayments() {
        System.out.println("================================ ALL PAYMENTS ===============================\n");
        printHeader();

        for (TransactionDetails t : transactions) {
            if (t.getAmount() < 0) {
                System.out.printf("%-15s %-15s %-25s %-15s %-10.2f\n", t.getDate(), t.getTime(), t.getDescription(),
                        t.getVendor(), t.getAmount());
            }
        }
        ApplicationInterface.showLedgerScreen();
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
                getPreviousYear();
                break;
            case 5:
                // ask user for vendor name and display vendor transactions
                searchByVendor();
                break;
            case 6:
                // Call customSearch method
                customSearch();
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
        LocalDate today = LocalDate.now();

        System.out.println("\n================================ Month to Date ================================");
        printHeader();

        for (TransactionDetails transaction : transactions) {
            LocalDate transactionDate = transaction.getDate();
            if (transactionDate.getMonth() == today.getMonth() && transactionDate.getYear() == today.getYear()) {
                System.out.printf("%-15s %-15s %-25s %-15s %-10.2f\n", transaction.getDate(), transaction.getTime(), transaction.getDescription(),
                        transaction.getVendor(), transaction.getAmount());
            }
        }
    }

    public static void getPreviousMonth() {
        /*this is used so that whatever the current month is, it's value is -1 which would
         be the month before the current month
         */
        LocalDate today = LocalDate.now();
        int prevMonthValue = today.minusMonths(1).getMonthValue();

        System.out.println("\n================================ Previous Month ================================");
        printHeader();

        for (TransactionDetails transaction : transactions) {
            LocalDate transactionDate = transaction.getDate();
            if (transactionDate.getMonthValue() == prevMonthValue && transactionDate.getYear() == today.getYear()) {
                System.out.printf("%-15s %-15s %-25s %-15s %-10.2f\n", transaction.getDate(), transaction.getTime(), transaction.getDescription(),
                        transaction.getVendor(), transaction.getAmount());
            }
        }
    }

    public static void getYearToDate() {
        LocalDate today = LocalDate.now();

        System.out.println("\n================================ Year to Date ================================");
        printHeader();

        for (TransactionDetails transaction : transactions) {
            LocalDate transactionDate = transaction.getDate();
            if (transactionDate.getYear() == today.getYear()) {
                System.out.printf("%-15s %-15s %-25s %-15s %-10.2f\n", transaction.getDate(), transaction.getTime(), transaction.getDescription(),
                        transaction.getVendor(), transaction.getAmount());
            }
        }
    }


    public static void getPreviousYear() {
        LocalDate today = LocalDate.now();

        System.out.println("\n================================ Previous Year ================================");
        printHeader();

        for (TransactionDetails transaction : transactions) {
            LocalDate transactionDate = transaction.getDate();
            if (transactionDate.getYear() == today.getYear() - 1) {
                System.out.printf("%-15s %-15s %-25s %-15s %-10.2f\n", transaction.getDate(), transaction.getTime(), transaction.getDescription(),
                        transaction.getVendor(), transaction.getAmount());
            }
        }
    }

    public static void searchByVendor() {

        System.out.print("Enter vendor name: ");
        String vendorName = scanner.nextLine();

        System.out.println("\n==================== Transactions for " + vendorName + " ====================");
        printHeader();

        for (TransactionDetails transaction : transactions) {
            if (transaction.getVendor().equalsIgnoreCase(vendorName)) {
                System.out.printf("%-15s %-25s %-15s %-10.2f\n", transaction.getDate(), transaction.getDescription(),
                        transaction.getVendor(), transaction.getAmount());
            } else if (transactions.isEmpty()) {
                System.out.println("No transactions found for vendor: " + vendorName);
            }
        }
    }

    public static void customSearch() {
        /* Prompt the user for search values
           if the user wants to skip the search value they can press enter.
         */
        System.out.print("Enter start date (yyyy-mm-dd, or press Enter to skip): ");
        String startDateInput = scanner.nextLine().trim();
        LocalDate startDate;
        if (startDateInput.isEmpty()) {
            startDate = null;
        } else {
            startDate = LocalDate.parse(startDateInput);
        }

        System.out.print("Enter end date (yyyy-mm-dd, or press Enter to skip): ");
        String endDateInput = scanner.nextLine().trim();
        LocalDate endDate;
        if (endDateInput.isEmpty()) {
            endDate = null;
        } else {
            endDate = LocalDate.parse(endDateInput);
        }


        System.out.print("Enter description (or press Enter to skip): ");
        String description = scanner.nextLine().trim();

        System.out.print("Enter vendor (or press Enter to skip): ");
        String vendor = scanner.nextLine().trim();

        System.out.print("Enter amount (or press Enter to skip): ");
        String amountInput = scanner.nextLine().trim();
        double amount;
        if (amountInput.isEmpty()) {
            amount = 0;
        } else {
            amount = Double.parseDouble(amountInput);
        }
        System.out.println("\n============================== SEARCH RESULTS ==============================");
        printHeader();
        for (TransactionDetails transaction : transactions) {
            boolean matchesStartDate = startDate == null || !transaction.getDate().isBefore(startDate);
            boolean matchesEndDate = endDate == null || !transaction.getDate().isAfter(endDate);
            boolean matchesDescription = description.isEmpty() || transaction.getDescription().toLowerCase().contains(description.toLowerCase());
            boolean matchesVendor = vendor.isEmpty() || transaction.getVendor().toLowerCase().contains(vendor.toLowerCase());
            boolean matchesAmount = amount == 0 || Math.abs(transaction.getAmount() - amount) < 0.001;

            if (matchesStartDate && matchesEndDate && matchesDescription && matchesVendor && matchesAmount) {
                System.out.printf("%-15s %-15s %-25s %-15s %-10.2f\n", transaction.getDate(), transaction.getTime(), transaction.getDescription(),
                        transaction.getVendor(), transaction.getAmount());
            }
        }
        if (transactions.isEmpty()) {
            System.out.println("\nNo transactions found.");
        }
    }
}
