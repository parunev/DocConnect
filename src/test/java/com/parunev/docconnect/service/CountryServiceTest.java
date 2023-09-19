package com.parunev.docconnect.service;

import com.parunev.docconnect.models.Country;
import com.parunev.docconnect.models.payloads.country.CountryRequest;
import com.parunev.docconnect.models.payloads.country.CountryResponse;
import com.parunev.docconnect.repositories.CountryRepository;
import com.parunev.docconnect.security.exceptions.CountryServiceException;
import com.parunev.docconnect.services.CountryService;
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
import static org.mockito.Mockito.*;

class CountryServiceTest {

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CountryService countryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddCountry_Success() {
        CountryRequest countryRequest = new CountryRequest();
        countryRequest.setCountryName("Test name");

        when(httpServletRequest.getRequestURI()).thenReturn("/api/v1/countries");
        when(countryRepository.existsByCountryName(countryRequest.getCountryName())).thenReturn(false);

        Country country = Country.builder()
                .countryName(countryRequest.getCountryName())
                .build();

        when(countryRepository.save(country)).thenReturn(country);

        CountryResponse response = countryService.addCountry(countryRequest);

        assertEquals("/api/v1/countries", response.getPath());
        assertEquals("Country was added", response.getMessage());
        assertEquals(country.getCountryName(), response.getCountryName());
        assertNotNull(response.getTimestamp());
        assertEquals(HttpStatus.CREATED, response.getStatus());

        verify(countryRepository, times(1)).existsByCountryName(countryRequest.getCountryName());
        verify(countryRepository, times(1)).save(country);
    }

    @Test
    void testAddCountry_ThrowsException(){
        CountryRequest countryRequest = new CountryRequest();
        countryRequest.setCountryName("Test name");

        when(httpServletRequest.getRequestURI()).thenReturn("/api/v1/countries");
        when(countryRepository.existsByCountryName(countryRequest.getCountryName())).thenReturn(true);

        assertThrows(CountryServiceException.class, () -> countryService.addCountry(countryRequest));
        verify(countryRepository, times(1)).existsByCountryName(countryRequest.getCountryName());
        verify(countryRepository, never()).save(any(Country.class));
    }

    @Test
    void testUpdateCountry_Success(){
        when(httpServletRequest.getRequestURI()).thenReturn("/api/v1/countries");

        Long countryId = 1L;
        String initialName = "InitialName";
        Country initialCountry = Country.builder()
                .countryName(initialName)
                .build();

        String updatedName = "UpdatedName";
        when(countryRepository.findById(countryId)).thenReturn(Optional.of(initialCountry));

        CountryRequest updateRequest = new CountryRequest();
        updateRequest.setCountryName(updatedName);
        CountryResponse response = countryService.updateCountry(countryId, updateRequest);

        assertEquals("/api/v1/countries", response.getPath());
        assertEquals("Country with id:%d updated".formatted(initialCountry.getId()), response.getMessage());
        assertEquals(updatedName, response.getCountryName());
        assertNotNull(response.getTimestamp());
        assertEquals(HttpStatus.OK, response.getStatus());

        verify(countryRepository, times(1)).findById(countryId);
        verify(countryRepository, times(1)).save(initialCountry);

        assertEquals(updatedName, initialCountry.getCountryName());
    }

    @Test
    void testUpdateCountry_ThrowsException(){
        when(httpServletRequest.getRequestURI()).thenReturn("/api/v1/countries");

        Long countryId = 1L;
        CountryRequest updateRequest = new CountryRequest();
        updateRequest.setCountryName("Test name");

        when(countryRepository.findById(countryId)).thenThrow(CountryServiceException.class);
        assertThrows(CountryServiceException.class, ()-> countryService.updateCountry(countryId, updateRequest));

        verify(countryRepository, times(1)).findById(countryId);
    }

