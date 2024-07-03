package dev.orisha.event_booker.data.repositories;

import dev.orisha.event_booker.data.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("SELECT e FROM Event e WHERE e.organizer.id=:organizerId")
    List<Event> findAllEventsForOrganizer(Long organizerId);
}
