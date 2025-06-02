// File: Transaction.java
// package com.bharatbank.model; // Example package structure

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a single financial transaction.
 * This class is designed to be immutable once created.
 */
class Transaction {
    private final LocalDateTime timestamp;
    private final TransactionType type;
    private final double amount;
    private final double balanceAfterTransaction;
    private final String remarks; // More descriptive than just "description"

    // A standard date-time formatter for consistent display
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Constructs a new Transaction.
     * @param type The type of the transaction.
     * @param amount The amount involved in the transaction.
     * @param balanceAfterTransaction The account balance after this transaction.
     * @param remarks A brief description or note about the transaction.
     */
    public Transaction(TransactionType type, double amount, double balanceAfterTransaction, String remarks) {
        this.timestamp = LocalDateTime.now(); // Capture the exact moment of transaction creation
        this.type = type;
        this.amount = amount;
        this.balanceAfterTransaction = balanceAfterTransaction;
        this.remarks = remarks;
    }

    // Getters for transaction details (no setters to maintain immutability)
    public LocalDateTime getTimestamp() { return timestamp; }
    public TransactionType getType() { return type; }
    public double getAmount() { return amount; }
    public double getBalanceAfterTransaction() { return balanceAfterTransaction; }
    public String getRemarks() { return remarks; }

    /**
     * Provides a formatted string representation of the transaction,
     * suitable for display in statements.
     * @return A formatted string.
     */
    @Override
    public String toString() {
        return String.format("| %-19s | %-22s | %10.2f | %12.2f | %s",
                timestamp.format(DATE_TIME_FORMATTER),
                type.getDescription(), // Use the descriptive name from enum
                amount,
                balanceAfterTransaction,
                remarks);
    }
}