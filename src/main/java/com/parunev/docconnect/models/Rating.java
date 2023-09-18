package com.parunev.docconnect.models;

import com.parunev.docconnect.models.commons.BaseEntity;
import com.parunev.docconnect.models.specialist.Specialist;
import jakarta.persistence.*;
import lombok.*;

/**
 * The `Rating` class is an entity that models user ratings and comments for specialists' services.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "RATING")
@EqualsAndHashCode(callSuper = true)
@AttributeOverride(name = "id", column = @Column(name = "RATING_ID"))
public class Rating extends BaseEntity {

    /**
     * The numeric rating provided by a user for a specialist's service.
     * It represents the quality or satisfaction level of the service.
     */
    @Column(name = "RATING_SIZE", nullable = false)
    private Integer ratingSize;

    /**
     * A textual comment or review provided by a user for a specialist's service.
     * This field allows users to provide additional feedback or comments about their experience.
     */
    @Column(name = "COMMENT", columnDefinition = "TEXT")
    private String comment;

    /**
     * The specialist associated with this rating.
     * This field establishes a many-to-one relationship with the Specialist entity.
     * It links a rating to a specific specialist, indicating which specialist received the rating.
     */
    @ManyToOne
    @JoinColumn(name = "SPECIALIST_ID", referencedColumnName = "SPECIALIST_ID")
    private Specialist specialist;

    /**
     * The user who submitted this rating and comment.
     * This field establishes a many-to-one relationship with the User entity.
     * It links a rating to the user who provided the rating and feedback.
     */
    @ManyToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
    private User user;

}
