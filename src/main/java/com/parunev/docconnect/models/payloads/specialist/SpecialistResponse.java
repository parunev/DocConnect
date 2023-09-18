package com.parunev.docconnect.models.payloads.specialist;

import com.parunev.docconnect.models.payloads.city.CityResponse;
import com.parunev.docconnect.models.payloads.country.CountryResponse;
import com.parunev.docconnect.models.payloads.specialty.SpecialtyResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Specialist Response", description = "Payload for specialist response")
public class SpecialistResponse {

    @Schema(description = "The unique identifier of the specialist.", example = "1")
    private Long id;

    @Schema(description = "The first name of the specialist.", example = "John")
    private String firstName;

    @Schema(description = "The last name of the specialist.", example = "Smith")
    private String lastName;

    @Schema(description = "The phone number of the specialist.", example = "+359 888 888 888")
    private String phoneNumber;

    @Schema(description = "The email of the specialist.", example = "john.smith@email.com")
    private String email;

    @Schema(description = "The summary of the specialist.", example = "Very experienced specialist with 10+ years of experience.")
    private String summary;

    @Schema(description = "The image of the specialist.", example = "https://images.pexels.com/photos/8942523/pexels-photo-8942523.jpeg?auto=compress&cs=tinysrgb&w=600")
    private String imageUrl;

    @Schema(description = "The experience years of the specialist.", example = "10")
    private int experienceYears;

    @Schema(description = "Represents the response data for a specialist city.", example = "cityId: 1, cityName: Sofia")
    private CityResponse city;

    @Schema(description = "Represents the response data for a specialist country.", example = "countryId: 1, countryName: Bulgaria")
    private CountryResponse country;

    @Schema(description = "Represents the response data for a specialist specialty.", example = "specialtyId: 1, specialtyName: Dermatology, imageUrl: https://res.cloudinary.com/...")
    private SpecialtyResponse specialty;

    @Schema(description = "Represents the a list response data for a specialist addresses.", example = "docAddress: first address, docAddress: second address etc.")
    private List<SpecialistAddressResponse> addresses;

    @Schema(description = "Represents the average rating of the specialist.", example = "4.5")
    private Double rating;
}
