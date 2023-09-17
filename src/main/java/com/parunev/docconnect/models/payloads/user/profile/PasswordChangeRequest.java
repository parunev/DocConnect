package com.parunev.docconnect.models.payloads.user.profile;

import com.parunev.docconnect.utils.annotations.password.PasswordConfirmation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@PasswordConfirmation
@Schema(description = "Password change request", name = "Password Change Request")
public class PasswordChangeRequest {

    @NotBlank(message = "Please enter your old password")
    @Schema(description = "Please enter your old password", example = "oldPassword")
    private String oldPassword;

    @NotBlank(message = "Please enter a password.")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters long.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&.,#\"':;><\\\\/|\\[\\]€£~{}()+=^_-])[A-Za-z\\d@$!%*?&.,#\"':;><\\\\/|\\[\\]€£~{}()+=^_-]{7,}$",
            message = "Your password must have at least 8 characters, with a mix of uppercase, lowercase, numbers and symbols.")
    @Schema(description = "Please enter a new password.", example = "password")
    private String newPassword;

    @NotBlank(message = "Please confirm your password.")
    @Schema(description = "Please confirm your new password.", example = "password")
    private String confirmNewPassword;
}
