package com.parunev.docconnect.services;

import com.parunev.docconnect.models.City;
import com.parunev.docconnect.models.Country;
import com.parunev.docconnect.models.payloads.city.CityRequest;
import com.parunev.docconnect.models.payloads.city.CityResponse;
import com.parunev.docconnect.models.payloads.country.CountryResponse;
import com.parunev.docconnect.repositories.CityRepository;
import com.parunev.docconnect.repositories.CountryRepository;
import com.parunev.docconnect.security.exceptions.CityServiceException;
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
 * The {@code CityService} class provides functionality for managing city records in the application.
 * It handles operations such as adding, updating, retrieving, and deleting cities.
 */
@Service
@Validated
@RequiredArgsConstructor
public class CityService {

    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final DCLogger dcLogger = new DCLogger(CityService.class);
    private final ModelMapper modelMapper;
    private final HttpServletRequest httpServletRequest;

    /**
     * Adds a new city record to the database.
     *
     * @param request The request containing the city data to be added.
     * @return A response indicating the status of the operation.
     */
    public CityResponse addCity(@Valid CityRequest request) {
        dcLogger.info("Request to add city with name {}", request.getCityName());

        boolean cityDoExist = cityRepository.existsByCityName(request.getCityName());

        dcLogger.info("Searching for country with id {}", request.getCountryId());
        Country country = countryRepository.findById(request.getCountryId())
                .orElseGet(() -> {
                    dcLogger.warn("Country with id {} not found", request.getCountryId());

                    throw new CountryServiceException(CountryResponse.builder()
                            .path(httpServletRequest.getRequestURI())
                            .message("Country with id:%d not found".formatted(request.getCountryId()))
                            .timestamp(LocalDateTime.now())
                            .status(HttpStatus.NOT_FOUND)
                            .build());
                });

        if (cityDoExist){
            dcLogger.warn("City with name {} already exists", request.getCityName());

            throw new CityServiceException(CityResponse.builder()
                    .path(httpServletRequest.getRequestURI())
                    .message("City already exists")
                    .cityName(request.getCityName())
                    .countryName("Country related to the city:" + country.getCountryName())
                    .timestamp(LocalDateTime.now())
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }

        City city = City.builder()
                .cityName(request.getCityName())
                .country(country)
                .build();

        dcLogger.info("City with name {} was added", request.getCityName());
        return CityResponse.builder()
                .path(httpServletRequest.getRequestURI())
                .message("City was added")
                .cityName(cityRepository.save(city).getCityName())
                .countryName(country.getCountryName())
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK)
                .build();
    }

    /**
     * Updates an existing city record in the database.
     *
     * @param id      The ID of the city to be updated.
     * @param request The request containing the updated city data.
     * @return A response indicating the status of the operation.
     */
    public CityResponse updateCity(Long id, @Valid CityRequest request){
        City city = findCityById(id);
        Country country;

        if (request.getCountryId() != null){
            country = findCountryById(request.getCountryId());
            city.setCountry(country);
        }

        dcLogger.info("City with id: {} was updated", id);
        city.setCityName(request.getCityName());

        return CityResponse.builder()
                .path(httpServletRequest.getRequestURI())
                .message("City with id:%d updated".formatted(city.getId()))
                .cityName(request.getCityName())
                .countryName(city.getCountry().getCountryName())
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK)
                .build();
    }

    /**
     * Deletes a city record from the database.
     *
     * @param id The ID of the city to be deleted.
     * @return A response indicating the status of the operation.
     */
    public CityResponse deleteCity(Long id){
        City city = findCityById(id);

        dcLogger.info("City with id {} was removed", id);
        cityRepository.deleteById(id);

        return CityResponse.builder()
                .path(httpServletRequest.getRequestURI())
                .message("City with id:%d removed".formatted(city.getId()))
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK)
                .build();
    }

    /**
     * Retrieves a list of all city records from the database.
     *
     * @return A list of city responses.
     */
    public List<CityResponse> getAllCities(){
        List<City> cities = cityRepository.findAll();

        return cities.stream()
                .map(city -> modelMapper.map(city, CityResponse.class))
                .toList();
    }

    /**
     * Retrieves a city record by its ID.
     *
     * @param id The ID of the city to retrieve.
     * @return A response containing the city data.
     */
    public CityResponse getCityById(Long id) {
        return modelMapper.map(findCityById(id), CityResponse.class);
    }

    /**
     * Retrieves a list of all city records by country id from the database.
     *
     * @return A list of city responses.
     */
    public List<CityResponse> getAllCitiesByCountryId(Long id){
        List<City> cities = cityRepository.findAllByCountryId(id);

        if (cities.isEmpty()){
            dcLogger.warn("Cities with country id: {} not found", id);

            throw new CityServiceException(CityResponse.builder()
                    .path(httpServletRequest.getRequestURI())
                    .message("Cities with country id:%d not found".formatted(id))
                    .timestamp(LocalDateTime.now())
                    .status(HttpStatus.NOT_FOUND)
                    .build());
        }

        return cities.stream()
                .map(city -> modelMapper.map(city, CityResponse.class))
                .toList();
    }

    private City findCityById(Long id) {
        return cityRepository.findById(id)
                .orElseGet(() -> {
                            dcLogger.warn("City with id {} not found", id);

                            throw new CityServiceException(CityResponse.builder()
                                    .path(httpServletRequest.getRequestURI())
                                    .message("City with id:%d not found".formatted(id))
                                    .timestamp(LocalDateTime.now())
                                    .status(HttpStatus.NOT_FOUND)
                                    .build());
                        }
                );
    }

    private Country findCountryById(Long id){
        return countryRepository.findById(id)
                .orElseGet(() -> {
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
