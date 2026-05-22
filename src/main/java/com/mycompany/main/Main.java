/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.main;

/**
 *
 * @author sammy
 */

import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Login   login   = new Login();

    public static void main(String[] args) {

        String choice;

        while (true) {
            System.out.println("\nQuickChat,Registration & Login");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> register();
                case "2" -> {
                    if (loginUser()) {
                        quickChatMenu();
                    }
                }
                case "3" -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice. Please enter 1, 2, or 3.");
            }
        }
    }

    private static void register() {
        System.out.println("\n--- Register New Account ---");
        System.out.print("First name: ");
        String firstName = scanner.nextLine();

        System.out.print("Surname: ");
        String surname = scanner.nextLine();

        System.out.print("Username (must contain '_' and be max 5 characters): ");
        String username = scanner.nextLine();

        System.out.println("Password requirements: 8+ chars, 1 uppercase, 1 number, 1 special character");
        System.out.print("Password: ");
        String password = scanner.nextLine();

        System.out.println("Phone format: +27 followed by exactly 9 digits (e.g. +27123456789)");
        System.out.print("Cell phone number: ");
        String phoneNumber = scanner.nextLine();

        System.out.println(login.registerUser(firstName, surname, username, password, phoneNumber));
    }

    private static boolean loginUser() {
        System.out.println("\n--- Login ---");
        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        boolean success = login.loginUser(username, password);
        System.out.println(login.returnLoginStatus());
        return success;
    }

    private static void quickChatMenu() {
        System.out.println("\nWelcome to QuickChat.");

        // Ask how many messages they want to send
        int numMessages = 0;
        while (numMessages <= 0) {
            System.out.print("How many messages would you like to send? ");
            try {
                numMessages = Integer.parseInt(scanner.nextLine().trim());
                if (numMessages <= 0) {
                    System.out.println("Please enter a number greater than 0.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a whole number.");
            }
        }

        String menuChoice;
        while (true) {
            System.out.println("\nQuickChat Menu");
            System.out.println("1. Send Messages");
            System.out.println("2. Show Recently Sent Messages");
            System.out.println("3. Quit");
            System.out.print("Choose an option: ");
            menuChoice = scanner.nextLine().trim();

            switch (menuChoice) {
                case "1" -> sendMessages(numMessages);
                case "2" -> System.out.println("Coming Soon.");
                case "3" -> {
                    System.out.println("Returning to main menu.");
                    return;
                }
                default -> System.out.println("Invalid choice. Please enter 1, 2, or 3.");
            }
        }
    }

    private static void sendMessages(int numMessages) {

        for (int i = 0; i < numMessages; i++) {
            System.out.println("\nMessage " + (i + 1) + " of " + numMessages + " ");

            String recipient;
            while (true) {
                System.out.print("Recipient cell number (must start with +27): ");
                recipient = scanner.nextLine().trim();
                if (PhoneNumberChecker.isNumberValid(recipient)) {
                    System.out.println("Cell phone number successfully captured.");
                    break;
                } else {
                    System.out.println("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.");
                }
            }

            String messageText;
            while (true) {
                System.out.print("Enter your message (max 250 characters): ");
                messageText = scanner.nextLine();
                if (messageText.length() <= 250) {
                    break;
                }
                int excess = messageText.length() - 250;
                System.out.println("Message exceeds 250 characters by " + excess
                        + "; please reduce the size.");
            }

            Message message = new Message(recipient, messageText, i);

            System.out.println("\nMessage ID   : " + message.getMessageID());
            System.out.println("Message Hash : " + message.getMessageHash());

            System.out.println("\nWhat would you like to do with this message?");
            System.out.println("1. Send Message");
            System.out.println("2. Disregard Message");
            System.out.println("3. Store Message");
            System.out.print("Choose an option: ");
            String actionChoice = scanner.nextLine().trim();

            String result = message.sentMessage(actionChoice);
            System.out.println(result);

            if (actionChoice.equals("1")) {
                System.out.println("\nMessage Details");
                System.out.println("Message ID   : " + message.getMessageID());
                System.out.println("Message Hash : " + message.getMessageHash());
                System.out.println("Recipient    : " + message.getRecipient());
                System.out.println("Message      : " + message.getMessageText());
            }
        }

        System.out.println("\nTotal messages sent this session: " + Message.returnTotalMessages());
        System.out.println(Message.printMessages());
    }
}
