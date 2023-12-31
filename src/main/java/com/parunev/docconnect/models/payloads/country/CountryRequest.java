package com.parunev.docconnect.models.payloads.country;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(name = "Country Request", description = "The request payload to create a new country")
public class CountryRequest {

    @NotBlank(message = "Country name must not be blank")
    @Size(min = 3, max = 50, message = "Country name must be between 3 and 50 characters")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Country name must contain only letters")
    @Schema(name = "Country Name", description = "The name of the country", example = "United States")
    private String countryName;
}
