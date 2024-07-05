package dev.orisha.event_booker.dtos.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReserveTicketResponse {
    @JsonProperty("reserved_ticket_id")
    private Long id;
    private Long ticketId;
    @JsonProperty("numberOfTicketsReserved")
    private int quantity;
    @JsonFormat(pattern = "dd-MMMM-yyyy 'at' hh:mm a")
    private LocalDateTime expiryDate;
}
