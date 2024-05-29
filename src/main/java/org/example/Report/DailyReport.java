package org.example.Report;

import org.example.Entity.CustomerRecords;
import org.hibernate.Session;
import org.hibernate.query.Query;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DailyReport {
    public void todayCheckIn(Session session){
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate todayDate = LocalDate.now();
        String date = (String) todayDate.format(myFormatObj);
        Query query = session.createSQLQuery("SELECT * FROM reservation_details r INNER JOIN customer_records c ON r.customer_id = c.id WHERE r.check_in like '%"+date+"%';")
                .addEntity(CustomerRecords.class);
        List<CustomerRecords> list = query.list();
        System.out.println("Full Name : Gender : Id Card");
        for(CustomerRecords customerRecords : list){
            System.out.println(customerRecords.getFullName() +" : "+ customerRecords.getGender() +" : "+ customerRecords.getIdCard());
        }
    }
    public void todayCheckOut(Session session){
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate todayDate = LocalDate.now();
        String date = (String) todayDate.format(myFormatObj);
        Query query = session.createSQLQuery("SELECT * FROM reservation_details r INNER JOIN customer_records c ON r.customer_id = c.id WHERE r.check_out like '%"+date+"%';")
                .addEntity(CustomerRecords.class);
        List<CustomerRecords> list = query.list();
        System.out.println("Full Name : Gender : Id Card");
        for(CustomerRecords customerRecords : list){
            System.out.println(customerRecords.getFullName() +" : "+ customerRecords.getGender() +" : "+ customerRecords.getIdCard());
        }
    }
}