    @Test
    void testDeleteCountry_Success(){
        when(httpServletRequest.getRequestURI()).thenReturn("/api/v1/countries");
        Long countryId = 1L;
        Country initialCountry = Country.builder()
                .countryName("Test name")
                .build();

        when(countryRepository.findById(countryId)).thenReturn(Optional.ofNullable(initialCountry));
        CountryResponse response = countryService.deleteCountry(countryId);

        assertEquals("/api/v1/countries", response.getPath());
        assertEquals("Country with id:%d removed".formatted(countryId), response.getMessage());
        assertNull(response.getCountryName());
        assertNotNull(response.getTimestamp());
        assertEquals(HttpStatus.OK, response.getStatus());
    }

    @Test
    void testDeleteCountry_ThrowsException(){
        when(httpServletRequest.getRequestURI()).thenReturn("/api/v1/countries");
        Long countryId = 1L;

        when(countryRepository.findById(countryId)).thenThrow(CountryServiceException.class);
        assertThrows(CountryServiceException.class, () -> countryService.deleteCountry(countryId));

        verify(countryRepository, times(1)).findById(countryId);
    }

    @Test
    void testGetAllCountries_Success(){
        List<Country> countryList = new ArrayList<>();
        Country country = new Country();
        country.setCountryName("Test 1");
        Country country2 = new Country();
        country2.setCountryName("Test 2");

        countryList.add(country);
        countryList.add(country2);

        when(countryRepository.findAll()).thenReturn(countryList);
        List<CountryResponse> countryResponseList = new ArrayList<>();
        CountryResponse countryResponse = new CountryResponse();
        countryResponse.setCountryName(country.getCountryName());
        CountryResponse countryResponse2 = new CountryResponse();
        countryResponse2.setCountryName(country2.getCountryName());
        countryResponseList.add(countryResponse);
        countryResponseList.add(countryResponse2);

        when(countryService.getAllCountries()).thenReturn(countryResponseList);

        assertEquals(2, countryResponseList.size());
        assertEquals("Test 1", countryResponseList.get(0).getCountryName());
        assertEquals("Test 2", countryResponseList.get(1).getCountryName());

        verify(countryRepository, times(1)).findAll();
    }

    @Test
    void testGetCountryById_Success() {
        Long countryId = 1L;
        Country country = new Country();
        country.setCountryName("Test 1");

        when(countryRepository.findById(countryId)).thenReturn(Optional.of(country));

        CountryResponse countryResponse = new CountryResponse();
        countryResponse.setCountryName(country.getCountryName());

        when(countryService.getCountryById(countryId)).thenReturn(countryResponse);

        assertEquals(country.getCountryName(), countryResponse.getCountryName());
        verify(countryRepository, times(1)).findById(countryId);
    }

    @Test
    void testFindCountryById_Success() {
        Long countryId = 1L;
        String countryName = "TestCountry";
        Country sampleCountry = Country.builder()
                .countryName(countryName)
                .build();

        when(countryRepository.findById(countryId)).thenReturn(Optional.of(sampleCountry));

        Country result = countryService.findCountryById(countryId);

        assertNotNull(result);
        assertEquals(countryName, result.getCountryName());

        verify(countryRepository, times(1)).findById(countryId);
    }

    @Test
    void testFindCountryById_NotFound() {
        Long nonExistentCountryId = 2L;

        when(countryRepository.findById(nonExistentCountryId)).thenReturn(Optional.empty());

        String expectedMessage = "Country with id:2 not found";

        when(httpServletRequest.getRequestURI()).thenReturn("/api/v1/countries");

        CountryServiceException exception = assertThrows(CountryServiceException.class, () -> countryService.findCountryById(nonExistentCountryId));

        assertThrows(CountryServiceException.class, () -> countryService.findCountryById(nonExistentCountryId));

        assertEquals("/api/v1/countries", exception.getCountryResponse().getPath());
        assertEquals(expectedMessage, exception.getCountryResponse().getMessage());
        assertNotNull(exception.getCountryResponse().getTimestamp());
        assertEquals(HttpStatus.NOT_FOUND, exception.getCountryResponse().getStatus());

        verify(countryRepository, times(2)).findById(nonExistentCountryId);
    }
}
