package com.parunev.docconnect.models.payloads.user.login;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(name = "Forgot Password Request", description = "Forgot password request payload")
public class ForgotPasswordRequest {

    @NotBlank(message = "Please enter an email address.")
    @Pattern(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$",
            message = "Please enter a valid email address.")
    @Schema(name = "Email Address", description = "User/Specialist's email address")
    private String emailAddress;
}
