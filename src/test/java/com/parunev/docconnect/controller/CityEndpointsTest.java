package com.parunev.docconnect.controller;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Tag("integration")
@Sql({"classpath:db/DbCountryPreparationScript.sql", "classpath:db/DbCityPreparationScript.sql"})
class CityEndpointsTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    void testGetCitiesByCountryId() throws Exception {
        mockMvc.perform(
                        get("/api/v1/cities/country/{id}", 1)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].cityName").value("Sofia"))
                .andExpect(jsonPath("$[0].countryName").value("Bulgaria"))
                .andExpect(jsonPath("$[1].cityName").value("Plovdiv"))
                .andExpect(jsonPath("$[1].countryName").value("Bulgaria"))
                .andExpect(jsonPath("$[2].cityName").value("Varna"))
                .andExpect(jsonPath("$[2].countryName").value("Bulgaria"));
    }

    @Test
    void testGetCitiesByCountryIdWithInvalidId() throws Exception {
        mockMvc.perform(
                        get("/api/v1/cities/{id}", 4)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllCities() throws Exception{
        mockMvc.perform(
                get("/api/v1/cities")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].cityName").value("Sofia"))
                .andExpect(jsonPath("$[0].countryName").value("Bulgaria"))
                .andExpect(jsonPath("$[1].cityName").value("Plovdiv"))
                .andExpect(jsonPath("$[1].countryName").value("Bulgaria"))
                .andExpect(jsonPath("$[2].cityName").value("Varna"))
                .andExpect(jsonPath("$[2].countryName").value("Bulgaria"));
    }
}
