package com.parunev.docconnect.service;

import com.parunev.docconnect.models.City;
import com.parunev.docconnect.models.Country;
import com.parunev.docconnect.models.payloads.city.CityRequest;
import com.parunev.docconnect.models.payloads.city.CityResponse;
import com.parunev.docconnect.repositories.CityRepository;
import com.parunev.docconnect.repositories.CountryRepository;
import com.parunev.docconnect.security.exceptions.CityServiceException;
import com.parunev.docconnect.security.exceptions.CountryServiceException;
import com.parunev.docconnect.services.CityService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CityServiceTest {

    @Mock
    private CityRepository cityRepository;

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CityService cityService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddCity_Success() {
        when(httpServletRequest.getRequestURI()).thenReturn("/api/v1/cities");

        CityRequest cityRequest = new CityRequest();
        cityRequest.setCityName("TestCity");
        cityRequest.setCountryId(1L);

        when(cityRepository.existsByCityName(cityRequest.getCityName())).thenReturn(false);

        Long countryId = 1L;
        String countryName = "TestCountry";
        Country sampleCountry = Country.builder()
                .countryName(countryName)
                .build();

        when(countryRepository.findById(countryId)).thenReturn(Optional.of(sampleCountry));

        City sampleCity = City.builder()
                .cityName(cityRequest.getCityName())
                .country(sampleCountry)
                .build();

        when(cityRepository.save(any(City.class))).thenReturn(sampleCity);

        CityResponse response = cityService.addCity(cityRequest);

        assertEquals("/api/v1/cities", response.getPath());
        assertEquals("City was added", response.getMessage());
        assertEquals(cityRequest.getCityName(), response.getCityName());
        assertEquals(sampleCountry.getCountryName(), response.getCountryName());
        assertNotNull(response.getTimestamp());
        assertEquals(HttpStatus.CREATED, response.getStatus());

        verify(cityRepository, times(1)).existsByCityName(cityRequest.getCityName());
        verify(countryRepository, times(1)).findById(countryId);
        verify(cityRepository, times(1)).save(any());
    }

    @Test
    void testAddCity_CityAlreadyExists() {
        when(httpServletRequest.getRequestURI()).thenReturn("/api/v1/cities");

        CityRequest cityRequest = new CityRequest();
        cityRequest.setCityName("TestCity");
        cityRequest.setCountryId(1L);

        when(cityRepository.existsByCityName(cityRequest.getCityName())).thenReturn(true);

        Long countryId = 1L;
        String countryName = "TestCountry";
        Country sampleCountry = Country.builder()
                .countryName(countryName)
                .build();

        when(countryRepository.findById(countryId)).thenReturn(Optional.of(sampleCountry));

        CityServiceException exception = assertThrows(CityServiceException.class,
                () -> cityService.addCity(cityRequest));

        assertEquals("/api/v1/cities", exception.getCityResponse().getPath());
        assertEquals("City already exists", exception.getCityResponse().getMessage());
        assertNotNull(exception.getCityResponse().getTimestamp());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getCityResponse().getStatus());

        verify(cityRepository, times(1)).existsByCityName(cityRequest.getCityName());
    }

    @Test
    void testAddCity_CountryNotFound() {
        when(httpServletRequest.getRequestURI()).thenReturn("/api/v1/cities");

        CityRequest cityRequest = new CityRequest();
        cityRequest.setCityName("TestCity");
        cityRequest.setCountryId(2L);

        when(cityRepository.existsByCityName(cityRequest.getCityName())).thenReturn(false);

        when(countryRepository.findById(cityRequest.getCountryId())).thenReturn(Optional.empty());

        CountryServiceException exception = assertThrows(CountryServiceException.class,
                () -> cityService.addCity(cityRequest));

        assertEquals("/api/v1/cities", exception.getCountryResponse().getPath());
        assertEquals("Country with id:2 not found", exception.getCountryResponse().getMessage());
        assertNotNull(exception.getCountryResponse().getTimestamp());
        assertEquals(HttpStatus.NOT_FOUND, exception.getCountryResponse().getStatus());

        verify(cityRepository, times(1)).existsByCityName(cityRequest.getCityName());
        verify(countryRepository, times(1)).findById(cityRequest.getCountryId());
    }

    @Test
    void testUpdateCity_Success(){
        when(httpServletRequest.getRequestURI()).thenReturn("/api/v1/cities");

        Long cityId = 1L;
        String cityName = "TestCity";
        Long countryId = 2L;
        CityRequest cityRequest = new CityRequest();
        cityRequest.setCityName(cityName);
        cityRequest.setCountryId(countryId);

        City sampleCity = City.builder()
                .cityName(cityName)
                .build();
        when(cityRepository.findById(cityId)).thenReturn(Optional.of(sampleCity));

        String countryName = "TestCountry";
        Country sampleCountry = Country.builder()
                .countryName(countryName)
                .build();
        when(countryRepository.findById(countryId)).thenReturn(Optional.of(sampleCountry));

        CityResponse response = cityService.updateCity(cityId, cityRequest);

        assertEquals("/api/v1/cities", response.getPath());
        // Since the id is private and cannot be GET nor SET that's the desired behaviour
        assertEquals("City with id:%d updated".formatted(null),response.getMessage());
        assertEquals(cityRequest.getCityName(), response.getCityName());
        assertEquals(sampleCountry.getCountryName(), response.getCountryName());
        assertNotNull(response.getTimestamp());
        assertEquals(HttpStatus.OK, response.getStatus());

        verify(cityRepository, times(1)).findById(cityId);
        verify(countryRepository, times(1)).findById(countryId);
    }

    @Test
    void testUpdateCity_CityNotFound() {
        when(httpServletRequest.getRequestURI()).thenReturn("/api/v1/cities");

        Long cityId = 1L;
        String cityName = "TestCity";
        Long countryId = 2L;
        CityRequest cityRequest = new CityRequest();
        cityRequest.setCityName(cityName);
        cityRequest.setCountryId(countryId);

        when(cityRepository.findById(cityId)).thenReturn(Optional.empty());

        CityServiceException exception = assertThrows(CityServiceException.class,
                () -> cityService.updateCity(cityId, cityRequest));

        assertEquals("/api/v1/cities", exception.getCityResponse().getPath());
        assertEquals("City with id:1 not found", exception.getCityResponse().getMessage());
        assertNotNull(exception.getCityResponse().getTimestamp());
        assertEquals(HttpStatus.NOT_FOUND, exception.getCityResponse().getStatus());

        verify(cityRepository, times(1)).findById(cityId);
        verifyNoInteractions(countryRepository);
    }

    @Test
    void testUpdateCity_CountryNotFound() {
        when(httpServletRequest.getRequestURI()).thenReturn("/api/v1/cities");

        Long cityId = 1L;
        String cityName = "TestCity";
        Long countryId = 2L;
        CityRequest cityRequest = new CityRequest();
        cityRequest.setCityName(cityName);
        cityRequest.setCountryId(countryId);

        City sampleCity = City.builder()
                .cityName(cityName)
                .build();
        when(cityRepository.findById(cityId)).thenReturn(Optional.of(sampleCity));

        when(countryRepository.findById(countryId)).thenReturn(Optional.empty());

        CountryServiceException exception = assertThrows(CountryServiceException.class,
                () -> cityService.updateCity(cityId, cityRequest));

        assertEquals("/api/v1/cities", exception.getCountryResponse().getPath());
        assertEquals("Country with id:2 not found", exception.getCountryResponse().getMessage());
        assertNotNull(exception.getCountryResponse().getTimestamp());
        assertEquals(HttpStatus.NOT_FOUND, exception.getCountryResponse().getStatus());

        verify(cityRepository, times(1)).findById(cityId);
        verify(countryRepository, times(1)).findById(countryId);
    }

    @Test
    void testDeleteCity_Success(){
        when(httpServletRequest.getRequestURI()).thenReturn("/api/v1/cities");

        Long cityId = 1L;
        City sampleCity = City.builder()
                .cityName("TestCity")
                .build();
        when(cityRepository.findById(cityId)).thenReturn(Optional.of(sampleCity));

        CityResponse response = cityService.deleteCity(cityId);

        assertEquals("/api/v1/cities", response.getPath());
        // Since the id is private and cannot be GET nor SET that's the desired behaviour
        assertEquals("City with id:%d removed".formatted(null), response.getMessage());
        assertNotNull(response.getTimestamp());
        assertEquals(HttpStatus.OK, response.getStatus());

        verify(cityRepository, times(1)).findById(cityId);
        verify(cityRepository, times(1)).deleteById(cityId);
    }

    @Test
    void testDeleteCity_CityNotFound() {
        when(httpServletRequest.getRequestURI()).thenReturn("/api/v1/cities");

        Long cityId = 1L;

        when(cityRepository.findById(cityId)).thenReturn(Optional.empty());

        CityServiceException exception = assertThrows(CityServiceException.class,
                () -> cityService.deleteCity(cityId));

        assertEquals("/api/v1/cities", exception.getCityResponse().getPath());
        assertEquals("City with id:1 not found", exception.getCityResponse().getMessage());
        assertNotNull(exception.getCityResponse().getTimestamp());
        assertEquals(HttpStatus.NOT_FOUND, exception.getCityResponse().getStatus());

        verify(cityRepository, times(1)).findById(cityId);
        verify(cityRepository, times(0)).delete(any(City.class));
    }

    @Test
    void testGetAllCities_Success(){
        List<City> cityList = new ArrayList<>();
        Country country = new Country();
        country.setCountryName("Test 1");
        Country country2 = new Country();
        country2.setCountryName("Test 2");

        City city = new City();
        city.setCityName("Test 1");
        city.setCountry(country);
        City city2 = new City();
        city2.setCityName("Test 2");
        city2.setCountry(country2);

        cityList.add(city);
        cityList.add(city2);

        when(cityRepository.findAll()).thenReturn(cityList);
        List<CityResponse> cityResponses = new ArrayList<>();
        CityResponse cityResponse = new CityResponse();
        cityResponse.setCityName(city.getCityName());
        cityResponse.setCountryName(country.getCountryName());
        CityResponse cityResponse2 = new CityResponse();
        cityResponse2.setCityName(city2.getCityName());
        cityResponse2.setCountryName(country2.getCountryName());
        cityResponses.add(cityResponse);
        cityResponses.add(cityResponse2);

        when(cityService.getAllCities()).thenReturn(cityResponses);

        assertEquals(2, cityResponses.size());
        assertEquals("Test 1", cityResponses.get(0).getCountryName());
        assertEquals("Test 2", cityResponses.get(1).getCountryName());
        assertEquals("Test 1", cityResponses.get(0).getCityName());
        assertEquals("Test 2", cityResponses.get(1).getCityName());

        verify(cityRepository, times(1)).findAll();
    }

    @Test
    void testGetCityById_Success() {
        Long cityId = 1L;

        Country country = new Country();
        country.setCountryName("Test 1");

        City city = new City();
        city.setCityName("Test 1");
        city.setCountry(country);

        when(cityRepository.findById(cityId)).thenReturn(Optional.of(city));

        CityResponse cityResponse = new CityResponse();
        cityResponse.setCityName(city.getCityName());
        cityResponse.setCountryName(city.getCountry().getCountryName());

        when(cityService.getCityById(cityId)).thenReturn(cityResponse);

        assertEquals(city.getCountry().getCountryName(), cityResponse.getCountryName());
        assertEquals(city.getCityName(), cityResponse.getCityName());

        verify(cityRepository, times(1)).findById(cityId);
    }

    @Test
    void testGetAllCitiesByCountryId_Success() {
        when(httpServletRequest.getRequestURI()).thenReturn("/api/v1/cities");
        Long countryId = 1L;

        Country country = new Country();
        country.setCountryName("Test 1");

        Country country2 = new Country();
        country.setCountryName("Test 2");

        List<City> sampleCities = List.of(
                City.builder().cityName("City1").country(country).build(),
                City.builder().cityName("City2").country(country2).build()
        );
        when(cityRepository.findAllByCountryId(countryId)).thenReturn(sampleCities);

        List<CityResponse> cityResponses = List.of(
                CityResponse.builder()
                        .cityName("City1")
                        .countryName("Test 1")
                        .build(),
                CityResponse.builder()
                        .cityName("City2")
                        .countryName("Test 2")
                        .build()
        );
        List<CityResponse> responses = new ArrayList<>(cityResponses);
        when(cityService.getAllCitiesByCountryId(countryId)).thenReturn(responses);

        assertEquals(2, responses.size());
        assertEquals("City1",responses.get(0).getCityName());
        assertEquals("City2",responses.get(1).getCityName());
        assertEquals("Test 1",responses.get(0).getCountryName());
        assertEquals("Test 2",responses.get(1).getCountryName());

        verify(cityRepository, times(1)).findAllByCountryId(countryId);
    }

    @Test
    void testGetAllCitiesByCountryId_CitiesNotFound() {
        when(httpServletRequest.getRequestURI()).thenReturn("/api/v1/cities");

        Long countryId = 1L;
        when(cityRepository.findAllByCountryId(countryId)).thenReturn(List.of());

        CityServiceException exception = assertThrows(CityServiceException.class, () -> cityService.getAllCitiesByCountryId(countryId));

        assertEquals("/api/v1/cities", exception.getCityResponse().getPath());
        assertEquals("Cities with country id:%d not found".formatted(countryId), exception.getCityResponse().getMessage());
        assertNotNull(exception.getCityResponse().getTimestamp());
        assertEquals(HttpStatus.NOT_FOUND, exception.getCityResponse().getStatus());

        verify(cityRepository, times(1)).findAllByCountryId(countryId);
    }
}
