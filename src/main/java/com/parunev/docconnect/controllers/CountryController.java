package com.parunev.docconnect.controllers;

import com.parunev.docconnect.models.payloads.country.CountryRequest;
import com.parunev.docconnect.models.payloads.country.CountryResponse;
import com.parunev.docconnect.services.CountryService;
import com.parunev.docconnect.utils.DCLogger;
import com.parunev.docconnect.utils.annotations.swagger.country.*;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/countries")
@Tag(name = "Country Controller", description = "CRUD operations related to countries")
public class CountryController {
    private final CountryService countryService;
    private final DCLogger dcLogger = new DCLogger(CountryController.class);

    @ApiAddCountry
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<CountryResponse> addCountry(
            @Parameter(description = "Country data to be added. Cannot be empty.", required = true)
            @RequestBody CountryRequest request){
        dcLogger.info("Request to add country with name {}", request.getCountryName());
        return ResponseEntity.ok(countryService.addCountry(request));
    }

    @ApiUpdateCountry
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<CountryResponse> updateCountry(
            @Parameter(description = "ID of the country to be updated. Cannot be empty.", required = true)
            @PathVariable Long id,
            @Parameter(description = "Country data to be updated. Cannot be empty.", required = true)
            @RequestBody CountryRequest request){
        dcLogger.info("Request to update country with id {}", id);
        return ResponseEntity.ok(countryService.updateCountry(id, request));
    }

    @ApiDeleteCountry
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<CountryResponse> deleteCountry(
            @Parameter(description = "ID of the country to be deleted. Cannot be empty.", required = true)
            @PathVariable Long id){
        dcLogger.info("Request to delete country with id {}", id);
        return ResponseEntity.ok(countryService.deleteCountry(id));
    }

    @ApiGetAllCountries
    @GetMapping
    public ResponseEntity<List<CountryResponse>> getAllCountries(){
        dcLogger.info("Request to get all countries");
        return ResponseEntity.ok(countryService.getAllCountries());
    }

    @ApiGetCountryById
    @GetMapping("/{id}")
    public ResponseEntity<CountryResponse> getCountryById(
            @Parameter(description = "ID of the country to be obtained. Cannot be empty.", required = true)
            @PathVariable Long id){
        dcLogger.info("Request to get country with id {}", id);
        return ResponseEntity.ok(countryService.getCountryById(id));
    }

}
