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
    public void processRoomReservation(Session session, Scanner sc, String id){
        CustomerRecords customerRecords = getCustomerDetail(session, sc, id);
        RoomType roomType = inputValidator.getRoomType(sc);
        int rooms = inputValidator.inputNumberOfRooms(sc);
        int days = inputValidator.inputDaysToBook(sc);
        String checkIn = inputValidator.checkIn(sc);
        String checkOut = inputValidator.checkOut(checkIn, sc, days);
        RoomCategory roomCategory = getRoomCategory(roomType, session);
        List<RoomDetail> availableRooms = findRooms(roomCategory ,session);
        if (availableRooms.isEmpty()) {
            System.out.println(roomCategory.getRoomType() +" not exist in hotel");
        }else {
            setReservationDetails(availableRooms, sc , session, checkIn, checkOut, customerRecords, rooms);
        }
    }
    public CustomerRecords getCustomerDetail(Session session, Scanner sc, String id){
        if(id == null) {
            id = inputValidator.extractIDCardNumber(sc);
        }
        CustomerRecords customerRecords = checkExistingCustomer(session, id);
        if(customerRecords == null) {
            customerRecords = createNewCustomer(session, sc, id);
        }
        return customerRecords;
    }
    public CustomerRecords checkExistingCustomer(Session session, String id){
        Query query = session.createQuery("FROM CustomerRecords WHERE idCard = :id");
        query.setParameter("id", id);
        CustomerRecords customerRecords = (CustomerRecords) query.uniqueResult();
        return customerRecords;
    }
    public CustomerRecords createNewCustomer(Session session, Scanner sc, String id) {
        Transaction tx = session.beginTransaction();
        CustomerRecords customerRecords = new CustomerRecords();
        String fullName = inputValidator.extractFullName(sc);
        Gender gender = inputValidator.getGender(sc);
        customerRecords.setFullName(fullName);
        customerRecords.setGender(gender);
        customerRecords.setIdCard(id);
        session.save(customerRecords);
        tx.commit();
        return customerRecords;
    }
    public List<RoomDetail> findRooms(RoomCategory roomCategory, Session session){
        Query query = session.createSQLQuery("SELECT * FROM room_detail WHERE category_id = :categoryId")
                .addEntity(RoomDetail.class)
                .setParameter("categoryId", roomCategory.getId());
        List<RoomDetail> availableRooms = query.list();
        return availableRooms;
    }
    public void setReservationDetails(List<RoomDetail> list,Scanner sc ,Session session, String checkIn, String checkOut, CustomerRecords customerRecords, int rooms){
        List<RoomDetail> arrayRoomDetail = checkAvailability(list, session, checkIn, checkOut, customerRecords, sc, rooms);
        if(arrayRoomDetail.size() == rooms){
            for(RoomDetail roomDetail: arrayRoomDetail){
                Transaction tx = session.beginTransaction();
                ReservationDetails reservationDetails = new ReservationDetails();
                reservationDetails.setCheckIn(checkIn);
                reservationDetails.setCheckOut(checkOut);
                reservationDetails.setRoomDetail(roomDetail);
                reservationDetails.setCustomerRecords(customerRecords);
                session.save(reservationDetails);
                tx.commit();
            }
            System.out.println("Room book successfully");
        }else {
            System.out.println("Room is not available");
        }
        askToBookAnotherRoom(session, sc, customerRecords);
    }
    public List<RoomDetail> checkAvailability(List<RoomDetail> list, Session session, String checkIn, String checkOut, CustomerRecords customerRecords, Scanner sc, int rooms){
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        LocalDateTime checkInTime = LocalDateTime.parse(checkIn, myFormatObj);
        LocalDateTime checkOutTime = LocalDateTime.parse(checkOut, myFormatObj);
        List<RoomDetail> arrayRoomDetail = new ArrayList<>();
        for(RoomDetail roomDetail : list){
            if(isRoomAvailable(session, checkInTime, checkOutTime, roomDetail, myFormatObj) && arrayRoomDetail.size() < rooms){
                arrayRoomDetail.add(roomDetail);
            }
        }
        return arrayRoomDetail;
    }
    public boolean isRoomAvailable(Session session, LocalDateTime checkInTime, LocalDateTime checkOutTime, RoomDetail roomDetail, DateTimeFormatter myFormatObj){
        Query query = session.createSQLQuery("Select * from reservation_details where room_id = '"+roomDetail.getId()+"'")
                .addEntity(ReservationDetails.class);
        List<ReservationDetails> reservationDetailsList = query.list();
        for(ReservationDetails reservationDetails : reservationDetailsList) {
            LocalDateTime tableCheckInTime = LocalDateTime.parse(reservationDetails.getCheckIn(), myFormatObj);
            LocalDateTime tableCheckOutTime = LocalDateTime.parse(reservationDetails.getCheckOut(), myFormatObj);
            if(!(checkOutTime.isBefore(tableCheckInTime) || checkInTime.isAfter(tableCheckOutTime))) {
                return  false;
            }
        }
        return true;
    }
    public void askToBookAnotherRoom(Session session, Scanner sc, CustomerRecords customerRecords){
        System.out.print("Do you want book another room (y/n): ");
        char input = sc.next().charAt(0);
        if(input=='y'){
            String id = customerRecords.getIdCard();
            processRoomReservation(session, sc, id);
        }
    }
    public RoomCategory getRoomCategory( RoomType roomType, Session session){
        Query query =session.createQuery("From RoomCategory where roomType =: type");
        query.setParameter("type",roomType);
        return (RoomCategory) query.uniqueResult();
    }
}
