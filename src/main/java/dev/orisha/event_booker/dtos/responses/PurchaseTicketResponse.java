package dev.orisha.event_booker.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseTicketResponse {
    private int numberOfTickets;
    private BigDecimal purchaseAmount;
    private String message;
}
