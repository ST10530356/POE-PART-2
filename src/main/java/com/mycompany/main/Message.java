/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.main;

/**
 *
 * @author sammy
 */
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class Message {

    private String messageID;
    private int messageNumber;
    private String recipient;
    private String messageText;
    private String messageHash;

    public static ArrayList<String> sentMessages        = new ArrayList<>();
    public static ArrayList<String> disregardedMessages = new ArrayList<>();
    public static ArrayList<String> storedMessages      = new ArrayList<>();
    public static ArrayList<String> messageHashes       = new ArrayList<>();
    public static ArrayList<String> messageIDs          = new ArrayList<>();
    public static int totalSent = 0;

    public Message(String recipient, String messageText, int messageNumber) {
        this.messageID     = generateID();
        this.messageNumber = messageNumber;
        this.recipient     = recipient;
        this.messageText   = messageText;
        this.messageHash   = createMessageHash();
    }

    public Message(String recipient, String messageText, int messageNumber, String messageID) {
        this.messageID     = messageID;
        this.messageNumber = messageNumber;
        this.recipient     = recipient;
        this.messageText   = messageText;
        this.messageHash   = createMessageHash();
    }

    private String generateID() {
        Random rand = new Random();
        long id = (long)(rand.nextDouble() * 9000000000L) + 1000000000L;
        return String.valueOf(id);
    }

    public boolean checkMessageID() {
        return messageID.length() <= 10;
    }

    public String checkRecipientCell() {
        if (PhoneNumberChecker.isNumberValid(recipient)) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    }

    public String checkMessageLength() {
        if (messageText.length() <= 250) {
            return "Message ready to send.";
        }
        int over = messageText.length() - 250;
        return "Message exceeds 250 characters by " + over + "; please reduce the size.";
    }

    public String createMessageHash() {
        String[] words = messageText.trim().split("\\s+");
        String first = words[0].replaceAll("[^a-zA-Z0-9]", "");
        String last  = words[words.length - 1].replaceAll("[^a-zA-Z0-9]", "");
        return (messageID.substring(0, 2) + ":" + messageNumber + ":" + first + last).toUpperCase();
    }

    // Send, discard, or store the message
    public String sentMessage(String choice) {
        switch (choice) {
            case "1":
                sentMessages.add(messageText);
                messageHashes.add(messageHash);
                messageIDs.add(messageID);
                totalSent++;
                return "Message successfully sent.";
            case "2":
                disregardedMessages.add(messageText);
                return "Press 0 to delete the message.";
            case "3":
                storedMessages.add(messageText);
                storeMessage();
                return "Message successfully stored.";
            default:
                return "Invalid option.";
        }
    }

    // Shows all sent messages
    public static String printMessages() {
        if (sentMessages.isEmpty()) return "No messages sent.";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sentMessages.size(); i++) {
            sb.append("\nMessage ID   : ").append(messageIDs.get(i))
              .append("\nMessage Hash : ").append(messageHashes.get(i))
              .append("\nMessage      : ").append(sentMessages.get(i)).append("\n");
        }
        return sb.toString();
    }

    // Returns total messages sent
    public static int returnTotalMessages() {
        return totalSent;
    }

    // Saves message to messages.json
    public void storeMessage() {
        String entry = "  {\n"
            + "    \"messageID\": \""   + messageID    + "\",\n"
            + "    \"messageNumber\": " + messageNumber + ",\n"
            + "    \"recipient\": \""   + recipient    + "\",\n"
            + "    \"message\": \""     + messageText  + "\",\n"
            + "    \"messageHash\": \"" + messageHash  + "\"\n"
            + "  }";

        File file = new File("messages.json");
        try {
            if (!file.exists()) {
                FileWriter fw = new FileWriter(file);
                fw.write("[\n" + entry + "\n]");
                fw.close();
            } else {
                String existing = new String(java.nio.file.Files.readAllBytes(file.toPath())).trim();
                existing = existing.substring(0, existing.lastIndexOf("]")).trim();
                FileWriter fw = new FileWriter(file);
                fw.write(existing + ",\n" + entry + "\n]");
                fw.close();
            }
        } catch (IOException e) {
            System.out.println("Error saving message: " + e.getMessage());
        }
    }

    // Getters
    public String getMessageID()    { return messageID; }
    public String getMessageHash()  { return messageHash; }
    public String getRecipient()    { return recipient; }
    public String getMessageText()  { return messageText; }
    public int    getMessageNumber(){ return messageNumber; }
}

