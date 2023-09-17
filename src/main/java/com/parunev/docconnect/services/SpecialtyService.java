package com.parunev.docconnect.services;

import com.parunev.docconnect.models.Specialty;
import com.parunev.docconnect.models.payloads.specialty.SpecialtyRequest;
import com.parunev.docconnect.models.payloads.specialty.SpecialtyResponse;
import com.parunev.docconnect.repositories.SpecialtyRepository;
import com.parunev.docconnect.security.exceptions.SpecialtyServiceException;
import com.parunev.docconnect.utils.DCLogger;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

/**
 * The `SpecialtyService` class provides business logic and services related to medical specialties.
 * It handles operations such as adding, updating, deleting, and retrieving specialties.
 * This service interacts with the `SpecialtyRepository` for data access.
 */
@Service
@Validated
@RequiredArgsConstructor
public class SpecialtyService {

    private final SpecialtyRepository specialtyRepository;
    private final HttpServletRequest httpServletRequest;
    private final DCLogger dcLogger = new DCLogger(SpecialtyService.class);
    private final ModelMapper modelMapper;

    /**
     * Adds a new medical specialty based on the provided `SpecialtyRequest`.
     *
     * @param request The `SpecialtyRequest` object containing data for the new specialty.
     * @return A `SpecialtyResponse` indicating the result of the operation.
     * @throws SpecialtyServiceException if the specialty with the same name already exists.
     */
    public SpecialtyResponse addSpecialty(@Valid SpecialtyRequest request){
        boolean doExists = specialtyRepository.existsBySpecialtyName(request.getSpecialtyName());

        if (doExists) {
            dcLogger.warn("Specialty with name {} already exists", request.getSpecialtyName());

            throw new SpecialtyServiceException(SpecialtyResponse.builder()
                    .path(httpServletRequest.getRequestURI())
                    .message("Specialty already exists")
                    .specialtyName(request.getSpecialtyName())
                    .imageUrl(request.getImageUrl())
                    .timestamp(LocalDateTime.now())
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }

        specialtyRepository.save(Specialty
                .builder()
                .specialtyName(request.getSpecialtyName())
                .imageUrl(request.getImageUrl())
                .build());

        dcLogger.info("Specialty with name {} was added", request.getSpecialtyName());

        return SpecialtyResponse.builder()
                .path(httpServletRequest.getRequestURI())
                .message("Specialty created successfully")
                .specialtyName(request.getSpecialtyName())
                .imageUrl(request.getImageUrl())
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK)
                .build();
    }

    /**
     * Updates an existing medical specialty based on the provided `id` and `SpecialtyRequest`.
     *
     * @param id      The ID of the specialty to update.
     * @param request The `SpecialtyRequest` object containing updated data for the specialty.
     * @return A `SpecialtyResponse` indicating the result of the operation.
     */
    public SpecialtyResponse updateSpecialty(Long id, @Valid SpecialtyRequest request){
        Specialty specialty = findSpecialtyById(id);

        specialty.setSpecialtyName(request.getSpecialtyName());
        specialty.setImageUrl(request.getImageUrl());

        specialtyRepository.save(specialty);

        dcLogger.info("Specialty with name {} was updated", request.getSpecialtyName());

        return SpecialtyResponse.builder()
                .path(httpServletRequest.getRequestURI())
                .message("Specialty updated successfully")
                .specialtyName(specialty.getSpecialtyName())
                .imageUrl(specialty.getImageUrl())
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK)
                .build();
    }

    /**
     * Deletes a medical specialty based on the provided `id`.
     *
     * @param id The ID of the specialty to delete.
     * @return A `SpecialtyResponse` indicating the result of the operation.
     */
    public SpecialtyResponse deleteSpecialty(Long id){
        Specialty specialty = findSpecialtyById(id);

        specialtyRepository.delete(specialty);

        dcLogger.info("Specialty with id {} was deleted", id);

        return SpecialtyResponse.builder()
                .path(httpServletRequest.getRequestURI())
                .message("Specialty deleted successfully")
                .specialtyName(specialty.getSpecialtyName())
                .imageUrl(specialty.getImageUrl())
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK)
                .build();
    }

    /**
     * Retrieves a medical specialty based on the provided `id`.
     *
     * @param id The ID of the specialty to retrieve.
     * @return A `SpecialtyResponse` containing the retrieved specialty's information.
     * @throws SpecialtyServiceException if the specialty with the provided ID is not found.
     */
    public SpecialtyResponse getSpecialtyById(Long id){
        dcLogger.info("Specialty with id {} was retrieved", id);
        return modelMapper.map(findSpecialtyById(id), SpecialtyResponse.class);
    }

    /**
     * Retrieves a pageable list of medical specialties.
     *
     * @param pageable The `Pageable` object for pagination.
     * @return A `Page` of `SpecialtyResponse` objects representing the retrieved specialties.
     * @throws SpecialtyServiceException if no specialties are found.
     */
    public Page<SpecialtyResponse> getAllSpecialtiesPageable(Pageable pageable){
        Page<Specialty> specialtiesPage = specialtyRepository.findAll(pageable);

        if (specialtiesPage.isEmpty()) {
            dcLogger.warn("No specialties found");
            throw new SpecialtyServiceException(SpecialtyResponse.builder()
                    .path(httpServletRequest.getRequestURI())
                    .message("No specialties found")
                    .timestamp(LocalDateTime.now())
                    .status(HttpStatus.NOT_FOUND)
                    .build());
        }

        dcLogger.info("Retrieved {} specialties", specialtiesPage.getTotalElements());
        return specialtiesPage.map(entity -> modelMapper.map(entity, SpecialtyResponse.class));
    }

    private Specialty findSpecialtyById(Long id) {
        return specialtyRepository.findById(id)
                .orElseThrow(() -> new SpecialtyServiceException(SpecialtyResponse.builder()
                        .path(httpServletRequest.getRequestURI())
                        .message("Specialty with id:%d not found".formatted(id))
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.NOT_FOUND)
                        .build()));
    }

}
