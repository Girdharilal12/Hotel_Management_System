package org.example;

import org.example.constant.Gender;
import org.example.constant.RoomType;
import org.example.entity.CustomerRecords;
import org.example.entity.ReservationDetails;
import org.example.entity.RoomCategory;
import org.example.entity.RoomDetail;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RoomReservation {
    InputValidator inputValidator = new InputValidator();
    public CustomerRecords checkExistingCustomer(Session session, Scanner sc, String id){
        Query query = session.createQuery("From CustomerRecords where idCard =: id");
        query.setParameter("id", id);
        CustomerRecords customerRecords = (CustomerRecords) query.uniqueResult();
        return customerRecords;
    }
    public CustomerRecords getCustomerDetail(Session session, Scanner sc, String id){
        if(id == null) {
            id = inputValidator.extractIDCardNumber(sc);
        }
        CustomerRecords customerRecords = checkExistingCustomer(session, sc, id);
        if(customerRecords == null) {
            Transaction tx = session.beginTransaction();
            customerRecords = new CustomerRecords();
            String fullName = inputValidator.extractFullName(sc);
            Gender gender = inputValidator.getGender(sc);
            customerRecords.setFullName(fullName);
            customerRecords.setGender(gender);
            customerRecords.setIdCard(id);
            session.save(customerRecords);
            tx.commit();
        }
        return customerRecords;
    }
    public void inputReservationRoom(Session session, Scanner sc, String id){
        CustomerRecords customerRecords = getCustomerDetail(session, sc, id);
        List<RoomDetail> list = listOfRooms(session, id, sc);
        int rooms = inputValidator.inputNumberOfRooms(sc);
        int days = inputValidator.inputDaysToBook(sc);
        String checkIn = inputValidator.checkIn(sc);
        String checkOut = inputValidator.checkOut(checkIn, sc, days);
        checkAvailability(list, session, checkIn, checkOut, customerRecords, sc, rooms);
    }
    public void checkAvailability(List<RoomDetail> list, Session session, String checkIn, String checkOut, CustomerRecords customerRecords, Scanner sc, int rooms){
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        LocalDateTime checkInTime = LocalDateTime.parse(checkIn, myFormatObj);
        LocalDateTime checkOutTime = LocalDateTime.parse(checkOut, myFormatObj);
        List<RoomDetail> arrayRoomDetail = new ArrayList<>();
        for(RoomDetail roomDetail : list){
            boolean isValid = true;
            Query query = session.createSQLQuery("Select * from reservation_details where room_id = '"+roomDetail.getId()+"'")
                    .addEntity(ReservationDetails.class);
            List<ReservationDetails> reservationDetailsList = query.list();
            for(ReservationDetails reservationDetails : reservationDetailsList) {
                LocalDateTime tableCheckInTime = LocalDateTime.parse(reservationDetails.getCheckIn(), myFormatObj);
                LocalDateTime tableCheckOutTime = LocalDateTime.parse(reservationDetails.getCheckOut(), myFormatObj);
                if(!(checkOutTime.isBefore(tableCheckInTime) || checkInTime.isAfter(tableCheckOutTime))) {
                    isValid = false;
                }
            }
            if(isValid && arrayRoomDetail.size() < rooms){
                arrayRoomDetail.add(roomDetail);
            }
        }
        if(arrayRoomDetail.size() == rooms){
            setReservationDetails(sc ,session, arrayRoomDetail, checkIn, checkOut, customerRecords);
        }else {
            System.out.println("Room is not available");
        }
    }
    public List<RoomDetail> listOfRooms(Session session, String id, Scanner sc){
        RoomType roomType = inputValidator.getRoomType(sc);
        RoomCategory roomCategory = getRoomCategory(roomType, session);
        Query query = session.createSQLQuery("SELECT * FROM room_detail WHERE category_id = :categoryId")
                .addEntity(RoomDetail.class)
                .setParameter("categoryId", roomCategory.getId());
        List<RoomDetail> list = query.list();
        if(list.isEmpty()){
            System.out.println(roomCategory.getRoomType() +" is not available");
            inputReservationRoom(session, sc, id);
            return null;
        }
        return list;
    }
    public void setReservationDetails(Scanner sc ,Session session, List<RoomDetail> arrayRoomDetail, String checkIn, String checkOut, CustomerRecords customerRecords){
        ReservationDetails reservationDetails;
        for(RoomDetail roomDetail: arrayRoomDetail){
            Transaction tx = session.beginTransaction();
            reservationDetails = new ReservationDetails();
            reservationDetails.setCheckIn(checkIn);
            reservationDetails.setCheckOut(checkOut);
            reservationDetails.setRoomDetail(roomDetail);
            reservationDetails.setCustomerRecords(customerRecords);
            session.save(reservationDetails);
            tx.commit();
        }
        System.out.println("Room book successfully");
        askToBookAnotherRoom(session, sc, customerRecords);
    }
    public void askToBookAnotherRoom(Session session, Scanner sc, CustomerRecords customerRecords){
        System.out.print("Do you want book another room (y/n): ");
        char input = sc.next().charAt(0);
        if(input=='y'){
            String id = customerRecords.getIdCard();
            inputReservationRoom(session, sc, id);
        }
    }
    public RoomCategory getRoomCategory( RoomType roomType, Session session){
        Query query =session.createQuery("From RoomCategory where roomType =: type");
        query.setParameter("type",roomType);
        return (RoomCategory) query.uniqueResult();
    }
}
