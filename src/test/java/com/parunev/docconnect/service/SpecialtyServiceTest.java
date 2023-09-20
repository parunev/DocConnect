package com.parunev.docconnect.service;

import com.parunev.docconnect.models.Specialty;
import com.parunev.docconnect.models.payloads.specialty.SpecialtyRequest;
import com.parunev.docconnect.models.payloads.specialty.SpecialtyResponse;
import com.parunev.docconnect.repositories.SpecialtyRepository;
import com.parunev.docconnect.security.exceptions.SpecialtyServiceException;
import com.parunev.docconnect.services.SpecialtyService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SpecialtyServiceTest {

    @Mock
    private SpecialtyRepository specialtyRepository;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private SpecialtyService specialtyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddSpecialty_Success(){
        when(httpServletRequest.getRequestURI()).thenReturn("/api/v1/specialties");

        SpecialtyRequest specialtyRequest = new SpecialtyRequest();
        specialtyRequest.setSpecialtyName("Specialty Test");
        specialtyRequest.setImageUrl("https://google.com/image.url");

        when(specialtyRepository.existsBySpecialtyName("Specialty Test")).thenReturn(false);

        Specialty specialty = Specialty.builder()
                .specialtyName(specialtyRequest.getSpecialtyName())
                .imageUrl(specialtyRequest.getImageUrl())
                .build();

        when(specialtyRepository.save(any(Specialty.class))).thenReturn(specialty);

        SpecialtyResponse specialtyResponse = specialtyService.addSpecialty(specialtyRequest);

        assertEquals("/api/v1/specialties", specialtyResponse.getPath());
        assertEquals("Specialty created successfully", specialtyResponse.getMessage());
        assertEquals("Specialty Test", specialtyResponse.getSpecialtyName());
        assertEquals("https://google.com/image.url", specialtyResponse.getImageUrl());
        assertNotNull(specialtyResponse.getTimestamp());
        assertEquals(HttpStatus.CREATED, specialtyResponse.getStatus());

        verify(specialtyRepository, times(1)).existsBySpecialtyName("Specialty Test");
        verify(specialtyRepository, times(1)).save(any());
    }

    @Test
    void testAddSpecialty_SpecialtyAlreadyExists() {
        when(httpServletRequest.getRequestURI()).thenReturn("/api/v1/specialties");

        SpecialtyRequest specialtyRequest = new SpecialtyRequest();
        specialtyRequest.setSpecialtyName("Specialty Test");
        specialtyRequest.setImageUrl("https://google.com/image.url");

        when(specialtyRepository.existsBySpecialtyName("Specialty Test")).thenReturn(true);

        SpecialtyServiceException exception = assertThrows(SpecialtyServiceException.class,
                () -> specialtyService.addSpecialty(specialtyRequest));

        assertEquals("/api/v1/specialties", exception.getSpecialtyResponse().getPath());
        assertEquals("Specialty already exists", exception.getSpecialtyResponse().getMessage());
        assertEquals("Specialty Test", exception.getSpecialtyResponse().getSpecialtyName());
        assertEquals("https://google.com/image.url", exception.getSpecialtyResponse().getImageUrl());
        assertNotNull(exception.getSpecialtyResponse().getTimestamp());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getSpecialtyResponse().getStatus());

        verify(specialtyRepository, times(1)).existsBySpecialtyName("Specialty Test");
    }

    @Test
    void testUpdateSpecialty_Success(){
        when(httpServletRequest.getRequestURI()).thenReturn("/api/v1/specialties");

        Long specialtyId = 1L;
        SpecialtyRequest specialtyRequest = new SpecialtyRequest();
        specialtyRequest.setSpecialtyName("Specialty Test");
        specialtyRequest.setImageUrl("https://google.com/image.url");

        Specialty sampleSpecialty = Specialty.builder()
                .specialtyName("Specialty Test 1")
                .imageUrl("https://google.com/image1.url")
                .build();
        when(specialtyRepository.findById(specialtyId)).thenReturn(Optional.of(sampleSpecialty));

        SpecialtyResponse response = specialtyService.updateSpecialty(specialtyId, specialtyRequest);

        assertEquals("/api/v1/specialties", response.getPath());
        assertEquals("Specialty updated successfully", response.getMessage());
        assertEquals(sampleSpecialty.getSpecialtyName(), response.getSpecialtyName());
        assertEquals(sampleSpecialty.getImageUrl(), response.getImageUrl());
        assertNotNull(response.getTimestamp());
        assertEquals(HttpStatus.OK, response.getStatus());

        verify(specialtyRepository, times(1)).findById(specialtyId);
    }

    @Test
    void testUpdateSpecialty_ThrowsException(){
        when(httpServletRequest.getRequestURI()).thenReturn("/api/v1/specialties");

        Long specialtyId = 1L;
        SpecialtyRequest specialtyRequest = new SpecialtyRequest();
        specialtyRequest.setSpecialtyName("Specialty Test");
        specialtyRequest.setImageUrl("https://google.com/image.url");

        when(specialtyRepository.findById(specialtyId)).thenReturn(Optional.empty());

        SpecialtyServiceException exception = assertThrows(SpecialtyServiceException.class,
                () -> specialtyService.updateSpecialty(specialtyId, specialtyRequest));

        assertEquals("/api/v1/specialties", exception.getSpecialtyResponse().getPath());
        assertEquals("Specialty with id:1 not found", exception.getSpecialtyResponse().getMessage());
        assertNotNull(exception.getSpecialtyResponse().getTimestamp());
        assertEquals(HttpStatus.NOT_FOUND, exception.getSpecialtyResponse().getStatus());

        verify(specialtyRepository, times(1)).findById(specialtyId);
        verifyNoMoreInteractions(specialtyRepository);
    }

    @Test
    void testDeleteSpecialty_Success(){
        when(httpServletRequest.getRequestURI()).thenReturn("/api/v1/specialties");

        Long specialtyId = 1L;
        Specialty sampleSpecialty = Specialty.builder()
                .specialtyName("Specialty Test 1")
                .imageUrl("https://google.com/image1.url")
                .build();
        when(specialtyRepository.findById(specialtyId)).thenReturn(Optional.of(sampleSpecialty));

        SpecialtyResponse response = specialtyService.deleteSpecialty(specialtyId);

        assertEquals("/api/v1/specialties", response.getPath());
        assertEquals("Specialty deleted successfully", response.getMessage());
        assertNotNull(response.getTimestamp());
        assertEquals(HttpStatus.OK, response.getStatus());

        verify(specialtyRepository, times(1)).findById(specialtyId);
        verify(specialtyRepository, times(1)).delete(sampleSpecialty);
    }

    @Test
    void testDeleteSpecialty_ThrowsException(){
        when(httpServletRequest.getRequestURI()).thenReturn("/api/v1/specialties");

        Long specialtyId = 1L;
        when(specialtyRepository.findById(specialtyId)).thenReturn(Optional.empty());

        SpecialtyServiceException exception = assertThrows(SpecialtyServiceException.class,
                () -> specialtyService.deleteSpecialty(specialtyId));

        assertEquals("/api/v1/specialties", exception.getSpecialtyResponse().getPath());
        assertEquals("Specialty with id:%d not found".formatted(specialtyId), exception.getSpecialtyResponse().getMessage());
        assertNotNull(exception.getSpecialtyResponse().getTimestamp());
        assertEquals(HttpStatus.NOT_FOUND, exception.getSpecialtyResponse().getStatus());

        verify(specialtyRepository, times(1)).findById(specialtyId);
        verifyNoMoreInteractions(specialtyRepository);
    }

    @Test
    void testGetSpecialtyById_Success() {
        Long specialtyId = 1L;
        Specialty sampleSpecialty = Specialty.builder()
                .specialtyName("Specialty Test 1")
                .imageUrl("https://google.com/image1.url")
                .build();

        when(specialtyRepository.findById(specialtyId)).thenReturn(Optional.of(sampleSpecialty));

        SpecialtyResponse specialtyResponse = new SpecialtyResponse();
        specialtyResponse.setSpecialtyName("Specialty Test 1");
        specialtyResponse.setImageUrl("https://google.com/image1.url");

        when(specialtyService.getSpecialtyById(specialtyId)).thenReturn(specialtyResponse);

        assertEquals(sampleSpecialty.getSpecialtyName(), specialtyResponse.getSpecialtyName());
        assertEquals(sampleSpecialty.getImageUrl(), specialtyResponse.getImageUrl());

        verify(specialtyRepository, times(1)).findById(specialtyId);
    }

    @Test
    void testGetSpecialtyById_ThrowsException() {
        when(httpServletRequest.getRequestURI()).thenReturn("/api/v1/specialties");
        Long specialtyId = 1L;

        when(specialtyRepository.findById(specialtyId)).thenReturn(Optional.empty());

        SpecialtyServiceException exception = assertThrows(SpecialtyServiceException.class,
                () -> specialtyService.getSpecialtyById(specialtyId));

        assertEquals("/api/v1/specialties", exception.getSpecialtyResponse().getPath());
        assertEquals("Specialty with id:%d not found".formatted(specialtyId), exception.getSpecialtyResponse().getMessage());
        assertNotNull(exception.getSpecialtyResponse().getTimestamp());
        assertEquals(HttpStatus.NOT_FOUND, exception.getSpecialtyResponse().getStatus());

        verify(specialtyRepository, times(1)).findById(specialtyId);
    }

    @Test
    void testGetAllSpecialties_Success(){
        Pageable pageable = Pageable.ofSize(10).withPage(1);

        Page<Specialty> samplePage = new PageImpl<>(List.of(
                Specialty.builder().specialtyName("Specialty1").imageUrl("https://google.com/image1.url").build(),
                Specialty.builder().specialtyName("Specialty2").imageUrl("https://google.com/image2.url").build()
        ));
        when(specialtyRepository.findAll(pageable)).thenReturn(samplePage);

        Page<SpecialtyResponse> specialtyResponses = specialtyService.getAllSpecialtiesPageable(pageable);
        verify(specialtyRepository, times(1)).findAll(pageable);
        assertEquals(2, specialtyResponses.getTotalElements());
    }

    @Test
    void testGetAllSpecialties_ThrowsException(){
        when(httpServletRequest.getRequestURI()).thenReturn("/api/v1/specialties");

        Pageable pageable = Pageable.ofSize(10).withPage(1);
        when(specialtyRepository.findAll(pageable)).thenReturn(Page.empty());

        SpecialtyServiceException exception = assertThrows(SpecialtyServiceException.class,
                () -> specialtyService.getAllSpecialtiesPageable(pageable));

        assertEquals("/api/v1/specialties", exception.getSpecialtyResponse().getPath());
        assertEquals("No specialties found", exception.getSpecialtyResponse().getMessage());
        assertNotNull(exception.getSpecialtyResponse().getTimestamp());
        assertEquals(HttpStatus.NOT_FOUND, exception.getSpecialtyResponse().getStatus());

        verify(specialtyRepository,times(1)).findAll(pageable);
    }
}
