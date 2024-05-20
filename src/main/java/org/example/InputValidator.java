package org.example;

import org.example.constant.Gender;
import org.example.constant.RoomType;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.LocalDateTime;
public class InputValidator {
    public String extractIDCardNumber(Scanner sc){
        Matcher matcher;
        do{
            System.out.print("Please enter your ID card number in the format XXXXX-XXXXXXX-X: ");
            String id = sc.next();
            Pattern pattern = Pattern.compile("\\d{5}-\\d{7}-\\d");
            matcher = pattern.matcher(id);
            if(!matcher.find()){
                System.out.println("Please enter again valid id card number");
            }else {
                break;
            }
        }while (true);
        return matcher.group();
    }
    public String extractFullName(Scanner sc){
        Matcher matcher;
        do {
            System.out.print("Enter your Full-name: ");
            String name = sc.next();
            Pattern pattern = Pattern.compile("^[A-Za-z]{3,20}-[A-Za-z]{3,20}$");
            matcher = pattern.matcher(name);
            if(!matcher.find()){
                System.out.println("Please enter again valid Name");
            }else {
                break;
            }
        }while (true);
        return matcher.group();
    }
    public Gender getGender(Scanner sc){
        Gender gender = null;
        do {
            System.out.print("Enter gender: ");
            String input = sc.next();
            try {
                gender = Gender.valueOf(input.toUpperCase());
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Enter again valid gender");
            }
        }while(true);
        return gender;
    }
    public RoomType getRoomType(Scanner sc){
        RoomType roomType;
        do{
            System.out.print("""
                   STANDARD,
                   MODERATE,
                   SUPERIOR,
                   JUNIOR_SUITE,
                   SUITE
                    """);
            System.out.print("Enter Room Type here: ");
            String input = sc.next();
            try {
                roomType = RoomType.valueOf(input.toUpperCase());
                break;
            }catch(IllegalArgumentException e){
                System.out.println("Enter again valid Room type");
            }
        }while (true);
        return roomType;
    }
    public String checkIn(Scanner sc){
        String input, time;
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        do{
            System.out.print("Please enter the check-in date (dd-MM-yyyy): ");
            input = sc.next();
            try {
                LocalDate date = LocalDate.parse(input, myFormatObj);
                break;
            }catch(DateTimeException e){
                System.out.println(e.getMessage() + " Enter again");
            }
        }while (true);
        time = input+" "+checkInHour(sc);
        return  time;
    }
    public String checkInHour(Scanner sc){
        int hour;
        String time;
        do {
            System.out.print("Please enter the hour in 24-hour format (0-23): ");
            hour = sc.nextInt();
            if (hour > 0 && hour < 23){
                break;
            }else {
                System.out.println("Enter again valid hour in 24-hour format");
            }
        }while (true);
        if(hour<10){
            time = "0"+hour+":00:00";
        }else {
            time = hour+":00:00";
        }
        return time;
    }
    public String checkOut(String checkIn, Scanner sc, int days){
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(checkIn, myFormatObj);
        LocalDateTime modifiedDateTime = dateTime.plusDays(days);
        return (String) modifiedDateTime.format(myFormatObj);
    }
    public int inputDaysToBook(Scanner sc){
        int days;
        do {
            try {
                System.out.print("How many days do you want to book the room for: ");
                days = sc.nextInt();
                if(days>0){
                    break;
                }else {
                    System.out.println("Enter a valid number of days (greater than 0).");
                }
            }catch (Exception e){
                System.out.println("Enter again valid days");
                sc.next();
            }
        }while (true);
        return days;
    }
    public int inputNumberOfRooms(Scanner sc){
        int rooms;
        do {
            try {
                System.out.print("How many rooms do you want to book: ");
                rooms = sc.nextInt();
                if(rooms>0){
                    break;
                }else {
                    System.out.println("Enter a valid number of rooms (greater than 0).");
                }
            }catch (Exception e){
                System.out.println("Enter again valid number of rooms");
                sc.next();
            }
        }while (true);
        return rooms;
    }
}