package com.parunev.docconnect.models.payloads.user.login;

import com.parunev.docconnect.utils.annotations.password.PasswordConfirmation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@PasswordConfirmation
public class ResetPasswordRequest {

    @Schema(name = "Token", description = "Password reset token")
    private String token;

    @NotBlank(message = "Please enter a password.")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters long.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&.,#\"':;><\\\\/|\\[\\]€£~{}()+=^_-])[A-Za-z\\d@$!%*?&.,#\"':;><\\\\/|\\[\\]€£~{}()+=^_-]{7,}$",
            message = "Your password must have at least 8 characters, with a mix of uppercase, lowercase, numbers and symbols.")
    @Schema(name = "Password", description = "User's password")
    private String resetPassword;

    @NotBlank(message = "Please confirm your password.")
    @Schema(name = "Confirm Password", description = "User's password confirmation")
    private String confirmResetPassword;
}
