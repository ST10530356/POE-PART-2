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
public class LoginT {
    private Login login;

    @BeforeEach
    void setUp() {
        login = new LoginT();
    }

    @Test
    void testCheckUserNameValid() {
        assertTrue(login.checkUserName("abc_"));
        assertTrue(login.checkUserName("a_12"));
        assertTrue(login.checkUserName("_"));
    }

    @Test
    void testCheckUserNameInvalid() {
        assertFalse(login.checkUserName("abcdef")); // no underscore
        assertFalse(login.checkUserName("abc_def")); // too long (7 chars)
        assertFalse(login.checkUserName("abc")); // no underscore
    }

    @Test
    void testCheckPasswordComplexityValid() {
        assertTrue(login.checkPasswordComplexity("Password1!"));
        assertTrue(login.checkPasswordComplexity("Abcdefg1#"));
        assertTrue(login.checkPasswordComplexity("Zz99!@#$"));
    }

    @Test
    void testCheckPasswordComplexityInvalid() {
        assertFalse(login.checkPasswordComplexity("short")); // too short
        assertFalse(login.checkPasswordComplexity("nouppercase1!")); // no uppercase
        assertFalse(login.checkPasswordComplexity("NODIGIT!")); // no digit
        assertFalse(login.checkPasswordComplexity("NoSpecial1")); // no special char
    }

    @Test
    void testCheckPhoneNumberValid() {
        assertTrue(login.checkPhoneNumber("+27123456789"));
        assertTrue(login.checkPhoneNumber("+27876543210"));
        assertTrue(login.checkPhoneNumber("+27999123456"));
    }

    @Test
    void testCheckPhoneNumberInvalid() {
        assertFalse(login.checkPhoneNumber("123456789")); // missing +27
        assertFalse(login.checkPhoneNumber("+2712345678")); // only 8 digits
        assertFalse(login.checkPhoneNumber("+271234567890")); // 10 digits
        assertFalse(login.checkPhoneNumber("+27123abc789")); // contains letters
    }

    @Test
    void testRegisterUserSuccess() {
        String result = login.registerUser("John", "Doe", "j_doe", "Password1!", "+27123456789");
        assertTrue(result.contains("Username successfully captured"));
        assertTrue(result.contains("Password successfully captured"));
        assertTrue(result.contains("Cell phone number successfully added"));
    }

    @Test
    void testRegisterUserInvalidUsername() {
        String result = login.registerUser("John", "Doe", "invalid", "Password1!", "+27123456789");
        assertEquals("Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.", result);
    }

    @Test
    void testRegisterUserInvalidPassword() {
        String result = login.registerUser("John", "Doe", "j_doe", "weak", "+27123456789");
        assertEquals("Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number and a special character.", result);
    }

    @Test
    void testRegisterUserInvalidPhone() {
        String result = login.registerUser("John", "Doe", "j_doe", "Password1!", "12345");
        assertEquals("Cell phone number incorrectly formatted or does not contain international code; please the number and try again.", result);
    }

    @Test
    void testRegisterUserDuplicateUsername() {
        login.registerUser("John", "Doe", "j_doe", "Password1!", "+27123456789");
        String result = login.registerUser("Jane", "Smith", "j_doe", "Password2@", "+27876543210");
        assertEquals("Username already exists. Please choose a different username.", result);
    }

    @Test
    void testLoginUserSuccess() {
        login.registerUser("John", "Doe", "j_doe", "Password1!", "+27123456789");
        assertTrue(login.loginUser("j_doe", "Password1!"));
        assertEquals("Welcome John Doe, it is great to see you.", login.returnLoginStatus());
    }

    @Test
    void testLoginUserWrongPassword() {
        login.registerUser("John", "Doe", "j_doe", "Password1!", "+27123456789");
        assertFalse(login.loginUser("j_doe", "wrong"));
        assertEquals("Username/password incorrect, or user name has not registeredye. Please try again", login.returnLoginStatus());
    }

    @Test
    void testLoginUserNonExistent() {
        assertFalse(login.loginUser("unknown", "pass"));
        assertEquals("Username/password incorrect, or user name has not registeredye. Please try again", login.returnLoginStatus());
    }
}