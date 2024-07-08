package dev.orisha.event_booker.data.repositories;

import dev.orisha.event_booker.data.models.ReservedTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReservedTicketRepository extends JpaRepository<ReservedTicket, Long> {
    @Query("SELECT r FROM ReservedTicket r WHERE r.ticket.id=:ticketId")
    List<ReservedTicket> findReservedTicketsFor(Long ticketId);
}
