package org.example;

import org.example.services.AddRoomAndFloor;
import org.hibernate.Session;

import java.util.Scanner;

public class RoomAndFloorManagement {
    public void manageRoomAndFloor(Scanner sc, Session session){
        while (true) {
            System.out.print("""
                    1. Add new floor & rooms
                    2. Update available floor
                    3. Update prices of rooms
                    4. back
                    """);
            System.out.print("Enter Number: ");
            int input = sc.nextInt();
            if(input==1){
                AddRoomAndFloor addRoomAndFloor = new AddRoomAndFloor();
                addRoomAndFloor.checkExistingPriceOfRoom(session, sc);
            }else {
                break;
            }
        }
    }
}
