/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.main;

/**
 *
 * @author sammy
 */
import java.util.regex.Pattern;

//Handles phone number requirements are fullfilled
public class PhoneNumberChecker {
    public static boolean  isNumberValid(String cellPhone){
        return Pattern.matches("\\+27\\d{9}", cellPhone);
    }
    
}

