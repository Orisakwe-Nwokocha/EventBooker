package dev.orisha.event_booker.dtos.requests;

import dev.orisha.event_booker.data.constants.Type;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AddTicketRequest {
    private Long eventId;
    private BigDecimal price;
    private BigDecimal discount;
    private int availableTickets;
    private Type type;
}
