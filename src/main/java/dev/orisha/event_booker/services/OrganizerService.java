package dev.orisha.event_booker.services;


import dev.orisha.event_booker.dtos.requests.AddTicketRequest;
import dev.orisha.event_booker.dtos.requests.CreateEventRequest;
import dev.orisha.event_booker.dtos.requests.RegisterRequest;
import dev.orisha.event_booker.dtos.responses.AddTicketResponse;
import dev.orisha.event_booker.dtos.responses.ApiResponse;
import dev.orisha.event_booker.dtos.responses.CreateEventResponse;
import dev.orisha.event_booker.dtos.responses.RegisterResponse;

import java.util.List;

public interface OrganizerService {
    ApiResponse<RegisterResponse> register(RegisterRequest request);
    ApiResponse<CreateEventResponse> createEvent(CreateEventRequest request);
    ApiResponse<AddTicketResponse> addTicketToEvent(AddTicketRequest request);
    ApiResponse<List<String>> getAllGuestsFor(Long eventId);
}
