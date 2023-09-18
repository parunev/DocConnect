package com.parunev.docconnect.services;

import com.parunev.docconnect.models.Rating;
import com.parunev.docconnect.models.User;
import com.parunev.docconnect.models.payloads.rating.RatingRequest;
import com.parunev.docconnect.models.payloads.rating.RatingResponse;
import com.parunev.docconnect.models.specialist.Specialist;
import com.parunev.docconnect.repositories.RatingRepository;
import com.parunev.docconnect.repositories.SpecialistRepository;
import com.parunev.docconnect.repositories.UserRepository;
import com.parunev.docconnect.security.exceptions.RatingNotFoundException;
import com.parunev.docconnect.security.exceptions.SpecialistNotFoundException;
import com.parunev.docconnect.security.exceptions.UserNotFoundException;
import com.parunev.docconnect.security.payload.ApiError;
import com.parunev.docconnect.security.payload.AuthenticationError;
import com.parunev.docconnect.utils.DCLogger;
import com.parunev.docconnect.utils.validators.AuthHelpers;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

import static com.parunev.docconnect.security.SecurityUtils.getCurrentUserDetails;

@Service
@Validated
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final SpecialistRepository specialistRepository;
    private final AuthHelpers authHelpers;
    private final DCLogger dcLogger = new DCLogger(RatingService.class);

    public RatingResponse createRating(@Valid RatingRequest request, Long specialistId){
        User user = userRepository.findByEmail(getCurrentUserDetails().getUsername())
                .orElseThrow(() -> {
                    dcLogger.warn("User not found");
                    return new UserNotFoundException(AuthenticationError.builder()
                            .path(authHelpers.getRequest().getRequestURI())
                            .error("User not found")
                            .timestamp(LocalDateTime.now())
                            .status(HttpStatus.NOT_FOUND)
                            .build());
                });

        Specialist specialist = specialistRepository.findById(specialistId)
                .orElseThrow(() -> {
                    dcLogger.warn("Specialist not found");
                    return new SpecialistNotFoundException(AuthenticationError.builder()
                            .path(authHelpers.getRequest().getRequestURI())
                            .error("Specialist not found")
                            .timestamp(LocalDateTime.now())
                            .status(HttpStatus.NOT_FOUND)
                            .build());
                });

        dcLogger.info("Creating a new rating for user {} on specialist with ID: {}", user.getId(), specialistId);

        ratingRepository.save(Rating.builder()
                .user(user)
                .specialist(specialist)
                .ratingSize(request.getRating())
                .comment(request.getComment())
                .build());

        return RatingResponse.builder()
                .path(authHelpers.getRequest().getRequestURI())
                .message("Rating created successfully")
                .averageRating(ratingRepository.getAverageRatingBySpecialistId(specialistId))
                .comment(request.getComment())
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CREATED)
                .build();
    }

    public RatingResponse deleteRating(Long ratingId){
        dcLogger.info("Deleting rating with ID: {}", ratingId);
        ratingRepository.delete(ratingRepository.findById(ratingId)
                .orElseThrow(() -> {
                    dcLogger.warn("Rating not found");
                    return new RatingNotFoundException(ApiError.builder()
                            .path(authHelpers.getRequest().getRequestURI())
                            .error("Rating not found")
                            .timestamp(LocalDateTime.now())
                            .status(HttpStatus.NOT_FOUND)
                            .build());
                }));

        return RatingResponse.builder()
                .path(authHelpers.getRequest().getRequestURI())
                .message("Rating deleted successfully")
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK)
                .build();
    }

    public Page<RatingResponse> getAllRatingsForSpecialistPageable(Long specialistId, Pageable pageable) {
        dcLogger.info("Retrieving all ratings for specialist with ID: {}", specialistId);

        Page<Rating> ratingsPage = ratingRepository.findAllBySpecialistId(specialistId, pageable);

        return ratingsPage.map(RatingService::mapEntityToDto);
    }

    private static RatingResponse mapEntityToDto(Rating rating) {
        return RatingResponse.builder()
                .message(rating.getUser().getName())
                .rating(rating.getRatingSize())
                .comment(rating.getComment())
                .timestamp(rating.getCreateDate().atStartOfDay())
                .build();
    }
}
