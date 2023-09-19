package com.parunev.docconnect.services;

import com.parunev.docconnect.models.Country;
import com.parunev.docconnect.models.payloads.country.CountryRequest;
import com.parunev.docconnect.models.payloads.country.CountryResponse;
import com.parunev.docconnect.repositories.CountryRepository;
import com.parunev.docconnect.security.exceptions.CountryServiceException;
import com.parunev.docconnect.utils.DCLogger;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;

/**
 * The {@code CountryService} class provides functionality for managing country records in the application.
 * It handles operations such as adding, updating, retrieving, and deleting countries.
 */
@Service
@Validated
@RequiredArgsConstructor
public class CountryService {

    // Dependencies
    private final CountryRepository countryRepository;
    private final DCLogger dcLogger = new DCLogger(CountryService.class);
    private final ModelMapper modelMapper;
    private final HttpServletRequest httpServletRequest;

    /**
     * Adds a new country record to the database.
     *
     * @param request The request containing the country data to be added.
     * @return A response indicating the status of the operation.
     */
    public CountryResponse addCountry(@Valid CountryRequest request){
        boolean doExists = countryRepository.existsByCountryName(request.getCountryName());

        if (doExists) {
            dcLogger.warn("Country with name {} already exists", request.getCountryName());

            throw new CountryServiceException(CountryResponse.builder()
                    .path(httpServletRequest.getRequestURI())
                    .message("Country already exists")
                    .countryName(request.getCountryName())
                    .timestamp(LocalDateTime.now())
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }

        Country country = Country.builder()
                .countryName(request.getCountryName())
                .build();
        countryRepository.save(country);

        dcLogger.info("Country with name {} was added", request.getCountryName());
        return CountryResponse.builder()
                .path(httpServletRequest.getRequestURI())
                .message("Country was added")
                .countryName(country.getCountryName())
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CREATED)
                .build();
    }

    /**
     * Updates an existing country record in the database.
     *
     * @param id      The ID of the country to be updated.
     * @param request The request containing the updated country data.
     * @return A response indicating the status of the operation.
     */
    public CountryResponse updateCountry(Long id, @Valid CountryRequest request){
        Country country = findCountryById(id);

        dcLogger.info("Country with id {} was updated", id);
        country.setCountryName(request.getCountryName());
        countryRepository.save(country);

        return CountryResponse.builder()
                .path(httpServletRequest.getRequestURI())
                .message("Country with id:%d updated".formatted(country.getId()))
                .countryName(request.getCountryName())
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK)
                .build();
    }

    /**
     * Deletes a country record from the database.
     *
     * @param id The ID of the country to be deleted.
     * @return A response indicating the status of the operation.
     */
    public CountryResponse deleteCountry(Long id){
        Country country = findCountryById(id);

        dcLogger.info("Country with id {} was removed", id);
        countryRepository.deleteById(country.getId());

        return CountryResponse.builder()
                .path(httpServletRequest.getRequestURI())
                .message("Country with id:%d removed".formatted(id))
                .countryName(null)
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK)
                .build();
    }

    /**
     * Retrieves a list of all country records from the database.
     *
     * @return A list of country responses.
     */
    public List<CountryResponse> getAllCountries(){
        List<Country> countries = countryRepository.findAll();

        return countries.stream()
                .map(country -> modelMapper.map(country, CountryResponse.class))
                .toList();
    }

    /**
     * Retrieves a country record by its ID.
     *
     * @param id The ID of the country to retrieve.
     * @return A response containing the country data.
     */
    public CountryResponse getCountryById(Long id) {
        return modelMapper.map(findCountryById(id), CountryResponse.class);
    }

    public Country findCountryById(Long id){
        return countryRepository.findById(id)
                .orElseThrow(() -> {
                    dcLogger.warn("Country with id {} not found", id);

                    throw new CountryServiceException(CountryResponse.builder()
                            .path(httpServletRequest.getRequestURI())
                            .message("Country with id:%d not found".formatted(id))
                            .timestamp(LocalDateTime.now())
                            .status(HttpStatus.NOT_FOUND)
                            .build());
                });
    }
}
