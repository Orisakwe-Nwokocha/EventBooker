package dev.orisha.event_booker.services;

import dev.orisha.event_booker.data.models.Event;
import dev.orisha.event_booker.data.models.Ticket;
import dev.orisha.event_booker.dtos.requests.AddTicketRequest;
import dev.orisha.event_booker.dtos.requests.PurchaseTicketRequest;
import dev.orisha.event_booker.dtos.requests.ReserveTicketRequest;
import dev.orisha.event_booker.dtos.responses.ApiResponse;
import dev.orisha.event_booker.dtos.responses.GetAllTicketsResponse;
import dev.orisha.event_booker.dtos.responses.PurchaseTicketResponse;
import dev.orisha.event_booker.dtos.responses.ReserveTicketResponse;

import java.util.List;

public interface TicketService {
    Ticket saveTicket(AddTicketRequest request, Event event);

    ApiResponse<ReserveTicketResponse> reserveTicket(ReserveTicketRequest request);

    ApiResponse<PurchaseTicketResponse> purchaseTicket(PurchaseTicketRequest request);

    Ticket getBy(Long id);

    ApiResponse<List<GetAllTicketsResponse>> getAllTickets();

    ApiResponse<List<GetAllTicketsResponse>> getAllTicketsForEvent(Long eventId);
}
