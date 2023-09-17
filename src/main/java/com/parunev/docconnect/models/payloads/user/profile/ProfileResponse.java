package com.parunev.docconnect.models.payloads.user.profile;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Profile Response", description = "Payload for profile response")
public class ProfileResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(name = "Path", description = "The path of the request", example = "/api/v1/profile")
    private String path;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(name = "Email Address", description = "The email address of the logged user", example = "example@gmail.com")
    private String emailAddress;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "User first name", example = "Martin")
    private String fullName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "User country", example = "Bulgaria")
    private String country;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "User city", example = "Sofia")
    private String city;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "User profile picture url", example = "https://docconnect.s3.amazonaws.com/...")
    private String imageUrl;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "User weight in kg", example = "90")
    private Integer weight;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "User height in cm", example = "180")
    private Integer height;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "User blood pressure", example = "120/80")
    private String bloodPressure;

    @JsonInclude(JsonInclude.Include.NON_NULL)
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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(name = "Timestamp", description = "The timestamp of the response", example = "2021-09-26T20:00:00.000Z")
    private LocalDateTime timestamp;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(name = "Status", description = "The status of the response", example = "OK")
    private HttpStatus status;
}
