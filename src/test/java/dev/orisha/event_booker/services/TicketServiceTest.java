package dev.orisha.event_booker.services;

import dev.orisha.event_booker.dtos.requests.PurchaseTicketRequest;
import dev.orisha.event_booker.dtos.responses.ApiResponse;
import dev.orisha.event_booker.dtos.responses.GetAllTicketsResponse;
import dev.orisha.event_booker.dtos.responses.PurchaseTicketResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.List;

import static dev.orisha.event_booker.utils.TestUtils.buildPurchaseTicketRequest;
import static java.math.BigDecimal.valueOf;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Sql(scripts = "/db/data.sql")
class TicketServiceTest {
    @Autowired
    private TicketService ticketService;


    @Test
    void purchaseTicketTest() {
        PurchaseTicketRequest request = buildPurchaseTicketRequest();
        ApiResponse<PurchaseTicketResponse> response = ticketService.purchaseTicket(request);
        assertThat(response).isNotNull();
        BigDecimal purchaseAmount = response.getData().getPurchaseAmount();
        assertThat(purchaseAmount).isEqualTo(new BigDecimal("62500.00"));
    }

    @Test
    public void getAllTicketsTest() {
        ApiResponse<List<GetAllTicketsResponse>> response = ticketService.getAllTickets();
        assertThat(response).isNotNull();
        assertThat(response.getData()).hasSize(5);
    }

    @Test
    public void getAllTicketsForEventTest() {
        ApiResponse<List<GetAllTicketsResponse>> response = ticketService.getAllTicketsForEvent(200L);
        assertThat(response).isNotNull();
        assertThat(response.getData()).hasSize(3);
    }


}