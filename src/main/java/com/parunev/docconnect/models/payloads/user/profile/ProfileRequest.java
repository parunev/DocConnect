package com.parunev.docconnect.models.payloads.user.profile;

import com.parunev.docconnect.utils.annotations.uri.IsValidUri;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Profile Request", description = "Request for profile update")
public class ProfileRequest {

    @Size(max = 50, message = "First name must be less than 50 characters long.")
    @Pattern(regexp = "^[a-zA-Zà-üÀ-Ü]+$", message = "First name should contain only letters")
    @Schema(description = "User first name", example = "Martin")
    private String firstName;

    @Size(max = 50, message = "Last name must be less than 50 characters long.")
    @Pattern(regexp = "^[a-zA-Zà-üÀ-Ü]+$", message = "Last name should contain only letters")
    @Schema(description = "User last name", example = "Parunev")
    private String lastName;

    @Positive(message = "Country id must be a positive number")
    @Schema(description = "User country (id)", example = "1")
    private Long countryId;

    @Positive(message = "City id must be a positive number")
    @Schema(description = "User city (id)", example = "1")
    private Long cityId;

    @IsValidUri
    @Schema(description = "User profile picture url", example = "https://docconnect.s3.amazonaws.com/...")
    private String imageUrl;

    @Positive(message = "Weight must be a positive number")
    @Schema(description = "User weight in kg", example = "90")
    private Integer weight;

    @Positive(message = "Height must be a positive number")
    @Min(value = 65, message = "Height must be at least 65 cm")
    @Schema(description = "User height in cm", example = "180")
    private Integer height;

    @Pattern(regexp = "^[0-9]{1,3}/[0-9]{1,3}$", message = "Blood pressure must be in the format of 120/80")
    @Schema(description = "User blood pressure", example = "120/80")
    private String bloodPressure;

    @DecimalMin(value = "3.5", message = "Blood sugar level must be at least 3.5")
    @Schema(description = "User blood sugar level", example = "5.5")
    private Double bloodSugarLevel;

    @Schema(description = "User upcoming appointments notification", example = "true")
    private boolean upcomingNotification;

    @Schema(description = "User canceled appointments notification", example = "true")
    private boolean canceledNotification;

    @Schema(description = "User feedback appointments notification", example = "true")
    private boolean feedbackNotification;

    @Schema(description = "User MFA enabled", example = "true")
    private boolean mfaEnabled;
}
