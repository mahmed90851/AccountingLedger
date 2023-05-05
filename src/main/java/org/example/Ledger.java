package org.example;
import java.io.*;
import java.util.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class Ledger {
    public static Scanner scanner = new Scanner(System.in);
    //the array list is made public, so it can be used throughout the program.
    private static ArrayList<TransactionDetails> transactions = readTransactions();

    // this is the format for my header. it is repeated several times, so it has its own method.
    public static void printHeader() {
        System.out.printf("%-15s %-15s %-25s %-15s %-10s\n", "DATE", "TIME", "DESCRIPTION", "VENDOR", "AMOUNT");
        System.out.println("---------------------------------------------------------------------------------");
    }
    // array list read's the transaction.csv file and sorts them
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
            System.err.println("File not found");
            System.exit(0);
        }
        /*use the collections class to sort the list in order by newest date.
         */
        Collections.sort(transactions, Comparator.comparing(TransactionDetails::getDate)
                .thenComparing(TransactionDetails::getTime)
                .reversed());
        return transactions;
    }
    //prints all transactions that are recorded on the csv file
    public static void printAllTransactions() {
        System.out.println("================================ ALL TRANSACTIONS ===============================\n");
        printHeader();

        for (TransactionDetails t : transactions) {
            System.out.printf("%-15s %-15s %-25s %-15s %-20.2f\n", t.getDate(), t.getTime(), t.getDescription(),
                    t.getVendor(), t.getAmount());
        }
        ApplicationInterface.showLedgerScreen();
    }
    // prints out the deposits by selecting the amount value's that are greater than 0.
    // all deposits have a positive value.
    public static void printDeposits() {
        System.out.println("================================= ALL DEPOSITS ================================= \n");
        printHeader();

        for (TransactionDetails t : transactions) {
            if (t.getAmount() > 0) {
                System.out.printf("%-15s %-15s %-25s %-15s %-10.2f\n", t.getDate(), t.getTime(), t.getDescription(),
                        t.getVendor(), t.getAmount());
            }
        }
        ApplicationInterface.showLedgerScreen();
    }
    // prints out the payments by selecting the amount value's that are less than 0.
    // all payments have a negative value.
    public static void printPayments() {
        System.out.println("================================== ALL PAYMENTS =================================\n");
        printHeader();

        for (TransactionDetails t : transactions) {
            if (t.getAmount() < 0) {
                System.out.printf("%-15s %-15s %-25s %-15s %-10.2f\n", t.getDate(), t.getTime(), t.getDescription(),
                        t.getVendor(), t.getAmount());
            }
        }
        ApplicationInterface.showLedgerScreen();
    }
    // this screen allows the user to show the report of the ledger. they can request specific transactions to be displayed
    public static void showReport() {
        ArrayList<TransactionDetails> transactions = new ArrayList<TransactionDetails>();
        Scanner scanner = new Scanner(System.in);
            System.out.println("\n===== SELECT A REPORT =====\n"
                + "[1] Month To Date\n"
                + "[2] Previous Month\n"
                + "[3] Year To Date\n"
                + "[4] Previous Year\n"
                + "[5] Search by Vendor\n"
                + "[6] Custom Search\n"
                + "[0] Back");


        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> // Month To Date report
                    getMonthToDate();
            case 2 ->
                // Display Previous Month report
                    getPreviousMonth();
            case 3 ->
                // Display Year To Date report
                    getYearToDate();
            case 4 ->
                // Display Previous Year report
                    getPreviousYear();
            case 5 ->
                // ask user for vendor name and display vendor transactions
                    searchByVendor();
            case 6 ->
                // Call customSearch method
                    customSearch();
            case 0 ->
                // Return to previous screen
                    ApplicationInterface.showLedgerScreen();
            default -> System.err.println("Invalid choice.");
        }
    }
    // this method displays month to date transactions.
    // it prints out all transactions that match the current local month
    public static void getMonthToDate() {
        LocalDate today = LocalDate.now();

        System.out.println("\n================================= MONTH TO DATE =================================");
        printHeader();
        // this for loop compares the transaction's month and year to the current one and displays them if they match.
        // the comparison of the year is important because it prevents the program from printing the same current month from previous years.
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

        System.out.println("\n================================= PREVIOUS MONTH ================================");
        printHeader();

        for (TransactionDetails transaction : transactions) {
            LocalDate transactionDate = transaction.getDate();
            if (transactionDate.getMonthValue() == prevMonthValue && transactionDate.getYear() == today.getYear()) {
                System.out.printf("%-15s %-15s %-25s %-15s %-10.2f\n", transaction.getDate(), transaction.getTime(), transaction.getDescription(),
                        transaction.getVendor(), transaction.getAmount());
            }
        }
    }
    // prints all transactions that contain the year that we are currently in.
    public static void getYearToDate() {
        LocalDate today = LocalDate.now();

        System.out.println("\n================================= YEAR TO DATE ==================================");
        printHeader();

        for (TransactionDetails transaction : transactions) {
            LocalDate transactionDate = transaction.getDate();
            if (transactionDate.getYear() == today.getYear()) {
                System.out.printf("%-15s %-15s %-25s %-15s %-10.2f\n", transaction.getDate(), transaction.getTime(), transaction.getDescription(),
                        transaction.getVendor(), transaction.getAmount());
            }
        }
    }

    // prints all transactions from the previous year by printing transactions for this year minus 1
    public static void getPreviousYear() {
        LocalDate today = LocalDate.now();

        System.out.println("\n================================= PREVIOUS YEAR =================================");
        printHeader();

        for (TransactionDetails transaction : transactions) {
            LocalDate transactionDate = transaction.getDate();
            if (transactionDate.getYear() == today.getYear() - 1) {
                System.out.printf("%-15s %-15s %-25s %-15s %-10.2f\n", transaction.getDate(), transaction.getTime(), transaction.getDescription(),
                        transaction.getVendor(), transaction.getAmount());
            }
        }
    }
    // searches the list by comparing the user input to the vendors on the list. if the user inputs match it will print out the transactions.
    public static void searchByVendor() {

        System.out.print("Enter vendor name: ");
        String vendorName = scanner.nextLine();

        System.out.println("\n======================== TRANSACTION FOR: " + vendorName.toUpperCase() + " ========================");
        printHeader();

        for (TransactionDetails transaction : transactions) {
            if (transaction.getVendor().equalsIgnoreCase(vendorName)) {
                System.out.printf("%-15s %-15s %-25s %-15s %-10.2f\n", transaction.getDate(), transaction.getTime(), transaction.getDescription(),
                        transaction.getVendor(), transaction.getAmount());
            } else if (transactions.isEmpty()) {
                System.err.println("No transactions found for vendor: " + vendorName);
            }
        }
    }

    public static void customSearch() {
        /* Prompt the user for search values
           if the user wants to skip the search value they can press enter.
           the if statement makes it so that if there is no input from the user it can move on
         */
        System.out.print("Enter start date (yyyy-mm-dd, or press Enter to skip): ");
        String startDateInput = scanner.nextLine().trim();
        LocalDate startDate = startDateInput.isEmpty() ? null : LocalDate.parse(startDateInput);

        System.out.print("Enter end date (yyyy-mm-dd, or press Enter to skip): ");
        String endDateInput = scanner.nextLine().trim();
        LocalDate endDate = endDateInput.isEmpty() ? null : LocalDate.parse(endDateInput);

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
        System.out.println("\n================================ SEARCH RESULTS =================================");
        printHeader();
        for (TransactionDetails transaction : transactions) {
            // this checks to see if the start date for the transaction is NOT before the date entered by the user
            boolean matchesStartDate = startDate == null || !transaction.getDate().isBefore(startDate);

            // this checks to see if the end date for the transaction is NOT after the date entered by the user
            boolean matchesEndDate = endDate == null || !transaction.getDate().isAfter(endDate);

            // checks to see if users description input contains the description from the list
            boolean matchesDescription = description.isEmpty() || transaction.getDescription().toLowerCase().contains(description.toLowerCase());

            // checks to see if users vendor input contains the vendor from the list
            boolean matchesVendor = vendor.isEmpty() || transaction.getVendor().toLowerCase().contains(vendor.toLowerCase());

            // checks to see if the users amount input matches any amount from the list
            boolean matchesAmount = amount == 0 || transaction.getAmount() == amount;

            if (matchesStartDate && matchesEndDate && matchesDescription && matchesVendor && matchesAmount) {
                System.out.printf("%-15s %-15s %-25s %-15s %-10.2f\n", transaction.getDate(), transaction.getTime(), transaction.getDescription(),
                        transaction.getVendor(), transaction.getAmount());
            }
        }
    }
}