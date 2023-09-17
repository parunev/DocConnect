package com.parunev.docconnect.controllers;

import com.parunev.docconnect.models.payloads.specialty.SpecialtyRequest;
import com.parunev.docconnect.models.payloads.specialty.SpecialtyResponse;
import com.parunev.docconnect.services.SpecialtyService;
import com.parunev.docconnect.utils.DCLogger;
import com.parunev.docconnect.utils.annotations.swagger.specialty.*;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/specialties")
@Tag(name = "Specialty Controller", description = "CRUD operations related to specialties")
public class SpecialtyController {

    private final SpecialtyService specialtyService;
    private final DCLogger dcLogger = new DCLogger(SpecialtyController.class);

    @ApiAddSpecialty
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<SpecialtyResponse> addSpecialty(
            @Parameter(description = "Specialty data to be added. Cannot be empty.", required = true)
            @RequestBody SpecialtyRequest request){
        dcLogger.info("Request to add specialty with name {}", request.getSpecialtyName());
        return ResponseEntity.ok(specialtyService.addSpecialty(request));
    }

    @ApiUpdateSpecialty
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<SpecialtyResponse> updateSpecialty(
            @Parameter(description = "ID of the specialty to be updated. Cannot be empty.", required = true)
            @PathVariable Long id,
            @Parameter(description = "Specialty data to be updated. Cannot be empty.", required = true)
            @RequestBody SpecialtyRequest request){
        dcLogger.info("Request to update specialty with id {}", id);
        return ResponseEntity.ok(specialtyService.updateSpecialty(id, request));
    }

    @ApiDeleteSpecialty
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<SpecialtyResponse> deleteSpecialty(
            @Parameter(description = "ID of the specialty to be deleted. Cannot be empty.", required = true)
            @PathVariable Long id){
        dcLogger.info("Request to delete specialty with id {}", id);
        return ResponseEntity.ok(specialtyService.deleteSpecialty(id));
    }

    @ApiSpecialtyPageable
    @GetMapping
    public ResponseEntity<Page<SpecialtyResponse>> getAllSpecialties(Pageable pageable){
        dcLogger.info("Request to get all specialties");
        return ResponseEntity.ok(specialtyService.getAllSpecialtiesPageable(pageable));
    }

    @ApiGetSpecialtyById
    @GetMapping("/{id}")
    public ResponseEntity<SpecialtyResponse> getSpecialtyById(
            @Parameter(description = "ID of the specialty to be retrieved. Cannot be empty.", required = true)
            @PathVariable Long id){
        dcLogger.info("Request to get specialty with id {}", id);
        return ResponseEntity.ok(specialtyService.getSpecialtyById(id));
    }
}
