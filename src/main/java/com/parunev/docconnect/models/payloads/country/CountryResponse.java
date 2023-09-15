package com.parunev.docconnect.models.payloads.country;

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
@Schema(name = "Country Response", description = "Response object for country data")
public class CountryResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(name = "Path", description = "The path of the request", example = "/api/v1/countries")
    private String path;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(name = "Response message", description = "The message from the server", example = "Country created successfully")
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(name = "Country Name", description = "The name of the country", example = "United States")
    private String countryName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(name = "Timestamp", description = "The timestamp of the response", example = "2021-09-26T20:00:00.000Z")
    private LocalDateTime timestamp;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(name = "Status", description = "The status of the response", example = "OK")
    private HttpStatus status;

}
