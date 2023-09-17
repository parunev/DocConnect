package com.parunev.docconnect.models.payloads.specialty;

import com.parunev.docconnect.utils.annotations.uri.IsValidUri;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Specialty Request", description = "The request payload to create a new specialty")
public class SpecialtyRequest {

    @NotBlank(message = "Specialty name must not be blank")
    @Size(min = 3, max = 50, message = "Specialty name must be between 3 and 100 characters")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Specialty name must contain only letters")
    @Schema(name = "Specialty Name", description = "The name of the specialty", example = "Cardiology")
    private String specialtyName;

    @IsValidUri
    @NotBlank(message = "Specialty image URL must not be blank")
    private String imageUrl;
}
