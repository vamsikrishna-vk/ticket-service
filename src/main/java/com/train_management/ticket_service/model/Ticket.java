package com.train_management.ticket_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long TicketId;

    private User user;

    private String fromLocation;

    private String toLocation;

    private float pricePaid;

    private int seatNo;

    private String section;

    public Ticket(User user, String fromLocation, String toLocation, float pricePaid, int seatNo, String section) {
        this.user = user;
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
        this.pricePaid = pricePaid;
        this.seatNo = seatNo;
        this.section = section;
    }

    public String getFrom() {
        return fromLocation;
    }

    public void setFrom(String from) {
        this.fromLocation = from;
    }

    public String getToLocation() {
        return toLocation;
    }

    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
    }

    public float getPricePaid() {
        return pricePaid;
    }

    public void setPricePaid(float pricePaid) {
        this.pricePaid = pricePaid;
    }

    public int getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(int seatNo) {
        this.seatNo = seatNo;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public Long getTicketId() {
        return TicketId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
