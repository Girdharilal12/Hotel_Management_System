package org.example.services;

import org.example.constant.RoomType;
import org.example.entity.Room;
import org.example.entity.RoomDetail;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.query.Query;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Scanner;

public class AddRoomAndFloor {
    public int getLastFloor(Session session){
        try {
            Query query = session.createSQLQuery("select max(floor) from room_detail");
            return (Integer) query.uniqueResult();
        }catch (PersistenceException e){
            return 0;
        }
    }
    public void addRoomsInFloor(Scanner sc, Session session){
        Transaction tx = session.beginTransaction();
        int floor = getLastFloor(session)+1;
        RoomType[] roomTypes ={RoomType.STANDARD,RoomType.MODERATE,RoomType.SUPERIOR,RoomType.JUNIOR_SUITE,RoomType.SUITE};
        for(int i=0;i<roomTypes.length;i++){
            System.out.print("Please add number of "+ roomTypes[i] +" rooms: ");
            int input = sc.nextInt();
            Room room = getRoom(roomTypes[i], session);
            for(int j=0;j<input;j++){
                RoomDetail roomDetail = new RoomDetail();
                roomDetail.setFloor(floor);
                roomDetail.setAvailable(true);
                roomDetail.setRoom(room);
                session.save(roomDetail);
            }
        }
        tx.commit();
    }
    public Room getRoom(RoomType roomType,Session session){
        Query query = session.createQuery("FROM Room WHERE roomType =: roomType");
        query.setParameter("roomType", roomType);
        return (Room) query.uniqueResult();
    }
    public void addRoomsPriceAndType(Session session, Scanner sc){
        Transaction tx = session.beginTransaction();
        RoomType[] roomTypes ={RoomType.STANDARD,RoomType.MODERATE,RoomType.SUPERIOR,RoomType.JUNIOR_SUITE,RoomType.SUITE};
        for(int i=0;i<roomTypes.length;i++){
            Room room = new Room();
            System.out.print("Enter "+ roomTypes[i] +" room price: ");
            float input = sc.nextFloat();
            room.setRoomType(roomTypes[i]);
            room.setPrice(input);
            session.save(room);
        }
        tx.commit();
    }
    public void checkExistingPriceOfRoom(Session session,Scanner sc){
        Query query = session.createQuery("From Room");
        List<RoomDetail> list = query.list();
        if(list.isEmpty()){
            addRoomsPriceAndType(session,sc);
        }
        addRoomsInFloor(sc, session);
    }
}
