package dev.orisha.event_booker.data.repositories;

import dev.orisha.event_booker.data.models.Organizer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizerRepository extends JpaRepository<Organizer, Long> {
    boolean existsByUsername(String username);
}
