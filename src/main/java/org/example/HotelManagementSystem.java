package org.example;

import org.example.management.RoomAndFloorManagement;
import org.example.service.ReservationServices;
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
                    ReservationServices reservationServices = new ReservationServices();
                    reservationServices.processRoomReservation(session, sc, null);
                } else {
                    break;
                }
            }catch(InputMismatchException e){
                break;
            }
        }
    }
}
