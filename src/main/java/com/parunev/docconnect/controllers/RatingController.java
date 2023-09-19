package com.parunev.docconnect.controllers;

import com.parunev.docconnect.models.payloads.rating.RatingRequest;
import com.parunev.docconnect.models.payloads.rating.RatingResponse;
import com.parunev.docconnect.services.RatingService;
import com.parunev.docconnect.utils.DCLogger;
import com.parunev.docconnect.utils.annotations.swagger.rating.ApiCreateRating;
import com.parunev.docconnect.utils.annotations.swagger.rating.ApiDeleteRating;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ratings")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;
    private final DCLogger dcLogger = new DCLogger(RatingController.class);

    @ApiCreateRating
    @PostMapping("/{specialistId}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<RatingResponse> createRating(@RequestBody RatingRequest request, @PathVariable Long specialistId){
        dcLogger.info("Creating a new rating for specialist with ID: {}", specialistId);
        return ResponseEntity.ok(ratingService.createRating(request, specialistId));
    }

    @ApiDeleteRating
    @DeleteMapping("/{ratingId}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<RatingResponse> deleteRating(@PathVariable Long ratingId){
        dcLogger.info("Deleting rating with ID: {}", ratingId);
        return ResponseEntity.ok(ratingService.deleteRating(ratingId));
    }
}
