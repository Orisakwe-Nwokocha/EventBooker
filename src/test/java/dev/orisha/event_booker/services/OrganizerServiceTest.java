package dev.orisha.event_booker.services;

import dev.orisha.event_booker.dtos.requests.AddTicketRequest;
import dev.orisha.event_booker.dtos.requests.CreateEventRequest;
import dev.orisha.event_booker.dtos.requests.RegisterRequest;
import dev.orisha.event_booker.dtos.responses.AddTicketResponse;
import dev.orisha.event_booker.dtos.responses.ApiResponse;
import dev.orisha.event_booker.dtos.responses.CreateEventResponse;
import dev.orisha.event_booker.dtos.responses.RegisterResponse;
import dev.orisha.event_booker.exceptions.UsernameExistsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static dev.orisha.event_booker.data.constants.Type.REGULAR;
import static dev.orisha.event_booker.utils.TestUtils.buildRegisterRequest;
import static java.math.BigDecimal.valueOf;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql("/db/data.sql")
class OrganizerServiceTest {
    @Autowired
    private OrganizerService organizerService;

    @Test
    void registerOrganizerTest() {
        RegisterRequest request = buildRegisterRequest();
        ApiResponse<RegisterResponse> response = organizerService.register(request);
        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isTrue();
        String message = response.getData().getMessage();
        assertThat(message).contains("success");
    }

    @Test
    @DisplayName("test that only unique username can be saved")
    void registerOrganizer_throwsUsernameExistsException() {
        RegisterRequest request = buildRegisterRequest();
        ApiResponse<RegisterResponse> response = organizerService.register(request);
        assertThat(response).isNotNull();
        assertThrows(UsernameExistsException.class, () -> organizerService.register(request));
    }

    @Test
    public void createEventTest() {
        CreateEventRequest request = new CreateEventRequest();
        request.setOrganizerId(100L);
        request.setTitle("New event");
        request.setDescription("description");
        request.setEventDate(now());
        request.setGuests(List.of("guest1", "guest2"));
        ApiResponse<CreateEventResponse> response = organizerService.createEvent(request);
        assertThat(response).isNotNull();
        assertThat(response.getData().getGuests()).hasSize(2);

    }

    @Test
    public void addTicketToEventTest() {
        AddTicketRequest request = new AddTicketRequest();
        request.setEventId(200L);
        request.setType(REGULAR);
        request.setAvailableTickets(58);
        request.setPrice(valueOf(2000));
        ApiResponse<AddTicketResponse> response = organizerService.addTicketToEvent(request);
        assertThat(response).isNotNull();
        assertThat(response.getData().getType()).isEqualTo(REGULAR);
    }

    @Test
    public void getAllGuestsTest() {
        ApiResponse<List<String>> response = organizerService.getAllGuestsFor(200L);
        assertThat(response).isNotNull();
        assertThat(response.getData()).hasSize(3);
    }

}