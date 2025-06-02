# BharatBank - Core Java Console Banking Application

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java Version](https://img.shields.io/badge/Java-11%2B-blue.svg)](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
<!-- Add other badges if applicable, e.g., for build status if you set up CI -->

A comprehensive console-based banking application built using core Java principles. This project simulates basic banking operations such as account creation, login, deposit, withdrawal, fund transfer, statement viewing, and profile management. It's designed to showcase object-oriented programming, data structures, exception handling, and other fundamental Java concepts.

**Developed by: Tarun Amballa**
*   **LinkedIn:** [linkedin.com/in/amballatarun](https://www.linkedin.com/in/amballatarun/)
*   **GitHub:** [github.com/tarunamballa](https://github.com/tarunamballa)

## Table of Contents

*   [Features](#features)
*   [Core Java Concepts Demonstrated](#core-java-concepts-demonstrated)
*   [Project Structure](#project-structure)
*   [Prerequisites](#prerequisites)
*   [Getting Started](#getting-started)
    *   [Cloning the Repository](#cloning-the-repository)
    *   [Compiling the Code](#compiling-the-code)
    *   [Running the Application](#running-the-application)
*   [Usage](#usage)
*   [Screenshots (Optional)](#screenshots-optional)
*   [Future Enhancements](#future-enhancements)
*   [Contributing](#contributing)
*   [License](#license)

## Features

*   **Account Management:**
    *   Create new bank accounts with user details (Name, Mobile, PAN, Adhar, Address).
    *   Secure PIN creation for account access and transaction authorization.
    *   Unique account number generation (simulated).
*   **User Authentication:**
    *   Login system with mobile number and PIN.
    *   Maximum login attempt restrictions for security.
*   **Banking Operations:**
    *   **Deposit:** Add funds to the account.
    *   **Withdrawal:** Withdraw funds, with PIN verification and balance checks.
    *   **Fund Transfer:** Transfer funds to other (simulated) accounts, with PIN verification.
    *   **Balance Inquiry:** Check current account balance (requires PIN).
*   **Account Services:**
    *   **View Statement:** Display a detailed transaction history with timestamps, types, amounts, and running balances.
    *   **View Account Details:** Display all registered account information.
    *   **Edit Profile:** Update account holder name, mobile number, and address (with PIN verification).
    *   **Change PIN:** Securely change the account's security PIN.
*   **User-Friendly Console Interface:**
    *   Clear, menu-driven navigation.
    *   Input validation for robustness.

## Core Java Concepts Demonstrated

This project aims to effectively demonstrate a range of core Java concepts:

*   **Object-Oriented Programming (OOP):**
    *   **Encapsulation:** Bundling of data (attributes) and methods that operate on the data within classes (`BankAccount`, `Transaction`).
    *   **Abstraction:** Hiding complex implementation details and exposing only essential functionalities.
    *   **Inheritance:** (Potentially for future extension, e.g., different account types like `SavingsAccount`, `CurrentAccount`).
    *   **Polymorphism:** `toString()` method overridden in `Transaction` and `BankAccount` for custom string representations.
*   **Data Structures:**
    *   `java.util.ArrayList`: Used for managing the list of transactions in an account.
*   **Exception Handling:**
    *   `try-catch` blocks for handling potential runtime errors like `NumberFormatException` during input parsing.
    *   Conceptual understanding of custom exceptions for more specific error management (e.g., `InsufficientFundsException`).
*   **Enums (`enum`):**
    *   `TransactionType` enum for type-safe representation of transaction categories.
*   **Java Date/Time API (`java.time`):**
    *   `LocalDateTime` for accurate transaction timestamping.
    *   `DateTimeFormatter` for user-friendly date and time display.
*   **Input/Output (I/O):**
    *   `java.util.Scanner` for handling console input.
    *   Formatted output using `System.out.println()` and `String.format()`.
*   **Control Flow Statements:**
    *   `if-else`, `switch-case`, `while`, `for` loops for logical program flow.
*   **String Manipulation & Regular Expressions:**
    *   `String.format()` for output.
    *   `java.util.regex.Pattern` for validating input formats (PIN, Mobile, PAN).
*   **Static Members & Methods:**
    *   For constants (e.g., `IFSC_CODE`, `BANK_NAME`), utility methods, and managing the shared `Scanner` instance.
*   **Constants and `final` Keyword:**
    *   Ensuring immutability and defining fixed values.
*   **Modularity & Code Organization:**
    *   Breaking down the application into well-defined classes (`BharatBank`, `BankAccount`, `Transaction`, `TransactionType`) and methods.

## Project Structure

The project is organized into the following main Java files:

BharatBank-CoreJava-ConsoleApp/
├── src/ (or directly in the root if not using package structure for this demo)
│ ├── BharatBank.java # Main application class, handles UI and flow
│ ├── BankAccount.java # Represents a customer's bank account
│ ├── Transaction.java # Represents a single financial transaction
│ └── TransactionType.java # Enum for different types of transactions
├── .gitignore
└── README.md



*(Adjust the `src/` part if you decide to put files directly in the root or use a specific package structure like `com/bharatbank/app`)*

## Prerequisites

*   **Java Development Kit (JDK):** Version 11 or higher.
*   An IDE (like IntelliJ IDEA, Eclipse, VS Code with Java extensions) is recommended for development but not strictly required for running.

## Getting Started

Follow these instructions to get a copy of the project up and running on your local machine.

### Cloning the Repository

```bash
git clone https://github.com/tarunamballa/BharatBank-CoreJava-ConsoleApp.git
cd BharatBank-CoreJava-ConsoleApp