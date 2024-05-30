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
        when(ticketRepository.findAllSeatNo()).thenReturn(Arrays.asList(3,4,5));

        Ticket result= ticketService.purchaseTicket(ticket);

        assertNotNull(ticketService.purchaseTicket(result));

        assertEquals(1,result.getSeatNo());
        assertEquals("Section A",result.getSection());


    }


    @Test
    void testAvailableSeats() {
        List<Integer> bookedSeatsTest1 = Arrays.asList(3,4,5);
        List<Integer> availableSeats = IntStream.iterate(1,n -> n+1)
                .limit(TicketService.TRAIN_CAPACITY)
                .filter(x -> !bookedSeatsTest1.contains(x))
                .boxed()
                .collect(Collectors.toList());

        when(ticketRepository.findAllSeatNo()).thenReturn(bookedSeatsTest1);
        assertEquals(availableSeats, ticketService.availableSeats());

        List<Integer> bookedSeatsTest2 = Arrays.asList();
        availableSeats = IntStream.iterate(1,n -> n+1)
                .limit(TicketService.TRAIN_CAPACITY)
                .filter(x -> !bookedSeatsTest2.contains(x))
                .boxed()
                .collect(Collectors.toList());

        when(ticketRepository.findAllSeatNo()).thenReturn(Arrays.asList());

        assertEquals(availableSeats, ticketService.availableSeats());
    }

    @Test
    void testAllocateSeat() {
        when(ticketRepository.findAllSeatNo()).thenReturn(
                IntStream.iterate(1, n -> n + 1)
                .limit(TicketService.TRAIN_CAPACITY)
                .boxed()
                .collect(Collectors.toList()));
        assertThrows(CustomException.class, () -> ticketService.allocateSeat());

        when(ticketRepository.findAllSeatNo()).thenReturn(new ArrayList<>(Arrays.asList(3,4,5)));
        assertEquals(1,ticketService.allocateSeat());

        when(ticketRepository.findAllSeatNo()).thenReturn(new ArrayList<>(Arrays.asList(1,4,5)));
        assertEquals(2,ticketService.allocateSeat());
    }

    @Test
    void testGenerateSectionStr(){

        assertEquals("Section A",ticketService.generateSectionStr(5));
        assertEquals("Section A",ticketService.generateSectionStr(1));

        assertEquals("Section B",ticketService.generateSectionStr(6));
        assertEquals("Section B",ticketService.generateSectionStr(9));
    }


}
