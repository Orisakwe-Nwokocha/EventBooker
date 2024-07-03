package dev.orisha.event_booker.utils;

import dev.orisha.event_booker.dtos.requests.PurchaseTicketRequest;
import dev.orisha.event_booker.dtos.requests.RegisterRequest;

public class TestUtils {

    public static RegisterRequest buildRegisterRequest() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("Organizer");
        request.setPassword("password");
        return request;
    }

    public static PurchaseTicketRequest buildPurchaseTicketRequest() {
        PurchaseTicketRequest request = new PurchaseTicketRequest();
        request.setName("buyer");
        request.setEmail("buyer@email.com");
        request.setTicketId(301L);
        request.setNumberOfTickets(5);
        request.setCardNumber("123456789");
        return request;
    }

}
