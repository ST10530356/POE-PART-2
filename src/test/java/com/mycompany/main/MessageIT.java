/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.main;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

/**
 *
 * @author sammy
 */
public class MessageTest {
    private Message message;
    private final ObjectMapper mapper = new ObjectMapper();
    private static final String TEST_RECIPIENT = "+27123456789";
    private static final String TEST_MESSAGE = "Hello world test message";

    @BeforeEach
    void setUp() {
        Message.sentMessages.clear();
        Message.disregardedMessages.clear();
        Message.storedMessages.clear();
        Message.messageHashes.clear();
        Message.messageIDs.clear();
        Message.totalSent = 0;
        File jsonFile = new File("messages.json");
        if (jsonFile.exists()) jsonFile.delete();
        message = new Message(TEST_RECIPIENT, TEST_MESSAGE, 1);
    }

    @AfterEach
    void tearDown() {
        File jsonFile = new File("messages.json");
        if (jsonFile.exists()) jsonFile.delete();
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals(TEST_RECIPIENT, message.getRecipient());
        assertEquals(TEST_MESSAGE, message.getMessageText());
        assertEquals(1, message.getMessageNumber());
        assertNotNull(message.getMessageID());
        assertNotNull(message.getMessageHash());
    }

    @Test
    void testCheckMessageID() {
        assertTrue(message.checkMessageID());
        Message longIdMessage = new Message(TEST_RECIPIENT, TEST_MESSAGE, 1, "12345678901");
        assertFalse(longIdMessage.checkMessageID());
    }

    @Test
    void testCheckRecipientCell() {
        assertEquals("Cell phone number successfully captured.", message.checkRecipientCell());
        Message invalidPhoneMessage = new Message("12345", TEST_MESSAGE, 1);
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.", invalidPhoneMessage.checkRecipientCell());
    }

    @Test
    void testCheckMessageLength() {
        assertEquals("Message ready to send.", message.checkMessageLength());
        String longText = "a".repeat(251);
        Message longMessage = new Message(TEST_RECIPIENT, longText, 1);
        assertEquals("Message exceeds 250 characters by 1; please reduce the size.", longMessage.checkMessageLength());
    }

    @Test
    void testCreateMessageHash() {
        String hash = message.createMessageHash();
        String expectedPrefix = message.getMessageID().substring(0, 2) + ":1:HELLOWORLD".toUpperCase();
        assertEquals(expectedPrefix, hash);
    }

    @Test
    void testSentMessageSend() {
        String result = message.sentMessage("1");
        assertEquals("Message successfully sent.", result);
        assertEquals(1, Message.sentMessages.size());
        assertEquals(TEST_MESSAGE, Message.sentMessages.get(0));
        assertEquals(1, Message.messageHashes.size());
        assertEquals(1, Message.messageIDs.size());
        assertEquals(1, Message.totalSent);
    }

    @Test
    void testSentMessageDisregard() {
        String result = message.sentMessage("2");
        assertEquals("Press 0 to delete the message.", result);
        assertEquals(1, Message.disregardedMessages.size());
        assertEquals(TEST_MESSAGE, Message.disregardedMessages.get(0));
        assertEquals(0, Message.sentMessages.size());
        assertEquals(0, Message.totalSent);
    }

    @Test
    void testSentMessageStore() {
        String result = message.sentMessage("3");
        assertEquals("Message successfully stored.", result);
        assertEquals(1, Message.storedMessages.size());
        assertEquals(TEST_MESSAGE, Message.storedMessages.get(0));
    }

    @Test
    void testPrintMessages() {
        assertEquals("No messages sent.", Message.printMessages());
        message.sentMessage("1");
        String printed = Message.printMessages();
        assertTrue(printed.contains("Message ID"));
        assertTrue(printed.contains("Message Hash"));
        assertTrue(printed.contains(TEST_MESSAGE));
    }

    @Test
    void testReturnTotalMessages() {
        assertEquals(0, Message.returnTotalMessages());
        message.sentMessage("1");
        assertEquals(1, Message.returnTotalMessages());
        Message secondMessage = new Message(TEST_RECIPIENT, "Second message", 2);
        secondMessage.sentMessage("1");
        assertEquals(2, Message.returnTotalMessages());
    }

    @Test
    void testStoreMessageJSONPersistence() throws Exception {
        message.sentMessage("3");
        File jsonFile = new File("messages.json");
        assertTrue(jsonFile.exists());
        JsonNode root = mapper.readTree(jsonFile);
        assertTrue(root.isArray());
        assertEquals(1, root.size());
        JsonNode saved = root.get(0);
        assertEquals(message.getMessageID(), saved.get("messageID").asText());
        assertEquals(message.getMessageNumber(), saved.get("messageNumber").asInt());
        assertEquals(message.getRecipient(), saved.get("recipient").asText());
        assertEquals(message.getMessageText(), saved.get("message").asText());
        assertEquals(message.getMessageHash(), saved.get("messageHash").asText());
    }

    @Test
    void testMultipleStoreAppend() throws Exception {
        message.sentMessage("3");
        Message secondMessage = new Message("+27876543210", "Second message", 2);
        secondMessage.sentMessage("3");
        JsonNode root = mapper.readTree(new File("messages.json"));
        assertEquals(2, root.size());
        assertEquals(message.getMessageID(), root.get(0).get("messageID").asText());
        assertEquals(secondMessage.getMessageID(), root.get(1).get("messageID").asText());
    }

    @Test
    void testSentMessageInvalidOption() {
        String result = message.sentMessage("99");
        assertEquals("Invalid option.", result);
        assertEquals(0, Message.sentMessages.size());
        assertEquals(0, Message.totalSent);
    }
}