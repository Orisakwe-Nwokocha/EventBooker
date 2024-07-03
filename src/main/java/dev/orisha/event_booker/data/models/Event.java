package dev.orisha.event_booker.data.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.GenerationType.IDENTITY;
import static java.time.LocalDateTime.now;
import static lombok.AccessLevel.NONE;

@Entity
@Table(name = "events")
@Getter
@Setter
public class Event {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String title;
    private String description;
    private LocalDateTime eventDate;

    @ManyToOne
    private Organizer organizer;
    @ElementCollection(fetch = EAGER)
    private List<String> guests = new ArrayList<>();

    @Setter(NONE)
    private LocalDateTime dateCreated;
    @Setter(NONE)
    private LocalDateTime dateUpdated;

    @PrePersist
    private void setDateCreated() {
        dateCreated = now();
    }

    @PreUpdate
    private void setDateUpdated() {
        dateUpdated = now();
    }
}
