package dev.orisha.event_booker.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReserveTicketRequest {
    private Long ticketId;
    private String email;
    @JsonProperty("numberOfTickets")
    private int quantity;
}
