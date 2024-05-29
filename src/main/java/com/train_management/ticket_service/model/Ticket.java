package com.train_management.ticket_service.model;

import jakarta.persistence.*;

@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long TicketId;

    @ManyToOne(cascade = CascadeType.ALL)
    private User user;

    private String fromLocation;

    private String toLocation;

    private Float pricePaid;

    private Long seatNo;

    private String section;

    public Ticket(User user, String fromLocation, String toLocation, Float pricePaid, Long seatNo, String section) {
        this.user = user;
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
        this.pricePaid = pricePaid;
        this.seatNo = seatNo;
        this.section = section;
    }

    public Ticket(){}

    public String getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(String from) {
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

    public Long getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(Long seatNo) {
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
