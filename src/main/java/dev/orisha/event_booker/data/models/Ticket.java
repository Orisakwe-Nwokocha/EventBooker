package dev.orisha.event_booker.data.models;

import dev.orisha.event_booker.data.constants.Type;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.GenerationType.IDENTITY;
import static java.time.LocalDateTime.now;
import static lombok.AccessLevel.NONE;

@Entity
@Table(name = "tickets")
@Getter
@Setter
public class Ticket {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private BigDecimal price;
    private BigDecimal discount;
    private Integer availableTickets;

    @ElementCollection(fetch = EAGER)
    private List<String> buyerEmails = new ArrayList<>();

    @Enumerated(STRING)
    private Type type;

    @ManyToOne
    private Event event;

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
