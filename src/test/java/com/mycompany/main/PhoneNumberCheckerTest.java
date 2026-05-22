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
public class PhoneNumberCheckerTest {
    @Test
    void testIsNumberValidValidNumbers() {
        assertTrue(PhoneNumberChecker.isNumberValid("+27123456789"));
        assertTrue(PhoneNumberChecker.isNumberValid("+27876543210"));
        assertTrue(PhoneNumberChecker.isNumberValid("+27999000111"));
    }

    @Test
    void testIsNumberValidInvalidNumbers() {
        assertFalse(PhoneNumberChecker.isNumberValid("123456789"));
        assertFalse(PhoneNumberChecker.isNumberValid("+2712345678"));
        assertFalse(PhoneNumberChecker.isNumberValid("+271234567890"));
        assertFalse(PhoneNumberChecker.isNumberValid("+27123abc789"));
        assertFalse(PhoneNumberChecker.isNumberValid("+2712345678a"));
        assertFalse(PhoneNumberChecker.isNumberValid("+27000000000"));
        assertFalse(PhoneNumberChecker.isNumberValid("+27"));
        assertFalse(PhoneNumberChecker.isNumberValid(""));
    }
}
