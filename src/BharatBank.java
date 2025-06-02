import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;


/**
 * Main application class for the Bharat Bank console application.
 * This class handles user interaction, menu navigation, and coordinates
 * operations with the BankAccount class.
 *
 * This version supports a single active bank account at a time.
 * multiple accounts version in next update 
 */
public class BharatBank {

    // --- Constants ---
    private static final String BANK_NAME = "Bharat Bank";
    private static final int MAX_LOGIN_ATTEMPTS = 3; // Max attempts for login
    private static final int MAX_PIN_VERIFICATION_ATTEMPTS = 3; // Max attempts for PIN during operations
    private static final double MIN_INITIAL_DEPOSIT = 500.00; // Minimum opening balance

    // --- Static Resources ---
    private static final Scanner consoleScanner = new Scanner(System.in);
    private static BankAccount currentActiveAccount;
    private static boolean anAccountExists = false;

    public static void main(String[] args) {
        displayWelcomeMessage();
        mainMenuLoop();
        consoleScanner.close(); // Close the scanner when the application exits
        System.out.println("\nThank you for banking with " + BANK_NAME + ". Have a great day!");
    }

    /**
     * Displays the initial welcome message.
     */
    private static void displayWelcomeMessage() {
        System.out.println("**************************************************");
        System.out.println("*          Welcome to " + BANK_NAME + "             *");
        System.out.println("**************************************************");
    }

