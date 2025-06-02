import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a customer's bank account.
 * It holds all account-specific information and handles core banking operations
 * like deposit, withdrawal, and statement generation.
 */
class BankAccount {

    private final String accountNumber;
    private String accountHolderName;
    private long mobileNumber; 
    private String panCardNumber;
    private long adharCardNumber;
    private String address;
    private int securityPin; // 4-digit PIN for transaction authorization
    private double balance;
    private final List<Transaction> transactionHistory;

    // Constants associated with the bank account type or bank policy
    public static final String IFSC_CODE = "BBNK0001234"; // Bharat Bank IFSC Code
    private static long accountNumberCounter = 100000000001L; // Simple counter for unique account numbers

    /**
     * Constructs a new BankAccount.
     * This is typically called during the account creation process.
     *
     * @param accountHolderName The name of the account holder.
     * @param mobileNumber The registered mobile number.
     * @param panCardNumber The PAN card number.
     * @param adharCardNumber The Adhar card number.
     * @param address The residential address.
     * @param securityPin The initial security PIN.
     * @param initialDeposit The amount deposited at account opening.
     */
    public BankAccount(String accountHolderName, long mobileNumber, String panCardNumber,
                       long adharCardNumber, String address, int securityPin, double initialDeposit) {
        this.accountNumber = "BB" + (accountNumberCounter++); // Simple unique account number
        this.accountHolderName = accountHolderName;
        this.mobileNumber = mobileNumber;
        this.panCardNumber = panCardNumber;
        this.adharCardNumber = adharCardNumber;
        this.address = address;
        this.securityPin = securityPin;
        this.balance = 0; // Balance starts at 0 before initial deposit transaction
        this.transactionHistory = new ArrayList<>();

        // The first transaction is always the account opening deposit
        if (initialDeposit > 0) {
            this.deposit(initialDeposit, "Account Opening Deposit", TransactionType.ACCOUNT_OPENING);
        } else {
             this.balance = initialDeposit; // If somehow initial deposit is zero or less (though validated before)
             // Optionally log a non-financial transaction for account creation
             addTransaction(TransactionType.ACCOUNT_OPENING, 0, "Account Created");
        }
    }

    // --- Getters for account information ---
    public String getAccountNumber() { return accountNumber; }
    public String getAccountHolderName() { return accountHolderName; }
    public long getMobileNumber() { return mobileNumber; }
    public String getPanCardNumber() { return panCardNumber; }
    public long getAdharCardNumber() { return adharCardNumber; }
    public String getAddress() { return address; }
    public double getBalance() { return balance; }
    public int getSecurityPin() { return securityPin; } // Be cautious exposing PIN directly

    /**
     * Returns a copy of the transaction history to prevent external modification.
     * @return A list of transactions.
     */
    public List<Transaction> getTransactionHistory() {
        return new ArrayList<>(transactionHistory); // Return a defensive copy
    }

    // --- Setters for updatable profile information ---
    public void setAccountHolderName(String accountHolderName) { this.accountHolderName = accountHolderName; }
    public void setMobileNumber(long mobileNumber) { this.mobileNumber = mobileNumber; }
    public void setAddress(String address) { this.address = address; }
    public void setSecurityPin(int securityPin) { this.securityPin = securityPin; }


    // --- Core Banking Operations ---

    /**
     * Deposits a specified amount into the account.
     *
     * @param amount The amount to deposit. Must be positive.
     * @param remarks A description for the deposit.
     * @param type The type of transaction (usually DEPOSIT or ACCOUNT_OPENING).
     * @return true if deposit was successful, false otherwise.
     */
    public boolean deposit(double amount, String remarks, TransactionType type) {
        if (amount <= 0) {
            System.out.println("Deposit amount must be positive.");
            return false;
        }
        this.balance += amount;
        addTransaction(type, amount, remarks);
        System.out.println("Amount deposited successfully. Current Balance: " + String.format("%.2f", this.balance));
        return true;
    }

    /**
     * Withdraws a specified amount from the account.
     *
     * @param amount The amount to withdraw. Must be positive and not exceed balance.
     * @param remarks A description for the withdrawal.
     * @return true if withdrawal was successful, false otherwise.
     *         In a real system, this might throw InsufficientFundsException.
     */
    public boolean withdraw(double amount, String remarks) {
        if (amount <= 0) {
            System.out.println("Withdrawal amount must be positive.");
            return false;
        }
        if (amount > this.balance) {
            System.out.println("Insufficient funds. Withdrawal failed.");
            // Consider throwing new InsufficientFundsException("Not enough balance for withdrawal.");
            return false;
        }
        this.balance -= amount;
        addTransaction(TransactionType.WITHDRAWAL, amount, remarks);
        System.out.println("Amount withdrawn successfully. Current Balance: " + String.format("%.2f", this.balance));
        return true;
    }

    /**
     * Transfers funds from this account to another (simulated).
     * @param amount The amount to transfer.
     * @param recipientDetails Details of the recipient (e.g., account number).
     * @param remarks A description for the transfer.
     * @return true if transfer was successful, false otherwise.
     */
    public boolean transferFunds(double amount, String recipientDetails, String remarks) {
        if (amount <= 0) {
            System.out.println("Transfer amount must be positive.");
            return false;
        }
        if (amount > this.balance) {
            System.out.println("Insufficient funds for transfer.");
            return false;
        }
        this.balance -= amount;
        String fullRemarks = "To: " + recipientDetails + ". " + remarks;
        addTransaction(TransactionType.FUND_TRANSFER_DEBIT, amount, fullRemarks);
        System.out.println("Funds transferred successfully. Current Balance: " + String.format("%.2f", this.balance));
        return true;
    }

    /**
     * Adds a transaction to the history.
     * This is a private helper method to ensure all transactions are recorded consistently.
     */
    private void addTransaction(TransactionType type, double amount, String remarks) {
        Transaction transaction = new Transaction(type, amount, this.balance, remarks);
        this.transactionHistory.add(transaction);
    }

    /**
     * Validates the provided PIN against the account's stored PIN.
     * @param pinToValidate The PIN to check.
     * @return true if the PIN is correct, false otherwise.
     *         In a real system, this might throw InvalidPinException on failure after attempts.
     */
    public boolean validatePin(int pinToValidate) {
        return this.securityPin == pinToValidate;
    }

    /**
     * Provides a summary string representation of the bank account.
     * Useful for quick display of account holder and balance.
     * @return A summary string.
     */
    @Override
    public String toString() {
        return String.format("Account Holder: %s, Account No: %s, Balance: %.2f INR",
                accountHolderName, accountNumber, balance);
    }
}