package com.parunev.docconnect.models.payloads.rating;


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
@Schema(name = "Rating Response", description = "Response object for rating data")
public class RatingResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(name = "Path", description = "The path of the request", example = "/api/v1/specialties")
    private String path;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(name = "Response message", description = "The message from the server", example = "Specialty created successfully")
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(name = "Rating", description = "The rating of the doctor", example = "3")
    private Integer rating;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(name = "Average Rating", description = "The average rating of the doctor", example = "3.4")
    private Double averageRating;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(name = "Comment", description = "The comment of the user", example = "The best doctor ever!")
    private String comment;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(name = "Timestamp", description = "The timestamp of the response", example = "2021-09-26T20:00:00.000Z")
    private LocalDateTime timestamp;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(name = "Status", description = "The status of the response", example = "OK")
    private HttpStatus status;
}
