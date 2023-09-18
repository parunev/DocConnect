package com.parunev.docconnect.models.payloads.user.login;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(name = "Login Request", description = "Payload for login")
public class LoginRequest {

    @NotBlank(message = "Please enter your email address.")
    @Pattern(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$",
            message = "Please enter a valid email address.")
    @Schema(name = "emailAddress", example = "example@email.com")
    private String emailAddress;

    @NotBlank(message = "Please enter your password.")
    @Schema(name = "password", example = "GenericPassword1@")
    private String password;
}
