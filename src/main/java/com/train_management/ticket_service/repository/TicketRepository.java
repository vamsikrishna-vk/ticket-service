package com.train_management.ticket_service.repository;

import com.train_management.ticket_service.model.Ticket;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findBySection(String section);

    @Query("SELECT t.seatNo FROM Ticket t")
    List<Integer> findAllSeatNo();

    @Modifying
    @Transactional
    @Query("UPDATE Ticket t SET t.seatNo = :seatNo WHERE t.id = :ticketId")
    int updateSeatNo(Long ticketId, Integer seatNo);

    @Modifying
    @Transactional
    @Query("UPDATE Ticket t SET t.section = :section WHERE t.id = :ticketId")
    int updateSection(Long ticketId,String section);
}
