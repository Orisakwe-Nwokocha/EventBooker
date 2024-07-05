package dev.orisha.event_booker.data.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "reserved_tickets")
@Getter
@Setter
public class ReservedTicket {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String email;
    private Integer quantity;
    @ManyToOne(fetch = LAZY)
    private Ticket ticket;

}
