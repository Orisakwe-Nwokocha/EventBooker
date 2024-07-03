package dev.orisha.event_booker.dtos.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.orisha.event_booker.data.constants.Type;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class AddTicketResponse {
    @JsonProperty("ticket_id")
    private Long id;
    private Long eventId;
    private BigDecimal price;
    private BigDecimal discount;
    private Type type;
    @JsonFormat(pattern = "dd-MMMM-yyyy 'at' hh:mm a")
    private LocalDateTime dateCreated;
}
