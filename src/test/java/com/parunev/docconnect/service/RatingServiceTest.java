package com.parunev.docconnect.service;

import com.parunev.docconnect.models.Rating;
import com.parunev.docconnect.models.User;
import com.parunev.docconnect.models.enums.Role;
import com.parunev.docconnect.models.payloads.rating.RatingRequest;
import com.parunev.docconnect.models.payloads.rating.RatingResponse;
import com.parunev.docconnect.models.specialist.Specialist;
import com.parunev.docconnect.repositories.RatingRepository;
import com.parunev.docconnect.repositories.SpecialistRepository;
import com.parunev.docconnect.repositories.UserRepository;
import com.parunev.docconnect.security.exceptions.RatingNotFoundException;
import com.parunev.docconnect.security.exceptions.SpecialistNotFoundException;
import com.parunev.docconnect.security.exceptions.UserNotFoundException;
import com.parunev.docconnect.services.RatingService;
import com.parunev.docconnect.utils.validators.AuthHelpers;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RatingServiceTest {

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SpecialistRepository specialistRepository;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private AuthHelpers authHelpers;

    @InjectMocks
    private RatingService ratingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = "USER")
    void testCreateRating_Success(){
        Mockito.when(httpServletRequest.getRequestURI()).thenReturn("/api/v1/ratings");
        Mockito.when(authHelpers.getRequest()).thenReturn(httpServletRequest);

        RatingRequest ratingRequest = new RatingRequest();
        ratingRequest.setRating(5);
        ratingRequest.setComment("Great service");

        UserDetails userDetails = User.builder()
                .email("user@example.com")
                .password("password")
                .role(Role.ROLE_USER)
                .build();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails,null));

        String userEmail = "user@example.com";
        User user = User.builder()
                .email(userEmail)
                .password("test")
                .role(Role.ROLE_USER)
                .build();

        Mockito.when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));

        Long specialistId = 1L;
        Specialist specialist = Specialist.builder().build();
        Mockito.when(specialistRepository.findById(specialistId)).thenReturn(Optional.of(specialist));

        Double averageRating = 4.5;
        Mockito.when(ratingRepository.getAverageRatingBySpecialistId(specialistId)).thenReturn(averageRating);
        RatingResponse ratingResponse = ratingService.createRating(ratingRequest, specialistId);

        assertEquals("/api/v1/ratings",ratingResponse.getPath());
        assertEquals("Rating created successfully",ratingResponse.getMessage());
        assertEquals(averageRating ,ratingResponse.getAverageRating());
        assertEquals(ratingRequest.getComment(), ratingResponse.getComment());
        assertNotNull(ratingResponse.getTimestamp());
        assertEquals(HttpStatus.CREATED, ratingResponse.getStatus());

        verify(userRepository, times(1)).findByEmail(userEmail);
        verify(specialistRepository, times(1)).findById(specialistId);
        verify(ratingRepository, times(1)).getAverageRatingBySpecialistId(specialistId);
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = "USER")
    void testCreateRating_UserNotFound(){
        Mockito.when(httpServletRequest.getRequestURI()).thenReturn("/api/v1/ratings");
        Mockito.when(authHelpers.getRequest()).thenReturn(httpServletRequest);

        UserDetails userDetails = User.builder()
                .email("user@example.com")
                .password("password")
                .role(Role.ROLE_USER)
                .build();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails,null));

        String userEmail = "user@example.com";
        Mockito.when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

        Long specialistId = 1L;
        Specialist specialist = Specialist.builder().build();
        Mockito.when(specialistRepository.findById(specialistId)).thenReturn(Optional.of(specialist));

        RatingRequest ratingRequest = new RatingRequest();
        ratingRequest.setRating(5);
        ratingRequest.setComment("Great service");

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> ratingService.createRating(ratingRequest, specialistId));

        assertEquals("/api/v1/ratings", exception.getError().getPath());
        assertEquals("User not found", exception.getError().getError());
        assertNotNull(exception.getError().getTimestamp());
        assertEquals(HttpStatus.NOT_FOUND, exception.getError().getStatus());

        verify(userRepository, times(1)).findByEmail(userEmail);
        verify(specialistRepository, times(0)).findById(specialistId);
        verifyNoInteractions(ratingRepository);
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = "USER")
    void testCreateRating_SpecialistNotFound(){
        Mockito.when(httpServletRequest.getRequestURI()).thenReturn("/api/v1/ratings");
        Mockito.when(authHelpers.getRequest()).thenReturn(httpServletRequest);

        RatingRequest ratingRequest = new RatingRequest();
        ratingRequest.setRating(5);
        ratingRequest.setComment("Great service");

        UserDetails userDetails = User.builder()
                .email("user@example.com")
                .password("password")
                .role(Role.ROLE_USER)
                .build();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails,null));

        String userEmail = "user@example.com";
        User user = User.builder()
                .email(userEmail)
                .password("test")
                .role(Role.ROLE_USER)
                .build();

        Mockito.when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));

        Long specialistId = 1L;
        Mockito.when(specialistRepository.findById(specialistId)).thenReturn(Optional.empty());

        SpecialistNotFoundException exception = assertThrows(SpecialistNotFoundException.class,
                () -> ratingService.createRating(ratingRequest, specialistId));

        assertEquals("/api/v1/ratings", exception.getError().getPath());
        assertEquals("Specialist not found", exception.getError().getError());
        assertNotNull(exception.getError().getTimestamp());
        assertEquals(HttpStatus.NOT_FOUND, exception.getError().getStatus());

        verify(userRepository, times(1)).findByEmail(userEmail);
        verify(specialistRepository, times(1)).findById(specialistId);
        verifyNoInteractions(ratingRepository);
    }


    @Test
    @WithMockUser(username = "user@example.com", roles = "USER")
    void testDeleteRating_Success() {
        Mockito.when(httpServletRequest.getRequestURI()).thenReturn("/api/v1/ratings");
        Mockito.when(authHelpers.getRequest()).thenReturn(httpServletRequest);

        Long ratingId = 1L;
        Rating rating = Rating.builder()
                .build();
        Mockito.when(ratingRepository.findById(ratingId)).thenReturn(Optional.of(rating));

        Mockito.doNothing().when(ratingRepository).delete(rating);

        RatingResponse ratingResponse = ratingService.deleteRating(ratingId);

        assertEquals("/api/v1/ratings", ratingResponse.getPath());
        assertEquals("Rating deleted successfully", ratingResponse.getMessage());
        assertNotNull(ratingResponse.getTimestamp());
        assertEquals(HttpStatus.OK, ratingResponse.getStatus());

        verify(ratingRepository, times(1)).findById(ratingId);
        verify(ratingRepository, times(1)).delete(rating);
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = "USER")
    void testDeleteRating_RatingNotFound() {
        Mockito.when(httpServletRequest.getRequestURI()).thenReturn("/api/v1/ratings");
        Mockito.when(authHelpers.getRequest()).thenReturn(httpServletRequest);

        Long ratingId = 1L;
        Mockito.when(ratingRepository.findById(ratingId)).thenReturn(Optional.empty());

        RatingNotFoundException exception = assertThrows(RatingNotFoundException.class,
                () -> ratingService.deleteRating(ratingId));

        assertEquals("/api/v1/ratings", exception.getError().getPath());
        assertEquals("Rating not found", exception.getError().getError());
        assertNotNull(exception.getError().getTimestamp());
        assertEquals(HttpStatus.NOT_FOUND, exception.getError().getStatus());

        verify(ratingRepository,times(1)).findById(ratingId);
        verifyNoMoreInteractions(ratingRepository);
    }

    @Test
    void testGetAllRatingsForSpecialistPageable() {
        Long specialistId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        List<Rating> ratings = createMockRatingEntities();
        Page<Rating> page = new PageImpl<>(ratings, pageable, ratings.size());
        Mockito.when(ratingRepository.findAllBySpecialistId(specialistId, pageable)).thenReturn(page);

        Page<RatingResponse> ratingResponses = ratingService
                .getAllRatingsForSpecialistPageable(specialistId, pageable);

        assertEquals(3, ratingResponses.getTotalElements());
        List<RatingResponse> responseList = ratingResponses.getContent();
        assertEquals(5,responseList.get(0).getRating());
        assertEquals("Comment1",responseList.get(0).getComment());
        assertNotNull(responseList.get(0).getTimestamp());

        assertEquals(5,responseList.get(1).getRating());
        assertEquals("Comment1",responseList.get(1).getComment());
        assertNotNull(responseList.get(1).getTimestamp());

        assertEquals(5,responseList.get(2).getRating());
        assertEquals("Comment1",responseList.get(2).getComment());
        assertNotNull(responseList.get(2).getTimestamp());
    }

    private List<Rating> createMockRatingEntities() {
        List<Rating> ratings = new ArrayList<>();
        String comment = "Comment1";
        Rating rating = Rating.builder()
                .user(User.builder().build())
                .ratingSize(5)
                .comment(comment)
                .build();
        rating.setCreateDate(LocalDate.now());

        ratings.add(rating);
        Rating rating2 = Rating.builder()
                .user(User.builder().build())
                .ratingSize(5)
                .comment(comment)
                .build();
        rating2.setCreateDate(LocalDate.now());

        ratings.add(rating2);
        Rating rating3 = Rating.builder()
                .user(User.builder().build())
                .ratingSize(5)
                .comment(comment)
                .build();
        rating3.setCreateDate(LocalDate.now());

        ratings.add(rating3);

        return ratings;
    }
}
