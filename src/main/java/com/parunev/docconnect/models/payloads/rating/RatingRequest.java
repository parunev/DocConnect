package com.parunev.docconnect.models.payloads.rating;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request Body the user sends to add a comment.")
public class RatingRequest {

    @Schema(name = "rating", example = "3")
    private int rating;

    @Schema(name = "comment", example = "The best doctor ever!")
    private String comment;
}
