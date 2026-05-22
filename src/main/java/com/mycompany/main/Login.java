/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.main;

/**
 *
 * @author sammy
 */

import java.util.ArrayList;


public class Login{
    private static class User{
        String firstName; 
        String surname; 
        String username; 
        String password; 
        String phoneNumber;

        //Assings entered user info to the corresponding fields
        User(String firstName,
        String surname,
        String username,
        String password,
        String phoneNumber){

        //Stores set users to correct fields
        this.firstName = firstName;
        this.surname = surname;
        this.username = username;
        this.password = password; 
        this.phoneNumber = phoneNumber;

        }
    }

    //Creates a list to store registered users temporary
    private final ArrayList<User> users = new ArrayList<>();
    private String loginMessage;

    //Makes sure the requirements are fullfilled for user name
    public Boolean checkUserName(String username){
        return username.contains("_") && username.length() <= 5;
    }

    boolean hasUpper = false;
    boolean hasDigit = false;
    boolean hasSpecial = false;

    //Checks if all conditions are met for passwords
    public Boolean checkPasswordComplexity(String password){
        if(password.length() < 8) return false;
        for(char c : password.toCharArray()){
            if(Character.isUpperCase(c)) hasUpper = true;
            else if(Character.isDigit(c)) hasDigit = true;
            else if(!Character.isLetterOrDigit(c)) hasSpecial = true;

        }
        return hasUpper && hasDigit && hasSpecial;
    }

    //Calls in method for verifying phone number 
    //Allows phone number to be used 
    public Boolean checkPhoneNumber(String phoneNumber){
        return  PhoneNumberChecker.isNumberValid(phoneNumber);
    }

    //Handles the regigestration logic and conditions
    public String registerUser(String firstname, String surname, String username, String password, String phoneNumber){
        if(!checkUserName(username))
            return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
        if(!checkPasswordComplexity(password))
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number and a special character.";
        if(!checkPhoneNumber(phoneNumber))
            return "Cell phone number incorrectly formatted or does not contain international code; please the number and try again.";
        for(User u : users)
            if(u.username.equals(username))
                return "Username already exists. Please choose a different username.";
        users.add(new User(firstname, surname, username, password, phoneNumber));
        return "\nUsername successfully captured.\nPassword successfully captured.\nCell phone number successfully added.";
    }

    //Checks login info/input to registered users
    public Boolean loginUser(String username, String password){
        for(User u : users){
            if(u.username.equals(username)&& u.password.equals(password)){
                loginMessage = "Welcome " + u.firstName + " " + u.surname + ", it is great to see you.";
                return true;
            }
        }
        loginMessage = "Username/password incorrect, or user name has not registeredye. Please try again";
        return false;
    }

    public String returnLoginStatus(){
        return loginMessage;
    }
}
