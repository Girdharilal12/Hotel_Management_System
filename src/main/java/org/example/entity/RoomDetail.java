package org.example.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "room_detail")
public class RoomDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id", nullable = false)
    private int RoomId;
    @Column(name = "floor", nullable = false)
    private int floor;
    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private RoomCategory roomCategory;
    @Column(name = "available", nullable = false)
    private boolean available;
    public int getId() {
        return RoomId;
    }
    public int getFloor() {
        return floor;
    }
    public RoomCategory getRoom() {
        return roomCategory;
    }
    public boolean isAvailable() {
        return available;
    }
    public void setId(int id) {
        this.RoomId = id;
    }
    public void setFloor(int floor) {
        this.floor = floor;
    }
    public void setRoom(RoomCategory roomCategory) {
        this.roomCategory = roomCategory;
    }
    public void setAvailable(boolean available) {
        this.available = available;
    }
}
