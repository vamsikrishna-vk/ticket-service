package com.train_management.ticket_service.controller;

import com.train_management.ticket_service.model.Ticket;
import com.train_management.ticket_service.service.TicketService;
import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class TicketController {

    @Autowired
    TicketService ticketService;

    @PostMapping("/purchase")
    public Ticket purchaseTicket(@RequestBody Ticket ticket){
        return ticketService.purchaseTicket(ticket);
    }

    @GetMapping("/ticket/{id}")
    public Ticket viewTicket(@PathVariable Long id){
        return ticketService.viewTicket(id);
    }

    @GetMapping("tickets/{section}")
    public List<Ticket> ticketsBySection(@PathVariable String section){
        return  ticketService.ticketsBySection(section);
    }

    @DeleteMapping("cancel/{id}")
    public Map<String,String> cancelTicket(@PathVariable Long id){
        ticketService.cancelTicket(id);
        var response = new HashMap<String,String>();
        response.put("message","Ticket "+id+" Cancelled Successfully.");
        return response;
    }

    @GetMapping("availableSeats")
    public List<Integer> availableSeats(){
        return ticketService.availableSeats();
    }

    @PutMapping("updateSeat")
    public Map<String,String> updateSeat(@RequestParam Long ticketId, @RequestParam Integer seatNo){
        ticketService.modifySeat(seatNo,ticketId);
        var response = new HashMap<String,String>();
        response.put("message","seat updated Successfully.");
        return response;
    }


}
