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
    @Column(name = "id", nullable = false, unique = true)
    private int id;
    @Column(name = "floor", nullable = false)
    private int floor;
    @ManyToOne
    @JoinColumn(name = "room_id", referencedColumnName = "id")
    private Room room;
    @Column(name = "available", nullable = false)
    private boolean available;
    public int getId() {
        return id;
    }
    public int getFloor() {
        return floor;
    }
    public Room getRoom() {
        return room;
    }
    public boolean isAvailable() {
        return available;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setFloor(int floor) {
        this.floor = floor;
    }
    public void setRoom(Room room) {
        this.room = room;
    }
    public void setAvailable(boolean available) {
        this.available = available;
    }
}
