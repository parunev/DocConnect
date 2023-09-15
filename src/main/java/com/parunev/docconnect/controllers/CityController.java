package com.parunev.docconnect.controllers;

import com.parunev.docconnect.models.payloads.city.CityRequest;
import com.parunev.docconnect.models.payloads.city.CityResponse;
import com.parunev.docconnect.services.CityService;
import com.parunev.docconnect.utils.DCLogger;
import com.parunev.docconnect.utils.annotations.swagger.city.*;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cities")
@Tag(name = "City Controller", description = "CRUD operations related to cities")
public class CityController {

    private final CityService cityService;
    private final DCLogger dcLogger = new DCLogger(CityController.class);

    @ApiAddCity
    @PostMapping
    public ResponseEntity<CityResponse> addCity(
            @Parameter(description = "City request object", required = true)
            @RequestBody CityRequest request) {
        dcLogger.info("Request to add city with name {}", request.getCityName());
        return ResponseEntity.ok(cityService.addCity(request));
    }

    @ApiUpdateCity
    @PutMapping("/{id}")
    public ResponseEntity<CityResponse> updateCity(
            @Parameter(description = "City id", required = true)
            @PathVariable Long id,
            @Parameter(description = "City request object", required = true)
            @RequestBody CityRequest request) {
        dcLogger.info("Request to update city with id {}", id);
        return ResponseEntity.ok(cityService.updateCity(id, request));
    }

    @ApiDeleteCity
    @DeleteMapping("/{id}")
    public ResponseEntity<CityResponse> deleteCity(
            @Parameter(description = "City id", required = true)
            @PathVariable Long id) {
        dcLogger.info("Request to delete city with id {}", id);
        return ResponseEntity.ok(cityService.deleteCity(id));
    }

    @ApiGetAllCities
    @GetMapping
    public ResponseEntity<List<CityResponse>> getAllCities() {
        dcLogger.info("Request to get all cities");
        return ResponseEntity.ok(cityService.getAllCities());
    }

    @ApiGetCityById
    @GetMapping("/{id}")
    public ResponseEntity<CityResponse> getCityById(
            @Parameter(description = "City id", required = true)
            @PathVariable Long id) {
        dcLogger.info("Request to get city with id {}", id);
        return ResponseEntity.ok(cityService.getCityById(id));
    }

    @ApiGetCityByCountryId
    @GetMapping("/country/{id}")
    public ResponseEntity<List<CityResponse>> getAllCitiesByCountryId(
            @Parameter(description = "Country id", required = true)
            @PathVariable Long id) {
        dcLogger.info("Request to get all cities by country id {}", id);
        return ResponseEntity.ok(cityService.getAllCitiesByCountryId(id));
    }
}
