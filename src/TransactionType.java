// File: TransactionType.java
// package com.bharatbank.model; // Example package structure

/**
 * Enum representing the different types of financial transactions.
 * Using an enum improves type safety and readability compared to using simple strings or integers.
 */
enum TransactionType {
    ACCOUNT_OPENING("Account Opening"),
    DEPOSIT("Deposit"),
    WITHDRAWAL("Withdrawal"),
    FUND_TRANSFER_DEBIT("Fund Transfer (Dr)"),
    FUND_TRANSFER_CREDIT("Fund Transfer (Cr)"); // For future if receiving transfers

    private final String description;

    TransactionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return description;
    }
}