    /**
     * Main application loop presenting the primary menu.
     */
    private static void mainMenuLoop() {
        boolean running = true;
        while (running) {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. Create New Account");
            System.out.println("2. Login to Existing Account");
            System.out.println("3. Exit Application");
            System.out.print("Choose an option: ");

            int choice = readIntSafe();

            switch (choice) {
                case 1:
                    handleCreateAccount();
                    break;
                case 2:
                    handleLogin();
                    break;
                case 3:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    /**
     * Handles the account creation process.
     */
    private static void handleCreateAccount() {
        System.out.println("\n--- Create New Account ---");
        if (anAccountExists && currentActiveAccount != null) {
            System.out.println("An account is already active in this session. Please logout to create a new one.");
            System.out.println("Note: This demo supports one active account at a time.");
            return;
        }
        if (anAccountExists && currentActiveAccount == null) {
             System.out.println("An account was previously created in this session.");
             System.out.println("This demo supports one account per session. Please restart to create a new one, or login if you remember credentials.");
             return;
        }


        String name = readString("Enter Full Name: ");
        long mobile = readMobileNumber("Enter 10-digit Mobile Number: ");
        String pan = readPanNumber("Enter PAN Card Number (e.g., ABCDE1234F): ");
        long adhar = readAdharNumber("Enter 12-digit Adhar Card Number: ");
        String address = readString("Enter Full Address: ");
        int pin = readNewPin("Create a 4-digit numeric PIN: ");
        double initialDeposit;
        while (true) {
            initialDeposit = readDoubleSafe("Enter Initial Deposit Amount (Min " + MIN_INITIAL_DEPOSIT + "): ");
            if (initialDeposit >= MIN_INITIAL_DEPOSIT) {
                break;
            }
            System.out.println("Initial deposit must be at least " + String.format("%.2f", MIN_INITIAL_DEPOSIT) + ".");
        }

        currentActiveAccount = new BankAccount(name, mobile, pan, adhar, address, pin, initialDeposit);
        anAccountExists = true; // Mark that an account has been created

        System.out.println("\nAccount created successfully for " + currentActiveAccount.getAccountHolderName() + "!");
        System.out.println("Your Account Number: " + currentActiveAccount.getAccountNumber());
        System.out.println("IFSC Code: " + BankAccount.IFSC_CODE);
        System.out.println("Please login to access your account services.");
    }

    /**
     * Handles the user login process.
     */
    private static void handleLogin() {
        System.out.println("\n--- Account Login ---");
        if (!anAccountExists) {
            System.out.println("No account has been created in this session. Please create an account first.");
            return;
        }
        if (currentActiveAccount != null && currentActiveAccount.getMobileNumber() != 0) { 
             System.out.println("An session is already active. Logout first to login with different credentials .");
        }


        for (int attempt = 1; attempt <= MAX_LOGIN_ATTEMPTS; attempt++) {
            System.out.println("\nLogin Attempt " + attempt + " of " + MAX_LOGIN_ATTEMPTS);
            long mobileInput = readMobileNumber("Enter your registered Mobile Number: ");
            int pinInput = readPin("Enter your 4-digit PIN: ");

            if (currentActiveAccount != null && currentActiveAccount.getMobileNumber() == mobileInput && currentActiveAccount.validatePin(pinInput)) {
                System.out.println("\nLogin Successful! Welcome, " + currentActiveAccount.getAccountHolderName() + ".");
                loggedInUserMenu();
                return; // Return to main menu after user logs out from loggedInUserMenu
            } else {
                System.out.println("Invalid mobile number or PIN. Please try again.");
            }
        }
        System.out.println("\nMaximum login attempts reached. Account access locked temporarily for security.");
    
    }

    /**
     * Menu for logged-in users.
     */
    private static void loggedInUserMenu() {
        if (currentActiveAccount == null) {
            System.out.println("Error: No user is currently logged in. Returning to main menu.");
            return;
        }

        boolean stayLoggedIn = true;
        while (stayLoggedIn) {
            System.out.println("\n--- " + currentActiveAccount.getAccountHolderName() + "'s Dashboard (" + BANK_NAME + ") ---");
            System.out.println("Account No: " + currentActiveAccount.getAccountNumber() + " | Balance: " + String.format("%.2f", currentActiveAccount.getBalance()) + " INR");
            System.out.println("---------------------------------------------");
            System.out.println("1. Deposit Funds");
            System.out.println("2. Withdraw Funds");
            System.out.println("3. Fund Transfer");
            System.out.println("4. View Account Statement");
            System.out.println("5. View Account Details");
            System.out.println("6. Edit Profile");
            System.out.println("7. Logout");
            System.out.print("Choose an option: ");

            int choice = readIntSafe();

            switch (choice) {
                case 1: handleDeposit(); break;
                case 2: handleWithdrawal(); break;
                case 3: handleFundTransfer(); break;
                case 4: handleViewStatement(); break;
                case 5: handleViewAccountDetails(); break;
                case 6: handleEditProfile(); break;
                case 7:
                    stayLoggedIn = false;
                    System.out.println("\nYou have been logged out successfully.");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    // --- Transaction Handling Methods ---
    private static void handleDeposit() {
        System.out.println("\n--- Deposit Funds ---");
        double amount = readDoubleSafe("Enter amount to deposit: ");
        if (amount <= 0) {
            System.out.println("Deposit amount must be greater than zero.");
            return;
        }
        currentActiveAccount.deposit(amount, "Self Deposit", TransactionType.DEPOSIT);
    }

    private static void handleWithdrawal() {
        System.out.println("\n--- Withdraw Funds ---");
        if (!verifyPinForOperation("withdrawal")) return;

        double amount = readDoubleSafe("Enter amount to withdraw: ");
        currentActiveAccount.withdraw(amount, "ATM Withdrawal"); 
    }

    private static void handleFundTransfer() {
        System.out.println("\n--- Fund Transfer ---");
        if (!verifyPinForOperation("fund transfer")) return;

        String recipientAccNo = readString("Enter Recipient's Account Number: ");
        String recipientName = readString("Enter Recipient's Name (for remarks): "); // Optional
        double amount = readDoubleSafe("Enter amount to transfer: ");
        String remarks = readString("Enter Remarks/Reason for transfer (optional): ");
        if (remarks.trim().isEmpty()) {
            remarks = "Transfer to " + recipientName;
        }

        currentActiveAccount.transferFunds(amount, recipientAccNo + " (" + recipientName + ")", remarks);
    }

    // --- Information Viewing Methods ---
    private static void handleViewStatement() {
        System.out.println("\n--- Account Statement ---");
        System.out.println("Account Holder: " + currentActiveAccount.getAccountHolderName());
        System.out.println("Account Number: " + currentActiveAccount.getAccountNumber());
        System.out.println("-----------------------------------------------------------------------------------------------------------");
        System.out.printf("| %-19s | %-22s | %-10s | %-12s | %s%n", "Timestamp", "Transaction Type", "Amount (INR)", "Balance (INR)", "Remarks");
        System.out.println("-----------------------------------------------------------------------------------------------------------");

        List<Transaction> history = currentActiveAccount.getTransactionHistory();
        if (history.isEmpty()) {
            System.out.println("| No transactions found.                                                                                  |");
        } else {
            for (Transaction tx : history) {
                System.out.println(tx.toString()); // Relies on Transaction.toString()
            }
        }
        System.out.println("-----------------------------------------------------------------------------------------------------------");
        System.out.println("Current Balance: " + String.format("%.2f", currentActiveAccount.getBalance()) + " INR");
    }

    private static void handleViewAccountDetails() {
        System.out.println("\n--- Account Details ---");
        System.out.println("Bank Name:         " + BANK_NAME);
        System.out.println("IFSC Code:         " + BankAccount.IFSC_CODE);
        System.out.println("Account Holder:    " + currentActiveAccount.getAccountHolderName());
        System.out.println("Account Number:    " + currentActiveAccount.getAccountNumber());
        System.out.println("Registered Mobile: " + currentActiveAccount.getMobileNumber());
        System.out.println("PAN Card:          " + currentActiveAccount.getPanCardNumber());
        System.out.println("Adhar Card:        " + currentActiveAccount.getAdharCardNumber());
        System.out.println("Address:           " + currentActiveAccount.getAddress());
        System.out.println("Current Balance:   " + String.format("%.2f", currentActiveAccount.getBalance()) + " INR");
    }

    // --- Profile Editing ---
    private static void handleEditProfile() {
        boolean editing = true;
        while(editing) {
            System.out.println("\n--- Edit Profile ---");
            System.out.println("1. Update Account Holder Name");
            System.out.println("2. Update Mobile Number");
            System.out.println("3. Update Address");
            System.out.println("4. Change PIN");
            System.out.println("5. Back to Dashboard");
            System.out.print("Choose an option: ");
            int choice = readIntSafe();

            switch(choice) {
                case 1:
                    if (verifyPinForOperation("updating name")) {
                        String newName = readString("Enter new Account Holder Name: ");
                        currentActiveAccount.setAccountHolderName(newName);
                        System.out.println("Account holder name updated successfully.");
                    }
                    break;
                case 2:
                     if (verifyPinForOperation("updating mobile number")) {
                        long newMobile = readMobileNumber("Enter new 10-digit Mobile Number: ");
                        // Add check if new mobile is already in use in a multi-user system
                        currentActiveAccount.setMobileNumber(newMobile);
                        System.out.println("Mobile number updated successfully.");
                    }
                    break;
                case 3:
                    if (verifyPinForOperation("updating address")) {
                        String newAddress = readString("Enter new Address: ");
                        currentActiveAccount.setAddress(newAddress);
                        System.out.println("Address updated successfully.");
                    }
                    break;
                case 4:
                    handleChangePin();
                    break;
                case 5:
                    editing = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void handleChangePin() {
        System.out.println("\n--- Change PIN ---");

        System.out.print("Enter current 4-digit PIN: ");
        int currentPinAttempt = readPin(""); // Prompt is part of the print above
        if (!currentActiveAccount.validatePin(currentPinAttempt)) {
            System.out.println("Incorrect current PIN. PIN change aborted.");
            return;
        }

        int newPin1 = readNewPin("Enter new 4-digit numeric PIN: ");
        int newPin2 = readNewPin("Confirm new 4-digit numeric PIN: ");

        if (newPin1 == newPin2) {
            currentActiveAccount.setSecurityPin(newPin1);
            System.out.println("PIN changed successfully.");
        } else {
            System.out.println("New PINs do not match. PIN change aborted.");
        }
    }


    // --- Utility and Helper Methods ---

    /**
     * Verifies the user's PIN for a critical operation.
     * Allows a fixed number of attempts.
     * @param operationName Name of the operation requiring PIN (e.g., "withdrawal").
     * @return true if PIN is successfully verified, false otherwise.
     */
    private static boolean verifyPinForOperation(String operationName) {
        System.out.println("PIN verification required for " + operationName + ".");
        for (int attempt = 1; attempt <= MAX_PIN_VERIFICATION_ATTEMPTS; attempt++) {
            System.out.print("Enter your 4-digit PIN (Attempt " + attempt + "/" + MAX_PIN_VERIFICATION_ATTEMPTS + "): ");
            int pinAttempt = readPin(""); // Prompt is part of the print
            if (currentActiveAccount.validatePin(pinAttempt)) {
                return true;
            } else {
                System.out.println("Incorrect PIN.");
            }
        }
        System.out.println("Maximum PIN verification attempts reached. " + operationName + " cancelled for security.");
        // In a real app, might temporarily lock account or specific features.
        return false;
    }

    /**
     * Safely reads an integer from the console, handling potential NumberFormatExceptions.
     * @return The integer read from the console.
     */
    private static int readIntSafe() {
        while (true) {
            try {
                String line = consoleScanner.nextLine();
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a whole number: ");
            }
        }
    }

    /**
     * Safely reads a double from the console.
     * @param prompt The message to display to the user.
     * @return The double value entered.
     */
    private static double readDoubleSafe(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                String line = consoleScanner.nextLine();
                return Double.parseDouble(line);
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a valid number (e.g., 100.50): ");
            }
        }
    }

    /**
     * Reads a non-empty string from the console.
     * @param prompt The message to display to the user.
     * @return The string entered by the user.
     */
    private static String readString(String prompt) {
        String input;
        do {
            System.out.print(prompt);
            input = consoleScanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Input cannot be empty. Please try again.");
            }
        } while (input.isEmpty());
        return input;
    }

    /**
     * Reads a 4-digit numeric PIN from the console.
     * @param prompt The message to display to the user (can be empty if prompt handled outside).
     * @return The 4-digit PIN.
     */
    private static int readPin(String prompt) {
        Pattern pinPattern = Pattern.compile("^\\d{4}$"); // Regex for exactly 4 digits
        while (true) {
            if (!prompt.isEmpty()) System.out.print(prompt);
            String pinStr = consoleScanner.nextLine();
            if (pinPattern.matcher(pinStr).matches()) {
                try {
                    return Integer.parseInt(pinStr);
                } catch (NumberFormatException e) {
                    // Should not happen if regex matches, but good for robustness
                    System.out.print("Error parsing PIN. Please enter a 4-digit numeric PIN: ");
                }
            } else {
                System.out.print("Invalid PIN format. Please enter a 4-digit numeric PIN: ");
            }
        }
    }

    /**
     * Reads a new 4-digit numeric PIN, typically used during account creation or PIN change.
     * @param prompt The message to display.
     * @return The validated 4-digit PIN.
     */
    private static int readNewPin(String prompt) {
        // This method is essentially the same as readPin for now,
        // but separated in case future logic for 'new' PINs differs (e.g., complexity rules).
        return readPin(prompt);
    }


    /**
     * Reads a 10-digit mobile number.
     * @param prompt The message to display.
     * @return The validated 10-digit mobile number as long.
     */
    private static long readMobileNumber(String prompt) {
        Pattern mobilePattern = Pattern.compile("^\\d{10}$"); // Regex for exactly 10 digits
        while (true) {
            System.out.print(prompt);
            String mobileStr = consoleScanner.nextLine();
            if (mobilePattern.matcher(mobileStr).matches()) {
                try {
                    return Long.parseLong(mobileStr);
                } catch (NumberFormatException e) {
                    System.out.print("Error parsing mobile number. Please enter a 10-digit number: ");
                }
            } else {
                System.out.print("Invalid mobile number format. Please enter a 10-digit number: ");
            }
        }
    }

    /**
     * Reads a PAN card number. Basic validation for common format.
     * @param prompt The message to display.
     * @return The PAN card number as a string.
     */
    private static String readPanNumber(String prompt) {
        // Basic PAN format: 5 letters, 4 numbers, 1 letter. Case-insensitive for input.
        Pattern panPattern = Pattern.compile("^[A-Z]{5}\\d{4}[A-Z]$", Pattern.CASE_INSENSITIVE);
        while(true) {
            System.out.print(prompt);
            String panStr = consoleScanner.nextLine().toUpperCase().trim(); // Convert to uppercase for consistency
            if (panPattern.matcher(panStr).matches()) {
                return panStr;
            } else {
                System.out.print("Invalid PAN format (e.g., ABCDE1234F). Please try again: ");
            }
        }
    }

     /**
     * Reads a 12-digit Adhar card number.
     * @param prompt The message to display.
     * @return The Adhar card number as long.
     */
    private static long readAdharNumber(String prompt) {
        Pattern adharPattern = Pattern.compile("^\\d{12}$"); // Regex for exactly 12 digits
        while (true) {
            System.out.print(prompt);
            String adharStr = consoleScanner.nextLine().trim();
            if (adharPattern.matcher(adharStr).matches()) {
                try {
                    return Long.parseLong(adharStr);
                } catch (NumberFormatException e) {
                    // This should ideally not happen if regex matches
                    System.out.print("Error parsing Adhar number. Please enter a 12-digit numeric Adhar: ");
                }
            } else {
                System.out.print("Invalid Adhar format. Please enter a 12-digit numeric Adhar: ");
            }
        }
    }
}