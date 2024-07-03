package dev.orisha.event_booker.services;

import dev.orisha.event_booker.data.models.Event;
import dev.orisha.event_booker.data.models.Organizer;
import dev.orisha.event_booker.dtos.requests.CreateEventRequest;
import dev.orisha.event_booker.dtos.responses.ApiResponse;
import dev.orisha.event_booker.dtos.responses.GetEventResponse;

import java.util.List;

public interface EventService {
    Event saveEvent(CreateEventRequest request, Organizer organizer);
    Event getEventBy(Long id);
    List<String> getAllGuestsBy(Long id);
    ApiResponse<List<GetEventResponse>> getAllEventsFor(Long organizerId);
}
