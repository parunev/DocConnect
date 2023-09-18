package com.parunev.docconnect.models.payloads.specialist;

import com.parunev.docconnect.models.enums.Gender;
import com.parunev.docconnect.utils.annotations.password.PasswordConfirmation;
import com.parunev.docconnect.utils.annotations.uri.IsValidUri;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
@PasswordConfirmation
@Schema(name = "Specialist Registration Request", description = "Payload for specialist registration")
public class SpecialistRegistrationRequest {

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

    @NotBlank(message = "Please enter a phone number.")
    @Min(value = 6, message = "Please enter a valid phone number.")
    @Schema(name = "phoneNumber", example = "123456789")
    private String phoneNumber;

    @NotBlank(message = "Please enter an email address.")
    @Pattern(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$",
            message = "Please enter a valid email address.")
    @Schema(name = "emailAddress", example = "example@email.com")
    private String email;

    @NotBlank(message = "Please enter a password.")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters long.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&.,#\"':;><\\\\/|\\[\\]€£~{}()+=^_-])[A-Za-z\\d@$!%*?&.,#\"':;><\\\\/|\\[\\]€£~{}()+=^_-]{7,}$",
            message = "Your password must have at least 8 characters, with a mix of uppercase, lowercase, numbers and symbols.")
    @Schema(name = "password", example = "GenericPassword1@")
    private String password;

    @NotBlank(message = "Please confirm your password.")
    @Schema(name = "confirmPassword", example = "GenericPassword1@")
    private String confirmPassword;

    @NotBlank(message = "Please enter a summary.")
    @Length(min = 50)
    @Schema(name = "summary", example = "I am a specialist")
    private String summary;

    @Positive(message = "Please enter a valid experience years.")
    @Schema(name = "experienceYears", example = "5")
    private int experienceYears;

    @NotNull(message = "Please choose your gender.")
    @Schema(name = "gender", example = "Male")
    private Gender gender;

    @Positive(message = "City id must be a positive number")
    @Schema(description = "Specialist city (id)", example = "1")
    private Long cityId;

    @Positive(message = "Country id must be a positive number")
    @Schema(description = "Specialist country (id)", example = "1")
    private Long countryId;

    @Positive(message = "Specialty id must be a positive number")
    @Schema(description = "Specialist specialty (id)", example = "1")
    private Long specialtyId;

    @NotNull(message = "Please enter at least one address.")
    @Schema(description = "Specialist addresses", example = "['address1', 'address2']")
    private List<String> addresses;

    @IsValidUri
    @Schema(description = "Specialist profile picture url", example = "https://docconnect.s3.amazonaws.com/...")
    private String imageUrl;
}
