package com.parunev.docconnect.models.payloads.user.registration;

import com.parunev.docconnect.models.enums.Gender;
import com.parunev.docconnect.utils.annotations.password.PasswordConfirmation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@PasswordConfirmation
@Schema(name = "Registration Request", description = "Payload for registration")
public class RegistrationRequest {

    @NotBlank(message = "Please enter an email address.")
    @Pattern(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$",
            message = "Please enter a valid email address.")
    @Schema(name = "emailAddress", example = "example@email.com")
    private String emailAddress;

    @NotBlank(message = "Please enter a first name.")
    @Size(max = 50, message = "First name must be less than 50 characters long.")
    @Pattern(regexp = "^[a-zA-Zà-üÀ-Ü]+$", message = "First name should contain only letters")
    @Schema(name = "firstName", example = "John")
    private String firstName;

    @NotBlank(message = "Please enter a last name.")
    @Size(max = 50, message = "Last name must be less than 50 characters long.")
    @Pattern(regexp = "^[a-zA-Zà-üÀ-Ü]+$", message = "Last name should contain only letters")
    @Schema(name = "lastName", example = "Doe")
    private String lastName;

    @NotNull(message = "Please choose your gender.")
    @Schema(name = "gender", example = "Male")
    private Gender gender;

    @NotBlank(message = "Please enter a password.")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters long.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&.,#\"':;><\\\\/|\\[\\]€£~{}()+=^_-])[A-Za-z\\d@$!%*?&.,#\"':;><\\\\/|\\[\\]€£~{}()+=^_-]{7,}$",
            message = "Your password must have at least 8 characters, with a mix of uppercase, lowercase, numbers and symbols.")
    @Schema(name = "password", example = "GenericPassword1@")
    private String password;

    @NotBlank(message = "Please confirm your password.")
    @Schema(name = "confirmPassword", example = "GenericPassword1@")
    private String confirmPassword;

}
