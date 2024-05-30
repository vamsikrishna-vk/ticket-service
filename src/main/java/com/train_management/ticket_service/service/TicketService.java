package com.train_management.ticket_service.service;


import com.train_management.ticket_service.exception.SeatNotAvailableException;
import com.train_management.ticket_service.exception.TicketNotFoundException;
import com.train_management.ticket_service.model.Ticket;
import com.train_management.ticket_service.repository.TicketRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Service
public class TicketService {
    @Autowired
    TicketRepository ticketRepository;

    public static final int SECTION_SIZE = 5;
    public static final int NUM_SECTIONS = 2;
    public static final int TRAIN_CAPACITY = SECTION_SIZE * NUM_SECTIONS;

    /**
     * Allocates Seat,Section and saves the Ticket to ticket table.
     */
    public Ticket purchaseTicket(Ticket ticket){

        ticket.setSeatNo(allocateSeat());
        ticket.setSection(generateSectionStr(ticket.getSeatNo()));

        return ticketRepository.save(ticket);
    }

    /**
     * Retrieve's ticket from DB by id.
     */
    public Ticket viewTicket(Long id) throws TicketNotFoundException{
        return ticketRepository.findById(id).orElseThrow(()-> new TicketNotFoundException("Ticket not found with id "+id));
    }

    /**
     * Retrieve's tickets from DB by section.
     */
    public List<Ticket> ticketsBySection(String section){
        return ticketRepository.findBySection(section);
    }

    /**
     * remove's a ticket by the ticket id.
     */
    public void cancelTicket(Long id){
        ticketRepository.deleteById(id);
    }

    /**
     * Returns a list of available seats.
     */
    public List<Integer> availableSeats(){

        List<Integer> bookedSeats = ticketRepository.findAllSeatNo();
        List<Integer> availableSeats = IntStream.iterate(1, n -> n + 1)
                .limit(TRAIN_CAPACITY)
                .filter(n -> !bookedSeats.contains(n))
                .boxed()
                .collect(Collectors.toList());

        return availableSeats;
    }

    /**
     * Returns next available seat.
     */
    public Integer allocateSeat() throws SeatNotAvailableException{

        if(availableSeats().isEmpty()){
            throw new SeatNotAvailableException("No seat left to allocate. Train is full");
        }

        return  availableSeats().get(0);
    }

    /**
     * Modifies the seatNo and changes the section according to the seatNo.
     */
    @Transactional
    public void modifySeat(Integer seatNo,long ticketId) throws TicketNotFoundException,SeatNotAvailableException{

        if(availableSeats().contains(seatNo)){
            int updateSeatNo = ticketRepository.updateSeatNo(ticketId,seatNo);
            if(updateSeatNo==0){
                throw new TicketNotFoundException("Ticket not found with id "+ticketId);
            }
            String sectionName = generateSectionStr(seatNo);
            ticketRepository.updateSection(ticketId,sectionName);
        }
        else {
            throw new SeatNotAvailableException("Seat "+seatNo+"is already booked");
        }
    }

    /**
     * Returns Section String based on seatNo
     */
    public String generateSectionStr(int seatNo){

        int sectionNumber = (seatNo-1) / SECTION_SIZE;

        return "Section " + (char) ('A' + sectionNumber);
    }
}
