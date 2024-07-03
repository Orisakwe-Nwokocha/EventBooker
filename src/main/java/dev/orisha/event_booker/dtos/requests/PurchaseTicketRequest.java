package dev.orisha.event_booker.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseTicketRequest {
    private Long ticketId;
    private String name;
    private String email;
    private String cardNumber;
    private int numberOfTickets;
}
