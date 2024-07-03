package dev.orisha.event_booker.data.repositories;

import dev.orisha.event_booker.data.models.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Query("SELECT t FROM Ticket t WHERE t.event.id=:eventId")
    List<Ticket> findAllTicketsForEvent(Long eventId);
}
