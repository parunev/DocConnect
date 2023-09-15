package com.parunev.docconnect.models.payloads.city;

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
@Schema(name = "City Request", description = "The request payload to create a new city")
public class CityRequest {

    @NotBlank(message = "City name must not be blank")
    @Size(min = 3, max = 50, message = "City name must be between 3 and 50 characters")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "City name must contain only letters")
    @Schema(name = "City Name", description = "The name of the city", example = "New York")
    private String cityName;

    @Positive(message = "Country ID must be a positive number")
    @Schema(name = "Country ID", description = "The ID of the country to which the city belongs", example = "1")
    private Long countryId;
}
