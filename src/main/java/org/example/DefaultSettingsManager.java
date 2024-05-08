package org.example;

import org.example.constant.RoomType;
import org.example.entity.RoomCategory;
import org.example.entity.RoomDetail;
import org.example.Management.RoomAndFloorManagement;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;
import java.util.Scanner;

public class DefaultSettingsManager {
    public void checkExistingDetails(Session session, Scanner sc) {
        Query query = session.createQuery("From RoomCategory");
        List<RoomDetail> list = query.list();
        if(list.isEmpty()){
            System.out.print("Do you want go with predefined floor & room plan (y/n): ");
            char input = sc.next().charAt(0);
            char lowerCaseInput = Character.toLowerCase(input);
            if(lowerCaseInput=='y'){
                setDefaultSettings(session);
            }else if(lowerCaseInput=='n') {
                RoomAndFloorManagement roomAndFloorManagement = new RoomAndFloorManagement();
                roomAndFloorManagement.manageRoomAndFloor(sc, session);
            }else {
                System.out.println("Invalid input. Enter again");
                checkExistingDetails(session,sc);
            }
        }
    }
    public void setDefaultSettings(Session session){
        Transaction tx = session.beginTransaction();
        RoomType[] roomTypes ={RoomType.STANDARD,RoomType.MODERATE,RoomType.SUPERIOR,RoomType.JUNIOR_SUITE,RoomType.SUITE};
        float[] price = {3000,5000,10000,20000,50000};
        int floor = 0;
        for(int i=0;i<5;i++){
            RoomCategory roomCategory = new RoomCategory();
            roomCategory.setRoomType(roomTypes[i]);
            roomCategory.setPrice(price[i]);
            session.save(roomCategory);
            floor++;
            for(int j=0;j<10;j++){
                RoomDetail roomDetail = new RoomDetail();
                roomDetail.setFloor(floor);
                roomDetail.setRoom(roomCategory);
                roomDetail.setAvailable(true);
                session.save(roomDetail);
            }
        }
        tx.commit();
    }
}
