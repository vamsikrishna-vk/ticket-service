package com.train_management.ticket_service.service;

import com.train_management.ticket_service.exception.CustomException;
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

    public static final int sectionSize = 5;

    public Ticket purchaseTicket(Ticket ticket){
            ticket.setSection(allocateSection());
            ticket.setSeatNo(Long.valueOf(allocateSeat()));
        return ticketRepository.save(ticket);
    }

    public Ticket viewTicket(Long id){
        return ticketRepository.findById(id).orElseThrow(()-> new CustomException("ticket not found"));
    }

    public List<Ticket> ticketsBySection(String section){
        return ticketRepository.findBySection(section);
    }
    public void cancelTicket(Long id){
        ticketRepository.deleteById(id);
    }

    public List<Integer> availableSeats(){
        List<Integer> bookedSeats = ticketRepository.findAllSeatNo();
        List<Integer> availableSeats = IntStream.iterate(1, n -> n + 1)
                .limit(10)
                .filter(n -> !bookedSeats.contains(n))
                .boxed()
                .collect(Collectors.toList());
        return availableSeats;
    }

    public Integer allocateSeat(){
        if(availableSeats().isEmpty()){
            throw new CustomException("No seat left to allocate");
        }
        return  availableSeats().get(0);
    }
    @Transactional
    public void modifySeat(Integer seatNo,long ticketId){
        if(availableSeats().contains(seatNo)){
            ticketRepository.updateSeatNo(ticketId,seatNo);
            if(seatNo<=sectionSize){
                ticketRepository.updateSection(ticketId,"Section A");
                return;
            }
            ticketRepository.updateSection(ticketId,"Section B");
        }
        else {
            throw new CustomException("seat already booked");
        }
    }

    public String allocateSection() {

        long ticketsSold = ticketRepository.count();

        if(ticketsSold < sectionSize){
            return "Section A";
        }
        else if(ticketsSold >= sectionSize && ticketsSold < sectionSize*2){
            return "Section B";
        }else{
            throw new CustomException("Train is full");
        }
    }
}
