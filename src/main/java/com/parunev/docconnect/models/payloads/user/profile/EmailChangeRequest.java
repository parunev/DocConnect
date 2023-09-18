package com.parunev.docconnect.models.payloads.user.profile;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(name = "Email Change Request", description = "Request for changing user email.")
public class EmailChangeRequest {

    @NotBlank(message = "Please enter your new email address.")
    @Pattern(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$",
            message = "Please enter a valid new email address.")
    @Schema(name = "User new email", description = "User new email.")
    private String email;

    @NotBlank(message = "Please enter your new password to confirm your new email.")
    @Schema(name = "User current password", description = "Enter your password to confirm your new email.")
    private String password;

}
