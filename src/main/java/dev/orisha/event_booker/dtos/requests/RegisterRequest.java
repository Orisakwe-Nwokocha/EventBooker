package dev.orisha.event_booker.dtos.requests;

import lombok.Getter;
import lombok.Setter;

import static jakarta.persistence.EnumType.STRING;

@Getter
@Setter
public class RegisterRequest {
    private String username;
    private String password;

}
