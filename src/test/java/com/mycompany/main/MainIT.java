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

/**
 *
 * @author sammy
 */
public class MainTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final ByteArrayInputStream originalIn = System.in;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setIn(originalIn);
        clearLoginUsers();
        clearMessageStaticFields();
        deleteMessagesJson();
    }

    private void clearLoginUsers() {
        try {
            Login login = new Login();
            Field usersField = Login.class.getDeclaredField("users");
            usersField.setAccessible(true);
            java.util.ArrayList<?> users = (java.util.ArrayList<?>) usersField.get(login);
            users.clear();
        } catch (Exception e) {
            // ignore
        }
    }

    private void clearMessageStaticFields() {
        Message.sentMessages.clear();
        Message.disregardedMessages.clear();
        Message.storedMessages.clear();
        Message.messageHashes.clear();
        Message.messageIDs.clear();
        Message.totalSent = 0;
    }

    private void deleteMessagesJson() {
        java.io.File f = new java.io.File("messages.json");
        if (f.exists()) f.delete();
    }

    @Test
    void testRegisterAndLoginFlow() {
        String input = "1\nJohn\nDoe\nj_doe\nPassword1!\n+27123456789\n2\nj_doe\nPassword1!\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Main.main(new String[]{});
        String output = outContent.toString();
        assertTrue(output.contains("Username successfully captured"));
        assertTrue(output.contains("Welcome John Doe"));
        assertTrue(output.contains("Goodbye!"));
    }

    @Test
    void testLoginFailure() {
        String input = "2\nwronguser\nwrongpass\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Main.main(new String[]{});
        String output = outContent.toString();
        assertTrue(output.contains("Username/password incorrect"));
    }

    @Test
    void testRegisterInvalidUsername() {
        String input = "1\nJohn\nDoe\nbaduser\nPassword1!\n+27123456789\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Main.main(new String[]{});
        String output = outContent.toString();
        assertTrue(output.contains("Username is not correctly formatted"));
    }

    @Test
    void testRegisterInvalidPassword() {
        String input = "1\nJohn\nDoe\nj_doe\nweak\n+27123456789\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Main.main(new String[]{});
        String output = outContent.toString();
        assertTrue(output.contains("Password is not correctly formatted"));
    }

    @Test
    void testRegisterInvalidPhone() {
        String input = "1\nJohn\nDoe\nj_doe\nPassword1!\n12345\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Main.main(new String[]{});
        String output = outContent.toString();
        assertTrue(output.contains("Cell phone number incorrectly formatted"));
    }

    @Test
    void testSendMessageFlow() {
        String input = "1\nJohn\nDoe\nj_doe\nPassword1!\n+27123456789\n" +
                       "2\nj_doe\nPassword1!\n" +
                       "1\n" +      // How many messages? 1
                       "1\n" +      // Send Messages option
                       "+27123456789\n" + // recipient
                       "Hello test message\n" + // message text
                       "1\n" +      // Send option
                       "3\n" +      // Quit to main menu
                       "3\n";       // Exit app
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Main.main(new String[]{});
        String output = outContent.toString();
        assertTrue(output.contains("Message successfully sent"));
        assertTrue(output.contains("Total messages sent this session: 1"));
    }

    @Test
    void testStoreMessageAndJson() {
        String input = "1\nJohn\nDoe\nj_doe\nPassword1!\n+27123456789\n" +
                       "2\nj_doe\nPassword1!\n" +
                       "1\n" +      // 1 message
                       "1\n" +      // Send Messages
                       "+27123456789\n" +
                       "Store this message\n" +
                       "3\n" +      // Store option
                       "3\n3\n";    // Quit menu and exit
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Main.main(new String[]{});
        assertTrue(new java.io.File("messages.json").exists());
        String output = outContent.toString();
        assertTrue(output.contains("Message successfully stored"));
    }

    @Test
    void testMessageLengthExceeds() {
        String longMessage = "a".repeat(251);
        String input = "1\nJohn\nDoe\nj_doe\nPassword1!\n+27123456789\n" +
                       "2\nj_doe\nPassword1!\n" +
                       "1\n1\n" +
                       "+27123456789\n" +
                       longMessage + "\n" +
                       "1\n3\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Main.main(new String[]{});
        String output = outContent.toString();
        assertTrue(output.contains("exceeds 250 characters by 1"));
    }

    @Test
    void testShowRecentMessagesComingSoon() {
        String input = "1\nJohn\nDoe\nj_doe\nPassword1!\n+27123456789\n" +
                       "2\nj_doe\nPassword1!\n" +
                       "1\n" +      // 1 message
                       "2\n" +      // Show Recently Sent Messages (Coming Soon)
                       "3\n3\n";    // Quit
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Main.main(new String[]{});
        String output = outContent.toString();
        assertTrue(output.contains("Coming Soon"));
    }

    @Test
    void testInvalidMainMenuOption() {
        String input = "5\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Main.main(new String[]{});
        String output = outContent.toString();
        assertTrue(output.contains("Invalid choice"));
    }

    @Test
    void testInvalidQuickChatMenuOption() {
        String input = "1\nJohn\nDoe\nj_doe\nPassword1!\n+27123456789\n" +
                       "2\nj_doe\nPassword1!\n" +
                       "1\n" +      // 1 message
                       "5\n3\n3\n"; // invalid option then quit
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Main.main(new String[]{});
        String output = outContent.toString();
        assertTrue(output.contains("Invalid choice"));
    }
}

