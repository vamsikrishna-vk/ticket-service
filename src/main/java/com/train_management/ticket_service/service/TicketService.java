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

    public static final int SECTION_SIZE = 5;
    public static final int NUM_SECTIONS = 2;
    public static final int TRAIN_CAPACITY = SECTION_SIZE * NUM_SECTIONS;

    public Ticket purchaseTicket(Ticket ticket){

        ticket.setSeatNo(allocateSeat());
        ticket.setSection(generateSectionStr(ticket.getSeatNo()));

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
                .limit(TRAIN_CAPACITY)
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

            int updateSeatNo = ticketRepository.updateSeatNo(ticketId,seatNo);
            if(updateSeatNo==0){
                throw new CustomException("Ticket not found");
            }
            String sectionName = generateSectionStr(seatNo);
            ticketRepository.updateSection(ticketId,sectionName);
        }
        else {
            throw new CustomException("seat already booked");
        }
    }

    public String generateSectionStr(int seatNo){
        int sectionNumber = (seatNo-1) / SECTION_SIZE;
        return "Section " + (char) ('A' + sectionNumber);
    }
}
