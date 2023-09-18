package com.parunev.docconnect.models.payloads.user.login;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "VerificationRequest", description = "Request for 2FA verification")
public class VerificationRequest {

    @Schema(description = "User email", example = "test@gmail.com")
    private String email;

    @Schema(description = "Verification code", example = "123456")
    private String code;

}

