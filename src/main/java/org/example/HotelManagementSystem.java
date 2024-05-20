package org.example;

import org.example.Management.RoomAndFloorManagement;
import org.hibernate.Session;
import java.util.InputMismatchException;
import java.util.Scanner;

public class HotelManagementSystem {
    public void runHotelManagementSystem(Session session, Scanner sc){
        while (true){
            System.out.print("""
                    Welcome to the Hotel Management System
                    1. Floor & Room setting
                    2. Reservation
                    """);
            System.out.print("Enter number: ");
            try {
                int num = sc.nextInt();
                if(num==1){
                    RoomAndFloorManagement roomAndFloorManagement = new RoomAndFloorManagement();
                    roomAndFloorManagement.manageRoomAndFloor(sc, session);
                } else if(num==2) {
                    RoomReservation roomReservation = new RoomReservation();
                    roomReservation.inputReservationRoom(session, sc, null);
                } else {
                    break;
                }
            }catch(InputMismatchException e){
                break;
            }
        }
    }
}
