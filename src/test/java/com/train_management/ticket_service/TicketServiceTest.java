package com.train_management.ticket_service;

import com.train_management.ticket_service.exception.CustomException;
import com.train_management.ticket_service.repository.TicketRepository;
import com.train_management.ticket_service.service.TicketService;
import com.train_management.ticket_service.model.Ticket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class TicketServiceTest {
    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketService ticketService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPurchaseTicket() {
        Ticket ticket = new Ticket();

        when(ticketRepository.save(ticket)).thenReturn(ticket);
        when(ticketRepository.count()).thenReturn(4L);
        when(ticketRepository.findAllSeatNo()).thenReturn(new ArrayList<>(Arrays.asList(3,4,5)));
        Ticket result= ticketService.purchaseTicket(ticket);
        assertEquals(1,ticket.getSeatNo());
        assertEquals("Section A",ticket.getSection());
        assertNotNull(ticketService.purchaseTicket(ticket));
    }


    @Test
    void testAvailableSeats() {
        when(ticketRepository.findAllSeatNo()).thenReturn(Arrays.asList(3,4,5));
        assertEquals(Arrays.asList(1,2,6,7,8,9,10), ticketService.availableSeats());
    }

    @Test
    void testAllocateSeat() {
        when(ticketRepository.findAllSeatNo()).thenReturn(IntStream.iterate(1, n -> n + 1)
                .limit(10).boxed().collect(Collectors.toList()));
        assertThrows(CustomException.class, () -> ticketService.allocateSeat());
        when(ticketRepository.findAllSeatNo()).thenReturn(new ArrayList<>(Arrays.asList(3,4,5)));
        assertEquals(1,ticketService.allocateSeat());
    }


    @Test
    void testAllocateSection() {
        when(ticketRepository.count()).thenReturn(4L);
        assertEquals("Section A", ticketService.allocateSection());

        when(ticketRepository.count()).thenReturn(5L);
        assertEquals("Section B", ticketService.allocateSection());

        when(ticketRepository.count()).thenReturn(12L);
        assertThrows(CustomException.class, () -> ticketService.allocateSection());
    }

}
