package dev.orisha.event_booker.data.repositories;

import dev.orisha.event_booker.data.models.ReservedTicket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservedTicketRepository extends JpaRepository<ReservedTicket, Long> {

}
