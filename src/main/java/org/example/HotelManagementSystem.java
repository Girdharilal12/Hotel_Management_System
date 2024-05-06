package org.example;

import org.hibernate.Session;
import java.util.Scanner;

public class HotelManagementSystem {
    public void runHotelManagementSystem(Session session, Scanner sc){
        while (true){
            System.out.print("""
                    Welcome to the Hotel Management System
                    1. Floor & Room setting
                    """);
            System.out.print("Enter number: ");
            int num = sc.nextInt();
            if(num==1){
                RoomAndFloorManagement roomAndFloorManagement = new RoomAndFloorManagement();
                roomAndFloorManagement.manageRoomAndFloor(sc, session);
            }else {
                break;
            }
        }
    }
}
