package com.parunev.docconnect.models.payloads.specialist;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "Specialist Address Response", description = "Payload for specialist address response")
public class SpecialistAddressResponse {

    @Schema(description = "Specialist address", example = "District 13, st. 13, bl. 13, ap. 13")
    private String docAddress;
}
